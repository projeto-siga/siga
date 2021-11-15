package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class IdentidadesIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Gestao de identidade > Identidade
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void BuscarIdentidadesCadastradas() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/gi/identidade/listar");
		driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
	}
}
