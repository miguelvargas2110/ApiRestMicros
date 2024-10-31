package co.edu.uniquindio.microservicios.tallerapirest.Security;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private LocalDateTime startTime;
    private static final String VERSION = "1.0.0";

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        this.startTime = LocalDateTime.now();
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("version", VERSION)
                .withDetail("uptime", getUptime())
                .withDetail("status", "UP")
                .build();
    }

    private String getUptime() {
        return "Desde: " + startTime.toString();
    }
}
