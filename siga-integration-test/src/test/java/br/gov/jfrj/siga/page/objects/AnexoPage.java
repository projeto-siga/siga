package br.gov.jfrj.siga.page.objects;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
	}
	
	public void anexarArquivo(Properties propDocumentos) {
		botaoOk.click();
		util.closeAlertAndGetItsText(driver);
		util.preencheElemento(driver,dataAnexo, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("siglaSubscritor"));
		util.preencheElemento(driver, descricao, propDocumentos.getProperty("descricaoAnexo") ); 
		File file = new File("src/test/resources/" + propDocumentos.getProperty("arquivoAnexo"));
		arquivo.sendKeys(file.getAbsolutePath());
		botaoOk.click();	
	}
	
	public void clicarBotaovoltar() {
		botaoVoltar.click();
	}
}
