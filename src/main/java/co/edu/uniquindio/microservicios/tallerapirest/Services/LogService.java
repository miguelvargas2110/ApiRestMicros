package co.edu.uniquindio.microservicios.tallerapirest.Services;

import co.edu.uniquindio.microservicios.tallerapirest.Config.RabbitMQConfig;
import co.edu.uniquindio.microservicios.tallerapirest.DTO.LogRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public LogService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendLog(String application, String logType, String className, LocalDateTime timestamp, String summary, String description) {
        try {
            LogRequest logRequest = new LogRequest(application, logType, className, timestamp, summary, description);

            // Convierte el objeto a JSON
            String logRequestJson = objectMapper.writeValueAsString(logRequest);

            // Envía el JSON a la cola de RabbitMQ
            String logQueueName = RabbitMQConfig.QUEUE_NAME;
            rabbitTemplate.convertAndSend(logQueueName, logRequestJson);

            logger.info("Log enviado a RabbitMQ: {}", logRequestJson);
        } catch (JsonProcessingException e) {
            logger.error("Error al convertir log a JSON: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error al enviar log a RabbitMQ: {}", e.getMessage());
        }
    }
}
