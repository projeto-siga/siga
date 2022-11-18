package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Excluir extends AuthTest {

    public static void excluir(String sigla) {
        givenZZ99999()

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/excluir")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_Excluir_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
        excluir(siglaTmp);
    }

}
