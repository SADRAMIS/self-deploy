package com.ramis.selfdeploy.entity;

import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_recommendations", indexes = {
        @Index(name = "idx_project_id_rec", columnList = "project_id"),
        @Index(name = "idx_priority", columnList = "priority")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 100)
    private String recommendationType; // OPTIMIZATION, SECURITY ...

    @Column(columnDefinition = "text")
    private String recommendation;

    @Column(columnDefinition = "text")
    private String implementation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority; // LOW, MEDIUM, HIGH, CRITICAL

    private Integer confidenceScore; // 0-100

    private Boolean isImplemented = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 50)
    private String recommendedTool; // SonarQube, OWASP, K8s, etc.
}
