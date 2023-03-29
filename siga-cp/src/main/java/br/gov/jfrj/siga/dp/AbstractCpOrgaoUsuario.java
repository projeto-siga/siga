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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@SuppressWarnings("serial")
@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarSiglaOrgaoUsuario", query = "from CpOrgaoUsuario org where upper(org.siglaOrgaoUsu) = upper(:sigla) or upper(org.acronimoOrgaoUsu) = upper(:sigla) order by org.hisDtIni desc"),
		@NamedQuery(name = "consultarCpOrgaoUsuario", query = "select u from CpOrgaoUsuario u where "
				+ "  	exists (select 1 from CpOrgaoUsuario oAux where oAux.idOrgaoUsuIni = u.idOrgaoUsuIni "
				+ "		group by oAux.idOrgaoUsuIni having max(oAux.hisDtIni) = u.hisDtIni) or u.hisDtFim is null "
				+ " 	order by u.siglaOrgaoUsu "),
		@NamedQuery(name = "consultarCpOrgaoUsuarioTodos", query = "select u from CpOrgaoUsuario u order by u.siglaOrgaoUsu "), 
		@NamedQuery(name = "consultarCpOrgaoUsuarioOrdenadoPorNome", query = "select u from CpOrgaoUsuario u order by u.nmOrgaoUsu"),
		@NamedQuery(name = "consultarIdOrgaoUsuario", query = "from CpOrgaoUsuario org where upper(org.idOrgaoUsu) = upper(:idOrgaoUsu)"),
		@NamedQuery(name = "consultarNomeOrgaoUsuario", query = "from CpOrgaoUsuario org where upper(org.nmOrgaoAI) = upper(:nome)"),
		@NamedQuery(name = "consultarPorFiltroCpOrgaoUsuario", query = "from CpOrgaoUsuario org where (upper(org.nmOrgaoUsu) like upper('%' || :nome || '%'))	order by org.nmOrgaoUsu"),
		@NamedQuery(name = "consultarQuantidadeCpOrgaoUsuario", query = "select count(org) from CpOrgaoUsuario org"
				+ " where ((upper(org.nmOrgaoUsu) like upper('%' || :nome || '%')) or (upper(org.siglaOrgaoUsu) like upper('%' || :nome || '%'))) "
				+ " and (exists (select 1 from CpOrgaoUsuario oAux where oAux.idOrgaoUsuIni = org.idOrgaoUsuIni "
				+ "		group by oAux.idOrgaoUsuIni having max(oAux.hisDtIni) = org.hisDtIni) or org.hisDtFim is null)"
				+ " order by org.siglaOrgaoUsu")})

public abstract class AbstractCpOrgaoUsuario extends Objeto implements
		Serializable, HistoricoAuditavel {

	@Column(name = "CGC_ORGAO_USU")
	private Integer cgcOrgaoUsu;

	@Column(name = "COD_ORGAO_USU")
	private Integer codOrgaoUsu;

	@Id
	@SequenceGenerator(name = "CP_ORGAO_USUARIO_SEQ", sequenceName = "CORPORATIVO.CP_ORGAO_USUARIO_SEQ")
	@GeneratedValue(generator = "CP_ORGAO_USUARIO_SEQ")
	@Column(name = "ID_ORGAO_USU", unique = true, nullable = false)
	private Long idOrgaoUsu;

	@Column(name = "BAIRRO_ORGAO_USU", length = 50)
	private String bairroOrgaoUsu;

	@Column(name = "CEP_ORGAO_USU", length = 8)
	private String cepOrgaoUsu;

	@Column(name = "END_ORGAO_USU", length = 256)
	private String enderecoOrgaoUsu;

	@Column(name = "NM_RESP_ORGAO_USU", length = 60)
	private String nmRespOrgaoUsu;

	@Column(name = "RAZAO_SOCIAL_ORGAO_USU", length = 256)
	private String razaoSocialOrgaoUsu;

	@Column(name = "UF_ORGAO_USU", length = 2)
	private String ufOrgaoUsu;

	@Column(name = "SIGLA_ORGAO_USU", length = 15)
	private String siglaOrgaoUsu;

	@Column(name = "MUNICIPIO_ORGAO_USU", length = 50)
	private String municipioOrgaoUsu;

	@Column(name = "NM_ORGAO_USU", nullable = false, length = 256)
	private String nmOrgaoUsu;

	@Column(name = "TEL_ORGAO_USU", length = 10)
	private String telOrgaoUsu;

	@Column(name = "ACRONIMO_ORGAO_USU", length = 12)
	private String acronimoOrgaoUsu;
	
	@Column(name = "IS_EXTERNO_ORGAO_USU", length = 1)
	private Integer isExternoOrgaoUsu;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HIS_IDC_INI")
	@Desconsiderar
	private CpIdentidade hisIdcIni;

	@ManyToOne(fetch=FetchType.LAZY)
	@Desconsiderar
    @JoinColumn(name="HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;
	
	@Column(name = "ID_ORGAO_USU_INICIAL")
	@Desconsiderar
	private Long idOrgaoUsuIni;

	@Desconsiderar
	@Column(name = "MARCO_REGULATORIO")
	private String marcoRegulatorio;
	
	@Desconsiderar
	@Column(name = "DT_ALTERACAO")
	private Date dataAlteracao;
	
	@Column(name = "HIS_DT_INI")
	@Desconsiderar
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	@Desconsiderar
	private Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU_INICIAL", insertable = false, updatable = false)
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuarioInicial;

	public Integer getIsExternoOrgaoUsu() {
		return isExternoOrgaoUsu;
	}

	public void setIsExternoOrgaoUsu(Integer isExternoOrgaoUsu) {
		this.isExternoOrgaoUsu = isExternoOrgaoUsu;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof CpOrgaoUsuario))
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

	public CpOrgaoUsuario getOrgaoUsuarioInicial() {
		return orgaoUsuarioInicial;
	}

	public void setOrgaoUsuarioInicial(CpOrgaoUsuario orgaoUsuarioInicial) {
		this.orgaoUsuarioInicial = orgaoUsuarioInicial;
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

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	public void setHisIdcFim(CpIdentidade hisIdcFim) {
		this.hisIdcFim = hisIdcFim;
	}

	public Long getIdOrgaoUsuIni() {
		return idOrgaoUsuIni;
	}

	public void setIdOrgaoUsuIni(Long idOrgaoUsuIni) {
		this.idOrgaoUsuIni = idOrgaoUsuIni;
	}

	public String getMarcoRegulatorio() {
		return marcoRegulatorio;
	}

	public void setMarcoRegulatorio(String marcoRegulatorio) {
		this.marcoRegulatorio = marcoRegulatorio;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	public Date getDtFim() {
		return hisDtFim;
	}

	public void setDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	public Integer getAtivo() {
		return hisAtivo;
	}

	public void setAtivo(Integer hisAtivo) {
		this.hisAtivo = hisAtivo;
	}
	
}
