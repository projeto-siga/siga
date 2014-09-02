package br.gov.jfrj.siga.integration.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.AgendamentoPublicacaoPage;
import br.gov.jfrj.siga.page.objects.AnexoPage;
import br.gov.jfrj.siga.page.objects.AnotacaoPage;
import br.gov.jfrj.siga.page.objects.ApensacaoPage;
import br.gov.jfrj.siga.page.objects.AssinaturaDigitalPage;
import br.gov.jfrj.siga.page.objects.CancelamentoMovimentacaoPage;
import br.gov.jfrj.siga.page.objects.DefinePerfilPage;
import br.gov.jfrj.siga.page.objects.DesapensamentoPage;
import br.gov.jfrj.siga.page.objects.InclusaoCossignatarioPage;
import br.gov.jfrj.siga.page.objects.OficioPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.RedefineNivelAcessoPage;
import br.gov.jfrj.siga.page.objects.RegistraAssinaturaManualPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;
import br.gov.jfrj.siga.page.objects.VinculacaoPage;
import br.gov.jfrj.siga.page.objects.VisualizacaoDossiePage;

public class AcoesDocumentoIT extends IntegrationTestBase {
	private PrincipalPage principalPage;
	private OperacoesDocumentoPage operacoesDocumentoPage;
	private Properties propDocumentos = new Properties();
	private String codigoDocumento;
	
	@Parameters({ "baseURL", "login", "password" })
	public AcoesDocumentoIT(String baseURL, String login, String password) {
		super(baseURL, login, password);
		// TODO Auto-generated constructor stub
	}
	
	@BeforeClass	
	@Parameters("infoDocumentos")
	public void setUp(String infoDocumentos) {
		File file = new File(infoDocumentos);
		try {
			efetuaLogin();
			propDocumentos.load(new FileInputStream(file));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
			
			codigoDocumento = "TMP-5092";
			OficioPage oficioPage = PageFactory.initElements(driver, OficioPage.class);
			principalPage.clicarBotaoNovoDocumentoEx();
			oficioPage.criaOficio(propDocumentos);
			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");

								
			System.out.println("Código do documento: " + codigoDocumento);
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		} 
	}
	
	@BeforeMethod
	public void paginaInicial() {
		try {
			System.out.println("BeforeMethod... Titulo página: " + driver.getTitle());
			if(!driver.getCurrentUrl().contains("exibir.action")) {
				System.out.println("Efetuando busca!");
				driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled = true)
	public void duplicarDocumento() {
		operacoesDocumentoPage.clicarLinkDuplicar();
		operacoesDocumentoPage.clicarLinkExcluir();
		Assert.assertTrue(driver.getTitle().equals("SIGA - Página Inicial"), "A ação não direcionou para a página inicial!");
	}
	
	@Test(enabled = true)
	public void incluiCossignatario() {
		operacoesDocumentoPage.clicarLinkIncluirCossignatario();
		InclusaoCossignatarioPage inclusaoCossignatarioPage = PageFactory.initElements(driver, InclusaoCossignatarioPage.class);
		inclusaoCossignatarioPage.incluiCossignatario(propDocumentos);
		Assert.assertTrue(operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/div/table/tbody/tr/td[4]/a/span").contains("Markenson"));
		operacoesDocumentoPage.excluirCossignatario();		
		Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("/html/body/div[4]/div/div/table/tbody/tr/td[4]/a/span"))));
	}
	
	@Test(enabled = true, priority = 2)
	public void anexarArquivo() {
		operacoesDocumentoPage.clicarLinkAnexarArquivo();
		AnexoPage anexoPage = PageFactory.initElements(driver, AnexoPage.class);
		anexoPage.anexarArquivo(propDocumentos);
		Assert.assertTrue(new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.linkText("teste.pdf"))) != null);
		anexoPage.clicarBotaovoltar();
	}
	
	@Test(enabled = true, priority = 3)
	public void assinarAnexo() {
		operacoesDocumentoPage.clicarAssinarCopia(baseURL, codigoDocumento);	
		Assert.assertTrue(new WebDriverWait(driver, 15).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//td[7][contains(., 'Assinado por')]"), "Assinado por")));
	}
	
	@Test(enabled = true, priority = 1)
	public void finalizarDocumento() {
		operacoesDocumentoPage.clicarLinkFinalizar();
		Assert.assertTrue(operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h3[1]")
				.contains("Pendente de Assinatura, Como Subscritor"), "Texto Pendente de Assinatura, Como Subscritor não foi encontrado!");		 
	}
	
	@Test(enabled = true, priority = 3)
	public void fazerAnotacao() {
		operacoesDocumentoPage.clicarLinkFazerAnotacao();
		AnotacaoPage anotacaoPage = PageFactory.initElements(driver, AnotacaoPage.class);
		anotacaoPage.fazerAnotacao(propDocumentos);
		WebElement element = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Teste de anotação')]")));
		System.out.println("Element text: " + element.getText());
	}
	
	@Test(enabled = false, priority = 5)
	public void redefineNivelAcesso() {
		operacoesDocumentoPage.clicarLinkRedefinirNivelAcesso();
		RedefineNivelAcessoPage redefineNivelAcessoPage = PageFactory.initElements(driver, RedefineNivelAcessoPage.class);
		redefineNivelAcessoPage.redefineNivelAcesso(propDocumentos);
		operacoesDocumentoPage.clicarLinkDesfazerRedefinicaoSigilo();
		WebElement nivelAcesso = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[2][contains(., 'Nível de Acesso:')]")));
		System.out.println("Nível de acesso: " + nivelAcesso.getText());
		Assert.assertTrue(nivelAcesso.getText().contains("Público"), "Texto Público não encontrado!");
	}
	
	@Test(enabled = true, priority = 3)
	public void definirPerfil() throws InterruptedException {
		operacoesDocumentoPage.clicarLinkDefinirPerfil();
		DefinePerfilPage definePerfilPage = PageFactory.initElements(driver, DefinePerfilPage.class);
		definePerfilPage.definirPerfil(propDocumentos);
		WebElement definicaoPerfil = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Interessado:')]")));
		System.out.println(definicaoPerfil.getText());
		Assert.assertTrue(definicaoPerfil.getText().contains(propDocumentos.getProperty("nomeResponsavel")), "Nome do usuário responsável não encontrado!");	
		definicaoPerfil.findElement(By.linkText("Cancelar")).click();
		
		CancelamentoMovimentacaoPage cancelamentoMovimentacaoPage = PageFactory.initElements(driver, CancelamentoMovimentacaoPage.class);
		cancelamentoMovimentacaoPage.cancelarMovimentacao(propDocumentos);
		Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[7][contains(., 'Interessado:')]"))));
	}
	
	@Test(enabled = true, priority = 2)
	public void criarVia() {
		operacoesDocumentoPage.clicarCriarVia();
		operacoesDocumentoPage.clicarCancelarVia();
		WebElement via = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//h3[contains(.,'Via - Cancelado')])"))); 
		System.out.println("Via: " + via.getText());
		Assert.assertTrue(via.getText().contains("Cancelado"), "Texto Cancelado não encontrado");
	}
	
	@Test(enabled = true, priority = 3)
	public void registarAssinaturaManual() {
		operacoesDocumentoPage.clicarLinkRegistrarAssinaturaManual();
		RegistraAssinaturaManualPage registraAssinaturaManualPage = PageFactory.initElements(driver, RegistraAssinaturaManualPage.class);
		registraAssinaturaManualPage.registarAssinaturaManual();
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));		
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Registro de Assinatura')]")));
	}
	
	@Test(enabled = true, priority = 3)
	public void assinarDigitalmente() {
		operacoesDocumentoPage.clicarLinkAssinarDigitalmente();
		AssinaturaDigitalPage assinaturaDigitalPage = PageFactory.initElements(driver, AssinaturaDigitalPage.class);
		assinaturaDigitalPage.registrarAssinaturaDigital(baseURL, codigoDocumento);
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));		
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Assinatura')]")));
	}
	
	@Test(enabled = true, priority = 4)
	public void agendarPublicacao() {
		operacoesDocumentoPage.clicarLinkAgendarPublicacao();
		AgendamentoPublicacaoPage agendamentoPublicacaoPage = PageFactory.initElements(driver, AgendamentoPublicacaoPage.class);
		agendamentoPublicacaoPage.visualizaPagina();
	}
	
	@Test(enabled = true, priority = 4)
	public void solicitarPublicacaoBoletim() {
		operacoesDocumentoPage.clicarLinkSolicitarPublicacaoBoletim();
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Solicitação de Publicação no Boletim')]")));		
		operacoesDocumentoPage.clicarLinkDesfazerSolicitacaoPublicacaoBoletim();
		new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[2][contains(., 'Solicitação de Publicação no Boletim')]")));
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(By.linkText("Solicitar Publicação no Boletim")));
	}
	
	@Test(enabled = true, priority = 4)
	public void sobrestar() {
		operacoesDocumentoPage.clicarLinkSobrestar();
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Sobrestado')]")));	
		operacoesDocumentoPage.clicarLinkDesobrestar();
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));	
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Desobrestar')]")));
	}
	
	@Test(enabled = true, priority = 4)
	public void vincularDocumento() {
		operacoesDocumentoPage.clicarLinkVincular();
		VinculacaoPage vinculacaoPage = PageFactory.initElements(driver, VinculacaoPage.class);
		vinculacaoPage.vincularDocumento(propDocumentos, codigoDocumento);
		WebElement vinculacao = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Ver também:')]")));
		System.out.println("Vinculação: " + vinculacao.getText());		
		vinculacao.findElement(By.linkText("Cancelar")).click();

		CancelamentoMovimentacaoPage cancelamentoMovimentacaoPage = PageFactory.initElements(driver, CancelamentoMovimentacaoPage.class);
		cancelamentoMovimentacaoPage.cancelarMovimentacao(propDocumentos);
		Assert.assertTrue(new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[7][contains(., 'Ver também:')]"))));
	}
	
	@Test(enabled = true, priority = 4)
	public void arquivarCorrente() {
		operacoesDocumentoPage.clicarLinkArquivarCorrente();
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Arquivamento Corrente')]")));
		operacoesDocumentoPage.clicarLinkDesfazerArquivamentoCorrente();
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));	
	}
	
	@Test(enabled = true, priority = 4)
	public void apensarDocumento() {
		operacoesDocumentoPage.clicarLinkApensar();
		ApensacaoPage apensacaoPage = PageFactory.initElements(driver, ApensacaoPage.class);
		apensacaoPage.apensarDocumento(propDocumentos, codigoDocumento);
		WebElement apensacao = new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Apensado ao documento')]")));
		System.out.println("Apensação: " + apensacao.getText());

		operacoesDocumentoPage.clicarLinkDesapensar();
		DesapensamentoPage desapensamentoPage = PageFactory.initElements(driver, DesapensamentoPage.class);
		desapensamentoPage.desapensarDocumento(propDocumentos);
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));	
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Desapensado do documento')]")));
	}
	
	@Test(enabled = true, priority = 4)
	public void despacharDocumento() {
		operacoesDocumentoPage.clicarLinkDespacharTransferir();
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		transferenciaPage.despacharDocumento(propDocumentos);
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., '" + propDocumentos.getProperty("despacho") + "')]")));
	}
	
	@Test(enabled = true, priority = 5)
	public void assinarDespacho() {
		operacoesDocumentoPage.clicarAssinarDespacho(baseURL, codigoDocumento);
		new WebDriverWait(driver, 15).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//td[7][contains(., 'Assinado por')]"), "Assinado por"));
	}
	  
	@Test(enabled = true, priority = 4)
	public void transferirDocumento() {
		operacoesDocumentoPage.clicarLinkDespacharTransferir();
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		transferenciaPage.transferirDocumento(propDocumentos);
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'A Receber (Físico)')]")));	
		
		operacoesDocumentoPage.clicarProtocolo();
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));	
	}
	
	@Test(enabled = true, priority = 4)
	public void despachoDocumentoFilho() {
		operacoesDocumentoPage.clicarLinkDespacharTransferir();
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		String codigoDocumentoJuntado = transferenciaPage.despachoDocumentoFilho(propDocumentos, codigoDocumento);
		
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., 'Documento juntado:')]")));		
	}
	
	@Test(enabled = true, priority = 4)
	public void visualizarDossie() {
		operacoesDocumentoPage.clicarLinkVisualizarDossie();
		VisualizacaoDossiePage visualizacaoDossiePage = PageFactory.initElements(driver, VisualizacaoDossiePage.class);
		visualizacaoDossiePage.visualizarDossie();
	}
	
	public void visualizarImpressao() {
		operacoesDocumentoPage.clicarLinkVisualizarImpressao();		
	}
	
	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}
}