package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sr.model.SrAcao.SrAcaoVO;
import br.gov.jfrj.siga.sr.model.vo.SrConfiguracaoAssociacaoVO;
import br.gov.jfrj.siga.sr.model.vo.SrConfiguracaoVO;
import br.gov.jfrj.siga.sr.model.vo.SrItemConfiguracaoVO;
import br.gov.jfrj.siga.sr.util.Util;
import br.gov.jfrj.siga.vraptor.entity.ConfiguracaoVraptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "SR_CONFIGURACAO", schema = "SIGASR")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracao extends ConfiguracaoVraptor {

	public static ActiveRecord<SrConfiguracao> AR = new ActiveRecord<>(SrConfiguracao.class);

	private static final long serialVersionUID = 4959384444345462871L;

	@Transient
	private SrItemConfiguracao itemConfiguracaoFiltro;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ITEM", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ITEM_CONFIGURACAO")})
	private List<SrItemConfiguracao> itemConfiguracaoSet;

	@Transient
	private SrAcao acaoFiltro;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ACAO", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ACAO")})
	private List<SrAcao> acoesSet;

	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	private DpLotacao atendente;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_ATRIBUTO")
	private SrAtributo atributo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	private SrPesquisa pesquisaSatisfacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	private SrLista listaPrioridade;

	@Enumerated
	private SrPrioridade prioridade;

	@Column(name = "PRIORIDADE_LISTA")
	@Enumerated
	private SrPrioridade prioridadeNaLista;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_PERMISSAO", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "TIPO_PERMISSAO"), schema="SIGASR")
	private List<SrTipoPermissaoLista> tipoPermissaoSet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ACORDO")
	private SrAcordo acordo;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	private boolean atributoObrigatorio;

	@Transient
	private boolean herdado;

	@Transient
	private boolean utilizarItemHerdado;

	public SrConfiguracao() {
	}

	public SrConfiguracao(DpPessoa solicitante, CpComplexo local, SrItemConfiguracao item) {
		this.setDpPessoa(solicitante);
		this.setComplexo(local);
		this.itemConfiguracaoFiltro = item;
	}

	public SrItemConfiguracao getItemConfiguracaoFiltro() {
		return itemConfiguracaoFiltro;
	}

	public void setItemConfiguracaoFiltro(SrItemConfiguracao itemConfiguracaoFiltro) {
		this.itemConfiguracaoFiltro = itemConfiguracaoFiltro;
	}

	public List<SrItemConfiguracao> getItemConfiguracaoSet() {
		return itemConfiguracaoSet;
	}

	public void setItemConfiguracaoSet(List<SrItemConfiguracao> itemConfiguracaoSet) {
		this.itemConfiguracaoSet = itemConfiguracaoSet;
	}

	public SrAcao getAcaoFiltro() {
		return acaoFiltro;
	}

	public void setAcaoFiltro(SrAcao acaoFiltro) {
		this.acaoFiltro = acaoFiltro;
	}

	public List<SrAcao> getAcoesSet() {
		return acoesSet;
	}

	public void setAcoesSet(List<SrAcao> acoesSet) {
		this.acoesSet = acoesSet;
	}

	public DpLotacao getAtendente() {
		return atendente;
	}

	public void setAtendente(DpLotacao atendente) {
		this.atendente = atendente;
	}

	public SrAtributo getAtributo() {
		return atributo;
	}

	public void setAtributo(SrAtributo atributo) {
		this.atributo = atributo;
	}

	public SrPesquisa getPesquisaSatisfacao() {
		return pesquisaSatisfacao;
	}

	public void setPesquisaSatisfacao(SrPesquisa pesquisaSatisfacao) {
		this.pesquisaSatisfacao = pesquisaSatisfacao;
	}

	public SrLista getListaPrioridade() {
		return listaPrioridade;
	}

	public void setListaPrioridade(SrLista listaPrioridade) {
		this.listaPrioridade = listaPrioridade;
	}

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public SrPrioridade getPrioridadeNaLista() {
		return prioridadeNaLista;
	}

	public void setPrioridadeNaLista(SrPrioridade prioridadeNaLista) {
		this.prioridadeNaLista = prioridadeNaLista;
	}

	public List<SrTipoPermissaoLista> getTipoPermissaoSet() {
		return tipoPermissaoSet;
	}

	public void setTipoPermissaoSet(List<SrTipoPermissaoLista> tipoPermissaoSet) {
		this.tipoPermissaoSet = tipoPermissaoSet;
	}

	public SrAcordo getAcordo() {
		return acordo;
	}

	public void setAcordo(SrAcordo acordo) {
		this.acordo = acordo;
	}

	public boolean isAtributoObrigatorio() {
		return atributoObrigatorio;
	}

	public void setAtributoObrigatorio(boolean atributoObrigatorio) {
		this.atributoObrigatorio = atributoObrigatorio;
	}

	public boolean isHerdado() {
		return herdado;
	}

	public void setHerdado(boolean herdado) {
		this.herdado = herdado;
	}

	public boolean isUtilizarItemHerdado() {
		return utilizarItemHerdado;
	}

	public void setUtilizarItemHerdado(boolean utilizarItemHerdado) {
		this.utilizarItemHerdado = utilizarItemHerdado;
	}

	public Selecionavel getSolicitante() {
		if (this.getDpPessoa() != null)
			return this.getDpPessoa();
		else if (this.getLotacao() != null)
			return this.getLotacao();
		else if (this.getCargo() != null)
			return this.getCargo();
		else if (this.getFuncaoConfianca() != null)
			return this.getFuncaoConfianca();
		else return this.getCpGrupo();
	}

	public String getPesquisaSatisfacaoString() {
		return pesquisaSatisfacao.nomePesquisa;
	}

	public String getAtributoObrigatorioString() {
		return atributoObrigatorio ? "Sim" : "N�o";
	}

	public void salvarComoDesignacao() throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		salvar();
	}

	public boolean isDesignacao() {
		if (this.getCpTipoConfiguracao() != null && this.getCpTipoConfiguracao().getIdTpConfiguracao() != null)
			return this.getCpTipoConfiguracao().getIdTpConfiguracao().equals(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
		else
			return false;
	}

	public void salvarComoInclusaoAutomaticaLista(SrLista srLista) throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class, CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA));
		salvar();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarDesignacoes(boolean mostrarDesativados, DpLotacao atendente) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);

		if (atendente != null) {
			sb.append(" and conf.atendente.idLotacaoIni = ");
			sb.append(atendente.getIdLotacaoIni());
		}

		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		else {
			sb.append(" and conf.idConfiguracao in (");
			sb.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			sb.append(" SrConfiguracao GROUP BY hisIdIni) ");
		}

		return AR.em().createQuery(sb.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarDesignacoes(SrEquipe equipe) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);

		if (equipe != null && equipe.getLotacao() != null && equipe.getLotacao().getIdLotacaoIni() != null) {
			sb.append(" and conf.atendente.idLotacaoIni = ");
			sb.append(equipe.getLotacao().getIdLotacaoIni());
		}

		sb.append(" and conf.hisDtFim is null");

		return AR.em().createQuery(sb.toString()).getResultList();
	}

	public static List<SrConfiguracao> listarDesignacoes(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		conf.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return listar(conf, ArrayUtils.addAll(atributosDesconsideradosFiltro,
				new int[] {}));
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAbrangenciasAcordo(boolean mostrarDesativados, SrAcordo acordo) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO);

		if (acordo != null) {
			sb.append(" and conf.acordo.hisIdIni = ");
			sb.append(acordo.getHisIdIni());
		}

		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		else {
			sb.append(" and conf.idConfiguracao in (");
			sb.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			sb.append(" SrConfiguracao GROUP BY hisIdIni) ");
		}

		return AR
				.em()
				.createQuery(sb.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAbrangenciasAcordo() {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO);
		sb.append(" and conf.hisDtFim is null");

		return AR
				.em()
				.createQuery(sb.toString()).getResultList();
	}

	public void salvarComoAbrangenciaAcordo() throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
		salvar();


	}

	public void salvarComoPermissaoUsoLista() throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
		salvar();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarPermissoesUsoLista(SrLista lista,
			boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer(
				"select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA);
		sb.append(" and conf.listaPrioridade.hisIdIni = ");
		sb.append(lista.getHisIdIni());

		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");

		sb.append(" order by conf.orgaoUsuario");

		return AR.em().createQuery(sb.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarInclusaoAutomatica(SrLista lista,
			boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer(
				"select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA);
		sb.append(" and conf.listaPrioridade.hisIdIni = ");
		sb.append(lista.getHisIdIni());

		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");

		sb.append(" order by conf.orgaoUsuario");

		return AR.em().createQuery(sb.toString()).getResultList();
	}

	public void salvarComoAssociacaoAtributo() throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		salvar();
	}

	public void salvarComoAssociacaoPesquisa() throws Exception {
		setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_PESQUISA));
		salvar();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesAtributo(SrAtributo atributo, Boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		queryBuilder.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO);
		queryBuilder.append(" and conf.atributo.hisIdIni = ");
		queryBuilder.append(atributo.getHisIdIni());

		if (!mostrarDesativados) {
			queryBuilder.append(" and conf.hisDtFim is null ");
		} else {
			queryBuilder.append(" and conf.idConfiguracao IN (");
			queryBuilder.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			queryBuilder.append(" SrConfiguracao GROUP BY hisIdIni)) ");
		}
		queryBuilder.append(" order by conf.orgaoUsuario");

		return AR.em().createQuery(queryBuilder.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesPesquisa(SrPesquisa pesquisa, Boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		queryBuilder.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_PESQUISA);
		queryBuilder.append(" and conf.pesquisaSatisfacao.hisIdIni = ");
		queryBuilder.append(pesquisa.getHisIdIni());

		if (!mostrarDesativados) {
			queryBuilder.append(" and conf.hisDtFim is null ");
		} else {
			queryBuilder.append(" and conf.idConfiguracao IN (");
			queryBuilder.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			queryBuilder.append(" SrConfiguracao GROUP BY hisIdIni)) ");
		}
		queryBuilder.append(" order by conf.orgaoUsuario");

		return AR.em().createQuery(queryBuilder.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesAtributo(Boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		queryBuilder.append(CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO);

		if (!mostrarDesativados) {
			queryBuilder.append(" and conf.hisDtFim is null ");
		} else {
			queryBuilder.append(" and conf.idConfiguracao IN (");
			queryBuilder.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			queryBuilder.append(" SrConfiguracao GROUP BY hisIdIni)) ");
		}
		queryBuilder.append(" order by conf.orgaoUsuario");

		return AR
				.em()
				.createQuery(queryBuilder.toString())
				.getResultList();
	}

	private static SrConfiguracao buscar(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		return (SrConfiguracao) SrConfiguracaoBL.get().buscaConfiguracao(conf,
				atributosDesconsideradosFiltro, null);
	}

	public static SrConfiguracao buscarDesignacao(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, new int[] { SrConfiguracaoBL.ATENDENTE});
	}

	public static SrConfiguracao buscarDesignacao(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		conf.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, ArrayUtils.addAll(atributosDesconsideradosFiltro,
				new int[] { SrConfiguracaoBL.ATENDENTE }));
	}

	public static SrConfiguracao buscarAssociacao(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		return buscar(conf, new int[] {});
	}

	public static SrConfiguracao buscarAbrangenciaAcordo(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(AR.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
		return buscar(conf, new int[] {});
	}

	public static List<SrConfiguracao> listar(SrConfiguracao conf) throws Exception {
		return listar(conf, new int[] {});
	}

	public static List<SrConfiguracao> listar(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		return SrConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
				atributosDesconsideradosFiltro);
	}

	@Override
	public Long getId() {
		return getIdConfiguracao();
	}

	@Override
	public void setId(Long id) {
		setIdConfiguracao(id);
	}

	public String getDescrItemConfiguracaoAtual() {
		String descrItemConfiguracao = null;
		if (this.itemConfiguracaoSet != null && this.itemConfiguracaoSet.size() > 0) {
			SrItemConfiguracao conf = this.itemConfiguracaoSet.get(this.itemConfiguracaoSet.size() -1);

			if (conf != null) {
				descrItemConfiguracao = conf.getAtual().tituloItemConfiguracao;

				if (this.itemConfiguracaoSet.size() > 1)
					if (descrItemConfiguracao != null)
						descrItemConfiguracao = descrItemConfiguracao.concat(" ...");
					else descrItemConfiguracao = new String("...");
			}
		}
		else
			descrItemConfiguracao = new String();

		return descrItemConfiguracao;
	}

	public String getDescrTipoPermissao() {
		if (this.tipoPermissaoSet != null && this.tipoPermissaoSet.size() > 0) {
			SrTipoPermissaoLista tipoPermissao = this.tipoPermissaoSet.get(0);
			return tipoPermissao.descrTipoPermissaoLista.concat(" ...");
		}
		return "";
	}

	public String getDescrAcaoAtual() {
		String descrAcao = null;
		if (this.acoesSet != null && this.acoesSet.size() > 0) {
			SrAcao acao = this.acoesSet.get(this.acoesSet.size() -1);

			if (acao != null) {
				descrAcao = acao.getAtual().getTituloAcao();

				if (this.acoesSet.size() > 1)
					if (descrAcao != null)
						descrAcao = descrAcao.concat(" ...");
					else
						descrAcao = new String("...");
			}
		}
		else
			descrAcao = new String();

		return descrAcao;
	}

	/**
	 * Método que retorna um número referente ao tipo de solicitante
	 * selecionado. Esse número refere-se ao índice do item selecionado no
	 * componente pessoaLotaFuncCargoSelecao.html
	 *
	 * @return <li>1 para Pessoa; <li>2 para Lotação; <li>3 para Funcao; <li>4
	 *         para Cargo;
	 */
	public int getTipoSolicitante() {
		if (this.getLotacao() != null
				&& this.getLotacao().getLotacaoAtual() != null)
			return 2;
		else if (this.getFuncaoConfianca() != null)
			return 3;
		else if (this.getCargo() != null)
			return 4;
		else if (this.getCpGrupo() != null)
			return 5;
		else
			return 1;
	}

	/**
	 * Retorna um Json de {@link SrConfiguracaoVO} que contém:
	 * <li> {@link SrListaConfiguracaoVO}</li>
	 * <li> {@link SrItemConfiguracaoVO}</li>
	 * <li> {@link SrAcaoVO}</li>
	 *
	 */
	public String getSrConfiguracaoJson() {
		return this.toVO().toJson();
	}

	public String getSrConfiguracaoJson(SrItemConfiguracao itemConfiguracao) {
		JsonObject jsonObject = this.toVO().toJsonObject();
		jsonObject.add("itemConfiguracao", itemConfiguracao.toVO().toJsonObject());

		return jsonObject.toString();
	}


	public String getSrConfiguracaoTipoPermissaoJson() {
		return new SrConfiguracaoVO(this).toJson();
	}

	public static String convertToJSon(List<SrConfiguracao> lista) {
		List<SrConfiguracaoVO> listaVO = new ArrayList<SrConfiguracaoVO>();
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		if (lista != null) {
			for (SrConfiguracao conf : lista) {
				listaVO.add(conf.toVO());
			}
		}
		return gson.toJson(listaVO);
	}

	public static String convertToAssociacaoJSon(List<SrConfiguracao> lista) {
		List<SrConfiguracaoVO> listaVO = new ArrayList<SrConfiguracaoVO>();
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		if (lista != null) {
			for (SrConfiguracao conf : lista) {
				listaVO.add(conf.toVO());
			}
		}

		return gson.toJson(listaVO);
	}

	public int getNivelItemParaComparar() {
		int soma = 0;
		if (itemConfiguracaoSet != null && itemConfiguracaoSet.size() > 0){
			for (SrItemConfiguracao i : itemConfiguracaoSet){
				SrItemConfiguracao iAtual = i.getAtual();
				if (iAtual != null)
					soma += i.getNivel();
			}
			return soma / itemConfiguracaoSet.size();
		}
		return 0;
	}

	public int getNivelAcaoParaComparar() {
		int soma = 0;
		if (acoesSet != null && acoesSet.size() > 0){
			for (SrAcao i : acoesSet){
				SrAcao iAtual = i.getAtual();
				if (iAtual != null)
					soma += i.getNivel();
			}
			return soma / acoesSet.size();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> buscaParaConfiguracaoInsercaoAutomaticaLista(SrLista lista, boolean mostrarDesativados) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select conf from SrConfiguracao as conf ");
		sb.append("where conf.cpTipoConfiguracao.idTpConfiguracao = :tipoConfiguracao ");
		sb.append("and conf.listaPrioridade.hisIdIni = :idIniLista ");

		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		else {
			sb.append(" and conf.idConfiguracao in (");
			sb.append(" SELECT max(idConfiguracao) as idConfiguracao FROM ");
			sb.append(" SrConfiguracao GROUP BY hisIdIni) ");
		}
		return em()
				.createQuery(sb.toString())
				.setParameter("tipoConfiguracao", CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA)
				.setParameter("idIniLista", lista.getIdInicial())
				.getResultList();
	}

	public SrConfiguracaoAssociacaoVO toAssociacaoVO() {
		return new SrConfiguracaoAssociacaoVO(this);
	}

	public SrConfiguracaoVO toVO() {
		return new SrConfiguracaoVO(this, isAtributoObrigatorio());
	}

	public String toJson() {
		return toVO().toJson();
	}

	public static List<SrConfiguracao> listarPorItem(SrItemConfiguracao itemConfiguracao)  throws Exception{
		List<SrConfiguracao> lista = new ArrayList<SrConfiguracao>();

		SrConfiguracao confFiltro = new SrConfiguracao();
		confFiltro.setBuscarPorPerfis(true);
		confFiltro.itemConfiguracaoFiltro = itemConfiguracao;

		lista.addAll(SrItemConfiguracao.marcarComoHerdadas(SrConfiguracao.listarDesignacoes(
					confFiltro, new int[] { SrConfiguracaoBL.ITEM_CONFIGURACAO}), itemConfiguracao));

		return lista;
	}

	public static String buscaParaConfiguracaoInsercaoAutomaticaListaJSON(SrLista lista, boolean mostrarDesativados) throws Exception {
		JsonArray jsonArray = new JsonArray();

		for (SrConfiguracao configuracao : SrConfiguracao.buscaParaConfiguracaoInsercaoAutomaticaLista(lista, mostrarDesativados)) {
			jsonArray.add(configuracao.toVO().toJsonObject());
		}
		return jsonArray.toString();
	}


}
