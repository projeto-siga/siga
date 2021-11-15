package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarNovoProcessoPersonalizarIT extends DriverBase {

	/**
	 * Caminho: Menu Principal > Ferramentas > Cadastro documento com personalizar
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void incluirComSucessoPersonializar() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/editar");
		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.name("exDocumentoDTO.personalizacao")).click();
		driver.findElement(By.id("personalizarFuncao")).click();
		driver.findElement(By.id("personalizarFuncao")).sendKeys("teste");
		driver.findElement(By.id("personalizarUnidade")).sendKeys("uni");
		driver.findElement(By.id("personalizarLocalidade")).sendKeys("joao pesoa");
		driver.findElement(By.id("personalizarNome")).sendKeys("teste allysson");
		driver.findElement(By.id("descrDocumento")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("personalizar");
		driver.findElement(By.name("gravar")).click();
	}
}
