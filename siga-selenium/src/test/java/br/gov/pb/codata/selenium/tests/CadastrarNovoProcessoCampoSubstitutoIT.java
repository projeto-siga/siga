package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarNovoProcessoCampoSubstitutoIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Ferramentas > Cadastro documento com substituto
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void incluirComSucessoCampoSubstituto() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.cssSelector(".dropdown-item:nth-child(5) .pl-2")).click();
		driver.findElement(By.name("exDocumentoDTO.substituicao")).click();
		driver.findElement(By.id("titularSelButton")).click();
		{
			WebElement element = driver.findElement(By.id("titularSelButton"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("COD10327")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}
}
