package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Receber extends AuthTest {

    public static void receber(Pessoa pessoa, String sigla) {
        givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/receber")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_TramitarParaLotacaoEReceber_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        receber(Pessoa.ZZ99998, sigla);
    }

}
