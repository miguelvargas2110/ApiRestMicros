package co.edu.uniquindio.microservicios.tallerapirest.Security;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LivenessCheck implements HealthIndicator {

    private LocalDateTime startTime;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        this.startTime = LocalDateTime.now();  // Inicializa el tiempo de inicio
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("from", startTime.toString())
                .withDetail("status", "ALIVE")
                .build();
    }
}
