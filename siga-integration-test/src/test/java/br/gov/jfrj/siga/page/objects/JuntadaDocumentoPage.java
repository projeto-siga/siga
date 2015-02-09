package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class JuntadaDocumentoPage {
	private WebDriver driver;
	
	@FindBy(id="juntar_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="juntar_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="juntar_gravar_documentoRefSel_sigla")
	private WebElement documento;
	
	@FindBy(id="documentoRefSelButton")
	private WebElement botaoDocumento;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public JuntadaDocumentoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void juntarDocumento(Properties propDocumentos, String codigoDocumento) {
		util.preencheElemento(driver, documento, codigoDocumento);
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		botaoOk.click();		
	}
}
