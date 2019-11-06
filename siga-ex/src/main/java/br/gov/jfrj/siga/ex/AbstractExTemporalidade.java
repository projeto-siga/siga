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
 * Created Mon Nov 14 13:33:06 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Where;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;

/**
 * A class that represents a row in the EX_TEMPORALIDADE table. You can
 * customize the behavior of this class by editing the class, {@link
 * ExTemporalidade()}.
 */
@MappedSuperclass
public abstract class AbstractExTemporalidade extends HistoricoAuditavelSuporte {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_TEMPORALIDADE_SEQ", name = "EX_TEMPORALIDADE_SEQ")
	@GeneratedValue(generator = "EX_TEMPORALIDADE_SEQ")
	@Column(name = "ID_TEMPORALIDADE", unique = true, nullable = false)
	private Long idTemporalidade;

	/** The value of the simple descTemporalidade property. */
	@Column(name = "DESC_TEMPORALIDADE", nullable = false, length = 128)
	private java.lang.String descTemporalidade;

	/** Valor em dias, meses ou anos */
	@Column(name = "VALOR_TEMPORALIDADE")
	private Integer valorTemporalidade;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_MEDIDA", nullable = true)
	private CpUnidadeMedida cpUnidadeMedida;

	@OrderBy(value="codVia")
	@Where(clause="HIS_ATIVO = 1")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "temporalidadeCorrente")
	private Set<ExVia> exViaArqCorrenteSet;

	@OrderBy(value="codVia")
	@Where(clause="HIS_ATIVO = 1")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "temporalidadeIntermediario")
	private Set<ExVia> exViaArqIntermediarioSet;

	/**
	 * Simple constructor of AbstractExTemporalidade instances.
	 */
	public AbstractExTemporalidade() {
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
		if ((rhs == null) || !(rhs instanceof ExTemporalidade))
			return false;
		final ExTemporalidade that = (ExTemporalidade) rhs;
		if ((this.getIdTemporalidade() == null ? that.getIdTemporalidade() == null
				: this.getIdTemporalidade().equals(that.getIdTemporalidade()))) {
			if ((this.getDescTemporalidade() == null ? that
					.getDescTemporalidade() == null : this
					.getDescTemporalidade().equals(that.getDescTemporalidade())))
				return true;
		}
		return false;
	}

	/**
	 * Return the value of the DESC_TEMPORALIDADE column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescTemporalidade() {
		return this.descTemporalidade;
	}

	public Long getIdTemporalidade() {
		return idTemporalidade;
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
		int idValue = this.getIdTemporalidade() == null ? 0 : this
				.getIdTemporalidade().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescTemporalidade() == null ? 0 : this
				.getDescTemporalidade().hashCode();
		return result * 37 + idValue;
	}

	/**
	 * Set the value of the DESC_TEMPORALIDADE column.
	 * 
	 * @param descTemporalidade
	 */
	public void setDescTemporalidade(final java.lang.String descTemporalidade) {
		this.descTemporalidade = descTemporalidade;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idTemporalidade
	 */
	public void setIdTemporalidade(final Long idTemporalidade) {
		this.idTemporalidade = idTemporalidade;
	}

	public void setValorTemporalidade(Integer valorTemporalidade) {
		this.valorTemporalidade = valorTemporalidade;
	}

	public Integer getValorTemporalidade() {
		return valorTemporalidade;
	}

	public void setCpUnidadeMedida(CpUnidadeMedida cpUnidadeMedida) {
		this.cpUnidadeMedida = cpUnidadeMedida;
	}

	public CpUnidadeMedida getCpUnidadeMedida() {
		return cpUnidadeMedida;
	}

	public void setExViaArqCorrenteSet(Set<ExVia> exViaArqCorrenteSet) {
		this.exViaArqCorrenteSet = exViaArqCorrenteSet;
	}

	public Set<ExVia> getExViaArqCorrenteSet() {
		return exViaArqCorrenteSet;
	}

	public void setExViaArqIntermediarioSet(Set<ExVia> exViaArqIntermediarioSet) {
		this.exViaArqIntermediarioSet = exViaArqIntermediarioSet;
	}

	public Set<ExVia> getExViaArqIntermediarioSet() {
		return exViaArqIntermediarioSet;
	}

}
