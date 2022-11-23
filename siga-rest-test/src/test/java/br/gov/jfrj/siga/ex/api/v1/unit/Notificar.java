package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;

public class Notificar extends DocTest {

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
                .post("/sigaex/api/v1/documentos/{sigla}/notificar")

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
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        notificar(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.CAIXA_DE_ENTRADA, Lotacao.ZZLTEST2);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", "red");
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");
    }

}
