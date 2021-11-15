package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class EnvioEmailNovosUsuariosIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Gestao de identidades > Enviar email Objetivo:
	 * Enviar email para novos usuarios criados
	 * 
	 * @author Allysson Cruz
	 */

	// @Test1
	// só roda uma vez
	public void envioEmailNovosUsuarios() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/enviarEmail");
		driver.findElement(By.id("nome")).click();
		driver.findElement(By.id("nome")).sendKeys("allysson");
		driver.findElement(By.cssSelector(".col-sm-2 > .btn")).click();
		driver.findElement(By.cssSelector(".gt-table-buttons > .btn")).click();
		driver.findElement(By.cssSelector("#exampleModal .btn-primary")).click();
	}
}
