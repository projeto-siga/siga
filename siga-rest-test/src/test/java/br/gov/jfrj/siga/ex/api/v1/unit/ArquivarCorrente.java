package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class ArquivarCorrente extends DocTest {

    public static void arquivar(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when().post("/sigaex/api/v1/documentos/{sigla}/arquivar-corrente").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_Arquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        arquivar(Pessoa.ZZ99999, sigla);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ARQUIVADO_CORRENTE, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("arquivar_corrente_gravar", false);
        contemAcao("reabrir_gravar", true);
    }

}
