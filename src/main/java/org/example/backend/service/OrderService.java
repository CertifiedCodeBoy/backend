package org.example.backend.service;

import org.example.backend.dto.request.OrderRequest;
import org.example.backend.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {

    Page<OrderResponse> getAllOrders(int page, int size, String sortBy, String direction);

    OrderResponse getOrderById(UUID taskId);

    Page<OrderResponse> getOrdersByAssignedWorkerId(String assignedWorkerId, int page, int size, String sortBy,
            String direction);

    OrderResponse createOrder(OrderRequest request);

    OrderResponse updateOrder(UUID taskId, OrderRequest request);

    void deleteOrder(UUID taskId);
}