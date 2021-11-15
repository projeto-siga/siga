package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;

public class PrimeiroAcessoIT extends DriverBase {

	private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	/**
	 * Caminho: Primeiro acesso na pagina de login Objetivo: Criar um usuário com
	 * email válido Enviar email Receber usuário e código por email
	 */

	//@Test
	public void primeiroAcesso() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		loginPage.primeiroAcesso();

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith("PBDoc - Primeiro Acesso"));

		driver.findElement(By.id("txtMatricula")).sendKeys("usuario");
		driver.findElement(By.name("usuario.cpf")).sendKeys("CPF");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

}
