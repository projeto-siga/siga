package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class ReclassificacaoIT extends DriverBase{
	
	/**
	 * Caminho: Criar um processo generico, depois ir na aba "Reclassificacao"
	 * 
	 * @author Allysson Cruz
	 */
	
	//ajeitar o assinar
	//@Test
    public void reclassificarDocumeto() throws Exception {

    	WebDriver driver = SigadocStartUp.startUp();
        driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
        driver.findElement(By.cssSelector(".selected-label")).click();
        driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
        {
          WebElement dropdown = driver.findElement(By.name("exDocumentoDTO.nivelAcesso"));
          dropdown.findElement(By.xpath("//option[. = 'Limitado ao órgão (padrão)']")).click();
        }
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
        driver.findElement(By.id("descrDocumento")).click();
        driver.findElement(By.id("descrDocumento")).sendKeys("tesateacesso");
        driver.findElement(By.cssSelector(".btn-primary > u")).click();
      }
}
