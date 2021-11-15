package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class PaginaInicialIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > pagina inicial
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void direcionarPaginaInicial() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/mesa");
		driver.findElement(By.linkText("Documentos")).click();
		driver.findElement(By.cssSelector(".show > li:nth-child(3) > .dropdown-item")).click();
	}
}
