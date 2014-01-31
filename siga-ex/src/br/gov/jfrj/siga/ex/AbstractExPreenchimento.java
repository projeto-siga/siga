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
 */

package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_TIPO_DESPACHO table. You can
 * customize the behavior of this class by editing the class,
 * {@link ExTipoDespacho()}.
 */
public abstract class AbstractExPreenchimento extends Objeto implements Serializable {

	/** The composite primary key value. */

	private int hashValue = 0;

	private java.lang.Long idPreenchimento;

	private DpLotacao dpLotacao;

	private ExModelo exModelo;

	private String nomePreenchimento;

	private java.sql.Blob preenchimentoBlob;

	/**
	 * Simple constructor of AbstractExTipoDespacho instances.
	 */
	public AbstractExPreenchimento() {
	}

	public AbstractExPreenchimento(final java.lang.Long idPreenchimento) {
		this.setIdPreenchimento(idPreenchimento);
	}

	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}

	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}

	public ExModelo getExModelo() {
		return exModelo;
	}

	public void setExModelo(ExModelo exModelo) {
		this.exModelo = exModelo;
	}

	public java.lang.Long getIdPreenchimento() {
		return idPreenchimento;
	}

	public void setIdPreenchimento(java.lang.Long idPreenchimento) {
		this.idPreenchimento = idPreenchimento;
	}

	public java.sql.Blob getPreenchimentoBlob() {
		return preenchimentoBlob;
	}

	public void setPreenchimentoBlob(java.sql.Blob preenchimentoBlob) {
		this.preenchimentoBlob = preenchimentoBlob;
	}

	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof ExPreenchimento))
			return false;
		final ExPreenchimento that = (ExPreenchimento) rhs;
		if ((this.getIdPreenchimento() == null ? that.getIdPreenchimento() == null
				: this.getIdPreenchimento().equals(that.getPreenchimentoBlob())))
			return true;
		return false;

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
		if (this.hashValue == 0) {
			int result = 17;
			final int idDocValue = this.getIdPreenchimento() == null ? 0 : this
					.getIdPreenchimento().hashCode();
			result = result * 37 + idDocValue;
			this.hashValue = result;
		}
		return this.hashValue;
	}

	public String getNomePreenchimento() {
		return nomePreenchimento;
	}

	public void setNomePreenchimento(String nomePreenchimento) {
		this.nomePreenchimento = nomePreenchimento;
	}

}
