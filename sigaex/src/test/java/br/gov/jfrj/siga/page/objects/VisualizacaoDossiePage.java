package br.gov.jfrj.siga.page.objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VisualizacaoDossiePage {
	private WebDriver driver;
	
	@FindBy(css="table.gt-table")
	private WebElement documentosTable;
	
	public VisualizacaoDossiePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void visualizarDossie() {
		List<WebElement> rows = documentosTable.findElements(By.cssSelector("tr"));		
		String windowHandle = driver.getWindowHandle();
		WebElement element;
		for (int i = 1; i < rows.size(); i++) {
			element =  rows.get(i);
			System.out.println("Texto: " + element.getText());
			element.click();

			new WebDriverWait(driver, 15).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("painel")));
			new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(), 'Nº')]")));
			driver.switchTo().window(windowHandle);
		}
	}
}
