package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class DefinePerfilPage {
	private WebDriver driver;
	
	@FindBy(id="vincularPapel_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="vincularPapel_gravar_responsavelSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="vincularPapel_gravar_idPapel")
	private WebElement perfil;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public DefinePerfilPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void definirPerfil(Properties propDocumentos) {
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		util.getSelect(driver, perfil).selectByVisibleText(propDocumentos.getProperty("perfil"));
		botaoOk.click();
	}
}
