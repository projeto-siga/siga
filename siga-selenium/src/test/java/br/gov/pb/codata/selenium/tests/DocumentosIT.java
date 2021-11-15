package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class DocumentosIT extends DriverBase {

	/**
	 * Caminho: Menu Modulos > Documentos
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void direcionarPesquisarDocumentos() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/mesa");
		driver.findElement(By.linkText("MENU PRINCIPAL")).click();
		driver.findElement(By.linkText("Módulos")).click();
		driver.findElement(By.linkText("Documentos")).click();
	}
}
