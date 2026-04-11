package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

/**
 * Dieses Objekt wird an das Frontend zurückgeschickt.
 * Es enthält die "Eintrittskarte" (Session-ID in Tokenform) für openBIS.
 */
public record LoginResponseDto(
        String token,
        String username
) {}