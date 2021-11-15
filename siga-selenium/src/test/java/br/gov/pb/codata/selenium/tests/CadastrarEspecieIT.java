package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarEspecieIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de espécies > Incluir
	 * 
	 * @author Allysson Cruz
	 */

	@Test
	// Apenas para ZZZ1
	public void novaEspecie() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
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

	@Test
	// Apenas para ZZZ
	public void validarTodosOsCamposVazios() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/forma/editar?");

			driver.findElement(By.xpath("//button[@type='submit']")).click();
			driver.findElement(By.xpath("//h3[contains(.,'não é possível salvar um tipo sem informar a descrição.')]"));
		}

	}

	@Test
	// Apenas para ZZZ
	public void validarSigla() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/forma/editar?");

			// descricao
			driver.findElement(By.name("descricao")).sendKeys("testes 11");
			// sigla
			driver.findElement(By.id("gravar_sigla")).sendKeys("aqb22c");

			driver.findElement(By.xpath("//button[@type='submit']")).click();
			driver.findElement(By.xpath("//h3")).getCssValue("Sigla inválida. A sigla deve ser formada por 3 letras.");
		}
	}
}
