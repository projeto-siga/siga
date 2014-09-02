package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PesquisaDocumentoPage {
	private WebDriver driver;
	
	@FindBy(xpath="//input[@value='Buscar']")
	private WebElement botaoBuscar;
	
	public PesquisaDocumentoPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void buscarDocumento(String codigoDocumento) {		
		System.out.println("Handle buscar: " + driver.getWindowHandle());
		System.out.println("URL: " + driver.getCurrentUrl());
		new WebDriverWait(driver, 15).until(ExpectedConditions.titleIs("SIGA - Lista de Expedientes"));
		new WebDriverWait(driver, 15).until(ExpectedConditions.elementToBeClickable(botaoBuscar));
		botaoBuscar.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[1]/a[not(contains(., '" +codigoDocumento+"'))]"))).click();
	}
}
