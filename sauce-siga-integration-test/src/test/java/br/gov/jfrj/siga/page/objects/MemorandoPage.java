package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MemorandoPage extends EditaDocumentoPage {
	
	@FindBy(id="scayt_0")
	private WebElement frameMemorando;
	
	@FindBy(css="table.cke_editor")
	private WebElement tableCkEditor;
		
	public MemorandoPage(WebDriver driver) {
		super(driver);
	}
	
	public void criaMemorando(Properties propDocumentos) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));
		selectTipoDocumento("Memorando", "Memorando", By.xpath("//td[text() = 'Dados complementares']"));
		util.isElementVisible(driver, tableCkEditor);
		preencheDocumentoInterno(propDocumentos, Boolean.TRUE, Boolean.TRUE);	
		botaoOk.click();
	}
}
