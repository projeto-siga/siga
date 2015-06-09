package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class AssinaturaAnexoPage {
	private WebDriver driver;

	private IntegrationTestUtil util;

	public AssinaturaAnexoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}


	public void assinarCopia(String baseURL, String codigoDocumento) {
		String codigoAnexo;

		util.openPopup(driver);

		try {
			String urlPopup = driver.getCurrentUrl();
			System.out.println("URL: " + driver.getCurrentUrl());
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//b[contains(text(), 'Link para assinatura externa')]")));
			codigoAnexo = urlPopup.substring(urlPopup.indexOf("id=")+3, urlPopup.indexOf("&"));
			System.out.println("Código anexo: " + codigoAnexo);		
		} finally {
			util.closePopup(driver);
		}

		driver.get(baseURL + "/sigaex/app/expediente/mov/simular_assinatura_mov?sigla="+ codigoDocumento + "&id="+codigoAnexo);
		System.out.println("URL: " + driver.getCurrentUrl());
	}

}
