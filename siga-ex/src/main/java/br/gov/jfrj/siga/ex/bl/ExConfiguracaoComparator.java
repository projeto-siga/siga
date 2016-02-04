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

import java.util.Comparator;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoComparator;
import br.gov.jfrj.siga.ex.ExConfiguracao;

/**
 * Classe que permite a comparação entre duas configurações de workflow.
 * 
 * @author kpf
 * 
 */
public class ExConfiguracaoComparator extends CpConfiguracaoComparator {

	/**
	 * Compara duas configurações.
	 * 
	 */
	public int compare(CpConfiguracao c1, CpConfiguracao c2) {

		int i = super.compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		if (c1 instanceof ExConfiguracao && c2 instanceof ExConfiguracao) {
			ExConfiguracao exC1 = (ExConfiguracao) c1;
			ExConfiguracao exC2 = (ExConfiguracao) c2;

			if (exC1.getExNivelAcesso() == null
					&& exC2.getExNivelAcesso() != null)
				return 1;
			else if (exC1.getExNivelAcesso() != null
					&& exC2.getExNivelAcesso() == null)
				return -1;

			if (exC1.getDpPessoa() == null && exC2.getDpPessoa() != null)
				return 1;
			else if (exC1.getDpPessoa() != null && exC2.getDpPessoa() == null)
				return -1;

			if (exC1.getLotacao() == null && exC2.getLotacao() != null)
				return 1;
			else if (exC1.getLotacao() != null && exC2.getLotacao() == null)
				return -1;

			if (exC1.getFuncaoConfianca() == null
					&& exC2.getFuncaoConfianca() != null)
				return 1;
			else if (exC1.getFuncaoConfianca() != null
					&& exC2.getFuncaoConfianca() == null)
				return -1;

			if (exC1.getOrgaoUsuario() == null
					&& exC2.getOrgaoUsuario() != null)
				return 1;
			else if (exC1.getOrgaoUsuario() != null
					&& exC2.getOrgaoUsuario() == null)
				return -1;

			if (exC1.getCargo() == null && exC2.getCargo() != null)
				return 1;
			else if (exC1.getCargo() != null && exC2.getCargo() == null)
				return -1;

			if (exC1.getExClassificacao() == null
					&& exC2.getExClassificacao() != null)
				return 1;
			else if (exC1.getExClassificacao() != null
					&& exC2.getExClassificacao() == null)
				return -1;

			if (exC1.getExModelo() == null && exC2.getExModelo() != null)
				return 1;
			else if (exC1.getExModelo() != null && exC2.getExModelo() == null)
				return -1;

			if (exC1.getExFormaDocumento() == null
					&& exC2.getExFormaDocumento() != null)
				return 1;
			else if (exC1.getExFormaDocumento() != null
					&& exC2.getExFormaDocumento() == null)
				return -1;

			if (exC1.getExTipoDocumento() == null
					&& exC2.getExTipoDocumento() != null)
				return 1;
			else if (exC1.getExTipoDocumento() != null
					&& exC2.getExTipoDocumento() == null)
				return -1;

			if (exC1.getExPapel() == null && exC2.getExPapel() != null)
				return 1;
			else if (exC1.getExPapel() != null && exC2.getExPapel() == null)
				return -1;

			if (exC1.getExTipoFormaDoc() == null
					&& exC2.getExTipoFormaDoc() != null)
				return 1;
			else if (exC1.getExTipoFormaDoc() != null
					&& exC2.getExTipoFormaDoc() == null)
				return -1;
		}

		i = super.untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return c1.getIdConfiguracao().compareTo(c2.getIdConfiguracao());
	}
}
