package com.ramis.selfdeploy.repository;

import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.entity.RepositoryAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositoryAnalysisRepository extends JpaRepository<RepositoryAnalysis, Long> {

    List<RepositoryAnalysis> findByProject(Project project);

    List<RepositoryAnalysis> findByProjectId(Long projectId);

    List<RepositoryAnalysis> findByAnalysisStatus(String analysisStatus);

    List<RepositoryAnalysis> findByAnalysisDateAfter(LocalDateTime analysisDate);

    // поиск по PostgreSQL ARRAY detected_technologies
    @Query(value = """
            select ra.* from repository_analysis ra
            where :tech = ANY(ra.detected_technologies)
            """, nativeQuery = true)
    List<RepositoryAnalysis> findByDetectedTechnology(String tech);
}
