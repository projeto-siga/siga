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
public class PerfilController extends GrupoController {


	/**
	 * @deprecated CDI eyes only
	 */
	public PerfilController() {
		super();
	}

	@Inject
	public PerfilController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		prepare();
	}

	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_PERFIL_DE_ACESSO;
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

	@Get("/app/gi/perfil/editar")
	public void edita(Long idCpGrupo) throws Exception {
		
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aEditar(idCpGrupo);
		
		// Tipo Grupo = Perfil
		result.include("idCpTipoGrupo", getIdTipoGrupo());
		result.include("cpTipoGrupo", getCpTipoGrupo());
		// Dados do Grupo Perfil
		result.include("idCpGrupo", getIdCpGrupo());
		result.include("siglaGrupo", getSiglaGrupo());
		result.include("dscGrupo", getDscGrupo());
		result.include("grupoPaiSel", getGrupoPaiSel());
		// Dados utilizados para montar a tela
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
		result.include("lotacaoGestoraSel", getLotacaoGestoraSel());
		result.include("confGestores", getConfGestores(idCpGrupo));
		result.include("configuracoesGrupo", getConfiguracoesGrupo());
		result.include("tiposConfiguracaoGrupoParaTipoDeGrupo", getTiposConfiguracaoGrupoParaTipoDeGrupo());
		result.include("idConfiguracaoNova", getIdConfiguracaoNova());
	}

	@Transacional
	@SuppressWarnings("unchecked")
	@Post("/app/gi/perfil/gravar")
	public void gravar(Long idCpGrupo, String siglaGrupo, String dscGrupo,
			CpGrupoDeEmailSelecao grupoPaiSel,
			List<String> codigoTipoConfiguracaoSelecionado,
			Integer codigoTipoConfiguracaoNova, 
			String conteudoConfiguracaoNova,
			List<String> idConfiguracao,
			List<String> conteudoConfiguracaoSelecionada)
			throws Exception {

		assertAcesso("PERFIL:Gerenciar grupos de email");

		Long novoIdCpGrupo = super.aGravar(
				idCpGrupo
				, siglaGrupo
				, dscGrupo
				, grupoPaiSel
				, codigoTipoConfiguracaoNova
				, conteudoConfiguracaoNova
				, idConfiguracao
				, codigoTipoConfiguracaoSelecionado
				, conteudoConfiguracaoSelecionada);
		
		result.redirectTo(MessageFormat.format("/app/gi/perfil/editar?idCpGrupo={0}", novoIdCpGrupo.toString()));
	}

	@Transacional
	@Post("/app/gi/perfil/excluir")
	public void excluir(Long idCpGrupo) throws Exception {
		assertAcesso("PERFIL:Gerenciar grupos de email");
		super.aExcluir(idCpGrupo);
		result.redirectTo(this).lista();
	}
	
	@Post
	@Get
	@Path("/app/gi/perfil/buscar")
	public void buscar(final String sigla,
			           final String postback,
			           final Long idCpTipoGrupo,
			           final Integer offset) throws Exception{
		
		getP().setOffset(offset);
		super.aBuscar(sigla, postback);
		result.include("tamanho", getTamanho());
		result.include("request",getRequest());
		result.include("itens",this.getItens());
		result.include("sigla",sigla);
		result.include("postback",postback);
		result.include("offset",offset);
		result.include("param",getRequest().getParameterMap());
	}
	
	@Get("/app/gi/perfil/selecionar")
	public void selecionar(String sigla){
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

}
