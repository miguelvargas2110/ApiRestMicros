package co.edu.uniquindio.microservicios.tallerapirest.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record SignUpDTO(
        String username,
        String password,
        String email
) {
}
