package br.gov.jfrj.siga.sr.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sr.model.vo.SrAtributoVO;

@Entity
@Table(name = "SR_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrAtributo extends HistoricoSuporte {
	private static final long serialVersionUID = 1L;
	
	public static ActiveRecord<SrAtributo> AR = new ActiveRecord<>(SrAtributo.class);	
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ATRIBUTO_SEQ", name = "srAtributoSeq")
	@GeneratedValue(generator = "srAtributoSeq")
	@Column(name = "ID_ATRIBUTO")
	public Long idAtributo;

	@Column(name = "NOME")
	public String nomeAtributo;

	@Column(name = "DESCRICAO")
	public String descrAtributo;

	@Column(name = "TIPO_ATRIBUTO")
	@Enumerated
	public SrTipoAtributo tipoAtributo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_OBJETIVO")
	public SrObjetivoAtributo objetivoAtributo;

	@Column(name = "DESCR_PRE_DEFINIDO")
	public String descrPreDefinido;

	@Column(name = "CODIGO_ATRIBUTO")
	public String codigoAtributo;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAtributo atributoInicial;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "atributoInicial", fetch = FetchType.LAZY)
	//@OrderBy("hisDtIni desc")
	public List<SrAtributo> meuAtributoHistoricoSet;

	@Override
	public Long getId() {
		return idAtributo;
	}

	@Override
	public void setId(Long id) {
		idAtributo = id;
	}

	public static List<SrAtributo> listarParaSolicitacao(
			boolean mostrarDesativados) {
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.findById(SrObjetivoAtributo.OBJETIVO_SOLICITACAO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaAcordo(boolean mostrarDesativados) {
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.findById(SrObjetivoAtributo.OBJETIVO_ACORDO);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listarParaIndicador(
			boolean mostrarDesativados) {
		SrObjetivoAtributo obj = SrObjetivoAtributo
				.findById(SrObjetivoAtributo.OBJETIVO_INDICADOR);
		return listar(obj, mostrarDesativados);
	}

	public static List<SrAtributo> listar(SrObjetivoAtributo objetivo,
			boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();

		if (!mostrarDesativados) {
			queryBuilder.append(" hisDtFim is null");
		} else {
			queryBuilder.append("SELECT ta FROM SrAtributo ta ");
			queryBuilder
					.append("WHERE ta.idAtributo in (SELECT MAX(idAtributo) FROM SrAtributo GROUP BY hisIdIni) ");
		}

		if (objetivo != null)
			queryBuilder.append(" and objetivoAtributo.idObjetivo = "
					+ objetivo.idObjetivo);

		return SrAtributo.find(queryBuilder.toString()).fetch();
	}

	public List<SrAtributo> getHistoricoAtributo() {
		if (atributoInicial != null)
			return atributoInicial.meuAtributoHistoricoSet;
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
		Set<String> preDefinidos = new HashSet<String>();
		if (tipoAtributo == SrTipoAtributo.VL_PRE_DEFINIDO) {
			preDefinidos.addAll(Arrays.asList(descrPreDefinido.split(";")));
		}
		return preDefinidos;
	}

	public List<SrConfiguracao> getAssociacoes(DpLotacao lotaTitular,
			DpPessoa pess) {
		try {
			SrConfiguracao confFiltro = new SrConfiguracao();
			confFiltro.setLotacao(lotaTitular);
			confFiltro.setDpPessoa(pess);
			confFiltro
					.setCpTipoConfiguracao(JPA
							.em()
							.find(CpTipoConfiguracao.class,
									CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
			return SrConfiguracao.listar(confFiltro,
					new int[] { SrConfiguracaoBL.TIPO_ATRIBUTO });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SrAtributo get(String codigo) {
		return SrAtributo.find("byCodigoAtributo", codigo).first();
	}

	@Override
	public void salvar() throws Exception {

		if (objetivoAtributo == null)
			throw new IllegalStateException("Objetivo nao informado");

		super.salvar();
	}

	public String asGetter() {
		if (codigoAtributo == null || codigoAtributo.isEmpty())
			return null;
		return "get" + codigoAtributo.substring(0, 1).toUpperCase()
				+ codigoAtributo.substring(1);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.idAtributo.equals(((SrAtributo)obj).idAtributo);
	}
	
	public SrAtributoVO toVO(boolean listarAssociacoes) {
		return SrAtributoVO.createFrom(this, listarAssociacoes);
	}
	
	/**
	 * Retorna um Json de {@link SrAtributo}.
	 */
	public String toJson(boolean listarAssociacoes) {
		return this.toVO(listarAssociacoes).toJson();
	}
	
	/**
	 * Retorna um Json de {@link SrAtributo}.
	 */
	public String toJson() {
		return this.toVO(false).toJson();
	}
}
