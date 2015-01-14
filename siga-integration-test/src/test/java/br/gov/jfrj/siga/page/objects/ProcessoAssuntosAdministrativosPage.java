package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProcessoAssuntosAdministrativosPage extends EditaDocumentoPage {

	public ProcessoAssuntosAdministrativosPage(WebDriver driver) {
		super(driver);
	}
		
	public void criaProcesso(Properties propDocumentos, Boolean isDigital, String modeloDocumento) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));
		selectTipoDocumento("Processo de Outros Assuntos Administrativos", modeloDocumento, By.xpath("//td[text() = 'Dados complementares']"));
		preencheDocumentoInterno(propDocumentos, isDigital, Boolean.TRUE);
		botaoOk.click();
	}
}
