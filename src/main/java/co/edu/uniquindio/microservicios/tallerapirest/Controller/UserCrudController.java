package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.*;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.ApiError;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.ApiSuccess;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Security.JwtService;
import co.edu.uniquindio.microservicios.tallerapirest.Services.AuthenticationServiceImpl;
import co.edu.uniquindio.microservicios.tallerapirest.Services.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping
public class UserCrudController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    //--------------------------------------------------------------------------------

    @PostMapping("/login")
    @PreAuthorize("permitAll")
    public ResponseEntity<?> login(@RequestBody LoginDTO authRequest, HttpServletRequest request) {
        try {
            AuthenticationResponse jwtDto = authenticationService.login(authRequest);
            return ResponseEntity.ok(jwtDto);
        } catch (BadCredentialsException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Usuario o contraseña no validos",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
        } catch (InternalAuthenticationServiceException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Usuario o contraseña no validos",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Ocurrió un error interno",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);        }
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

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam(value = "username", required = false) String username, HttpServletRequest request) {
        try {
            Optional<User> userOptional = userServiceImpl.searchByUserName(username);
            if (userOptional.isPresent()) {
                userServiceImpl.delete(userOptional.get());
                ApiSuccess apiSuccess = new ApiSuccess(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        "Usuario eliminado",
                        request.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
            } else {
                ApiError apiError = new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "El usuario a eliminar no fue encontrado",
                        request.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
            }
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Ocurrió un error interno",
                    request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) throws Exception {
        try {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            userServiceImpl.save(user);
            ApiSuccess apiSuccess = new ApiSuccess(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Usuario creado",
                    "/signup"
            );
            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Error al crear el usuario",
                    "/signup"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@ModelAttribute UpdateDTO updateDTO) {
        try {
            User user = userServiceImpl.searchByUserName(updateDTO.username()).get();
            if ((updateDTO.email() != null && !updateDTO.email().isEmpty()) || (updateDTO.password() != null && !updateDTO.password().isEmpty())) {
                user.setEmail(updateDTO.email());
                user.setPassword(passwordEncoder.encode(updateDTO.password()));
            }else {
                ApiError apiError = new ApiError(
                        HttpStatus.NO_CONTENT.value(),
                        HttpStatus.NO_CONTENT.getReasonPhrase(),
                        "Ambos datos a actualizar son nulos",
                        "/signup"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
            }
            userServiceImpl.update(user);
            ApiSuccess apiSuccess = new ApiSuccess(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Usuario actualizado",
                    "/updateUser"
            );
            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        }catch (NullPointerException e){
            ApiError apiError = new ApiError(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    "Ambos datos a actualizar son nulos",
                    "/signup"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Error al crear actualizar el usuario",
                    "/signup"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }

    @PostMapping("/generateChangePasswordToken")
    public ResponseEntity<?> generateChangePasswordToken(@RequestParam(value = "username", required = false) String username) {
        try {
            User user = userServiceImpl.searchByUserName(username).get();
            String token = jwtService.getChangePasswordToken(user);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (NoSuchElementException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Usuario no encontrado.",
                    "/generateChangePasswordToken"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Error al generar el token.",
                    "/generateChangePasswordToken"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@ModelAttribute PasswordDTO passwordDTO) {
        try {
            User user = userServiceImpl.searchByUserName(passwordDTO.username()).get();
            user.setPassword(passwordEncoder.encode(passwordDTO.password()));
            userServiceImpl.update(user);
            ApiSuccess apiSuccess = new ApiSuccess(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Contraseña actualizada",
                    "/changePassword"
            );
            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Error al cambiar la contraseña.",
                    "/changePassword"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> obtenerEntidadesPaginadas(@RequestParam(value = "numeroPagina", required = false) int numeroPagina, @RequestParam(value = "tamanoPagina", required = false) int tamanoPagina) {
        try {
            return ResponseEntity.ok(userServiceImpl.obtenerEntidadesPaginadas(numeroPagina, tamanoPagina));
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Error al obtener los usuarios paginados.",
                    "/users"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }

}
