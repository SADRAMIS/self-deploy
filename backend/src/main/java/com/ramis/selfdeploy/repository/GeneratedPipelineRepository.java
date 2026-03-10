package com.ramis.selfdeploy.repository;

import com.ramis.selfdeploy.entity.GeneratedPipeline;
import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.enums.PipelineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GeneratedPipelineRepository extends JpaRepository<GeneratedPipeline, Long> {

    List<GeneratedPipeline> findByProject(Project project);

    List<GeneratedPipeline> findByProjectId(Long projectId);

    List<GeneratedPipeline> findByPipelineType(PipelineType pipelineType);

    List<GeneratedPipeline> findByIsActiveTrue();

    Optional<GeneratedPipeline> findFirstByProjectIdAndIsActiveTrueOrderByGeneratedAtDesc(Long projectId);

    // активные пайплайны, готовые к Kubernetes‑деплою
    @Query("select gp from GeneratedPipeline gp " +
            "where gp.isActive = true and gp.isKubernetesReady = true")
    List<GeneratedPipeline> findActiveKubernetesReady();
}
