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

import java.util.Comparator;

import br.gov.jfrj.siga.cp.CpConfiguracao;

public class CpConfiguracaoComparator implements Comparator<CpConfiguracao> {

	public int compareSelectedFields(CpConfiguracao c1, CpConfiguracao c2) {

		if (c1.getCpServico() == null && c2.getCpServico() != null)
			return 1;
		else if (c1.getCpServico() != null && c2.getCpServico() == null)
			return -1;

		if (c1.getCpIdentidade() == null && c2.getCpIdentidade() != null)
			return 1;
		else if (c1.getCpIdentidade() != null && c2.getCpIdentidade() == null)
			return -1;

		if (c1.getDpPessoa() == null && c2.getDpPessoa() != null)
			return 1;
		else if (c1.getDpPessoa() != null && c2.getDpPessoa() == null)
			return -1;

		if (c1.getCpGrupo() == null && c2.getCpGrupo() != null)
			return 1;
		else if (c1.getCpGrupo() != null && c2.getCpGrupo() == null)
			return -1;

		if (c1.getLotacao() == null && c2.getLotacao() != null)
			return 1;
		else if (c1.getLotacao() != null && c2.getLotacao() == null)
			return -1;

		if (c1.getComplexo() == null && c2.getComplexo() != null)
			return 1;
		else if (c1.getComplexo() != null && c2.getComplexo() == null)
			return -1;

		if (c1.getFuncaoConfianca() == null && c2.getFuncaoConfianca() != null)
			return 1;
		else if (c1.getFuncaoConfianca() != null
				&& c2.getFuncaoConfianca() == null)
			return -1;

		if (c1.getOrgaoUsuario() == null && c2.getOrgaoUsuario() != null)
			return 1;
		else if (c1.getOrgaoUsuario() != null && c2.getOrgaoUsuario() == null)
			return -1;

		if (c1.getCargo() == null && c2.getCargo() != null)
			return 1;
		else if (c1.getCargo() != null && c2.getCargo() == null)
			return -1;

		if (c1.getCpGrupo() != null && c2.getCpGrupo() != null) {
			int i1 = c1.getCpGrupo().getNivel();
			int i2 = c2.getCpGrupo().getNivel();
			if (i1 > i2)
				return -1;
			if (i1 < i2)
				return 1;
		}

		return 0;
	}

	// Critérios de desempate
	//
	public int untieSelectedFields(CpConfiguracao c1, CpConfiguracao c2) {

		// A configuração mais restritiva deve ser priorizada.
		//
		if (c1.getCpSituacaoConfiguracao() != null
				&& c2.getCpSituacaoConfiguracao() != null) {
			long i1 = c1.getCpSituacaoConfiguracao().getRestritividade();
			long i2 = c2.getCpSituacaoConfiguracao().getRestritividade();
			if (i1 > i2)
				return -1;
			if (i1 < i2)
				return 1;
		}

		// Se não houver critério melhor, priorizar em função da id do grupo
		// para que não mude cada vez que a lista é recarregada ou que uma nova
		// configuração for inserida na lista, pois nesse caso ela ganharia uma
		// nova id e alteraria a ordenação.
		//
		if (c1.getCpGrupo() != null && c2.getCpGrupo() != null)
			return c1.getCpGrupo().getId().compareTo(c2.getCpGrupo().getId());

		return 0;
	}

	public int compare(CpConfiguracao c1, CpConfiguracao c2) {
		int i = compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		i = untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return c1.getIdConfiguracao().compareTo(c2.getIdConfiguracao());
	}

}
