package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;


//Bibliotecas para o saucelabs
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.PortariaPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
// fim saucelabs

//O listener envia o resultado do testng para o saucelab
//@Listeners({SauceOnDemandTestListener.class})
public class AcoesDocumentoDigitalIT extends IntegrationTestBase implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
	private String codigoDocumento;
	
	public AcoesDocumentoDigitalIT() throws FileNotFoundException, IOException {
		super();
	}
	
	@BeforeClass(dependsOnMethods={"iniciaWebDriver"})
	public void setUp() {
		try {
			PrincipalPage principalPage = efetuaLogin();			
			principalPage.clicarBotaoNovoDocumentoEx();		
			
			PortariaPage portariaPage = PageFactory.initElements(driver, PortariaPage.class);
			operacoesDocumentoPage = portariaPage.criaPortaria(propDocumentos);			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Exceção no método setUp: " + e);
		} 
	}
	
	@BeforeMethod
	public void paginaInicial(Method method) {
		try {
			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle());
			if(!driver.getCurrentUrl().contains("exibir.action") || driver.getTitle().contains("SIGA - Erro Geral")) {
				System.out.println("Efetuando busca!");
				driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);			
			}
			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled = true, priority = 1)
	public void finalizar() {
		super.finalizarDocumento(); 
	}
	
	@Test(enabled = true, priority = 2, dependsOnMethods = {"finalizar"})
	public void assinarDocumentoDigitalmente() {
		super.assinarDigitalmente(codigoDocumento, "Nº");
	}
	
	@Test(enabled = true, priority = 3, dependsOnMethods = {"assinarDocumentoDigitalmente"})
	public void anexarArquivo() {
		super.anexarArquivo(propDocumentos.getProperty("arquivoAnexo"));		
		Assert.assertTrue(operacoesDocumentoPage.isEstadoAtualDocumento("Anexo Pendente Assinatura/Conferência"), "Texto 'Anexo Pendente de Assinatura/Conferência' não foi encontrado!");		
	}
	
	@Test(enabled = true, priority = 3, dependsOnMethods = {"assinarDocumentoDigitalmente"})
	public void despacharDocumento() {
		TransferenciaPage transferenciaPage = operacoesDocumentoPage.clicarLinkDespacharTransferir();
		transferenciaPage.despacharDocumento(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isDespachoVisivel(propDocumentos.getProperty("despacho")));
	}
	
	// os métodos abaixo são necessários para implementar as interfaces SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider
	@Override
	public String getSessionId() {
	    SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
	    return (sessionId == null) ? null : sessionId.toString();
	}

	@Override
	public SauceOnDemandAuthentication getAuthentication() {
	    return authentication;
	}	
	
}