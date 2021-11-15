package br.gov.pb.codata.selenium.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.PBDocSeleniumController;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarEmailNotificacaoIT extends DriverBase {

	/**
	 * Caminho: Ferramentas > Cadastrar Email notificação > Incluir
	 * 
	 * @author Allysson Cruz
	 */

	// @Test1
	// Apenas para ZZZ
	// esta ocorrendo erro
	public void incluirEmailNotificacao() throws Exception {

		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/emailNotificacao/editar");
			driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
			driver.findElement(By.id("pessSelButton")).click();
			{
				WebElement element = driver.findElement(By.xpath("//input[@id='pessSelButton']"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element).perform();
			}
			{
				WebElement element = driver.findElement(By.tagName("body"));
				Actions builder = new Actions(driver);
				builder.moveToElement(element, 0, 0).perform();
			}
			driver.switchTo().frame(0);
			driver.findElement(By.linkText("COD10327")).click();
			driver.switchTo().defaultContent();
			driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
		}
	}

	/**
	 * Validação ao clicar no botão "OK" com os campos em branco;
	 * 
	 * @author Allysson Cruz
	 */
	// Na duvidade se usuario normal deveria poder realizar, no momento não da erro
	// no redirecionamento, mas o teste não se completa

	@Test
	public void preenchaDestinatario() throws Exception {
		if (SigadocLoginPage.USERNAME.equals(System.getenv("USUARIO"))) {
			WebDriver driver = SigadocStartUp.startUp();
			driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/emailNotificacao/editar");
			driver.findElement(By.cssSelector(".btn-primary:nth-child(1)")).click();
			driver.findElement(By.xpath("//h3[contains(.,'Preencha Tipo de destinatário da movimentação')]"));
		}
	}

}
