package br.delonborges.tests;

import br.delonborges.common.BaseTest;
import br.delonborges.dto.Movimentacao;
import br.delonborges.utils.ContaUtils;
import br.delonborges.utils.DataUtils;
import br.delonborges.utils.MovimentacaoUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacoesTests extends BaseTest {

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao movimentacao = MovimentacaoUtils.getMovimentacaoValida();

        given()
            .body(movimentacao)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201)
            .extract()
                .path("id");
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
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
    public void naoDeveInserirMovimentacaoComDataFutura() {
        Movimentacao movimentacao = MovimentacaoUtils.getMovimentacaoValida();
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
    public void naoDeveRemoverContaComMovimentacao() {
        String contaNome = "Conta com movimentacao";
        Integer contaId = ContaUtils.getContaIdPeloNome(contaNome);

        given()
            .pathParam("id", contaId)
        .when()
            .delete("/contas/{id}")
        .then()
            .statusCode(500)
            .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void deveRemoverMovimentacaoComSucesso() {
        String movimentacaoNome = "Movimentacao para exclusao";
        Integer movimentacaoId = MovimentacaoUtils.getMovimentacaoIdPelaDescricao(movimentacaoNome);

        given()
            .pathParam("id", movimentacaoId)
        .when()
            .delete("/transacoes/{id}")
        .then()
            .statusCode(204);
    }
}
