package io.github.supplierratingsoftware.supplierratingbackend.controller;

// Das sind die "Werkzeuge", die wir brauchen
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.LoginRequestDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.LoginResponseDto;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Dieser Controller ist die Anlaufstelle für alles, was mit Anmeldung zu tun hat.
 */
@RestController // Sagt Spring: "Ich bin eine Schnittstelle für das Internet"
@RequestMapping("/api/v1/auth") // Die Basis-URL ist http://localhost:8080/api/v1/auth
@RequiredArgsConstructor // Erstellt automatisch den Konstruktor für den OpenBisClient
public class AuthController {

    // Wir brauchen unseren "openBIS-Dolmetscher"
    private final OpenBisClient openBisClient;

    /**
     * Validiert den PAT des Users gegen openBIS.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest) {
        // Wir nehmen den PAT aus dem password-Feld des Requests
        String userPat = loginRequest.password();

        // 1. PAT bei openBIS prüfen und userId abrufen
        String userIdFromOpenBis = openBisClient.validatePat(userPat);

        // 2. Sicherheits-Check: Stimmt der eingegebene Name mit dem PAT-Inhaber überein?
        if (!userIdFromOpenBis.equalsIgnoreCase(loginRequest.username())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Wenn alles okay ist, schicken wir den PAT als Token zurück
        return ResponseEntity.ok(new LoginResponseDto(userPat, userIdFromOpenBis));
    }
}