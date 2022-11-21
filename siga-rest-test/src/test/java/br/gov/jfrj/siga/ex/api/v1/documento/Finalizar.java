package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Finalizar extends AuthTest {

    public static String finalizar(Pessoa pessoa, String siglaTmp) {
        String sigla = givenFor(pessoa)

                .pathParam("sigla", siglaTmp)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/finalizar")

                .then()
                .statusCode(200)
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"))

                .extract()
                .path("sigla");
        sigla = Criar.compactarSigla(sigla);
        return sigla;
    }

    @Test
    public void test_Finalizar_OK() {

        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);

        // Finaliza
        String sigla = finalizar(Pessoa.ZZ99999, siglaTmp);
    }

}
