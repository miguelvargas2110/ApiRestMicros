package stepdefinitions;

import co.edu.uniquindio.microservicios.tallerapirest.Entities.User;
import co.edu.uniquindio.microservicios.tallerapirest.Repositories.UserRepository;
import co.edu.uniquindio.microservicios.tallerapirest.Services.UserServiceImpl;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@Transactional
public class UserCrudStepDefinitions {

    @MockBean
    private UserServiceImpl userServiceImpl;

    private String baseUrl = "http://localhost:8001";
    private Response response;
    private String username = "";
    private String password = "";
    private String email = "";
    private String token = "";
    private int tamanoPagina;
    private int numeroPagina;

    //SignUp
    @Given("un nuevo usuario con detalles válidos")
    public void unNuevoUsuarioConDetallesVálidos() {
        verificarUsuarioE();
        username = "Ortiz";
        password = "1234";
        email = "ortiz@gmail.com";
    }


    @Given("un nuevo usuario con detalles invalidos")
    public void unNuevoUsuarioConDetallesInvalidos() {
        username = "Tunubala";
        password = "123456";
        email = "ortiz@gmail.com";
    }

    @When("se envía una solicitud de registro con los datos de ese usuario")
    public void seEnvíaUnaSolicitudDeRegistroConLosDatosDeEseUsuario() {
        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\"}";

        response = given()
                .contentType("application/json")
                .body(body)
                .post(baseUrl + "/signup");
    }

    //Login
    @Given("un usuario con credenciales válidas")
    public void unUsuarioConCredencialesValidas() {
        username = "Ortiz";
        password = "1234";
    }

    @Given("un usuario con credenciales inválidas")
    public void unUsuarioConCredencialesInvalidas() {
        username = "Ortiz";
        password = "123456";
    }

    @When("se envía una solicitud de autenticacion con esas credenciales")
    public void seEnvíaUnaSolicitudDeAutenticacionConEsasCredenciales() {
        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        response = given()
                .contentType("application/json")
                .body(body)
                .post(baseUrl + "/login");
    }

    @Then("se debe devolver un token JWT")
    public void seDebeDevolverUnTokenJWT() {
        token = response.jsonPath().getString("jwt");
        assertNotNull(token);
    }

    //Autenticacion de usuario
    @Given("Un usuario autenticado")
    public void unUsuarioAutenticado() {
        verificarUsuarioNoE();
    }

    @Given("Un usuario no autenticado")
    public void unUsuarioNoAutenticado() {
        token = "";
    }

    //Update
    @And("intenta actualizarse a si mismo")
    public void intentaActualizarseASiMismo() {
        username = "Ortiz";
        password = "5678";
        email = "ortiz@gmail.com";
    }

    @And("intenta actualizar otro usuario")
    public void intentaActualizarOtroUsuario() {
        username = "Tunubala";
        password = "5678";
        email = "ortiz@gmail.com";

    }

    @When("se envía una solicitud de actualizacion con los datos")
    public void seEnvíaUnaSolicitudDeActualizacionConLosDatos() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("username", username)
                .multiPart("password", password)
                .multiPart("email", email)
                .put(baseUrl + "/user");
    }

    @When("se envía una solicitud de actualizacion sin los datos")
    public void seEnvíaUnaSolicitudDeActualizacionSinLosDatos() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("username", username)
                .put(baseUrl + "/user");
    }

    //Delete
    @And("intenta eliminarse a si mismo")
    public void intentaEliminarseASiMismo() {
        username = "Ortiz";
    }

    @And("intenta eliminar un usuario no existente")
    public void intentaEliminarUnUsuarioNoExistente() {
        username = "Ortiz";
    }

    @And("intenta eliminar otro usuario")
    public void intentaEliminarOtroUsuario() {
        username = "Tunubala";
    }

    @When("se envía una solicitud de eliminacion con el usuario")
    public void seEnvíaUnaSolicitudDeEliminacionConElUsuario() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("username", username)
                .delete(baseUrl + "/user");
    }

    //Pageable
    @Given("Hay usuarios en la base de datos")
    public void hayUsuariosEnLaBaseDeDatos() {

    }

    @And("se dan valores positivos para {int} y {int}")
    public void seDanValoresPositivosParaY(int tamanoPagina, int numeroPagina) {
        this.tamanoPagina = tamanoPagina;
        this.numeroPagina = numeroPagina;
    }

    @And("la respuesta debe contener una lista de usuarios con detalles de paginación")
    public void laRespuestaDebeContenerUnaListaDeUsuariosConDetallesDePaginación() {
        String mensaje = response.jsonPath().getString("message");
    }

    @And("se dan valores negativos para {int} y {int}")
    public void seDanValoresNegativosParaY(int tamanoPagina, int numeroPagina) {
        this.tamanoPagina = tamanoPagina;
        this.numeroPagina = numeroPagina;
    }

    @When("se envía una solicitud de paginacion con {int} y {int}")
    public void seEnvíaUnaSolicitudDePaginacionConY(int tamanoPagina, int numeroPagina) {
        response = given()
                .contentType("multipart/form-data")
                .multiPart("numeroPagina", numeroPagina)
                .multiPart("tamanoPagina", tamanoPagina)
                .get(baseUrl + "/users");
    }

    //Global
    @Then("el estado de la respuesta debe ser {int}")
    public void elEstadoDeLaRespuestaDebeSer(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCode());
    }

    @And("el mensaje de respuesta debe ser {string}")
    public void elMensajeDeRespuestaDebeSer(String message) {
        String mensaje = response.jsonPath().getString("message");
        assertEquals(message, mensaje);
    }

    @And("el mensaje de error debe ser {string}")
    public void elMensajeDeErrorDebeSer(String errorMessage) {
        String mensajeError = response.jsonPath().getString("message");
        assertEquals(errorMessage, mensajeError);
    }

    //generateChangePasswordToken
    @Given("Un usuario existente")
    public void unUsuarioExistente() {
        username = "Ortiz";
    }

    @Given("Un usuario no existente")
    public void unUsuarioNoExistente() {
        username = "Miguel";
    }

    @When("se envía una solicitud al servicio de generacion de token para cambio de contraseña con el usuario")
    public void seEnvíaUnaSolicitudAlServicioDeGeneracionDeTokenParaCambioDeContraseñaConElUsuario() {
        response = given()
                .contentType("multipart/form-data")
                .multiPart("username", username)
                .post(baseUrl + "/generateChangePasswordToken");
    }

    //ChangePassword
    @Given("Un usuario valido para hacer el cambio de contraseña")
    public void unUsuarioValidoParaHacerElCambioDeContraseña() {
        username = "Ortiz";
        password = "1234566";
        token = "eyJhbGciOiJIUzI1NiJ9.eyJjaGFuZ2VQYXNzd29yZCI6dHJ1ZSwic3ViIjoiT3J0aXoiLCJpc3MiOiJpbmdlc2lzLnVuaXF1aW5kaW8uZWR1LmNvIiwiaWF0IjoxNzI2NzYwNDQzLCJleHAiOjE3MjY3NjEwNDN9.Oz2Wb4mZXsIZx47HksD-9C3dqfuL55DoC2ktaq7fCEg";
    }


    @Given("Un usuario invalido para hacer el cambio de contraseña")
    public void unUsuarioInvalidoParaHacerElCambioDeContraseña() {
        username = "Tunubala";
        password = "1234";
        token = "eyJhbGciOiJIUzI1NiJ9.eyJjaGFuZ2VQYXNzd29yZCI6dHJ1ZSwic3ViIjoiT3J0aXoiLCJpc3MiOiJpbmdlc2lzLnVuaXF1aW5kaW8uZWR1LmNvIiwiaWF0IjoxNzI2NzYwNDQzLCJleHAiOjE3MjY3NjEwNDN9.Oz2Wb4mZXsIZx47HksD-9C3dqfuL55DoC2ktaq7fCEg";
    }

    @When("se envía una solicitud de cambio de contraseña con una nueva contraseña")
    public void seEnvíaUnaSolicitudDeCambioDeContraseñaConUnaNuevaContraseña() {
        response = given()
                .header("Authorization", "Bearer " + token)
                .contentType("multipart/form-data")
                .multiPart("username", username)
                .multiPart("password", password)
                .post(baseUrl + "/changePassword");
    }

    public void crearUsuario(){
        username = "Ortiz";
        password = "1234";
        email = "ortiz@gmail.com";
        seEnvíaUnaSolicitudDeRegistroConLosDatosDeEseUsuario();
    }

    public void borrarUsuario(){
        username = "Ortiz";
        seEnvíaUnaSolicitudDeEliminacionConElUsuario();
    }

    public void verificarUsuarioNoE(){
        Optional<User> userOptional = userServiceImpl.searchByUserName("Ortiz");
        if(userOptional.isEmpty()){
            crearUsuario();
        }
        generarToken();
    }

    public void verificarUsuarioE(){
        Optional<User> userOptional = userServiceImpl.searchByUserName("Ortiz");
        if(userOptional.isPresent()){
            generarToken();
            borrarUsuario();
        }
    }

    public void generarToken(){
        Optional<User> userOptional = userServiceImpl.searchByUserName("Ortiz");
        username = "Ortiz";
        password = userOptional.get().getPassword();
        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        response = given()
                .contentType("application/json")
                .body(body)
                .post(baseUrl + "/login");

        token = response.jsonPath().getString("jwt");
    }

}