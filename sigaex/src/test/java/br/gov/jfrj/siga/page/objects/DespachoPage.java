package br.gov.jfrj.siga.page.objects;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DespachoPage extends EditaDocumentoPage {

	public DespachoPage(WebDriver driver) {
		super(driver);
	}
	
	public void criarDespacho(Properties propDocumentos, Boolean isDigital) {
		preencheDocumentoInternoSemModelo(propDocumentos, "Despacho", propDocumentos.getProperty("internoProduzido"), isDigital);
		new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(botaoOk));
		botaoOk.click();
	}
}
