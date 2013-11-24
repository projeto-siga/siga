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

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

public class CpGrupoDeEmailAction extends CpGrupoAction {
	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO;
	}

	@Override
	public String aEditar() throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf(); 
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição") || 
				conf.podeGerirGrupo(getTitular(), getLotaTitular(),getIdCpGrupo(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))){
			return super.aEditar();	
		}
		
		
		throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
	}

	@Override
	public String aExcluir() throws Exception {
		assertAcesso("GDISTR:Gerenciar grupos de distribuição;EXC:Excluir");
		return super.aExcluir();
	}

	@Override
	public String aGravar() throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf(); 
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição") || 
				conf.podeGerirGrupo(getTitular(), getLotaTitular(),getIdCpGrupo(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))){
			return super.aGravar();	
		}

		throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		
	}

	@Override
	public String aListar() throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf(); 
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), "SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição") || 
				conf.podeGerirAlgumGrupo(getTitular(), getLotaTitular(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))){
			return super.aListar();	
		}

		throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		
	}

}
