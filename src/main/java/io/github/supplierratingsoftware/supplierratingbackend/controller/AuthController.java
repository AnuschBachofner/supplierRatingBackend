package io.github.supplierratingsoftware.supplierratingbackend.controller;

// Das sind die "Werkzeuge", die wir brauchen
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.LoginRequestDto;
import io.github.supplierratingsoftware.supplierratingbackend.dto.api.LoginResponseDto;
import io.github.supplierratingsoftware.supplierratingbackend.integration.openbis.OpenBisClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * Diese Methode empfängt die Login-Daten vom Frontend.
     * Aufruf via: POST http://localhost:8080/api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest) {

        // 1. Wir rufen die Methode auf, die du gerade im OpenBisClient erstellt hast.
        // Wir übergeben den Namen und das Passwort aus der Anfrage.
        String sessionToken = openBisClient.authenticate(
                loginRequest.username(),
                loginRequest.password()
        );

        // 2. Wenn authenticate() keinen Fehler geworfen hat, war der Login erfolgreich.
        // Wir packen den Token in unser Antwort-Gefäß (LoginResponseDto).
        LoginResponseDto response = new LoginResponseDto(sessionToken, loginRequest.username());

        // 3. Wir schicken die Antwort mit dem HTTP-Status 200 (OK) zurück ans Frontend.
        return ResponseEntity.ok(response);
    }
}