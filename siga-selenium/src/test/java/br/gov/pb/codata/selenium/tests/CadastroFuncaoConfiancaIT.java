package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastroFuncaoConfiancaIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de Função de confiança >
	 * Incluir/pesquisar/alterar
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void incluirComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/listar");
		driver.findElement(By.cssSelector(".gt-table-buttons > .btn")).click();
		driver.findElement(By.id("nmFuncao")).click();
		driver.findElement(By.id("nmFuncao")).sendKeys("testeoke55");
		driver.findElement(By.cssSelector(".row:nth-child(3) .btn:nth-child(1)")).click();
	}

	// @Test
	public void AlertaPreencherNome() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/editar");
		Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown.selectByValue("190401");
		// nome
		driver.findElement(By.id("nmFuncao")).sendKeys("te");
		// clica no botao ok
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
		// Mensagem de incluido com sucesso
		driver.findElement(By.xpath("//div[@id='alertaModal']/div/div/div[2]"));
	}

	/**
	 * Primeiro cadastra e depois tenta cadastrar com o mesm nome
	 */

	// @Test
	// Apenas para ZZZ
	public void FuncaoJaCadastrada() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/editar");
			Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
			dropdown.selectByValue("190401");
			// nome
			driver.findElement(By.id("nmFuncao")).sendKeys("testeoke5");
			// Clica no botao ok
			driver.findElement(By.xpath("//input[@value='Ok']")).click();
			// Mensagem
			driver.findElement(By.xpath("//h3[contains(.,'Nome da função já cadastrado!')]"));
		}
	}

	@Test
	public void pesquisarComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/listar");
		Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown.selectByValue("190401");
		// nome vazio para vim todos os ja cadastrados
		driver.findElement(By.id("nome")).sendKeys("te");
		driver.findElement(By.xpath("//input[@value='Pesquisar']")).click();
	}

	@Test
	public void alterarComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/listar");
		Select dropdown = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown.selectByValue("190401");
		// nome vazio para vim todos os ja cadastrados
		driver.findElement(By.id("nome")).sendKeys("te");
		driver.findElement(By.xpath("//input[@value='Pesquisar']")).click();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/funcao/editar?id=124");
		Select dropdown1 = new Select(driver.findElement(By.name("idOrgaoUsu")));
		dropdown1.selectByValue("190401");
		// nome
		driver.findElement(By.id("nmFuncao")).sendKeys("Nome8a4da9");
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}
}
