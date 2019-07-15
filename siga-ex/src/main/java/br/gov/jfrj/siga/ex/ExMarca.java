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
package br.gov.jfrj.siga.ex;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
@Entity
@DiscriminatorValue("1")
public class ExMarca extends AbstractExMarca implements Comparable {

	public int compareTo(Object o) {
		ExMarca other = (ExMarca) o;
		int i = getCpMarcador().getIdMarcador().compareTo(
				other.getCpMarcador().getIdMarcador());
		if (i != 0)
			return i;
		if (getDpLotacaoIni() == null) {
			if (other.getDpLotacaoIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpLotacaoIni() == null)
				i = 1;
			else
				i = getDpLotacaoIni().getIdLotacao().compareTo(
						other.getDpLotacaoIni().getIdLotacao());
		}
		if (i != 0)
			return i;
		if (getDpPessoaIni() == null) {
			if (other.getDpPessoaIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpPessoaIni() == null)
				i = 1;
			else
				i = getDpPessoaIni().getIdPessoa().compareTo(
						other.getDpPessoaIni().getIdPessoa());
		}
		if (i != 0)
			return i;
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.getCpMarcador().getDescrMarcador());

		if (getDtIniMarca() != null && getDtIniMarca().after(new Date())) {
			sb.append("a partir de ");
			sb.append(getDtIniMarcaDDMMYYYY());
		}

		if (getDtFimMarca() != null) {
			sb.append(" até ");
			sb.append(getDtFimMarcaDDMMYYYY());
		}

		if (getDpLotacaoIni() != null || getDpPessoaIni() != null) {
			sb.append(" [");
			if (getDpLotacaoIni() != null) {
				sb.append(getDpLotacaoIni().getLotacaoAtual().getSigla());
			}
			if (getDpPessoaIni() != null) {
				if (getDpLotacaoIni() != null)
					sb.append(", ");
				sb.append(getDpPessoaIni().getPessoaAtual().getSigla());
			}
			sb.append("]");
		}
		return sb.toString();
	}

}
