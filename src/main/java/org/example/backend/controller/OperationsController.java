package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.request.OrderRequest;
import org.example.backend.dto.response.ApiResponse;
import org.example.backend.dto.response.OrderResponse;
import org.example.backend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OperationsController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('operation:read')")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "taskId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<OrderResponse> orders = orderService.getAllOrders(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }

    @GetMapping("/employee/{assignedWorkerId}")
    @PreAuthorize("hasAuthority('operation:read')")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrdersByAssignedWorkerId(
            @PathVariable String assignedWorkerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "taskId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<OrderResponse> orders = orderService.getOrdersByAssignedWorkerId(assignedWorkerId, page, size, sortBy,
                direction);
        return ResponseEntity.ok(ApiResponse.success(orders, "Employee orders retrieved successfully"));
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('operation:read')")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable UUID taskId) {
        OrderResponse order = orderService.getOrderById(taskId);
        return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('operation:write')")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(order, "Order created successfully"));
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('operation:write')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable UUID taskId,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse order = orderService.updateOrder(taskId, request);
        return ResponseEntity.ok(ApiResponse.success(order, "Order updated successfully"));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('operation:write')")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable UUID taskId) {
        orderService.deleteOrder(taskId);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully"));
    }
}
