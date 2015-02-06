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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

import org.hibernate.jdbc.Work;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExModelo;
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
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.persistencia.oracle.JDBCUtilOracle;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;

@Resource
public class ExRelatorioController extends ExController implements IUsaMascara{

	public ExRelatorioController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
		lotacaoDestinatarioSel = new DpLotacaoSelecao();
		
	}

	private List dataSource;

	private Connection connection;

	private boolean mostrarRel;

	private Integer idLotaTitular;

	private DpLotacaoSelecao lotacaoDestinatarioSel;

	ServletActionContext ctx;

	ActionInvocation invocation;

	protected String streamRel = "inputStream";

	private String contentType = "image/pdf";

	private InputStream inputStream;
	
	@Get("app/expediente/rel/relRelatorios")
	public void aRelRelatorios(String nomeArquivoRel) throws AplicacaoException {
		lotacaoDestinatarioSel = new DpLotacaoSelecao();
		result.include("nomeArquivoRel", nomeArquivoRel);
		
		if (nomeArquivoRel.equals("relDocumentosSubordinados.jsp")) {
			fazerResultsParaRelDocumentosSubordinados();
		} else if (nomeArquivoRel.equals("relClassificacao.jsp")) {
			fazerResultsParaRelClassificacao();
		} else if (nomeArquivoRel.equals("relConsultaDocEntreDatas.jsp")) {
			fazerResultsParaRelConsultaDocEntreDatas();
		} else if (nomeArquivoRel.equals("relCrDocSubordinados.jsp")) {
			fazerResultsParaRelCrDocSubordinados();
		}	else if (nomeArquivoRel.equals("relDocsClassificados.jsp")) { 
			fazerResultsParaRelDocClassificados();
		} else if (nomeArquivoRel.equals("relDocumentosSubordinados.jsp")) {
			fazerResultsParaRelDocumentosSubordinados();
		} else if (nomeArquivoRel.equals("relExpedientes.jsp")) {
			fazerResultsParaRelExpedientes();
		} else if (nomeArquivoRel.equals("relFormularios.jsp")) {
			fazerResultsParaRelFormularios();
		} else if (nomeArquivoRel.equals("relModelos.jsp")) {
			fazerResultsParaRelModelos();
		} else if (nomeArquivoRel.equals("relMovCad.jsp")) {
			fazerResultsParaRelMovCad();
		} else if (nomeArquivoRel.equals("relMovimentacao.jsp")) {
			fazerResultsParaRelMovimentacao();
		} else if (nomeArquivoRel.equals("relMovimentacaoDocSubordinados.jsp")) {
			fazerResultsParaRelMovimentacaoDosSubordinados();
		} else if (nomeArquivoRel.equals("relMovInconsistentes.jsp")) {
			fazerResultsParaRelMovInconsistentes();
		} else if (nomeArquivoRel.equals("relMovProcesso.jsp")) {
			fazerResultsParaRelMovProcesso();
		} else if (nomeArquivoRel.equals("relOrgao.jsp")) {
			fazerResultsParaRelOrgao();
		} else if (nomeArquivoRel.equals("relTipoDoc.jsp")) {
			fazerResultsParaRelTipoDoc();
		} else {
			throw new AplicacaoException("Modelo de relatório não definido!");
		}
		
	}

	
	public void fazerResultsParaRelClassificacao() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("mascaraEntrada", this.getMascaraEntrada());
		result.include("mascaraSaida", this.getMascaraSaida());
		result.include("mascaraJavascript", this.getMascaraJavascript());
	}
	
	public void fazerResultsParaRelConsultaDocEntreDatas() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());
	}
	
	public void fazerResultsParaRelCrDocSubordinados() {
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
	}
	
	public void fazerResultsParaRelDocClassificados() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("mascaraEntrada", this.getMascaraEntrada());
		result.include("mascaraSaida", this.getMascaraSaida());
		result.include("mascaraJavascript", this.getMascaraJavascript());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
	}
	
	public void fazerResultsParaRelDocumentosSubordinados() {
		result.include("listaTipoRel", this.getListaTipoRel());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("titular", this.getTitular());
	}
	
	
	public void fazerResultsParaRelExpedientes() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		
	}
	
	public void fazerResultsParaRelFormularios() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("titular", this.getTitular());
		
	}
	
	public void fazerResultsParaRelModelos() {
		result.include("lotaTitular", this.getLotaTitular());
	}
	
	public void fazerResultsParaRelMovCad() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());
	}
	
	public void fazerResultsParaRelMovimentacao() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());
	}
	
	public void fazerResultsParaRelMovimentacaoDosSubordinados() {
			result.include("lotaTitular", this.getLotaTitular());
			result.include("listaTipoRel", this.getListaTipoRel());
			result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
			result.include("listaExTipoFormaDoc", this.getListaExTipoFormaDoc());
			result.include("titular", this.getTitular());
	}

	public void fazerResultsParaRelMovInconsistentes() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
	}
	
	public void fazerResultsParaRelMovProcesso() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());
	}
	
	public void fazerResultsParaRelOrgao() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("orgaosUsu", this.getOrgaosUsu());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());	
	}
	
	public void fazerResultsParaRelTipoDoc() {
		result.include("lotaTitular", this.getLotaTitular());
		result.include("lotacaoDestinatarioSel", this.getLotacaoDestinatarioSel());
		result.include("titular", this.getTitular());
	}
	
	private Map<Integer, String> getListaTipoRel() {
		Map<Integer, String> listaTipoRel = new HashMap<Integer,  String>();
		listaTipoRel.put(1, "Documentos Ativos");
		listaTipoRel.put(2, "Como Gestor");
		listaTipoRel.put(3,"Como Interessado");
		return listaTipoRel;
	}
	
	public Download aRelFormulariosOld() throws Exception {

		final Map<String, String> parameters = new HashMap();
		parameters.put("tipoRelatorio", param("tipoRelatorio"));
		parameters.put("secaoUsuario", param("secaoUsuario"));
		aGeraRelatorio(parameters);
		return new InputStreamDownload(this.getInputStream(), "application/pdf","aRelFormulariosOld");
	}
	
	@Get("app/expediente/rel/emiteRelFormularios")
	public Download aRelFormularios(String secaoUsuario, String orgaoUsuario, String lotacaoTitular, String idTit, String nomeArquivoRel) throws Exception {
			
		
		assertAcesso("FORMS:Relação de formulários");

		Map<String, String> parametros = new HashMap<String, String>(); 
		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("orgaoUsuario", orgaoUsuario);
		parametros.put("lotacaoTitular",lotacaoTitular);
		parametros.put("idTit", idTit);

		RelatorioModelos rel = new RelatorioModelos(parametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelFormularios");
	}

	private class FormulariosListItem {
		public String idForma;

		public String descricaoForma;

		public String idModelo;

		public String nomeModelo;

		public String siglaClassificacao;

		public String siglaClassCriacaoVia;

		FormulariosListItem(ExModelo o) {
			if (null != o.getExFormaDocumento()) {
				this.idForma = o.getExFormaDocumento().getId().toString();
				this.descricaoForma = o.getExFormaDocumento().getDescricao();
			}
			this.idModelo = o.getIdMod().toString();
			this.nomeModelo = o.getNmMod();
			if (null != o.getExClassificacao())
				this.siglaClassificacao = o.getExClassificacao().getSigla();
			if (null != o.getExClassCriacaoVia())
				this.siglaClassCriacaoVia = o.getExClassCriacaoVia().getSigla();
		}
	}

	public void aFormularios() throws Exception {

		assertAcesso("FORMS:Relação de formulários");

		List<ExModelo> l = dao().listarExModelos();

		LinkedList<FormulariosListItem> ll = new LinkedList<FormulariosListItem>();
		for (ExModelo o : l) {
			ll.add(new FormulariosListItem(o));
		}
		setDataSource(ll);
		gerarRelatorio("formularios");
	}

	public void gerarRelatorio(String arquivo) throws JRException, Exception {

		final String cam = (String) getRequest().getRealPath(
				"/WEB-INF/page/exRelatorio/");

		setConnection(JDBCUtilOracle.getConnection());

		/*
		 * Here we compile our xml jasper template to a jasper file. Note: this
		 * isn't exactly considered 'good practice'. You should either use
		 * precompiled jasper files (.jasper) or provide some kind of check to
		 * make sure you're not compiling the file on every request. If you
		 * don't have to compile the report, you just setup your data source
		 * (eg. a List)
		 */
		final String sJRXml = cam + "/" + arquivo + ".xml";
		final String sJasper = cam + "/" + arquivo + ".jasper";

		try {
			JasperCompileManager.compileReportToFile(sJRXml, sJasper);
		} catch (Exception e) {
			throw new Exception("Erro ao criar um relatório", e);
		}
	}

	@Get("app/expediente/rel/emiteRelExpedientes")
	public Download aRelExpedientes(DpLotacaoSelecao lotacaoDestinatarioSel) throws Exception {

		
		this.setLotacaoDestinatarioSel(lotacaoDestinatarioSel);
		
		final String DataInicio = param("dataInicio");
		final String DataFim = param("dataFim");
		
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("tipoRelatorio", param("tipoRelatorio"));
		parameters.put("secaoUsuario", param("secaoUsuario"));
		parameters.put("dataInicio", DataInicio);
		parameters.put("dataFim", DataFim);

		if (this.lotacaoDestinatarioSel.getId() != null) {
			DpLotacao lota = dao()
					.consultar(getLotacaoDestinatarioSel().getId(),
							DpLotacao.class, false);

			if (lota == null)
				throw new AplicacaoException("Lotação inexistente");

			parameters.put("lotacao", lota.getIdLotacao().toString());
			parameters.put("siglalotacao", lota.getSiglaLotacao());

		} else {
			throw new AplicacaoException("Lotação não informada");
		}

		if (DataInicio.equals("") || DataFim.equals(""))
			throw new AplicacaoException("Data não informada");

		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date dataI = formatador.parse(DataInicio);
		java.util.Date dataF = formatador.parse(DataFim);

		if (dataI.after(dataF)) {
			throw new AplicacaoException("Data informada não é válida");
		}
		
		aGeraRelatorio(parameters);
		
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelDocumentosSubordinados");
		
	}

	private void aGeraRelatorio(Map parameters) throws JRException {

		String cam = (String) getContext().getRealPath("/WEB-INF/page/exRelatorio/");
		JasperDesign design = JRXmlLoader.load(cam + "/"+ (String) parameters.get("tipoRelatorio"));
		JasperReport jr = JasperCompileManager.compileReport(design);
		JasperPrint relGerado = JasperFillManager.fillReport(jr, parameters, connection);
		this.setInputStream(new ByteArrayInputStream( JasperExportManager.exportReportToPdf(relGerado)));

	}

	public boolean isMostrarRel() {
		return mostrarRel;
	}

	public void setMostrarRel(boolean mostrarRel) {
		this.mostrarRel = mostrarRel;
	}
	
	public Integer getIdLotaTitular() {
		return idLotaTitular;
	}

	public void setIdLotaTitular(Integer idLotaTitular) {
		this.idLotaTitular = idLotaTitular;
	}

	public DpLotacaoSelecao getLotacaoDestinatarioSel() {
		return lotacaoDestinatarioSel;
	}

	public void setLotacaoDestinatarioSel(
			DpLotacaoSelecao lotacaoDestinatarioSel) {
		this.lotacaoDestinatarioSel = lotacaoDestinatarioSel;
	}

	public String getStreamRel() {
		return streamRel;
	}

	public void setStreamRel(String streamRel) {
		this.streamRel = streamRel;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public List getDataSource() {
		return dataSource;
	}

	public void setDataSource(List dataSource) {
		this.dataSource = dataSource;
	}

	@Get("app/expediente/rel/emiteRelModelos")
	public Download aRelModelos() throws Exception {
		RelatorioModelos rm = new RelatorioModelos(null);
		rm.gerar();
		this.setInputStream(new ByteArrayInputStream(rm.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelDocumentosSubordinados");
	}
	

	@Get("app/expediente/rel/emiteRelDocumentosSubordinados")
	public Download aRelDocumentosSubordinados() throws Exception {
		
		assertAcesso("SUBORD:Relatório de documentos em setores subordinados");

		Map<String, String> parametros = new HashMap<String, String>();

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

		RelatorioDocumentosSubordinados rel = new RelatorioDocumentosSubordinados(
				parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelDocumentosSubordinados");
	}

	@Get("app/expediente/rel/emiteRelMovDocsSubordinados")
	public Download aRelMovDocumentosSubordinados() throws Exception {

		assertAcesso("MVSUB:Relatório de movimentação de documentos em setores subordinados");

		Map<String, String> parametros = new HashMap<String, String>();

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
				+ "/expediente/doc/exibir.action?sigla=");

		RelMovimentacaoDocSubordinados rel = new RelMovimentacaoDocSubordinados(
				parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelMovDocsSubordinados");
	}
	
	@Get("app/expediente/rel/emiteRelDocsSubCriados")
	public Download aRelDocsSubCriados() throws Exception {

		assertAcesso("CRSUB:Relatório de criação de documentos em setores subordinados");

		Map<String, String> parametros = new HashMap<String, String>();

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
				+ "/expediente/doc/exibir.action?sigla=");

		RelDocSubordinadosCriados rel = new RelDocSubordinadosCriados(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelDocsSubCriados");
	}


	public List<ExTipoFormaDoc> getListaExTipoFormaDoc() {
		List<ExTipoFormaDoc> listaQry = (List<ExTipoFormaDoc>) HibernateUtil
				.getSessao().createQuery("from ExTipoFormaDoc").list();
		List<ExTipoFormaDoc> resultado = new LinkedList<ExTipoFormaDoc>();
		ExTipoFormaDoc todos = new ExTipoFormaDoc();
		todos.setDescTipoFormaDoc("(Todos)");
		resultado.add(todos);
		resultado.addAll(listaQry);
		return resultado;
	}

	@Get("app/expediente/rel/emiteRelDocEntreDatas")
	public Download aRelDocEntreDatas() throws Exception {

		assertAcesso("DATAS:Relação de documentos entre datas");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/expediente/doc/exibir.action?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		RelConsultaDocEntreDatas rel = new RelConsultaDocEntreDatas(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelDocEntreDatas");
	
	}
	
	@Get("app/expediente/rel/emiteRelMovimentacao")
	public Download aRelMovimentacao() throws Exception {

		assertAcesso("DATAS:Relação de documentos entre datas");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/expediente/doc/exibir.action?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		RelMovimentacao rel = new RelMovimentacao(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelMovimentacao");
	}
	
	@Get("app/expediente/rel/emiteRelMovCad")
	public Download aRelMovCad() throws Exception {

		assertAcesso("MOVCAD:Relação de movimentações por cadastrante");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/expediente/doc/exibir.action?sigla=");

		parametros.put("orgaoUsuario", getRequest()
				.getParameter("orgaoUsuario"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		RelMovCad rel = new RelMovCad(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelMovCad");
	}
	
	@Get("app/expediente/rel/emiteRelOrgao")
	public Download aRelOrgao() throws Exception {

		assertAcesso("DATAS:Relação de documentos entre datas");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao", getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest().getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/expediente/doc/exibir.action?sigla=");
		
		if (!getRequest().getParameter("orgaoUsu").isEmpty())
			parametros.put("orgao", getRequest().getParameter("orgaoUsu"));
		parametros.put("lotacaoTitular", getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		RelOrgao rel = new RelOrgao(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelOrgao");
	}
	
	@Get("app/expediente/rel/emiteRelTipoDoc")
	public Download aRelTipoDoc() throws Exception {

		assertAcesso("DATAS:Relação de documentos entre datas");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("lotacao",
				getRequest().getParameter("lotacaoDestinatarioSel.id"));
		parametros.put("secaoUsuario", getRequest()
				.getParameter("secaoUsuario"));
		parametros.put("dataInicial", getRequest().getParameter("dataInicial"));
		parametros.put("dataFinal", getRequest().getParameter("dataFinal"));
		parametros.put("link_siga", "http://" + getRequest().getServerName()
				+ ":" + getRequest().getServerPort()
				+ getRequest().getContextPath()
				+ "/expediente/doc/exibir.action?sigla=");

		//parametros.put("orgao", getRequest()
		//	.getParameter("orgaoUsu"));
		parametros.put("lotacaoTitular",
				getRequest().getParameter("lotacaoTitular"));
		parametros.put("idTit", getRequest().getParameter("idTit"));

		RelTipoDoc rel = new RelTipoDoc(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelTipoDoc");
	}
	
	@Get("app/expediente/rel/emiteRelMovProcesso")
	public Download aRelMovProcesso() throws Exception {

		assertAcesso("RELMVP:Relação de movimentações de processos");

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtIni = df.parse(getRequest().getParameter("dataInicial"));
		Date dtFim = df.parse(getRequest().getParameter("dataFinal"));
		if (dtFim.getTime() - dtIni.getTime() > 31536000000L)
			throw new Exception(
					"O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");

		Map<String, String> parametros = new HashMap<String, String>();

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
				+ "/expediente/doc/exibir.action?sigla=");

		RelMovProcesso rel = new RelMovProcesso(parametros);

		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelMovProcesso");	
	}
	
	@Get("app/expediente/rel/aRelClassificacao")
	public Download aRelClassificacao() throws AplicacaoException, Exception {
		assertAcesso("CLSD:Classificação Documental;CLASS:Relação de classificações");

		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("codificacao", getRequest().getParameter("codificacao"));
		parametros.put("secaoUsuario",getRequest().getParameter("secaoUsuario"));

		RelClassificacao rel = new RelClassificacao(parametros);
		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","aRelClassificacao");
	}
	
	@Get("app/expediente/rel/emiteRelClassDocDocumentos")
	public Download aRelClassDocDocumentos() throws AplicacaoException, Exception {
		assertAcesso("CLSD:Classificação Documental;DOCS:Relação de documentos classificados");

		Map<String, String> parametros = new HashMap<String, String>();
		String codificacao = 	getRequest().getParameter("codificacao");
		String idLotacao = getRequest().getParameter("lotacaoDestinatarioSel.id");
		String idOrgaoUsu = getRequest().getParameter("orgaoUsuario");
		String secaoUsuario = getRequest().getParameter("secaoUsuario");

		if ((codificacao==null || codificacao.length()==0)&& (idLotacao == null || idLotacao.length()==0)){
			throw new AplicacaoException("Especifique pelo menos um dos parâmetros!");
		}

		parametros.put("codificacao", codificacao);
		parametros.put("idLotacao", idLotacao);
		parametros.put("idOrgaoUsu", idOrgaoUsu);	
		parametros.put("secaoUsuario", secaoUsuario);

		RelDocsClassificados rel = new RelDocsClassificados(parametros);
		rel.gerar();

		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(this.getInputStream(), "application/pdf","emiteRelClassDocDocumentos");
	}

	public void assertAcesso(String pathServico) throws AplicacaoException,
	Exception {
		super.assertAcesso("REL:Gerar relatórios;" + pathServico);
	}

	public String getMascaraEntrada(){
		return MascaraUtil.getInstance().getMascaraEntrada();
	}

	public String getMascaraSaida(){
		return MascaraUtil.getInstance().getMascaraSaida();
	}

	public String getMascaraJavascript(){
		return SigaExProperties.getExClassificacaoMascaraJavascript();
	}

}
