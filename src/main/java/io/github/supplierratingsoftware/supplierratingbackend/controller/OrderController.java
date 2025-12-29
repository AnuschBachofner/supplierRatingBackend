package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing order resources.
 * Provides endpoints to retrieve order information.
 *
 * <p><strong>Base URL:</strong> {@code /api/v1/orders}</p>
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * Retrieves a list of all orders from OpenBIS.
     *
     * @return List of OrderDto objects representing all orders.
     */
    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}
