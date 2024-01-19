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
import br.gov.jfrj.siga.base.AplicacaoException;
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
import br.gov.jfrj.siga.ex.bl.Mesa2Ant;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExMesa2AntController extends ExController {
	/**
	 * @deprecated CDI eyes only
	 */
	public ExMesa2AntController() {
		super();
	}

	@Inject
	public ExMesa2AntController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/mesa2ant")
	public void lista(Boolean exibirAcessoAnterior, Long idVisualizacao, String msg) throws Exception {
		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		result.include("exibirMesaVirtualComoPadrao", Cp.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(),
				CpTipoDeConfiguracao.EXIBIR_MESA_VIRTUAL));
		try {
			result.include("podeNovoDocumento", Cp.getInstance().getConf().podePorConfiguracao(getTitular(), getTitular().getLotacao(),
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
		if (msg != null) {
			result.include("mensagemCabec", msg);
			result.include("msgCabecClass", "alert-info fade-close");
		}
	}

	@Post("app/mesa2ant.json")
	public void json(Integer grupo, Integer start, Integer offset, Long idVisualizacao, boolean exibeLotacao, boolean trazerAnotacoes, boolean trazerArquivados, 
			boolean trazerComposto, boolean trazerCancelados, boolean ordemCrescenteData, 
			boolean usuarioPosse, String parms) throws Exception {
		
		List<br.gov.jfrj.siga.ex.bl.Mesa2Ant.GrupoItem> g = new ArrayList<br.gov.jfrj.siga.ex.bl.Mesa2Ant.GrupoItem>();
		Map<String, Mesa2Ant.SelGrupo> selGrupos = null;
		List<Mesa2Ant.GrupoItem> gruposMesa = new ArrayList<Mesa2Ant.GrupoItem>();
		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		List<Integer> marcasAIgnorar = new ArrayList<Integer>();

		if (SigaMessages.isSigaSP()) { 
			if (!trazerArquivados) {
				marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_CORRENTE.getId()); 
				marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId()); 
				marcasAIgnorar.add((int) CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId()); 
			}
			if (!trazerCancelados) 
				marcasAIgnorar.add((int) CpMarcadorEnum.CANCELADO.getId());
		}
		try {
			if (parms != null) {
				ObjectMapper mapper = new ObjectMapper();
				selGrupos = mapper.readValue(parms, new TypeReference<Map<String, Mesa2Ant.SelGrupo>>() {});
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
			DpLotacao lotaTitular = null;
			if(idVisualizacao != null && !idVisualizacao.equals(Long.valueOf(0)) 
					&& Cp.getInstance().getConf().podePorConfiguracao
						(getCadastrante(), 
						 getCadastrante().getLotacao(), 
						 ExTipoDeConfiguracao.DELEGAR_VISUALIZACAO)) {
				DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
				lotaTitular = vis.getTitular().getLotacao();
				gruposMesa = Mesa2Ant.getContadores(dao(), vis.getTitular(), lotaTitular, selGrupos, 
						exibeLotacao, marcasAIgnorar);
				g = Mesa2Ant.getMesa(dao(), vis.getTitular(), lotaTitular, selGrupos, 
						gruposMesa, exibeLotacao, trazerAnotacoes, trazerComposto, ordemCrescenteData, usuarioPosse, marcasAIgnorar);
			} else {
				lotaTitular = getTitular().getLotacao();
				gruposMesa = Mesa2Ant.getContadores(dao(), getTitular(), lotaTitular, selGrupos, 
						exibeLotacao, marcasAIgnorar);
				g = Mesa2Ant.getMesa(dao(), getTitular(), lotaTitular, selGrupos, 
						gruposMesa, exibeLotacao, trazerAnotacoes, trazerComposto, ordemCrescenteData, usuarioPosse, marcasAIgnorar);
			}
	
			String s = ExAssinadorExternoController.gson.toJson(g);
			
			result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);
		} catch (Exception e) {
			throw e;
		} 
	}
}