package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class DesapensamentoPage {
	private WebDriver driver;
	
	@FindBy(id="desapensar_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="desapensar_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="desapensar_gravar_titularSel_sigla")
	private WebElement titular;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public DesapensamentoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void desapensarDocumento(Properties propDocumentos) {
		util.preencheElemento(driver, data, new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		util.preencheElemento(driver, titular, propDocumentos.getProperty("siglaSubscritor"));
		botaoOk.click();
	}
}
