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
package br.gov.jfrj.siga.cp;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@Entity
@Table(name = "CP_GRUPO", schema = Catalogs.CORPORATIVO)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ID_TP_GRUPO", discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractCpGrupo extends HistoricoAuditavelSuporte {
	@SequenceGenerator(name = "generator", sequenceName = "CP_GRUPO_SEQ")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ID_GRUPO", nullable = false)
	@Desconsiderar
	private Long idGrupo;
	@Column(name = "SIGLA_GRUPO")
	private String siglaGrupo;
	@Column(name = "DESC_GRUPO")
	private String dscGrupo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_GRUPO", insertable = false, updatable = false)
	private CpTipoGrupo cpTipoGrupo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_PAI")
	private CpGrupo cpGrupoPai;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscGrupo == null) ? 0 : dscGrupo.hashCode());
		result = prime * result + ((idGrupo == null) ? 0 : idGrupo.hashCode());
		result = prime * result
				+ ((siglaGrupo == null) ? 0 : siglaGrupo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractCpGrupo)) {
			return false;
		}
		AbstractCpGrupo other = (AbstractCpGrupo) obj;
		if (dscGrupo == null) {
			if (other.dscGrupo != null) {
				return false;
			}
		} else if (!dscGrupo.equals(other.dscGrupo)) {
			return false;
		}
		if (idGrupo == null) {
			if (other.idGrupo != null) {
				return false;
			}
		} else if (!idGrupo.equals(other.idGrupo)) {
			return false;
		}
		if (siglaGrupo == null) {
			if (other.siglaGrupo != null) {
				return false;
			}
		} else if (!siglaGrupo.equals(other.siglaGrupo)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the idGrupo
	 */
	public Long getId() {
		return getIdGrupo();
	}

	/**
	 * @return the idGrupo
	 */
	public Long getIdGrupo() {
		return idGrupo;
	}

	/**
	 * @param idGrupo
	 *            the idGrupo to set
	 */
	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}

	/**
	 * @return the siglaGrupo
	 */
	public String getSiglaGrupo() {
		return siglaGrupo;
	}

	/**
	 * @param siglaGrupo
	 *            the siglaGrupo to set
	 */
	public void setSiglaGrupo(String siglaGrupo) {
		this.siglaGrupo = siglaGrupo;
	}

	/**
	 * @return the dscGrupo
	 */
	public String getDscGrupo() {
		return dscGrupo;
	}

	/**
	 * @param dscGrupo
	 *            the dscGrupo to set
	 */
	public void setDscGrupo(String dscGrupo) {
		this.dscGrupo = dscGrupo;
	}

	/**
	 * @return the cpTipoGrupo
	 */
	public CpTipoGrupo getCpTipoGrupo() {
		return cpTipoGrupo;
	}

	/**
	 * @param cpTipoGrupo
	 *            the cpTipoGrupo to set
	 */
	public void setCpTipoGrupo(CpTipoGrupo cpTipoGrupo) {
		this.cpTipoGrupo = cpTipoGrupo;
	}

	/**
	 * @return the cpGrupoPai
	 */
	public CpGrupo getCpGrupoPai() {
		return cpGrupoPai;
	}

	/**
	 * @param cpGrupoPai
	 *            the cpGrupoPai to set
	 */
	public void setCpGrupoPai(CpGrupo cpGrupoPai) {
		this.cpGrupoPai = cpGrupoPai;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

}
