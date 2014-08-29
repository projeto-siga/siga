package br.gov.jfrj.siga.page.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
	protected WebElement Digital;
	
	@FindBy(id="eletronicoCheck2")
	protected WebElement Fisico;

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
			new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id("destinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Órgão Integrado")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_lotacaoDestinatarioSel_sigla")), siglaDestinatario);			
			descricao.click();
			new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id("lotacaoDestinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Órgão Externo")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_orgaoExternoDestinatarioSel_sigla")), siglaDestinatario);			
			descricao.click();
			new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.id("orgaoExternoDestinatarioSelSpan")));
		} else if(tipoDestinatario.equals("Campo Livre")) {
			util.preencheElemento(driver, driver.findElement(By.id("frm_nmDestinatario")), siglaDestinatario);			
		}
	}
		
	public void preencheDocumento(String origemDocumento, Boolean isDigital, Properties propDocumentos) {		
		if(isDigital) {
			Digital.click();
		} else {
			Fisico.click();
		}
		util.getSelect(driver, origem).selectByVisibleText(origemDocumento);
		util.preencheElemento(driver,dataDocumento, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		preencheTipoDestinatario(propDocumentos.getProperty("tipoDestinatarioCriacao"), propDocumentos.getProperty("siglaDestinatarioCriacao"));
		util.preencheElemento(driver,funcaoLotacaoLocalidade, propDocumentos.getProperty("funcaoLocalidade"));
		util.preencheElemento(driver,classificacao, propDocumentos.getProperty("classificacao"));
		descricao.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("classificacaoSelSpan"))).click();
		util.preencheElemento(driver,descricao,  propDocumentos.getProperty("descricao"));	
	}

	public void preencheDocumentoInterno(Properties propDocumentos, String tipoDocumento, String modeloDocumento, String origemDocumento, Boolean isDigital) {
		util.getSelect(driver,tipo).selectByVisibleText(tipoDocumento);
		descricao.click();
		util.getSelect(driver,modelo).selectByVisibleText(modeloDocumento);
		descricao.click();	
		preencheDocumento(origemDocumento, isDigital, propDocumentos);
		util.preencheElemento(driver,siglaSubscritor, propDocumentos.getProperty("siglaSubscritor"));
		descricao.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("subscritorSelSpan")));
	}	
	
	public void preencheDocumentoInternoSemModelo(Properties propDocumentos, String tipoDocumento, String origemDocumento, Boolean isDigital) {
		util.getSelect(driver,tipo).selectByVisibleText(tipoDocumento);
		descricao.click();
		preencheDocumento(origemDocumento, isDigital, propDocumentos);
		util.preencheElemento(driver,siglaSubscritor, propDocumentos.getProperty("siglaSubscritor"));
		descricao.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("subscritorSelSpan")));
	}	
	
	public void preencheDocumentoExterno(Properties propDocumentos) {
		preencheDocumento(propDocumentos.getProperty("externo"), Boolean.TRUE, propDocumentos);
		util.preencheElemento(driver,dataOriginalDocumento, new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));
		util.preencheElemento(driver,numeroOriginal, propDocumentos.getProperty("numeroOriginal"));
		util.preencheElemento(driver,orgao, propDocumentos.getProperty("orgao"));
		descricao.click();
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.id("cpOrgaoSelSpan")));
		util.preencheElemento(driver,observacaoOrgaoExterno, propDocumentos.getProperty("observacaoOrgaoExterno"));
		util.preencheElemento(driver,subscritor, propDocumentos.getProperty("subscritorExterno"));
		botaoOk.click();
	}
		
	public void preencheDocumentoInternoImportado(Properties propDocumentos) {			
		preencheDocumentoInterno(propDocumentos, "Memorando", "Memorando", propDocumentos.getProperty("internoImportado"), Boolean.TRUE);
		util.preencheElemento(driver, numeroOriginal, propDocumentos.getProperty("numeroOriginal"));
		botaoOk.click();
	}
}