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

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Formula;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
public abstract class AbstractCpOrgao extends HistoricoSuporte implements
		Serializable {

	@Column(name = "BAIRRO_ORGAO")
	private String bairroOrgao;

	@Column(name = "CEP_ORGAO")
	private String cepOrgao;

	@Column(name = "CGC_ORGAO")
	private Integer cgcOrgao;

	@Column(name = "DSC_TIPO_ORGAO")
	private String dscTipoOrgao;

	@Column(name = "EMAIL_CONTATO_ORGAO")
	private String emailContatoOrgao;

	@Column(name = "EMAIL_RESPONSAVEL_ORGAO")
	private String emailRespOrgao;

	@Column(name = "END_ORGAO")
	private String enderecoOrgao;

	@SequenceGenerator(name = "generator", sequenceName = "CP_ORGAO_SEQ")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ID_ORGAO", nullable = false)
	@Desconsiderar
	private Long idOrgao;

	@Column(name = "MUNICIPIO_ORGAO")
	private String municipioOrgao;

	@Column(name = "NOME_CONTATO_ORGAO")
	private String nmContatoOrgao;

	@Column(name = "NM_ORGAO", nullable = false)
	private String nmOrgao;

	@Column(name = "NOME_RESPONSAVEL_ORGAO")
	private String nmRespOrgao;

	@Column(name = "RAZAO_SOCIAL_ORGAO")
	private String razaoSocialOrgao;

	@Column(name = "SIGLA_ORGAO")
	private String siglaOrgao;

	@Column(name = "TEL_CONTATO_ORGAO")
	private String telContatoOrgao;

	@Column(name = "UF_ORGAO")
	private String ufOrgao;

	@Column(name = "FG_ATIVO")
	private String ativo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;
	
	@Column(name = "HIS_IDE")
	private String ideOrgao;

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
