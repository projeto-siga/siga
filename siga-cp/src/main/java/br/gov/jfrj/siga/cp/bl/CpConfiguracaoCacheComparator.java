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
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;

public class CpConfiguracaoCacheComparator implements Comparator<CpConfiguracaoCache> {

	public int compareSelectedFields(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {

		if (c1.cpServico == 0 && c2.cpServico != 0)
			return 1;
		else if (c1.cpServico != 0 && c2.cpServico == 0)
			return -1;

		if (c1.cpIdentidade == 0 && c2.cpIdentidade != 0)
			return 1;
		else if (c1.cpIdentidade != 0 && c2.cpIdentidade == 0)
			return -1;

		if (c1.dpPessoa == 0 && c2.dpPessoa != 0)
			return 1;
		else if (c1.dpPessoa != 0 && c2.dpPessoa == 0)
			return -1;

		if (c1.cpGrupo == 0 && c2.cpGrupo != 0)
			return 1;
		else if (c1.cpGrupo != 0 && c2.cpGrupo == 0)
			return -1;

		if (c1.lotacao == 0 && c2.lotacao != 0)
			return 1;
		else if (c1.lotacao != 0 && c2.lotacao == 0)
			return -1;

		if (c1.complexo == 0 && c2.complexo != 0)
			return 1;
		else if (c1.complexo != 0 && c2.complexo == 0)
			return -1;

		if (c1.funcaoConfianca == 0 && c2.funcaoConfianca != 0)
			return 1;
		else if (c1.funcaoConfianca != 0 && c2.funcaoConfianca == 0)
			return -1;

		if (c1.orgaoUsuario == 0 && c2.orgaoUsuario != 0)
			return 1;
		else if (c1.orgaoUsuario != 0 && c2.orgaoUsuario == 0)
			return -1;

		if (c1.cargo == 0 && c2.cargo != 0)
			return 1;
		else if (c1.cargo != 0 && c2.cargo == 0)
			return -1;

		if (c1.cpGrupo != 0 && c2.cpGrupo != 0) {
			int i1 = c1.cpGrupoNivel;
			int i2 = c2.cpGrupoNivel;
			if (i1 > i2)
				return -1;
			if (i1 < i2)
				return 1;
		}

		return 0;
	}

	// Critérios de desempate
	//
	public int untieSelectedFields(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {

		// A configuração mais restritiva deve ser priorizada.
		//
		if (c1.situacao != null && c2.situacao != null) {
			long i1 = c1.situacao.getRestritividade();
			long i2 = c2.situacao.getRestritividade();
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
		if (c1.cpGrupo != 0 && c2.cpGrupo != 0)
			return Long.compare(c1.cpGrupo, c2.cpGrupo);

		return 0;
	}

	public int compare(CpConfiguracaoCache c1, CpConfiguracaoCache c2) {
		int i = compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		i = untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return Long.compare(c1.idConfiguracao, c2.idConfiguracao);
	}

}
