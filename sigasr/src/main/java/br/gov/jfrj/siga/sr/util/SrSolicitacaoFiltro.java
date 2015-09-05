package br.gov.jfrj.siga.sr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcaoSelecao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAcordoSelecao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracaoSelecao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoVO;

public class SrSolicitacaoFiltro extends SrSolicitacao {

	private static final long serialVersionUID = 1L;
	public static final Long QUALQUER_LISTA_OU_NENHUMA = -1L;
	public static final Long NENHUMA_LISTA = 0L;
	private static final String AND = " AND ";

	private boolean pesquisar = false;

	private String dtIni;

	private String dtFim;

	private CpMarcador situacao;

	private DpPessoa atendente;

	private SrAcordo acordo;

	private DpLotacao lotaAtendente;

	private Long idListaPrioridade;

	private boolean naoDesignados;

	private boolean apenasFechados;

	private Long idNovoAtributo;

	private Long start;

	private Long length;
	
	private String orderBy;
	
	private SentidoOrdenacao sentidoOrdenacao = SentidoOrdenacao.DESC;

	private DpPessoaSelecao atendenteSel;
	private DpLotacaoSelecao lotaAtendenteSel;

	private DpPessoaSelecao cadastranteSel;
	private DpLotacaoSelecao lotaTitularSel;

	private DpPessoaSelecao solicitanteSel;
	private DpLotacaoSelecao lotaSolicitanteSel;

	private SrAcaoSelecao acaoSel;
	private SrAcordoSelecao acordoSel;
	private SrItemConfiguracaoSelecao itemConfiguracaoSel;
	
	public enum SentidoOrdenacao{
		ASC, DESC;
	}
	
	public SrSolicitacaoFiltro() {
		super();
	}	

	public void carregarSelecao() {
		if (atendenteSel != null) {
			this.setAtendente(atendenteSel.buscarObjeto());
			atendenteSel.carregarDadosParaView(this.getAtendente());
		}

		if (lotaAtendenteSel != null) {
			this.setLotaAtendente(lotaAtendenteSel.buscarObjeto());
			lotaAtendenteSel.carregarDadosParaView(this.getLotaAtendente());
		}

		if (cadastranteSel != null) {
			this.setCadastrante(cadastranteSel.buscarObjeto());
			cadastranteSel.carregarDadosParaView(this.getCadastrante());
		}

		if (lotaTitularSel != null) {
			this.setLotaTitular(lotaTitularSel.buscarObjeto());
			this.lotaTitularSel.carregarDadosParaView(this
					.getLotaTitular());
		}

		if (solicitanteSel != null) {
			this.setSolicitante(solicitanteSel.buscarObjeto());
			solicitanteSel.carregarDadosParaView(this.getSolicitante());
		}

		if (lotaSolicitanteSel != null) {
			this.setLotaSolicitante(lotaSolicitanteSel.buscarObjeto());
			lotaSolicitanteSel.carregarDadosParaView(this.getLotaSolicitante());
		}

		if (acaoSel != null) {
			this.setAcao(acaoSel.getObjeto());
			acaoSel.carregarDadosParaView(this.getAcao());
		}

		if (acordoSel != null) {
			this.setAcordo(acordoSel.buscarObjeto());
			acordoSel.carregarDadosParaView(this.getAcordo());
		}

		if (situacao != null && situacao.getIdMarcador() != null) {
			this.setSituacao(CpMarcador.AR.findById(situacao.getIdMarcador()));
		}

		if (itemConfiguracaoSel != null) {
			this.setItemConfiguracao(getItemConfiguracaoSel().buscarObjeto());
			itemConfiguracaoSel.carregarDadosParaView(this
					.getItemConfiguracao());
		}
	}

	@SuppressWarnings("unchecked")
	public Long consultarQuantidade() throws Exception {
		// Edson: foi necessario separar em subquery porque o Oracle nao aceita
		// distinct em coluna CLOB em query contendo join
		StringBuilder query = new StringBuilder("select count(*) ");
		montarBusca(query);
		return (Long) ContextoPersistencia.em().createQuery(query.toString())
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> buscarPorFiltro() throws Exception {
		
		StringBuilder query = new StringBuilder("");
		// Edson: foi necessario separar em subquery porque o Oracle nao aceita
		// distinct em coluna CLOB em query contendo join
		query.append("select sol, situacao, ultMov");
		query.append(idListaPrioridade > 0 ? ", l " : " "); 
				
		montarBusca(query);
		
		query.append(" order by ");
		if (orderBy.equals("dtReg"))
			query.append(" sol.dtReg ");
		else if (orderBy.equals("codigo"))
			query.append(" sol.codigo ");
		else if (orderBy.equals("descrSolicitacao"))
			query.append(" cast(descrSolicitacao as varchar2(100)) ");
		else if (orderBy.equals("solicitante"))
			query.append(" sol.solicitante ");
		else if (orderBy.equals("lotaSolicitante"))
			query.append(" sol.lotaSolicitante.siglaLotacao ");
		else if (orderBy.equals("cadastrante"))
			query.append(" sol.cadastrante ");
		else if (orderBy.equals("lotaTitular"))
			query.append(" sol.lotaTitular.siglaLotacao ");
		else if (orderBy.equals("prioridade"))
			query.append(" sol.prioridade ");
		else if (orderBy.equals("prioridadeTecnica"))
			query.append(" sol.dnmPrioridadeTecnica ");
		else if (orderBy.equals("posicaoNaLista"))
			query.append(" l.numPosicao ");
		else if (orderBy.equals("situacao"))
			query.append(" situacao.cpMarcador.descrMarcador ");
		else if (orderBy.equals("atendente"))
			query.append(" situacao.dpPessoaIni ");
		else if (orderBy.equals("lotaAtendente"))
			query.append(" situacao.dpLotacaoIni.siglaLotacao ");
		else if (orderBy.equals("dtUltimaMovimentacao"))
			query.append(" ultMov.dtIniMov ");
		else if (orderBy.equals("ultimaMovimentacao"))
			query.append(" ultMov.descrMovimentacao ");
		else {
			query.append(" sol.idSolicitacao ");
		}
		
		query.append(sentidoOrdenacao.name());
		Query jq = ContextoPersistencia.em().createQuery(query.toString());
		jq.setFirstResult(getStart().intValue());
		if (getLength() > 0)
			jq.setMaxResults(getLength().intValue());
		return jq.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarSimplificado() throws Exception {
		StringBuilder query = new StringBuilder("select sol.idSolicitacao, sol.descrSolicitacao, sol.codigo, item.tituloItemConfiguracao" + " from SrSolicitacao sol inner join sol.itemConfiguracao as item ");
		montarBusca(query);
		return ContextoPersistencia.em().createQuery(query.toString()).setMaxResults(10).getResultList();
	}

	private void montarBusca(StringBuilder query) throws Exception {

		query.append(" from SrSolicitacao sol inner join sol.meuMarcaSet situacao left join sol.dnmUltimaMovimentacao ultMov");
		query.append(idListaPrioridade != null && idListaPrioridade > 0 ? " inner join sol.meuPrioridadeSolicitacaoSet l " : "");
		query.append(" where sol.hisDtFim is null ");

		if (Filtros.deveAdicionar(getCadastrante()))
			query.append(" and sol.cadastrante.idPessoaIni = "
					+ getCadastrante().getIdInicial());
		if (Filtros.deveAdicionar(getLotaTitular()))
			query.append(" and sol.lotaTitular.idLotacaoIni = "
					+ getLotaTitular().getIdInicial());
		if (Filtros.deveAdicionar(getSolicitante()))
			query.append(" and sol.solicitante.idPessoaIni = "
					+ getSolicitante().getIdInicial());
		if (Filtros.deveAdicionar(getLotaSolicitante()))
			query.append(" and sol.lotaSolicitante.idLotacaoIni = "
					+ getLotaSolicitante().getIdInicial());
		
		if (Filtros.deveAdicionar(getItemConfiguracao())){
			query.append(" and sol.dnmItemConfiguracao.hisIdIni = "
					+ getItemConfiguracao().getItemInicial()
							.getIdItemConfiguracao());
		}
		if (Filtros.deveAdicionar(getAcao())){
			query.append(" and sol.dnmAcao.hisIdIni = "
					+ getAcao().getAcaoInicial().getIdAcao());
		}
		
		if (getPrioridade() != null && getPrioridade().getIdPrioridade() > 0L){
			query.append(" and (sol.dnmPrioridadeTecnica <= " + getPrioridade().ordinal());
			query.append(" or sol.prioridade <= " + getPrioridade().ordinal() + ") ");
		}

		if (getIdListaPrioridade() != null
				&& !getIdListaPrioridade().equals(QUALQUER_LISTA_OU_NENHUMA)) {
			if (getIdListaPrioridade().equals(NENHUMA_LISTA)) {
				query.append(" and not exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.hisIdIni = sol.hisIdIni) ");
			} else {
				SrLista lista = SrLista.AR.findById(getIdListaPrioridade());
				query.append(" and l.lista.hisIdIni = "
						+ lista.getListaInicial().getId());
			}
		}

		if (getDescrSolicitacao() != null
				&& !"".equals(getDescrSolicitacao().trim())) {
			for (String s : getDescrSolicitacao().split(" ")) {
				query.append(" and ( lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ");
				query.append(" or sol in (select mov.solicitacao from SrMovimentacao mov");
				query.append(" where lower(mov.descrMovimentacao) like '%"
						+ s.toLowerCase() + "%' )) ");
			}
		}

		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		if (getDtIni() != null)
			try {
				query.append(" and sol.dtReg >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(getDtIni()))
						+ "', 'yyyy-MM-dd') ");
			} catch (ParseException e) {
				//
			}

		if (getDtFim() != null)
			try {
				query.append(" and sol.dtReg <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(getDtFim()))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ");
			} catch (ParseException e) {
				//
			}
		
		if (Filtros.deveAdicionar(getAcordo()))
			query.append(" and sol.acordos.hisIdIni = " + getAcordo().getHisIdIni() + " ");

		if (getSituacao() != null && getSituacao().getIdMarcador() != null
				&& getSituacao().getIdMarcador() > 0)
			query.append(" and situacao.cpMarcador.idMarcador = "
					+ getSituacao().getIdMarcador());
		else query.append(" and situacao.cpMarcador.idMarcador in (42, 43, 44, 45, 61) ");
		query.append(" and (situacao.dtIniMarca is null or "
					+ "situacao.dtIniMarca < sysdate) ");
		query.append(" and (situacao.dtFimMarca is null or "
					+ "situacao.dtFimMarca > sysdate) ");

		if (Filtros.deveAdicionar(getAtendente()))
			query.append("and situacao.dpPessoaIni.idPessoa = "
					+ getAtendente().getIdInicial());
		else if (Filtros.deveAdicionar(getLotaAtendente())) {
				query.append("and situacao.dpLotacaoIni.idLotacao = "
						+ getLotaAtendente().getIdInicial());
		}
		if (isNaoDesignados())
			query.append(" and situacao.dpPessoaIni is null ");

		montarQueryAtributos(query);
	}

	private void montarQueryAtributos(StringBuilder query) {
		Boolean existeFiltroPreenchido = Boolean.FALSE; // Indica se foi
														// preenchido algum dos
														// atributos informados
														// na requisicao

		StringBuilder subqueryAtributo = new StringBuilder();
		if (meuAtributoSolicitacaoSet != null
				&& !meuAtributoSolicitacaoSet.isEmpty()) {
			subqueryAtributo.append(" and (");

			for (SrAtributoSolicitacao att : meuAtributoSolicitacaoSet) {
				if (att.getValorAtributoSolicitacao() != null
						&& !att.getValorAtributoSolicitacao().trim().isEmpty()) {
					subqueryAtributo.append("(");
					subqueryAtributo.append(" att.atributo.idAtributo = "
							+ att.getAtributo().getIdAtributo());
					subqueryAtributo
							.append(" and att.valorAtributoSolicitacao = '"
									+ att.getValorAtributoSolicitacao() + "' ");

					subqueryAtributo.append(")");
					subqueryAtributo.append(AND);

					existeFiltroPreenchido = Boolean.TRUE;
				}
			}
			subqueryAtributo
					.setLength(subqueryAtributo.length() - AND.length()); // remove
																			// o
																			// ultimo
																			// AND
			subqueryAtributo.append(" )");
		}
		if (existeFiltroPreenchido) {
			subqueryAtributo
					.insert(0,
							" and exists (from SrAtributoSolicitacao att where att.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
			subqueryAtributo.append(" )");
			query.append(subqueryAtributo);
		}
	}

	public List<SrAtributo> getTiposAtributosConsulta() {
		List<SrAtributo> tiposAtributosConsulta = new ArrayList<SrAtributo>();

		if (meuAtributoSolicitacaoSet != null) {
			for (SrAtributoSolicitacao srAtributo : meuAtributoSolicitacaoSet) {
				tiposAtributosConsulta.add(srAtributo.getAtributo());
			}
		}
		return tiposAtributosConsulta;
	}

	public List<SrAtributo> itensDisponiveis(
			List<SrAtributo> atributosDisponiveis, SrAtributo atributo) {
		List<SrAtributo> arrayList = new ArrayList<SrAtributo>(
				atributosDisponiveis);
		arrayList.add(atributo);

		Collections.sort(arrayList, new Comparator<SrAtributo>() {
			@Override
			public int compare(SrAtributo s0, SrAtributo s1) {
				if (s0.getNomeAtributo() == null
						&& s1.getNomeAtributo() == null) {
					return 0;
				} else if (s0.getNomeAtributo() == null) {
					return 1;
				} else if (s1.getNomeAtributo() == null) {
					return -1;
				}
				return s0.getNomeAtributo().compareTo(s1.getNomeAtributo());
			}
		});
		return arrayList;
	}

	public boolean isRazoavelmentePreenchido() {
		return situacao != null
				|| cadastranteSel != null
				|| solicitanteSel != null
				|| atendenteSel != null
				|| lotaAtendenteSel != null
				|| acordoSel != null
				|| dtIni != null
				|| dtFim != null
				|| itemConfiguracaoSel != null
				|| acaoSel != null
				|| acordoSel != null
				|| (idListaPrioridade != null && idListaPrioridade >= 0)
				|| (getDescrSolicitacao() != null && !getDescrSolicitacao()
						.trim().equals("")) || getPrioridade() != null;
	}

	public boolean isPesquisar() {
		return pesquisar;
	}

	public void setPesquisar(boolean pesquisar) {
		this.pesquisar = pesquisar;
	}

	public String getDtIni() {
		return dtIni;
	}

	public void setDtIni(String dtIni) {
		this.dtIni = dtIni;
	}

	public String getDtFim() {
		return dtFim;
	}

	public void setDtFim(String dtFim) {
		this.dtFim = dtFim;
	}

	public CpMarcador getSituacao() {
		return situacao;
	}

	public void setSituacao(CpMarcador situacao) {
		this.situacao = situacao;
	}

	@Override
	public DpPessoa getAtendente() {
		return atendente;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public SentidoOrdenacao getSentidoOrdenacao() {
		return sentidoOrdenacao;
	}

	public void setSentidoOrdenacao(SentidoOrdenacao sentido) {
		this.sentidoOrdenacao = sentido;
	}

	public void setAtendente(DpPessoa atendente) {
		this.atendente = atendente;
	}

	public SrAcordo getAcordo() {
		return acordo;
	}

	public void setAcordo(SrAcordo acordo) {
		this.acordo = acordo;
	}

	@Override
	public DpLotacao getLotaAtendente() {
		return lotaAtendente;
	}

	public void setLotaAtendente(DpLotacao lotaAtendente) {
		this.lotaAtendente = lotaAtendente;
	}

	public Long getIdListaPrioridade() {
		return idListaPrioridade;
	}

	public void setIdListaPrioridade(Long idListaPrioridade) {
		this.idListaPrioridade = idListaPrioridade;
	}

	public boolean isNaoDesignados() {
		return naoDesignados;
	}

	public void setNaoDesignados(boolean naoDesignados) {
		this.naoDesignados = naoDesignados;
	}

	public boolean isApenasFechados() {
		return apenasFechados;
	}

	public void setApenasFechados(boolean apenasFechados) {
		this.apenasFechados = apenasFechados;
	}

	public Long getIdNovoAtributo() {
		return idNovoAtributo;
	}

	public void setIdNovoAtributo(Long idNovoAtributo) {
		this.idNovoAtributo = idNovoAtributo;
	}

	public DpPessoaSelecao getAtendenteSel() {
		return atendenteSel;
	}

	public void setAtendenteSel(DpPessoaSelecao atendenteSel) {
		this.atendenteSel = atendenteSel;
	}

	public DpLotacaoSelecao getLotaAtendenteSel() {
		return lotaAtendenteSel;
	}

	public void setLotaAtendenteSel(DpLotacaoSelecao lotaAtendenteSel) {
		this.lotaAtendenteSel = lotaAtendenteSel;
	}

	public DpPessoaSelecao getCadastranteSel() {
		return cadastranteSel;
	}

	public void setCadastranteSel(DpPessoaSelecao cadastranteSel) {
		this.cadastranteSel = cadastranteSel;
	}

	public DpLotacaoSelecao getLotaTitularSel() {
		return lotaTitularSel;
	}

	public void setLotaTitularSel(DpLotacaoSelecao lotacadastranteSel) {
		this.lotaTitularSel = lotacadastranteSel;
	}

	public DpPessoaSelecao getSolicitanteSel() {
		return solicitanteSel;
	}

	public void setSolicitanteSel(DpPessoaSelecao solicitanteSel) {
		this.solicitanteSel = solicitanteSel;
	}

	public DpLotacaoSelecao getLotaSolicitanteSel() {
		return lotaSolicitanteSel;
	}

	public void setLotaSolicitanteSel(DpLotacaoSelecao lotaSolicitanteSel) {
		this.lotaSolicitanteSel = lotaSolicitanteSel;
	}

	public SrAcaoSelecao getAcaoSel() {
		return acaoSel;
	}

	public void setAcaoSel(SrAcaoSelecao acaoSel) {
		this.acaoSel = acaoSel;
	}

	public SrAcordoSelecao getAcordoSel() {
		return acordoSel;
	}

	public void setAcordoSel(SrAcordoSelecao acordoSel) {
		this.acordoSel = acordoSel;
	}

	public SrItemConfiguracaoSelecao getItemConfiguracaoSel() {
		return itemConfiguracaoSel;
	}

	public void setItemConfiguracaoSel(
			SrItemConfiguracaoSelecao itemConfiguracaoSel) {
		this.itemConfiguracaoSel = itemConfiguracaoSel;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

}
