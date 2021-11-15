package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarNovoProcessoCampoAcessoIT extends DriverBase {

	/**
	 * Caminho: Criar novo documento > Memorando
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void campoAcessoLimitadoAoOrgao() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		{
			Select dropdown = new Select(driver.findElement(By.name("exDocumentoDTO.nivelAcesso")));
			dropdown.selectByVisibleText("Limitado ao órgão (padrão)");
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}

	@Test
	public void campoAcessoDePessoaParadivisao() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		{
			Select dropdown = new Select(driver.findElement(By.name("exDocumentoDTO.nivelAcesso")));
			dropdown.selectByVisibleText("Limitado de pessoa para divisão");
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}

	@Test
	public void campoAcessoDeDivisaoParaPessoa() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		{
			Select dropdown = new Select(driver.findElement(By.name("exDocumentoDTO.nivelAcesso")));
			dropdown.selectByVisibleText("Limitado de divisão para pessoa");
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}

	@Test
	public void campoAcessoLimitadoEntreLotacoes() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		{
			Select dropdown = new Select(driver.findElement(By.name("exDocumentoDTO.nivelAcesso")));
			dropdown.selectByVisibleText("Limitado entre lotações");
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}

	@Test
	public void campoAcessoLimitadoEntrePessoas() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		{
			Select dropdown = new Select(driver.findElement(By.name("exDocumentoDTO.nivelAcesso")));
			dropdown.selectByVisibleText("Limitado entre pessoas");
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
	}

}
