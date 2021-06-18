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

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoCacheComparator;
import br.gov.jfrj.siga.wf.model.WfConfiguracaoCache;

/**
 * Classe que permite a comparação entre duas configurações de workflow.
 * 
 * @author kpf
 * 
 */
public class WfConfiguracaoCacheComparator extends CpConfiguracaoCacheComparator {

	/**
	 * Compara duas configurações.
	 * 
	 */
	public int compare(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {

		int i = super.compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		if (c1 instanceof WfConfiguracaoCache && c2 instanceof WfConfiguracaoCache) {
			WfConfiguracaoCache wfC1 = (WfConfiguracaoCache) c1;
			WfConfiguracaoCache wfC2 = (WfConfiguracaoCache) c2;

			if (wfC1.definicaoDeProcedimento == 0 && wfC2.definicaoDeProcedimento != 0)
				return 1;
			else if (wfC1.definicaoDeProcedimento != 0 && wfC2.definicaoDeProcedimento == 0)
				return -1;
		}

		i = super.untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return Long.compare(c1.idConfiguracao, c2.idConfiguracao);
	}
}
