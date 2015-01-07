package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.SrAcao.SrAcaoVO;
import models.vo.SrConfiguracaoAssociacaoVO;
import models.vo.SrConfiguracaoVO;
import models.vo.SrItemConfiguracaoVO;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.Type;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Selecionavel;

@Entity
@Table(name = "SR_CONFIGURACAO", schema = "SIGASR")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracao extends CpConfiguracao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4959384444345462871L;

	@Column(name = "FORMA_ACOMPANHAMENTO")
	public SrFormaAcompanhamento formaAcompanhamento;

	@Transient
	public SrItemConfiguracao itemConfiguracaoFiltro;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ITEM", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ITEM_CONFIGURACAO")})
	public List<SrItemConfiguracao> itemConfiguracaoSet;
	
	@Transient
	public SrAcao acaoFiltro;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="SR_CONFIGURACAO_ACAO", schema = "SIGASR", joinColumns={@JoinColumn(name="ID_CONFIGURACAO")}, inverseJoinColumns={@JoinColumn(name="ID_ACAO")})
	public List<SrAcao> acoesSet;

	@Column(name = "GRAVIDADE")
	public SrGravidade gravidade;

	@Column(name = "TENDENCIA")
	public SrTendencia tendencia;

	@Column(name = "URGENCIA")
	public SrUrgencia urgencia;

	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	public DpLotacao atendente;

	@ManyToOne
	@JoinColumn(name = "ID_POS_ATENDENTE")
	public DpLotacao posAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_EQUIPE_QUALIDADE")
	public DpLotacao equipeQualidade;

	@ManyToOne
	@JoinColumn(name = "ID_PRE_ATENDENTE")
	public DpLotacao preAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_ATRIBUTO")
	public SrAtributo atributo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	public SrPesquisa pesquisaSatisfacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	public SrLista listaPrioridade;
	
	@Enumerated
	public SrPrioridade prioridade;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SR_LISTA_CONFIGURACAO", schema="SIGASR", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "ID_LISTA"))
	private List<SrLista> listaConfiguracaoSet;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="SR_CONFIGURACAO_PERMISSAO", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "TIPO_PERMISSAO"), schema="SIGASR")
	public List<SrTipoPermissaoLista> tipoPermissaoSet;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ACORDO")
	public SrAcordo acordo;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	public boolean atributoObrigatorio;

	@Column(name = "SLA_PRE_ATENDIMENTO_QUANT")
	public Integer slaPreAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_PRE_ATENDIMENTO")
	public CpUnidadeMedida unidadeMedidaPreAtendimento;

	@Column(name = "SLA_ATENDIMENTO_QUANT")
	public Integer slaAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_ATENDIMENTO")
	public CpUnidadeMedida unidadeMedidaAtendimento;

	@Column(name = "SLA_POS_ATENDIMENTO_QUANT")
	public Integer slaPosAtendimentoQuantidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_POS_ATENDIMENTO")
	public CpUnidadeMedida unidadeMedidaPosAtendimento;

	@Column(name = "MARGEM_SEGURANCA")
	public Integer margemSeguranca;

	@Lob
	@Column(name = "OBSERVACAO_SLA", length = 8192)
	public String observacaoSLA;

	@Column(name = "FG_DIVULGAR_SLA")
	@Type(type = "yes_no")
	public Boolean divulgarSLA;

	@Column(name = "FG_NOTIFICAR_GESTOR")
	@Type(type = "yes_no")
	public Boolean notificarGestor;

	@Column(name = "FG_NOTIFICAR_SOLICITANTE")
	@Type(type = "yes_no")
	public Boolean notificarSolicitante;

	@Column(name = "FG_NOTIFICAR_CADASTRANTE")
	@Type(type = "yes_no")
	public Boolean notificarCadastrante;

	@Column(name = "FG_NOTIFICAR_INTERLOCUTOR")
	@Type(type = "yes_no")
	public Boolean notificarInterlocutor;

	@Column(name = "FG_NOTIFICAR_ATENDENTE")
	@Type(type = "yes_no")
	public Boolean notificarAtendente;

	@Transient
	public SrSubTipoConfiguracao subTipoConfig;

	@Transient
	public boolean isHerdado;

	@Transient
	public boolean utilizarItemHerdado;

	public SrConfiguracao() {
	}

	public SrConfiguracao(DpPessoa solicitante, CpComplexo local, SrItemConfiguracao item) {
		this.setDpPessoa(solicitante);
		this.setComplexo(local);
		this.itemConfiguracaoFiltro = item;
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
		return atributoObrigatorio ? "Sim" : "Não";
	}

	public void salvarComoDesignacao() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		salvar();
	}

	public void salvarComoInclusaoAutomaticaLista(SrLista srLista) throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA));
		adicionarListaConfiguracoes(srLista);
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
		
		return JPA
				.em()
				.createQuery(sb.toString()).getResultList();
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
		
		return JPA
				.em()
				.createQuery(sb.toString()).getResultList();
	}
	
	public void salvarComoAbrangenciaAcordo() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ABRANGENCIA_ACORDO));
		salvar();
		
		
	}

	public void salvarComoPermissaoUsoLista() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
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

		return JPA.em().createQuery(sb.toString()).getResultList();
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

		return JPA.em().createQuery(sb.toString()).getResultList();
	}

	public void salvarComoAssociacaoAtributo() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
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
		
		return JPA.em().createQuery(queryBuilder.toString()).getResultList();
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
		
		return JPA
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
		conf.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, new int[] { SrConfiguracaoBL.ATENDENTE});
	}
	
	public static SrConfiguracao buscarDesignacao(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		conf.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		return buscar(conf, ArrayUtils.addAll(atributosDesconsideradosFiltro,
				new int[] { SrConfiguracaoBL.ATENDENTE }));
	}
	
	public static SrConfiguracao buscarAssociacao(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		return buscar(conf, new int[] {});
	}
	
	public static SrConfiguracao buscarAbrangenciaAcordo(SrConfiguracao conf)
			throws Exception {
		conf.setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
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

	public List<SrLista> getListaConfiguracaoSet() {
		return listaConfiguracaoSet;
	}

	public void setListaConfiguracaoSet(List<SrLista> listaConfiguracaoSet) {
		this.listaConfiguracaoSet = listaConfiguracaoSet;
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
				descrAcao = acao.getAtual().tituloAcao;
				
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

	public String getSrConfiguracaoTipoPermissaoJson() {
		return new SrConfiguracaoVO(null, null, null, tipoPermissaoSet, getDescrConfiguracao()).toJson();
	}
	
	public SrConfiguracaoVO toVO() {
		return new SrConfiguracaoVO(listaConfiguracaoSet, itemConfiguracaoSet, acoesSet, null, getDescrConfiguracao());
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

	public SrItemConfiguracao getItemConfiguracaoUnitario() {
		if (itemConfiguracaoSet == null || itemConfiguracaoSet.size() == 0)
			return null;
		return itemConfiguracaoSet.get(0);
	}

	public void setItemConfiguracaoUnitario(SrItemConfiguracao itemConfiguracao) {
		itemConfiguracaoSet = new ArrayList<SrItemConfiguracao>();
		itemConfiguracaoSet.add(itemConfiguracao);
	}

	public SrAcao getAcaoUnitaria() {
		if (acoesSet == null || acoesSet.size() == 0)
			return null;
		return acoesSet.get(0);
	}

	public void setAcaoUnitaria(SrAcao acao) {
		acoesSet = new ArrayList<SrAcao>();
		acoesSet.add(acao);
	}

	@Override
	public CpConfiguracao getConfiguracaoAtual() {
		return super.getConfiguracaoAtual();
	}
	
	public static SrConfiguracao buscarConfiguracaoInsercaoAutomaticaLista(SrLista lista) throws Exception {
		SrLista listaAtual = lista.getListaAtual();
		
		for (CpConfiguracao cpConfiguracao : SrConfiguracaoBL.get().getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA)) {
			SrConfiguracao srConfiguracao = (SrConfiguracao) cpConfiguracao;
			// DB: Nao implementei utilizando "contains" na lista por que implementacao do super.equals esta comparando instancias e nao iria funcionar nesse caso
			for (SrLista listaEncontrada : srConfiguracao.getListaConfiguracaoSet()) {
				// DB1: Conversamos com o Edson e por enquanto sera apenas uma configuracao para cada lista.
				if (srConfiguracao.getListaConfiguracaoSet() != null && listaEncontrada.getId().equals(listaAtual.getId())) {
					return srConfiguracao;
				}
			}
		}
		return new SrConfiguracao();
	}

	public void adicionarListaConfiguracoes(SrLista srLista) {
		if (this.listaConfiguracaoSet == null) {
			this.listaConfiguracaoSet = new ArrayList<SrLista>();
		}
		if (!this.listaConfiguracaoSet.contains(srLista)) {
			this.listaConfiguracaoSet.add(srLista);
		}
	}

	public SrConfiguracaoAssociacaoVO toAssociacaoVO() {
		SrItemConfiguracaoVO itemVO = (this.getItemConfiguracaoUnitario() != null? this.getItemConfiguracaoUnitario().getAtual().toVO() : null);
		SrAcaoVO acaoVO = (this.getAcaoUnitaria() != null? this.getAcaoUnitaria().getAtual().toVO() : null);
		
		return new SrConfiguracaoAssociacaoVO(this.getIdConfiguracao(), itemVO, acaoVO, this.atributoObrigatorio);
	}
}
