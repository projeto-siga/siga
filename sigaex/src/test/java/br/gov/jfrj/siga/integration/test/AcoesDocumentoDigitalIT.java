package br.gov.jfrj.siga.integration.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.AnexoPage;
import br.gov.jfrj.siga.page.objects.AssinaturaDigitalPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PortariaPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;

public class AcoesDocumentoDigitalIT extends IntegrationTestBase {
	private PrincipalPage principalPage;
	private OperacoesDocumentoPage operacoesDocumentoPage;
	private Properties propDocumentos = new Properties();
	private String codigoDocumento;
	
	@Parameters({ "baseURL", "login", "password" })
	public AcoesDocumentoDigitalIT(String baseURL, String login, String password) {
		super(baseURL, login, password);
	}
	
	@BeforeClass	
	@Parameters("infoDocumentos")
	public void setUp(String infoDocumentos) {
		File file = new File(infoDocumentos);
		try {
			efetuaLogin();
			propDocumentos.load(new FileInputStream(file));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
			
			PortariaPage portariaPage = PageFactory.initElements(driver, PortariaPage.class);
			principalPage.clicarBotaoNovoDocumentoEx();
			portariaPage.criaPortaria(propDocumentos);
			
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
			//codigoDocumento = "TMP-4967";
								
			System.out.println("Código do documento: " + codigoDocumento);
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
		} 
	}
	
	@BeforeMethod
	public void paginaInicial() {
		try {
			System.out.println("BeforeMethod... Titulo página: " + driver.getTitle());
			if(!driver.getCurrentUrl().contains("exibir.action")) {
				System.out.println("Efetuando busca!");
				driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);
				//operacoesDocumentoPage.efetuaBuscaDocumento(codigoDocumento);					
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(enabled = true, priority = 1)
	public void finalizarDocumento() {
		operacoesDocumentoPage.clicarLinkFinalizar();
		Assert.assertTrue(operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h3[1]")
				.contains("Pendente de Assinatura, Como Subscritor"), "Texto Pendente de Assinatura, Como Subscritor não foi encontrado!");		 
	}
	
	@Test(enabled = true, priority = 2)
	public void assinarDigitalmente() {
		operacoesDocumentoPage.clicarLinkAssinarDigitalmente();
		AssinaturaDigitalPage assinaturaDigitalPage = PageFactory.initElements(driver, AssinaturaDigitalPage.class);
		assinaturaDigitalPage.registrarAssinaturaDigital(baseURL, codigoDocumento);
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[1][contains(text(), 'Aguardando Andamento')]")));		
		new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[2][contains(., 'Assinatura')]")));
	}
	
	@Test(enabled = true, priority = 1)
	public void anexarArquivo() {
		operacoesDocumentoPage.clicarLinkAnexarArquivo();
		AnexoPage anexoPage = PageFactory.initElements(driver, AnexoPage.class);
		anexoPage.anexarArquivo(propDocumentos);
		Assert.assertTrue(new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.linkText("teste.pdf"))) != null);
		anexoPage.clicarBotaovoltar();
	}
	
	@Test(enabled = true, priority = 3)
	public void despacharDocumento() {
		operacoesDocumentoPage.clicarLinkDespacharTransferir();
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		transferenciaPage.despacharDocumento(propDocumentos);
		new WebDriverWait(driver, 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[7][contains(., '" + propDocumentos.getProperty("despacho") + "')]")));
	}

	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}
	
}
