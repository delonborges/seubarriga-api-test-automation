package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SeuBarrigaTests extends BaseTest {

    @Test
    public void naoDeveAcessarSemToken() {
        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);
    }

    @Test
    public void deveIncluirContaComSucesso() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "delon@borges.com");
        login.put("senha", "delonborges");

        String token = given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract()
                .path("token");

        given()
            .header("Authorization", "JWT " + token)
            .body("{\"nome\": \"Conta do Delon\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(201);
    }
}