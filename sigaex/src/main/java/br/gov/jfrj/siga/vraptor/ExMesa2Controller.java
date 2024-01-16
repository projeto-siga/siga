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
/*
 * Criado em 23/11/2005
 */

package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpAcesso;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.Mesa2;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExMesa2Controller extends ExController {
	private static final String ACESSO_MESA2BETA = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;MESA2:Mesa Versão 2;BETA:Utilizar versão beta";
	private static final String PERMITE_FILTRO = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;MESA2:Mesa Versão 2;FILTRO:Permitir usar o filtro de pesquisa";
	/**
	 * @deprecated CDI eyes only
	 */
	public ExMesa2Controller() {
		super();
	}

	@Inject
	public ExMesa2Controller(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/mesa2")
	public void lista(Boolean exibirAcessoAnterior, Long idVisualizacao, String msg) throws Exception {
		if( !Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
				ACESSO_MESA2BETA) ) {
			result.redirectTo("/app/mesa2ant" + (exibirAcessoAnterior != null? 
					"?exibirAcessoAnterior=" + exibirAcessoAnterior.toString() : "")); 
			return;
		}
		
		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		
		result.include("exibirMesaVirtualComoPadrao", Cp.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(),
				CpTipoDeConfiguracao.EXIBIR_MESA_VIRTUAL));
		
		try {
			result.include("podeNovoDocumento", Cp.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(),
					ExTipoDeConfiguracao.CRIAR_NOVO_EXTERNO));
		} catch (Exception e) {
			throw e;
		} 
		if (exibirAcessoAnterior != null && exibirAcessoAnterior) {
			CpAcesso a = dao.consultarAcessoAnterior(so.getCadastrante());
			if (a == null)
				return;
			String acessoAnteriorData = Data.formatDDMMYY_AS_HHMMSS(a.getDtInicio());
			String acessoAnteriorMaquina = a.getAuditIP();
			result.include("acessoAnteriorData", acessoAnteriorData);
			result.include("acessoAnteriorMaquina", acessoAnteriorMaquina);
		}
		if(idVisualizacao != null) {
			DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
			if(vis != null && vis.getDelegado().equals(getTitular())) {
				result.include("idVisualizacao", idVisualizacao);
				result.include("visualizacao", vis);
			} else {
				result.include("idVisualizacao", 0);
			}
		} else {
			result.include("idVisualizacao", 0);
		}
		
		if( !getLotaTitular().equivale(getCadastrante().getLotacao()) &&
				getTitular().equivale(getCadastrante())) {
			// É substituição de lotação
			result.include("mostrarUsuario", false);
		} else {
			result.include("mostrarUsuario", true);
		}
		
		if (msg != null) {
			result.include("mensagemCabec", msg);
			result.include("msgCabecClass", "alert-info fade-close");
		}
	}

	@Get("app/mesa2.json")
	public void json(final boolean contar, final Integer qtd, final Integer offset, final Long idVisualizacao, final boolean exibeLotacao, boolean trazerAnotacoes, final boolean trazerArquivados, 
			final boolean trazerComposto, final boolean trazerCancelados, final boolean ordemCrescenteData, 
			final boolean usuarioPosse, final String parms, final String filtro) throws Exception {
		
		List<Mesa2.GrupoItem> g = new ArrayList<Mesa2.GrupoItem>();
		Map<String, Mesa2.SelGrupo> selGrupos = null;
		
		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		List<Integer> marcasAIgnorar = new ArrayList<Integer>();

		if (!trazerArquivados) {
			marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_CORRENTE.getId()); 
			marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId()); 
			marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId()); 
		}
		
		if (!trazerCancelados) 
			marcasAIgnorar.add((int) CpMarcadorEnum.CANCELADO.getId());
		
		try {
			if (parms != null) {
				ObjectMapper mapper = new ObjectMapper();
				selGrupos = mapper.readValue(parms, new TypeReference<Map<String, Mesa2.SelGrupo>>() {});
			}
			if (exibeLotacao 
					&& (Ex.getInstance().getComp().ehPublicoExterno(
							getTitular()) 
					|| !Prop.getBool("/siga.mesa.carrega.lotacao"))) {
				result.use(Results.http()).addHeader("Content-Type", "text/plain")
					.body("Não é permitido exibir dados da sua " 
							+ SigaMessages.getMessage("usuario.lotacao"))
					.setStatusCode(200);				
				return;
			}
			if(filtro != null && !"".equals(filtro) 
					&& !Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(
							getTitular(), getLotaTitular(),	PERMITE_FILTRO)) {
					result.use(Results.http()).addHeader("Content-Type", "text/plain")
					.body("Usuário não autorizado a utilizar o filtro de pesquisa.")
					.setStatusCode(200);
				return;
			}
			if(idVisualizacao != null && !idVisualizacao.equals(Long.valueOf(0)) 
					&& Cp.getInstance().getConf().podePorConfiguracao
						(getCadastrante(), 
						 getCadastrante().getLotacao(), 
						 ExTipoDeConfiguracao.DELEGAR_VISUALIZACAO)) {
				DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
				g = Mesa2.getMesa(contar, qtd, offset, vis.getTitular(), vis.getTitular().getLotacao(), selGrupos,	exibeLotacao, trazerAnotacoes, 
						trazerComposto, ordemCrescenteData, usuarioPosse, marcasAIgnorar, filtro);
			} else {
				g = Mesa2.getMesa(contar, qtd, offset, getTitular(), getLotaTitular(), selGrupos, exibeLotacao, trazerAnotacoes, 
						trazerComposto, ordemCrescenteData, usuarioPosse, marcasAIgnorar, filtro);
			}
	
			String s = ExAssinadorExternoController.gson.toJson(g);
			
			result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);
		} catch (Exception e) {
			throw e;
		} 
	}
}