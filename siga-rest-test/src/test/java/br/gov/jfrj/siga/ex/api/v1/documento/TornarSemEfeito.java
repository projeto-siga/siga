package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import br.gov.jfrj.siga.ex.api.v1.AuthTest.Pessoa;

public class TornarSemEfeito extends AuthTest {

    public static void tornarSemEfeito(Pessoa pessoa, String sigla) {
        givenFor(pessoa)

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
        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        tornarSemEfeito(Pessoa.ZZ99999, sigla);
    }

}
