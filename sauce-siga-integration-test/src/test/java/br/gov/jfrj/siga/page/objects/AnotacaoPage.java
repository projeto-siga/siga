package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class AnotacaoPage {
	 
	protected WebDriver driver;
	@FindBy(id="anotar_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="anotar_gravar_subscritorSel_sigla")
	private WebElement responsavel; 
	
	@FindBy(id="anotar_gravar_nmFuncaoSubscritor")
	private WebElement funcaoResponsavel;
	
	@FindBy(id="anotar_gravar_descrMov")
	private WebElement nota;
	
	@FindBy(css="input.gt-btn-small.gt-btn-left")
	private WebElement botaoOk;
	
	private IntegrationTestUtil util;
	public AnotacaoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void fazerAnotacao(Properties propDocumentos) {
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("siglaDestinatario"));
		util.preencheElemento(driver, funcaoResponsavel, propDocumentos.getProperty("funcaoLocalidade"));
		util.preencheElemento(driver, nota, propDocumentos.getProperty("nota"));
	    botaoOk.click();
	}
}