package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastroClassificacaoDocumentalIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Novo Assunto/Classificação
	 * 
	 * @author Allysson Cruz
	 */

	//@Test
	// Apenas para ZZZ
	public void cadastrarComSucesso() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL")
					+ "sigaex/app/expediente/classificacao/editar?acao=nova_classificacao");
			System.out.println("O título da página é: " + driver.getTitle());
			// Insere texto no campo Codificacao
			driver.findElement(By.id("codificacao")).sendKeys("0011d293213");
			// Insere texto no campo Descricao
			driver.findElement(By.id("descrClassificacao")).sendKeys("dda nda cl3assificacao");
			// Insere texto no campo Observacao
			driver.findElement(By.id("obs")).sendKeys("a noda classific3acao");
			// Clica no botao Gravar
			driver.findElement(By.id("btGravarClassificacao")).click();
		}
	}
}