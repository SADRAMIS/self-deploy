package com.ramis.selfdeploy.entity;

import com.ramis.selfdeploy.enums.ExecutionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "pipeline_executions", indexes = {
        @Index(name = "idx_pipeline_id", columnList = "pipeline_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_start_time", columnList = "start_time")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PipelineExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private GeneratedPipeline pipeline;

    @Column(nullable = false, unique = true, length = 100)
    private String executionId; // GitHub/GitLab build ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status; // RUNNING, SUCCESS, FAILED, CANCELED

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer duration; // в секундах

    @Column(columnDefinition = "text")
    private String logs; // полные логи

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metrics; // build time, artifact size, coverage

    private String failureReason;

    private Integer retryCount = 0;

    @Column(length = 100)
    private String triggeredBy; // commit hash, manual, schedule
}
