package br.gov.jfrj.siga.page.objects;

import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class TarefaPage {
	private WebDriver driver;
	
	private IntegrationTestUtil util;
	
	@FindBy(name = "comentario")
	private WebElement comentario;
	
	@FindBy(name = "butc")
	private WebElement botaoAdicionar;
	
	@FindBy(id = "atorSel_sigla")
	private WebElement pessoa;
	
	@FindBy(id = "lotaAtorSel_sigla")
	private WebElement lotacao;
	
	@FindBy(id = "prioridade")
	private WebElement prioridade;
	
	@FindBy(name = "justificativa")
	private WebElement justificativa;
	
	@FindBy(id = "doc_document_expedienteSel_sigla")
	private WebElement documentoExpediente;
	
	@FindBy(name = "designar")
	private WebElement botaoDesignar;
	
	@FindBy(xpath = "//input[@value='Pegar tarefa para mim']")
	private WebElement botaoPegarTarefa;
	
	@FindBy(id = "atorSelSpan")
	private WebElement nomePessoa;
	
	@FindBy(xpath = "//input[contains(@value, 'Prosseguir')]")
	private WebElement botaoProsseguir;
	
	@FindBy(xpath = "//input[@value = 'to task-node1']")
	private WebElement botaoToTaskNode1;
	
	public TarefaPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}
	
	public void adicionarComentario(Properties propDocumentos) {
		util.preencheElemento(driver, comentario, propDocumentos.getProperty("comentario"));
		botaoAdicionar.click();
	}		
	
	public void designarTarefaLotacao(Properties propDocumentos) {
		util.preencheElemento(driver, pessoa, "");
		util.getSelect(driver, prioridade).selectByVisibleText(propDocumentos.getProperty("prioridade"));
		botaoDesignar.click();
	}
	
	public void pegarTarefaParaMim() {
		botaoPegarTarefa.click();
	}
	
	public void prosseguirTarefa() {
		botaoProsseguir.click();
	}
	
	public void prosseguirPagamento(String codigoDocumento) {
		util.preencheElemento(driver, documentoExpediente, codigoDocumento);
		documentoExpediente.sendKeys(Keys.TAB);
		botaoProsseguir.click();
	}
}