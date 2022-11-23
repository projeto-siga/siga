package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import br.gov.jfrj.siga.ex.api.v1.AuthTest.Pessoa;

public class Tramitar extends AuthTest {

    public static void tramitar(
            Pessoa pessoa, String sigla, String lotacaoDest, String pessoaDest, String orgao,
            String observacao, String dataDevolucao) {
        givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("lotacao", lotacaoDest)
                .param("matricula", pessoaDest)
                .param("orgao", orgao)
                .param("observacao", observacao)
                .param("dataDevolucao", dataDevolucao)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/tramitar")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    public static void tramitarParaLotacao(Pessoa pessoa, String sigla, Lotacao lotacao) {
        tramitar(pessoa, sigla, lotacao.name(), null, null, null, null);
    }

    @Test
    public void test_TramitarParaLotacao_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);
    }

}
