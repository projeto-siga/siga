package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MemorandoPage extends EditaDocumentoPage {
	
	@FindBy(id="scayt_0")
	private WebElement frameMemorando;
		
	public MemorandoPage(WebDriver driver) {
		super(driver);
	}
	
	public void criaMemorando(Properties propDocumentos) {
		preencheDocumentoInterno(propDocumentos, "Memorando", "Memorando", propDocumentos.getProperty("internoProduzido"), Boolean.TRUE);	
		botaoOk.click();
	}
/*	public void criaMemorando() {	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//js.executeScript("document.getElementById('texto_memorando').style.visibility='visible';");
		//js.executeScript("document.getElementById('texto_memorando').style.display='inline';");

		driver.switchTo().frame(frameMemorando);
		System.out.println("Pagesource: " + driver.getPageSource());
		
		driver.findElement(By.xpath("/html/body/p/span")).clear();
		driver.findElement(By.xpath("/html/body/p/span")).sendKeys("Vitória!!!!!!!!!!");
	}*/
}
