package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastroCargoIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Gestão de Identidade > Cadastrar Cargo
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void cadastrarComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/cargo/editar");
		driver.manage().window().setSize(new Dimension(1050, 792));
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
		driver.findElement(By.id("nmCargo")).click();
		driver.findElement(By.id("nmCargo")).sendKeys("novo cao4");
		driver.findElement(By.cssSelector(".form-group > .btn:nth-child(1)")).click();
	}

	// @Test
	public void NomeCargoEmBranco() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/cargo/editar");
		Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown.selectByValue("190401");
		driver.findElement(By.name("nmCargo")).sendKeys("");
		driver.findElement(By.cssSelector(".form-group > .btn:nth-child(1)")).click();
	}

	// @Test
	// Apenas para ZZZ
	public void cargoJaCadastrado() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "siga/app/cargo/editar");
			Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
			dropdown.selectByValue("190401");
			driver.findElement(By.name("nmCargo")).sendKeys("novo cao4");
			driver.findElement(By.xpath("//input[@value='Ok']")).click();
			driver.findElement(By.xpath("//h3[contains(.,'Nome do cargo já cadastrado!')]"));
		}

	}

	// @Test
	public void pesquisarCargo() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/cargo/listar");
		Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown.selectByValue("190401");
		driver.findElement(By.name("nome")).sendKeys("");
		driver.findElement(By.xpath("//input[@value='Pesquisar']"));
	}

	// @Test
	public void alterarCargo() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/cargo/editar?id=1");
		driver.findElement(By.name("nmCargo")).sendKeys("Noc355s5");
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}

}