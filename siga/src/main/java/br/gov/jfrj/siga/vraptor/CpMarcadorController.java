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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoAplicacaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoDataEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoTextoEnum;
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
	private static final String ACESSO_CAD_MARCADOR = "FE: Ferramentas;CAD_MARCADOR: Cadastro de Marcadores;";
	private static final String ACESSO_CAD_MARCADOR_LOTA = "MAR_LOTA:Cadastro de Marcador da Lotação;";
	private static final String ACESSO_CAD_MARCADOR_GERAL_LOTA = "MAR_GERAL_LOTA:Cadastro de Marcador Geral da Lotação;";

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
		assertAcesso("");
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
				
		result.include("listaTipoMarcador", listarTipoMarcador());
		result.include("listaLotacao", listaLotacao);
		result.include("listaCores", CpMarcadorCorEnum.values());
		result.include("listaIcones", CpMarcadorIconeEnum.values());
		result.include("listaTipoAplicacao", CpMarcadorTipoAplicacaoEnum.values());
		result.include("listaTipoExibicao", CpMarcadorTipoExibicaoEnum.values());
		result.include("listaTipoDataPlanejada", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoDataLimite", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoJustificativa", CpMarcadorTipoTextoEnum.values());
		result.include("listaTipoInteressado", CpMarcadorTipoInteressadoEnum.values());
		result.include("listaMarcadores", dao.listarCpMarcadoresPorLotacaoESublotacoes(getLotaCadastrante(), true));
	}

	@Get("/app/marcador/historico")
	public void historico(final Long id) throws Exception {
		assertAcesso("");
		List<CpMarcador> listMar = dao().listarTodosPorIdInicial(CpMarcador.class, id, "hisDtIni", true);
		result.include("listaHistorico", listMar);
	}

	@Get("/app/marcador/excluir")
	public void exclui(final Long id) {
		assertAcesso(ACESSO_CAD_MARCADOR_LOTA);
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
		assertAcesso(ACESSO_CAD_MARCADOR_LOTA);
		
		if (id != null) {
			CpMarcador marcador = dao().consultar(id, CpMarcador.class, false);
			marcador = dao().obterAtual(marcador);
			result.include("marcador", marcador);
		}
		result.include("listaTipoMarcador", listarTipoMarcador());
		result.include("listaCores", CpMarcadorCorEnum.values());
		result.include("listaIcones", CpMarcadorIconeEnum.values());
		result.include("listaTipoAplicacao", CpMarcadorTipoAplicacaoEnum.values());
		result.include("listaTipoExibicao", CpMarcadorTipoExibicaoEnum.values());
		result.include("listaTipoDataPlanejada", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoDataLimite", CpMarcadorTipoDataEnum.values());
		result.include("listaTipoJustificativa", CpMarcadorTipoTextoEnum.values());
		result.include("listaTipoInteressado", CpMarcadorTipoInteressadoEnum.values());
	}

	@Transacional
	@Post("/app/marcador/gravar")
	public void marcadorGravar(Long id, final String sigla, final String descricao, final String descrDetalhada,
			final Integer idCor, final Integer idIcone, final Integer grupoId, final Long idTpMarcador,
			final Integer idTpAplicacao, final Integer idTpDataPlanejada, final Integer idTpDataLimite,
			final Integer idTpExibicao, final Integer idTpTexto, final Integer idTpInteressado)
			throws Exception {

		assertAcesso(ACESSO_CAD_MARCADOR_LOTA);
		
		if (idTpMarcador.equals(CpTipoMarcador.TIPO_MARCADOR_GERAL))		
			assertAcesso(ACESSO_CAD_MARCADOR_GERAL_LOTA);

		if (id != null) {
			CpMarcador marcador = dao().consultar(id, CpMarcador.class, false);
			marcador = dao().obterAtual(marcador);
			id = marcador.getId();
		}
		
		Cp.getInstance().getBL().gravarMarcadorDaLotacao(id, getCadastrante(), getLotaTitular(),
				getIdentidadeCadastrante(), descricao, descrDetalhada, idCor, idIcone, 14, idTpMarcador,
				idTpAplicacao, idTpDataPlanejada, idTpDataLimite, idTpExibicao, idTpTexto, idTpInteressado);

		result.redirectTo(this).lista(null);
	}

	private List<CpTipoMarcador> listarTipoMarcador() {
		List<CpTipoMarcador> lstTpMarcador = dao.listarTodos(CpTipoMarcador.class, null);
		boolean podeMarcadorGeral;
		try {
			assertAcesso(ACESSO_CAD_MARCADOR_GERAL_LOTA);
			podeMarcadorGeral = true;
		} catch (AplicacaoException e) {
			podeMarcadorGeral = false;
		}
		for (Iterator<CpTipoMarcador> iter = lstTpMarcador.listIterator(); iter.hasNext(); ) {
		    CpTipoMarcador tipoMar = iter.next();
			if (tipoMar.getIdTpMarcador() == CpTipoMarcador.TIPO_MARCADOR_GERAL
					&& !podeMarcadorGeral)
				iter.remove();
			if (tipoMar.getIdTpMarcador() == CpTipoMarcador.TIPO_MARCADOR_SISTEMA)
				iter.remove();
		}
			
//		lstTpMarcador.removeIf(t -> t.getIdTpMarcador() == CpTipoMarcador.TIPO_MARCADOR_SISTEMA);
//		try {
//			assertAcesso(ACESSO_MARCADOR_GERAL_LOTA);
//		} catch (AplicacaoException e) {
//			lstTpMarcador.removeIf(t -> t.getIdTpMarcador() == CpTipoMarcador.TIPO_MARCADOR_GERAL);
//		}
		return lstTpMarcador;
	}

	protected void assertAcesso(String pathServico) throws AplicacaoException {
		super.assertAcesso(ACESSO_CAD_MARCADOR 
					+ pathServico);
	}
}