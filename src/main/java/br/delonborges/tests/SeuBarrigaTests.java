package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import org.junit.jupiter.api.Test;

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
}
