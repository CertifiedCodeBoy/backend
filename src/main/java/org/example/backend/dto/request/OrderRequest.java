package org.example.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.enums.TaskStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Package ID is required")
    private String packageId;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
    private Double weightKg;

    @NotNull(message = "Fragile flag is required")
    private Boolean fragile;

    @NotBlank(message = "From location is required")
    private String fromLocation;

    @NotBlank(message = "To location is required")
    private String toLocation;

    @NotNull(message = "Transfer path is required")
    private List<String> transferPath;

    @NotBlank(message = "Assigned worker ID is required")
    private String assignedWorkerId;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Scan step is required")
    private Integer scanStep;
}