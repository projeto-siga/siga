package br.gob.jfrj.siga.tp.test.selenium.ferramentas;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Pagina {
	private WebDriver driver;
	private Browser browser;

	public Pagina() throws Exception {
		this(Browser.FIREFOX);
	}

	public Pagina(Browser browser) throws Exception {
		this.browser = browser;
		driver = this.browser.getDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public boolean contemTexto(String texto) {
		return driver.getPageSource().contains(texto);
	}

	public boolean contemTextos(String... textos) {
		boolean retorno = true;
		for (int i = 0; i < textos.length; i++) {
			retorno = retorno && this.contemTexto(textos[i]);
		}
		return retorno;
	}

	public void finalizar() {
		driver.quit();
	}

	public void redirecionarViaJavaScript(String caminho) {
		((JavascriptExecutor) driver).executeScript("window.location = '" + caminho + "';");
	}

	public void clicarElementoPorId(String nomeComponente) {
		driver.findElement(By.id(nomeComponente)).click();
	}

	public void acessarViaGet(String url) {
		driver.get(url);
	}

	public void preencherCampoFormulario(String nome, String valor) {
		driver.findElement(By.name(nome)).sendKeys(valor);
	}

	public void clicarElementoPorXpath(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}

	public void clicarElementoPorSeletorCss(String seletorCss) {
		driver.findElement(By.cssSelector(seletorCss)).click();
	}
}