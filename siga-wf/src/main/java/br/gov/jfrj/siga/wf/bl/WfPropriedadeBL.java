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
package br.gov.jfrj.siga.wf.bl;

import br.gov.jfrj.siga.cp.bl.CpPropriedadeBL;

public class WfPropriedadeBL extends CpPropriedadeBL {
	private static WfPropriedadeBL instance = new WfPropriedadeBL();

	@Override
	public String getPrefixoModulo() {
		return "siga.wf";
	}

	public String getMailLinkTarefa() {
		String result = "indefinido";
		try {
			result = this.obterPropriedade("mail.link.tarefa");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getMailLinkDocumento() {
		String result = "indefinido";
		try {
			result = this.obterPropriedade("mail.link.documento");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Double getRelEstatGeraisMinMediaTrunc() {
		return Double.valueOf(getString("rel.estatisticas.gerais.media.truncada.min").replace(",", "."));
	}

	public Double getRelEstatGeraisMaxMediaTrunc() {
		return Double.valueOf(getString("rel.estatisticas.gerais.media.truncada.max").replace(",", "."));
	}

	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
