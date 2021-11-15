package br.gov.pb.codata.selenium.tests;

import java.sql.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.CadastroTipoDespachoPage01;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;
import io.appium.java_client.functions.ExpectedCondition;

/**
 * Caminho: Ferramentas > Cadastro Tipo DEspacho > Incluir
 * 
 * @author Allysson Cruz
 */

public class CadastroTipoDespachoIT extends DriverBase {

	//@Test
	// Apenas para ZZZ
	public void cadastrarComSucesso() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/despacho/tipodespacho/editar?");
			driver.findElement(By.name("exTipoDespacho.descTpDespacho")).click();
			driver.findElement(By.name("exTipoDespacho.descTpDespacho")).sendKeys("teste");
			driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
		}
	}

	private ExpectedCondition<Boolean> paginaDeveMostrarTexto(final String txt) {
		return driver -> driver.findElement(By.cssSelector("td:contains(\"" + txt + "\")")).getText()
				.equalsIgnoreCase(txt);
	}

	public void cadastrarComSucesso1() throws Exception {

		WebDriver driver = getDriver();
		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		// checa title da página de login
		System.out.println("O título da página é: " + driver.getTitle());

		loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

		driver.get(CadastroTipoDespachoPage01.URL);

		CadastroTipoDespachoPage01 tipoDespachoPage = new CadastroTipoDespachoPage01();

		System.out.println("O título da página é ..: " + driver.getTitle());

		String dados = "TESTE " + new Date(0).getTime();

		tipoDespachoPage.digitarDados(dados).enviarForm();

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		// wait.until(paginaDeveMostrarTexto(dados));

		// System.out.println("Na tabela foi encontrado:
		// "+driver.findElement(By.cssSelector("td:contains(\""+dados+"\")")).getText());

		System.out.println("O título da página é: " + driver.getTitle());
	}

}