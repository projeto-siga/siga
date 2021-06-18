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
package br.gov.jfrj.siga.ex.bl;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoCacheComparator;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;

/**
 * Classe que permite a comparação entre duas configurações de workflow.
 * 
 * @author kpf
 * 
 */
public class ExConfiguracaoCacheComparator extends CpConfiguracaoCacheComparator {

	public int compareFields(long o1, long o2) {
		if (o1 == 0 && o2 != 0)
			return 1;
		else if (o1 != 0 && o2 == 0)
			return -1;
		return 0;
	}

	/**
	 * Compara duas configurações.
	 * 
	 */
	public int compare(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {

		int i = super.compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		if (c1 instanceof ExConfiguracaoCache && c2 instanceof ExConfiguracaoCache) {
			ExConfiguracaoCache ExC1 = (ExConfiguracaoCache) c1;
			ExConfiguracaoCache ExC2 = (ExConfiguracaoCache) c2;

			i = compareFields(ExC1.exNivelAcesso, ExC2.exNivelAcesso);
			if (i != 0)
				return i;

			i = compareFields(ExC1.dpPessoa, ExC2.dpPessoa);
			if (i != 0)
				return i;

			i = compareFields(ExC1.lotacao, ExC2.lotacao);
			if (i != 0)
				return i;

			i = compareFields(ExC1.funcaoConfianca, ExC2.funcaoConfianca);
			if (i != 0)
				return i;

			i = compareFields(ExC1.orgaoUsuario, ExC2.orgaoUsuario);
			if (i != 0)
				return i;

			i = compareFields(ExC1.cargo, ExC2.cargo);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exClassificacao, ExC2.exClassificacao);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exModelo, ExC2.exModelo);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exFormaDocumento, ExC2.exFormaDocumento);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exTipoDocumento, ExC2.exTipoDocumento);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exPapel, ExC2.exPapel);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exTipoFormaDoc, ExC2.exTipoFormaDoc);
			if (i != 0)
				return i;

			i = compareFields(ExC1.exTipoDocumento, ExC2.exTipoDocumento);
			if (i != 0)
				return i;

		}

		i = super.untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return Long.compare(c1.idConfiguracao, c2.idConfiguracao);
	}
}
