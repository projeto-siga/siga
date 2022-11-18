package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

//TODO: O método de cancelar ainda não está implementado na API
public class Cancelar extends AuthTest {

    public static void cancelar(String sigla) {
        givenZZ99999()

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/cancelar")

                .then()
                .statusCode(200)
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"));
    }

    // @Test
    public void test_Cancelar_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
        String sigla = Finalizar.finalizar(siglaTmp);
        cancelar(sigla);
    }

}
