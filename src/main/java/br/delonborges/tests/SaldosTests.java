package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import br.delonborges.utils.ContaUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SaldosTests extends BaseTest {

    @Test
    public void deveCalcularSaldoContas() {
        String contaNome = "Conta para saldo";
        Integer contaId = ContaUtils.getContaIdPeloNome(contaNome);

        given()
        .when()
            .get("/saldo")
        .then()
            .statusCode(200)
            .body("find{it.conta_id == " + contaId + "}.saldo", is("534.00"));
    }
}
