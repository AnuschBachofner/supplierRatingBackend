package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierReadDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierUpdateDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing supplier resources.
 * Provides endpoints to retrieve supplier information.
 *
 * <p><strong>Base URL:</strong> {@code /api/v1/suppliers}</p>
 */
@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /**
     * Retrieves a list of all registered suppliers.
     *
     * @return ResponseEntity containing a list of SupplierReadDto objects.
     */
    @GetMapping
    public ResponseEntity<List<SupplierReadDto>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    /**
     * Retrieves a supplier by its openBIS PermID.
     *
     * @param id The PermID of the supplier to retrieve.
     * @return ResponseEntity containing the detailed supplier information.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierReadDto> getSupplierById(@PathVariable String id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    /**
     * Creates a new supplier.
     * Validates the input DTO and ensures the necessary properties are set.
     *
     * @param creationDto The payload containing the new supplier's details.
     * @return The created {@link SupplierReadDto} wrapped in a ResponseEntity with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<SupplierReadDto> createSupplier(@RequestBody @Valid SupplierCreationDto creationDto) {
        SupplierReadDto createdSupplier = supplierService.createSupplier(creationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdSupplier);
    }

    /**
     * Updates an existing supplier.
     *
     * @param id The openBIS PermID of the supplier.
     * @param updateDto The update payload.
     * @return The updated supplier details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierReadDto> updateSupplier(
            @PathVariable String id,
            @RequestBody @Valid SupplierUpdateDto updateDto) {

        SupplierReadDto updatedSupplier = supplierService.updateSupplier(id, updateDto);
        return ResponseEntity.ok(updatedSupplier);
    }

}
