package co.edu.uniquindio.microservicios.tallerapirest.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginDTO {
    private String username;
    private String password;

}