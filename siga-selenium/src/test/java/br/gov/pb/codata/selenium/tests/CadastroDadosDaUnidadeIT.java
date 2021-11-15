package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

/**
 * Caminho: Ferramentas > Cadastro da Unidade > Incluir/pesquisar/alterar
 * 
 * @author Allysson Cruz
 */

public class CadastroDadosDaUnidadeIT extends DriverBase {

	//@Test
	// Apenas para ZZZ
	public void incluirComSucesso() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "siga/app/lotacao/editar");
			System.out.println("O título da página é: " + driver.getTitle());
			// Seleciona o primeiro elemento do dropdown orgao
			Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
			comboOrgao.selectByValue("190401");
			// Insere texto no campo Nome
			driver.findElement(By.id("nmLotacao")).sendKeys("Testewu");
			// Insere texto no campo Sigla
			driver.findElement(By.id("siglaLotacao")).sendKeys("llp");
			// Seleciona a segunda opção do campo Localidade
			Select comboLocalidade = new Select(driver.findElement(By.name("idLocalidade")));
			comboLocalidade.selectByValue("1");
			// Seleciona Situacao Ativo
			driver.findElement(By.id("situacaoAtivo")).click();
			// Grava o documento
			driver.findElement(By.cssSelector(".col-sm-2:nth-child(1) .btn:nth-child(1)")).click();
		}
	}

	// @Test
	// Apenas para ZZZ
	public void CargoJaCadastrado() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "siga/app/lotacao/editar");
			System.out.println("O título da página é: " + driver.getTitle());
			// Seleciona o primeiro elemento do dropdown orgao
			Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
			comboOrgao.selectByValue("190401");
			// Insere texto no campo Nome
			driver.findElement(By.id("nmLotacao")).sendKeys("Testewu");
			// Insere texto no campo Sigla
			driver.findElement(By.id("siglaLotacao")).sendKeys("TER");
			// Seleciona a segunda opção do campo Localidade
			Select comboLocalidade = new Select(driver.findElement(By.name("idLocalidade")));
			comboLocalidade.selectByValue("1");
			// Seleciona Situacao Ativo
			driver.findElement(By.id("situacaoAtivo")).click();
			// Grava o documento
			driver.findElement(By.cssSelector(".col-sm-2:nth-child(1) .btn:nth-child(1)")).click();
			driver.findElement(By.xpath("//h3[contains(.,'Sigla já cadastrada para outra lotação')]"));
		}
	}

	@Test
	public void pesquisarComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/lotacao/listar");
		System.out.println("O título da página é: " + driver.getTitle());
		// Seleciona o primeiro elemento do dropdown orgao
		Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
		comboOrgao.selectByValue("190401");
		driver.findElement(By.xpath("//input[@value='Pesquisar']"));
	}

	@Test
	public void alterarComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		// Pesquisa primeiro
		driver.get(System.getenv("PBDOC_URL") + "siga/app/lotacao/listar");
		System.out.println("O título da página é: " + driver.getTitle());
		// Seleciona o primeiro elemento do dropdown orgao
		Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
		comboOrgao.selectByValue("190401");
		driver.findElement(By.xpath("//input[@value='Pesquisar']"));
		// Depois altera
		driver.get(System.getenv("PBDOC_URL") + "siga/app/lotacao/editar?id=22");
		System.out.println("O título da página é: " + driver.getTitle());
		// Insere texto no campo Nome
		driver.findElement(By.name("nmLotacao")).sendKeys("Teste CCdCfffwf");
		// Grava o documento
		driver.findElement(By.cssSelector(".col-sm-2:nth-child(1) .btn:nth-child(1)")).click();
	}
}
