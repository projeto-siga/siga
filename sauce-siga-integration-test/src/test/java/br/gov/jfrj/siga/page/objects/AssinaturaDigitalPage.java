package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class AssinaturaDigitalPage {
	private WebDriver driver;
	
	@FindBy(id="cmdAssinar")
	private WebElement botaoAssinar;
	
	private IntegrationTestUtil util;
	
	public AssinaturaDigitalPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
		
		if(!util.isDescricaoPaginaVisivel(driver,"Confirme os dados do documento abaixo:")) {
			throw new IllegalStateException("Esta não é a página de Assinatura Digital!");
		}
	}
	
	public OperacoesDocumentoPage registrarAssinaturaDigital(String baseURL, String codigoDocumento) {
		driver.get(baseURL + "/sigaex/expediente/mov/simular_assinatura.action?sigla=" + codigoDocumento);	
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
	public Boolean isIdentificacaoDocumentoVisivel(String textoBuscado) {
		return util.getWebElement(driver, By.xpath("//p[contains(., '" + textoBuscado + "')]")) != null;
	}
}
