package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.SupplierDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return A list of {@link SupplierDto} objects.
     */
    @GetMapping
    public List<SupplierDto> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

}
