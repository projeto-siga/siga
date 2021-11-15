package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

/**
*
* @author Thomas Ribeiro
*/
public class BuscarDocumentosIT extends DriverBase {

	@Test // corrigir
	public void buscarDocumentos() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/mesa");
		driver.findElement(By.linkText("Documentos")).click();
		driver.findElement(By.linkText("Pesquisar")).click();
		driver.findElement(By.cssSelector(".btn:nth-child(15)")).click();
	}

	@Test // so funciona com browser habilitado, corrigir
	public void buscarDocumentosDescricao() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/mesa");
		driver.findElement(By.linkText("Documentos")).click();
		driver.findElement(By.linkText("Pesquisar")).click();
		driver.findElement(By.name("descrDocumento")).click();
		driver.findElement(By.name("descrDocumento")).sendKeys("teste");
		driver.findElement(By.cssSelector(".btn:nth-child(15)")).click();
	}

	@Test // corrgir
	public void buscarDocumentosClassificacao() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/mesa");
		driver.findElement(By.linkText("Documentos")).click();
		driver.findElement(By.linkText("Pesquisar")).click();
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
		driver.findElement(By.linkText("01.03.02.03")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.name("descrDocumento")).click();
		driver.findElement(By.name("descrDocumento")).sendKeys("teste");
		driver.findElement(By.cssSelector(".btn:nth-child(15)")).click();
	}

}