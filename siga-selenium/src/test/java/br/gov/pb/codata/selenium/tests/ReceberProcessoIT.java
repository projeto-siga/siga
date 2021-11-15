package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;

public class ReceberProcessoIT extends DriverBase{
	

	/**
	 * Caminho: Menu Documentos > assinar > finalizar e encaminhar
	 * Objetivo: TEstar se outro usuario esa recebendo o ducumento
	 * 
	 * @author Allysson Cruz
	 */
		

	//@Test
    public void receceberProcesso() throws Exception {

    	WebDriver driver = getDriver();
    	
        driver.manage().window().setSize(new Dimension(1024, 768));
        
        driver.get(System.getenv("PBDOC_URL"));
        
        SigadocLoginPage loginPage = new SigadocLoginPage();

        System.out.println("O título da página é: " + driver.getTitle());

        loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

        WebDriverWait wait = new WebDriverWait(driver, 10, 100);
        
        System.out.println("O título da página é: " + driver.getTitle());
                
        driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
        
        driver.manage().window().setSize(new Dimension(1440, 812));
        driver.findElement(By.cssSelector(".selected-label")).click();
        driver.findElement(By.linkText("OfícioOfício")).click();
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.idTpDoc"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).clickAndHold().perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.idTpDoc"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.idTpDoc"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).release().perform();
        }
        driver.findElement(By.name("exDocumentoDTO.idTpDoc")).click();
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).clickAndHold().perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).release().perform();
        }
        driver.findElement(By.name("exDocumentoDTO.nivelAcesso")).click();
        driver.findElement(By.id("subscritorSelButton")).click();
        {
          WebElement element = driver.findElement(By.id("subscritorSelButton"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.tagName("body"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element, 0, 0).perform();
        }
        driver.switchTo().frame(1);
        driver.findElement(By.linkText("CDT10182")).click();
        driver.switchTo().defaultContent();
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.tipoDestinatario"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).clickAndHold().perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.tipoDestinatario"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.name("exDocumentoDTO.tipoDestinatario"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).release().perform();
        }
        driver.findElement(By.name("exDocumentoDTO.tipoDestinatario")).click();
        driver.findElement(By.id("lotacaoDestinatarioSelButton")).click();
        driver.switchTo().frame(1);
        driver.findElement(By.linkText("CDTTST")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.id("classificacaoSelButton")).click();
        {
          WebElement element = driver.findElement(By.id("classificacaoSelButton"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.tagName("body"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element, 0, 0).perform();
        }
        driver.switchTo().frame(1);
        driver.findElement(By.linkText("01.03.02.03")).click();
        driver.switchTo().defaultContent();
        driver.findElement(By.id("descrDocumento")).click();
        driver.findElement(By.id("descrDocumento")).sendKeys("teste1");
        driver.switchTo().frame(0);
        driver.findElement(By.cssSelector("html")).click();
        {
        }
        driver.switchTo().defaultContent();
        driver.findElement(By.name("gravar")).click();
        driver.findElement(By.cssSelector(".btn:nth-child(2) > u")).click();
        driver.findElement(By.id("ad_password_0")).click();
        driver.findElement(By.id("bot-assinar")).click();
      
        
        
        driver.findElement(By.id("#nomeUsuarioSubscritor")).sendKeys("CDT10182");
        							
        
        driver.findElement(By.id("senhaUsuarioSubscritor")).sendKeys("Charlote123");
        driver.findElement(By.xpath("//button[@id='senhaOk']")).click();
        
        
        
        
        
        driver.findElement(By.linkText("Incluir Documento")).click();
        driver.findElement(By.id("lotacaoDestinatarioSelButton")).click();
        
        
        
        
        {
          WebElement element = driver.findElement(By.id("lotacaoDestinatarioSelButton"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element).perform();
        }
        {
          WebElement element = driver.findElement(By.tagName("body"));
          Actions builder = new Actions(driver);
          builder.moveToElement(element, 0, 0).perform();
        }
        driver.switchTo().frame(1);
        driver.findElement(By.linkText("CDTTST")).click();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(0);
        driver.findElement(By.cssSelector("html")).click();
        {
          WebElement element = driver.findElement(By.cssSelector(".cke_editable"));
        }
        driver.switchTo().defaultContent();
        driver.findElement(By.name("gravar")).click();
      }
}
