package co.edu.uniquindio.microservicios.tallerapirest.DTO;

import lombok.NonNull;

public record PasswordDTO(
        @NonNull String username,
        @NonNull String password
) {
}
