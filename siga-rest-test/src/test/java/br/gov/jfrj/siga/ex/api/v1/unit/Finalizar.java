package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class Finalizar extends DocTest {

    public static String finalizar(Pessoa pessoa, String siglaTmp) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", siglaTmp)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/finalizar")

                .then();

        assertStatusCode200(resp);

        String sigla = resp
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"))

                .extract()
                .path("sigla");

        sigla = Criar.compactarSigla(sigla);
        return sigla;
    }

    @Test
    public void test_Finalizar_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);

        String sigla = finalizar(Pessoa.ZZ99999, siglaTmp);

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemMarca(CpMarcadorEnum.COMO_SUBSCRITOR, Pessoa.ZZ99999);
        contemAcao("assinar", true);
        contemAcao("transferir", false);
    }

}
