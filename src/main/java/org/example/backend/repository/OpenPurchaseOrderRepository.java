package org.example.backend.repository;

import org.example.backend.entity.OpenPurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OpenPurchaseOrderRepository extends JpaRepository<OpenPurchaseOrder, UUID> {

    Page<OpenPurchaseOrder> findByAssignedWorkerId(String assignedWorkerId, Pageable pageable);
}
