package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class RegistraAssinaturaManualPage {
	private WebDriver driver;

	@FindBy(id="registrar_assinatura_gravar_dtMovString")
	private WebElement data;

	@FindBy(id="registrar_assinatura_gravar_subscritorSel_sigla")
	private WebElement subscritor;

	@FindBy(xpath="//input[@value='Sim']")
	private WebElement botaoSim;

	@FindBy(xpath="//input[@value='Não']")
	private WebElement botaoNao;

	private IntegrationTestUtil util;

	public RegistraAssinaturaManualPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}

	public void registarAssinaturaManual() {
		util.preencheElemento(driver, data, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		botaoSim.click();
	}

}
