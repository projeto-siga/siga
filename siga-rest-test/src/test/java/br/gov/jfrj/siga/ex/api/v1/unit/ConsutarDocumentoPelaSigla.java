package br.gov.jfrj.siga.ex.api.v1.unit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import br.gov.jfrj.siga.api.v1.AuthTest.Lotacao;
import br.gov.jfrj.siga.api.v1.AuthTest.Pessoa;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.ex.api.v1.DocTest;
import io.restassured.response.ValidatableResponse;

public class ConsutarDocumentoPelaSigla extends DocTest {

    public static ValidatableResponse consultarDocumentoPelaSigla(Pessoa pessoa, String sigla) {
    	ValidatableResponse resp = givenFor(pessoa)
    			
                .pathParam("sigla", sigla)
             
                .when()
                .get("/sigaex/api/v1/documentos/{sigla}")
                
                .then();
                
                return resp;
    }

    @Test
    public void test_BuscarDocumentoPorSigla_OK() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String sigla = criarMemorando(pessoa);
    	
        ValidatableResponse response = consultarDocumentoPelaSigla(Pessoa.ZZ99999, sigla);
        
        response.statusCode(200);
        assertNotNull("A resposta não deve ser nula", response.extract().response().getBody());
    }
    
    @Test
    public void teste_SiglaCorreta() {
    	Pessoa pessoa = Pessoa.ZZ99999;
    	String sigla = criarMemorando(pessoa);
    	String siglaCompactada = compactarSigla(sigla);
        
    	ValidatableResponse response = consultarDocumentoPelaSigla(Pessoa.ZZ99999, sigla);
    	String siglaRetornada = response.extract().path("sigla");
    	// Adiciona +A pra ficar igual no método criarMemorando
    	siglaRetornada += "A";
    	
    	String siglaRetornadaCompactada = compactarSigla(siglaRetornada);
    	
        // Verifica se a resposta contém a sigla correta
        assertEquals("A sigla do documento deve corresponder à sigla enviada", siglaCompactada, siglaRetornadaCompactada);
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
