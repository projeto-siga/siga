package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import br.gov.jfrj.siga.page.objects.AssinaturaDigitalPage;
import br.gov.jfrj.siga.page.objects.CancelamentoJuntadaPage;
import br.gov.jfrj.siga.page.objects.PortariaPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.VisualizacaoDossiePage;




//Bibliotecas para o saucelabs
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
//fim saucelabs

public class ProcessoAdministrativoDigitalIT extends IntegrationTestBase implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
	private String codigoDocumento;
	private String codigoProcesso;
	
	public ProcessoAdministrativoDigitalIT() throws FileNotFoundException, IOException {
		super();
	}
	
	@BeforeClass(dependsOnMethods={"iniciaWebDriver"})	
	public void setUp() {
		try{
			PrincipalPage principalPage = efetuaLogin();			
			principalPage.clicarBotaoNovoDocumentoEx();
			
			PortariaPage portariaPage = PageFactory.initElements(driver, PortariaPage.class);
			operacoesDocumentoPage = portariaPage.criaPortaria(propDocumentos);
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
			
			operacoesDocumentoPage.clicarLinkFinalizar();
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
			
			AssinaturaDigitalPage assinaturaDigitalPage = operacoesDocumentoPage.clicarLinkAssinarDigitalmente();			
			operacoesDocumentoPage = assinaturaDigitalPage.registrarAssinaturaDigital(baseURL, codigoDocumento);			
		} catch (Exception e) {
			isTestSuccesful = Boolean.FALSE;
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
				
				if(codigoProcesso != null) {	
					driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoProcesso);		
				} else {
					driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);		
				}	
			}
			
			codigoProcesso = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled = true)
	public void autuarProcesso(){
		super.autuar(Boolean.TRUE, "Processo de Outros Assuntos Administrativos");
	}
	
	@Test(enabled = true, priority = 1, dependsOnMethods = {"autuarProcesso"})
	public void finalizar() {
		super.finalizarProcesso();
	}
	
	@Test(enabled = true, priority = 2, dependsOnMethods = {"finalizar"})
	public void assinarDocumentoDigitalmente() {
		super.assinarDigitalmente(codigoProcesso, propDocumentos.getProperty("descricao"));
	}
	
	@Test(enabled = true, priority = 3, dependsOnMethods = {"assinarDocumentoDigitalmente"})
	public void juntar() {
		// Se o documento for digital, o anterior terá sido juntado automaticamente ao processo no evento da assinatura do processo. 
		// Clicar em "Visualizar Dossiê"
		VisualizacaoDossiePage visualizacaoDossiePage = operacoesDocumentoPage.clicarLinkVisualizarDossie();
		
		// Garantir que o número da página esteja aparecendo						
		Assert.assertTrue(visualizacaoDossiePage.visualizaNumeroPagina(codigoDocumento), "O número da página não foi visualizado!");
		
		// Clicar em "Visualizar Movimentações"
		operacoesDocumentoPage = visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}
	
	@Test(enabled = true, priority = 4, dependsOnMethods = {"juntar"})	
	public void cancelarJuntada() {
		// Acessar o documento juntado, por meio do link existente no TR do evento de juntada
/*		WebElement linkDocumentoJuntado = util.getClickableElement(driver, By.partialLinkText(codigoDocumento));
		linkDocumentoJuntado.click();*/		
		operacoesDocumentoPage.clicarLinkDocumentoJuntado(codigoDocumento);
		
		// Clicar em "Desentranhar"
		/**
		 * Desentranhar diferente?????
		 */
		CancelamentoJuntadaPage cancelamentoJuntadaPage = operacoesDocumentoPage.clicarLinkDesentranharDigital();
		
		// Se o documento for digital, informar um motivo qualquer 
		operacoesDocumentoPage = cancelamentoJuntadaPage.cancelarJuntada(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isDocumentoJuntadoInvisivel(), "Evento de juntada continua visível!");

		validaDesentranhamento(codigoProcesso);
	}
	
	@Test(enabled = true, priority = 3, dependsOnMethods = {"assinarDocumentoDigitalmente"})
	public void anexarArquivoProcesso() {
		String nomeArquivo = propDocumentos.getProperty("arquivoAnexo");
		super.anexarArquivo(nomeArquivo);
		
		// Se o documento for digital, garantir que a String "Anexo Pendente de Assinatura/Conferência" apareça na tela
		
		Assert.assertNotNull(operacoesDocumentoPage.isEstadoAtualVolume("Anexo Pendente Assinatura/Conferência"), "Texto 'Anexo Pendente de Assinatura/Conferência' não foi encontrado!");
		
		// Clicar em "Visualizar Dossiê"
		VisualizacaoDossiePage visualizacaoDossiePage = operacoesDocumentoPage.clicarLinkVisualizarDossie();
		
		// Garantir que o nome do anexo apareça na tela (é a seção OBJETO, da capa do processo)
		//String documentoDossie = nomeArquivo.substring(0, nomeArquivo.indexOf(".")).toLowerCase();
		String documentoDossie = nomeArquivo.toLowerCase();
		
		Assert.assertTrue(visualizacaoDossiePage.visualizaConteudoAnexo(documentoDossie), "O número da página não foi visualizado!");
		
		// Clicar em "Visualizar Movimentações"
		visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}	
	
	@Test(enabled = true, priority = 4, dependsOnMethods = {"anexarArquivoProcesso"})
	public void assinarAnexo() {
		super.assinarAnexo(codigoDocumento);
		
		Assert.assertTrue(operacoesDocumentoPage.isPendenciaAssinaturaInvisivel(),
				"Texto 'Anexo Pendente de Assinatura/Conferência' ainda está visível!");
	}
	
	@Test(enabled = false, priority = 5)
	public void cancelarAnexo() {
		super.cancelarAnexo();
		
		// Se o documento for eletrônico, garantir que o texto "CERTIDÃO DE DESENTRANHAMENTO" e o nome do subscritor escolhido no cancelamento apareçam na tela
		VisualizacaoDossiePage visualizacaoDossiePage = PageFactory.initElements(driver, VisualizacaoDossiePage.class);		
		Assert.assertTrue(visualizacaoDossiePage.visualizaCertidaoDesentranhamento(propDocumentos.getProperty("nomeResponsavel").toUpperCase()), 
				"Certidão de desentranhamento ou nome do subscritor não encontrado!");
		
		// Clicar em "Visualizar Movimentações"
		visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}
	
	@Test(enabled = true, priority = 6, dependsOnMethods = {"assinarDocumentoDigitalmente"})
	public void encerrarVolume() {
		super.encerrarVolume();
		
		// Clicar em "Ver/Assinar" (no mesmo <tr> do evento Encerramento de Volume).  - Garantir que o texto "encerrei o volume 1" apareça na tela 
		Assert.assertTrue(operacoesDocumentoPage.clicarAssinarEncerramentoVolume(), "Texto 'encerrei o volume' não foi visualizado!");		
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