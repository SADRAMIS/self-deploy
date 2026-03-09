package com.ramis.selfdeploy.entity;

import com.ramis.selfdeploy.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_repo_url", columnList = "repository_url"),
        @Index(name = "idx_project_type", columnList = "project_type"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 500)
    private String repositoryUrl;

    @Column(nullable = false, length = 255)
    private String repositoryName;

    @Column(nullable = false, length = 1000)
    private String gitHubToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType; // MAVEN, GRADLE, NPM, DOCKER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PipelineType pipelineType; // GITHUB_ACTIONS, GITLAB_CI, JENKINS

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepositoryAnalysis> analyses;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GeneratedPipeline> pipelines;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AIRecommendation> recommendations;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String branch = "main";

    @Column(length = 50)

    private String language;

    private Integer buildTime; // в секундах

    @Column(nullable = false)
    private Boolean isActive = true;

    private String description;

    @Column(length = 50)
    private String kubernetesNamespace; // для K8s деплоя

    private Boolean enableKubernetesDeploy = false;

}
