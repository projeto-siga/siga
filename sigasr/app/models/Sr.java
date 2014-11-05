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
package models;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpCompetenciaBL;

/**
 * Classe que fornece uma instância do workflow.
 * 
 * @author kpf
 * 
 */
public class Sr extends Cp /*
							 * <ExConfiguracaoBL, ExCompetenciaBL, ExBL,
							 * ExPropriedadeBL>
							 */{

	/**
	 * Retorna uma instância do sistema de workflow. Através dessa instância é
	 * possível acessar a lógica de negócio, competências e configurações do
	 * sistema de workflow.
	 * 
	 * @return Instância de workflow
	 */
	public static Sr getInstance() {
		
		if (!isInstantiated()) {
			synchronized (Cp.class) {
				if (!isInstantiated()) {
					Sr instance = new Sr();
					setInstance(instance);
					instance.setConf(new SrConfiguracaoBL());
					instance.getConf().setComparator(new SrConfiguracaoComparator());
					instance.setComp(new CpCompetenciaBL());
					instance.getComp().setConfiguracaoBL(instance.getConf());
					// instance.setBL(new ExBL());
					// instance.getBL().setComp(instance.getComp());
					// instance.setProp(new ExPropriedadeBL());
				}
			}
		}
		return (Sr) Cp.getInstance();
	}
}
