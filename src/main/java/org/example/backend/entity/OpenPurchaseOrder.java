package org.example.backend.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "open_purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenPurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    @Column(nullable = false, length = 100)
    private String packageId;

    @Column(nullable = false)
    private Double weightKg;

    @Column(nullable = false)
    private Boolean fragile;

    @Column(nullable = false, length = 150)
    private String fromLocation;

    @Column(nullable = false, length = 150)
    private String toLocation;

    @ElementCollection
    @CollectionTable(name = "open_purchase_order_transfer_path", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "path_location", nullable = false, length = 150)
    @Builder.Default
    private List<String> transferPath = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String assignedWorkerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TaskStatus status = TaskStatus.CREATED;

    @Column(nullable = false)
    @Builder.Default
    private Integer scanStep = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime pickedAt;

    @Column
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == TaskStatus.PICKED && pickedAt == null) {
            pickedAt = LocalDateTime.now();
        }
        if (status == TaskStatus.COMPLETED) {
            if (pickedAt == null) {
                pickedAt = LocalDateTime.now();
            }
            if (completedAt == null) {
                completedAt = LocalDateTime.now();
            }
        }
    }
}
