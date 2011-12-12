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
 * Criado em  21/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

/**
 * Classe que representa uma linha na tabela DP_CARGO. Você pode customizar o
 * comportamento desta classe editando a classe {@link DpCargo}.
 */
public abstract class AbstractDpCargo implements Serializable {

	@Desconsiderar
	private Long idCargo;

	private String nomeCargo;

	@Desconsiderar
	private Date dataFimCargo;

	@Desconsiderar
	private Date dataInicioCargo;

	@Desconsiderar
	private Long idCargoIni;

	private String ideCargo;

	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;
	
	@Desconsiderar
	private String siglaCargo;

	/**
	 * @return Retorna o atributo idCargo.
	 */
	public Long getIdCargo() {
		return idCargo;
	}

	/**
	 * @return Retorna o atributo nomeCargo.
	 */
	public String getNomeCargo() {
		return nomeCargo;
	}

	/**
	 * @param idCargo
	 *            Atribui a idCargo o valor.
	 */
	public void setIdCargo(final Long idCargo) {
		this.idCargo = idCargo;
	}

	/**
	 * @param nomeCargo
	 *            Atribui a nomeCargo o valor.
	 */
	public void setNomeCargo(final String nomeCargo) {
		this.nomeCargo = nomeCargo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpCargo))
			return false;
		final DpCargo that = (DpCargo) rhs;

		if ((this.getIdCargo() == null ? that.getIdCargo() == null : this
				.getIdCargo().equals(that.getIdCargo()))) {
			if ((this.getNomeCargo() == null ? that.getNomeCargo() == null
					: this.getNomeCargo().equals(that.getNomeCargo())))
				return true;

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdCargo() == null ? 0 : this.getIdCargo()
				.hashCode();
		result = result * 37 + idValue;
		idValue = this.getNomeCargo() == null ? 0 : this.getNomeCargo()
				.hashCode();
		result = result * 37 + idValue;

		return result;

	}

	public Date getDataFimCargo() {
		return dataFimCargo;
	}

	public void setDataFimCargo(Date dataFimCargo) {
		this.dataFimCargo = dataFimCargo;
	}

	public Date getDataInicioCargo() {
		return dataInicioCargo;
	}

	public void setDataInicioCargo(Date dataInicioCargo) {
		this.dataInicioCargo = dataInicioCargo;
	}

	public Long getIdCargoIni() {
		return idCargoIni;
	}

	public void setIdCargoIni(Long idCargoIni) {
		this.idCargoIni = idCargoIni;
	}

	public String getIdeCargo() {
		return ideCargo;
	}

	public void setIdeCargo(String ideCargo) {
		this.ideCargo = ideCargo;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	/**
	 * @return the siglaCargo
	 */
	public String getSiglaCargo() {
		return siglaCargo;
	}

	/**
	 * @param siglaCargo the siglaCargo to set
	 */
	public void setSiglaCargo(String siglaCargo) {
		this.siglaCargo = siglaCargo;
	}

}
