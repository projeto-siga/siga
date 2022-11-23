package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Concluir extends AuthTest {

    public static void concluir(Pessoa pessoa, String sigla) {
        givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/concluir")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    @Test
    public void test_TramitarParaLotacaoEReceber_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        Notificar.notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        concluir(Pessoa.ZZ99998, sigla);
    }

}
