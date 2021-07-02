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
package br.gov.jfrj.siga.sr.model;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoCacheComparator;

/**
 * Classe que permite a comparação entre duas configurações de workflow.
 * 
 * @author kpf
 * 
 */
public class SrConfiguracaoCacheComparator extends CpConfiguracaoCacheComparator {

	/**
	 * Compara duas configurações.
	 * 
	 */
	public int compare(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {

		int i = super.compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		if (c1 instanceof SrConfiguracaoCache && c2 instanceof SrConfiguracaoCache) {
			SrConfiguracaoCache srC1 = (SrConfiguracaoCache) c1;
			SrConfiguracaoCache srC2 = (SrConfiguracaoCache) c2;

			// Quando c1 é mais abstrato, retorna 1.

			if (srC1.itemConfiguracaoSet == null) {
				if (srC2.itemConfiguracaoSet != null)
					return 1;
			} else {
				if (srC2.itemConfiguracaoSet == null)
					return -1;
				else {
					int nivelSrC1 = srC1.getNivelItemParaComparar();
					int nivelSrC2 = srC2.getNivelItemParaComparar();
					if (nivelSrC1 < nivelSrC2)
						return 1;
					if (nivelSrC1 > nivelSrC2)
						return -1;
				}
			}

			if (srC1.acoesSet == null) {
				if (srC2.acoesSet != null)
					return 1;
			} else {
				if (srC2.acoesSet == null)
					return -1;
				else {
					int nivelSrC1 = srC1.getNivelAcaoParaComparar();
					int nivelSrC2 = srC2.getNivelAcaoParaComparar();
					if (nivelSrC1 < nivelSrC2)
						return 1;
					if (nivelSrC1 > nivelSrC2)
						return -1;
				}
			}

		}

		i = super.untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return Long.compare(c1.idConfiguracao, c2.idConfiguracao);
	}
}
