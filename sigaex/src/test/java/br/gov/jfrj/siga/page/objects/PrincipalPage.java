package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PrincipalPage {

	protected WebDriver driver;
	
	@FindBy(css="a.gt-btn-small.gt-btn-right")
	private WebElement botaoNovoDocumentoEx;
	
	public void clicarBotaoNovoDocumentoEx() {
		botaoNovoDocumentoEx.click();
	}
}
