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
package br.gov.jfrj.siga.cp.bl;

// TODO: _LAGS - eliminar (deixar a String livre) - Ver com Renato o CpDao.criarHibernateCfg(String) já existe para a passagem de datasource 
public enum CpAmbienteEnumBL {
	PRODUCAO("prod"), HOMOLOGACAO("homolo"), TREINAMENTO("treina"), DESENVOLVIMENTO(
			"desenv");
	private final String sigla;

	CpAmbienteEnumBL(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}
}
