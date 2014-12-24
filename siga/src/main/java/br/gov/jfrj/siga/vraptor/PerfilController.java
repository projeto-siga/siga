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
package br.gov.jfrj.siga.vraptor;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Resource
public class PerfilController extends GrupoController {

	public PerfilController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();		
	}

	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_PERFIL_DE_ACESSO;
	}	

	@Get("/app/gi/perfil/editar")
	public void edita() throws Exception {
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aEditar();
	}

	
	@Get("/app/gi/perfil/excluir")
	public void excluir() throws Exception {
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aExcluir();
	}

	
	@Post("/app/gi/perfil/gravar")
	public void gravar() throws Exception {
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aGravar();
	}

	
	@Get("/app/gi/perfil/listar")
	public void lista() throws Exception {
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aListar();
		
		result.include("itens", getItens());
		result.include("cpTipoGrupo", getCpTipoGrupo());
		result.include("tamanho", getTamanho());
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
	}

}
