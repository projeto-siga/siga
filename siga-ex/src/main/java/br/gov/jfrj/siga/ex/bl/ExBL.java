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
package br.gov.jfrj.siga.ex.bl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.logging.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.base.util.SetUtils;
import br.gov.jfrj.siga.cd.service.CdService;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExBoletimDoc;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEditalEliminacao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTermoEliminacao;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.ext.AbstractConversorHTMLFactory;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.ex.util.Notificador;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.ex.util.ProcessadorModelo;
import br.gov.jfrj.siga.ex.util.ProcessadorModeloFreemarker;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.model.ObjetoBase;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.service.WfService;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.axis.encoding.Base64;

public class ExBL extends CpBL {
	private final String SHA1 = "1.3.14.3.2.26";

	private final boolean BUSCAR_CARIMBO_DE_TEMPO = false;
	private final boolean VALIDAR_LCR = false;

	private final ThreadLocal<SortedSet<ExMobil>> threadAlteracaoParcial = new ThreadLocal<SortedSet<ExMobil>>();

	private ProcessadorModelo processadorModeloJsp;
	private ProcessadorModelo processadorModeloFreemarker = new ProcessadorModeloFreemarker();

	private final static Logger log = Logger.getLogger(ExBL.class);

	private Set<ExMovimentacao> transferenciasSet = new TreeSet<ExMovimentacao>();
		
	public ThreadLocal<SortedSet<ExMobil>> getThreadAlteracaoParcial() {
		return threadAlteracaoParcial;
	}

	public ExCompetenciaBL getComp() {
		return (ExCompetenciaBL) super.getComp();
	}

	public ExConfiguracaoBL getConf() {
		return (ExConfiguracaoBL) super.getComp().getConfiguracaoBL();
	}

	public ProcessadorModelo getProcessadorModeloJsp() {
		return processadorModeloJsp;
	}

	public void setProcessadorModeloJsp(ProcessadorModelo processadorModelo) {
		this.processadorModeloJsp = processadorModelo;
	}

	public String fixTableCols(String html) throws Exception {
		StringReader st = new StringReader(html);
		SAXBuilder sb = new SAXBuilder();
		sb.setIgnoringElementContentWhitespace(true);
		Document doc = sb.build(st);
		Element raiz = doc.getRootElement();
		fixTableCols(raiz);
		return (new XMLOutputter()).outputString(doc);
	}

	private void fixTableCols(Element raiz) {
		List<Element> filhos = raiz.getChildren("table");
		for (Element table : filhos) {

			if (table.getAttribute("width") == null)
				table.setAttribute("width", "50%");

			if (table.getAttribute("border") == null
					&& !(table.getAttribute("style") != null && table
							.getAttribute("style").getValue()
							.contains("border")))
				table.setAttribute("border", "1");

			Element tr = null;
			Element tableBody = table.getChild("tbody");
			if (tableBody == null)
				tableBody = table.getChild("thead");
			if (tableBody == null)
				tr = (Element) table.getChildren("tr").get(0);
			else
				tr = (Element) tableBody.getChildren("tr").get(0);

			List<Element> tds = tr.getChildren("td");
			int cols = 0;
			for (Element td : tds) {
				Attribute colspan = td.getAttribute("colspan");
				if (colspan != null)
					cols += (new Integer(colspan.getValue()));
				else
					cols++;
				fixTableCols(td);
			}

			Element colgroup = new Element("colgroup");
			for (int i = 0; i < cols; i++) {
				Element col = new Element("col");
				col.setAttribute("width", 100 / cols + "%");
				colgroup.addContent(col);
			}
			table.addContent(0, colgroup);
		}
	}

	// Executa algoritmo de comparação entre dois sets e
	// preenche as listas: inserir, excluir e atualizar.
	// \
	private void encaixar(SortedSet<ExMarca> setA, SortedSet<ExMarca> setB,
			Set<ExMarca> incluir, Set<ExMarca> excluir,
			Set<Par<ExMarca, ExMarca>> atualizar) {
		Iterator<ExMarca> ia = setA.iterator();
		Iterator<ExMarca> ib = setB.iterator();

		ExMarca a = null;
		ExMarca b = null;

		if (ia.hasNext())
			a = ia.next();
		if (ib.hasNext())
			b = ib.next();
		while (a != null || b != null) {
			if ((a == null) || (b != null && a.compareTo(b) > 0)) {
				// Existe em setB, mas nao existe em setA
				incluir.add(b);
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
			} else if (b == null || (a != null && b.compareTo(a) > 0)) {
				// Existe em setA, mas nao existe em setB
				excluir.add(a);
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			} else {
				// O registro existe nos dois
				atualizar.add(new Par<ExMarca, ExMarca>(a, b));
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			}
		}
		ib = null;
		ia = null;
	}

	public void atualizarMarcas(ExDocumento doc) {
		atualizarMarcas(doc, false);
	}

	public void atualizarMarcasTemporalidade(ExDocumento doc) {
		atualizarMarcas(doc, true);
	}

	public void atualizarMarcas(ExDocumento doc, boolean apenasTemporalidade) {
		for (ExMobil mob : doc.getExMobilSet()) {
			SortedSet<ExMarca> setA = null, setB = null;

			if (apenasTemporalidade) {
				setA = mob.getExMarcaSetTemporalidade();
				setB = calcularMarcadoresTemporalidade(mob);
			} else {
				setA = mob.getExMarcaSet();
				setB = calcularMarcadores(mob);
			}

			if (setA == null)
				setA = new TreeSet<ExMarca>();

			Set<ExMarca> incluir = new TreeSet<ExMarca>();
			Set<ExMarca> excluir = new TreeSet<ExMarca>();
			Set<Par<ExMarca, ExMarca>> atualizar = new TreeSet<Par<ExMarca, ExMarca>>();
			encaixar(setA, setB, incluir, excluir, atualizar);
			for (ExMarca i : incluir) {
				if (i.getExMobil().getExMarcaSet() == null) {
					i.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
				}
				i.getExMobil().getExMarcaSet().add(i);
				dao().gravar(i);
			}
			for (ExMarca e : excluir) {
				if (e.getExMobil().getExMarcaSet() == null) {
					e.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
				}
				e.getExMobil().getExMarcaSet().remove(e);
				dao().excluir(e);
			}
			for (Entry<ExMarca, ExMarca> pair : atualizar) {
				ExMarca a = pair.getKey();
				ExMarca b = pair.getValue();
				if (a.getExMobil().getExMarcaSet() == null) {
					a.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
				}
				a.setDpLotacaoIni(b.getDpLotacaoIni());
				a.setDpPessoaIni(b.getDpPessoaIni());
				a.setDtFimMarca(b.getDtFimMarca());
				a.setDtIniMarca(b.getDtIniMarca());
				dao().gravar(a);
			}
		}
	}

	private void acrescentarMarca(SortedSet<ExMarca> set, ExMobil mob,
			Long idMarcador, Date dt, DpPessoa pess, DpLotacao lota) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(dao().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dt);
		set.add(mar);
	}

	private void acrescentarMarcaTransferencia(SortedSet<ExMarca> set, ExMobil mob,
			Long idMarcador, Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(dao().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	public static void main(String args[]) throws Exception {
		CpAmbienteEnumBL ambiente = CpAmbienteEnumBL.PRODUCAO;
		Configuration cfg = ExDao.criarHibernateCfg(ambiente);
		HibernateUtil.configurarHibernate(cfg);
		Ex.getInstance().getBL()
				.corrigirArquivamentosEmVolume(300000, 400000, false);
	}

	public void corrigirArquivamentosEmVolume(int primeiro, int ultimo,
			boolean efetivar) {
		Long ini = System.currentTimeMillis();
		List<ExDocumento> list = new ArrayList<ExDocumento>();

		Query query = dao().getSessao().createQuery(
				"select distinct(doc.idDoc) from ExMarca mar "
						+ "inner join mar.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where mar.cpMarcador.idMarcador = 6"
						+ "and mob.exTipoMobil.idTipoMobil = 4 "
						+ (primeiro > 0 ? "and doc.idDoc > " + primeiro : "")
						+ (ultimo > 0 ? "and doc.idDoc < " + ultimo : "")
						+ " order by doc.idDoc");

		int index = 0;

		List<Long> ids = query.list();

		for (Long id : ids) {
			index++;
			try {
				ExDocumento doc = dao().consultar(id, ExDocumento.class, false);
				ExMovimentacao mov = doc.getUltimoVolume()
						.getUltimaMovimentacaoNaoCancelada();
				DpPessoa pess = mov.getResp();
				DpLotacao lota = mov.getLotaResp();

				System.out.println();
				System.out.println(doc.getCodigo() + " (" + doc.getIdDoc()
						+ ")");

				if (!Ex.getInstance().getComp()
						.podeArquivarCorrente(pess, lota, doc.getMobilGeral()))
					System.out.println("NAO PODE");
				else if (efetivar)
					Ex.getInstance()
							.getBL()
							.arquivarCorrente(pess, lota, doc.getMobilGeral(),
									mov.getDtIniMov(), null, pess, false);
				// if (index % 10 == 0){
				dao().getSessao().clear();
				//System.gc();
				// }
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		System.out.println(index + " itens; "
				+ (System.currentTimeMillis() - ini) + " ms");
	}

	public void marcar(ExDocumento doc) throws Exception {
		ExDao.iniciarTransacao();
		atualizarMarcas(doc);
		for (ExMovimentacao m : doc.getExMovimentacaoSet()) {
			m.setNumPaginas(m.getContarNumeroDePaginas());
			dao().gravar(m);
		}
		ExDao.commitTransacao();
	}

	/**
	 * Método criado para contar o número de páginas de um documento que foi
	 * criado antes da função que grava um documento com o total de páginas.
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Integer ContarNumeroDePaginas(ExDocumento doc) {
		ExDao.iniciarTransacao();
		Integer numeroDePaginas = doc.getContarNumeroDePaginas();
		doc.setNumPaginas(numeroDePaginas);
		dao().gravar(doc);
		try {
			ExDao.commitTransacao();
		} catch (Throwable e) {
			System.out
					.println("Erro ao contar o número de páginas do documento."
							+ doc);
			e.printStackTrace();
		}

		return numeroDePaginas;
	}

	/**
	 * Método criado para contar o número de páginas de uma movimentacao que foi
	 * criada antes da função que grava uma movimentacao com o total de páginas.
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Integer ContarNumeroDePaginas(ExMovimentacao mov) {
		ExDao.iniciarTransacao();
		Integer numeroDePaginas = mov.getContarNumeroDePaginas();
		mov.setNumPaginas(numeroDePaginas);
		dao().gravar(mov);
		try {
			ExDao.commitTransacao();
		} catch (Throwable e) {
			System.out
					.println("Erro ao contar o número de páginas da movimentação."
							+ mov);
			e.printStackTrace();
		}

		return numeroDePaginas;
	}

	public void numerarTudo(int aPartirDe) throws Exception {
		List<ExDocumento> list = new ArrayList<ExDocumento>();

		final Criteria countCrit = dao().getSessao()
				.createCriteria(ExDocumento.class)
				.add(Restrictions.gt("idDoc", new Long(aPartirDe)));
		countCrit.setProjection(Projections.rowCount());
		Integer totalDocs = ((Long) countCrit.uniqueResult()).intValue();

		final Criteria crit = dao().getSessao()
				.createCriteria(ExDocumento.class)
				.add(Restrictions.gt("idDoc", new Long(aPartirDe)));
		crit.setMaxResults(60);
		crit.addOrder(Order.asc("idDoc"));

		int index = 0;

		do {
			long inicio = System.currentTimeMillis();
			//System.gc();
			iniciarAlteracao();
			crit.setFirstResult(index);
			list = crit.list();
			for (ExDocumento doc : list) {
				index++;
				try {
					for (ExMovimentacao m : doc.getExMovimentacaoSet()) {
						m.setNumPaginas(m.getContarNumeroDePaginas());
						dao().gravar(m);
					}
				} catch (Throwable e) {
					System.out.println("Erro ao marcar o doc " + doc);
					e.printStackTrace();
				}
				if (index % 50 == 0){
//					System.gc();
				}
				System.out.print(doc.getIdDoc() + " ok - ");
			}
			ExDao.commitTransacao();
			dao().getSessao().clear();
			long duracao = System.currentTimeMillis() - inicio;
			System.out.println();
			System.out.println(new SimpleDateFormat("HH:mm:ss")
					.format(new Date())
					+ " "
					+ String.valueOf(index)
					+ " numerados de " + totalDocs);
		} while (list.size() > 0);

		//System.gc();
	}

	public void marcarTudo() throws Exception {
		marcarTudo(0, 0, true, false, new PrintWriter(System.out));
	}

	public void marcarTudoTemporalidade(int aPartirDe) throws Exception {
		marcarTudo(aPartirDe, 0, true, true, new PrintWriter(System.out));
	}

	public void marcarTudo(int primeiro, int ultimo, boolean efetivar,
			boolean apenasTemporalidade, PrintWriter out) throws Exception {

		List<ExDocumento> list = new ArrayList<ExDocumento>();

		final Criteria countCrit = dao().getSessao()
				.createCriteria(ExDocumento.class)
				.add(Restrictions.ge("idDoc", new Long(primeiro)));
		if (ultimo != 0)
			countCrit.add(Restrictions.le("idDoc", new Long(ultimo)));
		countCrit.setProjection(Projections.rowCount());

		final Criteria crit = dao().getSessao()
				.createCriteria(ExDocumento.class)
				.add(Restrictions.ge("idDoc", new Long(primeiro)));
		if (ultimo != 0)
			crit.add(Restrictions.le("idDoc", new Long(ultimo)));
		crit.setMaxResults(5);
		crit.addOrder(Order.asc("idDoc"));

		out.println("-----------------------------------------------");
		out.print(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		out.print(" - Remarcando documentos.");
		out.print(" Primeiro: ");
		out.println(primeiro);
		if (ultimo != 0) {
			out.print(" Ultimo: ");
			out.println(ultimo);
		}
		if (efetivar)
			out.println("***EFETIVAR!!***");
		out.println("-----------------------------------------------");

		int index = 0;

		do {
			long inicio = System.currentTimeMillis();
			if (efetivar)
				iniciarAlteracao();
			crit.setFirstResult(index);
			list = crit.list();
			for (ExDocumento doc : list) {
				index++;
				StringBuilder msg = new StringBuilder();
				try {

					StringBuilder marcasAnteriores = new StringBuilder();
					for (ExMobil mob : doc.getExMobilSet()) {
						marcasAnteriores.append(mob.isGeral() ? "0" : mob
								.getNumSequencia());
						marcasAnteriores.append(" - ");
						marcasAnteriores
								.append(mob
										.getMarcadoresDescrCompleta(apenasTemporalidade));
					}
					atualizarMarcasTemporalidade(doc);
					StringBuilder marcasPosteriores = new StringBuilder();
					for (ExMobil mob : doc.getExMobilSet()) {
						marcasPosteriores.append(mob.isGeral() ? "0" : mob
								.getNumSequencia());
						marcasPosteriores.append(" - ");
						marcasPosteriores
								.append(mob
										.getMarcadoresDescrCompleta(apenasTemporalidade));
					}

					if (!marcasAnteriores.toString().equals(
							marcasPosteriores.toString())) {
						msg.append("Marcas:");
						msg.append("\n\tAntes: ");
						msg.append(marcasAnteriores);
						msg.append("\n\tDepois: ");
						msg.append(marcasPosteriores);
					}

				} catch (Throwable e) {
					msg.append("ERRO: ");
					msg.append(e.getMessage());
					e.printStackTrace(out);
				}

				if (msg.length() > 0) {
					msg.insert(0, "\n");
					msg.insert(0, doc.getCodigo());
					msg.insert(0, " - ");
					msg.insert(0,
							new SimpleDateFormat("HH:mm:ss").format(new Date()));
					msg.insert(0, "\n");
					out.println(msg);
				}

			}
			if (efetivar) {
				ExDao.commitTransacao();
				//System.gc();
			}
			dao().getSessao().clear();
		} while (list.size() > 0);

		out.println("\n-----------------------------------------------");
		out.print(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		out.println(" - Fim");
		out.println("-----------------------------------------------");

		//System.gc();
	}

	/**
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que
	 * foram feitas no documento.
	 * 
	 * @param mob
	 */
	private SortedSet<ExMarca> calcularMarcadores(ExMobil mob) {
		SortedSet<ExMarca> set = calcularMarcadoresTemporalidade(mob);

		ExMovimentacao ultMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();

		Boolean isDocumentoSemEfeito = mob.doc().isSemEfeito();

		if (mob.isGeral()) {
			if (!mob.doc().isFinalizado()) {
				acrescentarMarca(set, mob, CpMarcador.MARCADOR_EM_ELABORACAO,
						mob.doc().getDtRegDoc(), mob.doc().getCadastrante(),
						mob.doc().getLotaCadastrante());
				if (mob.getExDocumento().getSubscritor() != null)
					acrescentarMarca(set, mob, CpMarcador.MARCADOR_REVISAR, mob
							.doc().getDtRegDoc(), mob.getExDocumento()
							.getSubscritor(), null);

			}

			if (mob.getExMovimentacaoSet() != null) {
				if (isDocumentoSemEfeito) {
					for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
						if (mov.isCancelada())
							continue;

						Long t = mov.getIdTpMov();
						if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO) {
							acrescentarMarca(set, mob,
									CpMarcador.MARCADOR_SEM_EFEITO,
									mov.getDtIniMov(), mov.getCadastrante(),
									mov.getLotaCadastrante());
						}
					}

				} else {

					Long mDje = null;
					ExMovimentacao movDje = null;					
					for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
						if (mov.isCancelada())
							continue;
						Long m = null;
						Long t = mov.getIdTpMov();
						if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO) {
							acrescentarMarca(set, mob, CpMarcador.MARCADOR_PENDENTE_DE_ANEXACAO,
									mov.getDtIniMov(),
									mov.getCadastrante(),
									mov.getLotaCadastrante());
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
							DpLotacao lotaPerfil = null;
							switch ((int) (long) mov.getExPapel().getIdPapel()) {
							case (int) ExPapel.PAPEL_GESTOR:
								m = CpMarcador.MARCADOR_COMO_GESTOR;
								break;
							case (int) ExPapel.PAPEL_INTERESSADO:
								m = CpMarcador.MARCADOR_COMO_INTERESSADO;
								break;
							}
							if (m != null && !mob.doc().isEliminado()
									&& !mob.doc().isArquivadoPermanente()) {								
								if (mov.getSubscritor() != null) /* perfil foi cadastrado para a pessoa */
									lotaPerfil = null;
								else
									lotaPerfil = mov.getLotaSubscritor();
								acrescentarMarca(set, mob, m,
										mov.getDtIniMov(), mov.getSubscritor(), lotaPerfil);
							}
								
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO) {
							mDje = CpMarcador.MARCADOR_PUBLICACAO_SOLICITADA;
							movDje = mov;
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO) {
							mDje = CpMarcador.MARCADOR_REMETIDO_PARA_PUBLICACAO;
							movDje = mov;
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO) {
							mDje = CpMarcador.MARCADOR_DISPONIBILIZADO;
							movDje = mov;
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO
								&& mob.doc().isEletronico()
								&& !mob.doc().jaTransferido()) {
							m = CpMarcador.MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA;
							/*
							 * não é possível usar ExMovimentacao.isAssinada()
							 * pois não há tempo habil no BD de efetivar a
							 * inclusao de movimentacao de assinatura de
							 * movimentção Edson: Por que não?
							 */
							for (ExMovimentacao movAss : mob
									.getExMovimentacaoSet()) {
								if ((movAss.getExTipoMovimentacao()
										.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO || movAss
										.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO || movAss
										.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA || movAss
										.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
										&& movAss.getExMovimentacaoRef()
												.getIdMov() == mov.getIdMov()) {
									m = null;
									break;
								}
							}
							if (m != null)
								acrescentarMarca(set, mob, m,
										mov.getDtIniMov(),
										mov.getCadastrante(),
										mov.getLotaCadastrante());
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO) {
							if (mob.getDoc().isEletronico()) {
								if (!mob.getDoc().isAssinado())
									m = CpMarcador.MARCADOR_REVISAR;
								else {
									if (mob.getDoc().isAssinadoSubscritor())
										m = CpMarcador.MARCADOR_COMO_SUBSCRITOR;
									else
										m = CpMarcador.MARCADOR_REVISAR;
									for (ExMovimentacao assinatura : mob
											.getDoc().getTodasAsAssinaturas()) {
										if (assinatura.getSubscritor()
												.equivale(mov.getSubscritor())) {
											m = null;
											break;
										}
									}
								}
								if (m != null)
									acrescentarMarca(set, mob, m,
											mov.getDtIniMov(),
											mov.getSubscritor(), null);
							}
						} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA) {
							boolean jaAutenticado = false;
							for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
								if (movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO || 
										movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO) {
									jaAutenticado = true;
									break;
								}
							}	
							
							if(!jaAutenticado)
								acrescentarMarca(set, mob, CpMarcador.MARCADOR_DOCUMENTO_ASSINADO_COM_SENHA, mov.getDtIniMov(), mov.getSubscritor(), null);
						}
					}
					if (mDje != null && !mob.doc().isEliminado()) {
						acrescentarMarca(set, mob, mDje, movDje.getDtIniMov(),
								movDje.getTitular(), movDje.getLotaTitular());
					}
				}
			}
			return set;
		} else if (ultMovNaoCanc == null) {
			ExMovimentacao ultMov = mob.getUltimaMovimentacao();
			Date dt = null;
			if (ultMov != null) {
				dt = ultMov.getDtIniMov();
			}
			acrescentarMarca(set, mob, CpMarcador.MARCADOR_CANCELADO, dt, mob
					.doc().getCadastrante(), mob.doc().getLotaCadastrante());
			return set;
		}

		if (!isDocumentoSemEfeito) {

			boolean apensadoAVolumeDoMesmoProcesso = mob
					.isApensadoAVolumeDoMesmoProcesso();

			long m = CpMarcador.MARCADOR_CANCELADO;
			long m_aDevolverFora = CpMarcador.MARCADOR_A_DEVOLVER_FORA_DO_PRAZO;
			long m_aDevolver = CpMarcador.MARCADOR_A_DEVOLVER;
			long m_aguardando = CpMarcador.MARCADOR_AGUARDANDO;
			long m_aguardandoFora = CpMarcador.MARCADOR_AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO;
			long mAnterior = m;
			Date dt = null;
			//ExMovimentacao movT = new ExMovimentacao();		
			Set<ExMovimentacao> movT = new TreeSet<ExMovimentacao>();
			//contemDataRetorno = false;
			
			for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
				if (mov.isCancelada())
					continue;
				Long t = mov.getIdTpMov();
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO) 
					m = CpMarcador.MARCADOR_PENDENTE_DE_ANEXACAO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO)
					m = CpMarcador.MARCADOR_PUBLICACAO_SOLICITADA;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO)
					m = CpMarcador.MARCADOR_DISPONIBILIZADO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
					m = CpMarcador.MARCADOR_REMETIDO_PARA_PUBLICACAO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR)
					m = CpMarcador.MARCADOR_SOBRESTADO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
					m = CpMarcador.MARCADOR_JUNTADO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO)
					m = CpMarcador.MARCADOR_JUNTADO_EXTERNO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO
						&& apensadoAVolumeDoMesmoProcesso)
					m = CpMarcador.MARCADOR_APENSADO;
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA){
					m = CpMarcador.MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO;
					transferenciasSet.add(mov);
					if(mov.getDtFimMov()!=null){
						movT.add(mov);
					}
				}
				if ((t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA || t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA)
						&& !apensadoAVolumeDoMesmoProcesso){
					m = CpMarcador.MARCADOR_CAIXA_DE_ENTRADA;
					transferenciasSet.add(mov);
					if(mov.getDtFimMov()!=null){
						movT.add(mov);
					}
				}
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
						&& mob.doc().isEletronico()) {
					m = CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA;
					for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
						if ((movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO ||
								movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA)
								&& movAss.getExMovimentacaoRef().getIdMov() == mov
										.getIdMov()) {
							m = mAnterior;
						}
					}
				}

				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA
						|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO)
					if (mob.doc().isAssinado()
							|| mob.doc().getExTipoDocumento().getIdTpDoc() == 2
							|| mob.doc().getExTipoDocumento().getIdTpDoc() == 3) {

						if (!apensadoAVolumeDoMesmoProcesso) {
							m = CpMarcador.MARCADOR_EM_ANDAMENTO;
						}
						else
							m = CpMarcador.MARCADOR_APENSADO;

					} else if (mob.isApensado()) {
						m = CpMarcador.MARCADOR_APENSADO;
					} else {
						m = CpMarcador.MARCADOR_PENDENTE_DE_ASSINATURA;
					}

				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO
						&& mob.doc().isEletronico()) {
					m = CpMarcador.MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA;
					/*
					 * não é possível usar ExMovimentacao.isAssinada() pois não
					 * há tempo habil no BD de efetivar a inclusao de
					 * movimentacao de assinatura de movimentção
					 */
					for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
						if ((movAss.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO || movAss
								.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO || movAss
								.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA || movAss
								.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
								&& movAss.getExMovimentacaoRef().getIdMov() == mov
										.getIdMov()) {
							m = mAnterior;
							break;
						}
					}
				}
				
				if(t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA) {
					acrescentarMarca(set, mob, CpMarcador.MARCADOR_MOVIMENTACAO_ASSINADA_COM_SENHA, dt, mov.getSubscritor(), null);
				}

				if(t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA) {
						acrescentarMarca(set, mob, CpMarcador.MARCADOR_MOVIMENTACAO_CONFERIDA_COM_SENHA, dt, mov.getSubscritor(), null);
				}
				
				if (m != mAnterior) {
					dt = mov.getDtIniMov();
					mAnterior = m;
				}
			}
						
			Iterator itr = movT.iterator();

			while (itr.hasNext()) {
				ExMovimentacao element = (ExMovimentacao) itr.next();

				if (element.getLotaCadastrante() != null
						&& !transferenciasSet.isEmpty()
						&& !contemRetornoTransferencia(element)) {
					
					Date dtMarca = element.getDtFimMov();
					dtMarca.setHours(23);
					dtMarca.setMinutes(59);
					dtMarca.setSeconds(59);
					
					acrescentarMarcaTransferencia(set, mob, m_aguardando, dt,
							dtMarca, element.getCadastrante(),
							element.getLotaCadastrante()); // acrescenta a
															// marca
															// "Aguardando Devolução"

					acrescentarMarcaTransferencia(set, mob, m_aDevolver, dt,
							dtMarca, element.getResp(),
							element.getLotaResp());// acrescenta a marca
													// "A Devolver"

					acrescentarMarcaTransferencia(set, mob, m_aguardandoFora,
							dtMarca, null,
							element.getCadastrante(),
							element.getLotaCadastrante()); // acrescenta a
															// marca
															// "Aguardando Devolução (Fora do Prazo)"
					
					acrescentarMarcaTransferencia(set, mob, m_aDevolverFora,
							dtMarca, null, element.getResp(),
							element.getLotaResp());// acrescenta
													// a
													// marca
													// "A Devolver (Fora do Prazo)"
				}
			}
			
			if (m == CpMarcador.MARCADOR_PENDENTE_DE_ASSINATURA) {
				if (!mob.getDoc().isAssinadoSubscritor())
					acrescentarMarca(set, mob,
							CpMarcador.MARCADOR_COMO_SUBSCRITOR, dt, mob
									.getExDocumento().getSubscritor(), null);
			}

			if (m == CpMarcador.MARCADOR_CAIXA_DE_ENTRADA) {
				if (!mob.doc().isEletronico()) {
					m = CpMarcador.MARCADOR_A_RECEBER;
					acrescentarMarca(set, mob, CpMarcador.MARCADOR_EM_TRANSITO,
							dt, ultMovNaoCanc.getCadastrante(),
							ultMovNaoCanc.getLotaCadastrante());
				} else {
					if (ultMovNaoCanc.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA) {
						m = CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA;
					} else {
						acrescentarMarca(set, mob,
								CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO, dt,
								ultMovNaoCanc.getCadastrante(),
								ultMovNaoCanc.getLotaCadastrante());
					}
				}
			}

			if (m == CpMarcador.MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO) {
				acrescentarMarca(set, mob, m, dt,
						ultMovNaoCanc.getCadastrante(),
						ultMovNaoCanc.getLotaCadastrante());
			} else if (m == CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA) {
				if (ultMovNaoCanc.getCadastrante().getId() != ultMovNaoCanc
						.getSubscritor().getId()) {
					if (ultMovNaoCanc.getLotaCadastrante().getIdLotacao() != ultMovNaoCanc
							.getLotaSubscritor().getIdLotacao()) {
						acrescentarMarca(set, mob, m, dt,
								ultMovNaoCanc.getSubscritor(),
								ultMovNaoCanc.getLotaSubscritor());
					} else {
						acrescentarMarca(set, mob, m, dt,
								ultMovNaoCanc.getSubscritor(), null);
					}
				}
				acrescentarMarca(set, mob, m, dt,
						ultMovNaoCanc.getCadastrante(),
						ultMovNaoCanc.getLotaCadastrante());
			} else if (m == CpMarcador.MARCADOR_JUNTADO
					|| m == CpMarcador.MARCADOR_APENSADO) {
				if (!mob.isEliminado())
					acrescentarMarca(set, mob, m, dt, null, null);
			} else {
				// Edson: Os marcadores "Arq Corrente" e
				// "Aguardando andamento" são mutuamente exclusivos
				if (m != CpMarcador.MARCADOR_EM_ANDAMENTO
						|| !(mob.isArquivado() || mob.doc().getMobilGeral()
								.isArquivado()))
					acrescentarMarca(set, mob, m, dt, ultMovNaoCanc.getResp(),
							ultMovNaoCanc.getLotaResp());
			}

		}
		return set;
	}
	
	public boolean contemRetornoTransferencia(ExMovimentacao mov) {
		boolean contains = false;
		Iterator itr = transferenciasSet.iterator();
		
		while (itr.hasNext()) {
			ExMovimentacao element = (ExMovimentacao) itr.next();
			if(ExTipoMovimentacao.hasTransferencia(element.getIdTpMov()) == ExTipoMovimentacao.hasTransferencia(mov.getIdTpMov()))
			if (mov != null){
				if (element.getIdMov() != mov.getIdMov()
						&& ExTipoMovimentacao.hasTransferencia(element.getIdTpMov())
						&& ExTipoMovimentacao.hasTransferencia(mov.getIdTpMov())
						&& mov.getLotaCadastrante() == element.getLotaResp()
						) {
					contains = !contains;
					mov.setDtFimMov(null);
				}
			}
		}
		return contains;
	}
	
	/**
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que
	 * foram feitas no documento.
	 * 
	 * @param mob
	 */
	private SortedSet<ExMarca> calcularMarcadoresTemporalidade(ExMobil mob) {

		SortedSet<ExMarca> set = new TreeSet<ExMarca>();

		if (mob.isVolume() || !mob.doc().isFinalizado())
			return set;

		long[] mDest = new long[5];
		ExMovimentacao[] movDest = new ExMovimentacao[5];
		int nivelMDest = 0;

		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;

			Long t = mov.getIdTpMov();

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_CORRENTE;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_INTERMEDIARIO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_PERMANENTE;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_EM_EDITAL_DE_ELIMINACAO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest--;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ELIMINADO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE) {
				nivelMDest = 0;
			}

		}

		if (nivelMDest > 0) {
			acrescentarMarca(set, mob, mDest[nivelMDest],
					movDest[nivelMDest].getDtIniMov(),
					movDest[nivelMDest].getResp(),
					movDest[nivelMDest].getLotaResp());
			// Edson: nao calcular nada, por enquanto
			// calcularMarcadoresFuturosTemporalidade(set, mob,
			// movDest[nivelMDest], mDest[nivelMDest]);
		}

		return set;
	}

	private void calcularMarcadoresFuturosTemporalidade(SortedSet<ExMarca> set,
			ExMobil mob, ExMovimentacao mov, Long marcador) {

		if (marcador != CpMarcador.MARCADOR_ARQUIVADO_CORRENTE
				&& marcador != CpMarcador.MARCADOR_ARQUIVADO_INTERMEDIARIO)
			return;

		ExTemporalidade tmpCorrente = mob.getTemporalidadeCorrenteEfetiva();
		ExTemporalidade tmpIntermed = mob
				.getTemporalidadeIntermediarioEfetiva();
		ExTipoDestinacao destinacao = mob.getExDestinacaoFinalEfetiva();

		Date dtIniMarca = mov.getDtIniMov();
		Long marcadorFuturo = 0L;

		if (marcador == CpMarcador.MARCADOR_ARQUIVADO_CORRENTE) {
			if (tmpCorrente != null)
				dtIniMarca = tmpCorrente.getPrazoAPartirDaData(dtIniMarca);
			if (tmpIntermed != null)
				marcadorFuturo = CpMarcador.MARCADOR_TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO;
		} else if (tmpIntermed != null)
			dtIniMarca = tmpIntermed.getPrazoAPartirDaData(dtIniMarca);

		if (marcadorFuturo == 0)
			if (destinacao == null)
				return;
			else if (destinacao.getIdTpDestinacao().equals(
					ExTipoDestinacao.TIPO_DESTINACAO_ELIMINACAO))
				marcadorFuturo = CpMarcador.MARCADOR_A_ELIMINAR;
			else if (destinacao.getIdTpDestinacao().equals(
					ExTipoDestinacao.TIPO_DESTINACAO_GUARDA_PERMANENTE))
				marcadorFuturo = CpMarcador.MARCADOR_RECOLHER_PARA_ARQUIVO_PERMANENTE;
			else
				return;

		acrescentarMarca(set, mob, marcadorFuturo, dtIniMarca, mov.getResp(),
				mov.getLotaResp());

	}

	public void agendarPublicacaoBoletim(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc)
			throws AplicacaoException {

		try {
			if (!anexosAlternativosEstaoAssinados(doc))
				throw new AplicacaoException(
						"O documento "
								+ doc.getCodigo()
								+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), null,
					null, null, null, null, null);

			gravarMovimentacao(mov);

			// Grava o documento na tabela de boletim
			ExBoletimDoc boletim = new ExBoletimDoc();
			boletim.setExDocumento(mov.getExDocumento());

			ExDao.getInstance().gravar(boletim);

			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao agendar publicação no boletim.", 0, e);
		}
	}

	public void publicarBoletim(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtPubl) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtPubl,
					null, null, null, null, null);

			mov.setDescrMov("Publicado em " + mov.getDtMovDDMMYY());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

			for (ExDocumento ex : obterDocumentosBoletim(doc))
				notificarPublicacao(cadastrante, lotaCadastrante, ex,
						mov.getDtMov(), doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao atestar publicação no boletim.", 0, e);
		}
	}

	public void notificarPublicacao(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtPubl, final ExDocumento boletim)
			throws AplicacaoException {

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtPubl,
					null, null, null, null, null);

			mov.setExMobilRef(boletim.getMobilGeral());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

			try {
				String mensagemTeste = null;
				if (!SigaExProperties.isAmbienteProducao())
					mensagemTeste = SigaExProperties
							.getString("email.baseTeste");

				StringBuffer sb = new StringBuffer(
						"Documento publicado no Boletim Interno "
								+ doc.getCodigo());

				if (mensagemTeste != null)
					sb.append("\n " + mensagemTeste + "\n");

				StringBuffer sbHtml = new StringBuffer(
						"<html><body><p>Publicado no Boletim Interno: "
								+ boletim.getCodigo() + " em "
								+ FuncoesEL.getDataDDMMYYYY(dtPubl) + "</p> ");

				sbHtml.append("<p>Documento: " + doc.getCodigo() + "</p> ");
				sbHtml.append("<p>Descrição: " + doc.getDescrDocumento()
						+ "</p> ");


				if (mensagemTeste != null)
					sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

				sbHtml.append("</body></html>");

				String emailsAtendentes[] = null;
				String sDest = SigaExProperties
						.getString("bie.lista.destinatario.publicacao");

				if (sDest != null && !sDest.isEmpty())
					emailsAtendentes = sDest.split(",");

				if (emailsAtendentes != null && emailsAtendentes.length > 0) {
					Correio.enviar(SigaBaseProperties
							.getString("servidor.smtp.usuario.remetente"),
							emailsAtendentes,
							"Documento disponibilizado no Boletim Interno "
									+ doc.getCodigo(), sb.toString(), sbHtml
									.toString());
				}
			} catch (Exception e) {

			}
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao notificar publicação.", 0, e);
		}
	}

	public void pedirPublicacao(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final DpLotacao lotaTitular,
			final Date dtDispPublicacao, final String tipoMateria,
			final String lotPublicacao, final String descrPublicacao)
			throws AplicacaoException {

		try {
			// Verifica se o documento possui anexos alterantivos e se estes
			// anexos estão assinado
			if (!anexosAlternativosEstaoAssinados(mob.getExDocumento()))
				throw new AplicacaoException(
						"O documento "
								+ mob.getExDocumento().getCodigo()
								+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(dtDispPublicacao);
			// Esse teste é feito para validar a data de disponibilização e
			// pegar a data de publicação correta.
			// Pois se a data de disponibilização for sexta o sistema coloca a
			// data de publicação para segunda.
			String sMensagem = DJE.validarDataDeDisponibilizacao(true);
			if (sMensagem != null)
				throw new AplicacaoException(sMensagem);

			if (DJE.sao17Horas())
				throw new AplicacaoException(
						"Excedido Horário de Solicitação. Tente novamente amanhã ou defina a disponibilização para um dia depois do escolhido");

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, lotaTitular, null);

			mov.setDtDispPublicacao(dtDispPublicacao);
			mov.setDescrMov("Publicação prevista para "
					+ new SimpleDateFormat("dd/MM/yy").format(DJE
							.getDataPublicacao()));
			mov.setCadernoPublicacaoDje(tipoMateria);

			mov.setConteudoBlobXML("boletimadm", PublicacaoDJEBL
					.gerarXMLPublicacao(mov, tipoMateria, lotPublicacao,
							descrPublicacao));

			gravarMovimentacao(mov);

			// Verifica se está na base de teste
			String mensagemTeste = null;
			if (!SigaExProperties.isAmbienteProducao())
				mensagemTeste = SigaExProperties.getString("email.baseTeste");

			StringBuffer sb = new StringBuffer(
					"Foi feita uma solicitação de remessa do documento "
							+ mob.getExDocumento().getCodigo()
							+ " para publicação no DJE.\n ");

			if (mensagemTeste != null)
				sb.append("\n " + mensagemTeste + "\n");

			StringBuffer sbHtml = new StringBuffer(
					"<html><body><p>Foi feita uma solicitação de remessa do documento "
							+ mob.getExDocumento().getCodigo()
							+ " para publicação no DJE.</p> ");

			if (mensagemTeste != null)
				sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

			sbHtml.append("</body></html>");

			TreeSet<CpConfiguracao> atendentes = getConf().getListaPorTipo(
					CpTipoConfiguracao.TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO);

			ArrayList<String> emailsAtendentes = new ArrayList<String>();
			Date hoje = new Date();
			for (CpConfiguracao cpConf : atendentes) {
				if (!cpConf.ativaNaData(hoje))
					continue;
				if (!(cpConf instanceof ExConfiguracao))
					continue;
				ExConfiguracao conf = (ExConfiguracao) cpConf;
				if (conf.getCpSituacaoConfiguracao().getIdSitConfiguracao() == ExSituacaoConfiguracao.SITUACAO_PODE) {
					if (conf.getDpPessoa() != null) {
						if (!emailsAtendentes.contains(conf.getDpPessoa()
								.getEmailPessoaAtual())) {
							emailsAtendentes.add(conf.getDpPessoa()
									.getEmailPessoaAtual());
						}
					} else if (conf.getLotacao() != null) {
						List<DpPessoa> pessoasLotacao = CpDao.getInstance()
								.pessoasPorLotacao(
										conf.getLotacao().getIdLotacao(),
										false, true);
						for (DpPessoa pessoa : pessoasLotacao) {
							if (!emailsAtendentes.contains(pessoa
									.getEmailPessoaAtual()))
								emailsAtendentes.add(pessoa
										.getEmailPessoaAtual());
						}
					}
				}
			}

			Correio.enviar(
					SigaBaseProperties
							.getString("servidor.smtp.usuario.remetente"),
					emailsAtendentes.toArray(new String[emailsAtendentes.size()]),
					"Nova solicitação de publicação DJE ("
							+ mov.getLotaCadastrante().getSiglaLotacao() + ") ",
					sb.toString(), sbHtml.toString());

			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao agendar publicação.", 0, e);
		}
	}

	public void registrarDisponibilizacaoPublicacao(final ExMobil mob,
			final Date dtMov, final String pagPublicacao)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO,
					null, null, mob, dtMov, null, null, null, null, null);

			mov.setPagPublicacao(pagPublicacao);

			mov.setDescrMov("Documento disponibilizado no Diário");

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

			try {
				String mensagemTeste = null;
				if (!SigaExProperties.isAmbienteProducao())
					mensagemTeste = SigaExProperties
							.getString("email.baseTeste");

				StringBuffer sb = new StringBuffer(
						"Documento disponibilizado no Diário "
								+ mob.getExDocumento().getCodigo());

				if (mensagemTeste != null)
					sb.append("\n " + mensagemTeste + "\n");

				StringBuffer sbHtml = new StringBuffer(
						"<html><body><p>Documento disponibilizado no Diário.</p> ");

				sbHtml.append("<p>Documento: "
						+ mob.getExDocumento().getCodigo() + "</p> ");
				sbHtml.append("<p>Descrição: "
						+ mob.getExDocumento().getDescrDocumento() + "</p> ");
				sbHtml.append("<p>Data: " + FuncoesEL.getDataDDMMYYYY(dtMov)
						+ "</p> ");
				sbHtml.append("<p>Página: " + pagPublicacao + "</p> ");

				if (mensagemTeste != null)
					sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

				sbHtml.append("</body></html>");

				String emailsAtendentes[] = null;
				String sDest = SigaExProperties
						.getString("dje.lista.destinatario.publicacao");

				if (sDest != null && !sDest.isEmpty())
					emailsAtendentes = sDest.split(",");

				if (emailsAtendentes != null && emailsAtendentes.length > 0) {
					Correio.enviar(SigaBaseProperties
							.getString("servidor.smtp.usuario.remetente"),
							emailsAtendentes,
							"Documento disponibilizado no Diário "
									+ mob.getExDocumento().getCodigo(), sb
									.toString(), sbHtml.toString());
				}

			} catch (Exception e) {

			}

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao registrar disponibilização.",
					0, e);
		}
	}

	public void remeterParaPublicacao(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final DpLotacao lotaTitular,
			final Date dtDispPublicacao, final String tipoMateria,
			final String lotPublicacao, final String descrPublicacao)
			throws Exception {

		try {

			// Verifica se o documento possui anexos alterantivos e se estes
			// anexos estão assinado
			if (!anexosAlternativosEstaoAssinados(mob.getExDocumento()))
				throw new AplicacaoException(
						"O documento "
								+ mob.getExDocumento().getCodigo()
								+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, lotaTitular, null);

			mov.setDtDispPublicacao(dtDispPublicacao);
			mov.setCadernoPublicacaoDje(tipoMateria);

			// Calendar cal = new GregorianCalendar();
			// cal.setTime(dtDispPublicacao);
			// cal.add(Calendar.DAY_OF_MONTH, 1);
			// Date dtPublicacao = cal.getTime();

			// String nomeArq = "I-" + doc.getIdDoc();
			// Gera o arquivo zip composto de RTF e XML
			GeradorRTF gerador = new GeradorRTF();
			mov.setConteudoBlobRTF("boletim",
					gerador.geraRTFFOP(mob.getExDocumento()));
			mov.setConteudoBlobXML("boletimadm", PublicacaoDJEBL
					.gerarXMLPublicacao(mov, tipoMateria, lotPublicacao,
							descrPublicacao));

			// Verifica se o documento possui documentos filhos do tipo Anexo
			if (mob.getExDocumentoFilhoSet() != null) {

			}

			if (tipoMateria.equals("A"))
				mov.setNmArqMov("ADM.zip");
			else
				mov.setNmArqMov("JUD.zip");

			try {
				PublicacaoDJEBL.primeiroEnvio(mov);
			} catch (Throwable t) {
				throw new Exception(t.getMessage() + " -- "
						+ t.getCause().getMessage());
			}

			// mov.setNumTRFPublicacao(numTRF);
			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(dtDispPublicacao);
			DJE.validarDataDeDisponibilizacao(true);
			mov.setDescrMov("Publicação prevista para "
					+ new SimpleDateFormat("dd/MM/yy").format(DJE
							.getDataPublicacao()));

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new Exception(e.getMessage());
		}
	}

	public void anexarArquivo(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final String nmArqMov,
			final DpPessoa titular, final DpLotacao lotaTitular,
			final byte[] conteudo, final String tipoConteudo, String motivo, Set<ExMovimentacao> pendenciasResolvidas)
			throws AplicacaoException {

		final ExMovimentacao mov;

		try {
			iniciarAlteracao();

			mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular,
					lotaTitular, null);

			mov.setNmArqMov(nmArqMov);
			mov.setConteudoTpMov(tipoConteudo);
			mov.setConteudoBlobMov2(conteudo);
			mov.setDescrMov(motivo);

			gravarMovimentacao(mov);
			
			// Cancelar pendencias de anexação
			if (pendenciasResolvidas != null) {
				for (ExMovimentacao m : pendenciasResolvidas) {
					m.setExMovimentacaoCanceladora(mov); 
					gravarMovimentacao(m);
				}
			}
			
			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao anexar documento.", 0, e);
		}

		try {
			encerrarVolumeAutomatico(cadastrante, lotaCadastrante, mob, dtMov);
		} catch (Exception e) {
			// TODO: handle exception
		}

		alimentaFilaIndexacao(mob.getExDocumento(), true);
	}

	private void permitirOuNaoMovimentarDestinacao(ExMobil mob) {

		if (mob.isGeralDeProcesso()) {

			String sobrestados = "", emTransito = "", foraDaLota = "", apensados = "", msgFinal = "", comApensos = "";

			DpLotacao lotaBase = null, lotaVerif = null;

			for (ExMobil mobVerif : mob.doc().getVolumes()) {

				lotaVerif = mobVerif.getUltimaMovimentacaoNaoCancelada()
						.getLotaResp();

				if (lotaBase != null && !lotaVerif.equivale(lotaBase))
					foraDaLota += (foraDaLota.length() < 2 ? " Os seguintes volumes encontram-se em lotação diferente de "
							+ lotaBase.getSigla() + ": "
							: ", ")
							+ mobVerif.getSigla();

				if (lotaBase == null)
					lotaBase = lotaVerif;

				if (mobVerif.isSobrestado())
					sobrestados += (sobrestados.length() < 2 ? " Os seguintes volumes encontram-se sobrestados: "
							: ", ")
							+ mobVerif.getSigla();

				if (mobVerif.isEmTransito())
					emTransito += (emTransito.length() < 2 ? " Os seguintes volumes encontram-se em trânsito: "
							: ", ")
							+ mobVerif.getSigla();

				if (mobVerif.isApensado()
						&& !mobVerif.getMestre().doc().equals(mobVerif.doc()))
					apensados += (apensados.length() < 2 ? " Os seguintes volumes encontram-se apensados a outro processo: "
							: ", ")
							+ mobVerif.getSigla();
				
				for (ExMobil apenso : mobVerif.getApensos()){
					if (!apenso.doc().equals(mobVerif.doc()))
						comApensos += (comApensos.length() < 2 ? " Os seguintes volumes possuem volumes de outros processos apensados: "
								: ", ")
								+ apenso.getSigla() + " apensado a " + mobVerif.getSigla();
				}
			}

			msgFinal += foraDaLota + sobrestados + emTransito + apensados + comApensos;
			if (msgFinal.length() > 2)
				throw new AplicacaoException(
						"Não foi possível movimentar o processo." + msgFinal);

		} else if (mob.isApensado() || mob.temApensos())
			throw new AplicacaoException(
					"Não foi possível movimentar a via porque ela encontra-se apensada ou possui apensos.");
	}
	
	
	public void arquivarCorrenteAutomatico(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob) throws Exception {
			
	    arquivarCorrente(cadastrante, lotaCadastrante, mob, null, null, null, true);
	    try {
	    	ExDao.iniciarTransacao();
	    	for (ExMarca marc : mob.getExMarcaSet()) {
		    	if (!marc.getCpMarcador().getIdMarcador().equals(CpMarcador.MARCADOR_ARQUIVADO_CORRENTE)) {
		    		dao().excluir(marc);
		    	}
		    } 
	    	ExDao.commitTransacao();
	    } catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}	    
	    
	}	

	public void arquivarCorrente(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			Date dtMovIni, DpPessoa subscritor, boolean automatico) throws Exception {

		permitirOuNaoMovimentarDestinacao(mob);

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					null, null, dt);

			if (mob.isGeralDeProcesso()) {
				ExMobil ultVol = mob.doc().getUltimoVolume();
				ExMovimentacao ultMovNanCanc = ultVol
						.getUltimaMovimentacaoNaoCancelada();
				if (ultMovNanCanc != null) {
					mov.setLotaResp(ultMovNanCanc.getLotaResp());
					mov.setResp(ultMovNanCanc.getResp());
				}
			}
			
			if (automatico)
				mov.setDescrMov("Arquivamento automático.");
			
			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao arquivar documento.", 0, e);
		}
	}

	public void arquivarIntermediario(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			DpPessoa subscritor, String descrMov) throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					null, null, dt);
			mov.setDescrMov(descrMov);
			gravarMovimentacao(mov);

			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao arquivar um documento.", 0, e);
		}
	}

	public void arquivarPermanente(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			DpPessoa subscritor) throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					null, null, dt);
			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao arquivar permanentemente um documento.", 0, e);
		}
	}

	public void eliminar(DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			ExMobil mob, Date dtMov, DpPessoa subscritor, ExMobil termo)
			throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					null, null, dt);
			mov.setExMobilRef(termo);
			gravarMovimentacao(mov);
			concluirAlteracaoParcial(mob);
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao eliminar o documento.", 0, e);
		}
	}

	public void sobrestar(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			Date dtMovIni, DpPessoa subscritor) throws AplicacaoException {

		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();
		for (ExMobil m : set) {
			if (!m.getExDocumento().isFinalizado())
				throw new AplicacaoException(
						"Não é possível sobrestar um documento não finalizado");
		}

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR,
						cadastrante, lotaCadastrante, m, dtMov, subscritor,
						null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao sobrestar documento.", 0, e);
		}
	}

	public void avaliarReclassificar(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final ExClassificacao novaClassif, final String motivo,
			boolean fAvaliacao) throws AplicacaoException {

		boolean fReclassif = (novaClassif != null);

		if (!fAvaliacao && !fReclassif)
			throw new AplicacaoException(
					"Não foram informados dados para a reclassificação ou avaliação");

		if (fReclassif && (motivo == null || motivo.trim().equals("")))
			throw new AplicacaoException(
					"É necessário informar o motivo da reclassificação");

		if (fReclassif)
			for (ExMobil m : mob.getArvoreMobilesParaAnaliseDestinacao())
				if (m.isEmEditalEliminacao())
					throw new AplicacaoException(
							"Não é possível reclassificar porque "
									+ m.getSigla()
									+ " encontra-se em edital de eliminação. Após a eliminação, esta operação estará novamente disponível.");

		Date dt = dtMov != null ? dtMov : dao().dt();

		long tpMov;

		try {
			iniciarAlteracao();

			if (fAvaliacao)
				if (fReclassif)
					tpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO_COM_RECLASSIFICACAO;
				else
					tpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO;
			else
				tpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECLASSIFICACAO;

			final ExMovimentacao mov = criarNovaMovimentacao(tpMov,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					null, null, dtMov);

			if (fReclassif) {
				mov.setExClassificacao(novaClassif);
				mov.setDescrMov("Classificação documental alterada para "
						+ novaClassif.getDescricaoSimples() + " | Motivo: "
						+ motivo);
			} else
				mov.setDescrMov(motivo);

			gravarMovimentacao(mov);

			for (ExMobil m : mob.doc().getExMobilSet()) {
				ExDocumento docPai = m.getMobilPrincipal().getExDocumento();
				if (!docPai.equals(mob.doc()))
					atualizarMarcas(docPai);
			}
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao reclassificar.", 0, e);
		}

	}
	
	public void simularAssinaturaDocumento(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc){
		
		if (doc.isCancelado())
			throw new AplicacaoException(
					"Não é possível assinar um documento cancelado.");

		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!fPreviamenteAssinado) {
			try {
				processarComandosEmTag(doc, "pre_assinatura");
			} catch (AplicacaoException e) {
				throw e;
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), new Date(),
					doc.getSubscritor(), null, null, null, null);

			
			mov.setDescrMov(doc.getSubscritor().getNomePessoa());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

			// Verifica se o documento possui documento pai e faz a juntada
			// automática. Caso o pai seja um volume de um processo, primeiro
			// verifica se o volume está encerrado, se estiver procura o último
			// volume para juntar.

			if (doc.getExMobilPai() != null) {
				if (doc.getExMobilPai().getDoc().isProcesso()
						&& doc.getExMobilPai().isVolumeEncerrado()) {
					doc.setExMobilPai(doc.getExMobilPai().doc()
							.getUltimoVolume());
					gravar(cadastrante, lotaCadastrante, doc, null);
				}
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, mov.getDtMov(),
						cadastrante, cadastrante, mov);
			}

			if (doc.getExMobilAutuado() != null) {
				juntarAoDocumentoAutuado(cadastrante, lotaCadastrante, doc,
						mov.getDtMov(), cadastrante, cadastrante, mov);
			}

			if (!fPreviamenteAssinado && doc.isAssinado()) {
				processarComandosEmTag(doc, "assinatura");
			}

		} catch (final Exception e) {
			cancelarAlteracao();

			if (e.getMessage().contains("junta"))
				throw new AplicacaoException(
						"O documento foi assinado com sucesso mas não foi possível juntar este documento ao documento pai. O erro da juntada foi - "
								+ e.getMessage(), 0, e);

			throw new AplicacaoException("Erro ao assinar documento.", 0, e);
		}

	}
	
	public void simularAssinaturaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, long tpMovAssinatura) throws AplicacaoException {

		if (movAlvo == null) {
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}

		try{
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura,
						cadastrante, lotaCadastrante, movAlvo.getExMobil(), null,
						null, null, null, null, null);
			
			mov.setExMovimentacaoRef(movAlvo);
			
			mov.setSubscritor(movAlvo.getSubscritor() != null ? movAlvo.getSubscritor() : movAlvo.getCadastrante());
			mov.setDescrMov(movAlvo.getSubscritor().getNomePessoa());
			
			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			log.error("Erro ao assinar movimentação.", e);
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao assinar movimentação.", 0, e);
		}

	}
	
	public String criarDocTeste() throws Exception {
		//Método utilizado para testar criação de documentos por webService
		/*ExService client = Service.getExService();
		String s;
		
		s = client.criarDocumento("RJ13989", "RJ13989", "CEF", "Agência Av. Rio Branco, 326", "Interno Produzido", "Formulário", "Formulário de Solicitação de Deslocamento", null, null,
				true, "Limitado entre pessoas", "alteracao=N%E3o&solicitacaoDentroDoPrazo=Sim&razoesDoDeslocamento=Cursos%2C+Semin%E1rios%2C+Simp%F3sios%2C+Debates%2C+F%F3runs+e+afins&internacional=N%E3o&uf=AC&cidadeCacheAC=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CRETORNA_CIDADES_ESTADOResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CRETORNA_CIDADES_ESTADOResult%253E%253Cxs%253Aschema%2520id%253D%2522NewDataSet%2522%2520xmlns%253D%2522%2522%2520xmlns%253Axs%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%2520xmlns%253Amsdata%253D%2522urn%253Aschemas-microsoft-com%253Axml-msdata%2522%253E%253Cxs%253Aelement%2520name%253D%2522NewDataSet%2522%2520msdata%253AIsDataSet%253D%2522true%2522%2520msdata%253AUseCurrentLocale%253D%2522true%2522%253E%253Cxs%253AcomplexType%253E%253Cxs%253Achoice%2520minOccurs%253D%25220%2522%2520maxOccurs%253D%2522unbounded%2522%253E%253Cxs%253Aelement%2520name%253D%2522CIDADES%2522%253E%253Cxs%253AcomplexType%253E%253Cxs%253Asequence%253E%253Cxs%253Aelement%2520name%253D%2522CODCID%2522%2520type%253D%2522xs%253Aint%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522NOMCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522ESTCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522DDDCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522CEPCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253C%252Fxs%253Asequence%253E%253C%252Fxs%253AcomplexType%253E%253C%252Fxs%253Aelement%253E%253C%252Fxs%253Achoice%253E%253C%252Fxs%253AcomplexType%253E%253C%252Fxs%253Aelement%253E%253C%252Fxs%253Aschema%253E%253Cdiffgr%253Adiffgram%2520xmlns%253Amsdata%253D%2522urn%253Aschemas-microsoft-com%253Axml-msdata%2522%2520xmlns%253Adiffgr%253D%2522urn%253Aschemas-microsoft-com%253Axml-diffgram-v1%2522%253E%253CNewDataSet%2520xmlns%253D%2522%2522%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES1%2522%2520msdata%253ArowOrder%253D%25220%2522%253E%253CCODCID%253E43%253C%252FCODCID%253E%253CNOMCID%253EACRELANDIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69945000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES2%2522%2520msdata%253ArowOrder%253D%25221%2522%253E%253CCODCID%253E711%253C%252FCODCID%253E%253CNOMCID%253EASSIS%2520BRASIL%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69935000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES3%2522%2520msdata%253ArowOrder%253D%25222%2522%253E%253CCODCID%253E1397%253C%252FCODCID%253E%253CNOMCID%253EBRASILEIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69932000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES4%2522%2520msdata%253ArowOrder%253D%25223%2522%253E%253CCODCID%253E1463%253C%252FCODCID%253E%253CNOMCID%253EBUJARI%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69923000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES5%2522%2520msdata%253ArowOrder%253D%25224%2522%253E%253CCODCID%253E1970%253C%252FCODCID%253E%253CNOMCID%253ECAPIXABA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69922000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES6%2522%2520msdata%253ArowOrder%253D%25225%2522%253E%253CCODCID%253E2714%253C%252FCODCID%253E%253CNOMCID%253ECRUZEIRO%2520DO%2520SUL%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69980000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES7%2522%2520msdata%253ArowOrder%253D%25226%2522%253E%253CCODCID%253E3062%253C%252FCODCID%253E%253CNOMCID%253EEPITACIOLANDIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69934000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES8%2522%2520msdata%253ArowOrder%253D%25227%2522%253E%253CCODCID%253E3223%253C%252FCODCID%253E%253CNOMCID%253EFEIJO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69960000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES9%2522%2520msdata%253ArowOrder%253D%25228%2522%253E%253CCODCID%253E4659%253C%252FCODCID%253E%253CNOMCID%253EJORDAO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69975000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES10%2522%2520msdata%253ArowOrder%253D%25229%2522%253E%253CCODCID%253E5157%253C%252FCODCID%253E%253CNOMCID%253EMANCIO%2520LIMA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69990000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES11%2522%2520msdata%253ArowOrder%253D%252210%2522%253E%253CCODCID%253E5188%253C%252FCODCID%253E%253CNOMCID%253EMANOEL%2520URBANO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69950000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES12%2522%2520msdata%253ArowOrder%253D%252211%2522%253E%253CCODCID%253E5263%253C%252FCODCID%253E%253CNOMCID%253EMARECHAL%2520THAUMATURGO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69983000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES13%2522%2520msdata%253ArowOrder%253D%252212%2522%253E%253CCODCID%253E6870%253C%252FCODCID%253E%253CNOMCID%253EPLACIDO%2520DE%2520CASTRO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69928000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES14%2522%2520msdata%253ArowOrder%253D%252213%2522%253E%253CCODCID%253E7006%253C%252FCODCID%253E%253CNOMCID%253EPORTO%2520ACRE%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69921000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES15%2522%2520msdata%253ArowOrder%253D%252214%2522%253E%253CCODCID%253E7065%253C%252FCODCID%253E%253CNOMCID%253EPORTO%2520WALTER%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69982000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES16%2522%2520msdata%253ArowOrder%253D%252215%2522%253E%253CCODCID%253E7435%253C%252FCODCID%253E%253CNOMCID%253ERIO%2520BRANCO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%2520%252F%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES17%2522%2520msdata%253ArowOrder%253D%252216%2522%253E%253CCODCID%253E7562%253C%252FCODCID%253E%253CNOMCID%253ERODRIGUES%2520ALVES%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69985000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES18%2522%2520msdata%253ArowOrder%253D%252217%2522%253E%253CCODCID%253E7913%253C%252FCODCID%253E%253CNOMCID%253ESANTA%2520ROSA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69955000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES19%2522%2520msdata%253ArowOrder%253D%252218%2522%253E%253CCODCID%253E8871%253C%252FCODCID%253E%253CNOMCID%253ESENA%2520MADUREIRA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69940000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES20%2522%2520msdata%253ArowOrder%253D%252219%2522%253E%253CCODCID%253E8880%253C%252FCODCID%253E%253CNOMCID%253ESENADOR%2520GUIOMARD%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69925000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES21%2522%2520msdata%253ArowOrder%253D%252220%2522%253E%253CCODCID%253E9263%253C%252FCODCID%253E%253CNOMCID%253ETARAUACA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CDDDCID%253E68%253C%252FDDDCID%253E%253CCEPCID%253E69970000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES22%2522%2520msdata%253ArowOrder%253D%252221%2522%253E%253CCODCID%253E9960%253C%252FCODCID%253E%253CNOMCID%253EXAPURI%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69930000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253C%252FNewDataSet%253E%253C%252Fdiffgr%253Adiffgram%253E%253C%252FRETORNA_CIDADES_ESTADOResult%253E%253C%252FRETORNA_CIDADES_ESTADOResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&cidade=ACRELANDIA&diaUnico=N%E3o&numDePeriodos=1&dataIniPeriodo1=&dataFimPeriodo1=&proposto=CJF&numDePropostos=2&propostos1_pessoaSel.id=262253&propostos1_pessoaSel.sigla=RJ14054&propostos1_pessoaSel.descricao=ADRIANA+GRASSI+MARTINS+RIBEIRO+DE+ALMEIDA&ramal1=&solicitacaoComo1=Escolha&precisaAutorizacao1=Nao&propostos2_pessoaSel.id=279603&propostos2_pessoaSel.sigla=RJ13989&propostos2_pessoaSel.descricao=ANDRE+LUIS+SOUSA+DA+SILVA&ramal2=&solicitacaoComo2=Escolha&precisaAutorizacao2=Nao&contadorDePropostos=2&nomeDoEvento=&entidadePromotora=CEJ&cargaHoraria=1h&areaDeConhecimento=Escolha&abrangencia=Sim&foraDaRegiaoMetropolitana=Sim&idaDiaAnterior1=Nao&voltaDiaSubsequente1=Nao&diarias=Sim&agecccache14054=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CConsultaDadosBancariosResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CConsultaDadosBancariosResult%253E%253CMatricula%253E14054%253C%252FMatricula%253E%253CNome%253EADRIANA%2520GRASSI%2520MARTINS%2520RIBEIRO%2520DE%2520ALMEIDA%253C%252FNome%253E%253CEmpresa%253E2%253C%252FEmpresa%253E%253CBanco%253E1%253C%252FBanco%253E%253CAgencia%253E24767%253C%252FAgencia%253E%253CContaCorrente%253E351725%253C%252FContaCorrente%253E%253C%252FConsultaDadosBancariosResult%253E%253C%252FConsultaDadosBancariosResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&banco1=1&agencia1=24767&contaCorrente1=175994&agecccache13989=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CConsultaDadosBancariosResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CConsultaDadosBancariosResult%253E%253CMatricula%253E13989%253C%252FMatricula%253E%253CNome%253EANDRE%2520LUIS%2520SOUSA%2520DA%2520SILVA%253C%252FNome%253E%253CEmpresa%253E2%253C%252FEmpresa%253E%253CBanco%253E1%253C%252FBanco%253E%253CAgencia%253E24767%253C%252FAgencia%253E%253CContaCorrente%253E175994%253C%252FContaCorrente%253E%253C%252FConsultaDadosBancariosResult%253E%253C%252FConsultaDadosBancariosResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&banco2=1&agencia2=24767&contaCorrente2=175994&passagemAerea=Nao&usoDeCarroOficial=Nao", 
				null, false);
		
		return s;*/
		
		return null;
	}

	public String assinarDocumento(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final byte[] pkcs7, final byte[] certificado, long tpMovAssinatura)
			throws AplicacaoException {
		String sNome;
		Long lCPF = null;

		if (doc.isCancelado())
			throw new AplicacaoException(
					"Não é possível assinar um documento cancelado.");

		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!fPreviamenteAssinado) {
			try {
				processarComandosEmTag(doc, "pre_assinatura");
			} catch (AplicacaoException e) {
				throw e;
			} catch (Exception e) {
				throw new Error(e);
			}
		}

		final byte[] cms;
		try {
			final byte[] data = doc.getConteudoBlobPdf();

			CdService client = Service.getCdService();

			String s;

			if (certificado != null) {
				cms = client.validarECompletarPacoteAssinavel(certificado,
						data, pkcs7, true, (dtMov != null) ? dtMov : dao()
								.consultarDataEHoraDoServidor());
			} else {
				// s = client.validarAssinatura(pkcs7, data, dao().dt(),
				// VALIDAR_LCR);
				// Service.throwExceptionIfError(s);
				//
				// if (BUSCAR_CARIMBO_DE_TEMPO) {
				cms = client.validarECompletarAssinatura(pkcs7, data, true,
						dtMov);
			}
			sNome = client
					.validarAssinatura(cms, data, dao().dt(), VALIDAR_LCR);

			// cms = client
			// .converterPkcs7EmCMSComCertificadosLCRsECarimboDeTempo(pkcs7);
			// Service.throwExceptionIfError(cms);
			//
			// sNome = client.validarAssinaturaCMS(
			// MessageDigest.getInstance("SHA1").digest(data), SHA1,
			// cms, dao().dt());
			Service.throwExceptionIfError(sNome);

			String sCPF = client.recuperarCPF(cms);
			Service.throwExceptionIfError(sCPF);

			lCPF = Long.valueOf(sCPF);
			// } else {
			// sNome = s;
			// String sCPF = client.recuperarCPF(pkcs7);
			// Service.throwExceptionIfError(sCPF);
			// lCPF = Long.valueOf(sCPF);
			// }

			// writeB64File("c:/trabalhos/java/cd_teste_doc.b64", data);
			// writeB64File("c:/trabalhos/java/cd_teste_hash.b64",
			// MessageDigest
			// .getInstance("SHA1").digest(data));
			// writeB64File("c:/trabalhos/java/cd_teste_pkcs7.b64", pkcs7);
			// writeB64File("c:/trabalhos/java/cd_teste_cms.b64", cms);

		} catch (final Exception e) {
			throw new AplicacaoException("Erro na assinatura de um documento: " + 
					e.getMessage()==null?"":e.getMessage(),
					0, e);
		}

		boolean fValido = false;
		Long lMatricula = null;

		DpPessoa usuarioDoToken = null;

		// Obtem a matricula do assinante
		try {
			if (sNome == null)
				throw new AplicacaoException(
						"Não foi possível acessar o nome do assinante");
			String sMatricula = sNome.split(":")[1];
			lMatricula = Long.valueOf(sMatricula);
		} catch (final Exception e) {
			// throw new AplicacaoException(
			// "Não foi possível obter a matrícula do assinante", 0, e);
		}

		// Verifica se a matrícula confere com o subscritor titular ou com um
		// cossignatario
		try {
			if (lMatricula != null) {
				if (doc.getSubscritor() != null
						&& lMatricula
								.equals(doc.getSubscritor().getMatricula())) {
					fValido = true;
					usuarioDoToken = doc.getSubscritor();
				}
				if (!fValido) {
					fValido = (lMatricula.equals(doc.getCadastrante()
							.getMatricula()))
							&& (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO);
				}
				if (!fValido)
					for (ExMovimentacao m : doc.getMobilGeral()
							.getExMovimentacaoSet()) {
						if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
								&& m.getExMovimentacaoCanceladora() == null
								&& lMatricula.equals(m.getSubscritor()
										.getMatricula())) {
							fValido = true;
							usuarioDoToken = m.getSubscritor();
							continue;
						}
					}
				
				if(!fValido && tpMovAssinatura == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO && Ex.getInstance().getComp().podeAutenticarDocumento(cadastrante, lotaCadastrante, doc) ) {
					fValido = true;
				}
			}

			if (!fValido && lCPF != null) {
				if (doc.getSubscritor() != null
						&& lCPF.equals(doc.getSubscritor().getCpfPessoa())) {
					fValido = true;
					usuarioDoToken = doc.getSubscritor();
				}
				if (!fValido) {
					fValido = (lCPF.equals(doc.getCadastrante().getCpfPessoa()))
							&& (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO);
				}
				if (!fValido)
					for (ExMovimentacao m : doc.getMobilGeral()
							.getExMovimentacaoSet()) {
						if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
								&& m.getExMovimentacaoCanceladora() == null
								&& lCPF.equals(m.getSubscritor().getCpfPessoa())) {
							fValido = true;
							usuarioDoToken = m.getSubscritor();
							continue;
						}
					}
				
				if(!fValido && tpMovAssinatura == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO && Ex.getInstance().getComp().podeAutenticarDocumento(cadastrante, lotaCadastrante, doc) ) {
					fValido = true;
				}
			}

			if (lMatricula == null && lCPF == null)
				throw new AplicacaoException(
						"Não foi possível recuperar nem a matrícula nem o CPF do assinante");
			if (fValido == false)
				throw new AplicacaoException(
						"Assinante não é subscritor nem cossignatario");
		} catch (final Exception e) {
			throw new AplicacaoException(
					"Só é permitida a assinatura digital do subscritor e dos cossignatários do documento",
					0, e);
		}

		String s = null;
		try {
			iniciarAlteracao();

			if (usuarioDoToken != null && usuarioDoToken.equivale(cadastrante))
				usuarioDoToken = cadastrante;

			final ExMovimentacao mov = criarNovaMovimentacao(
					tpMovAssinatura,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					usuarioDoToken, null, null, null, null);

			// if (BUSCAR_CARIMBO_DE_TEMPO) {
			// mov.setConteudoTpMov(CdService.MIME_TYPE_CMS);
			mov.setConteudoBlobMov2(cms);
			// } else {
			mov.setConteudoTpMov(CdService.MIME_TYPE_PKCS7);
			// mov.setConteudoBlobMov2(pkcs7);
			// }

			mov.setDescrMov(sNome);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

			// Verifica se o documento possui documento pai e faz a juntada
			// automática. Caso o pai seja um volume de um processo, primeiro
			// verifica se o volume está encerrado, se estiver procura o último
			// volume para juntar.

			if (doc.getExMobilPai() != null) {
				if (doc.getExMobilPai().getDoc().isProcesso()
						&& doc.getExMobilPai().isVolumeEncerrado()) {
					doc.setExMobilPai(doc.getExMobilPai().doc()
							.getUltimoVolume());
					gravar(cadastrante, lotaCadastrante, doc, null);
				}
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, dtMov,
						cadastrante, cadastrante, mov);
			}

			if (doc.getExMobilAutuado() != null) {
				juntarAoDocumentoAutuado(cadastrante, lotaCadastrante, doc,
						dtMov, cadastrante, cadastrante, mov);
			}

			if (!fPreviamenteAssinado && doc.isAssinado()) {
				processarComandosEmTag(doc, "assinatura");
			}

		} catch (final Exception e) {
			cancelarAlteracao();

			if (e.getMessage().contains("junta"))
				throw new AplicacaoException(
						"O documento foi assinado com sucesso mas não foi possível juntar este documento ao documento pai. O erro da juntada foi - "
								+ e.getMessage(), 0, e);

			throw new AplicacaoException("Erro ao assinar documento.", 0, e);
		}

		alimentaFilaIndexacao(doc, true);

		return s;
	}

	public String assinarDocumentoComSenha(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final String matriculaSubscritor, final String senhaSubscritor, final DpPessoa titular)
			throws Exception {
		
		DpPessoa subscritor = null;
		boolean fValido = false;
		
		if (matriculaSubscritor == null || matriculaSubscritor.isEmpty())
			throw new AplicacaoException(
					"Matrícula do Subscritor não foi informada.");

		if (senhaSubscritor == null || senhaSubscritor.isEmpty())
			throw new AplicacaoException(
					"Senha do Subscritor não foi informada.");

		final String hashAtual = GeraMessageDigest.executaHash(
				senhaSubscritor.getBytes(), "MD5");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(
				matriculaSubscritor, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");
		
		subscritor = id.getDpPessoa().getPessoaAtual();

		boolean senhaValida = id.getDscSenhaIdentidade().equals(hashAtual);
		
		if (!senhaValida) {
			throw new AplicacaoException("Senha do subscritor inválida.");
		}
		
		
		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!doc.isFinalizado())
			throw new AplicacaoException(
					"Não é possível registrar assinatura de um documento não finalizado");

		if (doc.isCancelado())
			throw new AplicacaoException(
					"Não é possível assinar um documento cancelado.");
		
		if (!getComp().podeAssinarComSenha(subscritor, subscritor.getLotacao(), doc.getMobilGeral()))
			throw new AplicacaoException("Usuário não tem permissão de assinar documento com senha.");
		
		
		// Verifica se a matrícula confere com o subscritor titular ou com um
		// cossignatario
		try {
			if (subscritor != null) {
				if (doc.getSubscritor() != null
						&& subscritor.equivale(doc.getSubscritor())) {
					fValido = true;
				}
				if (!fValido) {
					fValido = (subscritor.equivale(doc.getCadastrante()))
							&& (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO);
				}
				if (!fValido)
					for (ExMovimentacao m : doc.getMobilGeral()
							.getExMovimentacaoSet()) {
						if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
								&& m.getExMovimentacaoCanceladora() == null
								&& subscritor.equivale(m.getSubscritor())) {
							fValido = true;
							continue;
						}
					}
			}

			if (fValido == false)
				throw new AplicacaoException(
						"Assinante não é subscritor nem cossignatario");
		} catch (final Exception e) {
			throw new AplicacaoException(
					"Só é permitida a assinatura digital do subscritor e dos cossignatários do documento",
					0, e);
		}
		

		String s = null;
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					subscritor, null, null, null, null);
			
			mov.setDescrMov(subscritor.getNomePessoa() + ":" + subscritor.getSigla());

			gravarMovimentacao(mov);
			concluirAlteracao(doc);

			// Verifica se o documento possui documento pai e faz a juntada
			// automática.
			if (doc.getExMobilPai() != null) {
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, dtMov,
						subscritor, titular, mov);
			}

			if (!fPreviamenteAssinado && doc.isAssinado())
				s = processarComandosEmTag(doc, "assinatura");
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao registrar assinatura.", 0, e);
		}

		alimentaFilaIndexacao(doc, true);
		return s;

	}
	
	public void assinarMovimentacaoComSenha(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, final String matriculaSubscritor, final String senhaSubscritor,
			long tpMovAssinatura) throws Exception {
		
        DpPessoa subscritor = null;
    	boolean fValido = false;
    	
        if (matriculaSubscritor == null || matriculaSubscritor.isEmpty())
			throw new AplicacaoException(
					"Matrícula do Subscritor não foi informada.");

		if (senhaSubscritor == null || senhaSubscritor.isEmpty())
			throw new AplicacaoException(
					"Senha do Subscritor não foi informada.");
        
		final String hashAtual = GeraMessageDigest.executaHash(
				senhaSubscritor.getBytes(), "MD5");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(
				matriculaSubscritor, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");
		
		subscritor = id.getDpPessoa().getPessoaAtual();

		boolean senhaValida = id.getDscSenhaIdentidade().equals(hashAtual);
		
		if (!senhaValida) {
			throw new AplicacaoException("Senha do subscritor inválida.");
		}		
		
		if (movAlvo != null) {
			log.info("Assinando movimentacao: " + movAlvo.toString()
					+ " Id da movimentação: " + movAlvo.getIdMov());
		} else {
			log.warn("A movimentacao a ser assinada nao pode ser nula");
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			log.warn("A movimentacao a ser assinada está cancelada");
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}
		
		
		if (tpMovAssinatura == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA &&
				!getComp().podeConferirCopiaMovimentacaoComSenha(cadastrante, lotaCadastrante, movAlvo))
			throw new AplicacaoException("Usuário não tem permissão de autenticar documento com senha.");
		
		if (tpMovAssinatura == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA &&
				!getComp().podeAssinarMovimentacaoComSenha(cadastrante, lotaCadastrante, movAlvo))
			throw new AplicacaoException("Usuário não tem permissão de assinar com senha.");

		
		// Verifica se a matrícula confere com o subscritor do Despacho ou
		// do
		// desentranhamento
		try {
		
			if (movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
				|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
				|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
				|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {

				fValido = movAlvo.getSubscritor() != null
							&& subscritor.equivale(movAlvo.getSubscritor());
	
				if (fValido == false
						&& movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
					log.warn("Assinante não é subscritor do desentranhamento");
					throw new AplicacaoException(
						"Assinante não é subscritor do desentranhamento");
				}

				if (fValido == false) {
					log.warn("Assinante não é subscritor do despacho");
					throw new AplicacaoException(
							"Assinante não é subscritor do despacho");
				}
			}
	
			log.info("Iniciando alteração da movimentação "
				+ movAlvo.toString() + " Id da movimentação: "
				+ movAlvo.getIdMov());
			
			iniciarAlteracao();

			// Nato: isso esta errado. Deveriamos estar recebendo o cadastrante
			//e sua lotacao.
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura,
				cadastrante, lotaCadastrante, movAlvo.getExMobil(), null,
				null, null, null, null, null);

			mov.setDescrMov(subscritor.getNomePessoa() +  ":" + subscritor.getSigla());

			mov.setExMovimentacaoRef(movAlvo);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
			
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			log.error("Erro ao assinar movimentação.", e);
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao assinar movimentação.", 0, e);
		}
	}
	

	/**
	 * @param doc
	 * @param tag
	 * @throws Exception
	 */
	public String processarComandosEmTag(final ExDocumento doc, String tag)
			throws Exception {
		String s = processarModelo(doc, null, tag, null, null);

		// retorna a resposta produzida pelo processamento da instrucao de
		// assinatura
		int start = s.indexOf("<!-- " + tag + " -->");
		int end = s.indexOf("<!-- /" + tag + " -->");
		if (start != -1 && end != -1) {
			s = s.substring(start, end);
			if (s.contains("{") && s.contains("}")) {
				s = s.substring(s.lastIndexOf("{") + 1, s.indexOf("}"));
			} else
				s = null;
		} else
			s = null;

		return s;
	}

	/**
	 * @param sFileName
	 * @param data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void writeB64File(String sFileName, final byte[] data)
			throws FileNotFoundException, IOException {
		FileOutputStream fout2 = new FileOutputStream(sFileName);
		fout2.write(Base64.encode(data).getBytes());
		fout2.close();
	}

	public void alimentaFilaIndexacao(ExDocumento doc, boolean reindexar) {
		try {
			if (doc == null || (!doc.isIndexavel()))
				return;
			BufferedWriter out = new BufferedWriter(new FileWriter(
					SigaExProperties.getString("siga.lucene.index.path")
							+ "/siga-ex-lucene-index-fila/" + doc.getIdDoc()));
			out.close();
		} catch (IOException e) {
			//
		}
	}

	public void assinarMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, final byte[] pkcs7, final byte[] certificado,
			long tpMovAssinatura) throws AplicacaoException {

		if (movAlvo != null) {
			log.info("Assinando movimentacao: " + movAlvo.toString()
					+ " Id da movimentação: " + movAlvo.getIdMov());
		} else {
			log.warn("A movimentacao a ser assinada nao pode ser nula");
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			log.warn("A movimentacao a ser assinada está cancelada");
			throw new AplicacaoException(
					"Não é possível assinar uma movimentação cancelada.");
		}

		String sNome;
		Long lCPF = null;

		final byte[] cms;
		try {
			final byte[] data = movAlvo.getConteudoBlobpdf();

			CdService client = Service.getCdService();

			String s;

			if (certificado != null) {
				cms = client.validarECompletarPacoteAssinavel(certificado,
						data, pkcs7, true, (dtMov != null) ? dtMov : dao()
								.consultarDataEHoraDoServidor());
			} else {
				// s = client.validarAssinatura(pkcs7, data, dao().dt(),
				// VALIDAR_LCR);
				// Service.throwExceptionIfError(s);
				//
				// if (BUSCAR_CARIMBO_DE_TEMPO) {
				cms = client.validarECompletarAssinatura(pkcs7, data, true,
						dtMov);
			}

			if (cms == null)
				throw new Exception("Assinatura inválida!");
				
			if (data == null)
				throw new Exception("Conteúdo inválido na validação da assinatura!");
				
			sNome = client
					.validarAssinatura(cms, data, dao().dt(), VALIDAR_LCR);

			Service.throwExceptionIfError(sNome);

			String sCPF = client.recuperarCPF(cms);
			Service.throwExceptionIfError(sCPF);

			lCPF = Long.valueOf(sCPF);

			boolean fValido = false;
			Long lMatricula = null;

			try {
				if (sNome == null)
					throw new AplicacaoException(
							"Não foi possível acessar o nome do assinante");
				String sMatricula = sNome.split(":")[1];
				lMatricula = Long.valueOf(sMatricula.replace("-", ""));
			} catch (final Exception e) {
				// throw new AplicacaoException(
				// "Não foi possível obter a matrícula do assinante", 0, e);
			}

			// Verifica se a matrícula confere com o subscritor do Despacho ou
			// do
			// desentranhamento no caso de assinatura de despacho
			if (tpMovAssinatura == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO && (movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
					|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
					|| movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)) {

				try {
					if (lMatricula != null) {
						fValido = movAlvo.getSubscritor() != null
								&& lMatricula.equals(movAlvo.getSubscritor()
										.getMatricula());
					}

					if (!fValido && lCPF != null) {
						fValido = movAlvo.getSubscritor() != null
								&& lCPF.equals(movAlvo.getSubscritor()
										.getCpfPessoa());
					}

					if (lMatricula == null && lCPF == null) {
						log.warn("Não foi possível recuperar nem a matrícula nem o CPF do assinante");
						throw new AplicacaoException(
								"Não foi possível recuperar nem a matrícula nem o CPF do assinante");
					}

					if (fValido == false
							&& movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
						log.warn("Assinante não é subscritor do desentranhamento");
						throw new AplicacaoException(
								"Assinante não é subscritor do desentranhamento");
					}

					if (fValido == false) {
						log.warn("Assinante não é subscritor do despacho");
						throw new AplicacaoException(
								"Assinante não é subscritor do despacho");
					}

				} catch (final Exception e) {
					if (fValido == false
							&& movAlvo.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
						log.warn("Só é permitida a assinatura digital do subscritor do desentranhamento");
						throw new AplicacaoException(
								"Só é permitida a assinatura digital do subscritor do desentranhamento",
								0, e);
					}
					log.warn("Só é permitida a assinatura digital do subscritor do despacho");
					throw new AplicacaoException(
							"Só é permitida a assinatura digital do subscritor do despacho",
							0, e);
				}
			}

			log.info("Iniciando alteração da movimentação "
					+ movAlvo.toString() + " Id da movimentação: "
					+ movAlvo.getIdMov());
			iniciarAlteracao();

			// Nato: isso esta errado. Deveriamos estar recebendo o cadastrante
			// e sua lotacao.
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura,
					cadastrante, lotaCadastrante, movAlvo.getExMobil(), null,
					null, null, null, null, null);

			mov.setExMovimentacaoRef(movAlvo);

			// if (BUSCAR_CARIMBO_DE_TEMPO) {
			// mov.setConteudoTpMov(CdService.MIME_TYPE_CMS);
			mov.setConteudoBlobMov2(cms);
			// } else {
			mov.setConteudoTpMov(CdService.MIME_TYPE_PKCS7);
			// mov.setConteudoBlobMov2(pkcs7);
			// }

			mov.setDescrMov(sNome);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			log.error("Erro ao assinar movimentação.", e);
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao assinar movimentação.", 0, e);
		}

	}

	public void atualizarPublicacao(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final String pagPublicacao,
			final Date dtDispPublicacao) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					null, null, null, null, null);

			mov.setPagPublicacao(pagPublicacao);
			mov.setDtDispPublicacao(dtDispPublicacao);

			mov.setDescrMov("Disponibilizado na internet em "
					+ new SimpleDateFormat("dd/MM/yy").format(dtDispPublicacao)
					+ ", na página " + pagPublicacao);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao cancelar juntada.", 0, e);
		}

	}

	public void cancelarDocumento(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {

		try {
			iniciarAlteracao();
			cancelarMovimentacoes(cadastrante, lotaCadastrante, doc);
			cancelarMovimentacoesReferencia(cadastrante, lotaCadastrante, doc);
			concluirAlteracao(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao cancelar o documento.", 0, e);
		}
	}

	private void cancelarMovimentacoes(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {
		// Cancelar todas as criações
		//
		for (ExMobil mob : doc.getExMobilSet()) {
			while (true) {
				ExMovimentacao mov = mob.getUltimaMovimentacaoNaoCancelada();
				if (mov == null)
					break;
				cancelarMovimentacao(cadastrante, lotaCadastrante, mob);
			}
		}
	}

	private void cancelarMovimentacoesReferencia(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {
		for (ExMobil mob : doc.getExMobilSet()) {
			Set<ExMovimentacao> set = mob.getExMovimentacaoReferenciaSet();
			if (set.size() > 0) {
				final Object[] aMovimentacao = set.toArray();
				for (int i = 0; i < set.size(); i++) {
					final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
					Ex.getInstance()
							.getBL()
							.cancelar(cadastrante, lotaCadastrante,
									movimentacao.getExMobil(), movimentacao,
									null, cadastrante, cadastrante, "");
				}
			}
		}

	}

	public void cancelarJuntada(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, String textoMotivo) throws Exception {

		try {

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			final ExMovimentacao ultMov = mob.getUltimaMovimentacao();

			mov.setDescrMov(textoMotivo);

			if (mov.getExNivelAcesso() == null)
				mov.setExNivelAcesso(ultMov.getExNivelAcesso());

			if (ultMov.getExTipoMovimentacao().getId() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO) {
				mov.setExMovimentacaoRef(mov.getExMobil()
						.getUltimaMovimentacao(
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA));
				final ExMobil mobPai = mov.getExMovimentacaoRef()
						.getExMobilRef();

				if (mobPai.isArquivado())
					throw new AplicacaoException(
							"Não é possível fazer o desentranhamento porque o documento ao qual este está juntado encontra-se arquivado.");

				final ExMovimentacao ultMovPai = mobPai.getUltimaMovimentacao();

				mov.setExMobilRef(mobPai);

				mov.setLotaResp(ultMovPai.getLotaResp());
				mov.setResp(ultMovPai.getResp());

				if (mobPai.isNumeracaoUnicaAutomatica()) {
					List<ExArquivoNumerado> ans = mov.getExMobil()
							.filtrarArquivosNumerados(null, true);
					armazenarCertidaoDeDesentranhamento(mov, mobPai, ans,
							textoMotivo);
				}
			} else {
				mov.setExMovimentacaoRef(mov
						.getExMobil()
						.getUltimaMovimentacao(
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO));
				mov.setResp(mob.getExDocumento().getTitular());
				mov.setLotaResp(mob.getExDocumento().getLotaTitular());
			}

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao cancelar juntada.", 0, e);
		}
	}

	/**
	 * Calcula o número original de páginas do documento que está sendo
	 * desentranhado, com base em um vetor de arquivos numerados. Depois, inclui
	 * essa informação no contexto do processador html para gerar a certidão.
	 * Também armazena o número original de páginas em campo específico da
	 * movimentação de cancelamento ou desentranhamento.
	 * 
	 * @param mov
	 * @param mob
	 * @param ans
	 * @param textoMotivo
	 * @throws AplicacaoException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void armazenarCertidaoDeDesentranhamento(final ExMovimentacao mov,
			final ExMobil mob, List<ExArquivoNumerado> ans, String textoMotivo)
			throws AplicacaoException, IOException,
			UnsupportedEncodingException, Exception {
		if (ans == null || ans.size() == 0)
			throw new AplicacaoException(
					"Não foi possível obter a numeração única automática das páginas da movimentação a ser cancelada.");

		Integer paginaInicial = ans.get(0).getPaginaInicial();
		Integer paginaFinal = ans.get(ans.size() - 1).getPaginaFinal();

		mov.setNumPaginasOri(paginaFinal - paginaInicial + 1);
		criarCertidaoDeDesentranhamento(mov, mob, paginaInicial, paginaFinal,
				textoMotivo);
	}

	/**
	 * @param movCanceladora
	 * @param textoMotivo
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void criarCertidaoDeDesentranhamento(
			final ExMovimentacao movCanceladora, ExMobil mob, int folhaInicial,
			int folhaFinal, String textoMotivo) throws IOException,
			UnsupportedEncodingException, Exception {
		Map<String, String> form = new TreeMap<String, String>();
		form.put("folhaInicial", Integer.toString(folhaInicial));
		form.put("folhaFinal", Integer.toString(folhaFinal));
		form.put("textoMotivo", textoMotivo);
		movCanceladora.setConteudoBlobForm(urlEncodedFormFromMap(form));

		// Gravar o Html //Nato
		final String strHtml = processarModelo(movCanceladora,
				"processar_modelo", null, null);
		movCanceladora.setConteudoBlobHtmlString(strHtml);

		// Gravar o Pdf
		final byte pdf[] = Documento.generatePdf(strHtml);
		movCanceladora.setConteudoBlobPdf(pdf);
		movCanceladora.setConteudoTpMov("application/zip");
	}

	/**
	 * Retorna o conteúdo de um Map<String,String> na forma de um array de bytes
	 * formatado de acordo com os padrões de Url Encoded Form e utilizando
	 * iso-8859-1 como charset.
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] urlEncodedFormFromMap(Map<String, String> map)
			throws IOException, UnsupportedEncodingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (String sKey : map.keySet()) {
			if (baos.size() != 0)
				baos.write("&".getBytes("iso-8859-1"));
			baos.write(sKey.getBytes("iso-8859-1"));
			baos.write('=');
			baos.write(URLEncoder.encode(map.get(sKey), "iso-8859-1")
					.getBytes());
		}
		byte[] baForm = baos.toByteArray();
		return baForm;
	}

	/**
	 * @param map
	 * @param form
	 */
	public static void mapFromUrlEncodedForm(Map map, final byte[] form) {
		if (form != null) {
			final String as[] = new String(form).split("&");
			for (final String s : as) {
				final String param[] = s.split("=");
				try {
					if (param.length == 2) {
						map.put(param[0],
								URLDecoder.decode(param[1], "iso-8859-1"));
					}
				} catch (final UnsupportedEncodingException e) {
				}
			}
		}
	}

	public void cancelarMovimentacao(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob)
			throws Exception {
		try {
			boolean indexar = false;
			SortedSet<ExMobil> set = null;
			ExMovimentacao movACancelar = mob
					.getUltimaMovimentacaoNaoCancelada();
			switch ((int) (long) movACancelar.getExTipoMovimentacao()
					.getIdTpMov()) {
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR:
				set = mob.getMobilETodosOsApensos();
				break;
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO:
				set = mob.getMobilETodosOsApensos();
				break;
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA:
				indexar = true;
				set = new TreeSet<ExMobil>();
				set.add(mob);
				break;
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO:
				indexar = true;
				set = mob.getMobilETodosOsApensos();
				break;
			default:
				set = new TreeSet<ExMobil>();
				set.add(mob);
			}

			iniciarAlteracao();
			for (ExMobil m : set) {
				if (m.getUltimaMovimentacao().getExTipoMovimentacao()
						.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM) {
					cancelarPedidoBI(m.doc());
				}
				
				final ExMovimentacao ultMov = m.getUltimaMovimentacao();
				final ExMovimentacao ultMovNaoCancelada = m
						.getUltimaMovimentacaoNaoCancelada(movACancelar);

				/*
				 * O correto seria a variável abaixo guardar a movimentação
				 * anterior à movimentação acima. Não necessariamente será a
				 * penúltima.
				 */
				final ExMovimentacao penultMovNaoCancelada = m
						.getPenultimaMovimentacaoNaoCancelada();

				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
						cadastrante, lotaCadastrante, m, null, null, null,
						null, null, null);

				gravarMovimentacao(mov);

				mov.setExMovimentacaoRef(ultMovNaoCancelada);
				mov.setExNivelAcesso(ultMovNaoCancelada.getExNivelAcesso());
				
				if (ultMovNaoCancelada.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
					PublicacaoDJEBL.cancelarRemessaPublicacao(mov);

				if (ultMovNaoCancelada.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA
						&& ultMovNaoCancelada.getExMovimentacaoRef() != null) {
					mov.setExMobilRef(ultMovNaoCancelada.getExMovimentacaoRef()
							.getExMobilRef());
				}

				if (penultMovNaoCancelada != null) {
					// if (mov.getLotaResp() == null)
					mov.setLotaResp(penultMovNaoCancelada.getLotaResp());
					// if (mov.getResp() == null)
					mov.setResp(penultMovNaoCancelada.getResp());
					// if (mov.getLotaDestinoFinal() == null)
					mov.setLotaDestinoFinal(penultMovNaoCancelada
							.getLotaDestinoFinal());
					// if (mov.getDestinoFinal() == null)
					mov.setDestinoFinal(penultMovNaoCancelada.getDestinoFinal());
					mov.setExClassificacao(penultMovNaoCancelada
							.getExClassificacao());
				} else {
					mov.setExClassificacao(null);
					if (ultMovNaoCancelada != null) {
						// if (mov.getLotaResp() == null)
						mov.setLotaResp(ultMovNaoCancelada.getLotaResp());
						// if (mov.getResp() == null)
						mov.setResp(ultMovNaoCancelada.getResp());
						// if (mov.getLotaDestinoFinal() == null)
						mov.setLotaDestinoFinal(ultMovNaoCancelada
								.getLotaDestinoFinal());
						// if (mov.getDestinoFinal() == null)
						mov.setDestinoFinal(ultMovNaoCancelada
								.getDestinoFinal());
					} else {
						// if (mov.getLotaResp() == null)
						mov.setLotaResp(ultMov.getLotaResp());
						// if (mov.getResp() == null)
						mov.setResp(ultMov.getResp());
						// if (mov.getLotaDestinoFinal() == null)
						mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
						// if (mov.getDestinoFinal() == null)
						mov.setDestinoFinal(ultMov.getDestinoFinal());
					}
				}

				gravarMovimentacaoCancelamento(mov, ultMovNaoCancelada);

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO) {
					atualizarDnmAnotacao(m);
				}

				if (ultMovNaoCancelada.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECLASSIFICACAO
						|| ultMovNaoCancelada.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO_COM_RECLASSIFICACAO)
					for (ExMobil mobRemarcar : mob.doc().getExMobilSet()) {
						ExDocumento docPai = mobRemarcar.getMobilPrincipal()
								.getExDocumento();
						if (!docPai.equals(mob.doc()))
							atualizarMarcas(docPai);
					}

				concluirAlteracaoParcial(m);
			}

			concluirAlteracao(null);

			if (indexar)
				alimentaFilaIndexacao(mob.getExDocumento(), true);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao cancelar movimentação.", 0, e);
			// throw e;
		}
	}

	public void cancelar(final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob, final ExMovimentacao movCancelar,
			final Date dtMovForm, final DpPessoa subscritorForm,
			final DpPessoa titularForm, String textoMotivo) throws Exception {

		if (movCancelar.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO) {
			if (!getComp().podeCancelarAnexo(titular, lotaTitular, mob,
					movCancelar))
				throw new AplicacaoException("Não é possível cancelar anexo");
		} else if (movCancelar.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO) {
			if (!getComp().podeAtenderPedidoPublicacao(titular, lotaTitular,
					mob))
				throw new AplicacaoException(
						"Usuário não tem permissão de cancelar pedido de publicação no DJE.");
		} else if (ExTipoMovimentacao.hasDespacho(movCancelar.getIdTpMov())) {
			if (!getComp().podeCancelarDespacho(titular, lotaTitular, mob,
					movCancelar))
				throw new AplicacaoException("Não é possível cancelar anexo");

		} else if (movCancelar.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarVinculacaoPapel(titular, lotaTitular, mob,
							movCancelar))
				throw new AplicacaoException(
						"Não é possível cancelar definição de perfil");

		} else if (movCancelar.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarVinculacaoDocumento(titular, lotaTitular, mob,
							movCancelar))
				throw new AplicacaoException(
						"Não é possível cancelar vinculação de documento");

		} else if (movCancelar.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM
				&& movCancelar.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
			if (!getComp().podeCancelar(titular, lotaTitular, mob, movCancelar))
				throw new AplicacaoException(
						"Não é permitido cancelar esta movimentação.");
		}

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
					titular, lotaTitular, mob, dtMovForm, subscritorForm, null,
					titularForm, null, null);

			// gravarMovimentacao(mov);
			mov.setExMovimentacaoRef(movCancelar);
			mov.setDescrMov(textoMotivo);

			if ((ExTipoMovimentacao.hasDocumento(movCancelar.getIdTpMov()))
					&& movCancelar.getExMobil().isNumeracaoUnicaAutomatica()) {
				List<ExArquivoNumerado> ans = mob.filtrarArquivosNumerados(
						mov.getExMovimentacaoRef(), false);
				armazenarCertidaoDeDesentranhamento(mov, mob, ans, textoMotivo);
				// if (ans.size() != 1)
				// throw new AplicacaoException(
				// "Não foi possível obter a numeração única automática das
				// páginas da movimentação a ser cancelada.");
				//
				// criarCertidaoDeDesentranhamento(mov, mob, ans.get(0)
				// .getPaginaInicial(), ans.get(0).getPaginaFinal());
			}

			if (movCancelar.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM) {
				cancelarPedidoBI(mob.doc());
			}

			gravarMovimentacaoCancelamento(mov, movCancelar);
			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao cancelar movimentação.", 0, e);
			// throw e;
		}
	}

	public void criarVia(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc)
			throws Exception {
		criarVia(cadastrante, lotaCadastrante, doc, null);
		return;
	}

	public void criarVia(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			Integer numVia) throws Exception {
		try {
			iniciarAlteracao();

			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_VIA,
					ExTipoMobil.class, false));
			mob.setNumSequencia(numVia == null ? (int) dao()
					.obterProximoNumeroVia(doc) : numVia);
			mob.setExDocumento(doc);
			doc.getExMobilSet().add(mob);
			mob = dao().gravar(mob);

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, cadastrante,
					lotaCadastrante, mob, null, null, null, null, null, null);

			gravarMovimentacao(mov);
			concluirAlteracao(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao criar nova via.", 0, e);
			// throw e;
		}
	}

	private ExDao dao() {
		return ExDao.getInstance();
	}

	public void desarquivarCorrente(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor)
			throws AplicacaoException {
		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();
		for (ExMobil m : set) {
			if (!m.isArquivado())
				throw new AplicacaoException(
						"Não é possível desarquivar um documento não arquivado");
		}

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE,
						cadastrante, lotaCadastrante, m, dtMov, subscritor,
						null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao reabrir.", 0, e);
		}
	}

	public void desarquivarIntermediario(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor)
			throws AplicacaoException {

		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();
		for (ExMobil m : set) {
			if (!m.isArquivadoIntermediario())
				throw new AplicacaoException(
						"Não é possível retirar do arquivo intermediário um documento que não esteja arquivado");
		}

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO,
						cadastrante, lotaCadastrante, m, dtMov, subscritor,
						null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao desarquivar intermediário.",
					0, e);
		}
	}

	public void desobrestar(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor)
			throws AplicacaoException {
		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();
		for (ExMobil m : set) {
			if (!m.isSobrestado())
				throw new AplicacaoException(
						"Não é possível desobrestar um documento que não esteja sobrestado");
		}

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR,
						cadastrante, lotaCadastrante, m, dtMov, subscritor,
						null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao desobrestar.", 0, e);
		}
	}

	public void excluirMovimentacao(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExMobil mob, Long idMov)
			throws AplicacaoException {
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = dao().consultar(idMov,
					ExMovimentacao.class, false);
			// ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO
			// movDao.excluir(mov);
			excluirMovimentacao(mov);
			if(!mob.doc().isAssinado() &&
					((mob.doc().isFisico() && !mob.doc().isFinalizado())
					|| (mob.doc().isEletronico() && !mob.doc().possuiAlgumaAssinatura())))
				processar(mob.getExDocumento(), true, false, null);
			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao excluir movimentação.", 0, e);
		}
		alimentaFilaIndexacao(mob.getExDocumento(), true);
	}

	@SuppressWarnings("unchecked")
	public String finalizar(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc, String realPath) throws Exception {

		if (doc.isFinalizado())
			throw new AplicacaoException("Documento já está finalizado.");

		if (!doc.getExClassificacao().isAtivo())
			throw new AplicacaoException("Classificação documental encerrada. Selecione outra na tela de edição.");
	
		if (doc.getExModelo() != null && doc.getExModelo().isFechado()) {
			throw new AplicacaoException("Este modelo não está mais em uso. Selecione outro na tela de edição");
		}
		
		if(doc.getExModelo() != null  && !doc.getExModelo().isAtivo()) {
			throw new AplicacaoException("Este modelo foi alterado. Edite-o para atualizá-lo");	
		}
		
		if(!doc.isEletronico() 
				&& doc.isProcesso() 
				&& doc.getExMobilPai() != null && doc.getExMobilPai().getExDocumento().isProcesso()
				&& doc.getExMobilPai().getExDocumento().isEletronico())
			throw new AplicacaoException(
					"Não é possível criar Subprocesso físico de processo eletrônico.");
		
		if(!getComp().podeSerSubscritor(doc))
			throw new AplicacaoException("O usuário não pode ser subscritor do documento");
		
		if(doc.isProcesso() && doc.getMobilGeral().temAnexos())
			throw new AplicacaoException("Processos não podem possuir anexos antes da finalização. Exclua todos os anexos para poder finalizar. Os anexos poderão ser incluídos no primeiro volume após a finalização.");

		Set<ExVia> setVias = doc.getSetVias();

		try {
			iniciarAlteracao();

			Date dt = dao().dt();
			Calendar c = Calendar.getInstance();
			c.setTime(dt);

			if (doc.getDtDoc() != null) {
				Calendar dtDocCalendar = Calendar.getInstance();
				dtDocCalendar.setTime(doc.getDtDoc());

				if (c.before(dtDocCalendar))
					throw new Exception(
							"Não é permitido criar documento com data futura");
			}

			// Pega a data sem horas, minutos e segundos...
			if (doc.getDtDoc() == null) {
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
				doc.setDtDoc(c.getTime());
			}

			if (doc.getOrgaoUsuario() == null)
				doc.setOrgaoUsuario(doc.getLotaCadastrante().getOrgaoUsuario());

			if (doc.getNumExpediente() == null)
				doc.setNumExpediente(obterProximoNumero(doc));

			doc.setDtFinalizacao(dt);

			if (doc.getExMobilPai() != null) {
				if (doc.getExMobilPai().doc().isProcesso() && doc.isProcesso()) {
					if (getComp().podeCriarSubprocesso(cadastrante,
							doc.getLotaCadastrante(), doc.getExMobilPai())) {
						int n = dao().obterProximoNumeroSubdocumento(
								doc.getExMobilPai());
						doc.setNumSequencia(n);
					} else {
						throw new AplicacaoException(
								"Documento filho não pode ser criado nessas condições.");
					}
				}
			}

			processar(doc, false, false, realPath);

			doc.setNumPaginas(doc.getContarNumeroDePaginas());
			dao().gravar(doc);

			if (doc.getExFormaDocumento().getExTipoFormaDoc().isExpediente()) {
				for (final ExVia via : setVias) {
					Integer numVia = null;
					if (via.getCodVia() != null)
						numVia = Integer.parseInt(via.getCodVia());
					if (numVia == null) {
						numVia = 1;
					}
					criarVia(cadastrante, lotaCadastrante, doc, numVia);
				}
			} else {
				criarVolume(cadastrante, lotaCadastrante, doc);
			}
			concluirAlteracao(doc);

			if (setVias == null || setVias.size() == 0)
				criarVia(cadastrante, lotaCadastrante, doc, null);

			String s = processarComandosEmTag(doc, "finalizacao");

			alimentaFilaIndexacao(doc, true);

			return s;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao finalizar o documento: "
					+ e.getMessage(), 0, e);
		}
	}

	public Long obterProximoNumero(ExDocumento doc) throws Exception {
		doc.setAnoEmissao(Long.valueOf(new Date().getYear()) + 1900);

		Long num = dao().obterProximoNumero(doc, doc.getAnoEmissao());

		if (num == null) {
			// Verifica se reiniciar a numeração ou continua com a numeração
			// anterior
			if (getComp().podeReiniciarNumeracao(doc)) {
				num = 1L;
			} else {
				// Obtém o próximo número considerando os anos anteriores até
				// 2006
				Long anoEmissao = doc.getAnoEmissao();
				while (num == null && anoEmissao > 2005) {
					anoEmissao = anoEmissao - 1;
					num = dao().obterProximoNumero(doc, anoEmissao);
				}
				// Se continuar null é porque nunca foi criado documento para
				// este formato.
				if (num == null)
					num = 1L;
			}
		}

		return num;
	}

	public void criarVolume(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc) throws AplicacaoException {
		try {
			iniciarAlteracao();

			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_VOLUME,
					ExTipoMobil.class, false));
			mob.setNumSequencia((int) dao().obterProximoNumeroVolume(doc));
			mob.setExDocumento(doc);
			doc.getExMobilSet().add(mob);
			mob = dao().gravar(mob);

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO, cadastrante,
					lotaCadastrante, mob, null, null, null, null, null, null);

			gravarMovimentacao(mov);
			if (mob.getNumSequencia() > 1) {
				ExMobil mobApenso = mob.doc().getVolume(
						mob.getNumSequencia() - 1);
				ExMovimentacao movApenso = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO,
						cadastrante, lotaCadastrante, mobApenso, null, null,
						null, null, null, null);
				movApenso.setExMobilRef(mob);
				gravarMovimentacao(movApenso);
			}
			concluirAlteracao(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao criar novo volume.", 0, e);
			// throw e;
		}
	}

	/**
	 * @param cadastrante
	 * @param titular
	 * @param lotaTitular
	 * @param doc
	 * @param nomeProcesso
	 * @throws Exception
	 */
	public void criarWorkflow(final DpPessoa cadastrante, DpPessoa titular,
			final DpLotacao lotaTitular, ExDocumento doc, String nomeProcesso)
			throws Exception {
		{ // Utiliza o serviço WebService do SigaWf
			WfService client = Service.getWfService();
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			{ // Adiciona no contexto a via Geral
				keys.add("doc_document");
				values.add(doc.getCodigo());
			}
			for (ExMobil mob : doc.getExMobilSet()) {
				if (mob.isGeral())
					continue;
				if (mob.isVia() || mob.isVolume()) {
					keys.add("doc_" + (char) ('a' + mob.getNumSequencia()));
					values.add((String) mob.getSigla());
				}
			}

			for (int n = 0; n < doc.getSetVias().size(); n++) { // Adiciona no
				// contexto a
				// via 'n'
				keys.add("doc_" + (char) ('a' + n));
				values.add(doc.getCodigo() + "-" + (char) ('A' + n));
			}
			List<ExPapel> papeis = dao().listarExPapeis();
			for (ExPapel papel : papeis) {
				List<DpResponsavel> responsaveis = doc
						.getResponsaveisPorPapel(papel);
				if (responsaveis != null && responsaveis.size() > 0) {
					keys.add("_doc_perfil_" + papel.getComoNomeDeVariavel());
					values.add(responsaveis.get(0).getSiglaCompleta());
				}
			}
			{
				client.criarInstanciaDeProcesso(
						nomeProcesso,
						SiglaParser.makeSigla(cadastrante,
								cadastrante.getLotacao()),
						SiglaParser.makeSigla(titular, lotaTitular), keys,
						values);
			}
		}
		atualizarWorkFlow(doc);
	}

	public static String descricaoSePuderAcessar(ExDocumento doc, DpPessoa titular,
			DpLotacao lotaTitular) {
		if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
			return "CONFIDENCIAL";
		else
			return doc.getDescrDocumento();
	}

	public static String descricaoConfidencial(ExDocumento doc,
			DpLotacao lotaTitular) {
		if (mostraDescricaoConfidencial(doc, lotaTitular))
			return "CONFIDENCIAL";
		else
			return doc.getDescrDocumento();
	}
	
	public String descricaoConfidencialDoDocumento(ExMobil mob,
			DpPessoa titular, DpLotacao lotaTitular) {

		try {
			if (!getComp().podeAcessarDocumento(titular, lotaTitular, mob))
				return "CONFIDENCIAL";
			else
				return mob.getExDocumento().getDescrDocumento();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "ERRO!";
		}
	}

	public static String selDescricaoConfidencial(Selecionavel sel,
			DpLotacao lotaTitular, DpPessoa titular) {
		if (sel instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) sel;
			if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
				return "CONFIDENCIAL";
			else
				return doc.getDescrDocumento();
		} else if (sel instanceof ExMobil) {
			ExDocumento doc = ((ExMobil) sel).getExDocumento();
			if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
				return "CONFIDENCIAL";
			else
				return doc.getDescrDocumento();
		} else if (sel instanceof DpLotacao) {
			DpLotacao lot = (DpLotacao) sel;
			return lot.getDescricaoAmpliada();
		}
		return sel.getDescricao();
	}

	public static boolean mostraDescricaoConfidencial(ExDocumento doc,
			DpLotacao lotaTitular) {
		if (doc == null)
			return false;
		if (doc.getExNivelAcesso() == null)
			return false;
		if (doc.getExNivelAcesso().getGrauNivelAcesso() > ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS
				|| (doc.getExNivelAcesso().getGrauNivelAcesso() == ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS && doc
						.getOrgaoUsuario().getIdOrgaoUsu() != lotaTitular
						.getOrgaoUsuario().getIdOrgaoUsu()))
			return true;
		return false;
	}
	
	public static boolean mostraDescricaoConfidencial(ExDocumento doc, DpPessoa titular,
			DpLotacao lotaTitular) {
		try {
			return !Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, doc.getMobilGeral());
		} catch (Exception e) {
			return true;
		}
	}

	public boolean mostraDescricaoConfidencialDoDocumento(ExDocumento doc,
			DpLotacao lotaTitular) {
		if (doc == null)
			return false;
		if (doc.getExNivelAcessoDoDocumento() == null)
			return false;
		if (doc.getExNivelAcessoDoDocumento().getGrauNivelAcesso() > ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS
				|| (doc.getExNivelAcessoDoDocumento().getGrauNivelAcesso() == ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS && doc
						.getOrgaoUsuario().getIdOrgaoUsu() != lotaTitular
						.getOrgaoUsuario().getIdOrgaoUsu()))
			return true;
		return false;
	}

	public static String anotacaoConfidencial(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mostraDescricaoConfidencial(mob.doc(), titular, lotaTitular))
			return "CONFIDENCIAL";
		String s = mob.getDnmUltimaAnotacao();
		if (s != null)
			return s;
		s = atualizarDnmAnotacao(mob);
		return s;
	}

	private static String atualizarDnmAnotacao(ExMobil mob) {
		String s;
		s = "";
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao().getIdTpMov()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO))
				s = mov.getDescrMov();
		}
		//Nato: precisei gravar um espaco pois estava desconsiderando a string vazia e gravando nulo.
		if (s == null || s.length() == 0)
			s = " ";
		mob.setDnmUltimaAnotacao(s);
		ExDao.getInstance().gravar(mob);
		return s;
	}

	public ExDocumento gravar(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc, String realPath) throws Exception {

		// Verifica se o documento possui documento pai e se o usuário possui
		// permissões de criar documento filho
		/*
		 * if (doc.getExMobilPai() != null &&
		 * doc.getExMobilPai().getExDocumento().getIdDoc() != null) if
		 * (doc.getExMobilPai().getExDocumento().isProcesso() &&
		 * doc.isProcesso()) { if
		 * (!ExCompetenciaBL.podeCriarSubprocesso(cadastrante, lotaCadastrante,
		 * doc.getExMobilPai())) throw new AplicacaoException(
		 * "Não foi possível criar Subprocesso do documento selecionado."); }
		 * else { if (!ExCompetenciaBL.podeCriarDocFilho(cadastrante,
		 * lotaCadastrante, doc.getExMobilPai())) throw new AplicacaoException(
		 * "Não é permitido criar documento filho do documento pai selecionado, pois este está inacessível ao usuário."
		 * ); }
		 */

		try {
			// System.out.println(System.currentTimeMillis() + " - INI gravar");
			iniciarAlteracao();

			if (doc.getCadastrante() == null)
				doc.setCadastrante(cadastrante);
			if (doc.getLotaCadastrante() == null) {
				doc.setLotaCadastrante(lotaCadastrante);
				if (doc.getLotaCadastrante() == null)
					doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
			}
			if (doc.getDtRegDoc() == null)
				doc.setDtRegDoc(dao().dt());

			// Nato: para obter o numero do TMP na primeira gravação
			if (doc.getIdDoc() == null)
				doc = ExDao.getInstance().gravar(doc);
			
			if(doc.getExModelo().isDescricaoAutomatica()) 
				doc.setDescrDocumento(processarComandosEmTag(doc, "descricao"));
				
			if(doc.getDescrDocumento() == null || doc.getDescrDocumento().isEmpty())
				throw new AplicacaoException(
						"A descrição do documento não pode ser vazia.");

			long tempoIni = System.currentTimeMillis();

			// Remover eventuais pendencias de anexos que foram inseridar por modelos anteriores e que não são necessárias no motelo atual.
			if (!doc.isFinalizado()) {
				while (true) {
					ExMovimentacao mov = doc.getMobilGeral().anexoPendente(null, false);
					if (mov == null)
						break;
					doc.getMobilGeral().getExMovimentacaoSet().remove(mov);
					dao().excluir(mov);
				}
			}

			// a estrutura try catch abaixo foi colocada de modo a impedir que
			// os erros na formatação impeçam a gravação do documento
			try {
				processar(doc, false, false, realPath);
			} catch (Throwable t) {
				System.out.println("gravação doc " + doc.getCodigo() + ", "
						+ new Date().toString() + " - erro na formatação - "
						+ t.getMessage());
				t.printStackTrace();
			}

			System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
					+ ", PESSOA " + doc.getCadastrante().getIdPessoa()
					+ ". Terminou processar: "
					+ (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();

			if (doc.getConteudoBlobDoc() != null)
				doc.setConteudoTpDoc("application/zip");

			processarResumo(doc);

			doc.setNumPaginas(doc.getContarNumeroDePaginas());
			doc = ExDao.getInstance().gravar(doc);
			for (ExMobil mob : doc.getExMobilSet()) {
				if (mob.getIdMobil() == null)
					mob = ExDao.getInstance().gravar(mob);
			}

			if (doc.getMobilGeral() == null) {
				throw new AplicacaoException(
						"Documento precisa de mobil geral.");
			}

			String funcao = doc.getForm().get("acaoGravar");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}
			
			String s = processarComandosEmTag(doc, "gravacao");

			concluirAlteracao(doc);

			System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
					+ ", PESSOA " + doc.getCadastrante().getIdPessoa()
					+ ". Terminou commit gravacao: "
					+ (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();
		} catch (final Exception e) {
			cancelarAlteracao();
			Throwable t = e.getCause();
			if (t != null && t instanceof InvocationTargetException)
				t = t.getCause();
			if (t != null && t instanceof AplicacaoException)
				throw (AplicacaoException) t;
			else
				throw new AplicacaoException("Erro na gravação", 0, e);
		}
		try {

		} catch (Exception ex) {
			//
		}
		// System.out.println(System.currentTimeMillis() + " - FIM gravar");
		return doc;
	}

	private void processarResumo(ExDocumento doc) throws Exception,
			UnsupportedEncodingException {
		String r = processarModelo(doc, null, "resumo", null, null);
		String resumo = null;
		if (r.contains("<!-- resumo -->") && r.contains("<!-- /resumo -->")) {
			if (r.contains("<!-- topico -->") && r.contains("<!-- /topico -->")) {
				r = r.substring(r.lastIndexOf("<!-- resumo -->") + 15,
						r.indexOf("<!-- /resumo -->")).trim();
				// r = r.replaceAll("\r", "");
				r = r.replaceAll("\n", "");
				r = r.replaceAll("\t", "");
				r = r.replaceAll("<!-- /topico -->", "");
				String topicos[] = r.split("<!-- topico -->");
				String topico = null;
				for (int i = 0; i < topicos.length; i++) {
					if (!topicos[i].equals("")) {
						String descr = topicos[i].substring(
								topicos[i].indexOf("name=\"")
										+ "name=\"".length(),
								topicos[i].indexOf("\" value="));
						String valor = topicos[i].substring(
								topicos[i].indexOf(" value=\"")
										+ " value=\"".length(),
								topicos[i].indexOf("\"/>"));
						topico = URLEncoder.encode(descr, "iso-8859-1") + "="
								+ URLEncoder.encode(valor, "iso-8859-1") + "&";
						if (resumo == null) {
							resumo = topico;
						} else {
							resumo = resumo + topico;
						}
					}
				}

				resumo = resumo.substring(0, resumo.length() - 1);

			}
		} else {
			resumo = null;
		}

		if (resumo != null) {
			doc.setConteudoBlobResumo(resumo.getBytes());
		}
	}
	
	public ExNivelAcesso atualizarDnmNivelAcesso(ExDocumento doc) {
		log.debug("[getExNivelAcesso] - Obtendo nível de acesso atual do documento...");
		ExNivelAcesso nivel = null;
		if (doc.getMobilGeral() != null
		                && doc.getMobilGeral().getUltimaMovimentacaoNaoCancelada() != null)
		        nivel = doc.getMobilGeral().getUltimaMovimentacaoNaoCancelada()
		                        .getExNivelAcesso();
		if (nivel == null)
			nivel = doc.getExNivelAcessoDoDocumento();
		doc.setDnmExNivelAcesso(nivel);
		ExDao.getInstance().gravar(doc);
		return nivel;
	}

	public void atualizarDnmAcesso(ExDocumento doc) {
		Date dt = ExDao.getInstance().dt();
		ExAcesso acesso = new ExAcesso();
		Set docsAlterados = new HashSet<Long>();

		// Se houve alteração, propagar para os documentos juntados em cada mobil
		//
		for (ExMobil mob : doc.getExMobilSet()) {
			int pularInferiores = 0;
			for (ExArquivoNumerado an : doc.getArquivosNumerados(mob)) {
				if (an.getArquivo() instanceof ExDocumento) {
					if (pularInferiores > an.getNivel())
						continue;
					else
						pularInferiores = 0;
					ExDocumento d = (ExDocumento)(an.getArquivo());
//					if (dt.equals(d.getDnmDtAcesso()))
//						continue;
					
					String sAcessoAntigo = d.getDnmAcesso();
					String sAcessoNovo = acesso.getAcessosString(d, dt);
					if (!sAcessoNovo.equals(sAcessoAntigo)) {
						docsAlterados.add(d.getIdDoc());
						d.setDnmAcesso(sAcessoNovo);
						d.setDnmDtAcesso(dt);
						ExDao.getInstance().gravar(d);
					} 
					if (!docsAlterados.contains(d.getIdDoc())) {
						pularInferiores = an.getNivel();
					}
				}
				
			}
		}
	}

	public void bCorrigirDataFimMov(final ExMovimentacao mov) throws Exception {
		try {
			iniciarAlteracao();

			dao().gravar(mov);

			mov.getExMobil().getExMovimentacaoSet().add(mov);

			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro na gravação", 0, e);

		}

	}

	public void bCorrigirCriacaoDupla(final ExMovimentacao mov)
			throws Exception {
		try {
			iniciarAlteracao();

			dao().excluir(mov);

			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro na gravação", 0, e);

		}

	}

	private void gravarMovimentacao(final ExMovimentacao mov)
			throws AplicacaoException, SQLException {
		// if (mov.getNumVia() != null && mov.getNumVia() == 0)
		// mov.setNumVia(null);
		//
		// if (ultMov == null)
		// ultMov = mov.getExDocumento()
		// .getUltimaMovimentacao(mov.getNumVia());
		//
		// if (ultMov != null) {
		// ultMov.setDtFimMov(new Date());
		// ExDao.getInstance().gravar(ultMov);
		// }
		mov.setNumPaginas(mov.getContarNumeroDePaginas());
		dao().gravar(mov);

		/*
		 * if (mov.getConteudoBlobMov() != null) movDao.gravarConteudoBlob(mov);
		 */

		if (mov.getExMobil().getExMovimentacaoSet() == null)
			mov.getExMobil()
					.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());

		mov.getExMobil().getExMovimentacaoSet().add(mov);

		if (!mov.getExTipoMovimentacao()
				.getIdTpMov()
				.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO)) {
			Notificador.notificarDestinariosEmail(mov,
					Notificador.TIPO_NOTIFICACAO_GRAVACAO);
		}
	}

	private void gravarMovimentacaoCancelamento(final ExMovimentacao mov,
			ExMovimentacao movCancelada) throws AplicacaoException,
			SQLException {
		// if (ultMov == null)
		// ultMov = mov.getExMobil().getUltimaMovimentacao();

		gravarMovimentacao(mov);

		if (movCancelada == null)
			movCancelada = mov.getExMobil().getUltimaMovimentacaoNaoCancelada();

		if (movCancelada != null) {
			movCancelada.setExMovimentacaoCanceladora(mov);
			dao().gravar(movCancelada);
		}

		Notificador.notificarDestinariosEmail(mov,
				Notificador.TIPO_NOTIFICACAO_CANCELAMENTO);
	}
	
	public void excluirDocumentoAutomatico(final ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) 
			throws Exception{
		excluirDocumento(doc, titular, lotaTitular, true);
	}
	
	
	public void excluirDocumento(final ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular, 
			boolean automatico) /* somente documentos temporários */
			throws Exception {
		try {
			ExDao.iniciarTransacao();

			try {
				final Date d = doc.getDtRegDoc();
			} catch (final ObjectNotFoundException e) {
				throw new AplicacaoException(
						"Documento já foi excluído anteriormente", 1, e);
			}

			if (doc.isFinalizado())

				throw new AplicacaoException(
						"Documento já foi finalizado e não pode ser excluído",
						2);
			for (ExMobil m : doc.getExMobilSet()) {
				Set set = m.getExMovimentacaoSet();
				
				if (!automatico && !Ex.getInstance().getComp()
						.podeExcluir(titular, lotaTitular, m))
					throw new AplicacaoException("Não é possível excluir");

				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						dao().excluir(movimentacao);
					}
				}

				for (ExMarca marc : m.getExMarcaSet())
					dao().excluir(marc);

				set = m.getExMovimentacaoReferenciaSet();
				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						Ex.getInstance()
								.getBL()
								.excluirMovimentacao(titular,
										lotaTitular,
										movimentacao.getExMobil(),
										movimentacao.getIdMov());
					}
				}

				dao().excluir(m);
			}

			// Exclui documento da tabela de Boletim Interno
			String funcao = doc.getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			dao().excluir(doc);
			ExDao.commitTransacao();
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}

	}
	

	public void excluirMovimentacao(final ExMovimentacao mov)
			throws AplicacaoException, SQLException {

		// Date dtFim = mov.getDtFimMov();
		// Date dtIni = mov.getDtIniMov();
		//
		// mov.getExMobil().getExMovimentacaoSet().remove(mov);
		// for (ExMovimentacao m : mov.getExMobil().getExMovimentacaoSet()) {
		// if (m.getDtFimMov() != null && m.getDtFimMov().equals(dtIni)) {
		// ExMovimentacao ultMov = m;
		// ultMov.setDtFimMov(dtFim);
		// dao().gravar(ultMov);
		// break;
		// }
		// }
		dao().excluir(mov);
		mov.getExMobil().getExMovimentacaoSet().remove(mov);
		for (ExMovimentacao m : mov.getExMobil().getExMovimentacaoSet()) {
			if (mov.equals(m.getExMovimentacaoCanceladora())) {
				m.setExMovimentacaoCanceladora(null);
			}
		}
		
		if (mov.getExTipoMovimentacao()
				.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO) {
			atualizarDnmAnotacao(mov.getExMobil());
		}

		Notificador.notificarDestinariosEmail(mov,
				Notificador.TIPO_NOTIFICACAO_EXCLUSAO);
	}

	public void incluirCosignatario(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor,
			final String funcaoCosignatario) throws AplicacaoException {

		try {
			if (subscritor == null) {
				throw new AplicacaoException("Cossignatário não foi informado");
			}

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					subscritor, null, null, null, null);

			mov.setDescrMov(funcaoCosignatario);
			mov.setNmFuncaoSubscritor(funcaoCosignatario);

			gravarMovimentacao(mov);
			processar(doc, true, false, null);
			concluirAlteracao(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao incluir Cossignatário.", 0, e);
		}
	}

	public void juntarDocumento(final DpPessoa cadastrante,
			final DpPessoa docTitular, final DpLotacao lotaCadastrante,
			final String idDocExterno, final ExMobil mob, final ExMobil mobPai,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final String idDocEscolha) throws Exception {

		if (idDocEscolha.equals("1")) {

			if (mobPai == null)
				throw new AplicacaoException(
						"Não foi selecionado um documento para a juntada");

			if (mob.getExDocumento().getIdDoc()
					.equals(mobPai.getExDocumento().getIdDoc())
					&& mob.getNumSequencia().equals(mobPai.getNumSequencia())
					&& mob.getExTipoMobil().getIdTipoMobil()
							.equals(mobPai.getExTipoMobil().getIdTipoMobil()))
				throw new AplicacaoException(
						"Não é possível juntar um documento a ele mesmo");

			if (!mobPai.getExDocumento().isFinalizado())
				throw new AplicacaoException(
						"Não é possível juntar a um documento não finalizado");

			if (mobPai.isGeral())
				throw new AplicacaoException(
						"É necessário informar a via à qual será feita a juntada");

			if (mob.doc().isEletronico()) {
				if (mob.temAnexosNaoAssinados()
						|| mob.temDespachosNaoAssinados())
					throw new AplicacaoException(
							"Não é possível juntar documento com anexo/despacho pendente de assinatura ou conferência");
			}

			if (!mob.getDoc().isEletronico() && mobPai.getDoc().isEletronico())
				throw new AplicacaoException(
						"Não é possível juntar um documento físico a um documento eletrônico.");

			if (mobPai.isSobrestado())
				throw new AplicacaoException(
						"Não é possível juntar um documento a um volume sobrestado.");

			// Verifica se o documeto pai já está apensado a este documento
			for (ExMobil apenso : mob.getApensos()) {
				if (apenso.getId() == mobPai.getId())
					throw new AplicacaoException(
							"Não é possível juntar um documento a um documento que está apensado a ele.");
			}

			if (!getComp().podeSerJuntado(docTitular, lotaCadastrante, mobPai))
				throw new AplicacaoException(
						"A via não pode ser juntada ao documento porque ele está em trânsito, arquivado, juntado, cancelado, arquivado, pendente de assinatura ou encontra-se em outra lotação");
		}

		final ExMovimentacao mov;

		try {
			iniciarAlteracao();

			Long idTpMov;
			if (idDocEscolha.equals("1")) {
				idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
			} else if (idDocEscolha.equals("2")) {
				idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO;
			} else
				throw new AplicacaoException("Opção inválida.");

			mov = criarNovaMovimentacao(idTpMov, cadastrante, lotaCadastrante,
					mob, dtMov, subscritor, null, titular, null, null);

			mov.setExMobilRef(mobPai);

			if (idDocEscolha.equals("1")) {
				mov.setDescrMov("Juntado ao documento "
						+ mov.getExMobilRef().getCodigo().toString());
			} else if (idDocEscolha.equals("2")) {
				mov.setDescrMov(idDocExterno);
			} else
				throw new AplicacaoException("Opção inválida.");

			if (idDocEscolha.equals("1") || idDocEscolha.equals("2")) {
			} else
				throw new AplicacaoException("Opção inválida.");

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao juntar documento.", 0, e);
		}

		try {
			encerrarVolumeAutomatico(cadastrante, lotaCadastrante,
					mov.getExMobilRef(), dtMov);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ExDocumento refazer(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {

		// As alterações devem ser feitas em cancelardocumento.
		try {
			iniciarAlteracao();

			cancelarMovimentacoesReferencia(cadastrante, lotaCadastrante, doc);

			ExDocumento novoDoc = duplicarDocumento(cadastrante,
					lotaCadastrante, doc);

			cancelarMovimentacoes(cadastrante, lotaCadastrante, doc);

			concluirAlteracao(novoDoc);
			// atualizarWorkflow(doc, null);
			return novoDoc;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao refazer o documento.", 0, e);
		}
	}

	// Nato: removi: HttpServletRequest request
	public ExDocumento duplicar(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {

		try {
			iniciarAlteracao();

			ExDocumento novoDoc = duplicarDocumento(cadastrante,
					lotaCadastrante, doc);

			concluirAlteracao(novoDoc);
			return novoDoc;

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao duplicar o documento.", 0, e);
		}
	}

	private ExDocumento duplicarDocumento(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {
		ExDocumento novoDoc = new ExDocumento();

		novoDoc.setConteudoBlobDoc(doc.getConteudoBlobDoc());
		novoDoc.setConteudoTpDoc(doc.getConteudoTpDoc());
		novoDoc.setDescrDocumento(doc.getDescrDocumento());

		if (doc.getDestinatario() != null && !doc.getDestinatario().isFechada())
			novoDoc.setDestinatario(doc.getDestinatario().getPessoaAtual());

		novoDoc.setFgEletronico(doc.getFgEletronico());
		novoDoc.setExNivelAcesso(doc.getExNivelAcesso());

		ExClassificacao classAtual = doc.getExClassificacaoAtual();
		if (classAtual != null && !classAtual.isFechada())
			novoDoc.setExClassificacao(classAtual.getAtual());

		novoDoc.setDescrClassifNovo(doc.getDescrClassifNovo());
		novoDoc.setExFormaDocumento(doc.getExFormaDocumento());

		if (!doc.getExModelo().isFechado())
			novoDoc.setExModelo(doc.getExModelo().getModeloAtual());
		else
			throw new AplicacaoException(
					"Não foi possível duplicar o documento pois este modelo não está mais em uso.");

		novoDoc.setExTipoDocumento(doc.getExTipoDocumento());

		if (doc.getLotaDestinatario() != null
				&& !doc.getLotaDestinatario().isFechada())
			novoDoc.setLotaDestinatario(doc.getLotaDestinatario()
					.getLotacaoAtual());

		if (doc.getSubscritor() != null && !doc.getSubscritor().isFechada()) {
			novoDoc.setSubscritor(doc.getSubscritor().getPessoaAtual());
			novoDoc.setLotaSubscritor(doc.getSubscritor().getPessoaAtual()
					.getLotacao());
		}

		novoDoc.setNmArqDoc(doc.getNmArqDoc());
		novoDoc.setNmDestinatario(doc.getNmDestinatario());
		novoDoc.setNmFuncaoSubscritor(doc.getNmFuncaoSubscritor());
		novoDoc.setNmOrgaoExterno(doc.getNmOrgaoExterno());
		novoDoc.setNmSubscritorExt(doc.getNmSubscritorExt());
		novoDoc.setNumExtDoc(doc.getNumExtDoc());
		novoDoc.setObsOrgao(doc.getObsOrgao());
		novoDoc.setOrgaoExterno(doc.getOrgaoExterno());
		novoDoc.setOrgaoExternoDestinatario(doc.getOrgaoExternoDestinatario());
		novoDoc.setExMobilPai(null);
		novoDoc.setOrgaoUsuario(cadastrante.getOrgaoUsuario());

		if (doc.getTitular() != null && !doc.getTitular().isFechada())
			novoDoc.setTitular(doc.getTitular().getPessoaAtual());

		if (doc.getLotaTitular() != null && !doc.getLotaTitular().isFechada())
			novoDoc.setLotaTitular(doc.getLotaTitular().getLotacaoAtual());

		novoDoc.setNumAntigoDoc(doc.getNumAntigoDoc());
		novoDoc.setExDocAnterior(doc);
		// novoDoc.setNumPaginas(novoDoc.getContarNumeroDePaginas());

		ExMobil mob = new ExMobil();
		mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL,
				ExTipoMobil.class, false));
		mob.setNumSequencia(1);
		mob.setExDocumento(novoDoc);
		mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
		novoDoc.setExMobilSet(new TreeSet<ExMobil>());
		novoDoc.getExMobilSet().add(mob);

		novoDoc = gravar(cadastrante, lotaCadastrante, novoDoc, null);

		// mob = dao().gravar(mob);

		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (doc.isFinalizado()
					&& mov.getDtIniMov().after(doc.getDtFinalizacao()))
				break;
			if (mov.isCancelada())
				continue;
			switch ((int) (long) mov.getIdTpMov()) {
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO:
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL:
				ExMovimentacao novaMov = duplicarMovimentacao(cadastrante,
						lotaCadastrante, mov);
				novaMov.setExMobil(novoDoc.getMobilGeral());

				try {
					iniciarAlteracao();
					gravarMovimentacao(novaMov);
					concluirAlteracao(novaMov.getExDocumento());
				} catch (final Exception e) {
					cancelarAlteracao();
					throw new AplicacaoException(
							"Erro ao gravar movimentacao.", 0, e);
				}
			}
		}

		// É necessário gravar novamente pois uma movimentação de inclusão
		// de cossignatário pode ter sido introduzida, gerando a necessidade
		// de refazer o HTML e o PDF.
		novoDoc = gravar(cadastrante, lotaCadastrante, novoDoc, null);

		return novoDoc;
	}

	private ExMovimentacao duplicarMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExMovimentacao mov)
			throws AplicacaoException {
		ExMovimentacao novaMov = new ExMovimentacao();
		novaMov.setCadastrante(cadastrante);
		novaMov.setConteudoBlobMov(mov.getConteudoBlobMov());
		novaMov.setConteudoTpMov(mov.getConteudoTpMov());
		novaMov.setDescrMov(mov.getDescrMov());
		novaMov.setDtIniMov(dao().dt());
		novaMov.setDtMov(mov.getDtMov());
		novaMov.setExNivelAcesso(mov.getExNivelAcesso());
		novaMov.setExTipoMovimentacao(mov.getExTipoMovimentacao());
		novaMov.setLotaCadastrante(lotaCadastrante);
		novaMov.setLotaResp(mov.getLotaResp());
		novaMov.setLotaSubscritor(mov.getLotaSubscritor());
		novaMov.setLotaTitular(mov.getLotaTitular());
		novaMov.setNmArqMov(mov.getNmArqMov());
		novaMov.setNmFuncaoSubscritor(mov.getNmFuncaoSubscritor());
		novaMov.setNumPaginas(mov.getNumPaginas());
		novaMov.setNumPaginasOri(mov.getNumPaginasOri());
		novaMov.setResp(mov.getResp());
		novaMov.setSubscritor(mov.getSubscritor());
		novaMov.setTitular(mov.getTitular());
		novaMov.setExPapel(mov.getExPapel());
		return novaMov;
	}

	// Nato: removi , final DpPessoa subscritor, final DpPessoa responsavel,
	// pois nao eram utilizados
	public void receber(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov)
			throws AplicacaoException {

		if (mob.isEmTransitoExterno())
			return;

		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();

		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO,
						cadastrante, lotaCadastrante, m, dtMov, cadastrante,
						null, null, null, null);

				final ExMovimentacao ultMov = m.getUltimaMovimentacao();
				if (ultMov.getDestinoFinal() != mov.getSubscritor()) {
					mov.setDestinoFinal(ultMov.getDestinoFinal());
					mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
				}

				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao(null);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao receber documento.", 0, e);
		}
	}

	public void receberEletronico(ExMovimentacao mov) throws AplicacaoException {

		// try {
		// iniciarAlteracao();
		// mov.setExEstadoDoc(dao().consultar(
		// ExEstadoDoc.ESTADO_DOC_EM_ANDAMENTO, ExEstadoDoc.class,
		// false));
		// dao().gravar(mov);
		// concluirAlteracao(mov.getExDocumento());
		// } catch (final Exception e) {
		// cancelarAlteracao();
		// throw new AplicacaoException("Erro ao receber documento.", 0, e);
		//
		// }

		// Nato: alterei para chamar a receber, pois temos que criar uma
		// movimentacao para influenciar no calculo dos marcadores
		receber(null, null, mov.getExMobil(), null);

	}

	public void indicarPermanente(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final String descrMov)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_INDICACAO_GUARDA_PERMANENTE,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao indicar para guarda permenente.", 0, e);
		}
	}

	public void reverterIndicacaoPermanente(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, String descrMov) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_REVERSAO_INDICACAO_GUARDA_PERMANENTE,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao reverter indicação para guarda permenente.", 0, e);
		}
	}

	public void referenciarDocumento(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final ExMobil mobRef, final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular) throws AplicacaoException {

		if (mobRef == null)
			throw new AplicacaoException(
					"Não foi selecionado um documento para o vinculo");

		if (mob.getExDocumento().getIdDoc()
				.equals(mobRef.getExDocumento().getIdDoc())
				&& mob.getNumSequencia().equals(mobRef.getNumSequencia())
				&& mob.getExTipoMobil().getIdTipoMobil()
						.equals(mobRef.getExTipoMobil().getIdTipoMobil()))
			throw new AplicacaoException(
					"Não é possível vincular um documento a ele mesmo");

		if (!mobRef.getExDocumento().isFinalizado())
			throw new AplicacaoException(
					"Não é possível vincular-se a um documento não finalizado");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			mov.setExMobilRef(mobRef);
			mov.setDescrMov("Vínculo: documento "
					+ mov.getExMobilRef().getCodigo().toString());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao vincular documento.", 0, e);
		}
	}

	public String RegistrarAssinatura(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular)
			throws AplicacaoException {
		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!doc.isFinalizado())
			throw new AplicacaoException(
					"Não é possível registrar assinatura de um documento não finalizado");

		String s = null;
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					subscritor, null, null, null, null);

			gravarMovimentacao(mov);
			concluirAlteracao(doc);

			// Verifica se o documento possui documento pai e faz a juntada
			// automática.
			if (doc.getExMobilPai() != null) {
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, dtMov,
						subscritor, titular, mov);
			}

			if (!fPreviamenteAssinado && doc.isAssinado())
				s = processarComandosEmTag(doc, "assinatura");
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao registrar assinatura.", 0, e);
		}

		alimentaFilaIndexacao(doc, true);
		return s;
	}
	
	public void transferirAutomatico(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, DpPessoa resp, DpLotacao lotaResp, ExMobil mob) throws Exception {
			
		transferir(null, null,
				cadastrante, lotaCadastrante, mob,
				null, null,
				null, lotaResp,
				resp, null,
				null, null, null, null, false,
				null, null, null, false, true);
	}	
	

	/**
	 * Transfere um documento de um local para outro. Esse método trata tanto da
	 * movimentação "Despacho com transferência" como "Transferência".
	 * 
	 * @param orgaoExterno
	 * @param obsOrgao
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param dtMovIni
	 * @param dtFimMov
	 * @param lotaResponsavel
	 * @param responsavel
	 * @param lotaDestinoFinal
	 * @param destinoFinal
	 * @param subscritor
	 * @param titular
	 * @param tpDespacho
	 * @param fInterno
	 * @param descrMov
	 * @param conteudo
	 * @param nmFuncaoSubscritor
	 * @throws AplicacaoException
	 * @throws Exception
	 */
	// Nato: retirei: final HttpServletRequest request,

	public void transferir(final CpOrgao orgaoExterno, final String obsOrgao,
			final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExMobil mob, final Date dtMov, final Date dtMovIni, final Date dtFimMov,
			DpLotacao lotaResponsavel, final DpPessoa responsavel,
			final DpLotacao lotaDestinoFinal, final DpPessoa destinoFinal,
			final DpPessoa subscritor, final DpPessoa titular,
			final ExTipoDespacho tpDespacho, final boolean fInterno,
			final String descrMov, final String conteudo,
			String nmFuncaoSubscritor, boolean forcarTransferencia, boolean automatico) throws AplicacaoException, Exception {


		boolean fDespacho = tpDespacho != null || descrMov != null
				|| conteudo != null;

		boolean fTranferencia = lotaResponsavel != null || responsavel != null;

		SortedSet<ExMobil> set = mob.getMobilETodosOsApensos();

		Date dtUltReceb = null;

		//Edson: apagar isto? A verificação já é feita no for abaixo...
		if (fDespacho && mob.isVolumeApensadoAoProximo())
			throw new AplicacaoException(
					"Não é possível fazer despacho em um documento que faça parte de um apenso");
		
		if(!automatico) {

			if (fTranferencia && mob.doc().isEletronico()) {
				if (mob.doc().getMobilGeral().temAnexosNaoAssinados()
						|| mob.doc().getMobilGeral().temDespachosNaoAssinados())
						throw new AplicacaoException(
								"Não é permitido fazer transferência em documento com anexo/despacho pendente de assinatura ou conferência");

			}

			for (ExMobil m : set) {
			
				if (!m.equals(mob)
						&& fDespacho && fTranferencia) {
					throw new AplicacaoException(
							"Não é permitido fazer despacho com transferência em um documento que faça parte de um apenso. Faça primeiro o despacho e depois transfira o documento.");
				}
			
				if (fDespacho && m.isVolumeEncerrado())
					if (m.isApensadoAVolumeDoMesmoProcesso())
						continue;
					else
						throw new AplicacaoException(
								"Não é permitido fazer despacho em volume que esta encerrado");

				if (fDespacho	
						&& !getComp()
						.podeDespachar(cadastrante, lotaCadastrante, m))
					throw new AplicacaoException(
							"Não é permitido fazer despacho. Verifique se a via ou processo não está arquivado(a) e se não possui despachos pendentes de assinatura.");

				if (fTranferencia) {
					
					if (lotaResponsavel.isFechada())
						throw new AplicacaoException(
								"Não é permitido transferir documento para lotação fechada");

					if (forcarTransferencia) {
						if (!getComp().podeSerTransferido(m))
							throw new AplicacaoException("Transferência não pode ser realizada (" + m.getSigla() + " ID_MOBIL: " + m.getId() + ")");
					} else {
						if (!getComp().podeTransferir(cadastrante, lotaCadastrante, m)) 
							throw new AplicacaoException("Transferência não permitida (" + m.getSigla() + " ID_MOBIL: " + m.getId() + ")");
					}
					if (!m.getExDocumento().isAssinado()
							&& !lotaResponsavel.equivale(m.getExDocumento()
									.getLotaTitular())
									&& !getComp().podeReceberDocumentoSemAssinatura(
											responsavel, lotaResponsavel, m))
						throw new AplicacaoException(
								"Não é permitido fazer transferência em documento que ainda não foi assinado");

					if (m.doc().isEletronico()) {
						if (m.temAnexosNaoAssinados()
								|| m.temDespachosNaoAssinados())
							throw new AplicacaoException(
									"Não é permitido fazer transferência em documento com anexo/despacho pendente de assinatura ou conferência");
					}

					if (m.getExDocumento().isEletronico()
							&& !m.getExDocumento().jaTransferido()
							&& !m.getExDocumento().isAssinado())
						throw new AplicacaoException(
								"Não é permitido fazer transferência em documento que ainda não foi assinado por todos os subscritores.");

				}

				if (!fDespacho) {
					if (responsavel == null && lotaResponsavel == null)
						if (orgaoExterno == null && obsOrgao == null)
							throw new AplicacaoException(
									"Não foram informados dados para o despacho/transferência");
				}			
			}
		}

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		ExMovimentacao mov;

		mov = new ExMovimentacao();

		try {
			iniciarAlteracao();

			for (ExMobil m : set) {

				Long idTpMov;
				if (!fDespacho) {
					if (responsavel == null && lotaResponsavel == null)
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA;
					else
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA;
				} else if (lotaResponsavel == null && responsavel == null
						&& orgaoExterno == null) {
					if (fInterno)
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO;
					else
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO;
				} else if (orgaoExterno != null) {
					if (fInterno)
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA;
					else
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA;
				} else {
					if (fInterno)
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA;
					else
						idTpMov = ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA;
				}

				// se não for apensado, pode.
				// se for apenas tranferência, pode.
				if (m.equals(mob)
						|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA
						|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) {

					final ExTipoMovimentacao tpmov = dao().consultar(idTpMov,
							ExTipoMovimentacao.class, false);

					mov = criarNovaMovimentacaoTransferencia(tpmov.getIdTpMov(),
							cadastrante, lotaCadastrante, m, dtMov, dtFimMov,
							(subscritor == null && fDespacho) ? cadastrante
									: subscritor, null, titular, null, dt);

					if (dt != null)
						mov.setDtIniMov(dt);

					if (dtFimMov != null)//
						mov.setDtFimMov(dtFimMov);//

					if (orgaoExterno != null || obsOrgao != null) {
						mov.setOrgaoExterno(orgaoExterno);
						mov.setObsOrgao(obsOrgao);
					}

					if (lotaResponsavel != null) {
						mov.setLotaResp(lotaResponsavel);
						mov.setResp(responsavel);
					} else {
						if (responsavel != null)
							lotaResponsavel = responsavel.getLotacao();
					}
					
					mov.setLotaTitular(mov.getLotaSubscritor());
					mov.setDestinoFinal(destinoFinal);
					mov.setLotaDestinoFinal(lotaDestinoFinal);

					mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);

					mov.setExTipoDespacho(tpDespacho);
					mov.setDescrMov(descrMov);

					if (tpDespacho != null || descrMov != null
							|| conteudo != null) {
						// Gravar o form
						String cont = null;
						if (conteudo != null) {
							cont = conteudo;
						} else if (descrMov != null) {
							cont = descrMov;
						} else {
							cont = tpDespacho.getDescTpDespacho();
						}
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						baos.write("conteudo".getBytes("iso-8859-1"));
						baos.write('=');
						baos.write(URLEncoder.encode(cont, "iso-8859-1")
								.getBytes());
						mov.setConteudoBlobForm(baos.toByteArray());

						// Gravar o Html //Nato
						final String strHtml = processarModelo(mov,
								"processar_modelo", null, mov.getTitular()
										.getOrgaoUsuario());
						mov.setConteudoBlobHtmlString(strHtml);

						// Gravar o Pdf
						final byte pdf[] = Documento.generatePdf(strHtml);
						mov.setConteudoBlobPdf(pdf);
						mov.setConteudoTpMov("application/zip");
					}
					if (automatico)
						mov.setDescrMov("Transferência automática.");
					
					gravarMovimentacao(mov);
					concluirAlteracaoParcial(m);
				}
			}

			concluirAlteracao(null);

		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao transferir documento.", 0, e);
		}

		if (fDespacho) {
			try {
				encerrarVolumeAutomatico(cadastrante, lotaCadastrante, mob,
						dtMovIni);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void registrarAcessoReservado(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExMobil mob) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ACESSO_INDEVIDO,
					cadastrante, lotaCadastrante, mob, null, null, null, null,
					null, null);

			mov.setDescrMov("Visualizado por " + cadastrante.getNomePessoa()
					+ " (" + lotaCadastrante.getNomeLotacao() + ")");

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao registrar acesso reservado.", 0, e);
		}
	}

	// Nato: removi: final HttpServletRequest request,
	public void anotar(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, DpLotacao lotaResponsavel,
			final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, final String descrMov,
			String nmFuncaoSubscritor) throws AplicacaoException {

		if (descrMov == null) {
			if (responsavel == null && lotaResponsavel == null)
				if (dtMov == null)
					throw new AplicacaoException(
							"Não foram informados dados para a anotação");
		}

		try {
			// criarWorkflow(cadastrante, lotaCadastrante, doc, "Exoneracao");
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular,
					null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			
			atualizarDnmAnotacao(mov.getExMobil());
			
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao fazer anotação.", 0, e);
		}

		alimentaFilaIndexacao(mob.getExDocumento(), true);

	}

	// Nato: removi: final HttpServletRequest request,
	public void vincularPapel(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, DpLotacao lotaResponsavel,
			final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, final String descrMov,
			String nmFuncaoSubscritor, ExPapel papel) throws AplicacaoException {

		if (descrMov == null) {
			if (responsavel == null && lotaResponsavel == null)
				if (dtMov == null)
					throw new AplicacaoException(
							"Não foram informados dados para a vinculação de papel");
		}
		if (papel == null)
			throw new AplicacaoException(
					"Não foi informado o papel para a vinculação de papel");

		try {
			// criarWorkflow(cadastrante, lotaCadastrante, doc, "Exoneracao");
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL,
					cadastrante, lotaCadastrante, mob, dtMov, responsavel,
					lotaResponsavel, titular, null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov(descrMov);
			mov.setExPapel(papel);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao fazer vinculação de papel.",
					0, e);
		}

	}

	public void redefinirNivelAcesso(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, DpLotacao lotaResponsavel,
			final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, String nmFuncaoSubscritor,
			ExNivelAcesso nivelAcesso) throws AplicacaoException {

		if (nivelAcesso == null) {
			throw new AplicacaoException(
					"Não foram informados dados para a redefinição do nível de acesso");
		}

		if (nivelAcesso.getIdNivelAcesso().equals(
				doc.getExNivelAcesso().getIdNivelAcesso()))
			throw new AplicacaoException(
					"Nível de acesso selecionado é igual ao atual");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov,
					subscritor, null, titular, null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov("Nível de acesso do documento alterado de "
					+ doc.getExNivelAcesso().getNmNivelAcesso() + " para "
					+ nivelAcesso.getNmNivelAcesso());

			mov.setExNivelAcesso(nivelAcesso);
			doc.setExNivelAcesso(nivelAcesso);

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao tentar redefinir nível de acesso", 0, e);
		}
		alimentaFilaIndexacao(doc, true);
	}
	
	public void exigirAnexo(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final String descrMov) throws AplicacaoException {
		
		if (mob.doc().getIdDoc() == null)
			return;

		if (descrMov == null) {
			throw new AplicacaoException(
					"Não foram informados dados para a exigência de anexação");
		}
		
		boolean fGravarMov = false;
		try {
			String as[] = descrMov.split(";");
			for (String s : as) {
				if (mob.anexoPendente(s, true) != null)
					continue;

				if (!fGravarMov) {
					iniciarAlteracao();
					fGravarMov = true;
				}
	
				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO, cadastrante,
						lotaCadastrante, mob, null, null, null, null,
						null, null);
				mov.setDescrMov(s);
				gravarMovimentacao(mov);
			}
			if (fGravarMov) 
				concluirAlteracao(mob.getExDocumento());
		} catch (final Exception e) {
			if (fGravarMov) 
				cancelarAlteracao();
			throw new AplicacaoException("Erro ao exigir anexação.", 0, e);
		}
	}

	/* Daqui para frente não são movimentacoes, mas sim funcoes auxiliares */

	public String canonicalizarHtml(String s, boolean fRemoverEspacos,
			boolean fRemoverTagsDesconhecidos, boolean fIso8859,
			boolean fBodyOnly) {
		String sRet;
		try {
			sRet = (new ProcessadorHtml().canonicalizarHtml(s, fRemoverEspacos,
					fRemoverTagsDesconhecidos, fIso8859, fBodyOnly, false));
			return sRet;
		} catch (Exception e) {
			return s;
		}
	}

	public void processar(final ExDocumento doc, final boolean gravar,
			final boolean transacao, String realPath) throws Exception {
		try {
			if (doc != null
					&& (doc.isAssinado() || doc.isAssinadoDigitalmente()))
				throw new AplicacaoException(
						"O documento não pode ser reprocessado, pois já está assinado");

			if ((doc.getExModelo() != null && ("template/freemarker".equals(doc
					.getExModelo().getConteudoTpBlob()) || doc.getExModelo()
					.getNmArqMod() != null))
					|| doc.getExTipoDocumento().getIdTpDoc() == 2) {
				if (doc.getConteudoBlobForm() != null) {
				}
				if (gravar && transacao) {
					iniciarAlteracao();
				}

				// Internos antigos devem usar sempre o modelo 778L
				Long backupID = null;
				if (doc.getExTipoDocumento().getIdTpDoc() == 2) {
					if (doc.getExModelo() != null)
						backupID = doc.getExModelo().getIdMod();
					doc.setExModelo(dao().consultarAtivoPorIdInicial(
							ExModelo.class,
							doc.isProcesso() ? SigaExProperties.getIdModPA()
									: SigaExProperties
											.getIdModInternoImportado()));
				}

				final String strHtml;
				try {
					strHtml = processarModelo(doc, null, "processar_modelo",
							null, null);
				} catch (Exception e) {
					throw new AplicacaoException(
							"Erro no processamento do modelo HTML.", 0, e);
				}

				// Restaurar o modelo do "Interno Antigo"
				if (doc.getExTipoDocumento().getIdTpDoc() == 2) {
					if (backupID != null) {
						doc.setExModelo(dao().consultar(backupID,
								ExModelo.class, false));
					} else {
						doc.setExModelo(null);
					}
				}

				doc.setConteudoBlobHtmlString(strHtml);

				final byte pdf[];
				AbstractConversorHTMLFactory fabricaConvHtml = AbstractConversorHTMLFactory
						.getInstance();
				ConversorHtml conversor = fabricaConvHtml.getConversor(
						getConf(), doc, strHtml);

				try {
					pdf = Documento.generatePdf(strHtml, conversor, realPath);
				} catch (Exception e) {
					throw new AplicacaoException(
							"Erro na geração do PDF. Por favor, verifique se existem recursos de formatação não suportados. Para eliminar toda a formatação do texto clique em voltar e depois, no editor, clique no botão de 'Selecionar Tudo' e depois no botão de 'Remover Formataçao'.",
							0, e);
				}
				doc.setConteudoBlobPdf(pdf);

				if (gravar) {
					doc.setNumPaginas(doc.getContarNumeroDePaginas());
					dao().gravar(doc);
					if (transacao) {
						concluirAlteracao(doc);
					}
				}
			}
		} catch (final AplicacaoException e) {
			if (gravar && transacao) {
				cancelarAlteracao();
			}
			throw e;
		} catch (final Exception e) {
			if (gravar && transacao) {
				cancelarAlteracao();
			}
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
	}

	public String processarModelo(final ExDocumento doc, String acao,
			Map<String, String> formParams, CpOrgaoUsuario orgaoUsu)
			throws Exception {
		return processarModelo(doc, null, acao, formParams, orgaoUsu);
	}

	public String processarModelo(final ExMovimentacao mov, final String acao,
			Map<String, String> formParams, CpOrgaoUsuario orgaoUsu)
			throws Exception {
		return processarModelo(mov.getExDocumento(), mov, acao, formParams,
				orgaoUsu);
	}

	private String processarModelo(ExDocumento doc, ExMovimentacao mov,
			String acao, Map<String, String> formParams, CpOrgaoUsuario orgaoUsu)
			throws Exception {
		Map<String, Object> attrs = new TreeMap<String, Object>();
		Map<String, Object> params = new TreeMap<String, Object>();
		ProcessadorModelo p = getProcessadorModeloJsp();

		// System.out.println(System.currentTimeMillis() + " - INI
		// processarModelo");

		// Parsear o registro de dados da entrevista que esta urlencoded
		// e gravar cada um dos valores no mapa de parametros
		if (formParams != null) {
			attrs.putAll(formParams);
		} else {
			if (doc.getExModelo() != null) {
				final byte[] form;
				if (mov == null)
					form = doc.getConteudoBlob("doc.form");
				else
					form = mov.getConteudoBlob("doc.form");
				mapFromUrlEncodedForm(attrs, form);
			}
		}
		if (acao != null)
			attrs.put(acao, "1");
		attrs.put("doc", doc);
		// rw.setAttribute("modelo", doc.getExModelo());
		if (mov == null) {
			if (doc.getExModelo() != null) {
				attrs.put("nmArqMod", doc.getExModelo().getNmArqMod());
				if ("template/freemarker".equals(doc.getExModelo()
						.getConteudoTpBlob())) {
					attrs.put("nmMod", doc.getExModelo().getNmMod());
					attrs.put("template", new String(doc.getExModelo()
							.getConteudoBlobMod2(), "utf-8"));
					p = processadorModeloFreemarker;
				}
			}
		} else {
			attrs.put("mov", mov);
			attrs.put("mob", mov.getExMobil());
			if (mov.getExTipoMovimentacao() != null
					&& mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME) {
				attrs.put("nmArqMod", "certidaoEncerramentoVolume.jsp");
			} else if (mov.getExTipoMovimentacao() != null
					&& (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA || (mov
							.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO)
							&& ExTipoMovimentacao.hasDocumento(mov
									.getExMovimentacaoRef().getIdTpMov()))) {
				attrs.put("nmArqMod", "certidaoDesentranhamento.jsp");
			} else {
				if (mov.getExTipoDespacho() != null) {
					attrs.put("despachoTexto", mov.getExTipoDespacho()
							.getDescTpDespacho());
				} else if (mov.getDescrMov() != null) {
					attrs.put("despachoTexto", mov.getDescrMov());
				} else if (attrs.get("conteudo") != null) {
					attrs.put("despachoTexto", attrs.get("conteudo"));
				}
				if (mov.getConteudoBlobHtmlString() != null) {
					attrs.put("despachoHtml", mov.getConteudoBlobHtmlString());
				}
				// attrs.put("nmArqMod", "despacho_mov.jsp");
				ExModelo m = dao().consultarExModelo(null,
						"Despacho Automático");
				attrs.put("nmMod", m.getNmMod());
				attrs.put("template", new String(m.getConteudoBlobMod2(),
						"utf-8"));

				p = processadorModeloFreemarker;
			}
		}

		// Nato: alterei essas linhas para que os modelos possam conhecer o
		// cadastrante e o titular
		// attrs.put("lotaTitular", doc.getLotaTitular());
		attrs.put("cadastrante", doc.getCadastrante());
		attrs.put("lotaCadastrante", doc.getLotaCadastrante());
		attrs.put("titular", doc.getTitular());
		attrs.put("lotaTitular", doc.getLotaTitular());

		params.put("processar_modelo", "1");
		params.put("finalizacao", "1");
		if (doc != null && doc.getIdDoc() != null)
			params.put("idDoc", doc.getIdDoc().toString());
		if (mov != null && mov.getIdMov() != null) {
			params.put("id", mov.getIdMov().toString());
		}
		CpOrgaoUsuario ou = orgaoUsu;
		if (doc != null)
			ou = doc.getOrgaoUsuario();
		if (mov != null && mov.getResp() != null
				&& mov.getResp().getOrgaoUsuario() != null)
			ou = mov.getResp().getOrgaoUsuario();
		String s = p.processarModelo(ou, attrs, params);

		// System.out.println(System.currentTimeMillis() + " - FIM
		// processarModelo");
		return s;
	}

	private void juntarAoDocumentoPai(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final ExMovimentacao mov) throws Exception,
			AplicacaoException {

		// for (int numVia = 1; numVia <= doc.getNumUltimaViaNaoCancelada();
		// numVia++)
		for (final ExMobil mob : doc.getExMobilSet()) {

			ExMovimentacao ultMov = mob.getUltimaMovimentacaoNaoCancelada();
			if (getComp().podeJuntar(ultMov.getCadastrante(),
					ultMov.getLotaCadastrante(), mob)
					& getComp().podeSerJuntado(ultMov.getCadastrante(),
							ultMov.getLotaCadastrante(), doc.getExMobilPai())) {
				juntarDocumento(ultMov.getCadastrante(), ultMov.getTitular(),
						ultMov.getLotaCadastrante(), null, mob,
						doc.getExMobilPai(), dtMov, ultMov.getSubscritor(),
						ultMov.getTitular(), "1");
				break;
			}
		}
	}

	private void juntarAoDocumentoAutuado(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final ExMovimentacao mov) throws Exception,
			AplicacaoException {

		// for (int numVia = 1; numVia <= doc.getNumUltimaViaNaoCancelada();
		// numVia++)
		for (final ExMobil mob : doc.getExMobilSet()) {
			ExMovimentacao ultMov = mob.getUltimaMovimentacaoNaoCancelada();
			if (getComp().podeJuntar(ultMov.getCadastrante(),
					ultMov.getLotaCadastrante(), doc.getExMobilAutuado())
					& getComp().podeSerJuntado(ultMov.getCadastrante(),
							ultMov.getLotaCadastrante(), mob)) {
				juntarDocumento(ultMov.getCadastrante(), ultMov.getTitular(),
						ultMov.getLotaCadastrante(), null,
						doc.getExMobilAutuado(), mob, dtMov,
						ultMov.getSubscritor(), ultMov.getTitular(), "1");
				break;
			}
		}
	}

	private ExMovimentacao criarNovaMovimentacao(final long idtpmov,
			final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExMobil mob, final Date dtMov, final DpPessoa subscritor,
			final DpLotacao lotaSubscritor, final DpPessoa titular,
			final DpLotacao lotaTitular, final Date dtOrNull)
			throws AplicacaoException {
		final ExMovimentacao mov;
		mov = new ExMovimentacao();

		final ExTipoMovimentacao tpmov = dao().consultar(idtpmov,
				ExTipoMovimentacao.class, false);

		final Date dt = dtOrNull == null ? dao().dt() : dtOrNull;

		mov.setCadastrante(cadastrante);
		mov.setLotaCadastrante(lotaCadastrante);
		if (lotaCadastrante == null && mov.getCadastrante() != null)
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());
		if (subscritor != null) {
			mov.setSubscritor(subscritor);
			mov.setLotaSubscritor(mov.getSubscritor().getLotacao());
		} else {
			if (idtpmov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
				mov.setSubscritor(null); /* o perfil(responsável) é uma lotação */
				mov.setLotaSubscritor(lotaSubscritor);
			} else {
				mov.setSubscritor(cadastrante);
				mov.setLotaSubscritor(mov.getLotaCadastrante());
			}
		}

		if (titular != null)
			mov.setTitular(titular);
		else
			mov.setTitular(subscritor);
		if (lotaTitular != null)
			mov.setLotaTitular(lotaTitular);
		else
			mov.setLotaTitular(mov.getLotaSubscritor());

		if (dtMov != null)
			mov.setDtMov(dtMov);
		else
			mov.setDtMov(dt);

		// A data de início da movimentação sempre será a data do servidor, não
		// a data que o usuário digitou
		mov.setDtIniMov(dao().dt());
		mov.setExMobil(mob);
		mov.setExTipoMovimentacao(tpmov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		if (ultMov != null) {
			// if (mov.getExMobilPai() == null)
			// mov.setExMobilPai(ultMov.getExMobilPai());
			if (mov.getExNivelAcesso() == null)
				mov.setExNivelAcesso(ultMov.getExNivelAcesso());
			if (mov.getExClassificacao() == null)
				mov.setExClassificacao(ultMov.getExClassificacao());
			if (mov.getLotaResp() == null)
				mov.setLotaResp(ultMov.getLotaResp());
			if (mov.getResp() == null)
				mov.setResp(ultMov.getResp());
			if (mov.getLotaDestinoFinal() == null)
				mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
			if (mov.getDestinoFinal() == null)
				mov.setDestinoFinal(ultMov.getDestinoFinal());
		}
		if (ultMov == null
				|| idtpmov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO) {
			if (mov.getLotaResp() == null)
				mov.setLotaResp(lotaCadastrante);
			if (mov.getResp() == null)
				mov.setResp(cadastrante);
		}
		return mov;
	}

	private ExMovimentacao criarNovaMovimentacaoTransferencia(final long idtpmov,
			final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExMobil mob, final Date dtMov, final Date dtFimMov, final DpPessoa subscritor,
			final DpLotacao lotaSubscritor, final DpPessoa titular,
			final DpLotacao lotaTitular, final Date dtOrNull)
					throws AplicacaoException {
		final ExMovimentacao mov;
		mov = new ExMovimentacao();

		final ExTipoMovimentacao tpmov = dao().consultar(idtpmov,
				ExTipoMovimentacao.class, false);

		final Date dt = dtOrNull == null ? dao().dt() : dtOrNull;

		mov.setCadastrante(cadastrante);
		mov.setLotaCadastrante(lotaCadastrante);
		if (lotaCadastrante == null && mov.getCadastrante() != null)
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());
		if (subscritor != null) {
			mov.setSubscritor(subscritor);
			mov.setLotaSubscritor(mov.getSubscritor().getLotacao());
		} else {
			if (idtpmov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
				mov.setSubscritor(null); /* o perfil(responsável) é uma lotação */
				mov.setLotaSubscritor(lotaSubscritor);
			} else {
				mov.setSubscritor(cadastrante);
				mov.setLotaSubscritor(mov.getLotaCadastrante());
			}
		}

		if (titular != null)
			mov.setTitular(titular);
		else
			mov.setTitular(subscritor);
		if (lotaTitular != null)
			mov.setLotaTitular(lotaTitular);
		else
			mov.setLotaTitular(mov.getLotaSubscritor());

		if (dtMov != null)
			mov.setDtMov(dtMov);
		else
			mov.setDtMov(dt);

		if (dtFimMov != null)
			mov.setDtFimMov(dtFimMov);
		else
			mov.setDtFimMov(dtFimMov);

		// A data de início da movimentação sempre será a data do servidor, não
		// a data que o usuário digitou
		mov.setDtIniMov(dao().dt());
		mov.setExMobil(mob);
		mov.setExTipoMovimentacao(tpmov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		if (ultMov != null) {
			// if (mov.getExMobilPai() == null)
			// mov.setExMobilPai(ultMov.getExMobilPai());
			if (mov.getExNivelAcesso() == null)
				mov.setExNivelAcesso(ultMov.getExNivelAcesso());
			if (mov.getExClassificacao() == null)
				mov.setExClassificacao(ultMov.getExClassificacao());
			if (mov.getLotaResp() == null)
				mov.setLotaResp(ultMov.getLotaResp());
			if (mov.getResp() == null)
				mov.setResp(ultMov.getResp());
			if (mov.getLotaDestinoFinal() == null)
				mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
			if (mov.getDestinoFinal() == null)
				mov.setDestinoFinal(ultMov.getDestinoFinal());
		}
		if (ultMov == null
				|| idtpmov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO) {
			if (mov.getLotaResp() == null)
				mov.setLotaResp(lotaCadastrante);
			if (mov.getResp() == null)
				mov.setResp(cadastrante);
		}
		return mov;
	}

	private void iniciarAlteracao() throws AplicacaoException {
		ExDao.iniciarTransacao();
	}

	private void concluirAlteracaoParcial(ExMobil mob) {
		SortedSet<ExMobil> set = threadAlteracaoParcial.get();
		if (set == null) {
			threadAlteracaoParcial.set(new TreeSet<ExMobil>());
			set = threadAlteracaoParcial.get();
		}
		if (mob.doc() != null) {
			atualizarMarcas(mob.doc());
			atualizarVariaveisDenormalizadas(mob.doc());
		}
		set.add(mob);
	}

	private void atualizarVariaveisDenormalizadas(ExDocumento doc) {
		atualizarDnmNivelAcesso(doc);
		atualizarDnmAcesso(doc);
	}

	private void concluirAlteracao(ExDocumento doc) throws Exception {
		if (doc != null) {
			atualizarMarcas(doc);
			atualizarVariaveisDenormalizadas(doc);
		}
		ExDao.commitTransacao();
		// if (doc != null)
		// atualizarWorkflow(doc, null);

		SortedSet<ExMobil> set = threadAlteracaoParcial.get();
		if (set != null && set.size() > 0) {
			for (ExMobil mob : set) {
				atualizarWorkflow(mob.doc(), null);
			}
			set.clear();
		} else {
			if (doc != null) {
				atualizarWorkflow(doc, null);
			}

		}
	}

	private void cancelarAlteracao() throws AplicacaoException {
		ExDao.rollbackTransacao();
		SortedSet<ExMobil> set = threadAlteracaoParcial.get();
		if (set != null)
			set.clear();
	}

	// Esse método deve ser chamado sempre que houver alteracao no documento.
	public void atualizarWorkflow(ExDocumento doc, ExMovimentacao mov)
			throws AplicacaoException {

		try {
			if (Contexto.resource("isWorkflowEnabled") != null
					&& Boolean.valueOf(String.valueOf(Contexto
							.resource("isWorkflowEnabled")))) {
				if (mov != null) {
					atualizarWorkFlow(mov);
				} else {
					atualizarWorkFlow(doc);
				}
			}
		} catch (NullPointerException nulpointer) {
			System.err.println("Não existe Contexto no ambiente");
		} catch (RuntimeException e) {
		}
	}

	public void atualizarWorkFlow(ExDocumento doc) throws AplicacaoException {
		try {
			Service.getWfService().atualizarWorkflowsDeDocumento(
					doc.getCodigo());
		} catch (Exception ex) {
			throw new AplicacaoException(
					"Erro ao tentar atualizar estado do workflow", 0, ex);
		}
	}

	public void atualizarWorkFlow(ExMovimentacao mov) throws AplicacaoException {
		try {
			Service.getWfService().atualizarWorkflowsDeDocumento(
					mov.getExMobil().getSigla());
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao tentar atualizar estado do workflow", 0, e);
		}
	}

	public List<ExDocumento> obterDocumentosBoletim(Map<String, String> form) {
		final Pattern p = Pattern.compile("^doc_boletim?([0-9]{1,7})");
		long l;
		List<ExDocumento> list = new ArrayList<ExDocumento>();
		ExDocumento doqueRef = new ExDocumento();
		for (String chave : form.keySet()) {
			final Matcher m = p.matcher(chave);
			if (m.find()) {
				if (m.group(1) != null && form.get(chave).equals("Sim")) {
					l = new Long(m.group(1));
					doqueRef = dao().consultar(l, ExDocumento.class, false);
					list.add(doqueRef);
				}
			}
		}
		return list;
	}

	public List<ExDocumento> obterDocumentosBoletim(ExDocumento doque) {
		return obterDocumentosBoletim(doque.getForm());

	}

	/**
	 * Obtem a lista de formas de documentos a partir dos modelos selecionados e
	 * das restrições de tipo (interno, externo) e de tipo da forma (expediente,
	 * processo)
	 * 
	 * @param modelos
	 * @param tipoDoc
	 * @param tipoForma
	 * @return
	 * @throws Exception
	 */
	public SortedSet<ExFormaDocumento> obterFormasDocumento(
			List<ExModelo> modelos, ExTipoDocumento tipoDoc,
			ExTipoFormaDoc tipoForma)

	throws Exception {
		SortedSet<ExFormaDocumento> formasSet = new TreeSet<ExFormaDocumento>();
		SortedSet<ExFormaDocumento> formasFinal = new TreeSet<ExFormaDocumento>();
		// Por enquanto, os parâmetros tipoForma e tipoDoc não podem ser
		// preenchidos simultaneamente. Melhorar isso.
		if (tipoDoc != null && tipoForma != null) {
			formasSet.addAll(SetUtils.intersection(
					tipoDoc.getExFormaDocumentoSet(),
					tipoForma.getExFormaDocSet()));
		} else if (tipoDoc != null)
			formasSet.addAll(tipoDoc.getExFormaDocumentoSet());
		else if (tipoForma != null)
			formasSet.addAll(tipoForma.getExFormaDocSet());
		else
			formasSet = null;

		for (ExModelo mod : modelos) {
			if (mod.getExFormaDocumento() == null)
				continue;
			if (formasSet == null
					|| formasSet.contains(mod.getExFormaDocumento()))
				formasFinal.add(mod.getExFormaDocumento());
		}

		return formasFinal;
	}

	public List<ExModelo> obterListaModelos(ExFormaDocumento forma,
			boolean despachando, String headerValue, boolean protegido,
			DpPessoa titular, DpLotacao lotaTitular, boolean autuando)
			throws Exception {
		ArrayList<ExModelo> modeloSetFinal = new ArrayList<ExModelo>();
		ArrayList<ExModelo> provSet;
		if (forma != null)
			modeloSetFinal = new ArrayList<ExModelo>(forma.getExModeloSet());
		else
			modeloSetFinal = (ArrayList) dao()
					.listarTodosModelosOrdenarPorNome(null);
		if (despachando) {
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal)
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod,
						CpTipoConfiguracao.TIPO_CONFIG_DESPACHAVEL))
					provSet.add(mod);
			modeloSetFinal = provSet;
		}
		if (autuando) {
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal)
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod,
						CpTipoConfiguracao.TIPO_CONFIG_AUTUAVEL))
					provSet.add(mod);
			modeloSetFinal = provSet;
		}
		if (protegido) {
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal) {
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod,
						CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					provSet.add(mod);
			}
			modeloSetFinal = provSet;
		}
		if (headerValue != null && modeloSetFinal.size() > 1) {
			ExModelo mod = new ExModelo();
			mod.setIdMod(0L);
			mod.setHisIdIni(0L);
			mod.setNmMod("[" + headerValue + "]");
			modeloSetFinal.add(0, mod);
		}
		return modeloSetFinal;
	}

	private boolean anexosAlternativosEstaoAssinados(ExDocumento doc) {
		// Verifica se o documento possui anexos alterantivos e se estes anexos
		// estão assinado
		for (ExMobil mob : doc.getExMobilSet()) {
			for (ExDocumento docFilho : mob.getExDocumentoFilhoSet()) {
				// Verifica se docFilho é do tipo anexo
				if (docFilho.getExFormaDocumento().getIdFormaDoc() == 55) {
					if (!docFilho.isCancelado() && !docFilho.isAssinado())
						return false;
				}
			}
		}
		return true;
	}

	public void gravarTermoEliminacao(ExDocumento doc) throws Exception {
		new ExTermoEliminacao(doc).gravar();
	}

	public void gravarEditalEliminacao(ExDocumento doc) throws Exception {
		new ExEditalEliminacao(doc).gravar();
	}

	public void incluirEmEditalEliminacao(ExDocumento edital, ExMobil mob)
			throws AplicacaoException {

		try {

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
					edital.getCadastrante(), edital.getLotaCadastrante(), mob,
					null, edital.getSubscritor(), edital.getLotaSubscritor(),
					edital.getTitular(), edital.getLotaTitular(), null);

			mov.setExMobilRef(edital.getMobilGeral());

			gravarMovimentacao(mov);

			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao incluir em edital de eliminação.", 0, e);
		}

	}

	public void retirarDeEditalEliminacao(ExMobil mob, DpPessoa cadastrante,
			DpLotacao lotaCadastrante, DpPessoa subscritor,
			DpLotacao lotaSubscritor, DpPessoa titular, DpLotacao lotaTitular,
			String descrMov) throws AplicacaoException {

		try {

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO,
					cadastrante, lotaCadastrante, mob, null, subscritor,
					lotaSubscritor, titular, lotaTitular, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);

			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao retirar do edital de eliminação.", 0, e);
		}

	}

	public void gravarBI(ExDocumento doc) throws Exception {
		final List<ExDocumento> documentosPublicar = obterDocumentosBoletim(doc);
		final List<ExDocumento> documentosNaoPublicar = obterDocumentosNaoVaoBoletim(doc
				.getForm());

		ExBoletimDoc boletim;
		ExDao exDao = ExDao.getInstance();

		for (ExDocumento docPubl : documentosPublicar) {

			if (docPubl
					.getMobilGeral()
					.getMovimentacoesPorTipo(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI)
					.size() > 0)

				throw new AplicacaoException(
						"O documento "
								+ docPubl.getCodigo()
								+ " já foi publicado em outro boletim. Retire esse documento da lista de documentos a publicar e entre em contato com a equipe de suporte do siga-doc.");

			boletim = exDao.consultarBoletimPorDocumento(docPubl);
			boletim.setBoletim(doc);
			exDao.gravar(boletim);
		}

		for (ExDocumento docPubl : documentosNaoPublicar) {
			boletim = exDao.consultarBoletimPorDocumento(docPubl);
			if (boletim.getBoletim() != null
					&& boletim.getBoletim().getIdDoc() == doc.getIdDoc())
				boletim.setBoletim(null);
			exDao.gravar(boletim);
		}
	}

	public void excluirBI(ExDocumento doc) throws Exception {
		ExDao exDao = ExDao.getInstance();

		final List<ExBoletimDoc> documentosDoBoletim = exDao
				.consultarBoletimPorBoletim(doc);
		for (ExBoletimDoc docBol : documentosDoBoletim) {
			docBol.setBoletim(null);
			exDao.gravar(docBol);
		}
	}

	public void cancelarPedidoBI(ExDocumento doc) throws Exception {
		ExBoletimDoc boletim;
		ExDao exDao = ExDao.getInstance();

		boletim = exDao.consultarBoletimPorDocumento(doc);
		exDao.excluir(boletim);
	}

	public void refazerBI(ExDocumento doc) throws Exception {
		final List<ExDocumento> documentosPublicar = obterDocumentosBoletim(doc);

		ExBoletimDoc boletim;
		ExDao exDao = ExDao.getInstance();

		for (ExDocumento docPubl : documentosPublicar) {
			boletim = exDao.consultarBoletimPorDocumento(docPubl);
			boletim.setBoletim(doc);
			exDao.gravar(boletim);
		}
	}

	public void finalizarBI(ExDocumento doc) throws Exception {
		final List<ExDocumento> documentosPublicar = obterDocumentosBoletim(doc);
		ExDao exDao = ExDao.getInstance();

		for (ExDocumento exDoc : documentosPublicar) {
			ExBoletimDoc boletim = exDao.consultarBoletimPorDocumento(exDoc);
			if (boletim == null) {
				throw new AplicacaoException(
						"Foi cancelada a solicitação de pedido de publicação do documento "
								+ exDoc.getCodigo()
								+ ". Por favor queira retorna à tela de edição de documento para uma nova conferência.");
			} else if (boletim.getBoletim() == null) {
				throw new AplicacaoException(
						"O documento "
								+ exDoc.getCodigo()
								+ " foi retirado da lista de documentos deste boletim"
								+ ". Por favor queira retorna à tela de edição de documento para uma nova conferência.");
			} else if (boletim.getBoletim() != doc) {
				throw new AplicacaoException(
						"O documento "
								+ exDoc.getCodigo()
								+ " já consta da lista de documentos do boletim "
								+ boletim.getBoletim().getCodigo()
								+ ". Por favor queira retorna à tela de edição de documento para uma nova conferência.");

			}
		}
	}

	public void obterMetodoPorString(String metodo, ExDocumento doc)
			throws Exception {
		if (metodo != null) {
			final Class[] classes = new Class[] { ExDocumento.class };

			ExBL exBl = Ex.getInstance().getBL();
			final Method method = ExBL.class.getDeclaredMethod(metodo, classes);
			method.invoke(exBl, new Object[] { doc });
		}
	}

	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception {

		ExArquivo arq = buscarPorNumeroAssinatura(num);

		Documento documento = new Documento();

		if (arq instanceof ExDocumento)
			return documento.getDocumento(((ExDocumento) arq).getMobilGeral(),
					null);
		if (arq instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) arq;
			return documento.getDocumento(mov.getExMobil(), mov);
		}

		return null;

	}

	public ExArquivo buscarPorNumeroAssinatura(String num) throws Exception {
		Pattern p = Pattern
				.compile("([0-9]{1,10})(.[0-9]{1,10})?-([0-9]{1,4})");
		Matcher m = p.matcher(num);

		if (!m.matches())
			throw new AplicacaoException("Número inválido");

		Long idDoc = Long.parseLong(m.group(1));

		ExDocumento doc = ExDao.getInstance().consultar(idDoc,
				ExDocumento.class, false);

		if (doc == null)
			throw new AplicacaoException("Documento não encontrado");

		// Testa se o documento existe na base
		try {
			doc.getDescrCurta();
		} catch (ObjectNotFoundException e) {
			throw new AplicacaoException("Documento não encontrado", 0, e);
		}

		/*
		 * if (doc.getExNivelAcesso().getGrauNivelAcesso() > 20) throw new
		 * Exception("Documento sigiloso");
		 */

		int hash = Integer.parseInt(m.group(3));

		ExMovimentacao move = null;

		if (Math.abs(doc.getDescrCurta().hashCode() % 10000) == hash) {
			return doc;
		} else {
			for (ExMovimentacao mov : doc.getExMovimentacaoSet())
				if (Math.abs((doc.getDescrCurta() + mov.getIdMov()).hashCode() % 10000) == hash
				|| Math.abs((doc.getDescrCurta() + mov.getIdMov() + "AssinaturaExterna")
						.hashCode() % 10000) == hash)
					move = mov; 
			if (move == null)
				throw new AplicacaoException("Número inválido");

			return move;
		}

	}

	public List<ExDocumento> obterDocumentosNaoVaoBoletim(
			Map<String, String> form) {
		final Pattern p = Pattern.compile("^doc_boletim?([0-9]{1,7})");
		long l;
		List<ExDocumento> list = new ArrayList<ExDocumento>();
		ExDocumento doqueRef = new ExDocumento();
		for (String chave : form.keySet()) {
			final Matcher m = p.matcher(chave);
			if (m.find()) {
				if (m.group(1) != null && form.get(chave).equals("Nao")) {
					l = new Long(m.group(1));
					doqueRef = ExDao.getInstance().consultar(l,
							ExDocumento.class, false);
					list.add(doqueRef);
				}
			}
		}
		return list;
	}

	public void apensarDocumento(final DpPessoa cadastrante,
			final DpPessoa docTitular, final DpLotacao lotaCadastrante,
			final ExMobil mob, final ExMobil mobMestre, final Date dtMov,
			final DpPessoa subscritor, final DpPessoa titular) throws Exception {

		if (mobMestre == null)
			throw new AplicacaoException(
					"Não foi selecionado um documento para a apensação");

		if (mob.getExDocumento().getIdDoc()
				.equals(mobMestre.getExDocumento().getIdDoc())
				&& mob.getNumSequencia().equals(mobMestre.getNumSequencia())
				&& mob.getExTipoMobil().getIdTipoMobil()
						.equals(mobMestre.getExTipoMobil().getIdTipoMobil()))
			throw new AplicacaoException(
					"Não é possível apensar um documento a ele mesmo");

		if (!mobMestre.getExDocumento().isFinalizado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento não finalizado");

		if (!mobMestre.doc().isAssinado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento não finalizado");

		if (mobMestre.isGeral())
			throw new Exception(
					"[E necessário definir a via ou volume do documento ao qual se quer apensar");

		if (!mobMestre.doc().isAssinado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento não finalizado");

		if (mobMestre.isArquivado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento arquivado");

		if (mobMestre.isSobrestado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento Sobrestado");

		if (mobMestre.isJuntado())
			throw new AplicacaoException(
					"Não é possível apensar a um documento juntado");

		if (!getComp().podeMovimentar(cadastrante, lotaCadastrante, mobMestre)
				|| !mob.estaNaMesmaLotacao(mobMestre))
			throw new AplicacaoException(
					"Não é possível apensar a um documento que esteja em outra lotação");

		if (mobMestre.isEmTransito())
			throw new AplicacaoException(
					"Não é possível apensar a um documento em trânsito");

		if (mobMestre.isCancelada())
			throw new AplicacaoException(
					"Não é possível apensar a um documento cancelado");

		if (!mob.isVolumeEncerrado() && mobMestre.isVolumeEncerrado())
			throw new AplicacaoException(
					"Não é possível apensar um volume aberto a um volume encerrado");

		for (ExMobil apenso : mobMestre.getMobilETodosOsApensos(false)) {
			if (apenso.getIdMobil() == mob.getIdMobil()) {
				throw new AplicacaoException(
						"Não é possível apensar ao documento "
								+ mobMestre.getSigla()
								+ ", pois este já está apensado ao documento "
								+ mob.getSigla());
			}
		}

		if (!getComp().podeApensar(docTitular, lotaCadastrante, mob))
			throw new AplicacaoException(
					"A apensação do documento não ser realizada porque ele está "
							+ "em transito, "
							+ "em transito externo, "
							+ "cancelado ou "
							+ "em local diferente da lotação em que se encontra o documento ao qual se quer apensar");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			mov.setExMobilRef(mobMestre);

			mov.setDescrMov("Apensado ao documento "
					+ mov.getExMobilRef().getCodigo().toString());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao apensar documento.", 0, e);
		}
	}

	/**
	 * Após desapensar o mobil, copiamos do grande mestre o responsável e sua
	 * lotação para o mobil em questão, de modo que nem todas as movimentações
	 * do grande mestre tenham que ser copiadas para todos os mobiles. Em
	 * especial, a transferência e o recebimento só será copiado quando não se
	 * tratar de volume processual apensado ao próximo.
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param subscritor
	 * @param titular
	 * @throws AplicacaoException
	 */
	public void desapensarDocumento(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular)
			throws AplicacaoException {

		if (!mob.isApensado())
			throw new AplicacaoException(
					"Impossível desapensar documento que não está apensado.");

		try {
			iniciarAlteracao();

			ExMobil mobMestre = mob.getGrandeMestre();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			final ExMovimentacao ultMov = mob.getUltimaMovimentacao();

			ExMovimentacao movRef = mov.getExMobil().getUltimaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO);
			mov.setExMovimentacaoRef(movRef);
			mov.setExMobilRef(movRef.getExMobilRef());
			mov.setDescrMov("Desapensado do documento "
					+ mov.getExMobilRef().getCodigo().toString());

			mov.setLotaResp(mobMestre.getUltimaMovimentacaoNaoCancelada()
					.getLotaResp());
			mov.setResp(mobMestre.getUltimaMovimentacaoNaoCancelada().getResp());

			gravarMovimentacao(mov);
			concluirAlteracao(mov.getExDocumento());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao desapensar.", 0, e);
		}

	}

	public void encerrarVolumeAutomatico(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov)
			throws AplicacaoException, Exception {

		if (mob.doc().isEletronico()) {
			dao().getSessao().refresh(mob);
			// Verifica se é Processo e conta o número de páginas para verificar
			// se tem que encerrar o volume
			if (mob.doc().isProcesso()) {
				if (mob.getTotalDePaginasSemAnexosDoMobilGeral() >= 200) {
					encerrarVolume(cadastrante, lotaCadastrante, mob, dtMov,
							null, null, null, true);
				}
			}
		}
	}

	/**
	 * Encerra um volume e insere uma certidão de encerramento de volume, que
	 * deve ser produzida, tanto em html quanto em pdf.
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param subscritor
	 * @param titular
	 * @param nmFuncaoSubscritor
	 * @throws AplicacaoException
	 * @throws Exception
	 */
	public void encerrarVolume(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, String nmFuncaoSubscritor,
			boolean automatico) throws AplicacaoException, Exception {

		if (mob.isVolumeEncerrado())
			throw new AplicacaoException(
					"Não é permitido encerrar um volume já encerrado");

		if (!mob.isVolume())
			throw new AplicacaoException(
					"Esta operação somente pode ser realizada em volumes.");

		// if (responsavel == null && lotaResponsavel == null)
		// throw new AplicacaoException(
		// "Não foram informados dados para o despacho/transferência");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null,
					titular, null, null);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);

			// Gravar o Html
			final String strHtml = processarModelo(mov, "processar_modelo",
					null, null);
			mov.setConteudoBlobHtmlString(strHtml);

			// Gravar o Pdf
			final byte pdf[] = Documento.generatePdf(strHtml);
			mov.setConteudoBlobPdf(pdf);
			mov.setConteudoTpMov("application/zip");

			if (automatico)
				mov.setDescrMov("Volume encerrado automaticamente.");

			gravarMovimentacao(mov);
			concluirAlteracao(mob.doc());
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao encerrar volume.", 0, e);
		}
	}

	public String verificarAssinatura(byte[] conteudo, byte[] assinatura,
			String mimeType, Date dtAssinatura) throws Exception {
		String sNome;
		Long lCPF;

		// try {
		CdService client = Service.getCdService();

		sNome = client.validarAssinatura(assinatura, conteudo, dtAssinatura,
				VALIDAR_LCR);
		Service.throwExceptionIfError(sNome);

		String sCPF = client.recuperarCPF(assinatura);
		Service.throwExceptionIfError(sCPF);
		lCPF = Long.valueOf(sCPF);

		return sNome;

		// sNome = AssinaturaDigital.verificarAssinatura(movAlvo
		// .getConteudoBlobpdf(), assinatura, null);
		// } catch (final Exception e) {
		// throw new AplicacaoException("Erro na validação da assinatura. "
		// + e.getMessage(), 0, e);
		// }
	}

	public void gravarModelo(ExModelo modNovo, ExModelo modAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		if (modNovo.getExFormaDocumento() == null)
			throw new AplicacaoException(
					"Não é possível salvar um modelo sem informar a forma do documento.");
		if (modNovo.getNmMod() == null
				|| modNovo.getNmMod().trim().length() == 0)
			throw new AplicacaoException(
					"Não é possível salvar um modelo sem informar o nome.");
		if (modNovo.getDescMod() == null
				|| modNovo.getDescMod().trim().length() == 0)
			throw new AplicacaoException(
					"Não é possível salvar um modelo sem informar a descrição.");
		try {
			ExDao.iniciarTransacao();
			dao().gravarComHistorico(modNovo, modAntigo, dt,
					identidadeCadastrante);
			ExDao.commitTransacao();
		} catch (Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Erro ao salvar um modelo.", 0, e);
		}
	}
	
	public void gravarForma(ExFormaDocumento forma) throws AplicacaoException {
		if (forma.getDescrFormaDoc() == null || forma.getDescrFormaDoc().isEmpty())
			throw new AplicacaoException(
					"Não é possível salvar um tipo sem informar a descrição.");
		if (forma.getExTipoFormaDoc() == null)
			throw new AplicacaoException(
					"Não é possível salvar um tipo sem informar se é processo ou expediente.");
		if (!forma.isSiglaValida())
			throw new AplicacaoException(
					"Sigla inválida. A sigla deve ser formada por 3 letras.");
		
		ExFormaDocumento formaConsulta = dao().consultarPorSigla(forma);
		if((forma.getIdFormaDoc() == null && formaConsulta != null) ||
				(forma.getIdFormaDoc() != null && formaConsulta != null && !formaConsulta.getIdFormaDoc().equals(forma.getIdFormaDoc())))
			throw new AplicacaoException(
					"Esta sigla já está sendo utilizada.");
		
		try {
			ExDao.iniciarTransacao();
			dao().gravar(forma);
			ExDao.commitTransacao();
		} catch (Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Erro ao salvar um tipo.", 0, e);
		}
	}	

	public void DesfazerCancelamentoDocumento(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {

		try {
			SortedSet<ExMobil> set = doc.getExMobilSet();

			iniciarAlteracao();

			for (ExMobil mob : set) {

				List<ExMovimentacao> movsCanceladas = mob
						.getMovimentacoesCanceladas();

				for (ExMovimentacao movARecuperar : movsCanceladas) {
					ExMovimentacao movCanceladora = movARecuperar
							.getExMovimentacaoCanceladora();

					movARecuperar.setExMovimentacaoCanceladora(null);

					gravarMovimentacao(movARecuperar);

					final ExMovimentacao novaMov = criarNovaMovimentacao(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
							cadastrante, lotaCadastrante, mob, null, null,
							null, null, null, null);

					novaMov.setExMovimentacaoRef(movCanceladora);

					gravarMovimentacaoCancelamento(novaMov, movCanceladora);
				}
				concluirAlteracaoParcial(mob);
			}

			concluirAlteracao(null);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao anular cancelamento do documento.", 0, e);
		}
	}

	public void TornarDocumentoSemEfeito(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc, String motivo)
			throws Exception {
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), null,
					null, null, null, null, null);

			mov.setDescrMov(motivo);
			gravarMovimentacao(mov);

			concluirAlteracao(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException(
					"Erro ao tornar o documento sem efeito.", 0, e);
		}
	}

	public void alterarExClassificacao(ExClassificacao exClassNovo,
			ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		verificarDuplicacaoTermoCompleto(exClassNovo, exClassAntigo);
		try {
			exClassNovo.setClassificacoesPosteriores(null);
			dao().gravarComHistorico(exClassNovo, exClassAntigo, dt,
					identidadeCadastrante);
			copiarReferencias(exClassNovo, exClassAntigo, dt,
					identidadeCadastrante);

		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao copiar as propriedades do modelo anterior. "
							+ e.getMessage());
		}

	}

	public void copiarReferencias(ExClassificacao exClassNova,
			ExClassificacao exClassAntiga, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		copiarVias(exClassNova, exClassAntiga, dt, identidadeCadastrante);
		copiarModelos(exClassNova, exClassAntiga, dt, identidadeCadastrante);
	}

	private void copiarModelos(ExClassificacao exClassNovo,
			ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			// classificacao geral
			for (ExModelo modAntigo : exClassAntigo.getExModeloSet()) {
				ExModelo modNovo = new ExModelo();

				PropertyUtils.copyProperties(modNovo, modAntigo);
				modNovo.setIdMod(null);
				modNovo.setExClassificacao(exClassNovo);

				dao().gravarComHistorico(modNovo, modAntigo, dt,
					    identidadeCadastrante);
				if (exClassNovo.getExModeloSet() == null) {
					exClassNovo.setExModeloSet(new HashSet<ExModelo>());
				}
				exClassNovo.getExModeloSet().add(modNovo);

			}

			// classificacao criacao via
			for (ExModelo modAntigo : exClassAntigo.getExModeloCriacaoViaSet()) {
				ExModelo modNovo = new ExModelo();

				PropertyUtils.copyProperties(modNovo, modAntigo);
				modNovo.setIdMod(null);
				modNovo.setExClassCriacaoVia(exClassNovo);

				dao().gravarComHistorico(modNovo, modAntigo, dt,
						identidadeCadastrante);
				if (exClassNovo.getExModeloCriacaoViaSet() == null) {
					exClassNovo
							.setExModeloCriacaoViaSet(new HashSet<ExModelo>());
				}
				exClassNovo.getExModeloCriacaoViaSet().add(modNovo);

			}

		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível fazer cópia dos modelos!");
		}
	}

	private void copiarVias(ExClassificacao exClassNovo,
			ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			for (ExVia viaAntiga : exClassAntigo.getExViaSet()) {
				if (viaAntiga.isAtivo()) {
					ExVia viaNova = new ExVia();

					PropertyUtils.copyProperties(viaNova, viaAntiga);
					viaNova.setId(null);
					viaNova.setExClassificacao(exClassNovo);

					dao().gravarComHistorico(viaNova, viaAntiga, dt,
							identidadeCadastrante);
					if (exClassNovo.getExViaSet() == null) {
						exClassNovo.setExViaSet(new HashSet<ExVia>());
					}
					exClassNovo.getExViaSet().add(viaNova);
				}

			}
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível fazer cópia das vias!");
		}
	}

	public void moverClassificacao(ExClassificacao exClassDest,
			ExClassificacao exClassOrigem, CpIdentidade identidadeCadastrante)
			throws AplicacaoException {

		Date dt = dao().consultarDataEHoraDoServidor();
		MascaraUtil m = MascaraUtil.getInstance();
		ExDao dao = ExDao.getInstance();

		List<ExClassificacao> filhos = dao.consultarFilhos(exClassOrigem, true);

		if (filhos.size() > 0
				&& m.calcularNivel(exClassDest.getCodificacao()) > m
						.calcularNivel(exClassOrigem.getCodificacao())) {
			throw new AplicacaoException(
					"O nível do destino é maior do que o nível da origem! Os filhos não caberão na hierarquia da classificação documental!");
		}

		ExClassificacao classExistente = dao().consultarPorSigla(exClassDest);
		if (classExistente != null) {
			throw new AplicacaoException(
					"A classificação destino já existe! <br/><br/>"
							+ classExistente.getCodificacao());
		}

		String mascaraDestino = m.getMscFilho(exClassDest.getCodificacao(),
				true);
		for (ExClassificacao f : filhos) {
			String novaCodificacao = m.substituir(f.getCodificacao(),
					mascaraDestino);
			ExClassificacao fNovo = getCopia(f);
			fNovo.setCodificacao(novaCodificacao);
			Ex.getInstance()
					.getBL()
					.alterarExClassificacao(fNovo, f, dt, identidadeCadastrante);
		}

		exClassDest.setHisIdIni(exClassOrigem.getHisIdIni());
		Ex.getInstance()
				.getBL()
				.alterarExClassificacao(exClassDest, exClassOrigem, dt,
						identidadeCadastrante);
	}

	public ExClassificacao getCopia(ExClassificacao exClassOrigem)
			throws AplicacaoException {
		ExClassificacao exClassCopia = new ExClassificacao();
		try {

			PropertyUtils.copyProperties(exClassCopia, exClassOrigem);

			// novo id
			exClassCopia.setId(null);
			exClassCopia.setHisDtFim(null);
			exClassCopia.setHisDtIni(null);
			exClassCopia.setAtivo();
			
			// objeto collection deve ser diferente (mas com mesmos elementos),
			// senão ocorre exception
			// HibernateException:Found shared references to a collection
			Set<ExVia> setExVia = new HashSet<ExVia>();
			exClassCopia.setExViaSet(setExVia);

			Set<ExModelo> setExModelo = new HashSet<ExModelo>();
			exClassCopia.setExModeloSet(setExModelo);

			Set<ExModelo> setExModeloCriacaoVia = new HashSet<ExModelo>();
			exClassCopia.setExModeloCriacaoViaSet(setExModeloCriacaoVia);

			Set<ExClassificacao> setPosteriores = new HashSet<ExClassificacao>();
			exClassCopia.setClassificacoesPosteriores(setPosteriores);

		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao copiar as propriedades do modelo anterior.");
		}

		return exClassCopia;
	}

	public void incluirExClassificacao(ExClassificacao exClass,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		verificarDuplicacaoTermoCompleto(exClass, null);
		dao().gravarComHistorico(exClass, null,
				dao().consultarDataEHoraDoServidor(), identidadeCadastrante);

	}

	private void verificarDuplicacaoTermoCompleto(ExClassificacao exClassNovo,
			ExClassificacao exClassAntigo) throws AplicacaoException {

		MascaraUtil m = MascaraUtil.getInstance();
		String mascara = m.getMscTodosDoNivel(m.calcularNivel(exClassNovo
				.getCodificacao()));
		List<ExClassificacao> exClassMesmoNivel = dao()
				.consultarExClassificacao(mascara,
						exClassNovo.getDescrClassificacao());
		if (exClassMesmoNivel.size() > 0) {
			for (ExClassificacao exClassConflito : exClassMesmoNivel) {

				if (exClassNovo.getDescricao().equalsIgnoreCase(
						exClassConflito.getDescricao())) {
					// se conflito não causado por movimentacao onde a própria
					// classificacao é o conflito
					if (exClassAntigo == null
							|| !exClassConflito.getCodificacao().equals(
									exClassAntigo.getCodificacao())) {
						throw new AplicacaoException(
								"Termo da classificação em conflito! <br/><br/>"
										+ exClassConflito
												.getDescricaoCompleta());
					}

				}

			}
		}

	}

	public void incluirExTemporalidade(ExTemporalidade exTemporalidade,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		ExDao.getInstance().gravarComHistorico(exTemporalidade, null, null,
				identidadeCadastrante);
	}

	public ExTemporalidade getCopia(ExTemporalidade exTempOrigem)
			throws AplicacaoException {
		ExTemporalidade exTempCopia = new ExTemporalidade();
		try {

			PropertyUtils.copyProperties(exTempCopia, exTempOrigem);

			// novo id
			exTempCopia.setId(null);
			exTempCopia.setHisDtFim(null);
			exTempCopia.setHisDtIni(null);
			exTempCopia.setAtivo();
			// objeto collection deve ser diferente (mas com mesmos elementos),
			// senão ocorre exception
			// HibernateException:Found shared references to a collection
			Set<ExVia> setExViaArqCorr = new HashSet<ExVia>();
			Set<ExVia> setExViaArqInterm = new HashSet<ExVia>();
			exTempCopia.setExViaArqCorrenteSet(setExViaArqCorr);
			exTempCopia.setExViaArqIntermediarioSet(setExViaArqInterm);

		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao copiar as propriedades do objeto ExTemporalidade.");
		}

		return exTempCopia;

	}

	public void alterarExTemporalidade(ExTemporalidade exTempNovo,
			ExTemporalidade exTempAntiga, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {

		dao().gravarComHistorico(exTempNovo, exTempAntiga, dt,
				identidadeCadastrante);

		// copiar Referências arq corrente
		try {
			for (ExVia viaAntiga : exTempAntiga.getExViaArqCorrenteSet()) {
				ExVia viaNova = new ExVia();

				PropertyUtils.copyProperties(viaNova, viaAntiga);
				viaNova.setIdVia(null);
				viaNova.setTemporalidadeCorrente(exTempNovo);

				dao().gravarComHistorico(viaNova, viaAntiga, dt,
						identidadeCadastrante);
				if (exTempNovo.getExViaArqCorrenteSet() == null) {
					exTempNovo.setExViaArqCorrenteSet(new HashSet<ExVia>());
				}
				exTempNovo.getExViaArqCorrenteSet().add(viaNova);

			}
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível fazer cópia das vias em arquivo corrente!");
		}

		// copiar Referências arq intermediário
		try {
			for (ExVia viaAntiga : exTempAntiga.getExViaArqIntermediarioSet()) {
				ExVia viaNova = new ExVia();

				PropertyUtils.copyProperties(viaNova, viaAntiga);
				viaNova.setIdVia(null);
				viaNova.setTemporalidadeIntermediario(exTempNovo);

				dao().gravarComHistorico(viaNova, viaAntiga, dt,
						identidadeCadastrante);
				if (exTempNovo.getExViaArqIntermediarioSet() == null) {
					exTempNovo
							.setExViaArqIntermediarioSet(new HashSet<ExVia>());
				}
				exTempNovo.getExViaArqIntermediarioSet().add(viaNova);

			}
		} catch (Exception e) {
			throw new AplicacaoException(
					"Não foi possível fazer cópia das vias em arquivo intermediário!");
		}

	}

	public ExModelo getCopia(ExModelo exModOrigem) throws AplicacaoException {
		ExModelo exModCopia = new ExModelo();
		try {

			PropertyUtils.copyProperties(exModCopia, exModOrigem);

			// novo id
			exModCopia.setId(null);
			exModCopia.setHisDtFim(null);
			exModCopia.setHisDtIni(null);
			exModCopia.setAtivo();

		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao copiar as propriedades do modelo anterior.");
		}

		return exModCopia;
	}

	public void excluirExClassificacao(ExClassificacao exClass,
			CpIdentidade idCadastrante) {
		Date dt = dao().consultarDataEHoraDoServidor();
		if (exClass.getExModeloSet().size() > 0
				|| exClass.getExModeloCriacaoViaSet().size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (ExModelo m : exClass.getExModeloSet()) {
				sb.append("(");
				sb.append(m.getId());
				sb.append(") ");
				sb.append(m.getNmMod());
				sb.append("<br/>");
			}
			for (ExModelo m : exClass.getExModeloCriacaoViaSet()) {
				sb.append("(");
				sb.append(m.getId());
				sb.append(") ");
				sb.append(m.getNmMod());
				sb.append(" (Criação de via)");
				sb.append("<br/>");
			}

			throw new AplicacaoException(
					"Não é possível excluir a classificação documental, pois está associada ao(s) seguinte(s) modelo(s):<br/><br/>"
							+ sb.toString());
		}

		/*
		 * AVISO:
		 * 
		 * 
		 * O código abaixo foi comentado para permitir a atualização da tabela
		 * de classificação documental enquanto a funcionalidade de
		 * reclassificação de documentos nõa está disponível.
		 */

		/*
		 * List<ExDocumento> docs = ExDao.getInstance()
		 * .consultarExDocumentoPorClassificacao(null,
		 * MascaraUtil.getInstance().getMscTodosDoMaiorNivel(),
		 * idCadastrante.getPessoaAtual().getOrgaoUsuario()); if (docs.size() >
		 * 0) { StringBuffer sb = new StringBuffer();
		 * 
		 * throw new AplicacaoException(
		 * "Não é possível excluir a classificação documental, pois já foi associada a documento(s)."
		 * + sb.toString()); }
		 */

		for (ExVia exVia : exClass.getExViaSet()) {
			dao().excluirComHistorico(exVia, dt, idCadastrante);
		}
		dao().excluirComHistorico(exClass, dt, idCadastrante);
	}

	public void incluirCosignatariosAutomaticamente(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc)
			throws Exception {

		final List<DpPessoa> cosignatariosDaEntrevista = obterCosignatariosDaEntrevista(doc
				.getForm());

		if (cosignatariosDaEntrevista != null
				&& !cosignatariosDaEntrevista.isEmpty()) {

			if (doc.getCosignatarios() != null
					&& !doc.getCosignatarios().isEmpty())
				excluirCosignatariosAutomaticamente(cadastrante,
						lotaCadastrante, doc);

			for (DpPessoa cosignatario : cosignatariosDaEntrevista) {

				incluirCosignatario(cadastrante, lotaCadastrante, doc, null,
						cosignatario, null);
			}
		}
	}

	public void excluirCosignatariosAutomaticamente(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc)
			throws Exception {

		for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
					&& m.getExMovimentacaoCanceladora() == null) {

				excluirMovimentacao(cadastrante, lotaCadastrante,
						doc.getMobilGeral(), m.getIdMov());
			}
		}
	}

	public List<DpPessoa> obterCosignatariosDaEntrevista(
			Map<String, String> docForm) {
		List<DpPessoa> list = new ArrayList<DpPessoa>();
		ExDao exDao = ExDao.getInstance();

		for (String chave : docForm.keySet()) {

			DpPessoa pessoa;

			if (chave.contains("cosignatarioSel.sigla")) {
				String valor = docForm.get(chave);

				if (valor != null) {

					pessoa = exDao.getPessoaFromSigla(valor);

					if (pessoa != null)
						list.add(pessoa);
				}
			}
		}
		return list;
	}

	private static class BlobSerializer implements
			JsonSerializer<java.sql.Blob>, JsonDeserializer<java.sql.Blob> {
		public JsonElement serialize(java.sql.Blob src, Type srcType,
				JsonSerializationContext context) {
			String s = null;
			byte ab[] = br.gov.jfrj.siga.cp.util.Blob.toByteArray(src);
			if (ab != null)
				s = Base64.encode(ab);
			return new JsonPrimitive(s);
		}

		public Blob deserialize(JsonElement json, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			String s = json.getAsString();
			if (s != null) {
				byte ab[] = Base64.decode(s);
				return HibernateUtil.getSessao().getLobHelper().createBlob(ab);
			}
			return null;
		}
	}

	private static class HibernateProxySerializer implements
			JsonSerializer<HibernateProxy>, JsonDeserializer<HibernateProxy> {
		private Gson gson;

		public JsonElement serialize(HibernateProxy src, Type srcType,
				JsonSerializationContext context) {
			Object obj = ((HibernateProxy) src).getHibernateLazyInitializer()
					.getImplementation();

			return gson.toJsonTree(obj);
		}

		public void setGson(Gson gson) {
			this.gson = gson;
		}

		public HibernateProxy deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static class ObjetoBaseSerializer implements
			JsonSerializer<ObjetoBase>, JsonDeserializer<ObjetoBase> {
		private Gson gson;

		public JsonElement serialize(ObjetoBase src, Type srcType,
				JsonSerializationContext context) {

			return gson.toJsonTree(src);
		}

		public void setGson(Gson gson) {
			this.gson = gson;
		}

		public ObjetoBase deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static Object getImplementationDeep(Object obj) throws Exception {
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getImplementation();
		}
		prune((Objeto) obj);
		getImplementationDeep(obj, obj.getClass(), new HashSet<Objeto>());
		return obj;
	}

	public static Object getImplementationDeep(Object o, Class clazz,
			HashSet<Objeto> set) throws Exception, IllegalAccessException {
		Field f[] = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(f, true);

		for (int i = 0; i < f.length; i++) {
			final Object object = f[i].get(o);
			if (object instanceof ObjetoBase) {
				Objeto objeto = (Objeto) object;

				if (objeto instanceof HibernateProxy) {
					objeto = (Objeto) (((HibernateProxy) objeto)
							.getHibernateLazyInitializer().getImplementation());
				}

				prune(objeto);

				if (!set.contains(objeto)) {
					set.add(objeto);
					objeto = (Objeto) getImplementationDeep(objeto,
							objeto.getClass(), set);
				}
				f[i].set(o, objeto);
			}
		}
		if (clazz.getSuperclass().getSuperclass() != null) {
			System.out.println("*** Classe: " + clazz.getName() + " - "
					+ clazz.getSuperclass().getName());
			getImplementationDeep(o, clazz.getSuperclass(), set);
		}
		return o;
	}

	private static void prune(Objeto objeto) {
		if (objeto instanceof ExTipoDocumento) {
			((ExTipoDocumento) objeto).setExFormaDocumentoSet(null);
		}

		if (objeto instanceof DpLotacao) {
			((DpLotacao) objeto).setDpLotacaoSubordinadosSet(null);
			((DpLotacao) objeto).setLotacaoInicial(null);
			((DpLotacao) objeto).setLotacaoPai(null);
			((DpLotacao) objeto).setLotacoesPosteriores(null);
		}

		if (objeto instanceof DpPessoa) {
			((DpPessoa) objeto).setPessoaInicial(null);
			((DpPessoa) objeto).setPessoasPosteriores(null);
		}

		if (objeto instanceof ExMobil) {
			((ExMobil) objeto).setExMarcaSet(null);
			((ExMobil) objeto).setExDocumentoFilhoSet(null);
			((ExMobil) objeto).setExMovimentacaoReferenciaSet(null);
			((ExMobil) objeto).setExMovimentacaoSet(null);
		}
	}

	public String toJSON(ExMobil mobil) throws Exception {
		// Prune
		ExMobil mob = (ExMobil) getImplementationDeep(mobil);

		// mob.setExDocumento((ExDocumento)
		// Objeto.getImplementation(mob.doc()));
		mob.setExDocumentoFilhoSet(null);
		// mob.setExMarcaSet(null);
		mob.setExMovimentacaoReferenciaSet(null);
		mob.setExMovimentacaoSet(null);
		mob.setExTipoMobil(null);

		ExDocumento doc = mob.doc();
		// doc.setCadastrante(null);
		// doc.setDestinatario(null);
		doc.setExBoletimDocSet(null);
		doc.setExClassificacao(null);
		doc.setExDocAnterior(null);
		doc.setExFormaDocumento(null);
		doc.setExMobilAutuado(null);
		doc.setExMobilPai(null);
		doc.setExMobilSet(null);
		doc.setExModelo(null);
		doc.setExNivelAcesso(null);
		// doc.setExTipoDocumento(null);
		// doc.getExTipoDocumento().setExFormaDocumentoSet(null);
		doc.setLotaCadastrante(null);
		doc.setLotaDestinatario(null);
		// doc.setLotaSubscritor((DpLotacao)
		// Objeto.getImplementation(doc.getLotaSubscritor()));
		// doc.setLotaSubscritor(null);
		// doc.setLotaTitular(null);
		// doc.setCadastrante(null);
		// doc.setDestinatario(null);
		// doc.setSubscritor(null);
		// doc.setTitular(null);
		// doc.setOrgaoExterno(null);
		// doc.setOrgaoExternoDestinatario(null);
		// doc.setOrgaoUsuario(null);

		ObjetoBaseSerializer hps = new ObjetoBaseSerializer();

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Blob.class, new BlobSerializer())
				// .registerTypeAdapter(ObjetoBase.class, hps)
				.setPrettyPrinting()
				.setFieldNamingPolicy(
						FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		hps.setGson(gson);

		String jsonOutput = gson.toJson(mob);

		// Importante para que as alterações do "prune" não sejam salvas no BD.
		dao().getSessao().clear();

		return jsonOutput;
	}


}
