package br.gov.jfrj.siga.integration.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import br.gov.jfrj.siga.page.objects.EstatisticaProcedimentoPage;
import br.gov.jfrj.siga.page.objects.OperacoesDocumentoPage;
import br.gov.jfrj.siga.page.objects.PesquisaDocumentoPage;
import br.gov.jfrj.siga.page.objects.PrincipalPage;
import br.gov.jfrj.siga.page.objects.SolicitacaoEletronicaContratacaoPage;
import br.gov.jfrj.siga.page.objects.TarefaPage;

public class WorkflowDigitalIT extends IntegrationTestBase {
	private String codigoDocumento;
	private TarefaPage tarefaPage;
	private String inicioTarefa;
	private String descricaoTarefa;
	private String url;

	public WorkflowDigitalIT() throws FileNotFoundException, IOException {
		super();
	}

	@BeforeClass(enabled = true)
	public void login() {
		efetuaLogin();
		tarefaPage = PageFactory.initElements(driver, TarefaPage.class);
		operacoesDocumentoPage = PageFactory.initElements(driver, OperacoesDocumentoPage.class);
	}

	@BeforeClass(enabled = true)
	public void solicitacaoEletronicaContratacao() {
		try {
			// No SIGA-DOC, criar documento:
			PrincipalPage principalPage = PageFactory.initElements(driver, PrincipalPage.class);
			principalPage.clicarBotaoNovoDocumentoEx();

			// Solicitação Eletrônica de Contratação - Digital - Selecionar Tipo "Solicitação Eletrônica de Contratação"
			// Preencher campos que ainda estiverem vazios - Clicar OK
			SolicitacaoEletronicaContratacaoPage secPage = PageFactory.initElements(driver, SolicitacaoEletronicaContratacaoPage.class);
			secPage.criaSolicitacaoEletronicaContratacao(propDocumentos);

			// Clicar em Finalizar
			operacoesDocumentoPage.clicarLinkFinalizar();

			// Assinar Digitalmente (simular assinatura)
			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
			assinarDigitalmente(codigoDocumento, "Nº " + codigoDocumento);

			codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");

			// Garantir que "Tarefa: Anexar Arquivos" apareça na tela
			WebElement tarefaElement = util.getWebElement(driver, By.xpath("//td[contains(., 'Tarefa: Anexar Arquivos')]"));
			if(tarefaElement == null) {
				throw new SkipException("Tarefa não encontrada!");
			}

			// Garantir que o link "Prosseguir" apareça na tela
			WebElement linkProsseguir = util.getWebElement(driver, By.xpath("//a[contains(text(), 'Prosseguir')]"));
			if(linkProsseguir == null) {
				throw new SkipException("Link Prosseguir não encontrado!");
			}

			// Armazenando nome da tarefa
			String tarefa = tarefaElement.getText();
			descricaoTarefa = tarefa.substring(tarefa.indexOf("Tarefa: "), tarefa.indexOf(" sendo")).substring(8);

			// Armazenando início da tarefa
			String procedimento = util.getWebElement(driver, By.xpath("//td[contains(.,'Procedimento:')]")).getText();
			inicioTarefa = procedimento.substring(procedimento.indexOf("(") + 1, procedimento.indexOf(")"));
		} catch(Exception e) {
			e.printStackTrace();
			throw new SkipException("Exceção no método setUp!");
		}
	}

	@BeforeMethod(enabled = true)
	public void paginaInicial(Method method) {

			System.out.println("BeforeMethod: " + method.getName() + " - Titulo página: " + driver.getTitle());

			if(!method.getName().equals("pagamento") && !method.getName().equals("relatorioContratacao")) {
				if(!driver.getCurrentUrl().contains("sigawf")) {
					// Ir para a página inicial através do menu SIGA > página Inicial
					util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
					util.getWebElement(driver, By.linkText("página Inicial")).sendKeys(Keys.ENTER);
					util.getWebElement(driver, By.cssSelector("a.gt-btn-small.gt-btn-right"));

					WebElement linkTarefa = util.getWebElement(driver, By.xpath("//div[h2 = 'Tarefas']//tbody/tr[td[1]/a[text() = '" + descricaoTarefa + "'] "
							+ "and td[last()][contains(., '" + inicioTarefa +"')]]/td/a"));
					linkTarefa.click();
				}
			} else {
				if(method.getName().equals("pagamento")) {
					try {
						geraProcesso();
					} catch(Exception e) {
						e.printStackTrace();
						throw new SkipException("Exceção no método geraProcesso!");
					}
				}

				url = driver.getCurrentUrl();
				System.out.println("Url: " + url);
				// Ir para a módulo workflow através do menu SIGA > Módulos > Workflow
				util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.linkText("Módulos")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.linkText("Workflow")).sendKeys(Keys.ENTER);
				util.getWebElement(driver, By.xpath("//h3[ text() = 'Quadro de tarefas']"));
			}
	}

	@Test(enabled = true, priority = 1)
	public void comentario() {
		tarefaPage.adicionarComentario(propDocumentos);
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[text() = '" + propDocumentos.getProperty("comentario") +"']")),
				"Texto '" + propDocumentos.getProperty("comentario") + "' não encontrado!");
	}

	@Test(enabled = true, priority = 1)
	public void designacaoTarefa() {
		// Deletar a pessoa e deixar somente a lotação - Alterar a prioridade para Alta - Clicar em Designar
		tarefaPage.designarTarefaLotacao(propDocumentos);

		// Garantir que o atendente da tarefa seja a lotação designada
		WebElement tarefa = util.getWebElement(driver, By.xpath("//div[h2 = 'Tarefas']//tbody/tr[td[1]/a[text() = 'Anexar Arquivos'] and td[last()][contains(., '" + inicioTarefa +"')]]"));
		List<WebElement> colunas = tarefa.findElements(By.tagName("td"));

		Assert.assertTrue(colunas.get(1).getText().equals(propDocumentos.getProperty("lotacao")));

		// E a prioridade seja alta no quadro de tarefas
		Assert.assertTrue(colunas.get(2).getText().equals("Alta"));

		// Clicar no link "Anexar Arquivos"
		util.getWebElement(driver, colunas.get(0), By.linkText("Anexar Arquivos")).click();

		// Clicar em Pegar tarefa para mim
		tarefaPage.pegarTarefaParaMim();

		// Garantir que o nome da pessoa apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.id("atorSelSpan")), "Nome da pessoa não encontrado!");
		// Garantir que o botão "Pegar tarefa para mim" não apareça na tela
		Assert.assertTrue(util.isElementInvisible(driver, By.xpath("//input[@value='Pegar tarefa para mim']")), "Botão 'Pegar tarefa para mim' ainda está sendo exibido!");
	}

	@Test(enabled = true, priority = 2)
	public void execucaoTarefaSigaDoc() {
		util.getClickableElement(driver, By.partialLinkText(codigoDocumento)).click();

		// Clicar em Prosseguir
		util.getWebElement(driver, By.partialLinkText("Prosseguir »")).click();

		// Garantir que "Tarefa: Verificar programação anual" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[contains(.,'Tarefa: Verificar programação anual')]")),
				"'Tarefa: Verificar programação anual' não encontrada!");

		// Garantir que os botões "Sim" e "não" apareçam na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Sim »')]")), "Link 'Sim' não encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'não »')]")), "Link 'não' não encontrado!");

		// Clicar no link do documento
		WebElement procedimento = util.getWebElement(driver, By.xpath("//td[contains(.,'Procedimento: Contratação: fase de análise')]"));
		inicioTarefa = procedimento.getText().substring(procedimento.getText().indexOf("(") + 1, procedimento.getText().indexOf(")"));
		descricaoTarefa = "Verificar programação anual";
	}

	@Test(enabled = true, priority = 3)
	public void execucaoTarefaWorkflow() {
		// Clicar em Sim
		util.getClickableElement(driver, By.xpath("//input[contains(@value, 'Sim')]")).click();

		// Garantir que "Tarefa: Realizar cotação" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'Tarefa: Realizar cotação')]")),
				"'Tarefa: Realizar cotação' não encontrada!");

		// Garantir que o link "Retificar SEC" e "Prosseguir" apareçam na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Retificar SEC')]")), "Botão 'Retificar SEC' não encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Prosseguir')]")), "Botão 'Prosseguir' não encontrado!");

		// Armazenando dados do início da tarefa e descrição da tarefa
		String inicio = util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'início:')]")).getText();
		inicioTarefa = inicio.substring(inicio.indexOf(":") + 1, inicio.length()).trim();
		descricaoTarefa = "Realizar cotação";
	}

	public void geraProcesso() {
		// Ir no menu Documento > Pesquisar
		util.getWebElement(driver, By.linkText("SIGA")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Módulos")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Documentos")).sendKeys(Keys.ENTER);

		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);

		// Situação: aguardando andamento - Tipo: Portaria - Clicar em "buscar"
		PesquisaDocumentoPage pesquisaDocumentoPage = PageFactory.initElements(driver, PesquisaDocumentoPage.class);
		pesquisaDocumentoPage.buscaPortaria();

		// Clicar em "autuar"
		// Preencher subscritor - Alterar Modelo para: "Contrato com Exclusividade" - Clicar em Ok
		super.autuar(Boolean.TRUE, "Contrato com Exclusividade");

		// Finalizar e assinar Processo criado
		operacoesDocumentoPage.clicarLinkFinalizar();
		// Assinar Digitalmente (simular assinatura)
		codigoDocumento = operacoesDocumentoPage.getTextoVisualizacaoDocumento("/html/body/div[4]/div/h2");
		assinarDigitalmente(codigoDocumento, propDocumentos.getProperty("descricao"));
	}

	@Test(enabled = true, priority = 4)
	public void pagamento() {
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);

		// Ir no menu Procedimentos > Iniciar > Pagamento
		util.getWebElement(driver, By.linkText("Procedimentos")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Iniciar")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Pagamento")).sendKeys(Keys.ENTER);

		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);

		// Colar a sigla copiada no campo de Execução da Tarefa
		// Clicar em prosseguir
		tarefaPage.prosseguirPagamento(codigoDocumento);

		// Garantir que "Tarefa: É registro de preços - pagamento" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//div[h3 = 'Dados da Tarefa']//p[contains(.,'Tarefa: É registro de preços - pagamento')]")),
				"'Tarefa:  É registro de preços - pagamento' não encontrada!");

		// Garantir que os botões "Sim" e "não" apareçam na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Sim') and @type = 'submit']")), "Botão 'Sim' não encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//input[contains(@value, 'Nao') and @type = 'submit']")), "Botão 'não' não encontrado!");

		// Clicar no link da sigla do Processo
		util.getClickableElement(driver, By.partialLinkText(codigoDocumento)).click();

		// Garantir que "Tarefa: É registro de preços - pagamento" apareça na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//td[contains(.,'Tarefa: É registro de preços - pagamento')]")),
				"'Tarefa: É registro de preços - pagamento' não encontrada!");

		// Garantir que os botões "Sim" e "não" apareçam na tela
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Sim »')]")), "Link 'Sim' não encontrado!");
		Assert.assertNotNull(util.getWebElement(driver, By.xpath("//a[contains(text(), 'Nao »')]")), "Link 'não' não encontrado!");
	}

	@Test(enabled = true, priority = 5)
	public void relatorioContratacao() {
		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);

		// Ir no menu Relatórios > Apresentar métricas > Contratação: fase de análise
		util.getWebElement(driver, By.linkText("Relatórios")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Apresentar métricas")).sendKeys(Keys.ENTER);
		util.getWebElement(driver, By.linkText("Contratação: fase de análise")).sendKeys(Keys.ENTER);

		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);

		// Alterar o relatório para "Tempo de documentos" - Inserir datas nos campos "Procedimento iniciado" e "Procedimento finalizado"
		// Clicar em Gerar relatório
		EstatisticaProcedimentoPage ePage = PageFactory.initElements(driver, EstatisticaProcedimentoPage.class);
		ePage.gerarRelatorioContratacao(propDocumentos.getProperty("dataInicialRelatorio"), propDocumentos.getProperty("dataFinalRelatorio"));

		util.trocaURL(url);
		url = driver.getCurrentUrl();
		System.out.println("Url: " + url);
		// Garantir que não retorne um erro 500
		util.isPDF(driver);
	}
}
