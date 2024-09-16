package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class UserCrudStepDefinitions {

    private String baseUrl = "http://localhost:8001";
    private Response response;
    private String username = "";
    private String password = "";

    @Given("un usuario con credenciales válidas")
    public void unUsuarioConCredencialesValidas() {
        username = "Ortiz";
        password = "1234";
    }

    @When("se envía una solicitud de autenticacion con esas credenciales")
    public void seEnvíaUnaSolicitudDeAutenticacionConEsasCredenciales() {
        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        response = given()
                .contentType("application/json")
                .body(body)
                .post(baseUrl + "/login");
    }

    @Then("el estado de la respuesta debe ser {int}")
    public void elEstadoDeLaRespuestaDebeSer(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCode());
    }

    @Then("se debe devolver un token JWT")
    public void seDebeDevolverUnTokenJWT() {
        String token = response.jsonPath().getString("jwt");
        assertNotNull(token);
    }

    @Given("un usuario con credenciales inválidas")
    public void unUsuarioConCredencialesInvalidas() {
        username = "Ortiz";
        password = "123456";
    }

    @Then("el mensaje de error debe ser {string}")
    public void elMensajeDeErrorDebeSer(String errorMessage) {
        String mensajeError = response.jsonPath().getString("message");
        assertEquals(errorMessage, mensajeError);
    }
}