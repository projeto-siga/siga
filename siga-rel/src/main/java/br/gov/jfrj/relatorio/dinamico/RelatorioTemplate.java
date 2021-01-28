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
package br.gov.jfrj.relatorio.dinamico;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import br.gov.jfrj.siga.base.AplicacaoException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * USE ESTA CLASSE para a criação de relatórios rápidos.<br>
 * 
 * Procedimentos para criar relatorios no siga-ex<br>
 * 
 * RESUMO:<br>
 * 1) Criar a classe do relatório (extends RelatorioTemplate)<br>
 * 2) Implementar os métodos:<br>
 * 2.1) construtor passando um Map <-- Com o parâmentros que podem ser usados no
 * relatório.<br>
 * 2.2) configurarRelatorio() <-- Para definir o layout do relatório.<br>
 * 2.3) processarDados() <-- Para gerar um Set ou List contendo os dados do
 * relatório.<br>
 * 3) Usar o relatório:<br>
 * Exemplo: MeuRelatorio r = new MeuRelatorio(null); r.gerar();
 * JasperViewer.viewReport(r.getRelatorioJasperPrint());
 * 
 * 
 * <br>
 * 
 * <br>
 * A) Crie a classe do relatorio<br>
 * a.1) Crie um builder baseado no AbstractRelatorioBaseBuilder caso não use o
 * RelatorioTemplate ou RelatorioRapido<br>
 * a.2) Crie a classe do relatorio baseada no template
 * (br.gov.jfrj.siga.ex.relatorio.dinamico.RelatorioTemplate.java)<br>
 * <br>
 * 1) Crie o menu do relatório (/sigaex/WebContent/paginas/menus/menu.jsp)<br>
 * Exemplo:<br>
 * <li><ww:url id="url" action="relRelatorios"<br>
 * namespace="/expediente/rel"><br>
 * <ww:param name="nomeArquivoRel">relModelos.jsp</ww:param><br>
 * </ww:url> <ww:a href="%{url}">Relatório de Modelos</ww:a></li> <br>
 * 
 * 
 * 2) Insira o código de teste do .jsp no relatorio.jsp
 * (/sigaex/WebContent/paginas/expediente/relatorio.jsp) <______ ATENÇÃO
 * /sigaex/WebContent/paginas/EXPEDIENTE!!!!!!/relatorio.jsp<br>
 * <br>
 * <br>
 * 2.1) Informe o nome do arquivo<br>
 * 2.2) Informe o actionName<br>
 * 2.3) Informe o título da página<br>
 * 2.4) Informe o nomeRelatorio<br>
 * <br>
 * Exemplo: <br>
 * </c:when><br>
 * <c:when test='${param.nomeArquivoRel eq "NOME_DO_RELATORIO.jsp"}'><br>
 * <c:set var="actionName" scope="request">emiteRelNOME_DO_RELATORIO</c:set><br>
 * <c:set var="titulo_pagina" scope="request">NOME_DO_RELATORIO</c:set><br>
 * <c:set var="nomeRelatorio" scope="request">relNOME_DO_RELATORIO.jsp</c:set><br>
 * </c:when><br>
 * <br>
 * 3) Crie a página .jsp que receberá os parâmetros do relatório<br>
 * /sigaex/WebContent/paginas/expediente/relatorios/<nomeDoRelatorio>.jsp<br>
 * <br>
 * 4) Crie a action no xwork.xml<br>
 * 4.1) Informe o nome da action (emiteRel...)<br>
 * 4.2) Informe a classe (br.gov.jfrj.webwork.action.ExRelatorioAction)<br>
 * 4.3) Informe o método que tratará o relatório<br>
 * 4.5) Informe o result name = relatorio<br>
 * 4.6) Informe o contentType = application/pdf<br>
 * 4.7) Informe o inputName = inputStream<br>
 * <br>
 * Ex:<br>
 * <br>
 * <action name="emiteRelDocumentosSubordinados"<br>
 * class="br.gov.jfrj.webwork.action.ExRelatorioAction"<br>
 * method="aRelDocumentosSubordinados"><br>
 * <result name="relatorio" type="stream"><br>
 * <param name="contentType">application/pdf</param><br>
 * <param name="inputName">inputStream</param><br>
 * </result><br>
 * </action><br>
 * <br>
 * 5) No br.gov.jfrj.webwork.action.ExRelatorioAction.java inclua o método que
 * vai gerar o relatório<br>
 * 5.1) Use um código como esse para gerar o relatorio<br>
 * <br>
 * public String aRelDocumentosSubordinados() throws Exception {<br>
 * <br>
 * Map parametros = new HashMap<String, String>(); <-- Para passar parâmetros
 * para o relatório<br>
 * <br>
 * <br>
 * Obrigatório para
 * RelatórioRapido--->parametros.put("secaoUsuario",getRequest()
 * .getParameter("secaoUsuario"));<br>
 * <br>
 * parametros.put("lotacao",getRequest().getParameter(
 * "lotacaoDestinatarioSel.sigla"));<br>
 * parametros.put("tipoFormaDoc", getRequest().getParameter("tipoFormaDoc"));<br>
 * parametros.put("incluirSubordinados",
 * getRequest().getParameter("incluirSubordinados"));<br>
 * parametros.put("lotacaoTitular",
 * getRequest().getParameter("lotacaoTitular"));<br>
 * parametros.put("orgaoUsuario",getRequest().getParameter("orgaoUsuario"));<br>
 * parametros.put("link_siga","http://" + getRequest().getServerName() + ":" <br>
 * + getRequest().getServerPort() <br>
 * + getRequest().getContextPath() + "/app/expediente/doc/exibir?id=");<br>
 * <br>
 * RelatorioDocumentosSubordinados rel = new
 * RelatorioDocumentosSubordinados(parametros);<br>
 * <br>
 * rel.gerar();<br>
 * <br>
 * this.setInputStream(new ByteArrayInputStream( rel.getRelatorioPDF()));<br>
 * <br>
 * return "relatorio";<br>
 * <br>
 * <br>
 * 5.1) Na última linha do método coloque (return "relatorio";)<br>
 * <br>
 * 
 * DICA: Para testar o relatório na própria classe, use o método main. Exemplo <br>
 * public static void main(String[] args){<br>
 * <br>
 * try {<br>
 * RelatorioEstatisticaProcedimento rep = new
 * RelatorioEstatisticaProcedimento(null);<br>
 * rep.gerar();<br>
 * JasperViewer.viewReport(rep.getRelatorioJasperPrint());<br>
 * } catch (JRException e) {<br>
 * // TODO Auto-generated catch block<br>
 * e.printStackTrace();<br>
 * } catch (DJBuilderException e) {<br>
 * // TODO Auto-generated catch block<br>
 * e.printStackTrace();<br>
 * } catch (Exception e) {<br>
 * // TODO Auto-generated catch block<br>
 * e.printStackTrace();<br>
 * }<br>
 * 
 * @author kpf
 * 
 */

public abstract class RelatorioTemplate extends RelatorioRapido {

	private Collection dados;
	protected AbstractRelatorioBaseBuilder relatorio;

	/**
	 * Caso o relatório precise receber parâmetros, use o construtor para
	 * obrigar o utilizador do relatório a informá-los. O ideal é que as
	 * validações dos parâmetros sejam feitas aqui no construtor<br>
	 * Exemplo: <br>
	 * public RelatorioDocumentosSubordinados(Map parametros) throws
	 * DJBuilderException {
	 * 
	 * super(parametros); if (parametros.get("secaoUsuario") == null){ throw new
	 * DJBuilderException("Parâmetro secaoUsuario não informado!"); } if
	 * (parametros.get("link_siga") == null){ throw new
	 * DJBuilderException("Parâmetro link_siga não informado!"); } }
	 */
	public RelatorioTemplate(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	public void gerar() throws Exception {

		relatorio = configurarRelatorio();
		dados = processarDados();
		if (dados != null && dados.size() > 0) {
			relatorio.setDados(dados);
		} else {
			throw new AplicacaoException("Não há dados para gerar o relatório!");
		}

	}

	/**
	 * Classe que permite o uso do Design Pattern Template Method.
	 * 
	 * Implemente este método para definir o desenho do relatório usando um
	 * builder. Use a classe RelatorioRapido como builder para gerar relatórios
	 * rápidos.<BR>
	 * Caso queira um builder de relatório personalizado, estenda a classe
	 * AbstractRelatorioBaseBuilder.<BR>
	 * 
	 * <br>
	 * Exemplo:<br>
	 * public AbstractRelatorioBaseBuilder configurarRelatorio() throws
	 * DJBuilderException, JRException {
	 * 
	 * this.setTitle("Relatório de Documentos em Setores Subordinados");
	 * this.addColuna("Setor", 0,RelatorioRapido.ESQUERDA,true);
	 * this.addColuna("Documento", 25,RelatorioRapido.ESQUERDA,false,true);
	 * this.addColuna("Descrição", 50,RelatorioRapido.ESQUERDA,false);
	 * this.addColuna("Responsável", 30,RelatorioRapido.ESQUERDA,false);
	 * this.addColuna("Situação", 30,RelatorioRapido.ESQUERDA,false);
	 * 
	 * return this; }
	 * 
	 * @return Retorna um builder de relatórios.
	 * @throws DJBuilderException
	 * @throws JRException
	 * @throws ColumnBuilderException
	 */
	public abstract AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException, ColumnBuilderException;

	/**
	 * Implemente este método para processar os dados que serão exibidos no
	 * relatório. É neste método que se faz as consultas a um banco de dados,
	 * por exemplo. <br>
	 * CUIDADO: A coleção de dados deve ser ordenada, senão os dados vão
	 * aparecer desordenados no relatório. Exemplo:<br>
	 * public Collection processarDados() {
	 * 
	 * List<String> d = new LinkedList<String>(); Query q =
	 * HibernateUtil.getSessao().createQuery("from ExModelo order by
	 * exFormaDocumento.descrFormaDoc");
	 * 
	 * for (Iterator<ExModelo> iterator = q.list().iterator();
	 * iterator.hasNext();) { ExModelo modelo = (ExModelo) iterator.next();
	 * d.add(modelo.getExFormaDocumento().getDescrFormaDoc());
	 * d.add(modelo.getNmMod()); d.add(modelo.getExClassificacao().getSigla());
	 * d.add(modelo.getExClassCriacaoVia().getSigla()); }
	 * 
	 * return d; }
	 * 
	 * 
	 * @return Retorna um Collection contendo os dados do relatório.
	 */
	public abstract Collection processarDados() throws Exception;

	/**
	 * Método que gera o relatório em PDF.<br>
	 * 
	 * @throws JRException
	 */
	public byte[] getRelatorioPDF() throws JRException {
		return JasperExportManager.exportReportToPdf(relatorio
				.getRelatorioJasperPrint());
		// JasperViewer.viewReport(relatorio.getRelatorio());
	}

	public StringBuffer getRelatorioHTML() throws JRException {
		
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(relatorio.getRelatorioJasperPrint()));
		
		/*
		 * Este metodo foi ajusta com causa de PROBLEMA DE COMPATIBILIDADE COM O JASPER 6.0
		 */
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		
		return new StringBuffer();
	}

	public byte[]  getRelatorioExcel() throws JRException, IOException {
		try (ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()) {
			JRXlsxExporter excelExp = new JRXlsxExporter();
			
			relatorio.estiloColuna.setPadding(15);
			relatorio.estiloColuna.setVerticalAlign(VerticalAlign.MIDDLE);
			
			excelExp.setParameter(JRExporterParameter.JASPER_PRINT,
					relatorio.getRelatorioJasperPrint());
			excelExp.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
			excelExp.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 0);
			excelExp.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
			excelExp.setParameter(JRXlsExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED, true);
			excelExp.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, false);
			excelExp.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			
			excelExp.exportReport();
			byte[] arquivo = xlsReport.toByteArray();
			return arquivo;
		}
	}

}
