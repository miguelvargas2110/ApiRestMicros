package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final HealthIndicator readinessCheck;
    private final HealthIndicator livenessCheck;

    @GetMapping()
    public ResponseEntity<?> healthCheck() {

        Map<String, Object> healthDetails = new HashMap<>();

        // Obtenemos los detalles de readiness y liveness
        var readiness = readinessCheck.health();
        var liveness = livenessCheck.health();

        // Construimos el JSON combinando ambos estados
        healthDetails.put("status", "UP");
        healthDetails.put("checks", new HashMap<String, Object>() {{
            put("readiness", readiness.getDetails());
            put("liveness", liveness.getDetails());
        }});

        return ResponseEntity.ok(healthDetails);
    }

}
