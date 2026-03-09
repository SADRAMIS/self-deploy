package com.ramis.selfdeploy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "repository_analysis", indexes = {
        @Index(name = "idx_project_id_analysis", columnList = "project_id"),
        @Index(name = "idx_analysis_date", columnList = "analysis_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> projectStructure; // Структура файлов как JSON

    @Column(columnDefinition = "text[]")
    private String[] detectedTechnologies;

    @Column(columnDefinition = "text[]")
    private String[] buildDependencies;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> analysisMetrics;

    @Column(length = 50)
    private String buildToolVersion;

    @Column(length = 10)
    private String javaVersion;

    private Integer codeLines;

    private Integer testCoverage;

    @CreationTimestamp
    private LocalDateTime analysisDate;

    @Column(length = 20)
    private String analysisStatus; // SUCCESS, FAILED, PARTIAL

    private String analysisError; // Если FAILED
}
