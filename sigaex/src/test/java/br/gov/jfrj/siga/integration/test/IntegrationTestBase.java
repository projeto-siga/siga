package br.gov.jfrj.siga.integration.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import br.gov.jfrj.siga.page.objects.LoginPage;

public class IntegrationTestBase {
	protected WebDriver driver;
	protected String baseURL;
	private String login;
	private String password;
	private LoginPage loginPage;
	
/*	public IntegrationTestBase(WebDriver driver) {
		this.driver = driver;
	}*/

	public IntegrationTestBase(String baseURL, String login, String password) {
		this.baseURL = baseURL;
		this.login = login;
		this.password = password;
		}

	public void efetuaLogin() {
		driver = new InternetExplorerDriver();
		try {
			driver.get(baseURL + "/siga");
			loginPage = PageFactory.initElements(driver, LoginPage.class);
			loginPage.login(login, password);
			new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.gt-btn-small.gt-btn-right")));
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		}
	}
}
