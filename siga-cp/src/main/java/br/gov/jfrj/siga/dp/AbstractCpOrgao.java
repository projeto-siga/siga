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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarPorSiglaCpOrgao", query = "select org from CpOrgao org where upper(org.siglaOrgao) like upper('%' || :siglaOrgao || '%') and org.ativo='S'"),
		@NamedQuery(name = "consultarPorSiglaExataCpOrgao", query = "select org from CpOrgao org where upper(org.siglaOrgao) = upper(:siglaOrgao) and org.ativo='S'"),
		@NamedQuery(name = "consultarPorFiltroCpOrgao", query = "from CpOrgao org where ((upper(org.nmOrgaoAI) like upper('%' || :nome || '%')) or (upper(org.siglaOrgao) like upper('%' || :nome || '%'))) and org.ativo='S' order by org.nmOrgao"),
		@NamedQuery(name = "consultarQuantidadeCpOrgao", query = "select count(org) from CpOrgao org where ((upper(org.nmOrgaoAI) like upper('%' || :nome || '%')) or (upper(org.siglaOrgao) like upper('%' || :nome || '%'))) and org.ativo='S' order by org.nmOrgao"),
		@NamedQuery(name = "consultarQuantidadeCpOrgaoTodos", query = "select count(org) from CpOrgao org"),
		@NamedQuery(name = "consultarCpOrgaoOrdenadoPorNome", query = "from CpOrgao org order by org.nmOrgao") })
public abstract class AbstractCpOrgao extends HistoricoSuporte implements
		Serializable {

	@Id
	@SequenceGenerator(name = "CP_ORGAO_SEQ", sequenceName = "CORPORATIVO.CP_ORGAO_SEQ")
	@GeneratedValue(generator = "CP_ORGAO_SEQ")
	@Column(name = "ID_ORGAO", unique = true, nullable = false)
	@Desconsiderar
	private Long idOrgao;

	@Column(name = "BAIRRO_ORGAO", length = 50)
	private String bairroOrgao;

	@Column(name = "CEP_ORGAO", length = 8)
	private String cepOrgao;

	@Column(name = "CGC_ORGAO")
	private Integer cgcOrgao;

	@Column(name = "DSC_TIPO_ORGAO", length = 100)
	private String dscTipoOrgao;

	@Column(name = "EMAIL_CONTATO_ORGAO")
	private String emailContatoOrgao;

	@Column(name = "EMAIL_RESPONSAVEL_ORGAO", length = 60)
	private String emailRespOrgao;

	@Column(name = "END_ORGAO", length = 256)
	private String enderecoOrgao;

	@Column(name = "MUNICIPIO_ORGAO", length = 50)
	private String municipioOrgao;

	@Column(name = "NOME_CONTATO_ORGAO")
	private String nmContatoOrgao;

	@Column(name = "NM_ORGAO", nullable = false, length = 256)
	private String nmOrgao;

	@Column(name = "NOME_RESPONSAVEL_ORGAO", length = 60)
	private String nmRespOrgao;

	@Column(name = "RAZAO_SOCIAL_ORGAO", length = 256)
	private String razaoSocialOrgao;

	@Column(name = "SIGLA_ORGAO", length = 30)
	private String siglaOrgao;

	@Column(name = "TEL_CONTATO_ORGAO", length = 10)
	private String telContatoOrgao;

	@Column(name = "UF_ORGAO", length = 2)
	private String ufOrgao;

	@Column(name = "FG_ATIVO", length = 1)
	private String ativo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU", nullable = false)
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;

	@Column(name = "HIS_IDE", length = 256)
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
