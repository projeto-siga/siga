package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DespachoPage extends EditaDocumentoPage {

	public DespachoPage(WebDriver driver) {
		super(driver);
	}
	
	public OperacoesDocumentoPage criarDespacho(Properties propDocumentos, Boolean isDigital) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));
		preencheDocumentoInternoSemModelo(propDocumentos, "Despacho", isDigital);
		util.getClickableElement(driver, botaoOk);
		preencheCKEditor();
		botaoOk.click();
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
}
