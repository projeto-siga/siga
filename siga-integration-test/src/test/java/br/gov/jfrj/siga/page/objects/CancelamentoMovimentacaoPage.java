package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class CancelamentoMovimentacaoPage {
	private WebDriver driver;
	
	@FindBy(id="cancelar_movimentacao_gravar_dtMovString")
	private WebElement data;
	
	@FindBy(id="cancelar_movimentacao_gravar_subscritorSel_sigla")
	private WebElement responsavel;
	
	@FindBy(id="cancelar_movimentacao_gravar_descrMov")
	private WebElement motivo;
	
	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;
	
	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;
	
	private IntegrationTestUtil util;
	
	public CancelamentoMovimentacaoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void cancelarMovimentacao(Properties propDocumentos) {
		util.preencheElemento(driver, data, new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver, responsavel, propDocumentos.getProperty("responsavel"));
		util.preencheElemento(driver, motivo, propDocumentos.getProperty("motivo"));
		botaoOk.click();
	}
}
