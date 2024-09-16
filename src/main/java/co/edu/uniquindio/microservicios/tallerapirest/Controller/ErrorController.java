package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    //@RequestMapping("/**")
    public ResponseEntity<String> handle404() {
        String notFoundHtml = "<html><body><h2>Recurso no encontrado</h2></body></html>";
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(notFoundHtml);
    }
}