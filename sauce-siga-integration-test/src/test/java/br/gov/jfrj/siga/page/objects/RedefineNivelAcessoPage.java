package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class RedefineNivelAcessoPage {
	private WebDriver driver;
	
	@FindBy(id="redefinir_nivel_acesso_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="redefinir_nivel_acesso_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="redefinir_nivel_acesso_gravar_nivelAcesso")
	private WebElement nivelAcesso;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public RedefineNivelAcessoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void redefineNivelAcesso(Properties propDocumentos) {
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		util.getSelect(driver, nivelAcesso).selectByVisibleText(propDocumentos.getProperty("nivelAcesso"));
		botaoOk.click();
	}
}
