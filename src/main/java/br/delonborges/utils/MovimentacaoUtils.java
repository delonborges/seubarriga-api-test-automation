package br.delonborges.utils;

import br.delonborges.dto.Movimentacao;
import io.restassured.RestAssured;

public class MovimentacaoUtils {

    public static Integer getMovimentacaoIdPelaDescricao(String descricao) {
        return RestAssured.get("/transacoes?descricao=" + descricao)
                .then().extract().path("id[0]");
    }

    public static Movimentacao getMovimentacaoValida() {
        String contaNome = "Conta para movimentacoes";
        Integer contaId = ContaUtils.getContaIdPeloNome(contaNome);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(contaId);
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
