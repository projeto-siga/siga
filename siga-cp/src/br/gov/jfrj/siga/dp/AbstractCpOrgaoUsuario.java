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
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;

public abstract class AbstractCpOrgaoUsuario implements Serializable {

	private Integer cgcOrgaoUsu;
	
	private Integer codOrgaoUsu;

	private Long idOrgaoUsu;

	private String bairroOrgaoUsu;

	private String cepOrgaoUsu;

	private String enderecoOrgaoUsu;

	private String nmRespOrgaoUsu;

	private String razaoSocialOrgaoUsu;

	private String ufOrgaoUsu;

	private String siglaOrgaoUsu;

	private String municipioOrgaoUsu;

	private String nmOrgaoUsu;

	private String telOrgaoUsu;
	
	private String acronimoOrgaoUsu;

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof CpOrgao))
			return false;
		final CpOrgao that = (CpOrgao) rhs;

		if ((this.getIdOrgaoUsu() == null ? that.getIdOrgao() == null : this
				.getIdOrgaoUsu().equals(that.getIdOrgao()))) {
			if ((this.getNmOrgaoUsu() == null ? that.getNmOrgao() == null
					: this.getNmOrgaoUsu().equals(that.getNmOrgao())))
				return true;

		}
		return false;

	}

	public String getBairroOrgaoUsu() {
		return bairroOrgaoUsu;
	}

	public String getCepOrgaoUsu() {
		return cepOrgaoUsu;
	}

	public Integer getCgcOrgaoUsu() {
		return cgcOrgaoUsu;
	}

	public String getEnderecoOrgaoUsu() {
		return enderecoOrgaoUsu;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public String getMunicipioOrgaoUsu() {
		return municipioOrgaoUsu;
	}

	public String getNmOrgaoUsu() {
		return nmOrgaoUsu;
	}

	public String getNmRespOrgaoUsu() {
		return nmRespOrgaoUsu;
	}

	public String getRazaoSocialOrgaoUsu() {
		return razaoSocialOrgaoUsu;
	}

	public String getSiglaOrgaoUsu() {
		return siglaOrgaoUsu;
	}

	public String getTelOrgaoUsu() {
		return telOrgaoUsu;
	}

	public String getUfOrgaoUsu() {
		return ufOrgaoUsu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdOrgaoUsu() == null ? 0 : this.getIdOrgaoUsu()
				.hashCode();
		result = result * 37 + idValue;
		idValue = this.getNmOrgaoUsu() == null ? 0 : this.getNmOrgaoUsu()
				.hashCode();
		result = result * 37 + idValue;

		return result;
	}

	public void setBairroOrgaoUsu(String bairroOrgaoUsu) {
		this.bairroOrgaoUsu = bairroOrgaoUsu;
	}

	public void setCepOrgaoUsu(String cepOrgaoUsu) {
		this.cepOrgaoUsu = cepOrgaoUsu;
	}

	public void setCgcOrgaoUsu(Integer cgcOrgaoUsu) {
		this.cgcOrgaoUsu = cgcOrgaoUsu;
	}

	public void setEnderecoOrgaoUsu(String enderecoOrgaoUsu) {
		this.enderecoOrgaoUsu = enderecoOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public void setMunicipioOrgaoUsu(String municipioOrgaoUsu) {
		this.municipioOrgaoUsu = municipioOrgaoUsu;
	}

	public void setNmOrgaoUsu(String nmOrgaoUsu) {
		this.nmOrgaoUsu = nmOrgaoUsu;
	}

	public void setNmRespOrgaoUsu(String nmRespOrgaoUsu) {
		this.nmRespOrgaoUsu = nmRespOrgaoUsu;
	}

	public void setRazaoSocialOrgaoUsu(String razaoSocialOrgaoUsu) {
		this.razaoSocialOrgaoUsu = razaoSocialOrgaoUsu;
	}

	public void setSiglaOrgaoUsu(String siglaOrgaoUsu) {
		this.siglaOrgaoUsu = siglaOrgaoUsu;
	}

	public void setTelOrgaoUsu(String telOrgaoUsu) {
		this.telOrgaoUsu = telOrgaoUsu;
	}

	public void setUfOrgaoUsu(String ufOrgaoUsu) {
		this.ufOrgaoUsu = ufOrgaoUsu;
	}

	public Integer getCodOrgaoUsu() {
		return codOrgaoUsu;
	}

	public void setCodOrgaoUsu(Integer codOrgaoUsu) {
		this.codOrgaoUsu = codOrgaoUsu;
	}

	public String getAcronimoOrgaoUsu() {
		return acronimoOrgaoUsu;
	}

	public void setAcronimoOrgaoUsu(String acronimoOrgaoUsu) {
		this.acronimoOrgaoUsu = acronimoOrgaoUsu;
	}

}
