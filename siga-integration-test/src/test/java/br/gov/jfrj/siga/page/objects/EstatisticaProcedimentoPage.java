package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class EstatisticaProcedimentoPage {
	private WebDriver driver;

	private IntegrationTestUtil util;

	@FindBy(id = "selecaoRelatorio")
	private WebElement relatorio;

	@FindBy(id = "dataInicialDe")
	private WebElement procedimentoIniciadoDe;

	@FindBy(id = "dataInicialAte")
	private WebElement procedimentoIniciadoAte;

	@FindBy(id = "dataFinalDe")
	private WebElement procedimentoFinalizadoDe;

	@FindBy(id = "dataFinalAte")
	private WebElement procedimentoFinalizadoAte;

	@FindBy(xpath = "//input[@value = 'Gerar relatório']")
	private WebElement botaoGerarRelatorio;

	public EstatisticaProcedimentoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}

	public void gerarRelatorioContratacao(String dataInicial, String dataFinal) {
		util.getSelect(driver, relatorio).selectByVisibleText("Tempo de documentos");
		util.preencheElemento(driver, procedimentoIniciadoDe, dataInicial);
		util.preencheElemento(driver, procedimentoIniciadoAte, dataFinal);
		util.preencheElemento(driver, procedimentoFinalizadoDe, dataInicial);
		util.preencheElemento(driver, procedimentoFinalizadoAte, dataFinal);
		botaoGerarRelatorio.click();
	}
}
