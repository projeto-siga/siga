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
import java.util.HashSet;
import java.util.Set;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.util.MascaraUtil;

/**
 * A class that represents a row in the EX_CLASSIFICACAO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExClassificacao()}.
 */
public abstract class AbstractExClassificacao extends HistoricoAuditavelSuporte implements Serializable {
	

//	private java.lang.Byte codAssunto;
	
	private Long idClassificacao;

	private String codificacao;
	private String descrClassificacao;
	private Set<ExVia> exViaSet;
	private String obs;

	private Set<ExModelo> exModeloSet;
	private Set<ExModelo> exModeloCriacaoViaSet;
	
	private ExClassificacao classificacaoInicial;
	private Set<ExClassificacao> classificacoesPosteriores = new HashSet<ExClassificacao>(0);

	/**
	 * The cached hash code value for this instance. Settting to 0 triggers
	 * re-calculation.
	 */
	private int hashValue = 0;


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
		if ((this.getIdClassificacao() == null ? that.getIdClassificacao() == null : this.getIdClassificacao().equals(
				that.getIdClassificacao()))) {
			if ((this.getDescrClassificacao() == null ? that.getDescrClassificacao() == null : this
					.getDescrClassificacao().equals(that.getDescrClassificacao())))
				return true;
		}
		return false;
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
	public java.lang.String getObs() {
		return this.obs;
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
	public void setObs(final java.lang.String obs) {
		this.obs = obs;
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

	public Set<ExModelo> getExModeloSet() {
		return exModeloSet;
	}

	public void setExModeloSet(final Set modeloSet) {
		this.exModeloSet = modeloSet;
	}

	public String getCodAssunto() {
		return MascaraUtil.getInstance().getCampoDaMascara(0, getCodificacao());
	}

//	public void setCodAssunto(java.lang.Byte codAssunto) {
//		this.codAssunto = codAssunto;
//	}

	public void setCodificacao(String codificacao) {
		this.codificacao = codificacao;
	}

	public String getCodificacao() {
		return codificacao;
	}

	public void setExModeloCriacaoViaSet(Set<ExModelo> exModeloCriacaoViaSet) {
		this.exModeloCriacaoViaSet = exModeloCriacaoViaSet;
	}

	public Set<ExModelo> getExModeloCriacaoViaSet() {
		return exModeloCriacaoViaSet;
	}

	public ExClassificacao getClassificacaoInicial() {
		return classificacaoInicial;
	}

	public void setClassificacaoInicial(ExClassificacao classificacaoInicial) {
		this.classificacaoInicial = classificacaoInicial;
	}

	public Set<ExClassificacao> getClassificacoesPosteriores() {
		return classificacoesPosteriores;
	}

	public void setClassificacoesPosteriores(
			Set<ExClassificacao> classificacoesPosteriores) {
		this.classificacoesPosteriores = classificacoesPosteriores;
	}

}
