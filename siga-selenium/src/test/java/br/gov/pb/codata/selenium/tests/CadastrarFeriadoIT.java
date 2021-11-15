package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarFeriadoIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de feriados > Incluir
	 * 
	 * @author Allysson Cruz
	 */

	// com erro (refazer)
	// @Test1
	public void novaEspecie() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();

		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/forma/editar?");

		// descricao
		driver.findElement(By.name("descricao")).sendKeys("tesl");
		// sigla
		driver.findElement(By.id("gravar_sigla")).sendKeys("abo");
		Select comboTipo = new Select(driver.findElement(By.name("idTipoFormaDoc")));
		comboTipo.selectByValue("1");
		driver.findElement(By.name("origemExterno")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
}
