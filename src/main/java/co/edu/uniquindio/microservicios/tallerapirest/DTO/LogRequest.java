package co.edu.uniquindio.microservicios.tallerapirest.DTO;

import java.time.LocalDateTime;

public record LogRequest(
        String application,
        String logType,
        String className,
        LocalDateTime timestamp,
        String summary,
        String description
) {
}
