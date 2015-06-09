package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProcessoFinanceiroPage extends EditaDocumentoPage {

	public ProcessoFinanceiroPage(WebDriver driver) {
		super(driver);
	}

	public void criaProcessoFinanceiro(Properties propDocumentos, Boolean isDigital, String modeloDocumento) {
		preencheOrigem(propDocumentos.getProperty("internoProduzido"));
		selectTipoDocumento("Processo de Execução Orçamentária e Financeira", modeloDocumento,  By.xpath("//td[text() = 'Dados complementares']"));
		preencheDocumentoInterno(propDocumentos, isDigital, Boolean.TRUE);
		botaoOk.click();
	}

}
