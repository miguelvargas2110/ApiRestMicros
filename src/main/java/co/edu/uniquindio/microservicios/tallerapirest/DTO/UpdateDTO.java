package co.edu.uniquindio.microservicios.tallerapirest.DTO;

import lombok.*;

public record UpdateDTO(
        String email,
        String password,
        @NonNull
        String username
) {
}
