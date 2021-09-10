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
 * Created Mon Nov 14 13:33:07 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.dp.DpLotacao;

/**
 * A class that represents a row in the 'EX_TIPO_DESPACHO' table. This class may
 * be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Table(name = "siga.ex_preenchimento")
public class ExPreenchimento extends AbstractExPreenchimento implements
		Serializable, Comparable<ExPreenchimento> {

	/**
	 * Simple constructor of ExTipoDespacho instances.
	 */
	public ExPreenchimento(long idPreenchimento, DpLotacao dpLotacao,
			String nomePreenchimento, ExModelo exModelo) {
		super();
		super.setIdPreenchimento(idPreenchimento);
		super.setExModelo(exModelo);
		super.setDpLotacao(dpLotacao);
		super.setNomePreenchimento(nomePreenchimento);
	}

	/**
	 * Simple constructor of ExTipoDespacho instances.
	 */

	public void setPreenchimentoBA(byte[] blob) {
		if (blob != null)
			setPreenchimentoBlob(blob);
	}

	public ExPreenchimento() {
	}

	public String getDescricao() {
		if (getIdPreenchimento() != null && getIdPreenchimento() != 0)
			return getNomePreenchimento() + " (" + getIdPreenchimento() + ")";
		else
			return getNomePreenchimento();
	}

	/**
	 * Constructor of ExTipoDespacho instances given a simple primary key.
	 * 
	 * @param idTpDespacho
	 */
	public ExPreenchimento(final java.lang.Long idPreenchimento) {
		super(idPreenchimento);
	}
	
	@Override
	public int compareTo(ExPreenchimento o) {
		int i = 0;
		
		if (this.getNomePreenchimento() != null) {
			i = this.getNomePreenchimento().compareTo(o.getNomePreenchimento());
			if (i != 0)
				return i;
		}
		return this.getIdPreenchimento().compareTo(o.getIdPreenchimento());
	}

}
