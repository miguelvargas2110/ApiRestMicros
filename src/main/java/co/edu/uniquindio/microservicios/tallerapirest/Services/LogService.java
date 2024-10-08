package co.edu.uniquindio.microservicios.tallerapirest.Services;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.LogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final RestTemplate restTemplate;
    private final String logServiceUrl = "http://logs:8002/logs"; // URL del servicio de logs

    @Autowired
    public LogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendLog(String application, String logType, String className, LocalDateTime timestamp, String summary, String description) {
        try {
            // Crea el cuerpo de la petición
            LogRequest logRequest = new LogRequest(application, logType, className, timestamp, summary, description);

            // Envía el log al sistema de logs
            restTemplate.postForEntity(logServiceUrl, logRequest, String.class);

            // También puedes agregar un log local si quieres
            logger.info("Log enviado al sistema de logs: {}", logRequest);
        } catch (Exception e) {
            logger.error("Error al enviar log al sistema de logs: {}", e.getMessage());
        }
    }
}

