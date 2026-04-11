package io.github.supplierratingsoftware.supplierratingbackend.dto.api;

import jakarta.validation.constraints.NotBlank;

/**
 * Dieses Objekt nimmt die Daten vom Login-Formular entgegen.
 * Ein Record ist eine spezielle Klasse. Er erstellt automatisch alles, was man normalerweise mühsam tippen müsste (wie Konstruktoren oder Getter-Methoden). Er ist perfekt für den Datentransport geeignet.
 * (AT)NotBlank: Das ist eine Sicherheitsprüfung. Wenn jemand versucht, sich ohne Benutzernamen anzumelden, stoppt Spring Boot die Anfrage sofort und schickt eine Fehlermeldung zurück.
 */
public record LoginRequestDto(
        @NotBlank(message = "Benutzername darf nicht leer sein")
        String username,

        @NotBlank(message = "Bitte geben Sie Ihren Personal Access Token ein")
        String password  // Dies wird im Frontend als PAT eingegeben
) {}