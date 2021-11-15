package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class FinalizarDocumentoExternoIT extends DriverBase {

	/**
	 * Caminho: Criar um processo generico, depois ir na aba "Finalizar"
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void finalizar() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		{
			WebElement element = driver.findElement(By.cssSelector(".selected-label"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		driver.findElement(By.cssSelector(".selected-label")).click();
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("classificacaoSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("01.03.02.03")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("finalizar documento");
		driver.findElement(By.cssSelector(".card-body")).click();
		driver.findElement(By.cssSelector(".btn-primary > u")).click();
		driver.findElement(By.linkText("Finalizar")).click();
	}

	@Test
	public void finalizarSemClassificacaoDocumental() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("finalizar sem classificacao documental");
		driver.findElement(By.name("gravar")).click();
		driver.findElement(By.linkText("Finalizar")).click();
		driver.findElement(By.xpath("//h3[contains(.,'É necessário informar a classificação documental.')]"));
	}
}
