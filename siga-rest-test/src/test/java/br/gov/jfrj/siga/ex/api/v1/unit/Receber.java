package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Receber extends DocTest {

    public static void receber(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when().post("/sigaex/api/v1/documentos/{sigla}/receber").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_TramitarParaLotacaoEReceber_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        receber(Pessoa.ZZ99998, sigla);
    }

}
