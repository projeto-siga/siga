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

import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.cp.model.CpGrupoDeEmailSelecao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class PerfilJEEController extends GrupoController {


	/**
	 * @deprecated CDI eyes only
	 */
	public PerfilJEEController() {
		super();
	}

	@Inject
	public PerfilJEEController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		
		prepare();
	}

	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_PERFIL_JEE;
	}
	
	@Get("/app/gi/perfilJEE/listar")
	public void lista() throws Exception {
		assertAcesso("PERFILJEE:Gerenciar grupos de email");
		super.aListar();
		
		result.include("itens", getItens());
		result.include("cpTipoGrupo", getCpTipoGrupo());
		result.include("tamanho", getTamanho());
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
	}
	

	@Get("/app/gi/perfilJEE/editar")
	public void edita(Long idCpGrupo) throws Exception {
		assertAcesso("PERFILJEE:Gerenciar grupos de email");
		super.aEditar(idCpGrupo);
		//Tipo Grupo = Perfil
		result.include("idCpTipoGrupo", getIdTipoGrupo());		
		result.include("cpTipoGrupo", getCpTipoGrupo());
		//Dados do Grupo Perfil
		result.include("idCpGrupo", getIdCpGrupo());
		result.include("siglaGrupo", getSiglaGrupo());
		result.include("dscGrupo", getDscGrupo());
		result.include("grupoPaiSel", getGrupoPaiSel());
		//Dados utilizados para montar a tela				
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());		
		result.include("lotacaoGestoraSel", getLotacaoGestoraSel());		
		result.include("confGestores", getConfGestores(idCpGrupo));
		result.include("configuracoesGrupo",getConfiguracoesGrupo());
		result.include("tiposConfiguracaoGrupoParaTipoDeGrupo",getTiposConfiguracaoGrupoParaTipoDeGrupo());
		result.include("idConfiguracaoNova", getIdConfiguracaoNova());
	}
	
	@Transacional
	@SuppressWarnings("unchecked")
	@Post("/app/gi/perfilJEE/gravar")
	public void gravar(Long idCpGrupo
			          ,String siglaGrupo
					  ,String dscGrupo			          
			          ,CpGrupoDeEmailSelecao grupoPaiSel
			          ,Integer codigoTipoConfiguracaoNova
			          ,String conteudoConfiguracaoNova
			          ,List<String> idConfiguracao
			          ,List<String> codigoTipoConfiguracaoSelecionada
			          ,List<String> conteudoConfiguracaoSelecionada) throws Exception {
		
		assertAcesso("PERFILJEE:Gerenciar grupos de email");
		Long novoIdCpGrupo = super.aGravar(idCpGrupo
				     ,siglaGrupo
					 ,dscGrupo
					 ,grupoPaiSel
					 ,codigoTipoConfiguracaoNova
					 ,conteudoConfiguracaoNova
					 ,idConfiguracao
					 ,codigoTipoConfiguracaoSelecionada
					 ,conteudoConfiguracaoSelecionada);
		
		result.redirectTo(MessageFormat.format("/app/gi/perfilJEE/editar?idCpGrupo={0}", novoIdCpGrupo.toString()));
	}	

	@Transacional
	@Post("/app/gi/perfilJEE/excluir")
	public void excluir(Long idCpGrupo) throws Exception {
		assertAcesso("PERFILJEE:Gerenciar grupos de email");
		super.aExcluir(idCpGrupo);
		result.redirectTo(this).lista();
	}
	
	@Get
	@Post
	@Path("/app/gi/perfilJEE/buscar")
	public void busca(String postback, String nome, Integer offset) throws Exception{
		setNome(nome);
		getP().setOffset(offset);		
		super.aBuscar(nome, postback);
		result.include("param", getRequest().getParameterMap());
		result.include("tamanho", getTamanho());
		result.include("itens", getItens());
		result.include("nome", getNome());
	}
	
	@Get("/app/gi/perfilJEE/selecionar")
	public void selecionar(String sigla) {
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

}
