package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import br.delonborges.dto.Movimentacao;
import br.delonborges.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SeuBarrigaTests extends BaseTest {

    private static Integer CONTA_ID;
    private static Integer MOVIMENTACAO_ID;
    private static final String CONTA_NOME = "Conta " + System.nanoTime();

    @BeforeAll
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
    }

    @Test
    public void t01_deveIncluirContaComSucesso() {
        CONTA_ID = given()
            .body("{\"nome\": \"" + CONTA_NOME + "\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(201)
            .extract()
                .path("id");
    }

    @Test
    public void t02_deveAlterarContaComSucesso() {
        given()
            .body("{\"nome\": \"" + CONTA_NOME + " alterada\"}\n")
            .pathParam("id", CONTA_ID)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is(CONTA_NOME + " alterada"));
    }

    @Test
    public void t03_naoDeveIncluirContaComMesmoNome() {
        given()
            .body("{\"nome\": \"" + CONTA_NOME + " alterada\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void t04_deveInserirMovimentacaoComSucesso() {
        Movimentacao movimentacao = getMovimentacaoValida();

        MOVIMENTACAO_ID = given()
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201)
            .extract()
                .path("id");
    }

    @Test
    public void t05_deveValidarCamposObrigatoriosMovimentacao() {
        given()
            .body("{}")
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(8))
            .body("msg", hasItems(
                    "Data da Movimentação é obrigatório",
                    "Data do pagamento é obrigatório",
                    "Descrição é obrigatório",
                    "Interessado é obrigatório",
                    "Valor é obrigatório",
                    "Valor deve ser um número",
                    "Conta é obrigatório",
                    "Situação é obrigatório"
            ));
    }

    @Test
    public void t06_naoDeveInserirMovimentacaoComDataFutura() {
        Movimentacao movimentacao = getMovimentacaoValida();
        movimentacao.setData_transacao(DataUtils.getDataAndAddDays(5));

        given()
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    public void t07_naoDeveRemoverContaComMovimentacao() {
        given()
            .pathParam("id", CONTA_ID)
        .when()
            .delete("/contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void t08_deveCalcularSaldoContas() {
        given()
        .when()
            .get("/saldo")
        .then()
            .statusCode(200)
            .body("find{it.conta_id == " + CONTA_ID + "}.saldo", is("100.00"));
    }

    @Test
    public void t09_deveRemoverMovimentacaoComSucesso() {
        given()
            .pathParam("id", MOVIMENTACAO_ID)
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204);
    }

    @Test
    public void t10_naoDeveAcessarSemToken() {
        FilterableRequestSpecification filterableRequestSpecification = (FilterableRequestSpecification) RestAssured.requestSpecification;
        filterableRequestSpecification.removeHeader("Authorization");

        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);
    }


    @NotNull
    private Movimentacao getMovimentacaoValida() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(CONTA_ID);
        movimentacao.setDescricao("Descrição da movimentação");
        movimentacao.setEnvolvido("Envolvido na movimentação");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao(DataUtils.getDataAndAddDays(0));
        movimentacao.setData_pagamento(DataUtils.getDataAndAddDays(10));
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);
        return movimentacao;
    }
}