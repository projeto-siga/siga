package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class VinculacaoPage {
	private WebDriver driver;
	
	@FindBy(id="referenciar_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="referenciar_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="referenciar_gravar_documentoRefSel_sigla")
	private WebElement documento;
	
	@FindBy(id="documentoRefSelButton")
	private WebElement botaoDocumento;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public VinculacaoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
		
		if(!util.isDescricaoPaginaVisivel(driver, "Vinculação de Documento")) {
			throw new RuntimeException("Esta não é a página de Vinculação de Documento!");
		}
	}
	
	public String vincularDocumento(Properties propDocumentos, String codigoDocumento) {
		String codigoDocumentoVinculado = null;
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		documento.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("subscritorSelSpan")));
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoDocumento)).click();
		
		System.out.println("WindowHandle size antes: " + driver.getWindowHandles().size());
		botaoDocumento.click();
		
		util.openPopup(driver);
		try {
			PesquisaDocumentoPage buscaPage = PageFactory.initElements(driver, PesquisaDocumentoPage.class);
			codigoDocumentoVinculado =  buscaPage.buscarDocumento(codigoDocumento, propDocumentos.getProperty("situacaoDocumento"));		
			System.out.println("WindowHandle size depois: " + driver.getWindowHandles().size());
			new WebDriverWait(driver, 30).until(util.popupFechada());
		} finally {
			util.closePopup(driver);
		}

		// Aparentemente existe um bug ao selecionar um documento na página de PesquisaDocumento
		// Aparentemente o Mouse termina sua posição na parte de cima da tela e ao pedir para clicar no botão de Ok
		// o mouse passa pelo menu do Siga, que abre e acaba que o click é dado numa oção do menu em vez do botão de Ok.
		// Esses cliques foram colocados para tentar fazer com que o Webdriver clique no botão certo.
		data.click();
		responsavel.click();
		documento.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoOk)).click();	
		
		return codigoDocumentoVinculado;
	}
}
