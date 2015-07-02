package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class OperacoesDocumentoPage {
	public static final String XPATH_STATUS_DOCUMENTO = "//h3[1]";

	protected WebDriver driver;

	@FindBy(linkText="Anexar Arquivo")
	private WebElement linkAnexarArquivo;

	@FindBy(linkText="Editar")
	private WebElement linkEditar;

	@FindBy(linkText="Duplicar")
	private WebElement linkDuplicar;

	@FindBy(linkText="Excluir")
	private WebElement linkExcluir;

	@FindBy(linkText="Finalizar")
	private WebElement linkFinalizar;

	@FindBy(linkText="Incluir Cossignatário")
	private WebElement linkIncluirCossignatario;

	@FindBy(linkText="Fazer Anotação")
	private WebElement linkFazerAnotacao;

	@FindBy(linkText="Agendar Publicação no DJE")
	private WebElement linkAgendarPublicacao;

	@FindBy(linkText="Redefinir Nível de Acesso")
	private WebElement linkRedefinirNivelAcesso;

	@FindBy(linkText="Desfazer Redefinição de Sigilo")
	private WebElement linkDesfazerRedefinicaoSigilo;

	@FindBy(linkText="Definir Perfil")
	private WebElement linkDefinirPerfil;

	@FindBy(linkText="Criar Via")
	private WebElement linkCriarVia;

	@FindBy(linkText="Registrar Assinatura Manual")
	private WebElement linkRegistrarAssinaturaManual;

	@FindBy(linkText="Assinar")
	private WebElement linkAssinarDigitalmente;

	@FindBy(linkText="Solicitar Publicação no Boletim")
	private WebElement linkSolicitarPublicacaoBoletim;

	@FindBy(linkText="Desfazer Solicitação de Publicação no Boletim")
	private WebElement linkDesfazerSolicitacaoPublicacaoBoletim;

	@FindBy(linkText="Sobrestar")
	private WebElement linkSobrestar;

	@FindBy(linkText="Desobrestar")
	private WebElement linkDesobrestar;

	@FindBy(linkText="Vincular")
	private WebElement linkVincular;

	@FindBy(linkText="Arq. Corrente")
	private WebElement linkArquivarCorrente;

	@FindBy(linkText="Desfazer Arquivamento Corrente")
	private WebElement linkDesfazerArquivamentoCorrente;

	@FindBy(linkText="Apensar")
	private WebElement linkApensar;

	@FindBy(linkText="Desapensar")
	private WebElement linkDesapensar;

	@FindBy(linkText="Despachar/Transferir")
	private WebElement linkDespacharTransferir;

	@FindBy(linkText="Desfazer Transferência")
	private WebElement linkDesfazerTransferencia;

	@FindBy(linkText="Desentranhar")
	private WebElement linkDesentranhar;

	@FindBy(linkText="Juntar")
	private WebElement linkJuntar;

	@FindBy(linkText="Visualizar Dossiê")
	private WebElement linkVisualizarDossie;

	@FindBy(linkText="Visualizar Impressão")
	private WebElement linkVisualizarImpressao;

	@FindBy(linkText="Desfazer Definição de Perfil")
	private WebElement linkDesfazerDefinicaoPerfil;

	@FindBy(linkText="Exibir Informações Completas")
	private WebElement linkExibirInformacoesCompletas;

	@FindBy(linkText="Cancelar Via")
	private WebElement linkCancelarVia;

	@FindBy(linkText="Autuar")
	private WebElement linkAutuar;

	@FindBy(linkText="Encerrar Volume")
	private WebElement linkEncerrarVolume;

	@FindBy(linkText="Abrir Novo Volume")
	private WebElement linkAbrirNovoVolume;

	@FindBy(linkText="Criar Subprocesso")
	private WebElement linkCriarSubprocesso;

	@FindBy(id="buscar_genericoSel_sigla")
	private WebElement buscaGenerica;

	private IntegrationTestUtil util;

	public OperacoesDocumentoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}

	public String getTextoVisualizacaoDocumento(String xpathElemento) {
		WebElement element = null;
		try {
			element =  new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathElemento)));
			System.out.println("Texto: " + element.getText());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return (element != null ? element.getText().trim() : "");
	}

	public void excluirCossignatario() {
		driver.findElement(By.xpath("(//a[contains(text(),'Excluir')])[2]")).click();
	}

	public void clicarLinkFazerAnotacao() {
		linkFazerAnotacao.click();
	}

	public void clicarLinkEditar() {
		linkEditar.click();
	}

	public void clicarLinkIncluirCossignatario() {
		linkIncluirCossignatario.click();
	}

	public void clicarLinkAnexarArquivo() {
		linkAnexarArquivo.click();
	}

	public void clicarLinkAgendarPublicacao() {
		linkAgendarPublicacao.click();
	}

	public void clicarLinkRedefinirNivelAcesso() {
		linkRedefinirNivelAcesso.click();
	}

	public void clicarLinkDefinirPerfil() {
		linkDefinirPerfil.click();
	}

	public void clicarLinkDesfazerRedefinicaoSigilo() {
		linkDesfazerRedefinicaoSigilo.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarCriarVia() {
		linkCriarVia.click();
	}

	public void clicarLinkRegistrarAssinaturaManual() {
		linkRegistrarAssinaturaManual.click();
	}

	public void clicarLinkAssinarDigitalmente() {
		linkAssinarDigitalmente.click();
	}

	public void clicarLinkSolicitarPublicacaoBoletim() {
		linkSolicitarPublicacaoBoletim.click();
	}

	public void clicarLinkDesfazerSolicitacaoPublicacaoBoletim() {
		linkDesfazerSolicitacaoPublicacaoBoletim.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarLinkSobrestar() {
		linkSobrestar.click();
	}

	public void clicarLinkDesobrestar() {
		linkDesobrestar.click();
	}

	public void clicarLinkVincular() {
		linkVincular.click();
	}

	public void clicarLinkArquivarCorrente() {
		linkArquivarCorrente.click();
	}

	public void clicarLinkDesfazerArquivamentoCorrente() {
		linkDesfazerArquivamentoCorrente.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarLinkApensar() {
		linkApensar.click();
	}

	public void clicarLinkDesapensar() {
		linkDesapensar.click();
	}

	public void clicarLinkDespacharTransferir() {
		linkDespacharTransferir.click();
	}

	public void clicarLinkDesentranhar() {
		linkDesentranhar.click();
	}

	public void clicarlinkJuntar() {
		linkJuntar.click();
	}

	public void clicarLinkVisualizarDossie() {
		linkVisualizarDossie.click();
	}

	public void clicarLinkExibirInformacoesCompletas() {
		linkExibirInformacoesCompletas.click();
	}

	public void clicarLinkAutuar() {
		linkAutuar.click();
	}

	public void clicarLinkCriarSubprocesso() {
		linkCriarSubprocesso.click();
	}

	public void clicarLinkAbrirNovoVolume() {
		linkAbrirNovoVolume.click();
		util.closeAlertAndGetItsText(driver);
		util.getWebElement(driver,By.xpath("//h3[contains(text(),'Volumes')]"));
	}

	public void clicarLinkEncerrarVolume() {
		linkEncerrarVolume.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarLinkDesfazerDefinicaoPerfil() {
		linkDesfazerDefinicaoPerfil.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarLinkDesfazerTransferencia() {
		linkDesfazerTransferencia.click();
		util.closeAlertAndGetItsText(driver);
	}

	public Boolean clicarLinkVisualizarImpressao() {
		linkVisualizarImpressao.click();
		util.openPopup(driver);

		try {
			return util.isPDF(driver);
		} finally {
			util.closePopup(driver);
		}
	}

	public void clicarCancelarVia() {
		//WebElement element = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(text(),'Cancelar Via')])")));
		linkCancelarVia.click();
		util.closeAlertAndGetItsText(driver);
	}

	public void clicarLinkDuplicar() {
		linkDuplicar.click();
		util.closeAlertAndGetItsText(driver);
		String URL= driver.getCurrentUrl();
		new WebDriverWait(driver, 30).until(util.trocaURL(URL));
	}

	public void clicarLinkExcluir() {
		linkExcluir.click();
		util.closeAlertAndGetItsText(driver);
		new WebDriverWait(driver, 30).until(ExpectedConditions.titleIs("SIGA - Página Inicial"));
	}

	public void clicarLinkFinalizar() {
		linkFinalizar.click();
	    util.closeAlertAndGetItsText(driver);
	    new WebDriverWait(driver, 30).until(ExpectedConditions.invisibilityOfElementLocated(By.linkText("Finalizar")));
	}

	public void clicarLinkAssinarCopia() {
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Assinar/Autenticar"))).click();
	}

	public void clicarLinkCancelarAnexo() {
		WebElement anexacao = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'anexacao ')]"));
		util.getWebElement(driver, anexacao, By.linkText("Cancelar")).click();
	}

	public void efetuaBuscaDocumento(String codigoDocumento) {
		buscaGenerica.clear();
		buscaGenerica.sendKeys(codigoDocumento);
		driver.findElement(By.linkText("/html/body/div[1]/div/div[1]/div/div[2]/img")).click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Visualizar Impressão")));
	}

	public void clicarAssinarDespacho(String baseURL, String codigoDocumento) {
/*		WebElement despacho = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., '" + propDocumentos.getProperty("despacho") + "')]")));
		despacho.findElement(By.linkText("Ver/Assinar")).click();*/
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Ver/Assinar"))).click();
		String codigoDespacho;

		util.openPopup(driver);
		try {
			String urlPopup = driver.getCurrentUrl();
			System.out.println("URL: " + driver.getCurrentUrl());

			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(., 'DESPACHO N')]")));
			codigoDespacho = urlPopup.substring(urlPopup.indexOf("id=")+3, urlPopup.indexOf("&"));
			System.out.println("Código anexo: " + codigoDespacho);

		} finally {
			util.closePopup(driver);
		}

		driver.get(baseURL + "/sigaex/app/expediente/mov/simular_assinatura_mov?sigla="+ codigoDocumento + "&id="+codigoDespacho);
		System.out.println("URL: " + driver.getCurrentUrl());
	}

	public Boolean clicarAssinarEncerramentoVolume() {
		WebElement textoEncerramento = null;
		WebElement linkVerAssinar = util.getWebElement(driver, By.xpath("//tr[@class='encerramento_volume ']/td/a[contains(text(), 'Ver/Assinar')]"));
		linkVerAssinar.click();
		System.out.println("Conteúdo do elemento: " + linkVerAssinar.getText());

		util.openPopup(driver);
		try {
			textoEncerramento = util.getWebElement(driver, By.xpath("//p[contains(text(), 'encerrei o volume')]"));
		} finally {
			util.closePopup(driver);
		}

		return (textoEncerramento != null);
	}

	public void clicarProtocolo() {
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Protocolo"))).click();
		util.openPopup(driver);
		try {
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(., 'Protocolo de Transferência')]")));

		} finally {
			util.closePopup(driver);
		}
	}

}
