package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.AuthenticationResponse;
import co.edu.uniquindio.microservicios.tallerapirest.DTO.LoginDTO;
import co.edu.uniquindio.microservicios.tallerapirest.Security.JwtService;
import co.edu.uniquindio.microservicios.tallerapirest.Services.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {
    JwtService jwtService = new JwtService();

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    @PreAuthorize("permitAll")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDTO authRequest){
        AuthenticationResponse jwtDto = authenticationService.login(authRequest);
        return ResponseEntity.ok(jwtDto);
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
