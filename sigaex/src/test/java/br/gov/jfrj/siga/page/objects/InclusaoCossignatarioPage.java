package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InclusaoCossignatarioPage {	
	private WebDriver driver;
	
	@FindBy(id="incluir_cosignatario_gravar_cosignatarioSel_sigla")
	private WebElement cossignatario;
	
	@FindBy(id="incluir_cosignatario_gravar_funcaoCosignatario")
	private WebElement funcaoLocalidade;
	
	@FindBy(css="input.gt-btn-small.gt-btn-left")
	private WebElement botaoOk;
	
	public InclusaoCossignatarioPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void incluiCossignatario(Properties propDocumento) {
		cossignatario.clear();
		cossignatario.sendKeys(propDocumento.getProperty("siglaDestinatario"));		
		funcaoLocalidade.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("cosignatarioSelSpan")));
		funcaoLocalidade.clear();
		funcaoLocalidade.sendKeys(propDocumento.getProperty("funcaoLocalidade"));
		botaoOk.click();
	}
	
}
