package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
//Biliotecas para o saucelabs
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.EditaDocumentoPage;
import br.gov.jfrj.siga.page.objects.MemorandoPage;
import br.gov.jfrj.siga.page.objects.OficioPage;
import br.gov.jfrj.siga.page.objects.PortariaPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
// fim saucelabs

//@Listeners({SauceOnDemandTestListener.class})
public class CriacaoDocumentoIT extends IntegrationTestBase implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider{
	private PrincipalPage principalPage;
	
	public CriacaoDocumentoIT() throws FileNotFoundException, IOException {
		super();
	}

	@BeforeClass(dependsOnMethods={"iniciaWebDriver"})	
	public void setUp() {
		try {
			principalPage = efetuaLogin();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Exceção no método setUp: " + e);
		} 
	}
	
	@BeforeMethod
	public void paginaInicial(Method method) {
		try {
			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle());
			
			if(driver.getCurrentUrl().contains("exibir.action") && util.getClickableElement(driver, By.linkText("Editar")) != null) {
				operacoesDocumentoPage.clicarLinkEditar();
			} else {
				if(!driver.getTitle().equals("SIGA - Página Inicial")) {
					driver.get(baseURL + "/siga");
				}
				principalPage.clicarBotaoNovoDocumentoEx();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test(enabled = true)
	public void criaDocumentoExterno() {
		EditaDocumentoPage editaDocumentoPage = PageFactory.initElements(driver, EditaDocumentoPage.class);
		operacoesDocumentoPage = editaDocumentoPage.preencheDocumentoExterno(propDocumentos);		
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("Expediente Externo Nº"), "Texto Expediente Externo Nº TMP não foi encontrado!");
	}

	@Test(enabled = false)
	public void criaDocumentoInternoImportado() {
		EditaDocumentoPage editaDocumentoPage = PageFactory.initElements(driver, EditaDocumentoPage.class);
		operacoesDocumentoPage = editaDocumentoPage.preencheDocumentoInternoImportado(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("Expediente Interno Nº TMP"), "Texto Expediente Interno Nº TMP não foi encontrado!");
	}
	
	@Test(enabled = true)
	public void criaMemorando() {
		MemorandoPage memorandoPage = PageFactory.initElements(driver, MemorandoPage.class);
		operacoesDocumentoPage = memorandoPage.criaMemorando(propDocumentos);
		
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("MEMORANDO Nº TMP"), "Texto MEMORANDO Nº TMP não foi encontrado!");
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("Atenciosamente"), "Fecho não encontrado!");
	}
	
	@Test(enabled = true)
	public void criaPortaria() {
		PortariaPage portariaPage = PageFactory.initElements(driver, PortariaPage.class);
		operacoesDocumentoPage = portariaPage.criaPortaria(propDocumentos);
		
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("PORTARIA Nº TMP"), "Texto PORTARIA Nº TMP não foi encontrado!");
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("Testes de Integração"), "Informação sobre o que Dispõe o documento não encontrada!");
	}
	
	@Test(enabled = true)
	public void criaOficio() {
		OficioPage oficioPage = PageFactory.initElements(driver, OficioPage.class);
		operacoesDocumentoPage = oficioPage.criaOficio(propDocumentos);

		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("OFÍCIO Nº TMP"), "Texto OFÍCIO Nº TMP não foi encontrado!");
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel(propDocumentos.getProperty("enderecoDestinatario")), "Endereço não encontrado!");
		Assert.assertTrue(operacoesDocumentoPage.isIdentificacaoDocumentoVisivel("Senhor Juiz"), "Forma de Tratamento não encontrada!");
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