package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Receber extends AuthTest {

    public static void receber(User user, String sigla) {
        givenFor(user)

                .pathParam("sigla", sigla)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/receber")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_TramitarParaLotacaoEReceber_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
        String sigla = AssinarComSenha.assinarComSenha(siglaTmp);
        sigla += "A";

        Tramitar.tramitarParaLotacao(User.ZZ99999, sigla, "LTEST2");

        receber(User.ZZ99998, sigla);
    }

}
