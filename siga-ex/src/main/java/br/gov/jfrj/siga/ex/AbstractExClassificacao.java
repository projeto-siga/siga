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
 * Created Mon Nov 14 13:36:50 GMT-03:00 2005 
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;

/**
 * A class that represents a row in the EX_CLASSIFICACAO table. You can
 * customize the behavior of this class by editing the class, {@link
 * ExClassificacao()}.
 */
@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarExClassificacaoComExcecao", query = "from ExClassificacao cl "
				+ "	      where cl.codificacao like :mascara"
				+ "	      	and cl.codificacao != :exceto"
				+ "	       	and cl.hisAtivo = 1"
				+ "	       	order by cl.descrClassificacao"),
		@NamedQuery(name = "consultarPorFiltroExClassificacao", query = "select distinct(cla) from ExClassificacao cla ,ExVia v "
				+ "		where 	("
				+ "			(upper(cla.descrClassificacao) like upper('%' || :descrClassificacao || '%')) or"
				+ "			(upper(cla.descrClassificacaoSemAcento) like upper('%' || :descrClassificacaoSemAcento || '%')) or "
				+ "			(cla.codificacao = :descrClassificacao)"
				+ "		)"
				+ "			and cla.codificacao like :mascara"
				+ "	    	and cla.hisAtivo = 1"
				+ "	       	and cla.idClassificacao = v.exClassificacao.idClassificacao "
				+ "	       	and v.hisAtivo = 1" + "	    	order by codificacao"),
		@NamedQuery(name = "consultarFilhosExClassificacao", query = "select distinct(cla) from ExClassificacao cla "
				+ "		where cla.codificacao like :mascara"
				+ "	    	and cla.hisAtivo = 1" + "	    	order by codificacao"),
		@NamedQuery(name = "consultarQuantidadeExClassificacao", query = "select count(distinct cla) from ExClassificacao cla ,ExVia v "
				+ "		where 	("
				+ "			(upper(cla.descrClassificacao) like upper('%' || :descrClassificacao || '%')) or"
				+ "			(upper(cla.descrClassificacaoSemAcento) like upper('%' || :descrClassificacaoSemAcento || '%')) or "
				+ "			(cla.codificacao = :descrClassificacao)"
				+ "		)"
				+ "			and cla.codificacao like :mascara"
				+ "	    	and cla.hisAtivo = 1"
				+ "	       	and cla.idClassificacao = v.exClassificacao.idClassificacao "
				+ "	       	and v.hisAtivo = 1"),
		@NamedQuery(name = "consultarPorSiglaExClassificacao", query = "select cla from ExClassificacao cla "
				+ " 		where cla.codificacao = :codificacao and cla.hisAtivo = 1"),
		@NamedQuery(name = "consultarAtualPorId", query = "select cla from ExClassificacao cla "
				+ " 		where  cla.hisIdIni = :hisIdIni"
				+ "    	and cla.hisAtivo = 1"),
		@NamedQuery(name = "consultarDescricaoExClassificacao", query = "select descrClassificacao from ExClassificacao cla"
				+ "		where cla.codificacao in (:listaCodificacao)"
				+ "		and cla.hisAtivo = 1 " + "		order by codificacao"),
		@NamedQuery(name = "consultarExClassificacaoPorMascara", query = "select distinct(cla) from ExClassificacao cla left join fetch cla.exViaSet"
				+ "		where cla.codificacao like :mascara"
				+ "			and (upper(cla.descrClassificacao) like upper('%' || :descrClassificacao || '%'))"
				+ "	    	and cla.hisAtivo = 1" + "	    	order by codificacao") })
public abstract class AbstractExClassificacao extends HistoricoAuditavelSuporte
		implements Serializable {

	@Id
	@SequenceGenerator(sequenceName = "EX_CLASSIFICACAO_SEQ", name = "EX_CLASSIFICACAO_SEQ")
	@GeneratedValue(generator = "EX_CLASSIFICACAO_SEQ")
	@Column(name = "ID_CLASSIFICACAO", unique = true, nullable = false)
	private Long idClassificacao;

	@Column(name = "CODIFICACAO", nullable = false, length = 13)
	private String codificacao;

	@Column(name = "DESCR_CLASSIFICACAO", nullable = false, length = 4000)
	private String descrClassificacao;

	@Formula("REMOVE_ACENTO(DESCR_CLASSIFICACAO)")
	private String descrClassificacaoSemAcento;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exClassificacao")
	@Cache(region = ExDao.CACHE_EX, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ExVia> exViaSet;

	@Column(name = "OBS", length = 4000)
	private String obs;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exClassificacao")
	private Set<ExModelo> exModeloSet;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exClassCriacaoVia")
	private Set<ExModelo> exModeloCriacaoViaSet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_ID_INI", insertable=false, updatable=false)
	private ExClassificacao classificacaoInicial;

	/**
	 * Simple constructor of AbstractExClassificacao instances.
	 */
	public AbstractExClassificacao() {
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof ExClassificacao))
			return false;
		final ExClassificacao that = (ExClassificacao) rhs;
		if ((this.getIdClassificacao() == null ? that.getIdClassificacao() == null
				: this.getIdClassificacao().equals(that.getIdClassificacao()))) {
			if ((this.getDescrClassificacao() == null ? that
					.getDescrClassificacao() == null : this
					.getDescrClassificacao().equals(
							that.getDescrClassificacao())))
				return true;
		}
		return false;
	}

	public ExClassificacao getClassificacaoInicial() {
		return classificacaoInicial;
	}

	public String getCodAssunto() {
		return MascaraUtil.getInstance().getCampoDaMascara(0, getCodificacao());
	}

	public String getCodificacao() {
		return codificacao;
	}

	/**
	 * Return the value of the DESCR_CLASSIFICACAO column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrClassificacao() {
		return this.descrClassificacao;
	}

	public String getDescrClassificacaoSemAcento() {
		return descrClassificacaoSemAcento;
	}

	public Set<ExModelo> getExModeloCriacaoViaSet() {
		return exModeloCriacaoViaSet;
	}

	public Set<ExModelo> getExModeloSet() {
		return exModeloSet;
	}

	/**
	 * Return the value of the ID_CLASSIFICACAO collection.
	 * 
	 * @return ExVia
	 */
	public java.util.Set<ExVia> getExViaSet() {
		return this.exViaSet;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdClassificacao() {
		return idClassificacao;
	}

	/**
	 * Return the value of the FACILITADOR_CLASS column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getObs() {
		return this.obs;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdClassificacao() == null ? 0 : this
				.getIdClassificacao().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrClassificacao() == null ? 0 : this
				.getDescrClassificacao().hashCode();
		result = result * 37 + idValue;

		return result;
	}

	// public void setCodAssunto(java.lang.Byte codAssunto) {
	// this.codAssunto = codAssunto;
	// }

	public void setClassificacaoInicial(ExClassificacao classificacaoInicial) {
		this.classificacaoInicial = classificacaoInicial;
	}

	public void setCodificacao(String codificacao) {
		this.codificacao = codificacao;
	}

	/**
	 * Set the value of the DESCR_CLASSIFICACAO column.
	 * 
	 * @param descrClassificacao
	 */
	public void setDescrClassificacao(final java.lang.String descrClassificacao) {
		this.descrClassificacao = descrClassificacao;
	}

	public void setDescrClassificacaoSemAcento(
			String descrClassificacaoSemAcento) {
		this.descrClassificacaoSemAcento = descrClassificacaoSemAcento;
	}

	public void setExModeloCriacaoViaSet(Set<ExModelo> exModeloCriacaoViaSet) {
		this.exModeloCriacaoViaSet = exModeloCriacaoViaSet;
	}

	public void setExModeloSet(final Set modeloSet) {
		this.exModeloSet = modeloSet;
	}

	/**
	 * Set the value of the ID_CLASSIFICACAO collection.
	 * 
	 * @param exViaSet
	 */
	public void setExViaSet(final java.util.Set<ExVia> exViaSet) {
		this.exViaSet = exViaSet;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idClassificacao
	 */
	public void setIdClassificacao(final java.lang.Long idClassificacao) {
		this.idClassificacao = idClassificacao;
	}

	/**
	 * Set the value of the FACILITADOR_CLASS column.
	 * 
	 * @param facilitadorClass
	 */
	public void setObs(final java.lang.String obs) {
		this.obs = obs;
	}

}
