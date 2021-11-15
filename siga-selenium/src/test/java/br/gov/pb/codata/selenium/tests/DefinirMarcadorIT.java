package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class DefinirMarcadorIT extends DriverBase {

	/**
	 * Caminho: Criar um processo generico, depois ir na aba "definir Marcador"
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void definirMarcador() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("teste");
		driver.findElement(By.name("gravar")).click();
		driver.findElement(By.linkText("Definir Marcador")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) input:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".mt-3")).click();
	}

	/**
	 * Caminho: marca a opçao no marcador depois desmarca
	 * 
	 * @author Allysson Cruz
	 */

	@Test
	public void desfazerMarcação() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("definir marcador, marcar e depois desmarcar");
		driver.findElement(By.cssSelector(".btn-primary > u")).click();
		driver.findElement(By.linkText("Definir Marcador")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) input:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".mt-3")).click();
		driver.findElement(By.linkText("Definir Marcador")).click();
		driver.findElement(By.cssSelector("tr:nth-child(1) input:nth-child(1)")).click();
		driver.findElement(By.cssSelector(".mt-3")).click();
	}
}
