package br.gov.jfrj.siga.page.objects;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
		
		if(util.getWebElement(driver, By.xpath("/html/body/div[4]/div/h2")) == null ||
				util.getWebElement(driver, By.xpath("/html/body/div[4]/div/h3")) == null) {
			throw new IllegalStateException("Esta não é a página de Operações do documento!");
		}
	}
		
	public WebElement getLinkSolicitarPublicacaoBoletim() {
		return linkSolicitarPublicacaoBoletim;
	}
	
	public String getTextoVisualizacaoDocumento() {
		WebElement element = null;
		try {
			element =  new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/h2")));
			System.out.println("Texto: " + element.getText());
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		return (element != null ? element.getText().trim() : "");
	}

	public OperacoesDocumentoPage excluirCossignatario(String cossignatario) {	
		util.getWebElement(driver, By.xpath("//div[h3 = 'Cossignatários']/ul/li[contains(., '"+ cossignatario +"')]/a")).click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage excluirAnotacao(String nota) {
		util.getWebElement(driver, By.xpath("//td[4][contains(., '"+ nota +"')]/a")).click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public AnotacaoPage clicarLinkFazerAnotacao() {
		linkFazerAnotacao.click();		
		return PageFactory.initElements(driver, AnotacaoPage.class);
	}
	
	public void clicarLinkEditar() {
		linkEditar.click();
	}	
	
	public InclusaoCossignatarioPage clicarLinkIncluirCossignatario() {
		linkIncluirCossignatario.click();
		return PageFactory.initElements(driver, InclusaoCossignatarioPage.class);
	}
	
	public AnexoPage clicarLinkAnexarArquivo() {
		linkAnexarArquivo.click();
		return PageFactory.initElements(driver, AnexoPage.class);
	}
	
	public AgendamentoPublicacaoPage clicarLinkAgendarPublicacao() {
		linkAgendarPublicacao.click();
		return PageFactory.initElements(driver, AgendamentoPublicacaoPage.class);
	}
	
	public RedefineNivelAcessoPage clicarLinkRedefinirNivelAcesso() {
		linkRedefinirNivelAcesso.click();
		return PageFactory.initElements(driver, RedefineNivelAcessoPage.class);
	}
	
	public DefinePerfilPage clicarLinkDefinirPerfil() {
		linkDefinirPerfil.click();
		return PageFactory.initElements(driver, DefinePerfilPage.class);
	}

	public void clicarLinkDesfazerRedefinicaoSigilo() {
		linkDesfazerRedefinicaoSigilo.click();
		util.closeAlertAndGetItsText(driver);		
	}
	
	public void clicarCriarVia() {
		linkCriarVia.click();
	}
	
	public RegistraAssinaturaManualPage clicarLinkRegistrarAssinaturaManual() {
		linkRegistrarAssinaturaManual.click();
		return PageFactory.initElements(driver, RegistraAssinaturaManualPage.class);
	}
	
	public AssinaturaDigitalPage clicarLinkAssinarDigitalmente() {
		linkAssinarDigitalmente.click();
		
		return PageFactory.initElements(driver, AssinaturaDigitalPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkSolicitarPublicacaoBoletimPre17Horas() {
		linkSolicitarPublicacaoBoletim.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean clicarLinkSolicitarPublicacaoBoletimPos17Horas() {
		linkSolicitarPublicacaoBoletim.click();
		
		return isNotificacaoHorarioSolicitacaoVisivel();
	}
	
	public OperacoesDocumentoPage clicarLinkDesfazerSolicitacaoPublicacaoBoletim() {
		linkDesfazerSolicitacaoPublicacaoBoletim.click();
		util.closeAlertAndGetItsText(driver);
		util.isElementInvisible(driver, By.linkText("Desfazer Solicitação de Publicação no Boletim"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkSobrestar() {
		linkSobrestar.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDesobrestar() {
		linkDesobrestar.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public VinculacaoPage clicarLinkVincular() {
		linkVincular.click();
		return PageFactory.initElements(driver, VinculacaoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkArquivarCorrente() {
		linkArquivarCorrente.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDesfazerArquivamentoCorrente() {
		linkDesfazerArquivamentoCorrente.click();
		util.closeAlertAndGetItsText(driver);
		util.isElementInvisible(driver, By.linkText("Desfazer Arquivamento Corrente"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public ApensacaoPage clicarLinkApensar() {
		linkApensar.click();
		return PageFactory.initElements(driver, ApensacaoPage.class);
	}
	
	public DesapensamentoPage clicarLinkDesapensar() {
		linkDesapensar.click();
		return PageFactory.initElements(driver, DesapensamentoPage.class);
	}
	
	public TransferenciaPage clicarLinkDespacharTransferir() {
		linkDespacharTransferir.click();
		return PageFactory.initElements(driver, TransferenciaPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDesentranhar() {
		linkDesentranhar.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Desfazer Desentranhamento")));	
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public CancelamentoJuntadaPage clicarLinkDesentranharDigital() {
		linkDesentranhar.click();
		return PageFactory.initElements(driver, CancelamentoJuntadaPage.class);
	}
	
	public JuntadaDocumentoPage clicarlinkJuntar() {
		linkJuntar.click();
		return PageFactory.initElements(driver, JuntadaDocumentoPage.class);
	}
	
	public VisualizacaoDossiePage clicarLinkVisualizarDossie() {
		linkVisualizarDossie.click();
		return PageFactory.initElements(driver, VisualizacaoDossiePage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkExibirInformacoesCompletas() {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(linkExibirInformacoesCompletas));	
		linkExibirInformacoesCompletas.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public ProcessoAssuntosAdministrativosPage clicarLinkAutuar() {
		linkAutuar.click();
		return PageFactory.initElements(driver, ProcessoAssuntosAdministrativosPage.class);
	}
	
	public ProcessoFinanceiroPage clicarLinkCriarSubprocesso() {
		linkCriarSubprocesso.click();
		return PageFactory.initElements(driver, ProcessoFinanceiroPage.class);
	}

	public OperacoesDocumentoPage clicarLinkAbrirNovoVolume() {
		linkAbrirNovoVolume.click();
		util.closeAlertAndGetItsText(driver);
		util.getWebElement(driver,By.xpath("//h3[contains(text(),'Volumes')]"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkEncerrarVolume() {
		linkEncerrarVolume.click();
		util.closeAlertAndGetItsText(driver);
		util.isElementInvisible(driver, By.linkText("Encerrar Volume"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDesfazerDefinicaoPerfil() {
		linkDesfazerDefinicaoPerfil.click();
		util.closeAlertAndGetItsText(driver);
		util.isElementInvisible(driver, By.linkText("Desfazer Definição de Perfil"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDesfazerTransferencia() {
		linkDesfazerTransferencia.click();
		util.closeAlertAndGetItsText(driver);		
		util.isElementInvisible(driver, By.linkText("Desfazer Transferência"));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean clicarLinkVisualizarImpressao() {
/*		linkVisualizarImpressao.click();
		
		util.openPopup(driver);
		
		try {		
			return util.isPDF(driver);
		} finally {
			util.closePopup(driver);
		}		*/
		HttpURLConnection urlConnection = null;
		try {
			String href = linkVisualizarImpressao.getAttribute("href");
			String urlSiga = System.getProperty("baseURL") + href.substring(href.indexOf("'")+1, href.indexOf("')"));
			System.out.println("URL: " + urlSiga);
			
			urlConnection = (HttpURLConnection) new URL(urlSiga).openConnection();
			Integer responseCode = urlConnection.getResponseCode();
			System.out.println("Response code: " + responseCode);
			
			return (responseCode.equals(200) ? Boolean.TRUE : Boolean.FALSE);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(urlConnection != null) {
				try {
					urlConnection.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void clicarCancelarVia() {
		//WebElement element = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[contains(text(),'Cancelar Via')])")));
		linkCancelarVia.click();
		util.closeAlertAndGetItsText(driver);
	}
		
	public OperacoesDocumentoPage clicarLinkDuplicar() {
		linkDuplicar.click();
		util.closeAlertAndGetItsText(driver);
		String URL= driver.getCurrentUrl();
		new WebDriverWait(driver, 30).until(util.trocaURL(URL));
		
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}	
	
	public PrincipalPage clicarLinkExcluir() {
		linkExcluir.click();
		util.closeAlertAndGetItsText(driver);
		new WebDriverWait(driver, 30).until(ExpectedConditions.titleIs("SIGA - Página Inicial"));
		return PageFactory.initElements(driver, PrincipalPage.class);
	}	
	
	public OperacoesDocumentoPage clicarLinkFinalizar() {
		linkFinalizar.click();
	    util.closeAlertAndGetItsText(driver);
	    util.isElementInvisible(driver, By.linkText("Finalizar"));
	    return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}	
	
	public AssinaturaAnexoPage clicarLinkAssinarCopia() {
		util.getWebElement(driver, By.linkText("Assinar/Autenticar")).click();
		return PageFactory.initElements(driver, AssinaturaAnexoPage.class);
	}
	
	public void clicarLinkCancelarAnexo() {
		WebElement anexacao = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'anexacao ')]"));
		util.getWebElement(driver, anexacao, By.linkText("Cancelar")).click();
	}
	
	public OperacoesDocumentoPage clicarLinkDocumentoJuntado(String codigoDocumento) {
		WebElement trJuntada = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'juntada ')]"));
		WebElement documentoJuntado =  util.getWebElement(driver, trJuntada, By.partialLinkText(codigoDocumento));
		documentoJuntado.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/h2[contains(.,'"+codigoDocumento +"')]")));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public OperacoesDocumentoPage clicarLinkDocumentoDesentranhado(String codigoDocumento) {
		WebElement desentranhamentoDocumento = util.getWebElement(driver, By.xpath("//tr[contains(@class, 'desentranhamento ')]"));
		WebElement linkProcessoDesentranhado = util.getWebElement(driver, desentranhamentoDocumento, By.partialLinkText(codigoDocumento));
		linkProcessoDesentranhado.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[4]/div/h2[contains(.,'"+codigoDocumento +"')]")));
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public void efetuaBuscaDocumento(String codigoDocumento) {
		buscaGenerica.clear();
		buscaGenerica.sendKeys(codigoDocumento);
		driver.findElement(By.linkText("/html/body/div[1]/div/div[1]/div/div[2]/img")).click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Visualizar Impressão")));
	}

	public OperacoesDocumentoPage clicarAssinarDespacho(String baseURL, String codigoDocumento) {		
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

		driver.get(baseURL + "/sigaex/expediente/mov/simular_assinatura_mov.action?sigla="+ codigoDocumento + "&id="+codigoDespacho);				
		System.out.println("URL: " + driver.getCurrentUrl());
		
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean clicarAssinarEncerramentoVolume() {
		WebElement textoEncerramento = null;
		WebElement linkVerAssinar = util.getWebElement(driver, By.xpath("//tr[@class='encerramento_volume ']/td/a[contains(text(), 'Ver/Assinar')]"));
		linkVerAssinar.click();
		System.out.println("Conteudo do elemento: " + linkVerAssinar.getText());
				
		util.openPopup(driver);
		try {
			textoEncerramento = util.getWebElement(driver, By.xpath("//p[contains(text(), 'encerrei o volume')]"));
		} finally {
			util.closePopup(driver);
		}
		
		return (textoEncerramento != null);
	}
	
	public OperacoesDocumentoPage clicarProtocolo() {	
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Protocolo"))).click();		
		util.openPopup(driver);
		try {	
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[contains(., 'Protocolo de Transferência')]")));

		} finally {
			util.closePopup(driver);
		}
		
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean isIdentificacaoDocumentoVisivel(String identificacaoDocumento) {			
		return util.getWebElement(driver, By.xpath("//div[@class='gt-content-box']//p[contains(., '"+ identificacaoDocumento +"')]")) != null;
	}
	
	public Boolean isCossignatarioVisivel(String cossignatario) {		
		return util.getWebElement(driver, By.xpath("//div[h3 = 'Cossignatários']/ul/li[contains(., '"+ cossignatario +"')]")) != null;
	}
	
	public Boolean isCossignatarioInvisivel(String cossignatario) {		
		return util.isElementInvisible(driver, By.xpath("//div[h3 = 'Cossignatários']/ul/li[contains(., '"+ cossignatario +"')]"));
	}
	
	public Boolean isDocumentoFinalizado() {
		return util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + 
				 "[contains(text(), 'Pendente de Assinatura, Como Subscritor')]|//div[h3 = 'Vias']/ul/li[contains(., 'Pendente de Assinatura') and contains(., 'Como Subscritor')]")) != null;
	}
	
	public Boolean isAnexoAssinado() {
		return util.getWebElement(driver, By.xpath("//td[4][contains(., 'Assinado por')]")) != null;
	}
	
	public Boolean isAnotacaoVisivel(String nota) {
		return util.getWebElement(driver, By.xpath("//td[4][contains(., '"+ nota +"')]")) != null;
	}
	
	public Boolean isAnotacaoInvisivel(String nota) {
		return util.isElementInvisible(driver, By.xpath("//td[4][contains(., '"+ nota +"')]"));
	}
	
	public Boolean isPerfilVisivel(String perfil, String nomeResponsavel) {
		return (util.getWebElement(driver, By.xpath("//div[h3 = 'Perfis' and p/b[contains(., '"+ perfil +"')] and ul/li[contains(., '" + nomeResponsavel + "')]]")) != null);
	}
	
	public Boolean isPerfilInvisivel(String perfil) {
		return util.isElementInvisible(driver, By.xpath("//div[h3 = 'Perfis']/p/b[contains(., '"+ perfil +"')]"));
	}
	
	public Boolean isDocumentoAssinado() {
		return util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + 
				"[contains(text(), 'Aguardando Andamento')]|"
				+ "//div[h3 = 'Volumes' or h3 = 'Vias']/ul/li[contains(., 'Aguardando Andamento')]")) != null;
	}
	
	public Boolean isEstadoAtualDocumento(String status) {
		return util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + "[contains(text(), '"+ status +"')]|"
				+ "//div[h3 = 'Vias']/ul/li[contains(., '"+ status +"')]")) != null;
	}
	
	public Boolean isEstadoAtualVolume(String status) {
		return util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + "[contains(text(), '"+ status +"')]|"
				+ "//div[h3 = 'Volumes']/ul/li[contains(., '"+ status +"')]")) != null;
	}
	
	public Boolean isSolicitacaoPublicacaoBoletimVisivel() {
		return util.getWebElement(driver, By.xpath("//td[3][contains(., 'Solicitação de Publicação no Boletim')]")) != null;
	}
	
	public Boolean isSolicitacaoPublicacaoBoletimInvisivel() {
		return 	util.isElementInvisible(driver, By.xpath("//td[3][contains(., 'Solicitação de Publicação no Boletim')]"));
	}
	
	public Boolean isNotificacaoHorarioSolicitacaoVisivel() {
		return util.getWebElement(driver, By.xpath("//h3[contains(., 'A solicitação de publicação no BIE apenas é permitida até as 17:00')]")) != null;
	}
	
	public Boolean isDocumentoConectadoVisivel(String codigoDocumento) {
		WebElement documentosRelacionados = util.getWebElement(driver, By.id("outputRelacaoDocs"));		
		return documentosRelacionados != null && documentosRelacionados.getText().contains(codigoDocumento);
	}
	
	public Boolean isDocumentoConectadoInvisivel() {
		return util.isElementInvisible(driver, By.id("outputRelacaoDocs"));
	}
	
	public CancelamentoMovimentacaoPage cancelarVinculoDocumento() {
		WebElement vinculacao = util.getWebElement(driver, By.xpath("//td[7][contains(., 'Ver também:')]"));
		Assert.assertNotNull(vinculacao, "Texto 'Ver também:' não encontrado");
		util.getClickableElement(driver, vinculacao.findElement(By.linkText("Cancelar"))).click();
		return  PageFactory.initElements(driver, CancelamentoMovimentacaoPage.class);
	}
	
	public Boolean isDespachoVisivel(String despacho) {
		return util.getWebElement(driver, By.xpath("//td[4][contains(., '" + despacho + "')]")) != null;
	}
	
	public Boolean isDespachoAssinado() {
		return util.getWebElement(driver, By.xpath("//td[4][contains(., 'Assinado por')]")) != null;
	}
	
	public Boolean isNivelAcessoModificado(String nivelAcesso) {
		return util.getWebElement(driver, By.xpath("(//p/b[contains(.,'" + nivelAcesso + "')])")) != null;
	}
	
	public Boolean isDocumentoJuntadoVisivel(String codigoDocumentoJuntado) {		
		return util.getWebElement(driver, By.xpath("//td[4][contains(., 'Documento juntado:') and contains(., '" + codigoDocumentoJuntado +"')]")) != null;
	}
	
	public Boolean isNumeroProcessoVisivel() {
		return util.getWebElement(driver, By.xpath("//b[contains(., 'Processo Nº')]")) != null;
	}
	
	public Boolean isProcessoFinalizado() {
		return util.getWebElement(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + 
				 "[contains(text(), '1º Volume - Pendente de Assinatura, Como Subscritor')]|"
				 + "//div[h3 = 'Volumes']/ul/li[contains(., 'Pendente de Assinatura') and contains(., 'Como Subscritor')]")) != null;
	}
	
	public Boolean isDocumentoJuntadoInvisivel() {
		return util.isElementInvisible(driver, By.xpath("//tr[contains(@class, 'juntada ')]"));
	}
	
	public Boolean isDesentramentoVisivel() {
		return util.getWebElement(driver, By.xpath("//tr[contains(@class, 'desentranhamento ')]")) != null;
	}
	
	public Boolean isEncerramentoVolumeVisivel() {
		return util.getWebElement(driver, By.xpath("//td[3][contains(text(),'Encerramento de Volume')]")) != null;
	}
	
	public Boolean isNovoVolumeVisivel(String statusVolume1, String statusVolume2) {
		WebElement volume1 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[1][contains(., '" + statusVolume1 + "')]"));
		WebElement volume2 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[2][contains(., '" + statusVolume2 + "')]"));
		
		return volume1 != null && volume2 != null;
	}
	
	public Boolean isVolumeAssinado() {
		return util.getWebElement(driver, By.xpath("//h3[contains(text(), 'Volume - Aguardando Andamento')]|//div[h3 = 'Volumes']/ul/li[2][contains(., 'Aguardando Andamento')]")) != null;
	}
	
	public Boolean isNumeroProcessoVisivel(String codigoProcesso) {
		return util.getWebElement(driver, By.xpath("//h2[contains(text(), '" + codigoProcesso + "')]")) != null;
	}
	
	public Boolean isPendenciaAssinaturaInvisivel() {
		return util.isElementInvisible(driver, By.xpath(OperacoesDocumentoPage.XPATH_STATUS_DOCUMENTO + "[contains(text(), 'Anexo Pendente de Assinatura/Conferência')]"));
	}
}