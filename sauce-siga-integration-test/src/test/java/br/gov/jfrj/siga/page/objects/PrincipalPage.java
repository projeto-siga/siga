package br.gov.jfrj.siga.page.objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class PrincipalPage {

	protected WebDriver driver;
	
	@FindBy(css="a.gt-btn-small.gt-btn-right") 
	//@FindBy(partialLinkText="Novo Documento")
	private WebElement botaoNovoDocumentoEx;
	
	@FindBy(id="buscar_genericoSel_sigla")
	private WebElement caixaBusca;
	
	private IntegrationTestUtil util;
	
	public PrincipalPage(WebDriver driver) {	
		this.driver = driver;
		util = new IntegrationTestUtil();
		
		// Verificando se as divs com informações de documento foram carregadas
		List<WebElement> divs = driver.findElements(By.cssSelector("div.gt-content-box.gt-for-table"));
		for (WebElement webElement : divs) {
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.click();
		}
		
		// O método getwebElement foi modificado para aceitar o tempo de espera parametrizado visto 
		// que todos os comandos foram definidos com 30 segundos. Isto ocorre porque na primeira vez 
		// que se acessa a página principal, logo depois que o app.server é criado, existe uma demora 
		// adicional devido ao hibernate e isto pode gerar um tempo superior a 30s para aparecer o botão Novo Documento
		if(util.getWebElementParametrizado(driver, By.cssSelector("a.gt-btn-small.gt-btn-right"), 70) == null) {
		//if(util.getWebElement(driver, By.cssSelector("a.gt-btn-small.gt-btn-right")) == null) {
		  	System.out.println("==> Botão Novo Documento não encontrado na página principal");
			throw new IllegalStateException("Esta não é a página principal!");
		}
	}
	
	public void clicarBotaoNovoDocumentoEx() {
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(botaoNovoDocumentoEx));
		String URL = driver.getCurrentUrl();
		botaoNovoDocumentoEx.click();
		try {
			new WebDriverWait(driver, 30).until(util.trocaURL(URL));	
		} catch (TimeoutException e) {
			System.out.println("TimeOut aguardando a página de novo documento ser exibida. Tentando clicar novamente...");
			botaoNovoDocumentoEx.click();
		}
	}
	
	public void buscarDocumento(String codigoDocumento) {
		util.preencheElemento(driver, caixaBusca, codigoDocumento);
		caixaBusca.sendKeys(Keys.ENTER);
		//util.getWebElement(driver, By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/img")).click();
	}
}
