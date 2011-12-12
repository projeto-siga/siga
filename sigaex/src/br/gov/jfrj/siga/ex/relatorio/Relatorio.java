/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Relatorio.java 16/04/2007
 *
 * Siga Ex
 */

package br.gov.jfrj.siga.ex.relatorio;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.log4j.Logger;

import br.gov.jfrj.siga.persistencia.oracle.JDBCUtilOracle;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;

public class Relatorio {

	private Connection con = JDBCUtilOracle.getConnection();

	protected static Logger log = Logger.getLogger(Relatorio.class);

	private String caminhoDestino = "";

	private String arquivo = "";

	private HttpServletResponse response = null;

	OutputStream outputStream = null;

	ActionInvocation invocation = null;

	ActionContext act = null;

	// private final ServletActionContext response = null;

	// Representa o relatório gerado.
	private JasperPrint relGerado;

	public Relatorio() {

	}

	public Relatorio(Connection con, HashMap parameters, String localRelatorio)
			throws JRException {

		try {

			// O objeto JasperReport representa o objeto JasperDesign (arquivo
			// .jrxml) compilado.
			// Ou seja, o arquivo .jasper
			JasperReport jr = (JasperReport) JRLoader
					.loadObject(localRelatorio);

			// JasperPrint representa o relatório gerado.
			// É criado um JasperPrint a partir de um JasperReport, contendo o
			// relatório preenchido.
			this.relGerado = JasperFillManager.fillReport(jr, parameters, con);

		} catch (JRException e) {
			throw e;
		}
	}

	public Relatorio(Map parameters, URL localRelatorio) throws JRException,
			IOException {

		this.arquivo = (String) parameters.get("tipoRelatorio");

		try {
			// O objeto JasperReport representa o objeto JasperDesign
			// (arquivo.jrxml) compilado.
			// Ou seja, o arquivo .jasper
			JasperReport jr = (JasperReport) JRLoader
					.loadObject(localRelatorio);
			// JREmptyDataSource é uma implementaçao de JRDataSource, o qual é
			// requerido
			// como parametro para preencher o relatório criado.
			// Ele armazena o dados do ResultSet, que, neste caso, é vazio
			JREmptyDataSource jrEDS = new JREmptyDataSource();
			// Jasper Print representa o relatório gerado.
			// É criado um JasperPrint a partir de um JasperReport, contendo o
			// relatório preenchido.
			this.relGerado = JasperFillManager
					.fillReport(jr, parameters, jrEDS);
		} catch (JRException e) {
			throw e;
		}
	}

	// Cria um novo relatorio
	public Relatorio(Map parameters, String cam) throws JRException, Exception {

		this.arquivo = (String) parameters.get("tipoRelatorio");
		this.caminhoDestino = cam;

		try {
			JasperDesign design = JRXmlLoader.load(this.caminhoDestino + "/"
					+ this.arquivo);

			JasperReport jr = JasperCompileManager.compileReport(design);
			this.relGerado = JasperFillManager.fillReport(jr, parameters,
					this.con);
		} catch (Exception e) {
			throw e;
		}
	}

	// Exibe o relatório na tela.
	public void exibirRelatorio() {
		// emite o relatório na tela
		// false indica que a aplicação não será finalizada caso o relatório
		// seja fechado
		JasperViewer.viewReport(this.relGerado, false);
	}

	public void exibirStreamRelatorio() throws JRException, IOException,
			IllegalStateException {

		try {
			JasperExportManager.exportReportToPdfStream(this.relGerado,
					ServletActionContext.getResponse().getOutputStream());
		} catch (JRException jre) {
			throw jre;
		}
	}

	// Exibe o relatório na tela.
	public void gerarRelatorio() throws JRException, IOException,
			IllegalStateException {
		
		 byte[] buf = new byte[2000 * 1024];   
		 buf = JasperExportManager.exportReportToPdf(this.relGerado);
		try { 
			ServletActionContext.getResponse().getOutputStream().write(buf,0,buf.length);
		} catch (IllegalStateException ise) {
			throw ise;
		}
	}

	public OutputStream gerarStreamPDF() throws JRException, IOException {

		try {
			byte[] bytePDF = JasperExportManager
					.exportReportToPdf(this.relGerado);
			OutputStream osPDF = new BufferedOutputStream(
					new ByteArrayOutputStream(bytePDF.length));

			osPDF.write(bytePDF);
			osPDF.flush();
			return osPDF;
		} catch (IOException e) {
			throw e;
		}
	}

	public String gerarHTML() {
		String strHTML = null;
		OutputStream osHTML = null;
		File fileHTML = null;
		String fileName = System.currentTimeMillis() + ".pdf";
		try {
			strHTML = this.lerHTML(osHTML, fileHTML, fileName);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		} catch (JRException jrex) {
			throw new RuntimeException(jrex);
		} finally {
			try {
				this.fecharStreams(osHTML, fileHTML, fileName);
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}
		return strHTML;
	}

	private void fecharStreams(OutputStream osHTML, File fileHTML,
			String fileName) throws IOException {
		if (osHTML != null) {
			osHTML.flush();
			osHTML.close();
		}
		if ((fileHTML != null) && fileHTML.exists()) {
			fileHTML.delete();
		}
		fileHTML = new File(fileName + "_files");
		if ((fileHTML != null) && fileHTML.exists() && fileHTML.isDirectory()) {
			fileHTML.delete();
		}
	}

	private String lerHTML(OutputStream osHTML, File fileHTML, String fileName)
			throws IOException, JRException {

		HttpServletResponse response = ServletActionContext.getResponse();
		fileHTML = new File(this.caminhoDestino + "/" + fileName);
		JasperExportManager.exportReportToPdfFile(this.relGerado,
				this.caminhoDestino + "/" + fileName);

		FileInputStream rd = new FileInputStream(this.caminhoDestino + "/"
				+ fileName);
		StringBuffer sbHTML = new StringBuffer();

		response.setContentType("application/pdf");
		response.setContentLength(sbHTML.length());
		int c;
		while ((c = rd.read()) != 1) {
			response.getOutputStream().write(c);
		}
		rd.close();
		return sbHTML.toString();
	}

	/* Grava o relatório em um arquivo de formato pdf. */
	public void exportaParaPdf(Map parameters) throws JRException {

		String nArquivo = (String) parameters.get("tipoRelatorio");
		this.arquivo = nArquivo.replace("jrxml", "");

		try {
			// Gerar o arquivo PDF
			JasperExportManager.exportReportToPdfFile(this.relGerado,
					this.caminhoDestino + this.arquivo + "pdf");
		} catch (JRException e) {
			throw e;
		}
	}

	// Grava o relatório em um arquivo de formato html.
	public void exportaParaHtml(Map parameters) throws JRException {

		String nArquivo = (String) parameters.get("tipoRelatorio");
		this.arquivo = nArquivo.replace("jrxml", "html");

		try {
			JasperExportManager.exportReportToHtmlFile(this.relGerado,
					this.caminhoDestino + this.arquivo);
		} catch (JRException e) {
			throw e;
		}
	}

	// Envia o relatório para impressão, exibindo uma caixa de dialogo de
	// impressão ou não.
	public void imprimir(boolean exibeCaixaDialogo) throws JRException {

		try {
			// Imprime o relatório
			// o segundo parâmetro indica se existirá uma caixa de dialogo antes
			// ou nao
			JasperPrintManager.printReport(this.relGerado, exibeCaixaDialogo);
		} catch (JRException e) {
			throw e;
		}
	}

}
