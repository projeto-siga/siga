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

import br.gov.jfrj.siga.page.objects.AssinaturaDigitalPage;
import br.gov.jfrj.siga.page.objects.JuntadaDocumentoPage;
import br.gov.jfrj.siga.page.objects.OficioPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.ProcessoFinanceiroPage;
import br.gov.jfrj.siga.page.objects.TransferenciaPage;
import br.gov.jfrj.siga.page.objects.VisualizacaoDossiePage;

public class ProcessoAdministrativoFisicoIT extends IntegrationTestBase {
	private String codigoDocumento;
	private String codigoProcesso;

	public ProcessoAdministrativoFisicoIT() throws FileNotFoundException, IOException {
		super();
	}

	@BeforeClass
	public void setUp() {
		try{
			efetuaLogin();
			operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
			PrincipalPage principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			principalPage.clicarBotaoNovoDocumentoEx();

			OficioPage oficioPage = PageFactory.initElements(driver, OficioPage.class);
			oficioPage.criaOficio(propDocumentos);
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");

			operacoesDocumentoPage.clicarLinkFinalizar();
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");

			operacoesDocumentoPage.clicarLinkAssinarDigitalmente();
			AssinaturaDigitalPage assinaturaDigitalPage = PageFactory.initElements(driver, AssinaturaDigitalPage.class);
			assinaturaDigitalPage.registrarAssinaturaDigital(baseURL, codigoDocumento);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SkipException("Exceção no método setUp!");
		}
	}

	@BeforeMethod
	public void paginaInicial(Method method) {
		try {
			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle());
			if(!driver.getCurrentUrl().contains("exibir.action") || driver.getTitle().contains("SIGA - Erro Geral")) {
				System.out.println("Efetuando busca!");

				if(codigoProcesso != null) {
					driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoProcesso);
				} else {
					driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);
				}
			}

			codigoProcesso = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div[1]/h2");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Test(enabled = true)
	public void autuar(){
		super.autuar(Boolean.FALSE, "Processo de Outros Assuntos Administrativos");
	}

	@Test(enabled = true, priority = 1)
	public void visualizarImpressao() {
		// Clicar em Visualizar Impressão - Garantir que não retorne um erro
		Assert.assertTrue(operacoesDocumentoPage.clicarLinkVisualizarImpressao());
	}

	@Test(enabled = true, priority = 1)
	public void finalizar() {
		super.finalizarProcesso();
	}

	@Test(enabled = true, priority = 2)
	public void registrarAssinaturaManual() {
		super.registrarAssinaturaManual();
	}

	@Test(enabled = true, priority = 3)
	public void juntar() {
		// Acessar o documento anterior
		driver.get(baseURL + "/sigaex/expediente/doc/exibir.action?sigla=" + codigoDocumento);
/*		PrincipalPage principalPage = PageFactory.initElements(driver, PrincipalPage.class);
		principalPage.buscarDocumento(codigoDocumento);*/

		// Clicar em "Juntar"
		operacoesDocumentoPage.clicarlinkJuntar();

		// Selecionar o processo - Clicar "OK"
		JuntadaDocumentoPage juntadaDocumentoPage = PageFactory.initElements(driver, JuntadaDocumentoPage.class);
		juntadaDocumentoPage.juntarDocumento(propDocumentos, codigoProcesso);

		// Clicar no link com o número do processo ao qual o documento foi juntado para retornar é visualização das movimentações do processo
		util.getWebElement(driver, By.xpath("//tr[contains(@class, 'juntada ')]"));
		util.getClickableElement(driver, By.partialLinkText(codigoProcesso)).click();

		// Clicar em "Visualizar Dossiê"
		operacoesDocumentoPage.clicarLinkVisualizarDossie();

		// Garantir que alguma parte do texto do documento juntado apareça na tela
		VisualizacaoDossiePage visualizacaoDossiePage = PageFactory.initElements(driver, VisualizacaoDossiePage.class);
		Assert.assertTrue(visualizacaoDossiePage.visualizaConteudo(By.xpath("//p[contains(text(), '"+ codigoDocumento +"')]")), "Conteúdo do documento juntado não encontrado!");
		visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}

	@Test(enabled = true, priority = 4)
	public void cancelarJuntada() {
		// Acessar o documento juntado, por meio do link existente no TR do evento de juntada
		WebElement linkDocumentoJuntado = util.getClickableElement(driver, By.partialLinkText(codigoDocumento));
		linkDocumentoJuntado.click();

		// Clicar em "Desentranhar"
		operacoesDocumentoPage.clicarLinkDesentranhar();
		Assert.assertTrue(util.isElementInvisible(driver, By.xpath("//tr[contains(@class, 'juntada ')]")), "Evento de juntada continua visível!");

		validaDesentranhamento(codigoProcesso);
	}

	@Test(enabled = true, priority = 2)
	public void anexarArquivo() {
		String nomeArquivo = propDocumentos.getProperty("arquivoAnexo");
		super.anexarArquivo(nomeArquivo);

		// Clicar em "Visualizar Dossiê"
		operacoesDocumentoPage.clicarLinkVisualizarDossie();

		// Garantir que o nome do anexo apareça na tela (é a seção OBJETO, da capa do processo)
		Assert.assertNotNull(util.getWebElement(driver, By.linkText(nomeArquivo.substring(0, nomeArquivo.indexOf(".")).toLowerCase())), "Nome do arquivo selecionado não encontrado na visualização do Dossiê!");

		// Clicar em "Visualizar Movimentações"
		VisualizacaoDossiePage visualizacaoDossiePage = PageFactory.initElements(driver, VisualizacaoDossiePage.class);
		visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}

	@Test(enabled = true, priority = 3)
	public void assinarAnexo() {
		super.assinarAnexo(codigoProcesso);
	}

	@Test(enabled = false, priority = 4)
	public void cancelarAnexo() {
		super.cancelarAnexo();

		// Clicar em "Visualizar Movimentações"
		VisualizacaoDossiePage visualizacaoDossiePage = PageFactory.initElements(driver, VisualizacaoDossiePage.class);
		visualizacaoDossiePage.clicarLinkVisualizarMovimentacoes();
	}

	@Test(enabled = true, priority = 4)
	public void encerrarVolume() {
		super.encerrarVolume();
	}

	@Test(enabled = true, priority = 5)
	public void criarVolume() {
		// Clicar em "Abrir Novo Volume"
		operacoesDocumentoPage.clicarLinkAbrirNovoVolume();

		// Garantir que os textos "1º Volume - Apensado" e "2º Volume - Aguardando Andamento" apareçam na tela
		WebElement volume1 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[1][contains(., 'Apensado')]"));
		WebElement volume2 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[2][contains(., 'Aguardando Andamento')]"));

		Assert.assertTrue(volume1 != null && volume2 != null, "Textos 'V01  -  Apensado' e 'V02  -  Aguardando Andamento' não encontrados!");

		// Clicar sobre a segunda ocorrência do link "Despachar/Transferir"
		operacoesDocumentoPage.clicarLinkDespacharTransferir();

		// Selecionar um atendente qualquer - Clicar "OK"
		TransferenciaPage transferenciaPage = PageFactory.initElements(driver, TransferenciaPage.class);
		transferenciaPage.transferirDocumento(propDocumentos);

		// Garantir que os textos "1º Volume - Apensado" e "2º Volume - Caixa de Entrada (Digital)"
		volume1 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[1][contains(., 'Apensado')]"));
		volume2 = util.getWebElement(driver, By.xpath("//div[h3 = 'Volumes']/ul/li[2][contains(., 'A Receber (Físico)')]"));

		Assert.assertTrue(volume1 != null && volume2 != null, "Textos 'V01  -  Apensado' e 'V02  -  A Receber (Físico)' não encontrados!");

		// Clicar em "Desfazer Transferência"
		operacoesDocumentoPage.clicarLinkDesfazerTransferencia();

		// Garantir que "2º Volume - Aguardando Andamento" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//h3[contains(text(), 'Volume - Aguardando Andamento')]|//div[h3 = 'Volumes']/ul/li[2][contains(., 'Aguardando Andamento')]")),
				"'Texto 2º Volume - Aguardando Andamento' não encontrado!");
	}

	@Test(enabled = true, priority = 6)
	public void criarSubprocesso() {
		// Clicar em "Criar Subprocesso"
		operacoesDocumentoPage.clicarLinkCriarSubprocesso();

		// Selecionar um subscritor qualquer - Clicar "OK"
		ProcessoFinanceiroPage processoFinanceiroPage = PageFactory.initElements(driver, ProcessoFinanceiroPage.class);
		processoFinanceiroPage.criaProcessoFinanceiro(propDocumentos, Boolean.FALSE, "Processo de Execução Orçamentária e Financeira");

		// Clicar em Finalizar
		operacoesDocumentoPage.clicarLinkFinalizar();

		// Garantir que o texto "<Número do processo principal>.01" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//h2[contains(text(), '" + codigoProcesso + ".01')]")));
	}
}
