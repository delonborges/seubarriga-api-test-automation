package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import br.delonborges.dto.Movimentacao;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SeuBarrigaTests extends BaseTest {

    private String TOKEN;

    @BeforeEach
    public void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "delon@borges.com");
        login.put("senha", "delonborges");

        TOKEN = given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract()
                .path("token");
    }

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
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"Conta do Delon\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(201);
    }

    @Test
    public void deveAlterarContaComSucesso() {
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"Conta alterada do Delon\"}\n")
        .when()
            .put("/contas/668408")
        .then()
            .statusCode(200)
            .body("nome", is("Conta alterada do Delon"));
    }

    @Test
    public void naoDeveIncluirContaComMesmoNome() {
        given()
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"Conta alterada do Delon\"}\n")
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao movimentacao = getMovimentacaoValida();

        given()
            .header("Authorization", "JWT " + TOKEN)
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201);
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
        given()
            .header("Authorization", "JWT " + TOKEN)
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
    public void naoDeveInserirMovimentacaoComDataFutura() {
        Movimentacao movimentacao = getMovimentacaoValida();
        movimentacao.setData_transacao("01/01/2100");

        given()
            .header("Authorization", "JWT " + TOKEN)
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @NotNull
    private Movimentacao getMovimentacaoValida() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(668408);
        movimentacao.setDescricao("Descrição da movimentação");
        movimentacao.setEnvolvido("Envolvido na movimentação");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao("01/01/2010");
        movimentacao.setData_pagamento("01/01/2020");
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);
        return movimentacao;
    }
}