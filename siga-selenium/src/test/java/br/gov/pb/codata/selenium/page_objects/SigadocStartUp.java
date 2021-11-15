package br.gov.pb.codata.selenium.page_objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.pb.codata.selenium.DriverBase;

/**
*
* @author Thomas Ribeiro
*/
public class SigadocStartUp extends DriverBase {

	private static ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	public static WebDriver startUp() throws Exception {
		WebDriver driver = getDriver();
		driver.manage().window().maximize();
		driver.get(System.getenv("PBDOC_URL"));
		SigadocLoginPage loginPage = new SigadocLoginPage();

		WebDriverWait wait = new WebDriverWait(driver, 30, 100);

		wait.until(pageTitleStartsWith("PBdoc - Página de Login"));
		loginPage.logar().enviarAutenticacao();

		wait.until(pageTitleStartsWith("PBdoc - Mesa Virtual"));

		return driver;
	}

}
