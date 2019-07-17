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
 * Criado em : 25/04/2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelClassificacao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelConsultaDocEntreDatas;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocSubordinadosCriados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocsClassificados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovCad;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovProcesso;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovimentacao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovimentacaoDocSubordinados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelOrgao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelTipoDoc;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelatorioDocumentosSubordinados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelatorioModelos;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Controller
public class ExRelatorioController extends ExController {

	private static final String ACESSO_CLSD_DOCS = "CLSD:Classificação Documental;DOCS:Relação de documentos classificados";
	private static final String ACESSO_CLSD = "CLSD:Classificação Documental;CLASS:Relação de classificações";
	private static final String ACESSO_RELMVP = "RELMVP:Relação de movimentações de processos";
	private static final String ACESSO_MOVCAD = "MOVCAD:Relação de movimentações por cadastrante";
	private static final String ACESSO_DATAS = "DATAS:Relação de documentos entre datas";
	private static final String ACESSO_CRSUB = "CRSUB:Relatório de criação de documentos em setores subordinados";
	private static final String ACESSO_MVSUB = "MVSUB:Relatório de movimentação de documentos em setores subordinados";
	private static final String ACESSO_SUBORD = "SUBORD:Relatório de documentos em setores subordinados";
	private static final String ACESSO_FORMS = "FORMS:Relação de formulários";
	private static final String APPLICATION_PDF = "application/pdf";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExRelatorioController() {
		this(null, null, null, null, null, null);
	}

	@Inject
	public ExRelatorioController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
	}

	@Get("app/expediente/rel/relRelatorios")
	public void aRelRelatorios(final String nomeArquivoRel) {
		final DpLotacaoSelecao lotacaoDestinatarioSel = new DpLotacaoSelecao();
		result.include("nomeArquivoRel", nomeArquivoRel);

		if (nomeArquivoRel.equals("relDocumentosSubordinados.jsp")) {
			fazerResultsParaRelDocumentosSubordinados(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relClassificacao.jsp")) {
			fazerResultsParaRelClassificacao();
		} else if (nomeArquivoRel.equals("relConsultaDocEntreDatas.jsp")) {
			fazerResultsParaRelConsultaDocEntreDatas(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relCrDocSubordinados.jsp")) {
			fazerResultsParaRelCrDocSubordinados(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relDocsClassificados.jsp")) {
			fazerResultsParaRelDocClassificados(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relDocumentosSubordinados.jsp")) {
			fazerResultsParaRelDocumentosSubordinados(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relExpedientes.jsp")) {
			fazerResultsParaRelExpedientes(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relFormularios.jsp")) {
			fazerResultsParaRelFormularios();
		} else if (nomeArquivoRel.equals("relModelos.jsp")) {
			fazerResultsParaRelModelos();
		} else if (nomeArquivoRel.equals("relMovCad.jsp")) {
			fazerResultsParaRelMovCad(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relMovimentacao.jsp")) {
			fazerResultsParaRelMovimentacao(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relMovimentacaoDocSubordinados.jsp")) {
			fazerResultsParaRelMovimentacaoDosSubordinados(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relMovInconsistentes.jsp")) {
			fazerResultsParaRelMovInconsistentes(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relMovProcesso.jsp")) {
			fazerResultsParaRelMovProcesso(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relOrgao.jsp")) {
			fazerResultsParaRelOrgao(lotacaoDestinatarioSel);
		} else if (nomeArquivoRel.equals("relTipoDoc.jsp")) {
			fazerResultsParaRelTipoDoc(lotacaoDestinatarioSel);
		} else {
			throw new AplicacaoException("Modelo de relatório não definido!");
		}
	}

	private void fazerResultsParaRelClassificacao() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("mascaraEntrada", this.getMascaraEntrada());
		result.include("mascaraSaida", this.getMascaraSaida());
		result.include("mascaraJavascript", this.getMascaraJavascript());
	}

	private void fazerResultsParaRelConsultaDocEntreDatas(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("listaExTipoDocumento", getTiposDocumento());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelCrDocSubordinados(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
	}

	private void fazerResultsParaRelDocClassificados(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("mascaraEntrada", this.getMascaraEntrada());
		result.include("mascaraSaida", this.getMascaraSaida());
		result.include("mascaraJavascript", this.getMascaraJavascript());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
	}

	private void fazerResultsParaRelDocumentosSubordinados(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelExpedientes(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);

	}

	private void fazerResultsParaRelFormularios() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelModelos() {
		result.include("lotaTitular", this.getLotaTitular());
	}

	private void fazerResultsParaRelMovCad(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovimentacao(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovimentacaoDosSubordinados(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovInconsistentes(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
	}

	private void fazerResultsParaRelMovProcesso(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelOrgao(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("orgaosUsu", this.getOrgaosUsu());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelTipoDoc(final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private Map<Integer, String> getListaTipoRel() {
		final Map<Integer, String> listaTipoRel = new HashMap<Integer, String>();
		listaTipoRel.put(1, "Documentos Ativos");
		listaTipoRel.put(2, "Como Gestor");
		listaTipoRel.put(3, "Como Interessado");
		return listaTipoRel;
	}

	@Get("app/expediente/rel/emiteRelFormularios")
	public Download aRelFormularios(final String secaoUsuario, final String orgaoUsuario, final String lotacaoTitular, final String idTit,
			final String nomeArquivoRel) throws Exception {

		assertAcesso(ACESSO_FORMS);

		final Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("orgaoUsuario", orgaoUsuario);
		parametros.put("lotacaoTitular", lotacaoTitular);
		parametros.put("idTit", idTit);

		final RelatorioModelos rel = new RelatorioModelos(parametros);
		rel.gerar();
		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelFormularios");
	}

	@Get("app/expediente/rel/emiteRelExpedientes")
	public Download aRelExpedientes(final DpLotacaoSelecao lotacaoDestinatarioSel) throws JRException, ParseException {
		final String dataInicio = param("dataInicio");
		final String dataFim = param("dataFim");

		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tipoRelatorio", param("tipoRelatorio"));
		parameters.put("secaoUsuario", param("secaoUsuario"));
		parameters.put("dataInicio", dataInicio);
		parameters.put("dataFim", dataFim);

		if (lotacaoDestinatarioSel != null && lotacaoDestinatarioSel.getId() != null) {
			final DpLotacao lota = dao().consultar(lotacaoDestinatarioSel.getId(), DpLotacao.class, false);

			if (lota == null) {
				throw new AplicacaoException("Lotação inexistente");
			}

			parameters.put("lotacao", lota.getIdLotacao().toString());
			parameters.put("siglalotacao", lota.getSiglaLotacao());
		} else {
			throw new AplicacaoException("Lotação não informada");
		}

		if (dataInicio.equals("") || dataFim.equals("")) {
			throw new AplicacaoException("Data não informada");
		}

		final SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date dataI = formatador.parse(dataInicio);
		java.util.Date dataF = formatador.parse(dataFim);

		if (dataI.after(dataF)) {
			throw new AplicacaoException("Data informada não é válida");
		}

		final InputStream inputStream = aGeraRelatorio(parameters);

		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelDocumentosSubordinados");
	}

	private InputStream aGeraRelatorio(final Map<String, Object> parameters) throws JRException {
		final String cam = (String) getContext().getRealPath("/WEB-INF/page/exRelatorio/");
		final JasperDesign design = JRXmlLoader.load(cam + "/" + (String) parameters.get("tipoRelatorio"));
		final JasperReport jr = JasperCompileManager.compileReport(design);
		final JasperPrint relGerado = JasperFillManager.fillReport(jr, parameters);
		return new ByteArrayInputStream(JasperExportManager.exportReportToPdf(relGerado));
	}

	@Get("app/expediente/rel/emiteRelModelos")
	public Download aRelModelos() throws Exception {
		final RelatorioModelos rm = new RelatorioModelos(null);
		rm.gerar();
		final InputStream inputStream = new ByteArrayInputStream(rm.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelDocumentosSubordinados");
	}

	@Get("app/expediente/rel/emiteRelDocumentosSubordinados")
	public Download aRelDocumentosSubordinados() throws Exception {
		assertAcesso(ACESSO_SUBORD);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest().getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados", getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "app/expediente/doc/exibir?sigla=");

		final RelatorioDocumentosSubordinados rel = new RelatorioDocumentosSubordinados(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelDocumentosSubordinados");
	}

	@Get("app/expediente/rel/emiteRelMovDocsSubordinados")
	public Download aRelMovDocumentosSubordinados() throws Exception {
		assertAcesso(ACESSO_MVSUB);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest().getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados", getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelMovimentacaoDocSubordinados rel = new RelMovimentacaoDocSubordinados(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelMovDocsSubordinados");
	}

	@Get("app/expediente/rel/emiteRelDocsSubCriados")
	public Download aRelDocsSubCriados() throws Exception {
		assertAcesso(ACESSO_CRSUB);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest().getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados", getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelDocSubordinadosCriados rel = new RelDocSubordinadosCriados(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelDocsSubCriados");
	}

	@SuppressWarnings("unchecked")
	private List<ExTipoFormaDoc> getListaExTipoFormaDoc() {
		final List<ExTipoFormaDoc> listaQry = (List<ExTipoFormaDoc>) dao.listarTodos(ExTipoFormaDoc.class, null);
		final List<ExTipoFormaDoc> resultado = new LinkedList<ExTipoFormaDoc>();
		final ExTipoFormaDoc todos = new ExTipoFormaDoc();
		todos.setDescTipoFormaDoc("(Todos)");
		resultado.add(todos);
		resultado.addAll(listaQry);
		return resultado;
	}

	@Get("app/expediente/rel/emiteRelDocEntreDatas")
	public Download aRelDocEntreDatas() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("origem", getRequest().getParameter("origem"));
		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelConsultaDocEntreDatas rel = new RelConsultaDocEntreDatas(parametros);
		
		try {
			rel.gerar();
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelDocEntreDatas");
	}

	@Get("app/expediente/rel/emiteRelMovimentacao")
	public Download aRelMovimentacao() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelMovimentacao rel = new RelMovimentacao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelMovimentacao");
	}

	@Get("app/expediente/rel/emiteRelMovCad")
	public Download aRelMovCad() throws Exception {
		assertAcesso(ACESSO_MOVCAD);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest().getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelMovCad rel = new RelMovCad(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelMovCad");
	}

	@Get("app/expediente/rel/emiteRelOrgao")
	public Download aRelOrgao() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		if (!getRequest().getParameter("orgaoUsu").isEmpty()) {
			parametros.put("orgao", getRequest().getParameter("orgaoUsu"));
		}
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelOrgao rel = new RelOrgao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelOrgao");
	}

	@Get("app/expediente/rel/emiteRelTipoDoc")
	public Download aRelTipoDoc() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelTipoDoc rel = new RelTipoDoc(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelTipoDoc");
	}

	@Get("app/expediente/rel/emiteRelMovProcesso")
	public Download aRelMovProcesso() throws Exception {
		assertAcesso(ACESSO_RELMVP);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("processo", getRequest().getParameter("processo"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelMovProcesso rel = new RelMovProcesso(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelMovProcesso");
	}

	@Get("app/expediente/rel/aRelClassificacao")
	public Download aRelClassificacao() throws Exception {
		assertAcesso(ACESSO_CLSD);

		final Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("codificacao", getRequest().getParameter("codificacao"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));

		final RelClassificacao rel = new RelClassificacao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "aRelClassificacao");
	}

	@Get("app/expediente/rel/emiteRelClassDocDocumentos")
	public Download aRelClassDocDocumentos() throws Exception {
		assertAcesso(ACESSO_CLSD_DOCS);

		final Map<String, String> parametros = new HashMap<String, String>();
		String codificacao = getRequest().getParameter("codificacao");
		String idLotacao = getRequest().getParameter("lotacaoDestinatarioSel.id");
		String idOrgaoUsu = getRequest().getParameter("orgaoUsuario");
		String secaoUsuario = getRequest().getParameter("secaoUsuario");

		if ((codificacao == null || codificacao.length() == 0) && (idLotacao == null || idLotacao.length() == 0)) {
			throw new AplicacaoException("Especifique pelo menos um dos parâmetros!");
		}

		parametros.put("codificacao", codificacao);
		parametros.put("idLotacao", idLotacao);
		parametros.put("idOrgaoUsu", idOrgaoUsu);
		parametros.put("secaoUsuario", secaoUsuario);

		final RelDocsClassificados rel = new RelDocsClassificados(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF, "emiteRelClassDocDocumentos");
	}

	protected void assertAcesso(final String pathServico) {
		super.assertAcesso("REL:Gerar relatórios;" + pathServico);
	}

	private String getMascaraEntrada() {
		return MascaraUtil.getInstance().getMascaraEntrada();
	}

	private String getMascaraSaida() {
		return MascaraUtil.getInstance().getMascaraSaida();
	}

	private String getMascaraJavascript() {
		return SigaExProperties.getExClassificacaoMascaraJavascript();
	}

}
