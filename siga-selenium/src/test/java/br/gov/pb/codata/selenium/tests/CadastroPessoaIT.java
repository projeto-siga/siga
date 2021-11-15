package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastroPessoaIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastro de Pessoa > Incluir
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	public void incluirComSucesso() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");
		Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
		comboOrgao.selectByValue("190401");
		// cargo
		Select comboCargo = new Select(driver.findElement(By.name("idCargo")));
		comboCargo.selectByValue("0");
		// funcao de confianca
		Select comboFuncao = new Select(driver.findElement(By.name("idFuncao")));
		comboFuncao.selectByValue("0");
		// lotacao
		Select comboUnidade = new Select(driver.findElement(By.name("idLotacao")));
		comboUnidade.selectByValue("0");
		// nome
		driver.findElement(By.id("nmPessoa")).sendKeys("Nomesste");
		driver.findElement(By.id("dtNascimento")).sendKeys("0912d954");
		// cpf
		driver.findElement(By.id("cpf")).sendKeys("142.997.410-99");
		// email
		driver.findElement(By.id("email")).sendKeys("emasssl@gmail.com");
		// verificar gravar
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
	}

	@Test
	public void ValidarNome() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");
		// gravar
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		driver.findElement(By.xpath("//div[@id='alertaModal']/div/div/div[2]"));
	}

	@Test
	public void ValidarCargo() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");
		Select comboOrgao = new Select(driver.findElement(By.name("idOrgaoUsu")));
		comboOrgao.selectByValue("190401");
		// nome
		driver.findElement(By.id("nmPessoa")).sendKeys("Nomesste");
		driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
		driver.findElement(By.xpath("//div[@id='alertaModal']/div/div/div[2]"));
	}

	@Test
	public void pesquisarPessoa() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/listar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".col-sm-2 > .btn")).click();
	}

	@Test
	public void alterarPessoa() throws Exception {
		WebDriver driver = SigadocStartUp.startUp();

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/listar");
		driver.manage().window().setSize(new Dimension(1440, 812));
		driver.findElement(By.cssSelector(".col-sm-2 > .btn")).click();
		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar?id=66");
		// nome
		driver.findElement(By.id("nmPessoa")).sendKeys("Nome ccc");
		driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
	}
}
