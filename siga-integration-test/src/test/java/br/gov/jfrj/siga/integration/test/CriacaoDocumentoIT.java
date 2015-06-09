package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.EditaDocumentoPage;
import br.gov.jfrj.siga.page.objects.MemorandoPage;
import br.gov.jfrj.siga.page.objects.OficioPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PortariaPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;

public class CriacaoDocumentoIT extends IntegrationTestBase{
	private PrincipalPage principalPage;
	private EditaDocumentoPage editaDocumentoPage;

	public CriacaoDocumentoIT() throws FileNotFoundException, IOException {
		super();
	}

	@BeforeClass
	public void setUp() {
		try {
			efetuaLogin();
			principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			editaDocumentoPage = PageFactory.initElements(driver, EditaDocumentoPage.class);
			operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SkipException("Exceção no método setUp!");
		}
	}

	@BeforeMethod
	public void paginaInicial(Method method) {
		try {
			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle());

			if(driver.getCurrentUrl().contains("exibir.action") && util.getClickableElement(driver, By.linkText("Editar")) != null) {
				operacoesDocumentoPage.clicarLinkEditar();
			} else {
				if(!driver.getTitle().equals("SIGA - Página Inicial")) {
					driver.get(baseURL + "/siga");
				}
				principalPage.clicarBotaoNovoDocumentoEx();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test(enabled = true)
	public void criaDocumentoExterno() {
		editaDocumentoPage.preencheDocumentoExterno(propDocumentos);
		WebElement divVisualizacaoDocumento = util.getWebElement(driver, By.cssSelector("div.gt-content-box"));
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'Expediente Externo Nº')]")), "Texto Expediente Externo Nº TMP não foi encontrado!");
	}

	@Test(enabled = true)
	public void criaDocumentoInternoImportado() {
		editaDocumentoPage.preencheDocumentoInternoImportado(propDocumentos);
		WebElement divVisualizacaoDocumento = util.getWebElement(driver, By.cssSelector("div.gt-content-box"));
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//b[contains(., 'Expediente Interno Nº TMP')]")), "Texto Expediente Interno Nº TMP não foi encontrado!");
	}

	@Test(enabled = true)
	public void criaMemorando() {
		MemorandoPage memorandoPage = PageFactory.initElements(driver, MemorandoPage.class);
		memorandoPage.criaMemorando(propDocumentos);
		WebElement divVisualizacaoDocumento = util.getWebElement(driver, By.cssSelector("div.gt-content-box"));
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'MEMORANDO Nº TMP')]")), "Texto MEMORANDO Nº TMP não foi encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'Atenciosamente')]")), "Fecho não encontrado");
	}

	@Test(enabled = true)
	public void criaPortaria() {
		PortariaPage portariaPage = PageFactory.initElements(driver, PortariaPage.class);
		portariaPage.criaPortaria(propDocumentos);
		WebElement divVisualizacaoDocumento = util.getWebElement(driver, By.cssSelector("div.gt-content-box"));
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'PORTARIA Nº TMP')]")), "Texto PORTARIA Nº TMP não foi encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'Testes de Integração')]")), "Informação sobre o que Dispõe o documento não encontrada!");
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//span[contains(., 'Exmo. Sr. Juiz Federal')]")), "Texto do memorando não encontrado!");
	}

	@Test(enabled = true)
	public void criaOficio() {
		OficioPage oficioPage = PageFactory.initElements(driver, OficioPage.class);
		oficioPage.criaOficio(propDocumentos);
		WebElement divVisualizacaoDocumento = util.getWebElement(driver, By.cssSelector("div.gt-content-box"));
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'OFÍCIO Nº TMP')]")), "Texto OFÍCIO Nº TMP não foi encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., '" + propDocumentos.getProperty("enderecoDestinatario") + "')]")),
				"Endereço não encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, divVisualizacaoDocumento, By.xpath("//p[contains(., 'Senhor Juiz')]")), "Forma de Tratamento não encontrada!");
	}
}
