package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;


public class LoginPage {
	
	protected WebDriver driver;
	
	@FindBy(id="j_username")
	private WebElement matricula;
	
	@FindBy(name="j_password")
	private WebElement senha;
	
	@FindBy(css="input.gt-btn-medium.gt-btn-right")
	private WebElement submitButton;
	
	private IntegrationTestUtil util;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		
		util = new IntegrationTestUtil();
		if(util.getWebElement(driver, By.xpath("//b[contains(., 'Bem-vindo ao SIGA.')]")) == null) {
			throw new RuntimeException("Esta não é a página de Login!");
		}
	}
	
	public PrincipalPage login(String login, String password) {
		matricula.clear();
		matricula.sendKeys(login);
		senha.clear();
		senha.sendKeys(password);
		submitButton.click();
		
		return PageFactory.initElements(driver, PrincipalPage.class);
	}	
}
