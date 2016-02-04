package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.gov.jfrj.siga.integration.test.util.IntegrationTestUtil;

public class EditaDocumentoPage {
	protected WebDriver driver;

	@FindBy(id = "frm_idTpDoc")
	protected WebElement origem;

	@FindBy(id = "frm_dtDocString")
	protected WebElement dataDocumento;

	@FindBy(id = "frm_nivelAcesso")
	protected WebElement acesso;

	@FindBy(id="eletronicoCheck1")
	protected WebElement digital;

	@FindBy(id="eletronicoCheck2")
	protected WebElement fisico;

	@FindBy(id = "frm_dtDocOriginalString")
	protected WebElement dataOriginalDocumento;

	@FindBy(id = "frm_numExtDoc")
	protected WebElement numeroOriginal;

	@FindBy(id = "frm_cpOrgaoSel_sigla")
	protected WebElement orgao;

	@FindBy(id = "frm_obsOrgao")
	protected WebElement observacaoOrgaoExterno;

	@FindBy(id = "frm_numAntigoDoc")
	protected WebElement numeroAntigo;

	@FindBy(id = "frm_nmSubscritorExt")
	protected WebElement subscritor;

	@FindBy(id = "frm_nmFuncaoSubscritor")
	protected WebElement funcaoLotacaoLocalidade;

	@FindBy(id = "frm_tipoDestinatario")
	protected WebElement destinatario;

	@FindBy(id = "destinatarioSelSpan")
	protected WebElement destinatarioSelSpan;

	@FindBy(id = "frm_classificacaoSel_sigla")
	protected WebElement classificacao;

	@FindBy(id = "descrDocumento")
	protected WebElement descricao;

	@FindBy(id="frm_idFormaDoc")
	protected WebElement tipo;

	@FindBy(id="frm_idMod")
	protected WebElement modelo;

	@FindBy(name = "gravar")
	protected WebElement botaoOk;

	protected IntegrationTestUtil util;

	/* Interno Importado */

	@FindBy(id="frm_subscritorSel_sigla")
	protected WebElement siglaSubscritor;

	public EditaDocumentoPage(WebDriver driver) {
		this.driver = driver;
		util = new IntegrationTestUtil();
	}

	public void preencheTipoDestinatario(String tipoDestinatario, String siglaDestinatario) {
		util.getSelect(driver,destinatario).selectByVisibleText(tipoDestinatario);
		if(tipoDestinatario.equals("Matrícula")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_destinatarioSel_sigla")), siglaDestinatario);
			descricao.click();
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("destinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Órgão Integrado")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_lotacaoDestinatarioSel_sigla")), siglaDestinatario);
			descricao.click();
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("lotacaoDestinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Órgão Externo")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_orgaoExternoDestinatarioSel_sigla")), siglaDestinatario);
			descricao.click();
			new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id("orgaoExternoDestinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Campo Livre")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_nmDestinatario")), siglaDestinatario);
		}
	}

	public void preencheDocumento(Boolean isDigital, Boolean hasClassificacao, Properties propDocumentos) {
		preencheAcesso();
		if(isDigital) {
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(digital));
			digital.click();
		} else {
			new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(fisico));
			fisico.click();
		}
		util.preencheElemento(driver,dataDocumento, new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		preencheTipoDestinatario(propDocumentos.getProperty("tipoDestinatarioCriacao"), propDocumentos.getProperty("siglaDestinatarioCriacao"));
		util.preencheElemento(driver,funcaoLotacaoLocalidade, propDocumentos.getProperty("funcaoLocalidade"));
		if(hasClassificacao) {
			util.preencheElemento(driver,classificacao, propDocumentos.getProperty("classificacao"));
			dataDocumento.click();
			new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("classificacaoSelSpan")));
		}
		util.preencheElemento(driver,descricao,  propDocumentos.getProperty("descricao"));
	}

	public void preencheDocumentoInterno(Properties propDocumentos, Boolean isDigital, Boolean hasClassificacao) {
		preencheDocumento(isDigital, hasClassificacao, propDocumentos);
		util.preencheElemento(driver,siglaSubscritor, propDocumentos.getProperty("siglaSubscritor"));
		descricao.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("subscritorSelSpan")));
	}

	public void selectTipoDocumento(String tipoDocumento, String modeloDocumento, By checkMudancaTipo) {
		util.getSelect(driver,tipo).selectByVisibleText(tipoDocumento);
		util.getWebElement(driver, checkMudancaTipo);
		if(util.checkCampo(driver, modelo) != null) {
			util.getSelect(driver,modelo).selectByVisibleText(modeloDocumento);
			util.getWebElement(driver, By.xpath("//td[text() = 'Dados básicos:']"));
		}
	}

	public void selectTipoDocumento(String tipoDocumento, By checkMudancaTipo) {
		util.getSelect(driver,tipo).selectByVisibleText(tipoDocumento);
		util.getWebElement(driver, checkMudancaTipo);
	}

	public void preencheDocumentoInternoSemModelo(Properties propDocumentos, String tipoDocumento, Boolean isDigital) {
		util.getSelect(driver,tipo).selectByVisibleText(tipoDocumento);
		util.getWebElement(driver, By.xpath("//td[text() = 'Dados básicos:']"));
		util.preencheElemento(driver,siglaSubscritor, propDocumentos.getProperty("siglaSubscritor"));
		descricao.click();
		preencheDocumento(isDigital, Boolean.TRUE, propDocumentos);
		util.getWebElement(driver, By.id("subscritorSelSpan"));
	}

	public void preencheDocumentoExterno(Properties propDocumentos) {
		preencheOrigem(propDocumentos.getProperty("externo"));
		preencheDocumento(Boolean.TRUE, Boolean.TRUE, propDocumentos);
		util.preencheElemento(driver,dataOriginalDocumento, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver,numeroOriginal, propDocumentos.getProperty("numeroOriginal"));
		util.preencheElemento(driver,orgao, propDocumentos.getProperty("orgao"));
		descricao.click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.id("cpOrgaoSelSpan")));
		util.preencheElemento(driver,observacaoOrgaoExterno, propDocumentos.getProperty("observacaoOrgaoExterno"));
		util.preencheElemento(driver,subscritor, propDocumentos.getProperty("subscritorExterno"));
		botaoOk.click();
	}

	public void preencheDocumentoInternoImportado(Properties propDocumentos) {
		preencheOrigem(propDocumentos.getProperty("internoImportado"));
		util.isElementVisible(driver, botaoOk);
		selectTipoDocumento("Memorando", "Memorando", By.xpath("//td[text() = 'Dados básicos:']"));
		preencheDocumentoInterno(propDocumentos, Boolean.TRUE, Boolean.TRUE);
		util.preencheElemento(driver, numeroOriginal, propDocumentos.getProperty("numeroOriginal"));
		botaoOk.click();
	}

	public void preencheAcesso() {
		util.getSelect(driver, acesso).selectByVisibleText("Público");
	}

	public void preencheOrigem(String origemDocumento) {
		util.getSelect(driver,origem).selectByVisibleText(origemDocumento);
		if(origemDocumento.equals("Externo")) {
			util.isElementVisible(driver, dataOriginalDocumento);
		} else if(origemDocumento.equals("Interno Importado"))  {
			util.isElementVisible(driver, numeroAntigo);
		} else {
			util.isElementInvisible(driver, By.id("frm_numAntigoDoc"));
		}
	}
}
