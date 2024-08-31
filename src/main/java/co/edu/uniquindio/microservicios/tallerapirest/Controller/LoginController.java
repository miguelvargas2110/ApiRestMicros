package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.LoginDTO;
import co.edu.uniquindio.microservicios.tallerapirest.Security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {
    JwtService jwtService = new JwtService();

    @PostMapping("/login")
    public ResponseEntity<?> Login (@RequestBody LoginDTO login) {
        if(!login.username().isEmpty() && !login.password().isEmpty()) {
            String token = jwtService.getToken(login.username());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/saludo")
    public ResponseEntity<String> saludo(@RequestParam(value = "nombre", required = false) String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            String errorHtml = "<html><body><h2>Solicitud no válida: El nombre es obligatorio</h2></body></html>";
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorHtml);
        }

        String saludoHtml = "<html><body><h2>Hola " + nombre + "</h2></body></html>";
        return ResponseEntity.ok(saludoHtml);
    }


}
