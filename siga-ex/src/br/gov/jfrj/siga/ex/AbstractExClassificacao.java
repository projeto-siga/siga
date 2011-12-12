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
import java.util.Date;
import java.util.Set;

/**
 * A class that represents a row in the EX_CLASSIFICACAO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExClassificacao()}.
 */
public abstract class AbstractExClassificacao implements Serializable {
	
	private java.lang.Byte codAssunto;
	
	/** The value of the simple codAssuntoPrincipal property. */
	private java.lang.Byte codAssuntoPrincipal;

	/** The value of the simple codAssuntoSecundario property. */
	private java.lang.Byte codAssuntoSecundario;

	/** The value of the simple codAtividade property. */
	private java.lang.Short codAtividade;

	/** The value of the simple codClasse property. */
	private java.lang.Byte codClasse;

	/** The value of the simple codSubclasse property. */
	private java.lang.Short codSubclasse;

	/** The value of the simple descrClassificacao property. */
	private java.lang.String descrClassificacao;

	/** The value of the exViaSet one-to-many association. */
	private java.util.Set<ExVia> exViaSet;

	/** The value of the simple facilitadorClass property. */
	private java.lang.String facilitadorClass;
	
	private Date dtIniReg;
	
	private Date dtFimReg;
	
	private Long idRegIni;

	private Set exModeloSet;

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;

	/** The composite primary key value. */
	private java.lang.Long idClassificacao;

	/**
	 * Simple constructor of AbstractExClassificacao instances.
	 */
	public AbstractExClassificacao() {
	}

	/**
	 * Constructor of AbstractExClassificacao instances given a simple primary
	 * key.
	 * 
	 * @param idClassificacao
	 */
	public AbstractExClassificacao(final java.lang.Long idClassificacao) {
		this.setIdClassificacao(idClassificacao);
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
		if ((this.getIdClassificacao() == null ? that.getIdClassificacao() == null : this.getIdClassificacao().equals(
				that.getIdClassificacao()))) {
			if ((this.getDescrClassificacao() == null ? that.getDescrClassificacao() == null : this
					.getDescrClassificacao().equals(that.getDescrClassificacao())))
				return true;
		}
		return false;
	}

	/**
	 * Return the value of the COD_ASSUNTO_PRINCIPAL column.
	 * 
	 * @return java.lang.Byte
	 */
	public java.lang.Byte getCodAssuntoPrincipal() {
		return this.codAssuntoPrincipal;
	}

	/**
	 * Return the value of the COD_ASSUNTO_SECUNDARIO column.
	 * 
	 * @return java.lang.Byte
	 */
	public java.lang.Byte getCodAssuntoSecundario() {
		return this.codAssuntoSecundario;
	}

	/**
	 * Return the value of the COD_ATIVIDADE column.
	 * 
	 * @return java.lang.Short
	 */
	public java.lang.Short getCodAtividade() {
		return this.codAtividade;
	}

	/**
	 * Return the value of the COD_CLASSE column.
	 * 
	 * @return java.lang.Byte
	 */
	public java.lang.Byte getCodClasse() {
		return this.codClasse;
	}

	/**
	 * Return the value of the COD_SUBCLASSE column.
	 * 
	 * @return java.lang.Short
	 */
	public java.lang.Short getCodSubclasse() {
		return this.codSubclasse;
	}

	/**
	 * Return the value of the DESCR_CLASSIFICACAO column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescrClassificacao() {
		return this.descrClassificacao;
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
	 * Return the value of the FACILITADOR_CLASS column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFacilitadorClass() {
		return this.facilitadorClass;
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
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdClassificacao() == null ? 0 : this.getIdClassificacao().hashCode();
		result = result * 37 + idValue;
		idValue = this.getDescrClassificacao() == null ? 0 : this.getDescrClassificacao().hashCode();
		result = result * 37 + idValue;
		this.hashValue = result;

		return this.hashValue;
	}

	/**
	 * Set the value of the COD_ASSUNTO_PRINCIPAL column.
	 * 
	 * @param codAssuntoPrincipal
	 */
	public void setCodAssuntoPrincipal(final java.lang.Byte codAssuntoPrincipal) {
		this.codAssuntoPrincipal = codAssuntoPrincipal;
	}

	/**
	 * Set the value of the COD_ASSUNTO_SECUNDARIO column.
	 * 
	 * @param codAssuntoSecundario
	 */
	public void setCodAssuntoSecundario(final java.lang.Byte codAssuntoSecundario) {
		this.codAssuntoSecundario = codAssuntoSecundario;
	}

	/**
	 * Set the value of the COD_ATIVIDADE column.
	 * 
	 * @param codAtividade
	 */
	public void setCodAtividade(final java.lang.Short codAtividade) {
		this.codAtividade = codAtividade;
	}

	/**
	 * Set the value of the COD_CLASSE column.
	 * 
	 * @param codClasse
	 */
	public void setCodClasse(final java.lang.Byte codClasse) {
		this.codClasse = codClasse;
	}

	/**
	 * Set the value of the COD_SUBCLASSE column.
	 * 
	 * @param codSubclasse
	 */
	public void setCodSubclasse(final java.lang.Short codSubclasse) {
		this.codSubclasse = codSubclasse;
	}

	/**
	 * Set the value of the DESCR_CLASSIFICACAO column.
	 * 
	 * @param descrClassificacao
	 */
	public void setDescrClassificacao(final java.lang.String descrClassificacao) {
		this.descrClassificacao = descrClassificacao;
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
	 * Set the value of the FACILITADOR_CLASS column.
	 * 
	 * @param facilitadorClass
	 */
	public void setFacilitadorClass(final java.lang.String facilitadorClass) {
		this.facilitadorClass = facilitadorClass;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idClassificacao
	 */
	public void setIdClassificacao(final java.lang.Long idClassificacao) {
		this.hashValue = 0;
		this.idClassificacao = idClassificacao;
	}

	public Set getExModeloSet() {
		return exModeloSet;
	}

	public void setExModeloSet(final Set modeloSet) {
		this.exModeloSet = modeloSet;
	}

	public Date getDtIniReg() {
		return dtIniReg;
	}

	public void setDtIniReg(Date dtIniReg) {
		this.dtIniReg = dtIniReg;
	}

	public Date getDtFimReg() {
		return dtFimReg;
	}

	public void setDtFimReg(Date dtFimReg) {
		this.dtFimReg = dtFimReg;
	}

	public Long getIdRegIni() {
		return idRegIni;
	}

	public void setIdRegIni(Long idRegIni) {
		this.idRegIni = idRegIni;
	}

	public java.lang.Byte getCodAssunto() {
		return codAssunto;
	}

	public void setCodAssunto(java.lang.Byte codAssunto) {
		this.codAssunto = codAssunto;
	}
}
