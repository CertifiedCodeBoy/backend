package org.example.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.request.OrderRequest;
import org.example.backend.dto.response.OrderResponse;
import org.example.backend.entity.OpenPurchaseOrder;
import org.example.backend.enums.TaskStatus;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.OpenPurchaseOrderRepository;
import org.example.backend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OpenPurchaseOrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(int page, int size, String sortBy, String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return orderRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID taskId) {
        OpenPurchaseOrder order = getExistingOrder(taskId);
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByAssignedWorkerId(String assignedWorkerId, int page, int size, String sortBy,
            String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return orderRepository.findByAssignedWorkerId(assignedWorkerId, pageable).map(this::toResponse);
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OpenPurchaseOrder order = OpenPurchaseOrder.builder()
                .packageId(request.getPackageId())
                .weightKg(request.getWeightKg())
                .fragile(request.getFragile())
                .fromLocation(request.getFromLocation())
                .toLocation(request.getToLocation())
                .transferPath(new ArrayList<>(request.getTransferPath()))
                .assignedWorkerId(request.getAssignedWorkerId())
                .status(request.getStatus())
                .scanStep(request.getScanStep())
                .build();

        applyStatusTimestamps(order);

        OpenPurchaseOrder savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrder(UUID taskId, OrderRequest request) {
        OpenPurchaseOrder order = getExistingOrder(taskId);

        order.setPackageId(request.getPackageId());
        order.setWeightKg(request.getWeightKg());
        order.setFragile(request.getFragile());
        order.setFromLocation(request.getFromLocation());
        order.setToLocation(request.getToLocation());
        order.setTransferPath(new ArrayList<>(request.getTransferPath()));
        order.setAssignedWorkerId(request.getAssignedWorkerId());
        order.setStatus(request.getStatus());
        order.setScanStep(request.getScanStep());

        applyStatusTimestamps(order);

        OpenPurchaseOrder updatedOrder = orderRepository.save(order);
        return toResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(UUID taskId) {
        OpenPurchaseOrder order = getExistingOrder(taskId);
        orderRepository.delete(order);
    }

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = (sortBy == null || sortBy.isBlank()) ? "taskId" : sortBy;
        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(sortDirection, sortField));
    }

    private OpenPurchaseOrder getExistingOrder(UUID taskId) {
        return orderRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with taskId: " + taskId));
    }

    private void applyStatusTimestamps(OpenPurchaseOrder order) {
        if (order.getStatus() == TaskStatus.PICKED && order.getPickedAt() == null) {
            order.setPickedAt(LocalDateTime.now());
        }

        if (order.getStatus() == TaskStatus.COMPLETED) {
            if (order.getPickedAt() == null) {
                order.setPickedAt(LocalDateTime.now());
            }
            if (order.getCompletedAt() == null) {
                order.setCompletedAt(LocalDateTime.now());
            }
        }

        if (order.getStatus() == TaskStatus.CREATED) {
            order.setPickedAt(null);
            order.setCompletedAt(null);
        }
    }

    private OrderResponse toResponse(OpenPurchaseOrder order) {
        return OrderResponse.builder()
                .taskId(order.getTaskId())
                .packageId(order.getPackageId())
                .weightKg(order.getWeightKg())
                .fragile(order.getFragile())
                .fromLocation(order.getFromLocation())
                .toLocation(order.getToLocation())
                .transferPath(new ArrayList<>(order.getTransferPath()))
                .assignedWorkerId(order.getAssignedWorkerId())
                .status(order.getStatus())
                .scanStep(order.getScanStep())
                .createdAt(order.getCreatedAt())
                .pickedAt(order.getPickedAt())
                .completedAt(order.getCompletedAt())
                .build();
    }
}