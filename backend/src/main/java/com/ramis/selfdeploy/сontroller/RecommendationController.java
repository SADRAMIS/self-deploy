package com.ramis.selfdeploy.сontroller;

import com.ramis.selfdeploy.entity.AIRecommendation;
import com.ramis.selfdeploy.repository.AIRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final AIRecommendationRepository recommendationRepository;

    // ✅ 1. Рекомендации для проекта
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<AIRecommendation>> getProjectRecommendations(@PathVariable Long projectId) {
        List<AIRecommendation> recommendations = recommendationRepository
                .findByProjectIdAndIsImplementedFalseOrderByPriorityDesc(projectId);
        return ResponseEntity.ok(recommendations);
    }

    // ✅ 2. Отметить рекомендацию как выполненную
    @PostMapping("/{recommendationId}/implemented")
    public ResponseEntity<AIRecommendation> markAsImplemented(@PathVariable Long recommendationId) {
        return recommendationRepository.findById(recommendationId)
                .map(rec -> {
                    rec.setImplemented(true);
                    return ResponseEntity.ok(recommendationRepository.save(rec));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
