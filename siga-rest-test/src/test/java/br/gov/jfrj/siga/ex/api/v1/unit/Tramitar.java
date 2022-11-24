package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Tramitar extends DocTest {

    public static void tramitar(
            Pessoa pessoa, String sigla, String lotacaoDest, String pessoaDest, String orgao,
            String observacao, String dataDevolucao) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)
                .param("lotacao", lotacaoDest)
                .param("matricula", pessoaDest)
                .param("orgao", orgao)
                .param("observacao", observacao)
                .param("dataDevolucao", dataDevolucao)

                .when().post("/sigaex/api/v1/documentos/{sigla}/tramitar").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    public static void tramitarParaLotacao(Pessoa pessoa, String sigla, Lotacao lotacao) {
        tramitar(pessoa, sigla, lotacao.name(), null, null, null, null);
    }

    @Test
    public void test_TramitarParaLotacao_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemMarca(CpMarcadorEnum.EM_TRANSITO_ELETRONICO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);

        consultar(Pessoa.ZZ99998, sigla);
        contemAcao("receber", true);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", false);
    }

}
