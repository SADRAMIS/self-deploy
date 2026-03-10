package com.ramis.selfdeploy.repository;

import com.ramis.selfdeploy.entity.GeneratedPipeline;
import com.ramis.selfdeploy.entity.PipelineExecution;
import com.ramis.selfdeploy.enums.ExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PipelineExecutionRepository extends JpaRepository<PipelineExecution, Long> {

    List<PipelineExecution> findByPipeline(GeneratedPipeline pipeline);

    List<PipelineExecution> findByPipelineId(Long pipelineId);

    Optional<PipelineExecution> findByExecutionId(String executionId);

    List<PipelineExecution> findByStatus(ExecutionStatus status);

    List<PipelineExecution> findByStartTimeAfter(LocalDateTime startTime);

    List<PipelineExecution> findByPipelineIdAndStatusOrderByStartTimeDesc(Long pipelineId, ExecutionStatus status);
}
