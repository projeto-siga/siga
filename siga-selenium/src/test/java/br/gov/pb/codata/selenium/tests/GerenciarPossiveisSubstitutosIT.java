package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class GerenciarPossiveisSubstitutosIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Administracao > Gerenciar Novos Substitutos > incluir
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void novoSubstituto() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/substituicao/editar");
		driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
	    driver.findElement(By.id("titularSelButton")).click();
	    driver.switchTo().frame(0);
	    driver.findElement(By.linkText("COD10327")).click();
	    driver.switchTo().defaultContent();
	    driver.findElement(By.id("substitutoSelButton")).click();
	    {
	      WebElement element = driver.findElement(By.id("substitutoSelButton"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).perform();
	    }
	    {
	      WebElement element = driver.findElement(By.tagName("body"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element, 0, 0).perform();
	    }
	    driver.switchTo().frame(0);
	    driver.findElement(By.linkText("COD10103")).click();
	    driver.switchTo().defaultContent();
	    driver.findElement(By.id("dtIniSubst")).click();
	    driver.findElement(By.id("dtIniSubst")).sendKeys("09092020");
	    driver.findElement(By.id("dtFimSubst")).click();
	    driver.findElement(By.id("dtFimSubst")).sendKeys("10092020");
	    driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
	  }
}
