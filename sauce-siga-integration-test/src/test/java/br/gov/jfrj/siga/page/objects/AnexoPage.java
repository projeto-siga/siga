package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class AnexoPage {
	private WebDriver driver;
	
	@FindBy(id="anexar_gravar_dtMovString")
	private WebElement dataAnexo;
	
	@FindBy(id="anexar_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="anexar_gravar_descrMov")
	private WebElement descricao;
	
	@FindBy(id="anexar_gravar_arquivo")
	private WebElement arquivo;
	
	@FindBy(css="input.gt-btn-medium.gt-btn-left")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Voltar']")
	private WebElement botaoVoltar;
	
	private IntegrationTestUtil util;
	
	public AnexoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
		
		if(!util.isDescricaoPaginaVisivel(driver, "Anexação de Arquivo")) {
			throw new IllegalStateException("Esta não é a página de Anexo!");
		}
	}
	
	public AnexoPage anexarArquivo(Properties propDocumentos) {
		botaoOk.click();
		util.closeAlertAndGetItsText(driver);
		util.preencheElemento(driver,dataAnexo, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("siglaSubscritor"));
		util.preencheElemento(driver, descricao, propDocumentos.getProperty("descricaoAnexo") ); 
		//File file = new File("src/test/resources/" + propDocumentos.getProperty("arquivoAnexo"));
		//arquivo.sendKeys(file.getAbsolutePath());
		arquivo.sendKeys(System.getProperty("baseURL") + "/siga/apostila_sigawf.pdf");
		botaoOk.click();
		return this;
	}
	
	public OperacoesDocumentoPage clicarBotaovoltar() {
		botaoVoltar.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean isAnexoVisivel(String nomeArquivo) {
		return util.getWebElement(driver, By.partialLinkText(nomeArquivo.toLowerCase())) != null;
	}
}
