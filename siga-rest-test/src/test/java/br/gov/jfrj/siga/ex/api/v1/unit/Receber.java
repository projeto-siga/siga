package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
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
    public void test_Receber_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        Tramitar.tramitarParaLotacao(Pessoa.ZZ99999, sigla, Lotacao.ZZLTEST2);

        receber(Pessoa.ZZ99998, sigla);

        consultar(Pessoa.ZZ99998, sigla);
        contemMarca(CpMarcadorEnum.EM_ANDAMENTO, Pessoa.ZZ99998, Lotacao.ZZLTEST2);
        contemAcao("receber", false);
        contemAcao("concluir_gravar", false);
        contemAcao("arquivar_corrente_gravar", true);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST.name(), "oval", null);
        contemVizNode("vizTramitacao", Lotacao.ZZLTEST2.name(), "rectangle", "red");
    }

}
