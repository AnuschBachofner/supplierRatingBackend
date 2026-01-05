package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.OrderUpdateDto;
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
     * @return ResponseEntity containing a list of OrderReadDto objects representing all orders.
     */
    @GetMapping
    public ResponseEntity<List<OrderReadDto>> getAllOrders(@RequestParam(required = false) String supplierId) {
        return ResponseEntity.ok(orderService.getAllOrders(supplierId));
    }

    /**
     * Retrieves an order by its OpenBIS PermID.
     *
     * @param id The PermID of the order to retrieve.
     * @return ResponseEntity containing the OrderReadDto of the retrieved order.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderReadDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Creates a new order.
     *
     * @param creationDto The payload containing the new order details.
     * @return The created {@link OrderReadDto} wrapped in a ResponseEntity with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<OrderReadDto> createOrder(@RequestBody @Valid OrderCreationDto creationDto) {
        OrderReadDto createdOrder = orderService.createOrder(creationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    /**
     * Updates an existing order.
     *
     * @param id        The PermID of the order to update.
     * @param updateDto The payload containing the updated order details.
     * @return The updated {@link OrderReadDto}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderReadDto> updateOrder(
            @PathVariable String id,
            @RequestBody @Valid OrderUpdateDto updateDto) {

        OrderReadDto updatedOrder = orderService.updateOrder(id, updateDto);
        return ResponseEntity.ok(updatedOrder);
    }
}
