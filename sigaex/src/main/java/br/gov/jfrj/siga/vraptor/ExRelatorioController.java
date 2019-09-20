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
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelClassificacao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelArmazenamento;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelConsultaDocEntreDatas;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocSubordinadosCriados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocsClassificados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocsOrgaoInteressado;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocumentosForaPrazo;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelDocumentosProduzidos;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovCad;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovProcesso;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovimentacao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelMovimentacaoDocSubordinados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelOrgao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelTempoTramitacaoPorEspecie;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelTipoDoc;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelVolumeTramitacao;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelVolumeTramitacaoPorModelo;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelatorioDocumentosSubordinados;
import br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios.RelatorioModelos;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.builder.ExMobilBuilder;

@Resource
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
	private static final String ACESSO_IGESTAO = "IGESTAO:Relatório de Indicadores de Gestão";
	private static final String ACESSO_RELDOCVOL = "RELDOCVOL:Relatório de documentos por volume";
	private static final String ACESSO_ORGAOINT = "ORGAOINT:Relatório de Documentos Por Órgão Interessado";
	private static final String ACESSO_RELFORAPRAZO = "RELFORAPRAZO:Relatório de documentos fora do prazo";
	private static final String ACESSO_RELDEVPROGRAMADA = "RELDEVPROGRAMADA:Relatório de documentos devolção programada";
	private static final String ACESSO_TRAMESP = "TRAMESP: Tempo Médio de Tramitação Por Espécie Documental";
	private static final String ACESSO_VOLTRAMMOD = "VOLTRAMMOD: Volume de Tramitação Por Nome do Documento";
	private static final String ACESSO_ARMAZ = "ARMAZ:Relatório de Páginas e Armazenamento Por Documento";
	private static final String APPLICATION_PDF = "application/pdf";
	
	public ExRelatorioController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
	}

	@Get
	@Post
	@Path("app/expediente/rel/relRelatorios")
	public void aRelRelatorios(final String nomeArquivoRel,
			final String primeiraVez, DpLotacaoSelecao lotacaoDestinatarioSel,
			DpLotacaoSelecao lotacaoSel, DpPessoaSelecao usuarioSel,
			String dataInicial, String dataFinal) {

		if (lotacaoDestinatarioSel == null) {
			lotacaoDestinatarioSel = new DpLotacaoSelecao();
		}

		if (lotacaoSel == null) {
			lotacaoSel = new DpLotacaoSelecao();
		}

		if (usuarioSel == null) {
			usuarioSel = new DpPessoaSelecao();
		}

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

	private void fazerResultsParaRelConsultaDocEntreDatas(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("listaExTipoDocumento", getTiposDocumento());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelCrDocSubordinados(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
	}

	private void fazerResultsParaRelDocClassificados(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("mascaraEntrada", this.getMascaraEntrada());
		result.include("mascaraSaida", this.getMascaraSaida());
		result.include("mascaraJavascript", this.getMascaraJavascript());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
	}

	private void fazerResultsParaRelDocumentosSubordinados(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelExpedientes(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
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

	private void fazerResultsParaRelMovCad(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovimentacao(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovimentacaoDosSubordinados(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelMovInconsistentes(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
	}

	private void fazerResultsParaRelMovProcesso(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelOrgao(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("orgaosUsu", this.getOrgaosUsu());
		result.include("lotacaoDestinatarioSel", lotacaoDestinatarioSel);
		result.include("titular", this.getTitular());
	}

	private void fazerResultsParaRelTipoDoc(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
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
	public Download aRelFormularios(final String secaoUsuario,
			final String orgaoUsuario, final String lotacaoTitular,
			final String idTit, final String nomeArquivoRel) throws Exception {

		assertAcesso(ACESSO_FORMS);

		final Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("orgaoUsuario", orgaoUsuario);
		parametros.put("lotacaoTitular", lotacaoTitular);
		parametros.put("idTit", idTit);

		final RelatorioModelos rel = new RelatorioModelos(parametros);
		rel.gerar();
		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelFormularios");
	}

	@Get("app/expediente/rel/emiteRelExpedientes")
	public Download aRelExpedientes(
			final DpLotacaoSelecao lotacaoDestinatarioSel) throws JRException,
			ParseException {
		final String dataInicio = param("dataInicio");
		final String dataFim = param("dataFim");

		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tipoRelatorio", param("tipoRelatorio"));
		parameters.put("secaoUsuario", param("secaoUsuario"));
		parameters.put("dataInicio", dataInicio);
		parameters.put("dataFim", dataFim);

		if (lotacaoDestinatarioSel != null
				&& lotacaoDestinatarioSel.getId() != null) {
			final DpLotacao lota = dao().consultar(
					lotacaoDestinatarioSel.getId(), DpLotacao.class, false);

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

		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelDocumentosSubordinados");
	}

	private InputStream aGeraRelatorio(final Map<String, Object> parameters)
			throws JRException {
		final String cam = (String) getContext().getRealPath(
				"/WEB-INF/page/exRelatorio/");
		final JasperDesign design = JRXmlLoader.load(cam + "/"
				+ (String) parameters.get("tipoRelatorio"));
		final JasperReport jr = JasperCompileManager.compileReport(design);
		final JasperPrint relGerado = JasperFillManager.fillReport(jr,
				parameters);
		return new ByteArrayInputStream(
				JasperExportManager.exportReportToPdf(relGerado));
	}

	@Get("app/expediente/rel/emiteRelModelos")
	public Download aRelModelos() throws Exception {
		final RelatorioModelos rm = new RelatorioModelos(null);
		rm.gerar();
		final InputStream inputStream = new ByteArrayInputStream(
				rm.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelDocumentosSubordinados");
	}

	@Get("app/expediente/rel/emiteRelDocumentosSubordinados")
	public Download aRelDocumentosSubordinados() throws Exception {
		assertAcesso(ACESSO_SUBORD);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest()
				.getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados",
				getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "app/expediente/doc/exibir?sigla=");

		final RelatorioDocumentosSubordinados rel = new RelatorioDocumentosSubordinados(
				parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelDocumentosSubordinados");
	}

	@Get("app/expediente/rel/emiteRelMovDocsSubordinados")
	public Download aRelMovDocumentosSubordinados() throws Exception {
		assertAcesso(ACESSO_MVSUB);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest()
				.getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados",
				getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelMovimentacaoDocSubordinados rel = new RelMovimentacaoDocSubordinados(
				parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelMovDocsSubordinados");
	}

	@Get("app/expediente/rel/emiteRelDocsSubCriados")
	public Download aRelDocsSubCriados() throws Exception {
		assertAcesso(ACESSO_CRSUB);

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("tipoFormaDoc", getRequest()
				.getParameter("tipoFormaDoc"));
		parametros.put("tipoRel", getRequest().getParameter("tipoRel"));
		parametros.put("incluirSubordinados",
				getRequest().getParameter("incluirSubordinados"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("idTit", getRequest().getParameter("idTit"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelDocSubordinadosCriados rel = new RelDocSubordinadosCriados(
				parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelDocsSubCriados");
	}

	@SuppressWarnings("unchecked")
	private List<ExTipoFormaDoc> getListaExTipoFormaDoc() {
		final List<ExTipoFormaDoc> listaQry = (List<ExTipoFormaDoc>) HibernateUtil
				.getSessao().createQuery("from ExTipoFormaDoc").list();
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
		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelConsultaDocEntreDatas rel = new RelConsultaDocEntreDatas(
				parametros);

		try {
			rel.gerar();
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelDocEntreDatas");
	}

	@Get("app/expediente/rel/emiteRelMovimentacao")
	public Download aRelMovimentacao() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelMovimentacao rel = new RelMovimentacao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelMovimentacao");
	}

	@Get("app/expediente/rel/emiteRelMovCad")
	public Download aRelMovCad() throws Exception {
		assertAcesso(ACESSO_MOVCAD);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelMovCad rel = new RelMovCad(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelMovCad");
	}

	@Get("app/expediente/rel/emiteRelOrgao")
	public Download aRelOrgao() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		if (!getRequest().getParameter("orgaoUsu").isEmpty()) {
			parametros.put("orgao", getRequest().getParameter("orgaoUsu"));
		}
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelOrgao rel = new RelOrgao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelOrgao");
	}

	@Get("app/expediente/rel/emiteRelTipoDoc")
	public Download aRelTipoDoc() throws Exception {
		assertAcesso(ACESSO_DATAS);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		final RelTipoDoc rel = new RelTipoDoc(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelTipoDoc");
	}

	@Get("app/expediente/rel/emiteRelMovProcesso")
	public Download aRelMovProcesso() throws Exception {
		assertAcesso(ACESSO_RELMVP);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		final Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
		}

		final Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("processo", getRequest().getParameter("processo"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelMovProcesso rel = new RelMovProcesso(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelMovProcesso");
	}

	@Get("app/expediente/rel/aRelClassificacao")
	public Download aRelClassificacao() throws Exception {
		assertAcesso(ACESSO_CLSD);

		final Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("codificacao", getRequest().getParameter("codificacao"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));

		final RelClassificacao rel = new RelClassificacao(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"aRelClassificacao");
	}

	@Get("app/expediente/rel/emiteRelClassDocDocumentos")
	public Download aRelClassDocDocumentos() throws Exception {
		assertAcesso(ACESSO_CLSD_DOCS);

		final Map<String, String> parametros = new HashMap<String, String>();
		String codificacao = getRequest().getParameter("codificacao");
		String idLotacao = getRequest().getParameter(
				"lotacaoDestinatarioSel.id");
		String idOrgaoUsu = getRequest().getParameter("orgaoUsuario");
		String secaoUsuario = getRequest().getParameter("secaoUsuario");

		if ((codificacao == null || codificacao.length() == 0)
				&& (idLotacao == null || idLotacao.length() == 0)) {
			throw new AplicacaoException(
					"Especifique pelo menos um dos parâmetros!");
		}

		parametros.put("codificacao", codificacao);
		parametros.put("idLotacao", idLotacao);
		parametros.put("idOrgaoUsu", idOrgaoUsu);
		parametros.put("secaoUsuario", secaoUsuario);

		final RelDocsClassificados rel = new RelDocsClassificados(parametros);
		rel.gerar();

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"emiteRelClassDocDocumentos");
	}

	@Get
	@Path("app/expediente/rel/relIndicadoresGestao")
	public void relIndicadoresGestao(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez) throws Exception {

		try {
			assertAcesso(ACESSO_IGESTAO);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelDocumentosProduzidos rel = new RelDocumentosProduzidos(
						parametros);
				rel.gerar();
				rel.processarDadosTramitados();

				List<String> indicadoresProducao = new ArrayList();

				for (int i = 0; i < rel.listDados.size(); i++) {
					indicadoresProducao.add(rel.listDados.get(i));
				}
				result.include("indicadoresProducao", indicadoresProducao);
				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
				result.include("totalPaginas", rel.totalPaginas.toString());
				result.include("totalTramitados",
						rel.totalTramitados.toString());

				final RelVolumeTramitacao relVol = new RelVolumeTramitacao(
						parametros);
				relVol.gerar();
				List<List<String>> volumeTramitacao = new ArrayList<List<String>>();

				for (int i = 0; i < relVol.listColunas.size(); i++) {
					List<String> linhaTramitacao = new ArrayList<String>();
					linhaTramitacao.add(relVol.listColunas.get(i));
					linhaTramitacao.add(relVol.listDados.get(i));
					volumeTramitacao.add(linhaTramitacao);
				}
				result.include("volumeTramitacao", volumeTramitacao);
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}
		result.include("primeiraVez", false);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get
	@Path("app/expediente/rel/relDocsOrgaoInteressado")
	public void relDocsOrgaoInteressado(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, final Long orgaoPesqId, boolean primeiraVez)
			throws Exception {

		try {
			assertAcesso(ACESSO_ORGAOINT);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("orgaoPesqId", orgaoPesqId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelDocsOrgaoInteressado rel = new RelDocsOrgaoInteressado(
						parametros);
				rel.gerar();

				String unidadeAtual = "";
				CpOrgaoUsuario ou = new CpOrgaoUsuario();
				ou.setIdOrgaoUsu(orgaoPesqId);

				result.include("listLinhas", rel.listLinhas);
				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
				result.include("orgaoPesqName", CpDao.getInstance()
						.consultarPorId(ou).getNmOrgaoUsu());
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		result.include("primeiraVez", false);
		result.include("orgaoPesqId", orgaoPesqId);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get
	@Path("app/expediente/rel/carregar_lista_orgaos")
	public void aCarregarListaOrgaos(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, final Long orgaoPesqId, boolean primeiraVez)
			throws Exception {
		try {
			assertAcesso(ACESSO_ORGAOINT);

			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (orgaoUsu != orgaoSelId) {
				throw new AplicacaoException(
						"Não é permitido consultas de outros órgãos.");
			}
			consistePeriodo(dataInicial, dataFinal);
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			List listOrgaos = dao().consultarOrgaosMarcadosComo(orgaoUsu,
					lotacaoSel.getId(), usuarioSel.getId(),
					formatter.parse((String) dataInicial),
					formatter.parse((String) somaUmDia(dataFinal)),
					CpMarcador.MARCADOR_COMO_INTERESSADO);
			if (listOrgaos == null) {
				throw new AplicacaoException(
						"Não foram encontrados documentos para a pesquisa solicitada.");
			}
			result.include("listOrgaos", listOrgaos);
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		result.include("primeiraVez", false);
		result.include("orgaoPesqId", orgaoPesqId);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
		result.use(Results.page()).forwardTo(
				"/WEB-INF/page/exRelatorio/relDocsOrgaoInteressado.jsp");
	}

	@Get
	@Path("app/expediente/rel/relTempoTramitacaoPorEspecie")
	public void relTempoTramitacaoPorEspecie(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez)
			throws Exception {

		try {
			assertAcesso(ACESSO_TRAMESP);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_especie", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/rel/emiteRelTempoTramitacaoPorEspecie");
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelTempoTramitacaoPorEspecie rel = new RelTempoTramitacaoPorEspecie(
						parametros);
				rel.gerar();

				result.include("listColunas", rel.listColunas);
				result.include("listLinhas", rel.listLinhas);
				result.include("listEspecie", rel.listEspecie);
				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		result.include("primeiraVez", false);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get 
	@Path("app/expediente/rel/emiteRelTempoTramitacaoPorEspecie")
	public Download aRelTempoTramitacaoPorEspecie(final String idFormaDoc,
			final String descrFormaDoc, final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal) throws Exception {

		assertAcesso(ACESSO_TRAMESP);
		final Map<String, String> parametros = new HashMap<String, String>();
		Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

		if (orgaoUsu != orgaoSelId) {
			throw new AplicacaoException(
					"Não é permitido consultas de outros órgãos.");
		}
		consistePeriodo(dataInicial, dataFinal);

		parametros.put("titulo", "Tempo Médio de Tramitação Por Espécie Documental");
		parametros.put("orgaoUsuario", getLotaTitular().getOrgaoUsuario()
				.getNmOrgaoUsu());
		parametros.put("descrEspecie", descrFormaDoc);

		parametros.put("orgao", orgaoSelId.toString());
		parametros.put("especie", idFormaDoc);
		parametros.put("lotacao",
				getRequest().getParameter("lotacaoSel.id"));
		parametros.put("usuario",
				getRequest().getParameter("usuarioSel.id"));
		parametros.put("dataInicial",
				getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", somaUmDia(getRequest()
				.getParameter("dataFinal")));
		parametros.put("link_siga", "http://"
				+ getRequest().getServerName() + ":"
				+ getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/app/expediente/doc/exibir?sigla=");

		final RelTempoTramitacaoPorEspecie rel = new RelTempoTramitacaoPorEspecie(
				parametros);
		rel.setTemplateFile("RelatorioBaseTempoTramitacaoPorEspecie.jrxml");
		rel.gerar();
		parametros.put("totalDocumentos", rel.totalDocumentos.toString());
		if (getRequest().getParameter("lotacaoSel.descricao") != "" )
			parametros.put("lotacaoRel",
				getRequest().getParameter("lotacaoSel.descricao"));
		else
			parametros.put("lotacaoRel","Todas");

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"relTempoTramitacaoPorEspecie");
	}

	@Get
	@Path("app/expediente/rel/relVolumeTramitacaoPorModelo")
	public void relVolumeTramitacaoPorModelo(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, String idMod, boolean primeiraVez)
			throws Exception {

		try {
			assertAcesso(ACESSO_VOLTRAMMOD);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_modelo", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/rel/relVolumeTramitacaoPorModelo");
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibirHistorico?sigla=");
				final RelVolumeTramitacaoPorModelo rel = new RelVolumeTramitacaoPorModelo(
						parametros);
				if (idMod == null) {
					rel.processarModelos();
					result.include("listModelos", rel.listModelos);
					result.include("totalDocumentos",
							rel.totalDocumentos.toString());
					result.include("totalTramites",
							rel.totalTramites.toString());
				} else {
					parametros.put("idMod", idMod);
					rel.gerar();
					result.include("listColunas", rel.listColunas);
					result.include("listLinhas", rel.listLinhas);
					result.include("totalModeloTramites",
							rel.totalModeloTramites.toString());
				}
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		result.include("primeiraVez", false);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get
	@Path("app/expediente/rel/relDocumentosPorVolume")
	public void relDocumentosPorVolume(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez) throws Exception {

		List<String> indicadoresProducao = new ArrayList();
		Locale localeBR = new Locale("pt", "BR");
		NumberFormat inteiro = NumberFormat.getInstance();

		try {
			assertAcesso(ACESSO_RELDOCVOL);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelDocumentosProduzidos rel = new RelDocumentosProduzidos(
						parametros);
				rel.gerar();

				String unidadeAtual = "";

				for (int i = 0; i < rel.listDados.size(); i++) {

					String resultado = "";

					if (!unidadeAtual.equals(rel.listDados.get(i))) {
						resultado = "<thead class='thead-light'>" + "<tr>"
								+ "<th rowspan='1' align='center'>"
								+ rel.listDados.get(i) + "</th>";
						unidadeAtual = rel.listDados.get(i);
					} else {
						resultado = "<thead>" + "<tr>"
								+ "<th rowspan='1' align='center'></th>";
					}

					i++;

					resultado += "<th colspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>";

					i++;

					resultado += "<th rowspan='1' class='text-right'>"
							+ String.format("%20s", inteiro.format(Long
									.parseLong(rel.listDados.get(i))))
							+ "</th>" + "</tr>" + "</thead>";

					indicadoresProducao.add(resultado);
				}

				result.include("totalDocumentos", String.format("%20s", inteiro
						.format(Long.parseLong(rel.totalDocumentos.toString()))
						.toString()));
				result.include("totalPaginas", String.format("%20s", inteiro
						.format(Long.parseLong(rel.totalPaginas.toString()))));
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		if (primeiraVez == false) {
			result.include("primeiraVez", false);
		}

		result.include("tamanho", indicadoresProducao.size());
		result.include("indicadoresProducao", indicadoresProducao);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get
	@Path("app/expediente/rel/relDocumentosForaPrazo")
	public void relDocumentosForaPrazo(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez) throws Exception {

		List<String> indicadoresProducao = new ArrayList();
		try {
			assertAcesso(ACESSO_RELFORAPRAZO);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				final Date dtIni = df.parse(dataInicial);
				final Date dtFim = df.parse(dataFinal);
				Date dataHoje = new Date(System.currentTimeMillis());
				dataHoje = df.parse(df.format(dataHoje));

				if (dtIni.compareTo(dataHoje) >= 0
						|| dtFim.compareTo(dataHoje) >= 0) {
					throw new AplicacaoException(
							"Data inicial ou data final não pode ser igual ou maior que a data atual.");
				}

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal",
						getRequest().getParameter("dataFinal"));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelDocumentosForaPrazo rel = new RelDocumentosForaPrazo(
						parametros);
				rel.gerar();

				String unidadeAtual = "";

				for (int i = 0; i < rel.listDados.size(); i++) {

					String resultado = "";

					if (!unidadeAtual.equals(rel.listDados.get(i))) {
						resultado = "<thead class='thead-light'>" + "<tr>"
								+ "<th rowspan='1' align='center'>"
								+ rel.listDados.get(i) + "</th>";
						unidadeAtual = rel.listDados.get(i);
					} else {
						resultado = "<thead>" + "<tr>"
								+ "<th rowspan='1' align='center'></th>";
					}

					i++;

					resultado += "<th colspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>";

					i++;

					resultado += "<th rowspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>";

					i++;

					resultado += "<th rowspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>" + "</tr>"
							+ "</thead>";

					indicadoresProducao.add(resultado);
				}

				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
			}
		} catch (Exception e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		if (primeiraVez == false) {
			result.include("primeiraVez", false);
		}

		result.include("tamanho", indicadoresProducao.size());
		result.include("indicadoresProducao", indicadoresProducao);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Get
	@Path("app/expediente/rel/relDocumentosDevolucaoProgramada")
	public void relDocumentosDevolucaoProgramada(
			final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez) throws Exception {

		List<String> indicadoresProducao = new ArrayList();
		try {
			assertAcesso(ACESSO_RELDEVPROGRAMADA);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			Long orgaoSelId = getIdOrgaoSel(lotacaoSel, usuarioSel, orgaoUsu);

			if (!primeiraVez) {
				if (orgaoUsu != orgaoSelId) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);

				final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				final Date dtIni = df.parse(dataInicial);
				final Date dtFim = df.parse(dataFinal);
				Date dataHoje = new Date(System.currentTimeMillis());
				dataHoje = df.parse(df.format(dataHoje));

				if (dtIni.compareTo(dataHoje) < 0
						|| dtFim.compareTo(dataHoje) < 0) {
					throw new AplicacaoException(
							"Data inicial ou data final não pode ser menor que a data atual.");
				}

				parametros.put("orgao", orgaoSelId.toString());
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal",
						getRequest().getParameter("dataFinal"));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelDocumentosForaPrazo rel = new RelDocumentosForaPrazo(
						parametros);
				rel.gerar();

				String unidadeAtual = "";

				for (int i = 0; i < rel.listDados.size(); i++) {

					String resultado = "";

					if (!unidadeAtual.equals(rel.listDados.get(i))) {
						resultado = "<thead class='thead-light'>" + "<tr>"
								+ "<th rowspan='1' align='center'>"
								+ rel.listDados.get(i) + "</th>";
						unidadeAtual = rel.listDados.get(i);
					} else {
						resultado = "<thead>" + "<tr>"
								+ "<th rowspan='1' align='center'></th>";
					}

					i++;

					resultado += "<th colspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>";

					i++;

					resultado += "<th rowspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>";

					i++;

					resultado += "<th rowspan='1' align='center'>"
							+ rel.listDados.get(i) + "</th>" + "</tr>"
							+ "</thead>";

					indicadoresProducao.add(resultado);
				}

				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
			}
		} catch (Exception e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		if (primeiraVez == false) {
			result.include("primeiraVez", false);
		}

		result.include("tamanho", indicadoresProducao.size());
		result.include("indicadoresProducao", indicadoresProducao);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
	}

	@Path("app/expediente/rel/emiteRelDocsPorVolumeDetalhes")
	public Download aRelDocsPorVolumeDetalhes(
			final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, String dataInicial,
			String dataFinal, boolean primeiraVez) throws Exception {
		assertAcesso(ACESSO_RELDOCVOL);

		Long orgaoUsu = 0L;
		Long orgaoSelId = 0L;

		final Map<String, String> parametros = new HashMap<String, String>();

		if (lotacaoSel.getId() != null) {
			DpLotacao lota = dao().consultar(lotacaoSel.getId(),
					DpLotacao.class, false);
			orgaoSelId = lota.getIdOrgaoUsuario();
			parametros.put("lotacaoRel", lota.getDescricao());
		} else {
			parametros.put("lotacaoRel", "Todas");
		}
		if (usuarioSel.getId() != null) {
			DpPessoa usu = dao().consultar(usuarioSel.getId(), DpPessoa.class,
					false);
			orgaoSelId = usu.getOrgaoUsuario().getIdOrgaoUsu();
		}
		orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		if (lotacaoSel.getId() == null && usuarioSel.getId() == null) {
			orgaoSelId = orgaoUsu;
		}
		if (orgaoUsu != orgaoSelId) {
			throw new AplicacaoException(
					"Não é permitido consultas de outros órgãos.");
		}

		consistePeriodo(dataInicial, dataFinal);

		parametros.put("orgao", orgaoSelId.toString());
		parametros.put("titulo", "Documentos Por Volume");
		parametros.put("orgaoUsuario", getLotaTitular().getOrgaoUsuario()
				.getNmOrgaoUsu());
		parametros.put("lotacao", getRequest().getParameter("lotacaoSel.id"));
		parametros.put("usuario", getRequest().getParameter("usuarioSel.id"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal",
				somaUmDia(getRequest().getParameter("dataFinal")));
		final RelDocumentosProduzidos rel = new RelDocumentosProduzidos(
				parametros);
		rel.setTemplateFile("RelatorioBaseGestao.jrxml");
		rel.gerarDetalhes();
		parametros.put("totalDocumentos", rel.totalDocumentos.toString());
		parametros.put("totalPaginas", rel.totalPaginas.toString());

		final InputStream inputStream = new ByteArrayInputStream(
				rel.getRelatorioPDF());
		return new InputStreamDownload(inputStream, APPLICATION_PDF,
				"relDocumentosPorVolumeDetalhes");
	}

	@Get
	@Path("app/expediente/rel/relArmazenamento")
	public void relArmazenamento(final Long orgaoPesqId,
			final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, final String dataInicial,
			final String dataFinal, final boolean getAll, boolean primeiraVez)
			throws Exception {

		List<CpOrgaoUsuario> listOrgaos = new ArrayList<CpOrgaoUsuario>();

		try {
			assertAcesso(ACESSO_ARMAZ);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())
					|| "PD".equals(getTitular().getOrgaoUsuario().getSigla())) {
				listOrgaos = dao().listarOrgaosUsuarios();
			} else {
				CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(
						getTitular().getOrgaoUsuario());
				listOrgaos.add(ou);
			}

			if (!primeiraVez) {
				if (!("ZZ".equals(getTitular().getOrgaoUsuario().getSigla()) || "PD"
						.equals(getTitular().getOrgaoUsuario().getSigla()))
						&& !orgaoUsu.equals(orgaoPesqId)) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);
				if (orgaoPesqId != 0) {
					parametros.put("orgao", orgaoPesqId.toString());
				}
				if (getAll) {
					parametros.put("getAll", "true");
				}
				parametros.put("lotacao",
						getRequest().getParameter("lotacaoSel.id"));
				parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				parametros.put("dataInicial",
						getRequest().getParameter("dataInicial"));
				parametros.put("dataFinal", somaUmDia(getRequest()
						.getParameter("dataFinal")));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelArmazenamento rel = new RelArmazenamento(parametros);
				rel.gerar();

				result.include("getAll", getAll);
				result.include("orgaoPesqId", orgaoPesqId);
				result.include("listLinhas", rel.listLinhas);
				result.include("listColunas", rel.listColunas);
				result.include("totalDocumentos",
						rel.totalDocumentos.toString());
				result.include("totalPaginas", rel.totalPaginas.toString());
				result.include("totalBlobsDoc", rel.totalBlobsDoc.toString());
				result.include("totalBlobsAnexos",
						rel.totalBlobsAnexos.toString());
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
		}

		if (primeiraVez == false) {
			result.include("primeiraVez", false);
		}

		result.include("listOrgaos", listOrgaos);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
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

	private void consistePeriodo(String dataInicial, String dataFinal)
			throws Exception {
		if (dataInicial == null || dataFinal == null) {
			throw new AplicacaoException(
					"Data inicial ou data final não informada.");
		}
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		final Date dtIni = df.parse(dataInicial);
		final Date dtFim = df.parse(dataFinal);
		if (dtFim.getTime() - dtIni.getTime() < 0L) {
			throw new AplicacaoException(
					"Data inicial maior que a data final.");
		}		
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L) {
			throw new AplicacaoException(
					"O intervalo máximo entre as datas deve ser de um ano.");
		}
	}

	private String somaUmDia(String dataFinal) throws Exception {
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtFim = df.parse(dataFinal);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dtFim);
		cal.add(Calendar.DATE, 1);
		return df.format(cal.getTime());
	}

	private Long getIdOrgaoSel(final DpLotacaoSelecao lotacaoSel,
			final DpPessoaSelecao usuarioSel, Long orgaoUsu) {
		Long orgaoSelId = 0L;
		if (lotacaoSel.getId() != null) {
			DpLotacao lota = dao().consultar(lotacaoSel.getId(),
					DpLotacao.class, false);
			orgaoSelId = lota.getIdOrgaoUsuario();
		}
		if (usuarioSel.getId() != null) {
			DpPessoa usu = dao().consultar(usuarioSel.getId(), DpPessoa.class,
					false);
			orgaoSelId = usu.getOrgaoUsuario().getIdOrgaoUsu();
		}
		if (lotacaoSel.getId() == null && usuarioSel.getId() == null) {
			orgaoSelId = orgaoUsu;
		}
		return orgaoSelId;
	}

	@Post
	@Path("app/expediente/rel/exportCsv")
	public Download exportCsv(final Long orgaoPesqId,	final DpLotacaoSelecao lotacaoSel,
				final DpPessoaSelecao usuarioSel, final String dataInicial,
				final String dataFinal, final boolean getAll, boolean primeiraVez,
				final String emailDest) throws Exception {
		List<CpOrgaoUsuario> listOrgaos = new ArrayList<CpOrgaoUsuario>();
		List<String> listColunas = new ArrayList<String>();
		List<List<String>> listLinhas = new ArrayList<List<String>>();

		try {
			assertAcesso(ACESSO_ARMAZ);

			final Map<String, String> parametros = new HashMap<String, String>();
			Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
			if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())
					|| "PD".equals(getTitular().getOrgaoUsuario().getSigla())) {
				listOrgaos = dao().listarOrgaosUsuarios();
			} else {
				CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(
						getTitular().getOrgaoUsuario());
				listOrgaos.add(ou);
			}

//			if (!primeiraVez) {
				if (!("ZZ".equals(getTitular().getOrgaoUsuario().getSigla()) || "PD"
						.equals(getTitular().getOrgaoUsuario().getSigla()))
						&& !orgaoUsu.equals(orgaoPesqId)) {
					throw new AplicacaoException(
							"Não é permitido consultas de outros órgãos.");
				}
				consistePeriodo(dataInicial, dataFinal);
				if (orgaoPesqId != 0) {
					parametros.put("orgao", orgaoPesqId.toString());
				}
				if (getAll) {
					parametros.put("getAll", "true");
				}
				if (!getRequest().getParameter("lotacaoSel.id").equals("")) {
					parametros.put("lotacao",
							getRequest().getParameter("lotacaoSel.id"));
				}
				if (!getRequest().getParameter("usuarioSel.id").equals("")) {
					parametros.put("usuario",
						getRequest().getParameter("usuarioSel.id"));
				}
				parametros.put("dataInicial", dataInicial);
				parametros.put("dataFinal", somaUmDia(dataFinal));
				parametros.put("link_siga", "http://"
						+ getRequest().getServerName() + ":"
						+ getRequest().getServerPort()
						+ getRequest().getContextPath()
						+ "/app/expediente/doc/exibir?sigla=");

				final RelArmazenamento rel = new RelArmazenamento(parametros);
				rel.gerar();
				listColunas = rel.listColunas;
				listLinhas = rel.listLinhas;
				
//				result.include("getAll", getAll);
//				result.include("orgaoPesqId", orgaoPesqId);
//				result.include("listLinhas", rel.listLinhas);
//				result.include("listColunas", rel.listColunas);
//				result.include("totalDocumentos",
//						rel.totalDocumentos.toString());
//				result.include("totalPaginas", rel.totalPaginas.toString());
//				result.include("totalBlobsDoc", rel.totalBlobsDoc.toString());
//				result.include("totalBlobsAnexos",
//						rel.totalBlobsAnexos.toString());
//			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-danger");
			result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exRelatorio/relArmazenamento.jsp");
		}

//		if (primeiraVez == false) {
//			result.include("primeiraVez", false);
//		}
//
		result.include("listOrgaos", listOrgaos);
		result.include("lotacaoSel", lotacaoSel);
		result.include("usuarioSel", usuarioSel);
		result.include("dataInicial", dataInicial);
		result.include("dataFinal", dataFinal);
		
		InputStream inputStream = null;
		StringBuffer texto = new StringBuffer();
		for (String col : listColunas) { 
			texto.append(col);
			texto.append(";");
		}
		texto.append(System.getProperty("line.separator"));
		for (List<String> rowCols : listLinhas) { 
			for (String rowCol : rowCols) { 
				texto.append(rowCol);
				texto.append(";");
			}
			texto.append(System.getProperty("line.separator"));
		}
		
		Correio.enviar(
				emailDest,
				"Relatório Armazenamento " + dataInicial + "-" + dataFinal,
				texto.toString()
				);
			
		inputStream = new ByteArrayInputStream(texto.toString().getBytes("UTF-8"));
		return new InputStreamDownload(inputStream, "text/csv", "RelDocs" + dataInicial 
				+ "-" + dataFinal + ".csv");	
	}
}
