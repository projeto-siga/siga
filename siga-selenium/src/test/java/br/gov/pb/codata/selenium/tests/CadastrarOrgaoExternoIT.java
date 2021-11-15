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

public class CadastrarOrgaoExternoIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Ferramentas > Cadastro de orgãos externos
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void incluirComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgao/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.id("nmOrgao")).click();
		driver.findElement(By.id("nmOrgao")).sendKeys("New OrgaoOk");
		driver.findElement(By.id("siglaOrgao")).click();
		driver.findElement(By.id("siglaOrgao")).sendKeys("ert");
		{
			WebElement dropdown = driver.findElement(By.name("ativo"));
			dropdown.findElement(By.xpath("//option[. = 'Não']")).click();
		}
		{
			WebElement element = driver.findElement(By.name("ativo"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("ativo"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("ativo"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("ativo")).click();
		{
			WebElement element = driver.findElement(By.name("idOrgaoUsu"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("idOrgaoUsu"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("idOrgaoUsu"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("idOrgaoUsu")).click();
		driver.findElement(By.cssSelector(".form-group > .btn:nth-child(1)")).click();

	}

	// @Test
	public void pesquisarOrgao() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgaoUsuario/listar");
		// nome
		driver.findElement(By.id("nome")).sendKeys("");
		// pesquisar
		driver.findElement(By.xpath("//input[@value='Pesquisar']")).click();
	}

	// @Test
	public void alterarOrgao() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgao/listar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector("tr:nth-child(1) .btn")).click();
		driver.findElement(By.id("nmOrgao")).click();
		driver.findElement(By.id("nmOrgao")).sendKeys("New Orgaooy");
		driver.findElement(By.cssSelector(".form-group > .btn:nth-child(1)")).click();
	}
}