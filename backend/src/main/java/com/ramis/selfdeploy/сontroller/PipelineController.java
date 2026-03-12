package com.ramis.selfdeploy.сontroller;

import com.ramis.selfdeploy.entity.GeneratedPipeline;
import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.repository.GeneratedPipelineRepository;
import com.ramis.selfdeploy.repository.ProjectRepository;
import com.ramis.selfdeploy.service.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pipelines")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineService pipelineService;
    private final ProjectRepository projectRepository;
    private final GeneratedPipelineRepository pipelineRepository;

    // ✅ 1. Сгенерировать пайплайн для проекта
    @PostMapping("/{projectId}/generate")
    public ResponseEntity<GeneratedPipeline> generatePipeline(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        GeneratedPipeline pipeline = pipelineService.generatePipeline(project);
        return ResponseEntity.ok(pipeline);
    }

    // ✅ 2. Получить пайплайны проекта
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<GeneratedPipeline>> getProjectPipelines(@PathVariable Long projectId) {
        List<GeneratedPipeline> pipelines = pipelineRepository.findByProjectId(projectId);
        return ResponseEntity.ok(pipelines);
    }

    // ✅ 3. Получить пайплайн по ID
    @GetMapping("/{id}")
    public ResponseEntity<GeneratedPipeline> getPipeline(@PathVariable Long id) {
        return pipelineRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
