package com.ramis.selfdeploy.service;

import com.ramis.selfdeploy.entity.GeneratedPipeline;
import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.enums.PipelineType;
import com.ramis.selfdeploy.repository.GeneratedPipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final GeneratedPipelineRepository pipelineRepository;

    public GeneratedPipeline generatePipeline(Project project) {
        String yaml = generateYaml(project);
        GeneratedPipeline pipeline = GeneratedPipeline.builder()
                .project(project)
                .pipelineType(PipelineType.GITHUB_ACTIONS)
                .pipelineYaml(yaml)
                .stages(new String[]{"build", "test", "deploy"})
                .isKubernetesReady(true)
                .build();
        return pipelineRepository.save(pipeline);
    }

    private String generateYaml(Project project) {
        return switch (project.getProjectType()) {
            case MAVEN -> """
                name: Maven CI/CD
                on: [push]
                jobs:
                  build:
                    runs-on: ubuntu-latest
                    steps:
                    - uses: actions/checkout@v3
                    - uses: actions/setup-java@v3
                      with: { java-version: '17' }
                    - run: mvn clean package
                """;
            default -> """
                name: Default CI/CD
                on: [push]
                jobs:
                  test:
                    runs-on: ubuntu-latest
                    steps:
                    - uses: actions/checkout@v3
                """;
        };
    }
}
