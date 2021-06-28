package br.gov.jfrj.siga.sr.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sr.model.enm.SrTipoDeConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.SrAtributoVO;

@Entity
@Table(name = "sr_atributo", schema = "sigasr")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrAtributo extends HistoricoSuporte implements SrSelecionavel, Selecionavel {
	private static final long serialVersionUID = 1L;
	private static final String DESCRICAO_VAZIA = "";
	
	public static final ActiveRecord<SrAtributo> AR = new ActiveRecord<>(SrAtributo.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGASR" +".SR_ATRIBUTO_SEQ", name = "srAtributoSeq")
	@GeneratedValue(generator = "srAtributoSeq")
	@Column(name = "ID_ATRIBUTO")
	private Long idAtributo;

	@Column(name = "NOME")
	private String nomeAtributo;

	@Column(name = "DESCRICAO")
	private String descrAtributo;

	@Column(name = "TIPO_ATRIBUTO")
	@Enumerated
	private SrTipoAtributo tipoAtributo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_OBJETIVO")
	private SrObjetivoAtributo objetivoAtributo;

	@Column(name = "DESCR_PRE_DEFINIDO")
	private String descrPreDefinido;

	@Column(name = "CODIGO_ATRIBUTO")
	private String codigoAtributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrAtributo atributoInicial;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "atributoInicial", fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	private List<SrAtributo> meuAtributoHistoricoSet;

	@Override
	public Long getId() {
		return getIdAtributo();
	}

	@Override
	public void setId(Long id) {
		setIdAtributo(id);
	}

	public static List<SrAtributo> listarParaSolicitacao(boolean mostrarDesativados) throws Exception {
		SrObjetivoAtributo obj = SrObjetivoAtributo.AR.findById(SrObjetivoAtributo.OBJETIVO_SOLICITACAO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaAcordo(boolean mostrarDesativados) throws Exception {
		SrObjetivoAtributo obj = SrObjetivoAtributo.AR.findById(SrObjetivoAtributo.OBJETIVO_ACORDO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaIndicador(boolean mostrarDesativados) throws Exception {
		SrObjetivoAtributo obj = SrObjetivoAtributo.AR.findById(SrObjetivoAtributo.OBJETIVO_INDICADOR);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listar(SrObjetivoAtributo objetivo, boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();

		if (!mostrarDesativados) {
			queryBuilder.append(" hisDtFim is null");
		} else {
			queryBuilder.append("SELECT ta FROM SrAtributo ta ");
			queryBuilder.append("WHERE ta.idAtributo in (SELECT MAX(idAtributo) FROM SrAtributo GROUP BY hisIdIni) ");
		}

		if (objetivo != null)
			queryBuilder.append(" and objetivoAtributo.idObjetivo = " + objetivo.getIdObjetivo());

		return SrAtributo.AR.find(queryBuilder.toString()).fetch();
	}

	public List<SrAtributo> getHistoricoAtributo() {
		if (getAtributoInicial() != null)
			return getAtributoInicial().getMeuAtributoHistoricoSet();
		return null;
	}

	public SrAtributo getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAtributo> sols = getHistoricoAtributo();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public Set<String> getPreDefinidoSet() {
		Set<String> preDefinidos = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		if (getTipoAtributo() == SrTipoAtributo.VL_PRE_DEFINIDO) {
			preDefinidos.addAll(Arrays.asList(getDescrPreDefinido().split(";")));
		}
		return preDefinidos;
	}

	public List<SrConfiguracaoCache> getAssociacoes(DpLotacao lotaTitular, DpPessoa pess) {
		try {
			SrConfiguracao confFiltro = new SrConfiguracao();
			confFiltro.setLotacao(lotaTitular);
			confFiltro.setDpPessoa(pess);
			confFiltro.setCpTipoConfiguracao(SrTipoDeConfiguracao.ASSOCIACAO_TIPO_ATRIBUTO);
			return SrConfiguracao.listar(confFiltro, new int[] { SrConfiguracaoBL.TIPO_ATRIBUTO });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SrAtributo get(String codigo) {
		return SrAtributo.AR.find("byCodigoAtributo", codigo).first();
	}

	public void salvar() throws Exception {

		if (getObjetivoAtributo() == null)
			throw new IllegalStateException("Objetivo nao informado");

		super.salvarComHistorico();
	}

	public String asGetter() {
		if (getCodigoAtributo() == null || getCodigoAtributo().isEmpty())
			return null;
		return "get" + getCodigoAtributo().substring(0, 1).toUpperCase() + getCodigoAtributo().substring(1);
	}

	@Override
	public boolean equals(Object obj) {
		return this.getAtributoInicial().getId().equals(((SrAtributo) obj).getAtributoInicial().getId());
	}
	
	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
		result = prime * result
				+ ((getAtributoInicial() == null) ? 0 : getAtributoInicial().getId().hashCode());
		return result;
	}
	
	public SrAtributoVO toVO(boolean listarAssociacoes) throws Exception {
		return SrAtributoVO.createFrom(this, listarAssociacoes);
	}

	/**
	 * Retorna um Json de {@link SrAtributo}.
	 * @throws Exception 
	 */
	public String toJson(boolean listarAssociacoes) throws Exception {
		return this.toVO(listarAssociacoes).toJson();
	}

	/**
	 * Retorna um Json de {@link SrAtributo}.
	 * @throws Exception 
	 */
	public String toJson() throws Exception {
		return this.toVO(false).toJson();
	}

	public Long getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}

	public String getNomeAtributo() {
		return nomeAtributo;
	}

	public void setNomeAtributo(String nomeAtributo) {
		this.nomeAtributo = nomeAtributo;
	}

	public String getDescrAtributo() {
		return descrAtributo;
	}

	public void setDescrAtributo(String descrAtributo) {
		this.descrAtributo = descrAtributo;
	}

	public SrTipoAtributo getTipoAtributo() {
		return tipoAtributo;
	}

	public void setTipoAtributo(SrTipoAtributo tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}

	public SrObjetivoAtributo getObjetivoAtributo() {
		return objetivoAtributo;
	}

	public void setObjetivoAtributo(SrObjetivoAtributo objetivoAtributo) {
		this.objetivoAtributo = objetivoAtributo;
	}

	public String getDescrPreDefinido() {
		return descrPreDefinido;
	}

	public void setDescrPreDefinido(String descrPreDefinido) {
		this.descrPreDefinido = descrPreDefinido;
	}

	public String getCodigoAtributo() {
		return codigoAtributo;
	}

	public void setCodigoAtributo(String codigoAtributo) {
		this.codigoAtributo = codigoAtributo;
	}

	public SrAtributo getAtributoInicial() {
		return atributoInicial;
	}

	public void setAtributoInicial(SrAtributo atributoInicial) {
		this.atributoInicial = atributoInicial;
	}

	public List<SrAtributo> getMeuAtributoHistoricoSet() {
		return meuAtributoHistoricoSet;
	}

	public void setMeuAtributoHistoricoSet(List<SrAtributo> meuAtributoHistoricoSet) {
		this.meuAtributoHistoricoSet = meuAtributoHistoricoSet;
	}

	@Override
	public String getSigla() {
		return getNomeAtributo();
	}

	@Override
	public void setSigla(String sigla) {
		setNomeAtributo(sigla);
	}

	@Override
	public String getDescricao() {
		return DESCRICAO_VAZIA;
	}

	@Override
	public void setDescricao(String descricao) {
		
	}

	@Override
	public List<SrAtributo> buscar() throws Exception {	
		boolean mostrarDesativados = false;
		List<SrAtributo> todosOsAtributos = listarParaSolicitacao(mostrarDesativados);
		List<SrAtributo> atributosFiltrados = new ArrayList<SrAtributo>();
		
		if (getNomeAtributo() == null || "".equals(getNomeAtributo()))
			return todosOsAtributos;
		
		for (SrAtributo atributo : todosOsAtributos)
			if (Texto.removeAcento(atributo.getNomeAtributo())
					.toLowerCase()
					.contains( Texto.removeAcento(this.getNomeAtributo())
									.toLowerCase()) )
				atributosFiltrados.add(atributo);
		
		return atributosFiltrados;
	}
	
	@Override
	public SrAtributo selecionar(String sigla) throws Exception {
		setSigla(sigla);
		List<SrAtributo> atributos = buscar();
		return atributos.size() == 1 ? atributos.get(0) : null;
	}
}
