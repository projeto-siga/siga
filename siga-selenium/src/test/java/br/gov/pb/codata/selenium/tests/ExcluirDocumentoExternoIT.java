package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class ExcluirDocumentoExternoIT extends DriverBase {

	/**
	 * Caminho: Criar um processo generico, depois ir na aba "Excluir"
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void excluirDocumento() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("caso de excluir");
		driver.findElement(By.name("gravar")).click();
		driver.findElement(By.linkText("Excluir")).click();
		driver.switchTo().alert().accept();
	}
}
