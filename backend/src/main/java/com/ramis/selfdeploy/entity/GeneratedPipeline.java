package com.ramis.selfdeploy.entity;

import com.ramis.selfdeploy.enums.PipelineType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "generated_pipelines", indexes = {
        @Index(name = "idx_project_id_pipeline", columnList = "project_id"),
        @Index(name = "idx_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedPipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PipelineType pipelineType;

    @Column(columnDefinition = "text")
    private String pipelineYaml; // YAML конфиг

    @Column(columnDefinition = "text")
    private String[] stages;  // build, test, deploy, release


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> environmentVariables;

    @Column(length = 255)
    private String dockerfilePath;

    @Column(length = 100)
    private String deploymentTarget; // AWS, Azure, Kubernetes, Heroku

    private Boolean hasTestStage = true;
    private Boolean hasSonarAnalysis = false;
    private Boolean hasDockerBuild = false;
    private Boolean hasSecurityScan = false;

    private Integer estimatedBuildTime; // в минутах

    @CreationTimestamp
    private LocalDateTime generatedAt;

    @Column(length = 50)
    private String generatedBy; // SYSTEM, USER, AI

    @Column(length = 40)
    private String gitCommitHash;

    private Boolean isActive = true;

    @OneToMany(mappedBy = "pipeline", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PipelineExecution> executions;

    @Column(columnDefinition = "text")
    private String kubernetesManifest; // K8s deployment YAML

    private Boolean isKubernetesReady = false;
}
