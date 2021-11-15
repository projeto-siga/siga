package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class VisualizarDocumentoCompletoIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Criar novo documento > Ver documento completo
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void visualizarDocumento() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("classificacaoSelButton")).click();
		{
			WebElement element = driver.findElement(By.id("classificacaoSelButton"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("01.01.04.01")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste visualizar");
		driver.findElement(By.cssSelector(".btn-primary > u")).click();
		driver.findElement(By.linkText("Ver Documento Completo")).click();
	}
}
