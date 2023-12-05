package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import br.gov.jfrj.siga.api.v1.AuthTest.Pessoa;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class ConsultarModeloDocumentoPelaSigla extends DocTest {

    public static ValidatableResponse consultarModeloDocumentoPelaSigla(Pessoa pessoa, String id) {
        ValidatableResponse resp = givenFor(pessoa)
        		
                .pathParam("id", id)
                
                .when()
                //.get("/sigaex/api/v1/documentos/{sigla}/modelo")
                .get("/sigaex/api/v1/documentos/{id}/consultar-modelo")
                .then();

        return resp;
    }

    @Test
    public void test_status_OK() {
        //Pessoa pessoa = Pessoa.ZZ99999;
        //String sigla = criarMemorando(pessoa);
        //ValidatableResponse response = consultarModeloDocumentoPelaSigla(Pessoa.ZZ99999, sigla);
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(Pessoa.ZZ99999, "2");
        response.statusCode(200);
    }
    
    @Test
    public void test_not_null() {
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(Pessoa.ZZ99999, "2");
        assertNotNull("A resposta não deve ser nula", response.extract().response().getBody());
    }
    
    @Test
    public void test_retorna_modelo() {
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(Pessoa.ZZ99999, "2");
        String idModelo = response.extract().path("idModelo");
        assertNotNull("O ID do modelo do documento deve ser retornado", idModelo);
    }
    
    //TODO: Suportar sigla como entrada
    //TODO: Deve retornar o id do documento pela sigla
    //TODO: Deve retornar o modelo correto
    
/*
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
*/
}
