package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class TornarSemEfeito extends AuthTest {

    public static void tornarSemEfeito(User user, String sigla) {
        givenFor(user)

                .pathParam("sigla", sigla)
                .param("motivo", "Foi criado apenas para fins de teste automatizado.")

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/tornar-sem-efeito")

                .then()
                .statusCode(200)
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_TornarSemEfeito_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
        String sigla = AssinarComSenha.assinarComSenha(siglaTmp);
        tornarSemEfeito(User.ZZ99999, sigla);
    }

}
