package br.gov.jfrj.siga.ex.api.v1.documento;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import br.gov.jfrj.siga.ex.api.v1.AuthTest;
import br.gov.jfrj.siga.ex.api.v1.AuthTest.Pessoa;

public class AssinarComSenha extends AuthTest {

    public static String assinarComSenha(Pessoa pessoa, String siglaTmp) {
        String sigla = givenFor(pessoa)

                .pathParam("sigla", siglaTmp)

                .when()
                .post("/sigaex/api/v1/documentos/{sigla}/assinar-com-senha")

                .then()
                .statusCode(200)
                .body("sigla", notNullValue())
                .body("status", equalTo("OK"))

                .extract()
                .path("sigla");
        sigla = Criar.compactarSigla(sigla);
        return sigla;
    }

    @Test
    public void test_AssinarComSenhaDocumentoFinalizado_OK() {

        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);

        // Finaliza
        String sigla = Finalizar.finalizar(Pessoa.ZZ99999, siglaTmp);

        // Assina
        sigla = assinarComSenha(Pessoa.ZZ99999, sigla);
    }

    @Test
    public void test_AssinarComSenhaDocumentoTemporario_OK() {

        String siglaTmp = Criar.criaMemorandoTemporario(Pessoa.ZZ99999);

        // Assina
        String sigla = assinarComSenha(Pessoa.ZZ99999, siglaTmp);
    }

}
