package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import br.gov.jfrj.siga.api.v1.AuthTest.Pessoa;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class ConsultarModeloDocumentoPelaSigla extends DocTest {

    public static ValidatableResponse consultarModeloDocumentoPelaSigla(Pessoa pessoa, String sigla) {
        ValidatableResponse resp = givenFor(pessoa)
        		
                .pathParam("sigla", sigla)
                
                .when()
                .get("/sigaex/api/v1/documentos/{sigla}/modelo")
                
                .then();

        return resp;
    }

    @Test
    public void test_BuscarModeloDocumentoPorSigla_OK() {
        Pessoa pessoa = Pessoa.ZZ99999;
        String sigla = criarMemorando(pessoa);

        ValidatableResponse response = consultarModeloDocumentoPelaSigla(Pessoa.ZZ99999, sigla);

        response.statusCode(200);
        assertNotNull("A resposta não deve ser nula", response.extract().response().getBody());
        assertNotNull("O ID do modelo do documento deve ser retornado", response.extract().path("idModelo"));
    }

    public static String criarMemorando(Pessoa pessoa) {
        String siglaTmp = Criar.criarMemorandoTemporario(pessoa);
        String sigla = AssinarComSenha.assinarComSenha(pessoa, siglaTmp);
        sigla += "A";
        return sigla;
    }

    public static String compactarSigla(String sigla) {
        if (sigla == null)
            return null;
        return sigla.replace("-", "").replace("/", "");
    }
}
