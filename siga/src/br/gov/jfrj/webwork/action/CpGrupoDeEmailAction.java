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
package br.gov.jfrj.webwork.action;

import java.lang.reflect.InvocationTargetException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoGrupo;

public class CpGrupoDeEmailAction extends CpGrupoAction {
	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_EMAIL;
	}

	@Override
	public String aEditar() throws Exception {
		assertAcesso("GEMAIL:Gerenciar grupos de email");
		return super.aEditar();
	}

	@Override
	public String aExcluir() throws Exception {
		assertAcesso("GEMAIL:Gerenciar grupos de email");
		return super.aExcluir();
	}

	@Override
	public String aGravar() throws Exception {
		assertAcesso("GEMAIL:Gerenciar grupos de email");
		return super.aGravar();
	}

	@Override
	public String aListar() throws Exception {
		assertAcesso("GEMAIL:Gerenciar grupos de email");
		return super.aListar();
	}

}
