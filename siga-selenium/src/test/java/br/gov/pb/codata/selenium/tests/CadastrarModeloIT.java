package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarModeloIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de Modelos > Incluir
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	// Apenas para ZZZ
	public void novoModelo() throws Exception {
		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/modelo/editar");
			driver.manage().window().setSize(new Dimension(1440, 812));
			driver.findElement(By.name("nome")).click();
			driver.findElement(By.name("nome")).sendKeys("tes modelos");
			driver.findElement(By.name("descricao")).click();
			driver.findElement(By.name("descricao")).sendKeys("teste");
			driver.findElement(By.id("classificacaoSelButton")).click();
			driver.switchTo().frame(0);
			driver.findElement(By.linkText("01.03.02.03")).click();
			driver.switchTo().defaultContent();
			driver.findElement(By.id("classificacaoCriacaoViasSelButton")).click();
			{
				WebElement element = driver.findElement(By.id("classificacaoCriacaoViasSelButton"));
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
			{
				WebElement dropdown = driver.findElement(By.name("forma"));
				dropdown.findElement(By.xpath("//option[. = 'Despacho']")).click();
			}
			{
				WebElement element = driver.findElement(By.name("forma"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).clickAndHold().perform();
			}
			{
				WebElement element = driver.findElement(By.name("forma"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).perform();
			}
			{
				WebElement element = driver.findElement(By.name("forma"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).release().perform();
			}
			driver.findElement(By.name("forma")).click();
			{
				WebElement dropdown = driver.findElement(By.name("nivel"));
				dropdown.findElement(By.xpath("//option[. = 'Público']")).click();
			}
			{
				WebElement element = driver.findElement(By.name("nivel"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).clickAndHold().perform();
			}
			{
				WebElement element = driver.findElement(By.name("nivel"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).perform();
			}
			{
				WebElement element = driver.findElement(By.name("nivel"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).release().perform();
			}
			driver.findElement(By.name("nivel")).click();
			driver.findElement(By.name("diretorio")).click();
			driver.findElement(By.name("diretorio")).sendKeys("t");
			driver.findElement(By.name("arquivo")).click();
			driver.findElement(By.name("arquivo")).sendKeys("arquivo");
			driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
		}
	}
}
