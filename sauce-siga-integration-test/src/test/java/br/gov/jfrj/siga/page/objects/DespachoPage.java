package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

public class DespachoPage extends EditaDocumentoPage {

	public DespachoPage(WebDriver driver) {
		super(driver);
	}
	
	public void criarDespacho(Properties propDocumentos, Boolean isDigital) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));
		preencheDocumentoInternoSemModelo(propDocumentos, "Despacho", isDigital);
		util.getClickableElement(driver, botaoOk);
		botaoOk.click();
	}
}
