package com.ramis.selfdeploy.сontroller;

import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.repository.ProjectRepository;
import com.ramis.selfdeploy.service.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final RepositoryService repositoryService;
    private final ProjectRepository projectRepository;

    // ✅ 1. Анализ + создание проекта
    @PostMapping
    public ResponseEntity<Project> analyzeRepository(
            @RequestParam String repositoryUrl,
            @RequestParam String gitHubToken) {
        Project project = repositoryService.analyzeRepository(repositoryUrl, gitHubToken);
        return ResponseEntity.status(201).body(project);
    }

    // ✅ 2. Список активных проектов
    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectRepository.findByIsActiveTrue();
        return ResponseEntity.ok(projects);
    }

    // ✅ 3. Получить проект по ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 4. Обновить проект
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setRepositoryName(updatedProject.getRepositoryName());
                    project.setIsActive(updatedProject.getIsActive());
                    return ResponseEntity.ok(projectRepository.save(project));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 5. Удалить проект
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
