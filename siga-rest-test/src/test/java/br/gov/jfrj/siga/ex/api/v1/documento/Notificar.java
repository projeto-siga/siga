package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import io.restassured.response.ValidatableResponse;

public class Notificar extends AuthTest {

    public static void notificar(
            Pessoa pessoa, String sigla, String pessoaDest, String lotacaoDest, String orgao,
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

    public static void notificar(Pessoa pessoa, String sigla, Pessoa pessoaDest) {
        notificar(pessoa, sigla, pessoaDest.name(), null, null, null, null);
    }

    public static void notificar(Pessoa pessoa, String sigla, Lotacao lotacaoDest) {
        notificar(pessoa, sigla, null, lotacaoDest.name(), null, null, null);
    }

    @Test
    public void test_NotificarLotacao_OK() {
        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        ValidatableResponse resp = Consultar.consultar(Pessoa.ZZ99998, sigla);

        Consultar.contemMarca(resp, CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        Consultar.contemMarca(resp, CpMarcadorEnum.EM_TRANSITO_ELETRONICO, Pessoa.ZZ99999, Lotacao.ZZLTEST);

        Consultar.contemAcao(resp, "receber", true);
        Consultar.contemAcao(resp, "concluir_gravar", false);

        // O recebimento é implícito?
        Receber.receber(Pessoa.ZZ99998, sigla);

        resp = Consultar.consultar(Pessoa.ZZ99998, sigla);

        Consultar.contemMarca(resp, CpMarcadorEnum.AGUARDANDO_CONCLUSAO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);

        Consultar.contemAcao(resp, "receber", false);
        Consultar.contemAcao(resp, "concluir_gravar", true);

        Concluir.concluir(Pessoa.ZZ99998, sigla);

        Consultar.contemAcao(resp, "receber", false);
        Consultar.contemAcao(resp, "concluir_gravar", false);
    }

}
