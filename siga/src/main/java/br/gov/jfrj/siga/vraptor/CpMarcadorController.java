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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import com.google.gson.Gson;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpMarcadorCoresEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoAplicacaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoDataEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoJustificativaEnum;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;

@Controller
public class CpMarcadorController extends SigaController {

	private static final Logger LOG = Logger.getLogger(CpMarcadorController.class);

	/**
	 * @deprecated CDI eyes only
	 */
	public CpMarcadorController() {
		super();
	}

	@Inject
	public CpMarcadorController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("/app/marcador/listar")
	public void lista(final DpLotacao lotacaoSel) throws Exception {
		DpLotacaoDaoFiltro lotacao = new DpLotacaoDaoFiltro();
		Long idOrgaoUsu = getCadastrante().getOrgaoUsuario().getId();
		lotacao.setNome("");
		lotacao.setIdOrgaoUsu(idOrgaoUsu);
		List<DpLotacao> listaLotacao = new ArrayList<DpLotacao>();
		DpLotacao l = new DpLotacao();
		l.setNomeLotacao("Selecione");
		l.setId(0L);
		l.setSiglaLotacao("");
		CpOrgaoUsuario cpOrgaoUsuario = new CpOrgaoUsuario();
		cpOrgaoUsuario.setIdOrgaoUsu(0L);
		cpOrgaoUsuario.setSiglaOrgaoUsu("");
		l.setOrgaoUsuario(cpOrgaoUsuario);
		listaLotacao.add(l);
		if (idOrgaoUsu != null && idOrgaoUsu != 0)
			listaLotacao.addAll(CpDao.getInstance().consultarPorFiltro(lotacao));
//		result.include("listaTipoExibicao", listaTipoMarcadorEnum);
//		String[] listaCores = (String[]) EnumUtils.getEnumMap(CpMarcador.CoresMarcadorEnum.class).keySet().toArray();
//		for (CoresMarcadorEnum item : CpMarcador.CoresMarcadorEnum.values()){
//		    listaCores.add(item.name().replace("COR_", ""));
//		}
		result.include("listaTipoMarcador", dao.listarTodos(CpTipoMarcador.class, null));
		result.include("listaLotacao", listaLotacao);
		result.include("listaCores", CpMarcadorCoresEnum.getList());
		result.include("listaTipoAplicacao", CpMarcadorTipoAplicacaoEnum.values());
		result.include("listaTipoExibicao", CpMarcadorTipoExibicaoEnum.values());
		result.include("listaTipoDataPlanejada", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoDataLimite", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoJustificativa", CpMarcadorTipoJustificativaEnum.values());
		result.include("listaTipoInteressado", CpMarcadorTipoInteressadoEnum.values());
		result.include("listaMarcadores", dao.listarCpMarcadoresPorLotacaoESublotacoes(getLotaCadastrante(), true));
	}

	@Get("/app/marcador/historico")
	public void historico(final Long id) throws Exception {
		List<CpMarcador> listMar = dao().listarTodosPorIdInicial(CpMarcador.class, id, "hisDtIni", true);
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(toJson(listMar))
				.setStatusCode(200);
	}

	@Get("/app/marcador/excluir")
	public void exclui(final Long id) {
		try {
			if (id != null) {
				CpMarcador mar = dao().consultar(id, CpMarcador.class, false);
				dao().excluirComHistorico(mar, null, getIdentidadeCadastrante());
				result.use(Results.http()).addHeader("Content-Type", "application/text").body("Excluído com sucesso.")
						.setStatusCode(200);
			}
		} catch (AplicacaoException e) {
			result.use(Results.http()).addHeader("Content-Type", "application/text")
					.body("Erro na exclusão do marcador: " + e.getMessage()).setStatusCode(400);
		}
	}

	@Get("/app/marcador/editar")
	public void edita(final Long id) {
		Gson gson = new Gson();
		String json = "";
		if (id != null) {
			CpMarcador marcador = dao().consultar(id, CpMarcador.class, false);
			marcador = dao().obterAtual(marcador);
			result.include("marcador", marcador);
		}
		result.include("listaTipoMarcador", dao.listarTodos(CpTipoMarcador.class, null));
		result.include("listaCores", CpMarcadorCoresEnum.getList());
		result.include("listaTipoAplicacao", CpMarcadorTipoAplicacaoEnum.values());
		result.include("listaTipoExibicao", CpMarcadorTipoExibicaoEnum.values());
		result.include("listaTipoDataPlanejada", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoDataLimite", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoJustificativa", CpMarcadorTipoJustificativaEnum.values());
		result.include("listaTipoInteressado", CpMarcadorTipoInteressadoEnum.values());
	}

	@Transacional
	@Post("/app/marcador/gravar")
	public void marcadorGravar(Long id, final String sigla, final String descricao, final String descrDetalhada,
			final String cor, final String icone, final Integer grupoId, final Integer idTpMarcador,
			final Integer idTpAplicacao, final Integer idTpDataPlanejada, final Integer idTpDataLimite,
			final Integer idTpExibicao, final Integer idTpJustificativa, final Integer idTpInteressado)
			throws Exception {

		// assertAcesso("GI:Módulo de Gestão de Identidade;CAD_MARCADOR:Cadastrar
		// Marcador");

		if (id != null) {
			CpMarcador marcador = dao().consultar(id, CpMarcador.class, false);
			marcador = dao().obterAtual(marcador);
			id = marcador.getId();
		}
		
		Cp.getInstance().getBL().gravarMarcadorDaLotacao(id, getCadastrante(), getLotaTitular(),
				getIdentidadeCadastrante(), descricao, descrDetalhada, cor.replace("#", ""), icone, 14, idTpMarcador,
				idTpAplicacao, idTpDataPlanejada, idTpDataLimite, idTpExibicao, idTpJustificativa, idTpInteressado);

		result.redirectTo(this).lista(null);
	}

	private String toJson(final List<CpMarcador> list) throws Exception {
		Gson gson = new Gson();
		ArrayList<HashMap<String, Object>> listObjJson = new ArrayList<>();

		for (CpMarcador marcador : list) {
			HashMap<String, Object> marcadorJson = toHashMap(marcador);
			listObjJson.add(marcadorJson);
		}
		String json = gson.toJson(listObjJson);
		return json;
	}

	private HashMap<String, Object> toHashMap(CpMarcador marcador) {
		HashMap<String, Object> marcadorJson = new HashMap<String, Object>();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		marcadorJson.put("id", marcador.getIdMarcador());
		marcadorJson.put("hisDtIni", (marcador.getHisDtIni() != null ? df.format(marcador.getHisDtIni()) : ""));
		marcadorJson.put("hisDtFim", (marcador.getHisDtFim() != null ? df.format(marcador.getHisDtFim()) : ""));
		marcadorJson.put("descricao", marcador.getDescrMarcador());
		marcadorJson.put("descricaoDetalhada", marcador.getDescrDetalhada());
		marcadorJson.put("cor", marcador.getCor());
		marcadorJson.put("icone", marcador.getIcone());
		marcadorJson.put("responsavel", marcador.getHisIdcIni().getNmLoginIdentidade());
		marcadorJson.put("nomeResponsavel", marcador.getHisIdcIni().getDpPessoa().getNomePessoa());
		marcadorJson.put("idTpAplicacao", marcador.getIdTpAplicacao());
		marcadorJson.put("idTpDataPlanejada", marcador.getIdTpDataLimite());
		marcadorJson.put("idTpDataLimite", marcador.getIdTpDataLimite());
		marcadorJson.put("idTpExibicao", marcador.getIdTpExibicao());
		marcadorJson.put("idTpJustificativa", marcador.getIdTpJustificativa());
		marcadorJson.put("idTpInteressado", marcador.getIdTpInteressado());
		return marcadorJson;
	}

}