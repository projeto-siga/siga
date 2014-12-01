package models;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_TIPO_ATRIBUTO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrAtributo extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_TIPO_ATRIBUTO_SEQ", name = "srTipoAtributoSeq")
	@GeneratedValue(generator = "srTipoAtributoSeq")
	@Column(name = "ID_TIPO_ATRIBUTO")
	public Long idAtributo;

	@Column(name = "NOME")
	public String nomeAtributo;

	@Column(name = "DESCRICAO")
	public String descrAtributo;

	@Column(name = "FORMATOCAMPO")
	@Enumerated
	public SrTipoAtributo tipoAtributo;
	
	@Column(name = "DESCR_PRE_DEFINIDO")
	public String descrPreDefinido;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAtributo atributoInicial;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "atributoInicial", fetch = FetchType.EAGER)
	@OrderBy("hisDtIni desc")
	public List<SrAtributo> meuAtributoHistoricoSet;
	
	@Transient
	public List<SrConfiguracao> associacoes;

	@Override
	public Long getId() {
		return idAtributo;
	}

	@Override
	public void setId(Long id) {
		idAtributo = id;
	}

	public static List<SrAtributo> listar(boolean mostrarDesativados) {
		StringBuilder queryBuilder = new StringBuilder();

		if (!mostrarDesativados) {
			queryBuilder.append(" hisDtFim is null");
		} else {
			queryBuilder.append("SELECT ta FROM SrAtributo ta ");
			queryBuilder.append("WHERE ta.idAtributo in (SELECT MAX(idAtributo) FROM SrAtributo GROUP BY hisIdIni) ");
		}
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
		if (tipoAtributo == SrTipoAtributo.VL_PRE_DEFINIDO){
			preDefinidos.addAll(Arrays.asList(descrPreDefinido.split(";"))); 
		}
		return preDefinidos;
	}

	public List<SrConfiguracao> getAssociacoes(DpLotacao lotaTitular, DpPessoa pess) {
		try {
			SrConfiguracao confFiltro = new SrConfiguracao();
			confFiltro.setLotacao(lotaTitular);
			confFiltro.setDpPessoa(pess);
			confFiltro.setCpTipoConfiguracao(JPA.em().find(
					CpTipoConfiguracao.class,
					CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
			return SrConfiguracao.listar(confFiltro, new int[] { SrConfiguracaoBL.TIPO_ATRIBUTO });
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
