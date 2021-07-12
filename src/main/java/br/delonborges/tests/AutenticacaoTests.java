package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AutenticacaoTests extends BaseTest {

    @Test
    public void naoDeveAcessarSemToken() {
        FilterableRequestSpecification filterableRequestSpecification = (FilterableRequestSpecification) RestAssured.requestSpecification;
        filterableRequestSpecification.removeHeader("Authorization");

        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);
    }
}
