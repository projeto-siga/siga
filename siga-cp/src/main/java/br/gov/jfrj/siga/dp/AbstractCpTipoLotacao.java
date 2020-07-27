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
package br.gov.jfrj.siga.dp;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public class AbstractCpTipoLotacao extends Objeto {

	@Id
	@Column(name = "ID_TP_LOTACAO", unique = true, nullable = false)
	private Long idTpLotacao;

	@Column(name = "SIGLA_TP_LOTACAO", length = 40)
	private String siglaTpLotacao;

	@Column(name = "DESC_TP_LOTACAO", length = 200)
	private String dscTpLotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_LOTACAO_PAI")
	private CpTipoLotacao tipoLotacaoPai;

	public Long getIdTpLotacao() {
		return idTpLotacao;
	}

	public void setIdTpLotacao(Long idTpLotacao) {
		this.idTpLotacao = idTpLotacao;
	}

	public String getSiglaTpLotacao() {
		return siglaTpLotacao;
	}

	public void setSiglaTpLotacao(String siglaTpLotacao) {
		this.siglaTpLotacao = siglaTpLotacao;
	}

	public String getDscTpLotacao() {
		return dscTpLotacao;
	}

	public void setDscTpLotacao(String dscTpLotacao) {
		this.dscTpLotacao = dscTpLotacao;
	}

	public CpTipoLotacao getTipoLotacaoPai() {
		return tipoLotacaoPai;
	}

	public void setTipoLotacaoPai(CpTipoLotacao tipoLotacaoPai) {
		this.tipoLotacaoPai = tipoLotacaoPai;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscTpLotacao == null) ? 0 : dscTpLotacao.hashCode());
		result = prime * result
				+ ((idTpLotacao == null) ? 0 : idTpLotacao.hashCode());
		result = prime * result
				+ ((siglaTpLotacao == null) ? 0 : siglaTpLotacao.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractCpTipoLotacao)) {
			return false;
		}
		AbstractCpTipoLotacao other = (AbstractCpTipoLotacao) obj;
		if (dscTpLotacao == null) {
			if (other.dscTpLotacao != null) {
				return false;
			}
		} else if (!dscTpLotacao.equals(other.dscTpLotacao)) {
			return false;
		}
		if (idTpLotacao == null) {
			if (other.idTpLotacao != null) {
				return false;
			}
		} else if (!idTpLotacao.equals(other.idTpLotacao)) {
			return false;
		}
		if (siglaTpLotacao == null) {
			if (other.siglaTpLotacao != null) {
				return false;
			}
		} else if (!siglaTpLotacao.equals(other.siglaTpLotacao)) {
			return false;
		}
		return true;
	}
}
