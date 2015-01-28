package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PortariaPage extends EditaDocumentoPage{
	
	@FindBy(name="dispoe_sobre")
	private WebElement dispoeSobre;
	
	public PortariaPage(WebDriver driver) {
		super(driver);
	}
	
	public void criaPortaria(Properties propDocumentos) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));		
		selectTipoDocumento("Portaria", "Portaria", By.xpath("//td[text() = 'Texto da portaria']"));
		preencheDocumentoInterno(propDocumentos, Boolean.TRUE, Boolean.TRUE);
		util.preencheElemento(driver, dispoeSobre, propDocumentos.getProperty("dispoeSobre"));
		botaoOk.click();
	}
}
