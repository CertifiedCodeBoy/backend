package org.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID taskId;
    private String packageId;
    private Double weightKg;
    private Boolean fragile;
    private String fromLocation;
    private String toLocation;
    private List<String> transferPath;
    private String assignedWorkerId;
    private TaskStatus status;
    private Integer scanStep;
    private LocalDateTime createdAt;
    private LocalDateTime pickedAt;
    private LocalDateTime completedAt;
}