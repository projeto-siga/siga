package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.SrListaConfiguracao.SrListaConfiguracaoVO;

import org.hibernate.annotations.Type;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Selecionavel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne
	@JoinColumn(name = "ID_ACAO")
	public SrAcao acao;

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
	public SrTipoAtributo tipoAtributo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESQUISA")
	public SrPesquisa pesquisaSatisfacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	public SrLista listaPrioridade;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = SrListaConfiguracao.class, mappedBy = "configuracao")
	private List<SrListaConfiguracao> listaConfiguracaoSet;

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
	
	//Edson: mudei de boolean pra Boolean porque havia configs no banco
	//com FG_DIVULGAR_SLA nulo (havia duas versoes parelelas), o que
	//gerava "Null value was assigned to a property of primitive type" 
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
	
	@Column(name = "TIPO_PERMISSAO")
	@Enumerated
	public SrTipoPermissaoLista tipoPermissao;
	
	@Transient
	public SrSubTipoConfiguracao subTipoConfig;
	
	@Transient
	public boolean isHerdado;
	
	@Transient
	public boolean utilizarItemHerdado;
	
	public SrConfiguracao() {
		
	}
	
	public Selecionavel getSolicitante() {
		if (this.getDpPessoa() != null)
			return this.getDpPessoa();
		else if (this.getLotacao() != null)
			return this.getLotacao();
		else if (this.getCargo() != null)
			return this.getCargo();
		else
			return this.getFuncaoConfianca();
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

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarDesignacoes(boolean mostrarDesativados, Long idItemConfiguracao) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
		
		if (idItemConfiguracao != null) {
			sb.append(" and item.idItemConfiguracao = ");
			sb.append(idItemConfiguracao);
		}
		
		if (!mostrarDesativados)
			sb.append(" and conf.hisDtFim is null");
		
		sb.append(" order by item.siglaItemConfiguracao, conf.orgaoUsuario ");
		
		return JPA
				.em()
				.createQuery(
						"select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario")
				.getResultList();
	}
	
	public void salvarComoPermissaoUsoLista() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
		salvar();
	}

	public static List<SrConfiguracao> listarPermissoesUsoLista(DpLotacao lota, boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA);
		sb.append(" and conf.listaPrioridade.lotaCadastrante.idLotacaoIni = ");
		sb.append(lota.getLotacaoInicial().getIdLotacao());
		
		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");
		
		sb.append(" order by conf.orgaoUsuario");
		
		return JPA
				.em()
				.createQuery(sb.toString(), SrConfiguracao.class).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarPermissoesUsoLista(SrLista lista, boolean mostrarDesativado) {
		StringBuffer sb = new StringBuffer("select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = ");
		sb.append(CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA);
		sb.append(" and conf.listaPrioridade.hisIdIni = ");
		sb.append(lista.getHisIdIni());
		
		if (!mostrarDesativado)
			sb.append(" and conf.hisDtFim is null ");
		
		sb.append(" order by conf.orgaoUsuario");
		
		return JPA
				.em()
				.createQuery(sb.toString()).getResultList();
	}

	public void salvarComoAssociacaoTipoAtributo() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		salvar();
	}

	@SuppressWarnings("unchecked")
	public static List<SrConfiguracao> listarAssociacoesTipoAtributo() {
		return JPA
				.em()
				.createQuery(
						"select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario")
				.getResultList();
	}

	public static SrConfiguracao buscar(SrConfiguracao conf) throws Exception {
		return buscar(conf, new int[] {});
	}

	public static SrConfiguracao buscar(SrConfiguracao conf,
			int[] atributosDesconsideradosFiltro) throws Exception {
		return (SrConfiguracao) SrConfiguracaoBL.get().buscaConfiguracao(conf,
				atributosDesconsideradosFiltro, null);
	}

	@Override
	public Long getId() {
		return getIdConfiguracao();
	}

	@Override
	public void setId(Long id) {
		setIdConfiguracao(id);
	}

	// Edson: Não consegui fazer com que esse cascade fosse automático.
	@Override
	public void salvar() throws Exception {
		super.salvar();
		if (this.listaConfiguracaoSet != null)
			for (SrListaConfiguracao lista: this.listaConfiguracaoSet) {
				lista.configuracao = this;
				lista.salvar();
			}
	}
	
	public List<SrListaConfiguracao> getListaConfiguracaoSet() {
		return listaConfiguracaoSet;
	}
	
	public void setListaConfiguracaoSet(
			List<SrListaConfiguracao> listaConfiguracaoSet) {
		this.listaConfiguracaoSet = listaConfiguracaoSet;
	}
	
	/**
	 * Método que retorna um número referente ao tipo de solicitante selecionado. Esse número
	 * refere-se ao índice do item selecionado no componente pessoaLotaFuncCargoSelecao.html
	 * 
	 * @return 
	 * <li>1 para Pessoa;
	 * <li>2 para Lotação;
	 * <li>3 para Funcao;
	 * <li>4 para Cargo;
	 */
	public int getTipoSolicitante() {
		if (this.getLotacao() != null && this.getLotacao().getLotacaoAtual() != null)
			return 2;
		else if (this.getFuncaoConfianca() != null)
			return 3;
		else if (this.getCargo() != null)
			return 4;
		else return 1;
	}
	
	/**
	 * Retorna um Json de {@link SrConfiguracaoVO} que contém as {@link SrListaConfiguracaoVO}
	 * vinculadas a ele.
	 */
	public String getListaConfiguracaoSetJson() {
		return new SrConfiguracaoVO(listaConfiguracaoSet).toJson();
	}
	
	/**
	 * Classe que representa um {@link SrConfiguracaoVO VO} da classe {@link SrConfiguracao}.
	 * 
	 * @author DB1
	 */
	public class SrConfiguracaoVO {
		public List<SrListaConfiguracao.SrListaConfiguracaoVO> listaConfiguracaoSetVO; 

		public SrConfiguracaoVO(List<SrListaConfiguracao> listaConfiguracaoSet) {
			listaConfiguracaoSetVO = new ArrayList<SrListaConfiguracao.SrListaConfiguracaoVO>();
			
			for (SrListaConfiguracao item : listaConfiguracaoSet) {
				listaConfiguracaoSetVO.add(item.toVO());
			}
		}
		
		/**
		 * Converte o objeto para Json.
		 */
		public String toJson() {
			GsonBuilder builder = new GsonBuilder();
	        builder.setPrettyPrinting().serializeNulls();
	        Gson gson = builder.create();
	        
	        return gson.toJson(this);
		}
	}

	/**
	 * Marca os itens como  herdados.
	 */
	public static List<SrConfiguracao> marcarComoHerdadas(List<SrConfiguracao> listasDesignacoesPai, SrItemConfiguracao item) {
		Iterator<SrConfiguracao> i = listasDesignacoesPai.iterator();
		
		while (i.hasNext()) {
			SrConfiguracao conf = i.next();
			boolean encontrou = false;
			
			conf.isHerdado = true;
			conf.utilizarItemHerdado = true;
			
			List<SrConfiguracaoIgnorada> itensIgnorados = SrConfiguracaoIgnorada.findByConfiguracao(conf);
			
			for (SrConfiguracaoIgnorada igItem : itensIgnorados) {
				// Se a configuração for do Item, vai como desmarcado
				if (item.getId().equals(igItem.itemConfiguracao.getId())) {
					conf.utilizarItemHerdado = false;
				}
				
				// se a configuração for do Item (histórico), vai como desmarcado
				else if (item.getHistoricoItemConfiguracao() != null && item.getHistoricoItemConfiguracao().size() > 0) {
					for (SrItemConfiguracao itemHist : item.getHistoricoItemConfiguracao()) {
						if (itemHist.getId().equals(igItem.itemConfiguracao.getId())) {
							conf.utilizarItemHerdado = false;
							encontrou = true;
							break;
						}
					}
				}
				
				else {
					SrItemConfiguracao itemPai = item.getPaiPorSigla();
					
					while(itemPai != null) {
						
						// Se for configuração do pai, não aparece na tela caso esteja marcada para Ignorar no Pai
						if (itemPai.getId().equals(igItem.itemConfiguracao.getId())) {
							i.remove();
							break;
						}
						else
							itemPai = itemPai.getPaiPorSigla();
					}
				}
				
				// Caso tenha encontrado a configuração correta, interrompe o loop
				if (encontrou)
					break;
			}
		}
		
		return listasDesignacoesPai;
	}
	
}
