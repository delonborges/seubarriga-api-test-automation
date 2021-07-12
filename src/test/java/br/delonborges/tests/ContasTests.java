package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import br.delonborges.utils.ContaUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ContasTests extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {
        String contaNome = "Conta inserida";

        given()
            .body("{\"nome\": \"" + contaNome + "\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(201)
            .extract()
                .path("id");
    }

    @Test
    public void deveAlterarContaComSucesso() {
        String contaNome = "Conta para alterar";
        Integer contaId = ContaUtils.getContaIdPeloNome(contaNome);

        given()
            .body("{\"nome\": \"" + contaNome + " alterada\"}\n")
            .pathParam("id", contaId)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is(contaNome + " alterada"));
    }

    @Test
    public void naoDeveIncluirContaComMesmoNome() {
        String contaNome = "Conta mesmo nome";

        given()
            .body("{\"nome\": \"" + contaNome + "\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"));
    }
}