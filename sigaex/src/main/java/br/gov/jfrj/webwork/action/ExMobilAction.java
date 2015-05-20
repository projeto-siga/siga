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
 * Criado em  13/09/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.CpOrgaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.persistencia.Aguarde;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import com.opensymphony.xwork.Action;

public class ExMobilAction extends
		ExSelecionavelActionSupport<ExMobil, ExMobilDaoFiltro> {
	// private static Log log = LogFactory.getLog(ExDocumentoAction.class);

	private String anexarString;

	private Integer apenasRefresh;

	private boolean exibirCompleto;

	private String anoEmissaoString;

	private File arquivo;

	private LinkedHashSet<ExPreenchimento> preenchSet = null;

	private String preenchParamRedirect;

	private String arquivoContentType;

	private String arquivoFileName;

	/** The value of the exClassificacao association. */
	private ExClassificacaoSelecao classificacaoSel;

	// private Long anoEmissao;

	/** The value of the simple conteudoBlobDoc property. */
	private String conteudo;

	/** The value of the simple conteudoTpDoc property. */
	private String conteudoTpDoc;

	private CpOrgaoSelecao cpOrgaoSel;

	private String descrClassifNovo;

	/** The value of the simple descrDocumento property. */
	private String descrDocumento;

	private String fullText;

	private DpPessoaSelecao destinatarioSel;

	private ExDocumento doc;

	private ExDocumentoSelecao documentoSel;

	private ExMobilSelecao documentoViaSel;

	/** The value of the simple dtDoc property. */
	private String dtDocString;

	private String dtDocFinalString;

	/** The value of the simple dtRegDoc property. */
	private String dtRegDoc;

	private boolean eletronico;

	// private String fgPessoal1;

	// private boolean fgPessoal;

	private Long orgaoUsu;

	private String gravarpreench = new String();

	private Long id;

	/** The composite primary key value. */
	private String idDoc;

	/** The value of the exFormaDocumento association. */
	private Integer idFormaDoc;

	private Long idTipoFormaDoc;

	// private Blob conteudoBlobDoc;
	// private String idCadastrante;

	private Long idMod;

	/** The value of the exTipoDocumento association. */
	private Long idTpDoc;

	private DpLotacaoSelecao lotacaoDestinatarioSel;

	private ExModelo modelo;

	/** The value of the simple nmArqDoc property. */
	private String nmArqDoc;

	// private Long lotaDestinatario;

	private String nmDestinatario;

	private String nmFuncaoSubscritor;

	private String nmOrgaoExterno;

	/** The value of the simple nmSubscritorExt property. */
	private String nmSubscritorExt;

	private String nomePreenchimento;

	private String numAntigoDoc;

	/** The value of the simple numExpediente property. */
	private String numExpediente;

	/** The value of the simple numExtDoc property. */
	private String numExtDoc;

	private Integer numVia;

	private String obsOrgao;

	private CpOrgao orgaoExterno;

	private CpOrgaoSelecao orgaoExternoDestinatarioSel;

	private CpOrgaoSelecao orgaoSel;

	private SortedMap<String, String> paramsEntrevista;

	private Long preenchimento;

	private String preenchRedirect;

	private DpPessoaSelecao subscritorSel;

	private boolean substituicao;

	private Integer tipoDestinatario;

	private DpPessoaSelecao titularSel;

	private DpPessoaSelecao cadastranteSel;

	private Long ultMovIdEstadoDoc;

	private DpLotacaoSelecao lotaCadastranteSel;

	private DpLotacaoSelecao ultMovLotaRespSel;

	private DpPessoaSelecao ultMovRespSel;

	private String userQuery;

	private Integer tipoCadastrante;

	private Integer ultMovTipoResp;

	private Integer via;

	private String podeExibir;

	private List<ExDocumento> results;

	private Long nivelAcesso;

	private boolean eletronicoFixo;

	private List showedResults;

	private List<Long> docsNaoIndexados;

	private String msg;

	private String primeiraVez;

	private boolean exibirDocumentosNaoAssinados;

	private Boolean fullSearchTambemSigilosos;

	private Integer ordem;

	private Integer visualizacao;

	private String matricula;

	public class GenericoSelecao implements Selecionavel {

		private Long id;

		private String sigla;

		private String descricao;

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSigla() {
			return sigla;
		}

		public void setSigla(String sigla) {
			this.sigla = sigla;
		}
	}

	public Integer getVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(Integer visualizacao) {
		this.visualizacao = visualizacao;
	}

	public ExMobilAction() {
		classificacaoSel = new ExClassificacaoSelecao();
		destinatarioSel = new DpPessoaSelecao();
		documentoSel = new ExDocumentoSelecao();
		documentoViaSel = new ExMobilSelecao();
		lotacaoDestinatarioSel = new DpLotacaoSelecao();
		orgaoExternoDestinatarioSel = new CpOrgaoSelecao();
		orgaoSel = new CpOrgaoSelecao();
		subscritorSel = new DpPessoaSelecao();
		titularSel = new DpPessoaSelecao();
		ultMovLotaRespSel = new DpLotacaoSelecao();
		ultMovRespSel = new DpPessoaSelecao();
		cadastranteSel = new DpPessoaSelecao();
		setItemPagina(50);
		lotaCadastranteSel = new DpLotacaoSelecao();
		paramsEntrevista = new TreeMap<String, String>();
		cpOrgaoSel = new CpOrgaoSelecao();
		results = new LinkedList<ExDocumento>();
	}

	@Override
	public String aSelecionar() throws Exception {
	//	assertAcesso("");
		String s = super.aSelecionar();
		if (getSel() != null && getMatricula() != null) {
			GenericoSelecao sel = new GenericoSelecao();
			sel.setId(getSel().getId());
			sel.setSigla(getSel().getSigla());
			sel.setDescricao("/sigaex/expediente/doc/exibir.action?sigla="
					+ sel.getSigla());
			setSel(sel);
		}
		return s;
	}

	@Override
	public String aBuscar() throws Exception {
		assertAcesso("");
		int offset = 0;
		int itemPagina = 0;
		if (param("p.offset") != null) {
			offset = paramInteger("p.offset");
		}
		if (param("itemPagina") != null) {
			itemPagina = paramInteger("itemPagina");
		}

		final ExMobilDaoFiltro flt = createDaoFiltro();

		if (getPostback() == null) {
			setOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());

			setIdTpDoc(0L);

			// getUltMovRespSel().setId(getTitular().getIdPessoa());
			getUltMovLotaRespSel().setId(getLotaTitular().getIdLotacao());
			flt.setUltMovLotaRespSelId(getUltMovLotaRespSel().getId());
			setUltMovTipoResp(2);
			setUltMovIdEstadoDoc(2L);
			flt.setUltMovIdEstadoDoc(getUltMovIdEstadoDoc());

			if (getUltMovLotaRespSel().getId() != null)
				setUltMovTipoResp(2);
			else
				setUltMovTipoResp(1);
		}

		flt.setSigla(param("sigla"));

		getClassificacaoSel().buscar();
		getSubscritorSel().buscar();
		getDestinatarioSel().buscar();
		getLotacaoDestinatarioSel().buscar();
		getOrgaoSel().buscar();
		getOrgaoExternoDestinatarioSel().buscar();
		getUltMovRespSel().buscar();
		getUltMovLotaRespSel().buscar();

		setTamanho(dao().consultarQuantidadePorFiltroOtimizado(flt,
				getTitular(), getLotaTitular()));
		setItens(dao().consultarPorFiltroOtimizado(flt, offset, itemPagina));

		return Action.SUCCESS;
	}

	public String aCarregarListaFormas() {
		setIdTipoFormaDoc(paramLong("tipoForma"));
		return Action.SUCCESS;
	}

	public String aListar() throws Exception {
		assertAcesso("");
		if (getPostback() == null) {
			if (getOrgaoUsu() == null)
				setOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
			setIdTpDoc(0L);
			setTipoDestinatario(2);
			if (getUltMovLotaRespSel().getId() != null)
				setUltMovTipoResp(2);
			else
				setUltMovTipoResp(1);
			if (getLotaCadastranteSel().getId() != null)
				setTipoCadastrante(2);
			else
				setTipoCadastrante(1);
		}

		int offset = 0;
		// int itemPagina = 10;
		if (getP().getOffset() != null) {
			offset = getP().getOffset();
		}
		// if (param("itemPagina") != null) {
		// itemPagina = paramInteger("itemPagina");
		// }

		classificacaoSel.buscar();
		destinatarioSel.buscar();
		documentoSel.buscar();
		lotacaoDestinatarioSel.buscar();
		orgaoExternoDestinatarioSel.buscar();
		orgaoSel.buscar();
		subscritorSel.buscar();
		ultMovLotaRespSel.buscar();
		ultMovRespSel.buscar();
		cadastranteSel.buscar();
		lotaCadastranteSel.buscar();

		if (getPrimeiraVez() == null || !getPrimeiraVez().equals("sim")) {
			final ExMobilDaoFiltro flt = createDaoFiltro();
			long tempoIni = System.currentTimeMillis();
			setTamanho(dao().consultarQuantidadePorFiltroOtimizado(flt,
					getTitular(), getLotaTitular()));

			/*
			 * if (getTamanho() > 100) { setTamanho(100); itemPagina = 100; }
			 */

			System.out.println("Consulta dos por filtro: "
					+ (System.currentTimeMillis() - tempoIni));

			setItens(dao().consultarPorFiltroOtimizado(flt, offset,
					getItemPagina(), getTitular(), getLotaTitular()));
		}

		return Action.SUCCESS;
	}

	@Override
	public ExMobilDaoFiltro createDaoFiltro() throws Exception {
		final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();

		if (flt.getIdOrgaoUsu() == null || flt.getIdOrgaoUsu() == 0) {
			if (matricula != null) {
				DpPessoa pes = daoPes(param("matricula"));
				flt.setIdOrgaoUsu(pes.getOrgaoUsuario().getId());
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			String dt = param("dtDocString");
			if (dt != null && !dt.equals(""))
				dt = dt + " 00:00:00";

			flt.setDtDoc(df.parse(dt));
		} catch (final ParseException e) {
			flt.setDtDoc(null);
		} catch (final NullPointerException e) {
			flt.setDtDoc(null);
		}

		try {
			String dt = param("dtDocFinalString");
			if (dt != null && !dt.equals(""))
				dt = dt + " 23:59:59";

			flt.setDtDocFinal(df.parse(dt));
		} catch (final ParseException e) {
			flt.setDtDocFinal(null);
		} catch (final NullPointerException e) {
			flt.setDtDocFinal(null);
		}

		flt.setAnoEmissao(paramLong("anoEmissaoString"));
		flt.setClassificacaoSelId(paramLong("classificacaoSel.id"));
		flt.setDescrDocumento(Texto
				.removeAcentoMaiusculas(param("descrDocumento")));
		String paramFullText = param("fullText");
		if (paramFullText != null) {
			paramFullText = paramFullText.trim(); /*
												 * retirando espaços em branco
												 * (inicio e final) e ""
												 */
			paramFullText = paramFullText.replace("\"", "");
			setFullText(paramFullText);
		}
		flt.setFullText(paramFullText);
		flt.setDestinatarioSelId(paramLong("destinatarioSel.id"));
		if (flt.getDestinatarioSelId() != null)
			flt.setDestinatarioSelId((daoPes(flt.getDestinatarioSelId()))
					.getIdInicial());
		flt.setIdFormaDoc(paramInteger("idFormaDoc"));
		flt.setIdTipoFormaDoc(paramLong("idTipoFormaDoc"));
		flt.setIdTpDoc(paramInteger("idTpDoc"));
		flt.setLotacaoDestinatarioSelId(paramLong("lotacaoDestinatarioSel.id"));
		if (flt.getLotacaoDestinatarioSelId() != null)
			flt.setLotacaoDestinatarioSelId((daoLot(flt
					.getLotacaoDestinatarioSelId())).getIdInicial());
		if (param("nmDestinatario") != null)
			flt.setNmDestinatario(param("nmDestinatario").toUpperCase());
		flt.setNmSubscritorExt(param("nmSubscritorExt"));
		flt.setNumExpediente(paramLong("numExpediente"));
		flt.setNumExtDoc(param("numExtDoc"));
		flt.setNumAntigoDoc(param("numAntigoDoc"));
		flt.setOrgaoExternoSelId(paramLong("cpOrgaoSel.id"));
		flt.setOrgaoExternoDestinatarioSelId(paramLong("orgaoExternoDestinatarioSel.id"));
		flt.setSubscritorSelId(paramLong("subscritorSel.id"));
		if (flt.getSubscritorSelId() != null)
			flt.setSubscritorSelId((daoPes(flt.getSubscritorSelId()))
					.getIdInicial());

		flt.setLotaCadastranteSelId(paramLong("lotaCadastranteSel.id"));
		if (flt.getLotaCadastranteSelId() != null)
			flt.setLotaCadastranteSelId((daoLot(flt.getLotaCadastranteSelId()))
					.getIdInicial());
		flt.setCadastranteSelId(paramLong("cadastranteSel.id"));
		if (flt.getCadastranteSelId() != null)
			flt.setCadastranteSelId((daoPes(flt.getCadastranteSelId()))
					.getIdInicial());

		flt.setUltMovIdEstadoDoc(paramLong("ultMovIdEstadoDoc"));
		flt.setUltMovLotaRespSelId(paramLong("ultMovLotaRespSel.id"));
		if (flt.getUltMovLotaRespSelId() != null)
			flt.setUltMovLotaRespSelId((daoLot(flt.getUltMovLotaRespSelId()))
					.getIdInicial());
		flt.setUltMovRespSelId(paramLong("ultMovRespSel.id"));
		if (flt.getUltMovRespSelId() != null)
			flt.setUltMovRespSelId((daoPes(flt.getUltMovRespSelId()))
					.getIdInicial());

		flt.setNumSequencia(paramInteger("numVia"));
		if (getCadastrante() != null)
			flt.setLotacaoCadastranteAtualId(getCadastrante().getLotacao()
					.getIdInicial());

		if (paramLong("orgaoUsu") != null)
			flt.setIdOrgaoUsu(paramLong("orgaoUsu"));
		if (flt.getIdOrgaoUsu() == null && getLotaTitular() != null)
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario()
					.getIdOrgaoUsu());
		flt.setIdMod(paramLong("idMod"));
		flt.setOrdem(ordem);

		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final ExMobilDaoFiltro flt)
			throws AplicacaoException {

		final ExMobil docVia = new ExMobil();

		try {
			/*
			 * bruno.lacerda@avantiprima.com.br - 30/07/2012 Correcao problema
			 * id to load is required for loading. Verificando se o ID do
			 * documento não é nulo antes de pesquisar
			 *
			 * final ExDocumento docdoc = dao().consultar(flt.getIdDoc(),
			 * ExDocumento.class, false); docVia.setExDocumento(docdoc);
			 *
			 * return docVia;
			 */
			if (flt != null && flt.getIdDoc() != null) {
				final ExDocumento docdoc = dao().consultar(flt.getIdDoc(),
						ExDocumento.class, false);
				docVia.setExDocumento(docdoc);
			}
		} catch (final Exception e) {
			throw new AplicacaoException(e.getMessage());
		}

		return docVia;
	}

	@Override
	public Selecionavel selecionarVerificar(Selecionavel sel)
			throws AplicacaoException {



		ExMobil mob = (ExMobil) sel;

		if (mob.doc() == null)
			return null;

		//Edson: Se a via, volume ou documento inteiro tiver sido eliminado(a), não retorna nada.
		if (mob.isEliminado())
			return null;

		// Se for uma via ou volume, retornar
		if (mob.isVia() || mob.isVolume())
			return mob;

		if (mob.isGeral() && mob.doc().isExpediente()) {
			// Se o numero da via nao foi especificado, tentar encontrar a via
			// de numero mais baixo que esteja com o titular.
			//
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (m.isGeral() || m.isEliminado())
					continue;
				ExMovimentacao mov = m.getUltimaMovimentacaoNaoCancelada();
				if (mov != null && mov.getResp() != null
						&& mov.getResp().equivale(super.getTitular())) {
					return m;
				}
			}

			// Se nao encontrar, tentar encontrar uma na lotacao do titular
			//
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (m.isGeral() || m.isEliminado())
					continue;
				ExMovimentacao mov = m.getUltimaMovimentacaoNaoCancelada();
				if (mov != null && mov.getLotaResp() != null
						&& mov.getLotaResp().equivale(super.getLotaTitular())) {
					return m;
				}
			}
		}

		if (mob.isGeral() && mob.doc().isProcesso()) {
			// Se o ultimo volume estiver na lotação do titular ou com ele,
			// retornar o ultimo volume
			//
			ExMobil m = mob.doc().getUltimoVolume();
			if (m != null) {
				ExMovimentacao mov = m.getUltimaMovimentacaoNaoCancelada();
				if (mov == null) {
					return m;
				}
				if (mov.getLotaResp() != null
						&& mov.getLotaResp().equivale(super.getLotaTitular())) {
					return m;
				}
				if (mov.getResp() != null
						&& mov.getResp().equivale(super.getTitular())) {
					return m;
				}
			}
		}

		return sel;
	}

	public String aOtimizarIndices() {
		FullTextSession fullTextSession = Search.getFullTextSession(ExDao.getInstance().getSessao());
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		searchFactory.optimize();
		return Action.SUCCESS;
	}

	public String aFullSearch() throws Exception {
		assertAcesso("");
		int offset = 0;
		int itemPagina = 20;

		if (getUserQuery() != null && !getUserQuery().trim().equals("")) {

			if (getP().getOffset() != null) {
				offset = getP().getOffset();
			}

			if (param("itemPagina") != null) {
				itemPagina = paramInteger("itemPagina");
			}

			// Essas duas vars definem a paginação que será passada para o
			// Lucene. No caso dos sigilosos não será feita a paginação pelo
			// Lucene
			int itemPaginaLucene, offsetLucene;

			String queryPesquisa = getUserQuery();

			if (getFullSearchTambemSigilosos() == null
					|| (!getFullSearchTambemSigilosos())) {
				queryPesquisa += " AND (nivelAcesso:10 OR nivelAcesso:20)";
				itemPaginaLucene = itemPagina;
				offsetLucene = offset;
			} else {
				itemPaginaLucene = 800;
				offsetLucene = 0;
			}

			System.out.println("Busca por " + getUserQuery() + " -> início");
			long tempoInicioBusca = System.currentTimeMillis();
			Object[] resultObjects = dao().consultarPorTexto(queryPesquisa,
					offsetLucene, itemPaginaLucene);
			showedResults = (List) resultObjects[0];
			setTamanho((Integer) resultObjects[1]);

			// Se foi feita a busca a sigilosos...
			if (getFullSearchTambemSigilosos() != null
					&& getFullSearchTambemSigilosos()) {
				List newShowedResults = new ArrayList();
				int i = 0;
				for (Object o : showedResults) {
					Object[] oArray = (Object[]) o;
					Integer nivel = Integer.valueOf((String) oArray[7]);
					Long id = (Long) oArray[3];
					ExDocumento doque = dao().consultar(id, ExDocumento.class,
							false);
					if (nivel <= 20
							|| Ex.getInstance()
									.getComp()
									.podeAcessarDocumento(getTitular(),
											getLotaTitular(),
											doque.getMobilGeral())) {
						if ((i >= offset) && (i < offset + itemPagina))
							newShowedResults.add(o);
						i++;
					}
				}
				showedResults = newShowedResults;
				setTamanho(i);
			}

			System.out.println("Busca por " + getUserQuery()
					+ " -> Fim total: "
					+ (System.currentTimeMillis() - tempoInicioBusca) + " ms");
		}
		return Action.SUCCESS;
	}

	public Long getOrgaoUsu() {
		return orgaoUsu;
	}

	public void setOrgaoUsu(Long orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	public Long getIdTpDoc() {
		return idTpDoc;
	}

	public void setIdTpDoc(Long idTpDoc) {
		this.idTpDoc = idTpDoc;
	}

	public Long getUltMovIdEstadoDoc() {
		return ultMovIdEstadoDoc;
	}

	public void setUltMovIdEstadoDoc(Long ultMovIdEstadoDoc) {
		this.ultMovIdEstadoDoc = ultMovIdEstadoDoc;
	}

	public DpLotacaoSelecao getUltMovLotaRespSel() {
		return ultMovLotaRespSel;
	}

	public void setUltMovLotaRespSel(DpLotacaoSelecao ultMovLotaRespSel) {
		this.ultMovLotaRespSel = ultMovLotaRespSel;
	}

	public DpPessoaSelecao getUltMovRespSel() {
		return ultMovRespSel;
	}

	public void setUltMovRespSel(DpPessoaSelecao ultMovRespSel) {
		this.ultMovRespSel = ultMovRespSel;
	}

	public Integer getUltMovTipoResp() {
		return ultMovTipoResp;
	}

	public void setUltMovTipoResp(Integer ultMovTipoResp) {
		this.ultMovTipoResp = ultMovTipoResp;
	}

	public Integer getVia() {
		return via;
	}

	public void setVia(Integer via) {
		this.via = via;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public void setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public CpOrgaoSelecao getCpOrgaoSel() {
		return cpOrgaoSel;
	}

	public void setCpOrgaoSel(CpOrgaoSelecao cpOrgaoSel) {
		this.cpOrgaoSel = cpOrgaoSel;
	}

	public DpPessoaSelecao getDestinatarioSel() {
		return destinatarioSel;
	}

	public void setDestinatarioSel(DpPessoaSelecao destinatarioSel) {
		this.destinatarioSel = destinatarioSel;
	}

	public DpLotacaoSelecao getLotacaoDestinatarioSel() {
		return lotacaoDestinatarioSel;
	}

	public void setLotacaoDestinatarioSel(
			DpLotacaoSelecao lotacaoDestinatarioSel) {
		this.lotacaoDestinatarioSel = lotacaoDestinatarioSel;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public CpOrgaoSelecao getOrgaoExternoDestinatarioSel() {
		return orgaoExternoDestinatarioSel;
	}

	public void setOrgaoExternoDestinatarioSel(
			CpOrgaoSelecao orgaoExternoDestinatarioSel) {
		this.orgaoExternoDestinatarioSel = orgaoExternoDestinatarioSel;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public void setSubscritorSel(DpPessoaSelecao subscritorSel) {
		this.subscritorSel = subscritorSel;
	}

	public DpPessoaSelecao getTitularSel() {
		return titularSel;
	}

	public void setTitularSel(DpPessoaSelecao titularSel) {
		this.titularSel = titularSel;
	}

	public CpOrgaoSelecao getOrgaoSel() {
		return orgaoSel;
	}

	public void setOrgaoSel(CpOrgaoSelecao orgaoSel) {
		this.orgaoSel = orgaoSel;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String userQuery) {
		this.userQuery = userQuery;
	}

	public List<ExDocumento> getResults() {
		return results;
	}

	public void setResults(List<ExDocumento> results) {
		this.results = results;
	}

	public String aMarcarTudo() throws Exception {
		assertAcesso("");
		int aPartirDe = 0;


		Ex.getInstance().getBL().marcarTudo();

		return Action.SUCCESS;
	}

	public String aNumerarTudo() throws Exception {
		assertAcesso("");
		int aPartirDe = 0;
		if (param("apartir") != null)
			aPartirDe = paramInteger("apartir");
		Ex.getInstance().getBL().numerarTudo(aPartirDe);

		return Action.SUCCESS;
	}

	public String aMarcar() throws Exception {
		assertAcesso("");
		String sigla = param("sigla");
		String[] siglasSparadas = sigla.split(";");
		for (String s : siglasSparadas) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(s);
			ExMobil mob = (ExMobil) dao().consultarPorSigla(filter);
			ExDocumento doque = mob.getExDocumento();
			Ex.getInstance().getBL().marcar(doque);
		}

		return Action.SUCCESS;
	}

	public String aIndexarTudo() throws Exception {
		assertAcesso("");
		dao().indexarTudo(new Aguarde() {
			public void setMensagem(String s) {
				setMensagemAguarde(s);
			}
		});

		return Action.SUCCESS;
	}

	public String aIndexarUltimas() throws Exception {
		assertAcesso("");
		String sDesde = param("min");
		int desde = 120; // De duas horas pra cá
		if (sDesde != null)
			desde = Integer.parseInt(sDesde);
		desde *= -1;
		dao().indexarUltimas(desde);
		return Action.SUCCESS;
	}

	public String aIndexar() throws Exception {
		assertAcesso("");
		String[] idDocs = getPar().get("id");
		ExDocumento doque;
		List<ExDocumento> listaDocs = new ArrayList<ExDocumento>();
		for (String idDoc : idDocs) {
			doque = dao().consultar(Long.valueOf(idDoc), ExDocumento.class,
					false);
			listaDocs.add(doque);
		}
		dao().reindexarVarios(listaDocs, false);

		return Action.SUCCESS;
	}

	public String aDesindexar() throws Exception {
		assertAcesso("");
		String[] idDocs = getPar().get("id");
		ExDocumento doque;
		List<ExDocumento> listaDocs = new ArrayList<ExDocumento>();
		for (String idDoc : idDocs) {
			doque = dao().consultar(Long.valueOf(idDoc), ExDocumento.class,
					false);
			listaDocs.add(doque);
		}
		dao().reindexarVarios(listaDocs, true);

		return Action.SUCCESS;
	}

	public String aIndexarFila() throws Exception {
		assertAcesso("");
		dao().indexarFila(null);
		return Action.SUCCESS;
	}

	public String aListarNaoIndexados() throws Exception {
		assertAcesso("");
		int aPartirDe = 0;
		boolean irIndexando = false;
		if (param("apartir") != null)
			aPartirDe = paramInteger("apartir");
		if (param("indexar") != null)
			irIndexando = true;
		dao().listarNaoIndexados(aPartirDe, irIndexando);
		return Action.SUCCESS;
	}

	public String aCarregarListaModelos() {
		setIdFormaDoc(paramInteger("forma"));
		return Action.SUCCESS;
	}

	public List<String> getListaAnos() {
		final ArrayList<String> lst = new ArrayList<String>();
		// map.add("", "[Vazio]");
		final Calendar cal = Calendar.getInstance();
		for (Integer ano = cal.get(Calendar.YEAR); ano >= 1990; ano--)
			lst.add(ano.toString());
		return lst;
	}

	public Map<Integer, String> getListaVias() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		for (byte k = 1; k <= 20; k++) {
			final byte[] k2 = { (byte) (k + 64) };
			map.put(new Integer(k), new String(k2));
		}

		return map;
	}

	public Map<Integer, String> getListaTipoDest() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		map.put(3, "Órgão Externo");
		map.put(4, "Campo Livre");
		return map;
	}

	public List<ExFormaDocumento> getTodasFormasDocPorTipoForma()
			throws Exception {
		ExBL bl = Ex.getInstance().getBL();
		ExTipoFormaDoc tipoForma = null;
		if (getIdTipoFormaDoc() != null && getIdTipoFormaDoc() != 0)
			tipoForma = dao().consultar(getIdTipoFormaDoc(),
					ExTipoFormaDoc.class, false);

		List<ExFormaDocumento> formasDoc = new ArrayList<ExFormaDocumento>();
		formasDoc.addAll(bl.obterFormasDocumento(




						bl.obterListaModelos(null, false, null, false, getTitular(), getLotaTitular(), false),
						null, tipoForma));
		return formasDoc;
//		return bl.obterFormasDocumento(
//				bl.obterListaModelos(null, false, null, false, getTitular(), getLotaTitular(), false),
//				doc.getExTipoDocumento(), null);
	}

	public List<ExTipoFormaDoc> getTiposFormaDoc() throws Exception {
		List<ExTipoFormaDoc> lista = new ArrayList<ExTipoFormaDoc>();
		return dao().listarExTiposFormaDoc();
	}

	public List<ExModelo> getModelos() throws Exception {
		ExFormaDocumento forma = null;
		if (getIdFormaDoc() != null && getIdFormaDoc() != 0)
			forma = dao().consultar(this.getIdFormaDoc(),
					ExFormaDocumento.class, false);

		return Ex
				.getInstance()
				.getBL()
				.obterListaModelos(forma, false, "Todos", false, getTitular(),
						getLotaTitular(), false);
	}

	public String getAnexarString() {
		return anexarString;
	}

	public void setAnexarString(String anexarString) {
		this.anexarString = anexarString;
	}

	public boolean isExibirCompleto() {
		return exibirCompleto;
	}

	public void setExibirCompleto(boolean exibirCompleto) {
		this.exibirCompleto = exibirCompleto;
	}

	public String getAnoEmissaoString() {
		return anoEmissaoString;
	}

	public void setAnoEmissaoString(String anoEmissaoString) {
		this.anoEmissaoString = anoEmissaoString;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public LinkedHashSet<ExPreenchimento> getPreenchSet() {
		return preenchSet;
	}

	public void setPreenchSet(LinkedHashSet<ExPreenchimento> preenchSet) {
		this.preenchSet = preenchSet;
	}

	public String getPreenchParamRedirect() {
		return preenchParamRedirect;
	}

	public void setPreenchParamRedirect(String preenchParamRedirect) {
		this.preenchParamRedirect = preenchParamRedirect;
	}

	public String getArquivoContentType() {
		return arquivoContentType;
	}

	public void setArquivoContentType(String arquivoContentType) {
		this.arquivoContentType = arquivoContentType;
	}

	public String getArquivoFileName() {
		return arquivoFileName;
	}

	public void setArquivoFileName(String arquivoFileName) {
		this.arquivoFileName = arquivoFileName;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getConteudoTpDoc() {
		return conteudoTpDoc;
	}

	public void setConteudoTpDoc(String conteudoTpDoc) {
		this.conteudoTpDoc = conteudoTpDoc;
	}

	public String getDescrClassifNovo() {
		return descrClassifNovo;
	}

	public void setDescrClassifNovo(String descrClassifNovo) {
		this.descrClassifNovo = descrClassifNovo;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public void setDescrDocumento(String descrDocumento) {
		this.descrDocumento = descrDocumento;
	}

	public ExDocumento getDoc() {
		return doc;
	}

	public void setDoc(ExDocumento doc) {
		this.doc = doc;
	}

	public ExDocumentoSelecao getDocumentoSel() {
		return documentoSel;
	}

	public void setDocumentoSel(ExDocumentoSelecao documentoSel) {
		this.documentoSel = documentoSel;
	}

	public ExMobilSelecao getDocumentoViaSel() {
		return documentoViaSel;
	}

	public void setDocumentoViaSel(ExMobilSelecao documentoViaSel) {
		this.documentoViaSel = documentoViaSel;
	}

	public String getDtDocString() {
		return dtDocString;
	}

	public void setDtDocString(String dtDocString) {
		this.dtDocString = dtDocString;
	}

	public String getDtRegDoc() {
		return dtRegDoc;
	}

	public void setDtRegDoc(String dtRegDoc) {
		this.dtRegDoc = dtRegDoc;
	}

	public boolean isEletronico() {
		return eletronico;
	}

	public void setEletronico(boolean eletronico) {
		this.eletronico = eletronico;
	}

	public String getGravarpreench() {
		return gravarpreench;
	}

	public void setGravarpreench(String gravarpreench) {
		this.gravarpreench = gravarpreench;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(String idDoc) {
		this.idDoc = idDoc;
	}

	public Integer getIdFormaDoc() {
		return idFormaDoc;
	}

	public void setIdFormaDoc(Integer idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
	}

	public Long getIdMod() {
		return idMod;
	}

	public void setIdMod(Long idMod) {
		this.idMod = idMod;
	}

	public ExModelo getModelo() {
		return modelo;
	}

	public void setModelo(ExModelo modelo) {
		this.modelo = modelo;
	}

	public String getNmArqDoc() {
		return nmArqDoc;
	}

	public void setNmArqDoc(String nmArqDoc) {
		this.nmArqDoc = nmArqDoc;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	public void setNmFuncaoSubscritor(String nmFuncaoSubscritor) {
		this.nmFuncaoSubscritor = nmFuncaoSubscritor;
	}

	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}

	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}

	public String getNmSubscritorExt() {
		return nmSubscritorExt;
	}

	public void setNmSubscritorExt(String nmSubscritorExt) {
		this.nmSubscritorExt = nmSubscritorExt;
	}

	public String getNomePreenchimento() {
		return nomePreenchimento;
	}

	public void setNomePreenchimento(String nomePreenchimento) {
		this.nomePreenchimento = nomePreenchimento;
	}

	public String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	public void setNumAntigoDoc(String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public String getNumExtDoc() {
		return numExtDoc;
	}

	public void setNumExtDoc(String numExtDoc) {
		this.numExtDoc = numExtDoc;
	}

	public Integer getNumVia() {
		return numVia;
	}

	public void setNumVia(Integer numVia) {
		this.numVia = numVia;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public void setObsOrgao(String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	public void setOrgaoExterno(CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public SortedMap<String, String> getParamsEntrevista() {
		return paramsEntrevista;
	}

	public void setParamsEntrevista(SortedMap<String, String> paramsEntrevista) {
		this.paramsEntrevista = paramsEntrevista;
	}

	public Long getPreenchimento() {
		return preenchimento;
	}

	public void setPreenchimento(Long preenchimento) {
		this.preenchimento = preenchimento;
	}

	public String getPreenchRedirect() {
		return preenchRedirect;
	}

	public void setPreenchRedirect(String preenchRedirect) {
		this.preenchRedirect = preenchRedirect;
	}

	public boolean isSubstituicao() {
		return substituicao;
	}

	public void setSubstituicao(boolean substituicao) {
		this.substituicao = substituicao;
	}

	public String getPodeExibir() {
		return podeExibir;
	}

	public void setPodeExibir(String podeExibir) {
		this.podeExibir = podeExibir;
	}

	public Long getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(Long nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

	public boolean isEletronicoFixo() {
		return eletronicoFixo;
	}

	public void setEletronicoFixo(boolean eletronicoFixo) {
		this.eletronicoFixo = eletronicoFixo;
	}

	public List<ExDocumento> getShowedResults() {
		return showedResults;
	}

	public void setShowedResults(List<ExDocumento> showedResults) {
		this.showedResults = showedResults;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPrimeiraVez() {
		return primeiraVez;
	}

	public void setPrimeiraVez(String primeiraVez) {
		this.primeiraVez = primeiraVez;
	}

	public String getTipoDocumento() {
		if (getIdTpDoc() == null && doc != null
				&& doc.getExTipoDocumento() != null)
			setIdTpDoc(doc.getExTipoDocumento().getId());
		if (getIdTpDoc() == 1L)
			return "interno";
		else if (getIdTpDoc() == 2L)
			return "antigo";
		else if (getIdTpDoc() == 3L)
			return "externo";

		// if (param("idTpDoc") == null)
		// setParam("idTpDoc", "1");
		return "";
	}

	public DpPessoaSelecao getCadastranteSel() {
		return cadastranteSel;
	}

	public void setCadastranteSel(DpPessoaSelecao cadastranteSel) {
		this.cadastranteSel = cadastranteSel;
	}

	public DpLotacaoSelecao getLotaCadastranteSel() {
		return lotaCadastranteSel;
	}

	public void setLotaCadastranteSel(DpLotacaoSelecao lotaCadastranteSel) {
		this.lotaCadastranteSel = lotaCadastranteSel;
	}

	public Integer getTipoCadastrante() {
		return tipoCadastrante;
	}

	public void setTipoCadastrante(Integer tipoCadastrante) {
		this.tipoCadastrante = tipoCadastrante;
	}

	public String getDtDocFinalString() {
		return dtDocFinalString;
	}

	public void setDtDocFinalString(String dtDocFinalString) {
		this.dtDocFinalString = dtDocFinalString;
	}

	public boolean isExibirDocumentosNaoAssinados() {
		return exibirDocumentosNaoAssinados;
	}

	public void setExibirDocumentosNaoAssinados(
			boolean exibirDocumentosNaoAssinados) {
		this.exibirDocumentosNaoAssinados = exibirDocumentosNaoAssinados;
	}

	public Long getIdTipoFormaDoc() {
		return idTipoFormaDoc;
	}

	public void setIdTipoFormaDoc(Long idTipoFormaDoc) {
		this.idTipoFormaDoc = idTipoFormaDoc;
	}

	public Integer getApenasRefresh() {
		return apenasRefresh;
	}

	public void setApenasRefresh(Integer apenasRefresh) {
		this.apenasRefresh = apenasRefresh;
	}

	public Boolean getFullSearchTambemSigilosos() {
		return fullSearchTambemSigilosos;
	}

	public void setFullSearchTambemSigilosos(Boolean fullSearchTambemSigilosos) {
		this.fullSearchTambemSigilosos = fullSearchTambemSigilosos;
	}

	public List<Long> getDocsNaoIndexados() {
		return docsNaoIndexados;
	}

	public void setDocsNaoIndexados(List<Long> docsNaoIndexados) {
		this.docsNaoIndexados = docsNaoIndexados;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}
