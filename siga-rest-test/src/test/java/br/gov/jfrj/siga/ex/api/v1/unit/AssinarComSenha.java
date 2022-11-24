package br.gov.jfrj.siga.ex.api.v1.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class AssinarComSenha extends DocTest {

    public static String assinarComSenha(Pessoa pessoa, String siglaTmp) {
        ValidatableResponse resp = givenFor(pessoa)

                .pathParam("sigla", siglaTmp)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/assinar-com-senha")

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
    public void test_AssinarComSenhaDocumentoFinalizado_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        String sigla = Finalizar.finalizar(Pessoa.ZZ99999, siglaTmp);

        sigla = assinarComSenha(Pessoa.ZZ99999, sigla);
        sigla += "A";

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("assinar", false);
        contemAcao("transferir", true);
    }

    @Test
    public void test_AssinarComSenhaDocumentoTemporario_OK() {
        String siglaTmp = Criar.criarMemorandoTemporario(Pessoa.ZZ99999);
        
        String sigla = AssinarComSenha.assinarComSenha(Pessoa.ZZ99999, siglaTmp);
        sigla += "A";

        consultar(Pessoa.ZZ99999, sigla);
        contemMarca(CpMarcadorEnum.ASSINADO, Pessoa.ZZ99999, Lotacao.ZZLTEST);
        contemAcao("assinar", false);
        contemAcao("transferir", true);
    }

}
