package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Concluir extends DocTest {

    public static void concluir(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when().post("/sigaex/api/v1/documentos/{sigla}/concluir").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_TramitarParaLotacaoEReceber_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        Notificar.notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        Receber.receber(Pessoa.ZZ99998, sigla);

        concluir(Pessoa.ZZ99998, sigla);
    }

}
