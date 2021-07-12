package br.delonborges.utils;

import io.restassured.RestAssured;

public class ContaUtils {

    public static Integer getContaIdPeloNome(String nome) {
        return RestAssured.get("/contas?nome=" + nome)
                .then().extract().path("id[0]");
    }
}
