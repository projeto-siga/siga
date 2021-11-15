package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocMesaVirtualPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

public class CadastrarFinalizarDocumentoIT {

	/**
	 * Objetivo: Criar novo processo administrativo, anexar documento e finalizar
	 * 
	 * @author Thomas Ribeiro
	 */

	private ExpectedCondition<Boolean> pageTitleStartsWith(final String searchString) {
		return driver -> driver.getTitle().toLowerCase().startsWith(searchString.toLowerCase());
	}

	//@Test
	public void cadastrarDocumento() throws Exception {
	

		WebDriver driver = SigadocStartUp.startUp();

		SigadocMesaVirtualPage mesaVirtualPage = new SigadocMesaVirtualPage();
		mesaVirtualPage.clicarCriarNovo();

		driver.findElement(By.cssSelector(".selected-label")).click();
		driver.findElement(By.linkText("Processo AdministrativoGenérico")).click();
		driver.findElement(By.id("descrDocumento")).sendKeys("testeProcessoAdministrativo");
		WebElement dropdown = driver.findElement(By.cssSelector("select.form-control"));
		dropdown.findElement(By.xpath("//*[@id='frm']/div[7]/div[1]/div/select/option[1]")).click();//Destinatário será um usuário
		driver.findElement(By.cssSelector("input#destinatarioSelButton.btn.btn-secondary")).click();//Define o destinatário
		{
			WebElement element = driver.findElement(By.cssSelector("input#destinatarioSelButton.btn.btn-secondary"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		Select select = new Select(driver.findElement(By.cssSelector("select.form-control")));
		select.selectByVisibleText("CODATA - CIA DE PROCESSAMENTO DE DADOS DA PARAIBA");//Define o orgão da pesquisa
		driver.findElement(By.cssSelector("input.form-control")).sendKeys(System.getenv("USUARIO"));//Busca um nome de usuário
		driver.findElement(By.cssSelector("button.btn.btn-primary")).submit();//Clica no botão de pesquisa
		driver.findElement(By.linkText(System.getenv("USUARIO"))).click();
		driver.switchTo().defaultContent();

		driver.findElement(By.xpath("//*[@id='classificacaoSelButton']")).click();//Seleciona a classificação
		{
			WebElement element = driver.findElement(By.xpath("//*[@id='classificacaoSelButton']"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("01.03.02.03")).click();
		driver.switchTo().defaultContent();

		driver.findElement(By.cssSelector(".btn-primary > u")).click();//Cria o documento

		driver.findElement(By.xpath("//*[@id='frm']/div[12]/div/button[2]")).click();//Finaliza o documento
		//*[@id='frm']/div[12]/div/button[2]"
		String docTitle = driver.getTitle();

		assinar(driver);//Assinar o documento para movimentar

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith(docTitle));

		newLogin(driver, SigadocLoginPage.USERNAME, SigadocLoginPage.SENHA);//Loga-se com o usuário ao qual foi encaminhado o documento

		String sigla = docTitle.substring(8).replace("-", "").replace("/", "");
		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/doc/exibir?sigla=" + sigla);

		newLogin(driver, SigadocLoginPage.USERNAME, SigadocLoginPage.SENHA);//Reloga-se com o usuário inicial

	}

	private void assinar(WebDriver driver) {
		driver.findElement(By.xpath("//*[@id='page']/div[2]/div/p/a[3]")).click();//Assina o documento

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith("PBDoc - Documento"));

		driver.findElement(By.xpath("//*[@id='ad_password_0']")).click();//Movimentar com senha

		driver.findElement(By.xpath("//*[@id='bot-assinar']")).click();//Assinar

		{
			WebElement element = driver.findElement(By.xpath("//*[@id='bot-assinar']"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.xpath("//*[@id='senhaUsuarioSubscritor']")).sendKeys(SigadocLoginPage.SENHA);//Informa a senha

		driver.findElement(By.xpath("//*[@id='senhaOk']")).click();//Ok
	}

	private void newLogin(WebDriver driver, String username, String password) throws Exception {
		driver.findElement(By.linkText("Sair")).click();//Realiza o logout da sessão

		WebDriverWait wait = new WebDriverWait(driver, 15, 100);
		wait.until(pageTitleStartsWith("PBDoc - Página de Login"));

		SigadocLoginPage loginPage = new SigadocLoginPage();
		loginPage.digitarCredenciais(username, password).enviarAutenticacao();

		System.out.println("O título da página é: " + driver.getTitle());
		wait.until(pageTitleStartsWith("PBDoc - Mesa Virtual"));
	}

}
