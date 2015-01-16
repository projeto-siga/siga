package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LoginPage {
	
	protected WebDriver driver;
	
	@FindBy(id="j_username")
	private WebElement matricula;
	
	@FindBy(name="j_password")
	private WebElement senha;
	
	@FindBy(css="input.gt-btn-medium.gt-btn-right")
	private WebElement submitButton;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void login(String login, String password) {
		matricula.clear();
		matricula.sendKeys(login);
		senha.clear();
		senha.sendKeys(password);
		submitButton.click();
	}	
}
