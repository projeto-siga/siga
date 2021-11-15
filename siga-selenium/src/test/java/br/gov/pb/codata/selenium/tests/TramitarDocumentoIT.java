package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class TramitarDocumentoIT extends DriverBase {

	/**
	 * Objetivo: Tramitar um documento criado
	 * Caminho: Criar novo documento > assinar/tramitar
	 * 
	 * @author Allysson Cruz
	 */

	private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	//@Test
	public void tramitarDocumento() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL")+ "sigaex/app/expediente/doc/editar");

		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("classificacaoSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("01.03.02.03")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("testes tramitar");
		driver.findElement(By.name("gravar")).click();
		String docTitle = driver.getTitle();
		driver.findElement(By.linkText("Assinar")).click();
		driver.findElement(By.id("ad_password_0")).click();
		driver.findElement(By.id("bot-assinar")).click();
		{
			WebElement element = driver.findElement(By.id("bot-assinar"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		assinar(driver);//Assinar o documento para movimentar
		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith(docTitle));
		driver.findElement(By.linkText("Tramitar")).click();
		driver.findElement(By.id("lotaResponsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("LTEST")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("u")).click();
	}

	private void assinar(WebDriver driver) {

		driver.findElement(By.xpath("//*[@id='senhaUsuarioSubscritor']")).sendKeys("Hexadecimal1");//Informa a senha

		driver.findElement(By.xpath("//*[@id='senhaOk']")).click();//Ok
	}
}
