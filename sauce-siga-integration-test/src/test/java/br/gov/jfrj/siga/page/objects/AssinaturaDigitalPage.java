package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AssinaturaDigitalPage {
	private WebDriver driver;
	
	@FindBy(id="cmdAssinar")
	private WebElement botaoAssinar;
	
	public AssinaturaDigitalPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void registrarAssinaturaDigital(String baseURL, String codigoDocumento) {
		driver.get(baseURL + "/sigaex/expediente/mov/simular_assinatura.action?sigla=" + codigoDocumento);		
	}
}
