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
 * Created Mon Nov 14 13:34:11 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;

/**
 * A class that represents a row in the EX_VIA table. You can customize the
 * behavior of this class by editing the class, {@link ExVia()}.
 */
@MappedSuperclass
public abstract class AbstractExVia extends HistoricoAuditavelSuporte implements
		Serializable {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_VIA_SEQ", name = "EX_VIA_SEQ")
	@GeneratedValue(generator = "EX_VIA_SEQ")
	@Column(name = "ID_VIA", unique = true, nullable = false)
	private java.lang.Long idVia;

	/** The value of the simple codVia property. */
	@Column(name = "COD_VIA", length = 2)
	private java.lang.String codVia;

	/** The value of the exClassificacao association. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSIFICACAO", nullable = false)
	private ExClassificacao exClassificacao;

	/** The value of the temporalidadeCorrente association. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEMPORAL_ARQ_COR")
	private ExTemporalidade temporalidadeCorrente;

	/** The value of the temporalidadeIntermediario association. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEMPORAL_ARQ_INT")
	private ExTemporalidade temporalidadeIntermediario;

	/** The value of the exTipoDestinacao association. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINACAO")
	private ExTipoDestinacao exTipoDestinacao;

	/** The value of the exTipoDestinacao association. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINACAO_FINAL")
	private ExTipoDestinacao exDestinacaoFinal;

	/** The value of the simple obs property. */
	@Column(name = "OBS", length = 4000)
	private java.lang.String obs;

	/**
	 * Simple constructor of AbstractExVia instances.
	 */
	public AbstractExVia() {
	}

	/**
	 * Constructor of AbstractExVia instances given a simple primary key.
	 * 
	 * @param idVia
	 */
	public AbstractExVia(final java.lang.Long idVia) {
		this.setIdVia(idVia);
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
		if ((rhs == null) || !(rhs instanceof ExVia))
			return false;
		final ExVia that = (ExVia) rhs;

		if ((this.getIdVia() == null ? that.getIdVia() == null : this
				.getIdVia().equals(that.getIdVia())))
			return true;
		return false;
	}

	/**
	 * Return the value of the COD_VIA column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCodVia() {
		return this.codVia;
	}

	/**
	 * Return the value of the ID_CLASSIFICACAO column.
	 * 
	 * @return ExClassificacao
	 */
	public ExClassificacao getExClassificacao() {
		return this.exClassificacao;
	}

	/**
	 * Return the value of the ID_TMP_CORRENTE column.
	 * 
	 * @return ExTemporalidade
	 */
	public ExTemporalidade getTemporalidadeCorrente() {
		return this.temporalidadeCorrente;
	}

	/**
	 * Return the value of the ID_TMP_INTERMEDIARIO column.
	 * 
	 * @return ExTemporalidade
	 */
	public ExTemporalidade getTemporalidadeIntermediario() {
		return this.temporalidadeIntermediario;
	}

	/**
	 * Return the value of the ID_TP_DESTINACAO column.
	 * 
	 * @return ExTipoDestinacao
	 */
	public ExTipoDestinacao getExTipoDestinacao() {
		return this.exTipoDestinacao;
	}

	public ExTipoDestinacao getExDestinacaoFinal() {
		return this.exDestinacaoFinal;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdVia() {
		return idVia;
	}

	/**
	 * Return the value of the OBS column.
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
		final int idValue = this.getIdVia() == null ? 0 : this.getIdVia()
				.hashCode();
		return result * 37 + idValue;
	}

	/**
	 * Set the value of the COD_VIA column.
	 * 
	 * @param codVia
	 */
	public void setCodVia(final java.lang.String codVia) {
		this.codVia = codVia;
	}

	/**
	 * Set the value of the ID_CLASSIFICACAO column.
	 * 
	 * @param exClassificacao
	 */
	public void setExClassificacao(final ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	/**
	 * Set the value of the ID_TMP_CORRENTE column.
	 * 
	 * @param temporalidadeCorrente
	 */
	public void setTemporalidadeCorrente(final ExTemporalidade exTemporalidade) {
		this.temporalidadeCorrente = exTemporalidade;
	}

	/**
	 * Set the value of the ID_TMP_INTERMEDIARIO column.
	 * 
	 * @param temporalidadeIntermediario
	 */
	public void setTemporalidadeIntermediario(
			final ExTemporalidade temporalidade) {
		this.temporalidadeIntermediario = temporalidade;
	}

	/**
	 * Set the value of the ID_TP_DESTINACAO column.
	 * 
	 * @param exTipoDestinacao
	 */
	public void setExTipoDestinacao(final ExTipoDestinacao exTipoDestinacao) {
		this.exTipoDestinacao = exTipoDestinacao;
	}

	public void setExDestinacaoFinal(final ExTipoDestinacao exDestinacaoFinal) {
		this.exDestinacaoFinal = exDestinacaoFinal;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idVia
	 */
	public void setIdVia(final java.lang.Long idVia) {
		this.idVia = idVia;
	}

	/**
	 * Set the value of the OBS column.
	 * 
	 * @param obs
	 */
	public void setObs(final java.lang.String obs) {
		this.obs = obs;
	}

}
