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
                .get("/sigaex/api/v1/documentos/{id}/consultar-modelo")
                .then();

        return resp;
    }
       
    @Test
    public void test_status_OK() {
        Pessoa pessoa = Pessoa.ZZ99999;
        String sigla = criarDocumentoERetornarSigla(pessoa);
        String siglaCompactada = compactarSigla(sigla);
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        response.statusCode(200);
    }
    
    @Test
    public void test_not_null() {
    	Pessoa pessoa = Pessoa.ZZ99999;
        String sigla = criarDocumentoERetornarSigla(pessoa);
        String siglaCompactada = compactarSigla(sigla);
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        assertNotNull("A resposta não deve ser nula", response.extract().response().getBody());
    }
    
    @Test
    public void teste_SiglaCorreta() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String sigla = criarDocumentoERetornarSigla(pessoa);
    	String siglaCompactada = compactarSigla(sigla);
    	ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, sigla);
    	String siglaRetornada = response.extract().path("siglaDocumento");
    	String siglaRetornadaCompactada = compactarSigla(siglaRetornada);
        assertEquals("A sigla do documento deve corresponder à sigla enviada", siglaCompactada, siglaRetornadaCompactada);
    }
    /*
    @Test
    public void teste_IdDoDocumentoCorreto() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String sigla = "OTZZ-MEM-2023/00001-A"; //sigla fica no ex_mobil
    	String siglaCompactada = compactarSigla(sigla);
    	String idDocumento = "2"; // documento que tem a sigla "OTZZ-MEM-2023/00001-A"
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String siglaDocRetorno = response.extract().path("siglaDocumento");
        String idDocRetorno = response.extract().path("idDoc");
        assertNotNull("O ID do documento deve ser retornado", idDocRetorno);
        assertEquals("O ID deve estar certo", idDocumento, idDocRetorno);
    }
    */

    //TODO: Criar um modelo no documento de teste ou usar mock-test
    /*
     * Funciona se no BD tiver esses dados
     * Documento com id = 2 e id_modelo = 26
     * O documento precisa ter um modelo associado
     */
    /*
    @Test
    public void test_retorna_modelo() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	
    	//sigla fica no ex_mobil
    	String sigla = "OTZZ-MEM-2023/00001-A";
    	String siglaCompactada = compactarSigla(sigla);
    	String idDocumento = "2";
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String idModelo = response.extract().path("idModelo");
        assertNotNull("O ID do modelo do documento deve ser retornado", idModelo);
    }
    
    @Test
    public void test_retorna_o_modelo_correto() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String idDocumento = "2";
    	String siglaCompactada = compactarSigla("OTZZ-MEM-2023/00001-A");
    	
    	String idModeloExpected = "26";
    	
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String idModeloRetornado = response.extract().path("idModelo");
        
        
        assertEquals("O modelo do documento deve ser o esperado", idModeloExpected, idModeloRetornado);
    }
    
    @Test
    public void test_retorna_nome_do_modelo() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String sigla = "OTZZ-MEM-2023/00001-A";
    	String siglaCompactada = compactarSigla(sigla);
    	String idDocumento = "2";        
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String nomeDoModelo = response.extract().path("nomeDoModelo");
        assertNotNull("O nome do modelo do documento deve ser retornado", nomeDoModelo);
    }
    
    @Test
    public void test_retorna_o_nome_do_modelo_correto() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String idDocumento = "2";
    	String siglaCompactada = compactarSigla("OTZZ-MEM-2023/00001-A");
    	String nomeModeloExpected = "Memorando";
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String nomeModeloRetornado = response.extract().path("nomeDoModelo");
        assertEquals("O nome do documento deve ser o esperado", nomeModeloExpected, nomeModeloRetornado);
    }
    
    @Test
    public void test_retorna_o_nome_do_modelo_correto_2() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String idDocumento = "27";
    	String siglaCompactada = compactarSigla("OTZZ-DES-2023/00020-A");
    	String nomeModeloExpected = "Despacho";
        ValidatableResponse response = consultarModeloDocumentoPelaSigla(pessoa, siglaCompactada);
        String nomeModeloRetornado = response.extract().path("nomeDoModelo");
        assertEquals("O nome do documento deve ser o esperado", nomeModeloExpected, nomeModeloRetornado);
    }
	*/
    public static String criarDocumentoERetornarSigla(Pessoa pessoa) {
    	//Cria documento
        String siglaTmp = Criar.criarMemorandoTemporario(pessoa);
        
        //Assinar documento
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
