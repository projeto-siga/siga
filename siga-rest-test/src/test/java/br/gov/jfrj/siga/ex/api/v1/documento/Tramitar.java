package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;

public class Tramitar extends AuthTest {

    public static void tramitar(User user, String sigla, String lotacao, String matricula, String orgao,
            String observacao, String dataDevolucao) {
        givenFor(user)

                .pathParam("sigla", sigla)
                .param("lotacao", lotacao)
                .param("matricula", matricula)
                .param("orgao", orgao)
                .param("observacao", observacao)
                .param("dataDevolucao", dataDevolucao)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/tramitar")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    public static void tramitarParaLotacao(User user, String sigla, String lotacao) {
        tramitar(user, sigla, lotacao, null, null, null, null);
    }

    @Test
    public void test_TramitarParaLotacao_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario();
        String sigla = AssinarComSenha.assinarComSenha(siglaTmp);
        sigla += "A";

        tramitarParaLotacao(User.ZZ99999, sigla, Lotacao.ZZLTEST2.name());
    }

}
