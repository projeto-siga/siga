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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoGrupo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class GrupoDeEmailController extends GrupoController {
	
	public GrupoDeEmailController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		prepare();
	}
	
	public int getIdTipoGrupo() {
		return CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO;
	}
	
	@Get("app/gi/grupoDeEmail/editar")
	public void edita(Long idCpGrupo) throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf();
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
				"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição")
				|| conf.podeGerirGrupo(getTitular(), getLotaTitular(), getIdCpGrupo(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))) {
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
			result.include("idConfiguracao", getIdConfiguracao());
			
		} else {
			throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		}
	}
	
	@Post("app/gi/grupoDeEmail/excluir")
	public void excluir(Long idCpGrupo) throws Exception {
		assertAcesso("GDISTR:Gerenciar grupos de distribuição;EXC:Excluir");
		super.aExcluir(idCpGrupo);
		result.redirectTo(this).lista();
	}
	
	@Post("app/gi/grupoDeEmail/gravar")
	public void gravar(Long idCpGrupo, String siglaGrupo, String dscGrupo, CpGrupoDeEmailSelecao grupoPaiSel, Integer codigoTipoConfiguracaoNova,
			String conteudoConfiguracaoNova) throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf();
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
				"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição")
				|| conf.podeGerirGrupo(getTitular(), getLotaTitular(), getIdCpGrupo(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))) {
			super.aGravar(idCpGrupo, siglaGrupo, dscGrupo, grupoPaiSel, codigoTipoConfiguracaoNova, conteudoConfiguracaoNova);
			result.redirectTo(this).lista();
		} else {
			throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		}
		
	}
	
	@Get("app/gi/grupoDeEmail/listar")
	public void lista() throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf();
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
				"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição")
				|| conf.podeGerirAlgumGrupo(getTitular(), getLotaTitular(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))) {
			super.aListar();
			result.include("itens", getItens());
			result.include("cpTipoGrupo", getCpTipoGrupo());
			result.include("tamanho", getTamanho());
			result.include("titular", getTitular());
			result.include("lotaTitular", getLotaTitular());
		} else {
			throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		}
		
	}
	
	@Get("app/gi/grupoDeEmail/excluirGestorGrupo")
	public void excluirGestorGrupo(Long idCpGrupo, Long idConfGestor) throws Exception {
		CpConfiguracaoBL conf = Cp.getInstance().getConf();
		if (conf.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
				"SIGA:Sistema Integrado de Gestão Administrativa;GI:Módulo de Gestão de Identidade;GDISTR:Gerenciar grupos de distribuição")
				|| conf.podeGerirAlgumGrupo(getTitular(), getLotaTitular(), Long.valueOf(CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO))) {
			super.aExcluirGestorGrupo(idCpGrupo, idConfGestor);
		} else {
			throw new AplicacaoException("Acesso negado!<br/> Você precisa ser um administrador ou gestor de grupo.");
		}
		result.redirectTo(this).edita(idCpGrupo);
	}
}
