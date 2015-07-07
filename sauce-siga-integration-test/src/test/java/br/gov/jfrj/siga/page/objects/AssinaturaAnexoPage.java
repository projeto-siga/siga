package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class AssinaturaAnexoPage {
	private WebDriver driver;
	
	private IntegrationTestUtil util;
	
	public AssinaturaAnexoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
		
		util.openPopup(driver);
		if(util.getWebElement(driver, By.xpath("//b[contains(text(), 'Link para assinatura externa')]")) == null) {
			util.closePopup(driver);
			throw new RuntimeException("Esta não é a página de Assinatura de Anexo!");
		}
	}
	
	
	public OperacoesDocumentoPage assinarCopia(String baseURL, String codigoDocumento) {		
		String codigoAnexo;
				
		try {
			String urlPopup = driver.getCurrentUrl();
			System.out.println("URL: " + driver.getCurrentUrl());
			codigoAnexo = urlPopup.substring(urlPopup.indexOf("id=")+3, urlPopup.indexOf("&"));
			System.out.println("Código anexo: " + codigoAnexo);		
		} finally {
			util.closePopup(driver);
		}

		driver.get(baseURL + "/sigaex/expediente/mov/simular_assinatura_mov.action?sigla="+ codigoDocumento + "&id="+codigoAnexo);				
		System.out.println("URL: " + driver.getCurrentUrl());
		
		return PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}
	
}
