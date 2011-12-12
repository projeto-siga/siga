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

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

public abstract class AbstractCpOrgao extends HistoricoSuporte implements
		Serializable {

	private String bairroOrgao;

	private String cepOrgao;

	private Integer cgcOrgao;

	private String dscTipoOrgao;

	private String emailContatoOrgao;

	private String emailRespOrgao;

	private String enderecoOrgao;

	@Desconsiderar
	private Long idOrgao;

	private String municipioOrgao;

	private String nmContatoOrgao;

	private String nmOrgao;

	private String nmRespOrgao;

	private String razaoSocialOrgao;

	private String siglaOrgao;

	private String telContatoOrgao;

	private String ufOrgao;

	private String ativo;

	private String ideOrgao;

	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;

	/*
	 * (non-Javadoc)
	 */
	/*
	 * @Override public boolean equals(final Object rhs) { if ((rhs == null) ||
	 * !(rhs instanceof CpOrgao)) return false; final CpOrgao that = (CpOrgao)
	 * rhs;
	 * 
	 * if ((this.getIdOrgao() == null ? that.getIdOrgao() == null : this
	 * .getIdOrgao().equals(that.getIdOrgao()))) { if ((this.getNmOrgao() ==
	 * null ? that.getNmOrgao() == null : this
	 * .getNmOrgao().equals(that.getNmOrgao()))) return true;
	 * 
	 * } return false;
	 * 
	 * }
	 */

	public String getBairroOrgao() {
		return bairroOrgao;
	}

	public String getCepOrgao() {
		return cepOrgao;
	}

	public Integer getCgcOrgao() {
		return cgcOrgao;
	}

	public String getDscTipoOrgao() {
		return dscTipoOrgao;
	}

	public String getEmailContatoOrgao() {
		return emailContatoOrgao;
	}

	public String getEmailRespOrgao() {
		return emailRespOrgao;
	}

	public String getEnderecoOrgao() {
		return enderecoOrgao;
	}

	/**
	 * @return Retorna o atributo idLotacao.
	 */
	public Long getIdOrgao() {
		return idOrgao;
	}

	public String getMunicipioOrgao() {
		return municipioOrgao;
	}

	public String getNmContatoOrgao() {
		return nmContatoOrgao;
	}

	/**
	 * @return Retorna o atributo nomeLotacao.
	 */
	public String getNmOrgao() {
		return nmOrgao;
	}

	public String getNmRespOrgao() {
		return nmRespOrgao;
	}

	public String getRazaoSocialOrgao() {
		return razaoSocialOrgao;
	}

	public String getSiglaOrgao() {
		return siglaOrgao;
	}

	public String getTelContatoOrgao() {
		return telContatoOrgao;
	}

	public String getUfOrgao() {
		return ufOrgao;
	}

	public String getAtivo() {
		return ativo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdOrgao() == null ? 0 : this.getIdOrgao()
				.hashCode();
		result = result * 37 + idValue;
		idValue = this.getNmOrgao() == null ? 0 : this.getNmOrgao().hashCode();
		result = result * 37 + idValue;

		return result;
	}

	public void setBairroOrgao(final String bairroOrgao) {
		this.bairroOrgao = bairroOrgao;
	}

	public void setCepOrgao(final String cepOrgao) {
		this.cepOrgao = cepOrgao;
	}

	public void setCgcOrgao(final Integer cgcOrgao) {
		this.cgcOrgao = cgcOrgao;
	}

	public void setDscTipoOrgao(final String dscTipoOrgao) {
		this.dscTipoOrgao = dscTipoOrgao;
	}

	public void setEmailContatoOrgao(final String emailContatoOrgao) {
		this.emailContatoOrgao = emailContatoOrgao;
	}

	public void setEmailRespOrgao(final String emailRespOrgao) {
		this.emailRespOrgao = emailRespOrgao;
	}

	public void setEnderecoOrgao(final String enderecoOrgao) {
		this.enderecoOrgao = enderecoOrgao;
	}

	/**
	 * @param idLotacao
	 *            Atribui a idLotacao o valor.
	 */
	public void setIdOrgao(final Long idLotacao) {
		this.idOrgao = idLotacao;
	}

	public void setMunicipioOrgao(final String municipioOrgao) {
		this.municipioOrgao = municipioOrgao;
	}

	public void setNmContatoOrgao(final String nmContatoOrgao) {
		this.nmContatoOrgao = nmContatoOrgao;
	}

	/**
	 * @param nomeLotacao
	 *            Atribui a nomeLotacao o valor.
	 */
	public void setNmOrgao(final String nomeLotacao) {
		this.nmOrgao = nomeLotacao;
	}

	public void setNmRespOrgao(final String nmRespOrgao) {
		this.nmRespOrgao = nmRespOrgao;
	}

	public void setRazaoSocialOrgao(final String razaoSocialOrgao) {
		this.razaoSocialOrgao = razaoSocialOrgao;
	}

	public void setSiglaOrgao(final String siglaOrgao) {
		this.siglaOrgao = siglaOrgao;
	}

	public void setTelContatoOrgao(final String telContatoOrgao) {
		this.telContatoOrgao = telContatoOrgao;
	}

	public void setUfOrgao(final String ufOrgao) {
		this.ufOrgao = ufOrgao;
	}

	public void setAtivo(final String ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the ideOrgao
	 */
	public String getIdeOrgao() {
		return ideOrgao;
	}

	/**
	 * @param ideOrgao
	 *            the ideOrgao to set
	 */
	public void setIdeOrgao(String ideOrgao) {
		this.ideOrgao = ideOrgao;
	}

	/**
	 * @return the orgaoUsuario
	 */
	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	/**
	 * @param orgaoUsuario
	 *            the orgaoUsuario to set
	 */
	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

}
