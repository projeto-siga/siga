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

import java.text.SimpleDateFormat;

import javax.persistence.Entity;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
@Entity
public abstract class CpMarca extends AbstractCPMarca {

	public String getDtIniMarcaDDMMYYYY() {
		if (getDtIniMarca() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtIniMarca());
		}
		return "";
	}
	
	public String getDtFimMarcaDDMMYYYY() {
		if (getDtFimMarca() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtFimMarca());
		}
		return "";
	}

	@Override
	public String toString() {
		return "CpMarca [Tipo=" +  ((getCpTipoMarca() != null) ? getCpTipoMarca().getDescrTipoMarca() : "null") + ", DtIni="
				+ getDtIniMarca() + ", DtFim=" + getDtFimMarca()
				+ ", Marcador=" + getCpMarcador().getDescrMarcador()
				+ ", Pessoa=" + ((getDpPessoaIni() != null) ? getDpPessoaIni().getSiglaCompleta() : "null")
				+ ", Lotacao=" + ((getDpLotacaoIni() != null) ? getDpLotacaoIni().getSiglaCompleta() : "null") + "]";
	}

	
}