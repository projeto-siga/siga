/**
package br.gov.pb.codata.selenium.tests;

import static br.gov.pb.codata.selenium.config.DriverSetup.HOMEPAGE_URL_PBDIGITAL;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;

* @author Thomas Ribeiro

public class AgendamentoPublicacaoIT  extends DriverBase{
	
	////@Test
    public void agendamentoPublicacao() throws Exception {

    	WebDriver driver = getDriver();
    	
        driver.manage().window().setSize(new Dimension(1024, 768));
        
        driver.get(HOMEPAGE_URL_PBDIGITAL);
        
        SigadocLoginPage loginPage = new SigadocLoginPage();

        System.out.println("O título da página é: " + driver.getTitle());

        loginPage.digitarCredenciais(System.getenv("USUARIO"), "Password1").enviarAutenticacao();

        WebDriverWait wait = new WebDriverWait(driver, 10, 100);
        
        System.out.println("O título da página é: " + driver.getTitle());
                
        driver.get(HOMEPAGE_URL_PBDIGITAL + "sigaex/app/expediente/doc/editar");
        
        wait = new WebDriverWait(driver, 25, 100);
       
  		//Insere texto no campo Data
  		driver.findElement(By.name("dt_dispon")).sendKeys("28/02/2020");
  		
  		
  		//lotacao publicacao
  		driver.findElement(By.id("formulario_lotaSubscritorSel_sigla")).click();
  		
  		
  		//Descreve publicacao
  		driver.findElement(By.id("descrPublicacao")).sendKeys("Descriscaoo publicação");
	  		
  		

  		//clicar no botão ok
  		driver.findElement(By.id("ok")).submit();
  		
    	}
    

}*/

