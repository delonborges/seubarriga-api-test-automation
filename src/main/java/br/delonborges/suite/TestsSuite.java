package br.delonborges.suite;

import br.delonborges.common.BaseTest;
import br.delonborges.tests.AutenticacaoTests;
import br.delonborges.tests.ContasTests;
import br.delonborges.tests.MovimentacoesTests;
import br.delonborges.tests.SaldosTests;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
        ContasTests.class,
        MovimentacoesTests.class,
        SaldosTests.class,
        AutenticacaoTests.class
})

public class TestsSuite extends BaseTest {

    @BeforeClass
    public static void login() {
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

        RestAssured.requestSpecification.header("Authorization", "JWT " + token);
        RestAssured.get("/reset").then().statusCode(200);
    }
}
