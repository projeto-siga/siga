package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class DesarquivarCorrente extends DocTest {

    public static void desarquivar(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", sigla)

                .when().post("/sigaex/api/v1/documentos/{sigla}/desarquivar-corrente").then();

        assertStatusCode200(resp);

        resp.body("status", equalTo("OK"));
    }

    @Test
    public void test_Arquivar_OK() {
        String sigla = Criar.criarMemorando(Pessoa.ZZ99999);

        ArquivarCorrente.arquivar(Pessoa.ZZ99999, sigla);

        desarquivar(Pessoa.ZZ99999, sigla);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("arquivar_corrente_gravar", true);
        contemAcao("reabrir_gravar", false);
    }

}
