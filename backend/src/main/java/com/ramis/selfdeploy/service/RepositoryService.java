package com.ramis.selfdeploy.service;

import com.ramis.selfdeploy.entity.*;
import com.ramis.selfdeploy.enums.PipelineType;
import com.ramis.selfdeploy.enums.ProjectType;
import com.ramis.selfdeploy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RepositoryService {

    private final ProjectRepository projectRepository;
    private final RepositoryAnalysisRepository analysisRepository;

    public Project analyzeRepository(String repoUrl, String gitHubToken) {
        // Клонируем репозиторий
        File tempDir = new File(System.getProperty("java.io.tmpdir") + "/git-" + System.currentTimeMillis());
        try (Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(tempDir)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("", gitHubToken))
                .call()) {

            log.info("✅ Cloned: {} → {}", repoUrl, tempDir.getAbsolutePath());

            Project project = createProjectFromGit(tempDir);  // ← Добавлен
            RepositoryAnalysis analysis = analyzeProjectStructure(project, tempDir);

            projectRepository.save(project);
            analysisRepository.save(analysis);

            log.info("💾 Saved project: {}", project.getRepositoryName());
            return project;
        } catch (GitAPIException e) {
            log.error("❌ Failed to clone: {}", repoUrl, e);
            throw new RuntimeException("Repository analysis failed: " + repoUrl, e);
        } finally {
            deleteTempDir(tempDir);
        }
    }

    private Project createProjectFromGit(File repoDir) {
        return Project.builder()
                .repositoryUrl(repoDir.getAbsolutePath())
                .repositoryName(repoDir.getName())
                .projectType(detectProjectType(repoDir))
                .pipelineType(PipelineType.GITHUB_ACTIONS)
                .isActive(true)
                .build();
    }

    private ProjectType detectProjectType(File repoDir) {
        if (new File(repoDir, "pom.xml").exists()) return ProjectType.MAVEN;
        if (new File(repoDir, "build.gradle").exists()) return ProjectType.GRADLE;
        if (new File(repoDir, "package.json").exists()) return ProjectType.NPM;
        if (new File(repoDir, "Dockerfile").exists()) return ProjectType.DOCKER;
        return ProjectType.MAVEN;  // default
    }

    private RepositoryAnalysis analyzeProjectStructure(Project project, File repoDir) {
        String[] technologies = Arrays.stream(repoDir.listFiles((dir, name) ->
                        name.matches(".*\\.(xml|gradle|json|yml|yaml|dockerfile|dockerfile).*")))
                .map(File::getName)
                .map(this::extractTechnology)
                .filter(t -> !t.isEmpty())
                .distinct()
                .toArray(String[]::new);

        return RepositoryAnalysis.builder()
                .project(project)
                .detectedTechnologies(technologies)
                .analysisStatus("SUCCESS")
                .build();
    }

    private String extractTechnology(String filename) {
        if (filename.contains("pom.xml")) return "Maven";
        if (filename.contains("build.gradle")) return "Gradle";
        if (filename.contains("package.json")) return "NPM";
        if (filename.contains("docker")) return "Docker";
        if (filename.contains("docker-compose")) return "Docker Compose";
        return "";
    }

    private void deleteTempDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteTempDir(file);  // Рекурсивно
                    } else {
                        file.delete();  // Удаляем файлы
                    }
                }
            }
        }
        dir.delete();
    }
}
