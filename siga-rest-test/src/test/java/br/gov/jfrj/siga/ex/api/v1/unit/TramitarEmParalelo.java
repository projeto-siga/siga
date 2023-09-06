package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;

public class TramitarEmParalelo extends DocTest {

    public static void tramitarEmParalelo(
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
                .post("/sigaex/api/v1/documentos/{sigla}/tramitar-em-paralelo")

                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }

    public static void tramitarEmParalelo(Pessoa pessoa, String sigla, Pessoa pessoaDest) {
        tramitarEmParalelo(pessoa, sigla, pessoaDest.name(), null, null, null, null);
    }

    public static void tramitarEmParalelo(Pessoa pessoa, String sigla, Lotacao lotacaoDest) {
        tramitarEmParalelo(pessoa, sigla, null, lotacaoDest.name(), null, null, null);
    }

    @Test
    public void test_TramitarEmParaleloParaLotacao_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        tramitarEmParalelo(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

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
