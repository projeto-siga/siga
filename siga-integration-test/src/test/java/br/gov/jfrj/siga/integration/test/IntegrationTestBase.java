package br.gov.jfrj.siga.integration.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;
import br.gov.jfrj.siga.page.objects.AnexoPage;
import br.gov.jfrj.siga.page.objects.AssinaturaAnexoPage;
import br.gov.jfrj.siga.page.objects.AssinaturaDigitalPage;
import br.gov.jfrj.siga.page.objects.CancelamentoMovimentacaoPage;
import br.gov.jfrj.siga.page.objects.LoginPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.ProcessoAssuntosAdministrativosPage;
import br.gov.jfrj.siga.page.objects.RegistraAssinaturaManualPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;

public class IntegrationTestBase {
	protected WebDriver driver;
	protected OperacoesDocumentoPage operacoesDocumentoPage;
	protected IntegrationTestUtil util;
	protected String baseURL;
	protected Properties propDocumentos = new Properties();

	public IntegrationTestBase() throws FileNotFoundException, IOException {
		this.baseURL = System.getProperty("baseURL");
		util = new IntegrationTestUtil();
		File file = new File(System.getProperty("infoDocumentos"));
		propDocumentos.load(new FileInputStream(file));
		propDocumentos.setProperty("siglaSubscritor", System.getProperty("userSiga"));
	}

	public void efetuaLogin() {
		try {
			driver = new InternetExplorerDriver();
			driver.get(baseURL + "/siga");
			driver.manage().window().maximize();
			LoginPage loginPage = PageFactory.initElements(driver,	LoginPage.class);
			loginPage.login(System.getProperty("userSiga"), System.getProperty("passSiga"));
			util.getWebElement(driver, By.cssSelector("a.gt-btn-small.gt-btn-right"));
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
	}

	public void efetuaLogout() {
		try {
			IntegrationTestUtil util = new IntegrationTestUtil();
			WebElement linkSair = util.getWebElement(driver, By.linkText("sair"));
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(linkSair));
			linkSair.click();
			util.getWebElement(driver, By.id("j_username"));
		} catch (Exception e) {
			System.out.println("Erro ao efetuar logout!");
		} finally {
			driver.quit();
		}
	}

	public void assinarAnexo(String codigoDocumento) {
		// Clicar em "Assinar/Autenticar"
		operacoesDocumentoPage.clicarLinkAssinarCopia();

		// Garantir que a String "Link para assinatura externa" apareça na tela - Assinar anexo
		AssinaturaAnexoPage assinaturaAnexoPage = PageFactory.initElements(driver, AssinaturaAnexoPage.class);
		assinaturaAnexoPage.assinarCopia(baseURL, codigoDocumento);
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[4][contains(., 'Assinado por')]")), "O texto 'Assinado por' não foi encontrado!");
	}

	public void autuar(Boolean isDigital, String modeloDocumento){
		operacoesDocumentoPage.clicarLinkAutuar();
		ProcessoAssuntosAdministrativosPage processoAssuntosAdministrativosPage = PageFactory.initElements(driver, ProcessoAssuntosAdministrativosPage.class);
		processoAssuntosAdministrativosPage.criaProcesso(propDocumentos, isDigital, modeloDocumento);
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//b[contains(., 'Processo Nº')]")), "Texto 'Processo Nº' não foi encontrado!");
	}

	public void finalizarProcesso() {
		operacoesDocumentoPage.clicarLinkFinalizar();

		Assert.assertNotNull(util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO +
		 "[contains(text(), '1º Volume - Pendente de Assinatura, Como Subscritor')]|//div[h3 = 'Volumes']/ul/li[contains(., 'Pendente de Assinatura') and contains(., 'Como Subscritor')]")), "Texto '1º Volume - Pendente de Assinatura, Como Subscritor' não foi encontrado!");
	}

	public void finalizarDocumento() {
		operacoesDocumentoPage.clicarLinkFinalizar();

		Assert.assertNotNull(util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO +
				 "[contains(text(), 'Pendente de Assinatura, Como Subscritor')]|//div[h3 = 'Vias']/ul/li[contains(., 'Pendente de Assinatura') and contains(., 'Como Subscritor')]")), "Texto Pendente de Assinatura, Como Subscritor não foi encontrado!");
	}

	public void validaDesentranhamento(String codigoProcesso) {
		// Clicar em Exibir Informações completas
		operacoesDocumentoPage.clicarLinkExibirInformacoesCompletas();

		// Acessar novamente o processo, pelo link existente na linha do evento de juntada
		WebElement desentranhamentoDocumento = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'desentranhamento ')]"));
		Assert.assertNotNull(desentranhamentoDocumento, "Evento de desentranhamento não encontrado!");
		WebElement linkProcessoDesentranhado = util.getWebElement(driver, desentranhamentoDocumento, By.partialLinkText(codigoProcesso));
		linkProcessoDesentranhado.click();

		// Clicar em Exibir informações completas
		operacoesDocumentoPage.clicarLinkExibirInformacoesCompletas();

		// Garantir que o texto "Desentranhamento" apareça na tela
		WebElement desentranhamentoProcesso = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'desentranhamento ')]"));
		Assert.assertNotNull(desentranhamentoProcesso, "Evento de desentranhamento não encontrado!");
	}

	public void cancelarAnexo() {
		// Clicar em "Cancelar" (link no <tr> do evento de anexação)
		operacoesDocumentoPage.clicarLinkCancelarAnexo();

		// Informar um motivo qualquer e um subscritor qualquer, diferente do usuário de teste
		CancelamentoMovimentacaoPage cancelamentoMovimentacaoPage = PageFactory.initElements(driver, CancelamentoMovimentacaoPage.class);
		cancelamentoMovimentacaoPage.cancelarMovimentacao(propDocumentos);

		// Garantir que o nome do anexo não apareça mais na tela
		String nomeArquivo = propDocumentos.getProperty("arquivoAnexo");
		Assert.assertTrue(util.isElementInvisible(driver, By.linkText(nomeArquivo.toLowerCase())), "Nome do arquivo continua visível na tela!");

		// Clicar "Visualizar Dossiê"
		operacoesDocumentoPage.clicarLinkVisualizarDossie();

		// Garantir que o nome do anexo não apareça mais na tela
		Assert.assertTrue(util.isElementInvisible(driver, By.linkText(nomeArquivo.substring(0, nomeArquivo.indexOf(".")).toLowerCase())),
				"Nome do anexo continua visível na visualização de dossiê!");
	}

	public void encerrarVolume() {
		// Clicar em "Encerrar Volume"
		operacoesDocumentoPage.clicarLinkEncerrarVolume();

		// Garantir que o texto "Encerramento de Volume" apareça na tela.
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[3][contains(text(),'Encerramento de Volume')]")));

		// Clicar em "Despachar/Transferir"
		operacoesDocumentoPage.clicarLinkDespacharTransferir();

		// Selecionar um despacho qualquer - Clicar "OK" - Garantir que o texto "Não é permitido" apareça na tela - Fechar a popup
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		Assert.assertFalse(transferenciaPage.despacharVolumeEncerrado(propDocumentos), "O despacho de volume encerrado foi permitido!");
	}

	public void anexarArquivo(String nomeArquivo) {
		// Clicar no link "Anexar Arquivo"
		operacoesDocumentoPage.clicarLinkAnexarArquivo();
		util.getWebElement(driver, By.xpath("//h2[contains(text(), 'Anexação de Arquivo')]"));
		// Clicar "OK" - Selecionar um arquivo qualquer - Clicar "OK"
		AnexoPage anexoPage = PageFactory.initElements(driver, AnexoPage.class);
		anexoPage.anexarArquivo(propDocumentos);

		// Garantir que o nome do arquivo selecionado apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.linkText(nomeArquivo.toLowerCase())), "Nome do arquivo selecionado não encontrado na tela!");

		// Clicar em voltar
		anexoPage.clicarBotaovoltar();
	}

	public void registrarAssinaturaManual() {
		operacoesDocumentoPage.clicarLinkRegistrarAssinaturaManual();
		RegistraAssinaturaManualPage registraAssinaturaManualPage = PageFactory.initElements(driver, RegistraAssinaturaManualPage.class);
		registraAssinaturaManualPage.registarAssinaturaManual();
		Assert.assertNotNull(util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + "[contains(text(), 'Aguardando Andamento')]|//div[h3 = 'Volumes' or h3 = 'Vias']/ul/li[contains(., 'Aguardando Andamento')]")), "Texto 'Aguardando Andamento' não encontrado!");
	}

	public void assinarDigitalmente(String codigoDocumento, String textoBuscado) {
		// Clicar em Assinar Digitalmente
		operacoesDocumentoPage.clicarLinkAssinarDigitalmente();

		// Garantir que a descrição do documento apareça na tela (é a seção OBJETO, da capa do processo)
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//p[contains(., '" + textoBuscado + "')]")), "Texto '" + textoBuscado + " ' não encontrado!");

		// usar o link /sigaex/app/expediente/mov/simular_assinatura?sigla=<código do documento> para gerar uma movimentação de assinatura digital
		AssinaturaDigitalPage assinaturaDigitalPage = PageFactory.initElements(driver, AssinaturaDigitalPage.class);
		assinaturaDigitalPage.registrarAssinaturaDigital(baseURL, codigoDocumento);

		// Garantir que "Aguardando Andamento" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + "[contains(text(), 'Aguardando Andamento')]|//div[h3 = 'Volumes' or h3 = 'Vias']/ul/li[contains(., 'Aguardando Andamento')]")), "Texto 'Aguardando Andamento' não encontrado!");
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		driver.quit();
	}
}
