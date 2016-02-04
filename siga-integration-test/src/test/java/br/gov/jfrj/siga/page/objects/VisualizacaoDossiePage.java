package br.gov.jfrj.siga.page.objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class VisualizacaoDossiePage {
	private WebDriver driver;

	@FindBy(css="table.gt-table")
	private WebElement documentosTable;

	@FindBy(linkText="Visualizar Movimentações")
	private WebElement linkVisualizarMovimentacoes;

	private IntegrationTestUtil util;

	public VisualizacaoDossiePage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}

	public Boolean visualizarDossie() {
		Boolean numeroDespachoEncontrado = Boolean.TRUE;
		List<WebElement> links = documentosTable.findElements(By.xpath("//a[contains(text(), '-DES-')]"));
		String windowHandle = driver.getWindowHandle();

		try {
			for (WebElement webElement : links) {
				webElement.click();
				new WebDriverWait(driver, 30).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("painel")));
				WebElement element = util.getWebElement(driver, By.xpath("//p[contains(text(), 'DESPACHO Nº')]"));
				if(element == null) {
					numeroDespachoEncontrado = Boolean.FALSE;
					break;
				}
			}
		} finally {
			driver.switchTo().window(windowHandle);
		}

		return numeroDespachoEncontrado;
	}

	public void clicarLinkDocumentoDossie(String codigoDocumento) {
		util.getWebElement(driver, By.partialLinkText(codigoDocumento)).click();
	}

	public void clicarLinkVisualizarMovimentacoes() {
		linkVisualizarMovimentacoes.click();
	}

	public Boolean visualizaConteudo(By option) {
		String windowHandle = driver.getWindowHandle();
		new WebDriverWait(driver, 30).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("painel")));
		WebElement element = util.getWebElement(driver, option);
		driver.switchTo().window(windowHandle);
		return (element != null ? true : false);
	}

	public String getNumeroPagina(String documentoDossie) {
		WebElement linhaDocumentoJuntado = util.getWebElement(driver, By.xpath("//tr[contains(td, '" + documentoDossie + "')]"));
		String[] dadosDocumentoJuntado = linhaDocumentoJuntado.getText().split(" ");

		return dadosDocumentoJuntado[dadosDocumentoJuntado.length - 1];
	}

	public Boolean visualizaNumeroPagina(String documentoDossie) {
		clicarLinkDocumentoDossie(documentoDossie);
		return visualizaConteudo(By.xpath("//div[contains(@class, 'numeracao') and contains(text(), '" + getNumeroPagina(documentoDossie) +"')]"));
	}
}
