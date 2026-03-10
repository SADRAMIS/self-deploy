package com.ramis.selfdeploy.repository;

import com.ramis.selfdeploy.entity.AIRecommendation;
import com.ramis.selfdeploy.entity.Project;
import com.ramis.selfdeploy.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIRecommendationRepository extends JpaRepository<AIRecommendation, Long> {

    List<AIRecommendation> findByProject(Project project);

    List<AIRecommendation> findByProjectId(Long projectId);

    List<AIRecommendation> findByPriorityOrderByCreatedAtDesc(Priority priority);

    List<AIRecommendation> findByProjectIdAndIsImplementedFalseOrderByPriorityDesc(Long projectId);
}
