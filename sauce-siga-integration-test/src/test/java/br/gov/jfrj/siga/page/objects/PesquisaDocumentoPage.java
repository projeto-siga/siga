package br.gov.jfrj.siga.page.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class PesquisaDocumentoPage {
	private WebDriver driver;
	
	@FindBy(id="ultMovIdEstadoDoc")
	private WebElement situacao;
	
	@FindBy(id="descrDocumento")
	private WebElement descricao;
	
	@FindBy(id="forma")
	private WebElement tipo;
	
	@FindBy(id="tipoCadastrante")
	private WebElement tipoCadastrante;
	
	@FindBy(id="lotaCadastranteSel_sigla")
	private WebElement lotacaoCadastrante;
	
	@FindBy(xpath="//input[@value='Buscar']")
	private WebElement botaoBuscar;
	
	private IntegrationTestUtil util;
	
	public PesquisaDocumentoPage(WebDriver driver) {
		this.driver = driver;
		util= new IntegrationTestUtil();		
	}
	
	public String buscarDocumento(String codigoDocumento, String situacaoDocumento) {		
		System.out.println("Handle buscar: " + driver.getWindowHandle());
		System.out.println("URL: " + driver.getCurrentUrl());
		new WebDriverWait(driver, 30).until(ExpectedConditions.titleIs("SIGA - Lista de Expedientes"));
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("ultMovLotaRespSelSpan")));
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoBuscar));
		descricao.click();
		util.getSelect(driver, situacao).selectByVisibleText(situacaoDocumento);
		botaoBuscar.click();
		WebElement element = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[1]/a[not(contains(., '" +codigoDocumento+"'))]")));
		String codigoDocumentoApensado = element.getText();
		element.click();
		
		return codigoDocumentoApensado;
	}
	
	public void buscaPortaria() {
		util.getSelect(driver, situacao).selectByVisibleText("Aguardando Andamento");
		util.getSelect(driver, tipo).selectByVisibleText("Portaria");
		util.getSelect(driver, tipoCadastrante).selectByValue("2");
		util.preencheElemento(driver, lotacaoCadastrante, "SESIA");
		lotacaoCadastrante.sendKeys(Keys.TAB);
		botaoBuscar.click();
		
		util.getClickableElement(driver, By.xpath("//td[1]/a")).click();
	}
}
