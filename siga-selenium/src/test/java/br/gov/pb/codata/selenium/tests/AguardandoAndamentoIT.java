package br.gov.pb.codata.selenium.tests;



import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.pb.codata.selenium.DriverBase;
import br.gov.pb.codata.selenium.page_objects.SigadocLoginPage;
import br.gov.pb.codata.selenium.page_objects.SigadocStartUp;

/**
*
* @author Thomas Ribeiro
*/
public class AguardandoAndamentoIT extends DriverBase {

	/**
	 * Caminho: Menu Aguardando andamento na mesa virtual 
	 * Objetivo: Testar alguns cenários quando um documento esta em "aguardando andamento"
	 * Cenários: reclassificarDocumeto/acompanhamentoDocumento/definirMarcador/cancelar/desfazerMarcação
	 * @author Allysson Cruz
	 */
	//refazer os casos
	//@Test
	public void reclassificarDocumeto() throws Exception {

		WebDriver driver = SigadocStartUp.startUp();

		driver.get(System.getenv("PBDOC_URL") + "sigaex/app/expediente/mov/reclassificar?sigla=CDT-MEM-2020/00057&");

		driver.findElement(By.name("dtMovString")).click();
		driver.findElement(By.name("dtMovString")).sendKeys("09072020");
		driver.findElement(By.id("subscritorSelButton")).click();
		{
			WebElement element = driver.findElement(By.id("subscritorSelButton"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("CDT10182")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("classificacaoSelSpan")).click();
		driver.findElement(By.id("classificacaoSelButton")).click();
		{
			WebElement element = driver.findElement(By.id("classificacaoSelButton"));
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
		driver.findElement(By.name("descrMov")).click();
		driver.findElement(By.name("descrMov")).sendKeys("tesses");
		{
			WebElement element = driver.findElement(By.cssSelector(".btn-primary:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).clickAndHold().perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".btn-primary:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.cssSelector(".btn-primary:nth-child(1)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).release().perform();
		}
		driver.findElement(By.xpath("//button[@type='submit']")).click();

	}

	//@Test
	public void acompanhamentoDocumento() throws Exception {

		WebDriver driver = getDriver();

		driver.manage().window().setSize(new Dimension(1024, 768));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		System.out.println("O título da página é: " + driver.getTitle());

		loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 10, 100);

		System.out.println("O título da página é: " + driver.getTitle());

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");

		wait = new WebDriverWait(driver, 25, 100);

		//Insere texto no campo Data
		driver.findElement(By.name("dtMovString")).sendKeys("28/01/2020");

		//responsavel
		Select comboResponsavel = new Select(driver.findElement(By.id("tipoResponsavel")));
		comboResponsavel.selectByValue("1");

		//formulario responsavel
		driver.findElement(By.cssSelector("#tipoResponsavel > option:nth-child(2)")).click();
		driver.findElement(By.id("lotaResponsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("LTEST")).click();
		driver.switchTo().defaultContent();

		//perfil
		Select comboPerfil = new Select(driver.findElement(By.name("idPapel")));
		comboPerfil.selectByValue("1");

		//verificar gravar
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}

	//@Test
	public void definirMarcador() throws Exception {

		WebDriver driver = getDriver();

		driver.manage().window().setSize(new Dimension(1024, 768));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		System.out.println("O título da página é: " + driver.getTitle());

		loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 10, 100);

		System.out.println("O título da página é: " + driver.getTitle());

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");

		wait = new WebDriverWait(driver, 25, 100);

		//Insere texto no campo Data
		driver.findElement(By.name("dtMovString")).sendKeys("28/01/2020");

		//responsavel
		Select comboResponsavel = new Select(driver.findElement(By.id("tipoResponsavel")));
		comboResponsavel.selectByValue("1");

		//formulario responsavel
		driver.findElement(By.cssSelector("#tipoResponsavel > option:nth-child(2)")).click();
		driver.findElement(By.id("lotaResponsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("LTEST")).click();
		driver.switchTo().defaultContent();

		//perfil
		Select comboPerfil = new Select(driver.findElement(By.name("idPapel")));
		comboPerfil.selectByValue("1");

		//verificar gravar
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}

	//@Test
	public void cancelar() throws Exception {

		WebDriver driver = getDriver();

		driver.manage().window().setSize(new Dimension(1024, 768));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		System.out.println("O título da página é: " + driver.getTitle());

		loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 10, 100);

		System.out.println("O título da página é: " + driver.getTitle());

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");

		wait = new WebDriverWait(driver, 25, 100);

		//Insere texto no campo Data
		driver.findElement(By.name("dtMovString")).sendKeys("28/01/2020");

		//responsavel
		Select comboResponsavel = new Select(driver.findElement(By.id("tipoResponsavel")));
		comboResponsavel.selectByValue("1");

		//formulario responsavel
		driver.findElement(By.cssSelector("#tipoResponsavel > option:nth-child(2)")).click();
		driver.findElement(By.id("lotaResponsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("LTEST")).click();
		driver.switchTo().defaultContent();

		//perfil
		Select comboPerfil = new Select(driver.findElement(By.name("idPapel")));
		comboPerfil.selectByValue("1");

		//verificar gravar
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}

	//@Test
	public void desfazerMarcação() throws Exception {

		WebDriver driver = getDriver();

		driver.manage().window().setSize(new Dimension(1024, 768));

		driver.get(System.getenv("PBDOC_URL"));

		SigadocLoginPage loginPage = new SigadocLoginPage();

		System.out.println("O título da página é: " + driver.getTitle());

		loginPage.digitarCredenciais("CDT10182", "Charlote123").enviarAutenticacao();

		WebDriverWait wait = new WebDriverWait(driver, 10, 100);

		System.out.println("O título da página é: " + driver.getTitle());

		driver.get(System.getenv("PBDOC_URL") + "siga/app/pessoa/editar");

		wait = new WebDriverWait(driver, 25, 100);

		//Insere texto no campo Data
		driver.findElement(By.name("dtMovString")).sendKeys("28/01/2020");

		//responsavel
		Select comboResponsavel = new Select(driver.findElement(By.id("tipoResponsavel")));
		comboResponsavel.selectByValue("1");

		//formulario responsavel
		driver.findElement(By.cssSelector("#tipoResponsavel > option:nth-child(2)")).click();
		driver.findElement(By.id("lotaResponsavelSelButton")).click();
		driver.switchTo().frame(0);
		driver.findElement(By.linkText("LTEST")).click();
		driver.switchTo().defaultContent();

		//perfil
		Select comboPerfil = new Select(driver.findElement(By.name("idPapel")));
		comboPerfil.selectByValue("1");

		//verificar gravar
		driver.findElement(By.xpath("//input[@value='Ok']")).click();
	}

}
