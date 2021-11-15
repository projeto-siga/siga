package br.gov.pb.codata.selenium.page_objects;

import static com.lazerycode.selenium.util.AssignDriver.initQueryObjects;

import org.openqa.selenium.By;

import com.lazerycode.selenium.util.Query;

import br.gov.pb.codata.selenium.DriverBase;

/**
*
* @author Thomas Ribeiro
*/
public class SigadocLoginPage {

	public static final String USERNAME = System.getenv("USUARIO");
	public static final String SENHA = System.getenv("SENHA");
	private final Query inputUsuario = new Query().defaultLocator(By.id("username"));
	private final Query inputSenha = new Query().defaultLocator(By.id("password"));
	private final Query buttonPrimeiroAcesso = new Query().defaultLocator(By.linkText("Primeiro acesso"));

	public SigadocLoginPage() throws Exception {
		initQueryObjects(this, DriverBase.getDriver());
	}

	public SigadocLoginPage digitarCredenciais(String username, String password) {
		inputUsuario.findWebElement().clear();
		inputUsuario.findWebElement().sendKeys(username);

		inputSenha.findWebElement().clear();
		inputSenha.findWebElement().sendKeys(password);

		return this;
	}

	public SigadocLoginPage logar() {
		inputUsuario.findWebElement().clear();
		inputUsuario.findWebElement().sendKeys(USERNAME);

		inputSenha.findWebElement().clear();
		inputSenha.findWebElement().sendKeys(SENHA);

		return this;
	}

	public SigadocLoginPage enviarAutenticacao() {
		inputSenha.findWebElement().submit();

		return this;
	}

	public SigadocLoginPage primeiroAcesso() {
		buttonPrimeiroAcesso.findWebElement().click();

		return this;
	}

}