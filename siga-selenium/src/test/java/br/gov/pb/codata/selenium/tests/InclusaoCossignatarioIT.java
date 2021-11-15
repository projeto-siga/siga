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

public class InclusaoCossignatarioIT extends DriverBase {

	/**
	 * Caminho: Criar um processo generico, depois ir na aba "Incluir cossignatario"
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void inclusaoConssignatario() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
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
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste incluir cossignario");
		driver.findElement(By.cssSelector(".btn-primary > u")).click();
		driver.findElement(By.linkText("Incluir Cossignatário")).click();
		driver.findElement(By.id("cosignatarioSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.name("sigla")).click();
		driver.findElement(By.linkText(System.getenv("USUARIO"))).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.name("funcaoCosignatario")).click();
		driver.findElement(By.name("funcaoCosignatario")).sendKeys("teste so");
		driver.findElement(By.cssSelector(".col-12 > .btn-primary")).click();
	}

	@Test
	public void ValidarCamposEmBranco() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste incuir - campo vazio");
		driver.findElement(By.name("gravar")).click();
		driver.findElement(By.linkText("Incluir Cossignatário")).click();
		driver.findElement(By.cssSelector(".col-12 > .btn-primary")).click();
		driver.findElement(By.xpath("//h3[contains(.,'Cossignatário não foi informado')]"));
	}
}
