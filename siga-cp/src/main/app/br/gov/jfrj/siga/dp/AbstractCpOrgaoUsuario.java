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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractCpOrgaoUsuario extends Objeto implements Serializable {

	@Column(name = "CGC_ORGAO_USU")
	private Integer cgcOrgaoUsu;
	
	@Column(name = "COD_ORGAO_USU")
	private Integer codOrgaoUsu;

	@Id
	@Column(name = "ID_ORGAO_USU", nullable = false)
	private Long idOrgaoUsu;

	@Column(name = "BAIRRO_ORGAO_USU")
	private String bairroOrgaoUsu;

	@Column(name = "CEP_ORGAO_USU")
	private String cepOrgaoUsu;

	@Column(name = "END_ORGAO_USU")
	private String enderecoOrgaoUsu;

	@Column(name = "NM_RESP_ORGAO_USU")
	private String nmRespOrgaoUsu;

	@Column(name = "RAZAO_SOCIAL_ORGAO_USU")
	private String razaoSocialOrgaoUsu;

	@Column(name = "UF_ORGAO_USU")
	private String ufOrgaoUsu;

	@Column(name = "SIGLA_ORGAO_USU")
	private String siglaOrgaoUsu;

	@Column(name = "MUNICIPIO_ORGAO_USU")
	private String municipioOrgaoUsu;

	@Column(name = "NM_ORGAO_USU", nullable = false)
	private String nmOrgaoUsu;

	@Column(name = "TEL_ORGAO_USU")
	private String telOrgaoUsu;
	
	@Column(name = "ACRONIMO_ORGAO_USU")
	private String acronimoOrgaoUsu;

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof CpOrgao))
			return false;
		final CpOrgaoUsuario that = (CpOrgaoUsuario) rhs;

		if ((this.getIdOrgaoUsu() == null ? that.getIdOrgaoUsu() == null : this
				.getIdOrgaoUsu().equals(that.getIdOrgaoUsu()))) {
			if ((this.getNmOrgaoUsu() == null ? that.getNmOrgaoUsu() == null
					: this.getNmOrgaoUsu().equals(that.getNmOrgaoUsu())))
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
