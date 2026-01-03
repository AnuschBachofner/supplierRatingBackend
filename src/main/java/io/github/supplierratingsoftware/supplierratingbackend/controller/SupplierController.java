package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierDto;
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
     * @return ResponseEntity containing a list of SupplierDto objects.
     */
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    /**
     * Creates a new supplier.
     * Validates the input DTO and ensures the necessary properties are set.
     *
     * @param creationDto The payload containing the new supplier's details.
     * @return The created {@link SupplierDto} wrapped in a ResponseEntity with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody @Valid SupplierCreationDto creationDto) {
        SupplierDto createdSupplier = supplierService.createSupplier(creationDto);
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
    public ResponseEntity<SupplierDto> updateSupplier(
            @PathVariable String id,
            @RequestBody @Valid SupplierUpdateDto updateDto) {

        SupplierDto updatedSupplier = supplierService.updateSupplier(id, updateDto);
        return ResponseEntity.ok(updatedSupplier);
    }

}
