package io.github.supplierratingsoftware.supplierratingbackend.controller;

import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingCreationDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.RatingDto;
import io.github.supplierratingsoftware.supplierratingbackend.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing supplier ratings.
 * Provides endpoints to retrieve or create ratings.
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

    /**
     * Creates a new rating for a specific order.
     * Calculates the total score automatically based on the provided criteria.
     *
     * @param creationDto The rating data.
     * @return The created RatingDto.
     */
    @PostMapping
    public ResponseEntity<RatingDto> createRating(@RequestBody @Valid RatingCreationDto creationDto) {
        RatingDto createdRating = ratingService.createRating(creationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRating);
    }
}