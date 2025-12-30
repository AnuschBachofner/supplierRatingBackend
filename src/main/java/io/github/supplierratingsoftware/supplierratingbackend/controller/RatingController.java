package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing supplier ratings.
 * Provides endpoints to retrieve rating information.
 *
 * <p><strong>Base URL:</strong> {@code /api/v1/ratings}</p>
 */
@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    /**
     * Retrieves a single rating by its OpenBIS PermID.
     *
     * @param id The OpenBIS PermID of the rating to retrieve.
     * @return The rating if found, or a 404 Not Found response if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingDto> getRatingById(@PathVariable String id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}