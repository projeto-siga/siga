package br.gov.pb.codata.selenium.tests;


import static br.gov.pb.codata.selenium.util.text.Dictionary.oneHundredChars;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;

public class LoginPBDocIT extends DriverBase {

	private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	private ExpectedCondition<Boolean> paginaDeveMostrarMensagem(final String msg) {
		return driver -> driver.findElement(By.className("login-invalido-titulo")).getText().equalsIgnoreCase(msg);
	}

	/**
	 * Caminho: Pagina de login Objetivo: Realizar um login válido
	 */
	// @Test1
	public void loginComSucesso() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		loginPage.logar().enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith("PBDoc - Mesa Virtual"));
	}

	/**
	 * Caminho: Pagina de login Objetivo: Realizar um login inválido
	 */
	@Test
	public void loginInvalido() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.get(System.getenv("PBDOC_URL"));
		SigadocLoginPage loginPage = new SigadocLoginPage();
		loginPage.digitarCredenciais("usuarioNaoCadastrado", "Senha123").enviarAutenticacao();
		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(paginaDeveMostrarMensagem("Ocorreu um erro tentando localizar a identidade do usuario 'usuarioNaoCadastrado'."));
	}

	/**
	 * Caminho: Pagina de login Objetivo: Validação de inputs
	 */
	//// @Test
	public void validacaoDeInputs() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().setSize(new Dimension(1920, 1080));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		loginPage.digitarCredenciais(oneHundredChars, oneHundredChars).enviarAutenticacao();
	}

}