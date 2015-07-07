package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
//Biliotecas para o saucelabs
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.AgendamentoPublicacaoPage;
import br.gov.jfrj.siga.page.objects.AnotacaoPage;
import br.gov.jfrj.siga.page.objects.ApensacaoPage;
import br.gov.jfrj.siga.page.objects.CancelamentoMovimentacaoPage;
import br.gov.jfrj.siga.page.objects.DefinePerfilPage;
import br.gov.jfrj.siga.page.objects.DesapensamentoPage;
import br.gov.jfrj.siga.page.objects.InclusaoCossignatarioPage;
import br.gov.jfrj.siga.page.objects.OficioPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.RedefineNivelAcessoPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;
import br.gov.jfrj.siga.page.objects.VinculacaoPage;
import br.gov.jfrj.siga.page.objects.VisualizacaoDossiePage;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
// fim saucelabs

//O listener envia o resultado do testng para o saucelab
//@Listeners({SauceOnDemandTestListener.class})
public class AcoesDocumentoIT extends IntegrationTestBase implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
	private String codigoDocumento;
	private Boolean isDocumentoTesteCriado = Boolean.FALSE;
	private PrincipalPage principalPage;
	
	public AcoesDocumentoIT() throws FileNotFoundException, IOException {
		super();
	}
	
	@BeforeClass(dependsOnMethods={"iniciaWebDriver"})
	public void setUp() {
		try {
			principalPage = efetuaLogin();					
			codigoDocumento = criaDocumento();				
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exceção no método setUp!");
		} 
	}
	
	@BeforeMethod
	public void paginaInicial(Method method) {
		try {
			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle() +
					"url: " + driver.getCurrentUrl());
			
			if((method.getName().equals("apensarDocumento") || method.getName().equals("vincularDocumento")) && !isDocumentoTesteCriado) {
				driver.get(baseURL + "/siga");
				util.getWebElement(driver, By.cssSelector("a.gt-btn-small.gt-btn-right"));
				String codigoDocumentoTeste = criaDocumento();
				super.finalizarDocumento();
				super.assinarDigitalmente(codigoDocumentoTeste, "Nº");	
				isDocumentoTesteCriado = Boolean.TRUE;
				driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);	
				operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
			} else if(!driver.getCurrentUrl().contains("exibir.action") || driver.getTitle().contains("SIGA - Erro Geral")) {
				System.out.println("Efetuando busca!");
				driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);		
				operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
			}
			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled = true)
	public void duplicarDocumento() {
		OperacoesDocumentoPage documentoDuplicadoPage = operacoesDocumentoPage.clicarLinkDuplicar();
		principalPage = documentoDuplicadoPage.clicarLinkExcluir();
		Assert.assertTrue(driver.getTitle().equals("SIGA - Página Inicial"), "A ação não direcionou para a página inicial!");
	}
	
	@Test(enabled = true)
	public void incluiCossignatario() {
		InclusaoCossignatarioPage inclusaoCossignatarioPage = operacoesDocumentoPage.clicarLinkIncluirCossignatario();		
		operacoesDocumentoPage = inclusaoCossignatarioPage.incluiCossignatario(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isCossignatarioVisivel(propDocumentos.getProperty("nomeCossignatario")), "Nome do cossignatário não encontrado!");
		operacoesDocumentoPage = operacoesDocumentoPage.excluirCossignatario(propDocumentos.getProperty("nomeCossignatario"));	
		Assert.assertTrue(operacoesDocumentoPage.isCossignatarioInvisivel(propDocumentos.getProperty("nomeCossignatario")), "Nome do cossignatário continua aparecendo na tela!");
	}
	
	@Test(enabled = true, priority = 2)
	public void anexarArquivo() {
		super.anexarArquivo(propDocumentos.getProperty("arquivoAnexo"));
	}
	
	@Test(enabled = true, priority = 3)
	public void assinarAnexo() {
		super.assinarAnexo(codigoDocumento);
	}
	
	@Test(enabled = true, priority = 1)
	public void finalizarDocumento() {
		super.finalizarDocumento(); 
	}
	
	@Test(enabled = true, priority = 3)
	public void fazerAnotacao() {
		AnotacaoPage anotacaoPage = operacoesDocumentoPage.clicarLinkFazerAnotacao();
		operacoesDocumentoPage = anotacaoPage.fazerAnotacao(propDocumentos);
		String nota = propDocumentos.getProperty("nota");
		Assert.assertTrue(operacoesDocumentoPage.isAnotacaoVisivel(nota), "Conteúdo da anotação não encontrado!");
/*		operacoesDocumentoPage.excluirAnotacao(nota);
		Assert.assertTrue(operacoesDocumentoPage.isAnotacaoInvisivel(nota), "Anotação continua sendo exibida");*/
	}
	
	@Test(enabled = true, priority = 5)
	public void redefineNivelAcesso() {
		RedefineNivelAcessoPage redefineNivelAcessoPage = operacoesDocumentoPage.clicarLinkRedefinirNivelAcesso();		
		operacoesDocumentoPage = redefineNivelAcessoPage.redefineNivelAcesso(propDocumentos);
		Assert.assertNotNull(operacoesDocumentoPage.isNivelAcessoModificado(propDocumentos.getProperty("nivelAcesso")), "Nível de acesso não foi modificado para público");		
/*		operacoesDocumentoPage.clicarLinkDesfazerRedefinicaoSigilo();		
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("(//p/b[contains(.,'Público')])")), "Nível de acesso não foi modificado para público");*/
	}
	
	@Test(enabled = true, priority = 3)
	public void definirPerfil() throws InterruptedException {
		DefinePerfilPage definePerfilPage = operacoesDocumentoPage.clicarLinkDefinirPerfil();
		operacoesDocumentoPage = definePerfilPage.definirPerfil(propDocumentos);
		
		Assert.assertTrue(operacoesDocumentoPage.isPerfilVisivel( propDocumentos.getProperty("perfil"), propDocumentos.getProperty("nomeResponsavel")));
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkDesfazerDefinicaoPerfil();
		Assert.assertTrue(operacoesDocumentoPage.isPerfilInvisivel(propDocumentos.getProperty("perfil")), "Texto " + propDocumentos.getProperty("perfil") + " continua visível!");
	}
	
	@Test(enabled = false, priority = 2)
	public void criarVia() {
		operacoesDocumentoPage.clicarCriarVia();
		WebElement divVias = util.getContentDiv(driver, By.cssSelector("div.gt-sidebar-content"), "Vias");		
		Assert.assertNotNull(divVias, "Texto 'Vias' não encontrado!");
		
		List<WebElement> listItems = divVias.findElements(By.tagName("li"));
		int i;
		for (i = 1; i < listItems.size(); i++) {
			WebElement listItem = listItems.get(i);
			if(!listItem.getText().contains("Cancelado")) {
				util.getClickableElement(driver, listItem.findElement(By.tagName("a"))).click();
				break;
			}
		}
		operacoesDocumentoPage.clicarCancelarVia();
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("(//ul/li["+ (i+1) +"][contains(.,'Cancelado')])")), "Texto Cancelado não encontrado!");
	}
	
	@Test(enabled = true, priority = 3)
	public void registrarAssinaturaManual() {		
		super.registrarAssinaturaManual();
	}
	
	@Test(enabled = true, priority = 3)
	public void assinarDigitalmente() {
		super.assinarDigitalmente(codigoDocumento, "Nº");
		//Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[2][contains(., 'Assinatura')]")), "Texto 'Assinatura' não encontrado!");
	}
	
	@Test(enabled = true, priority = 4)
	public void agendarPublicacao() {
		AgendamentoPublicacaoPage agendamentoPublicacaoPage = operacoesDocumentoPage.clicarLinkAgendarPublicacao();
		operacoesDocumentoPage = agendamentoPublicacaoPage.visualizaPagina();
		Assert.assertNotNull(operacoesDocumentoPage, "Não foi possível visualizar os botões da página de agendamento corretamente!");
	}
	
	// Rever amanhã!
	@Test(enabled = true, priority = 4)
	public void solicitarPublicacaoBoletim() {		
		if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 17) {
			Assert.assertTrue(operacoesDocumentoPage.clicarLinkSolicitarPublicacaoBoletimPos17Horas(),
					"Texto 'A solicitação de publicação no BIE apenas é permitida até as 17:00' não foi encontrado!");
		} else {
			operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkSolicitarPublicacaoBoletimPre17Horas();
			Assert.assertTrue(operacoesDocumentoPage.isSolicitacaoPublicacaoBoletimVisivel(), "Texto 'Solicitação de Publicação no Boletim' não foi encontrado!");		
			operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkDesfazerSolicitacaoPublicacaoBoletim();
			Assert.assertTrue(operacoesDocumentoPage.isSolicitacaoPublicacaoBoletimInvisivel(), 
					"Texto 'Solicitação de Publicação no Boletim' continua sendo exibido!");
			Assert.assertNotNull(operacoesDocumentoPage.getLinkSolicitarPublicacaoBoletim(), "Texto Solicitar Publicação no Boletim não foi encontrado!");		
		}
	}
	
	@Test(enabled = true, priority = 4)
	public void sobrestar() {
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkSobrestar();
		Assert.assertTrue(operacoesDocumentoPage.isEstadoAtualDocumento("Sobrestado"), "Texto 'Sobrestado' não encontrado!");	
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkDesobrestar();
		Assert.assertTrue( operacoesDocumentoPage.isEstadoAtualDocumento("Aguardando Andamento"), "Texto 'Aguardando Andamento' não encontrado!");	
		//Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[2][contains(., 'Desobrestar')]")), "Texto 'Desobrestar' não encontrado!");
	}
	
	@Test(enabled = true, priority = 4)
	public void vincularDocumento() {
		VinculacaoPage vinculacaoPage = operacoesDocumentoPage.clicarLinkVincular();		
		String documentoApensado = vinculacaoPage.vincularDocumento(propDocumentos, codigoDocumento);

		Assert.assertTrue(operacoesDocumentoPage.isDocumentoConectadoVisivel(documentoApensado), "Código do documento vinculado não foi encontrado!");
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkExibirInformacoesCompletas();
		
		CancelamentoMovimentacaoPage cancelamentoMovimentacaoPage = operacoesDocumentoPage.cancelarVinculoDocumento();		
		cancelamentoMovimentacaoPage.cancelarMovimentacao(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isDocumentoConectadoInvisivel());
	}
	
	@Test(enabled = true, priority = 4)
	public void arquivarCorrente() {
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkArquivarCorrente();
		Assert.assertTrue(operacoesDocumentoPage.isEstadoAtualDocumento("Arquivo Corrente"), "Texto Arquivado Corrente não foi encontrado!");
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkDesfazerArquivamentoCorrente();
		Assert.assertTrue(operacoesDocumentoPage.isEstadoAtualDocumento("Aguardando Andamento"), "Texto 'Aguardando Andamento' não foi encontrado!");	
	}
	
	@Test(enabled = true, priority = 4)
	public void apensarDocumento() {
		ApensacaoPage apensacaoPage = operacoesDocumentoPage.clicarLinkApensar();
		String documentoApensado = apensacaoPage.apensarDocumento(propDocumentos, codigoDocumento);
		WebElement documentosRelacionados = util.getWebElement(driver, By.id("outputRelacaoDocs"));		
		Assert.assertTrue(documentosRelacionados.getText().contains(documentoApensado), "Código do documento apensado não foi encontrado!");

		DesapensamentoPage desapensamentoPage = operacoesDocumentoPage.clicarLinkDesapensar();
		operacoesDocumentoPage = desapensamentoPage.desapensarDocumento(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isEstadoAtualDocumento("Aguardando Andamento"), "Texto 'Aguardando Andamento' não foi encontrado!");	
		Assert.assertTrue(operacoesDocumentoPage.isDocumentoConectadoInvisivel(), "Área de Documentos Relacionados ainda está visível!");	
	}
	
	@Test(enabled = true, priority = 4)
	public void despacharDocumento() {
		TransferenciaPage transferenciaPage = operacoesDocumentoPage.clicarLinkDespacharTransferir();
		operacoesDocumentoPage = transferenciaPage.despacharDocumento(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.isDespachoVisivel(propDocumentos.getProperty("despacho")), "Texto do despacho não encontrado!");
	}
	
	@Test(enabled = true, priority = 5)
	public void assinarDespacho() {
		operacoesDocumentoPage = operacoesDocumentoPage.clicarAssinarDespacho(baseURL, codigoDocumento);
		Assert.assertTrue(operacoesDocumentoPage.isDespachoAssinado(), "Texto 'Assinado por' não foi encontrado!");
	}
	  
	@Test(enabled = true, priority = 4)
	public void transferirDocumento() {
		TransferenciaPage transferenciaPage = operacoesDocumentoPage.clicarLinkDespacharTransferir();
		operacoesDocumentoPage = transferenciaPage.transferirDocumento(propDocumentos);
		Assert.assertNotNull(operacoesDocumentoPage.isEstadoAtualDocumento("A Receber (Físico)"), "Texto 'A Receber (Físico)' não foi encontrado!");	
		
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkExibirInformacoesCompletas();
		operacoesDocumentoPage = operacoesDocumentoPage.clicarProtocolo();
		operacoesDocumentoPage = operacoesDocumentoPage.clicarLinkDesfazerTransferencia();
		Assert.assertNotNull(operacoesDocumentoPage.isEstadoAtualDocumento("Aguardando Andamento"), "Texto 'Aguardando Andamento' não foi encontrado!");	
	}
	
	@Test(enabled = true, priority = 4)
	public void despachoDocumentoFilho() {
		TransferenciaPage transferenciaPage = operacoesDocumentoPage.clicarLinkDespacharTransferir();
		
		String codigoDocumentoJuntado = transferenciaPage.despachoDocumentoFilho(propDocumentos, codigoDocumento);
		Assert.assertTrue(operacoesDocumentoPage.isDocumentoJuntadoVisivel(codigoDocumentoJuntado), "Código do documento juntado não encontrado!");
/*		WebElement juntada = util.getWebElement(driver, By.xpath("//td[4][contains(., 'Documento juntado:')]"));
		
		Assert.assertNotNull(juntada, "Texto 'Documento juntado:' não foi encontrado!");	
		Assert.assertTrue(juntada.getText().contains(codigoDocumentoJuntado), "Código do documento juntado não encontrado!");	*/
	}
	
	@Test(enabled = false, priority = 4)
	public void visualizarDossie() {
		VisualizacaoDossiePage visualizacaoDossiePage = operacoesDocumentoPage.clicarLinkVisualizarDossie();
		Assert.assertTrue(visualizacaoDossiePage.visualizarDossie(), "Texto 'DESPACHO Nº' não foi encontrado");
	}
		
	public String criaDocumento() {
		principalPage.clicarBotaoNovoDocumentoEx();
		OficioPage oficioPage = PageFactory.initElements(driver, OficioPage.class);
		operacoesDocumentoPage = oficioPage.criaOficio(propDocumentos);		
		
		return operacoesDocumentoPage.getTextoVisualizacaoDocumento();
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