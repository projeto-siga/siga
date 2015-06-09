package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AgendamentoPublicacaoPage {
	private WebDriver driver;

	@FindBy(xpath="//input[@value='Ok']")
	private WebElement botaoOk;

	@FindBy(xpath="//input[@value='Cancela']")
	private WebElement botaoCancela;

	@FindBy(linkText="Visualizar Publicação")
	private WebElement botaoVisualizarPublicacao;

	public AgendamentoPublicacaoPage(WebDriver driver) {
		this.driver = driver;
	}

	public Boolean visualizaPagina() {
		try {
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoOk));
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoVisualizarPublicacao));
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoCancela)).click();
			return true;
		} catch (TimeoutException e) {
			e.printStackTrace();
			return false;
		}
	}
}
