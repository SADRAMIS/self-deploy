package com.ramis.selfdeploy.repository;

import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.enums.PipelineType;
import com.ramis.selfdeploy.enums.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByRepositoryUrl(String repositoryUrl);

    List<Project> findByIsActiveTrue();

    List<Project> findByProjectType(ProjectType projectType);

    List<Project> findByPipelineType(PipelineType pipelineType);

    List<Project> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("select p from Project p " +
            "where p.isActive = true and p.language = :language")
    List<Project> findActiveByLanguage(String language);

    @Query("select p from Project p " +
            "where p.isActive = true and p.projectType = :projectType " +
            "and p.pipelineType = :pipelineType")
    List<Project> findActiveByTypes(ProjectType projectType, PipelineType pipelineType);
}
