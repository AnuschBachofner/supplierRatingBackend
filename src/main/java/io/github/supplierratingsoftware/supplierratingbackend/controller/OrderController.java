package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Optionally filters for orders belonging to a specific supplier.
     *
     * @param supplierId (Optional) The suppliers PermID to filter for, or null to retrieve all orders.
     * @return ResponseEntity containing a list of OrderDto objects representing all orders.
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam(required = false) String supplierId) {
        return ResponseEntity.ok(orderService.getAllOrders(supplierId));
    }

    /**
     * Creates a new order.
     *
     * @param creationDto The payload containing the new order details.
     * @return The created {@link OrderDto} wrapped in a ResponseEntity with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderCreationDto creationDto) {
        OrderDto createdOrder = orderService.createOrder(creationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }
}
