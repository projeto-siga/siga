package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastroOrgaoIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de Orgao > Incluir/pesquisar/alterar
	 * 
	 * @author Allysson Cruz
	 */

	//@Test1
	public void incluirOrgao() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgaoUsuario/editar");
		driver.findElement(By.id("id")).click();
		driver.findElement(By.id("id")).sendKeys("5");
		driver.findElement(By.id("nmOrgaoUsuario")).click();
		driver.findElement(By.id("nmOrgaoUsuario")).sendKeys("teste agora");
		driver.findElement(By.id("siglaOrgaoUsuarioCompleta")).click();
		{
			WebElement element = driver.findElement(By.id("siglaOrgaoUsuario"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.id("siglaOrgaoUsuario"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.id("siglaOrgaoUsuario"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.cssSelector(".card-body > .row:nth-child(1)")).click();
		driver.findElement(By.id("siglaOrgaoUsuarioCompleta")).sendKeys("okkeue");
		driver.findElement(By.id("siglaOrgaoUsuario")).click();
		driver.findElement(By.id("siglaOrgaoUsuario")).sendKeys("oku");
		driver.findElement(By.cssSelector(".form-group > .btn:nth-child(1)")).click();
	}

	@Test
	public void pesquisarOrgao() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgaoUsuario/listar");
		// nome
		driver.findElement(By.id("nome")).sendKeys("tes");
		// pesquisar
		driver.findElement(By.xpath("//input[@value='Pesquisar']")).click();
	}

	@Test
	public void alterarOrgao() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgaoUsuario/listar");
		driver.findElement(By.id("nome")).sendKeys("oku");
		// pesquisar
		driver.findElement(By.xpath("//input[@value='Pesquisar']")).click();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/orgaoUsuario/editar?editar?id=3");
		driver.findElement(By.id("nmOrgaoUsuario")).sendKeys("Nodsm22");
		// sigla
		driver.findElement(By.id("siglaOrgaoUsuarioCompleta")).sendKeys("siglvs correta");
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}
}