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

public class DefinicaoAcompanhamentoIT extends DriverBase {

	/**
	 * Caminho: Criar um processo generico, depois ir na aba "definir
	 * acompanhamento"
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void acompanhamentoDocumento() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.id("dropdownMenuButton")).click();
		driver.findElement(By.cssSelector(".dropdown-item:nth-child(1) .pl-2")).click();
		driver.findElement(By.id("classificacaoSelButton")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.linkText("01.01.04.01")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste definir acompanhamento");
		driver.switchTo().frame(0);
		driver.findElement(By.cssSelector("html")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.name("gravar")).click();
		driver.findElement(By.linkText("Definir Acompanhamento")).click();
		driver.findElement(By.name("dtMovString")).click();
		driver.findElement(By.name("dtMovString")).sendKeys("09112020");
		{
			WebElement element = driver.findElement(By.id("tipoResponsavel"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.id("tipoResponsavel"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.id("tipoResponsavel"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.id("tipoResponsavel")).click();
		driver.findElement(By.id("responsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("COD10327")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
	}
}