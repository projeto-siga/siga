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
package br.gov.jfrj.siga.wf.vraptor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeResponsavel;
import br.gov.jfrj.siga.wf.model.WfResponsavel;
import br.gov.jfrj.siga.wf.util.WfUtil;

@Controller
@Path("app/responsavel")
public class WfResponsavelController extends WfController {
	private static final String VERIFICADOR_ACESSO = "FE;DEFR:Gerenciar Responsáveis";
	private static final Logger LOGGER = Logger.getLogger(WfResponsavelController.class);

	public static class Item {
		private Long orgaoId;
		private String orgaoSigla;
		private DpLotacao lotacao;
		private DpPessoa pessoa;

		public Long getOrgaoId() {
			return orgaoId;
		}

		public void setOrgaoId(Long orgaoId) {
			this.orgaoId = orgaoId;
		}

		public String getOrgaoSigla() {
			return orgaoSigla;
		}

		public void setOrgaoSigla(String orgaoSigla) {
			this.orgaoSigla = orgaoSigla;
		}

		public DpLotacao getLotacao() {
			return lotacao;
		}

		public void setLotacao(DpLotacao lotacao) {
			this.lotacao = lotacao;
		}

		public DpPessoa getPessoa() {
			return pessoa;
		}

		public void setPessoa(DpPessoa pessoa) {
			this.pessoa = pessoa;
		}
	}

	/**
	 * @deprecated CDI eyes only
	 */
	public WfResponsavelController() {
		super();
	}

	/**
	 * Inicializa os tipos de responsáveis e suas respectivas expressões, quando
	 * houver.
	 */
	@Inject
	public WfResponsavelController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	@Get("listar")
	public void lista() throws Exception {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<WfDefinicaoDeResponsavel> modelos = dao().listarAtivos(WfDefinicaoDeResponsavel.class, "nome");
			result.include("itens", modelos);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AplicacaoException(ex.getMessage(), 0, ex);
		}
	}

	@Get("carregar")
	public void carregar() throws Exception {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<WfDefinicaoDeResponsavel> list = dao().listarAtivos(WfDefinicaoDeResponsavel.class, "nome");
			result.use(Results.json()).from(list, "list").serialize();
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AplicacaoException(ex.getMessage(), 0, ex);
		}
	}

	@Get("editar/{id}")
	public void edita(final Long id) throws UnsupportedEncodingException {
		assertAcesso(VERIFICADOR_ACESSO);

		WfDefinicaoDeResponsavel dr = new WfDefinicaoDeResponsavel();
		List<Item> l = new ArrayList<>();

		if (id == null) {
			result.include("dr", dr);
			result.include("itens", l);
			return;
		}

		dr = dao().consultar(id, WfDefinicaoDeResponsavel.class, false);
		result.include("dr", dr);

		Map<Long, WfResponsavel> map = mapaDeResponsaveis(dr);

		for (CpOrgaoUsuario o : dao().consultaCpOrgaoUsuario()) {
			Item i = new Item();
			i.orgaoId = o.getId();
			i.orgaoSigla = o.getSigla();
			if (map.containsKey(o.getId())) {
				WfResponsavel r = map.get(o.getId());
				i.pessoa = r.getPessoa();
				i.lotacao = r.getLotacao();
			}
			l.add(i);
		}
		result.include("dr", dr);
		result.include("itens", l);
	}

	@Get("novo")
	public void novo() throws UnsupportedEncodingException {
		result.forwardTo(this).edita(null);
	}

	private Map<Long, WfResponsavel> mapaDeResponsaveis(WfDefinicaoDeResponsavel dr) {
		Map<Long, WfResponsavel> map = new HashMap<>();
		List<WfResponsavel> resps = dao().consultarResponsaveisPorDefinicaoDeResponsavel(dr);
		if (resps != null)
			for (WfResponsavel r : resps)
				map.put(r.getOrgaoUsuario().getId(), r);
		return map;
	}

	@Transacional
	@Post("gravar")
	public void gravar(final Long id, final String nome, final String descr, List<Item> itens)
			throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		Date dt = dao().consultarDataEHoraDoServidor();

		if (id == null) {
			WfDefinicaoDeResponsavel dr = new WfDefinicaoDeResponsavel();
			dr.setNome(nome);
			dr.setDescr(descr);
			dr.setHisDtIni(dt);
			dao().gravarComHistorico(dr, getIdentidadeCadastrante());
			result.redirectTo(this).edita(dr.getId());
			return;
		}
		WfDefinicaoDeResponsavel dr = dao().consultar(id, WfDefinicaoDeResponsavel.class, false);
		dr = dao().consultarAtivoPorIdInicial(WfDefinicaoDeResponsavel.class, dr.getHisIdIni());

		WfDefinicaoDeResponsavel drNovo = new WfDefinicaoDeResponsavel();
		drNovo.setNome(nome);
		drNovo.setDescr(descr);
		drNovo.setHisIdIni(dr.getHisIdIni());
		dr = (WfDefinicaoDeResponsavel) dao().gravarComHistorico(drNovo, dr, dt, getIdentidadeCadastrante());

		// Utilizaremos o sincronizador para perceber apenas as diferenças entre a
		// definição que está guardada no banco de dados e a nova versão submetida..
		Sincronizador sinc = new Sincronizador();
		SortedSet<Sincronizavel> setDepois = new TreeSet<>();
		SortedSet<Sincronizavel> setAntes = new TreeSet<>();

		List<WfResponsavel> resps = dao().consultarResponsaveisPorDefinicaoDeResponsavel(dr);
		if (resps != null)
			setAntes.addAll(resps);
		if (itens != null) {
			for (Item i : itens) {
				if (i.pessoa != null && i.pessoa.getId() == null)
					i.pessoa = null;
				else
					i.pessoa = dao().carregarPorId(i.pessoa);
				if (i.lotacao != null && i.lotacao.getId() == null)
					i.lotacao = null;
				else
					i.lotacao = dao().carregarPorId(i.lotacao);
				if (i.pessoa == null && i.lotacao == null)
					continue;
				WfResponsavel r = new WfResponsavel(i.pessoa, i.lotacao);
				r.setOrgaoUsuario(dao().consultar(i.orgaoId, CpOrgaoUsuario.class, false));
				r.setDefinicaoDeResponsavel(dao().consultar(dr.getIdInicial(), dr.getClass(), false));
				r.setIdExterna(r.criarIdExterna());
				setDepois.add(r);
			}
		}

		sinc.setSetNovo(setDepois);
		sinc.setSetAntigo(setAntes);
		List<br.gov.jfrj.siga.sinc.lib.Item> list = sinc.getEncaixe();

		for (br.gov.jfrj.siga.sinc.lib.Item i : list) {
			switch (i.getOperacao()) {
			case alterar:
				i.getNovo().setIdInicial(i.getAntigo().getIdInicial());
				dao().gravarComHistorico((HistoricoAuditavel) i.getNovo(), (HistoricoAuditavel) i.getAntigo(), dt,
						getIdentidadeCadastrante());
				break;
			case incluir:
				i.getNovo().setDataInicio(dt);
				dao().gravarComHistorico((HistoricoAuditavel) i.getNovo(), getIdentidadeCadastrante());
				break;
			case excluir:
				((HistoricoAuditavel) i.getAntigo()).setHisDtFim(dt);
				dao().gravarComHistorico((HistoricoAuditavel) i.getAntigo(), getIdentidadeCadastrante());
				break;
			}
		}

		result.redirectTo(this).lista();
	}
}
