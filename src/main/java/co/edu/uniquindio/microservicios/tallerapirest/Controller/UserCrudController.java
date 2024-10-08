package co.edu.uniquindio.microservicios.tallerapirest.Controller;

import co.edu.uniquindio.microservicios.tallerapirest.DTO.*;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.ApiError;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.ApiSuccess;
import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Security.JwtService;
import co.edu.uniquindio.microservicios.tallerapirest.Services.AuthenticationServiceImpl;
import co.edu.uniquindio.microservicios.tallerapirest.Services.LogService;
import co.edu.uniquindio.microservicios.tallerapirest.Services.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @Autowired
    private LogService logService;

    //--------------------------------------------------------------------------------

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody LoginDTO authRequest, HttpServletRequest request) {
        try {
            AuthenticationResponse jwtDto = authenticationService.login(authRequest);
            logService.sendLog(
                    "UserManagementApp",
                    "LOGIN_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Inicio de sesión exitoso",
                    "El usuario " + authRequest.getUsername() + " inició sesión exitosamente desde IP " + request.getRemoteAddr()
            );
            return ResponseEntity.ok(jwtDto);
        } catch (BadCredentialsException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Usuario o contraseña no validos",
                    request.getRequestURI()
            );
            logService.sendLog(
                    "UserManagementApp",
                    "LOGIN_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Credenciales incorrectas",
                    "Intento de inicio de sesión fallido para el usuario " + authRequest.getUsername() + " desde IP " + request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
        } catch (InternalAuthenticationServiceException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Usuario o contraseña no validos",
                    request.getRequestURI()
            );
            logService.sendLog(
                    "UserManagementApp",
                    "LOGIN_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Credenciales incorrectas",
                    "Intento de inicio de sesión fallido para el usuario " + authRequest.getUsername() + " desde IP " + request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Ocurrió un error interno",
                    request.getRequestURI()
            );
            logService.sendLog(
                    "UserManagementApp",
                    "LOGIN_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado para el usuario " + authRequest.getUsername() + " desde IP " + request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
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

    @DeleteMapping("/user")
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
                logService.sendLog(
                        "UserManagementApp",
                        "DELETE_SUCCESS",
                        this.getClass().getName(),
                        LocalDateTime.now(),
                        "Usuario eliminado",
                        "El usuario " + username + " fue eliminado exitosamente desde IP " + request.getRemoteAddr()
                );

                return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
            } else {
                ApiError apiError = new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "El usuario a eliminar no fue encontrado",
                        request.getRequestURI()
                );
                logService.sendLog(
                        "UserManagementApp",
                        "DELETE_FAILURE",
                        this.getClass().getName(),
                        LocalDateTime.now(),
                        "Usuario no encontrado",
                        "Intento de eliminar el usuario " + username + " fallido porque no fue encontrado, desde IP " + request.getRemoteAddr()
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
            logService.sendLog(
                    "UserManagementApp",
                    "DELETE_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado al intentar eliminar el usuario " + username + " desde IP " + request.getRemoteAddr()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            String password = signUpDTO.password();
            User user = new User();
            user.setUsername(signUpDTO.username());
            user.setEmail(signUpDTO.email());
            user.setPassword(passwordEncoder.encode(password));
            userServiceImpl.save(user);

            ApiSuccess apiSuccess = new ApiSuccess(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(),
                    "Usuario creado",
                    "/signup"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "SIGNUP_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Usuario registrado",
                    "El usuario " + signUpDTO.username() + " fue registrado exitosamente"
            );

            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        } catch (DataIntegrityViolationException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Este usuario ya existe",
                    "/signup"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "SIGNUP_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Usuario ya registrado",
                    "Intento de registro del usuario " + signUpDTO.username() + " fallido porque ya existe en base de datos"
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Error al crear el usuario",
                    "/signup"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "SIGNUP_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado al intentar registrar el usuario " + signUpDTO.username()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }


    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@ModelAttribute UpdateDTO updateDTO) {
        try {
            User user = userServiceImpl.searchByUserName(updateDTO.username()).get();
            if ((updateDTO.email() != null && !updateDTO.email().isEmpty()) || (updateDTO.password() != null && !updateDTO.password().isEmpty())) {
                if(updateDTO.email() != null && !updateDTO.email().isEmpty()){
                    user.setEmail(updateDTO.email());
                }
                if(updateDTO.password() != null && !updateDTO.password().isEmpty()){
                    user.setPassword(passwordEncoder.encode(updateDTO.password()));
                }
            }else {
                ApiError apiError = new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "Ambos datos a actualizar son nulos",
                        "/signup"
                );

                logService.sendLog(
                        "UserManagementApp",
                        "UPDATE_FAILURE",
                        this.getClass().getName(),
                        LocalDateTime.now(),
                        "Datos nulos",
                        "Intento de actualización del usuario " + updateDTO.username() + " fallido porque ambos datos son nulos"
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

            logService.sendLog(
                    "UserManagementApp",
                    "UPDATE_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Usuario actualizado",
                    "El usuario " + updateDTO.username() + " fue actualizado exitosamente"
            );

            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        }catch (NullPointerException e){
            ApiError apiError = new ApiError(
                    HttpStatus.NO_CONTENT.value(),
                    HttpStatus.NO_CONTENT.getReasonPhrase(),
                    "Ambos datos a actualizar son nulos",
                    "/signup"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "UPDATE_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Datos nulos",
                    "Intento de actualización del usuario " + updateDTO.username() + " fallido porque ambos datos son nulos"
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Error al crear actualizar el usuario",
                    "/signup"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "UPDATE_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado al intentar actualizar el usuario " + updateDTO.username()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }

    @PostMapping("/generateChangePasswordToken")
    public ResponseEntity<?> generateChangePasswordToken(@RequestParam(value = "username", required = false) String username) {
        try {
            User user = userServiceImpl.searchByUserName(username).get();
            String token = jwtService.getChangePasswordToken(user);

            logService.sendLog(
                    "UserManagementApp",
                    "TOKEN_GENERATION_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Token Generado",
                    "Se ha generado un token para cambio de contraseña por el usuario : " + username
            );

            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (NoSuchElementException e) {
            ApiError apiError = new ApiError(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Usuario no encontrado",
                    "/generateChangePasswordToken"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "TOKEN_GENERATION_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Usuario No Encontrado",
                    "Intento de generación de token para cambio de contraseña del usuario " + username + " fallido porque no se ha encontrado"
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Error al generar el token.",
                    "/generateChangePasswordToken"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "TOKEN_GENERATION_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado al generar un token0 para el usuario " + username
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

            logService.sendLog(
                    "UserManagementApp",
                    "CHANGE_PASSWORD_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Credenciales Actualizadas",
                    "Se ha actualizado la información del usuario : " + passwordDTO.username()
            );

            return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "Error al cambiar la contraseña",
                    "/changePassword"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "CHANGE_PASSWORD_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error inesperado al actualizar la información del usuario " + passwordDTO.username()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> obtenerEntidadesPaginadas(@RequestParam(value = "numeroPagina", required = false) int numeroPagina, @RequestParam(value = "tamanoPagina", required = false) int tamanoPagina) {
        try {
            if(numeroPagina < 0 && tamanoPagina < 0){
                ApiError apiError = new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        "No se pueden dar valores negativos",
                        "/users"
                );

                logService.sendLog(
                        "UserManagementApp",
                        "PAGINATED_ENTITIES_FAILURE",
                        this.getClass().getName(),
                        LocalDateTime.now(),
                        "Valores negativos presentes",
                        "No se pudieron obtener las entidades paginadas ya que se brindaron valores negativos"
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
            }

            logService.sendLog(
                    "UserManagementApp",
                    "PAGINATED_ENTITIES_SUCCESS",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Entidades paginadas correctamente",
                    "Se han generado las entidades paginadas correctamente con el número de pagina: " + numeroPagina + " y el tamaño de pagina: " + tamanoPagina
            );

            return ResponseEntity.ok(userServiceImpl.obtenerEntidadesPaginadas(numeroPagina, tamanoPagina));
        } catch (Exception e) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Error al obtener los usuarios paginados.",
                    "/users"
            );

            logService.sendLog(
                    "UserManagementApp",
                    "PAGINATED_ENTITIES_FAILURE",
                    this.getClass().getName(),
                    LocalDateTime.now(),
                    "Error interno del servidor",
                    "Ocurrió un error al obtener los usuarios paginados"
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam(value = "password")String password) {
        User user = userServiceImpl.searchByUserName("Ortiz").get();
        user.setPassword(passwordEncoder.encode(password));
        userServiceImpl.update(user);
        ApiSuccess apiSuccess = new ApiSuccess(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Contraseña actualizada",
                "/changePassword"
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiSuccess);
    }

}
