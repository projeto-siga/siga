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
package br.gov.jfrj.siga.wf.bl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfMovAnotacao;
import br.gov.jfrj.siga.wf.model.WfMovTransicao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfEngine;
import br.gov.jfrj.siga.wf.util.WfHandler;
import br.gov.jfrj.siga.wf.util.WfTarefa;
import br.gov.jfrj.siga.wf.util.WfTarefaComparator;

/**
 * Classe que representa a lógica do negócio do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfBL extends CpBL {
	public static final String WF_CADASTRANTE = "wf_cadastrante";
	public static final String WF_LOTA_CADASTRANTE = "wf_lota_cadastrante";
	public static final String WF_TITULAR = "wf_titular";
	public static final String WF_LOTA_TITULAR = "wf_lota_titular";
	private static WfTarefaComparator tic = new WfTarefaComparator();

	/**
	 * Cria uma instância de processo. Ao final da criação, define as seguintes
	 * variáveis na instância do processo: WF_CADASTRANTE - Pessoa responsável (que
	 * responde) pelas ações realizadas no sistema no momento da criação da
	 * instância do processo WF_LOTA_CADASTRANTE - Lotação da pessoa que está
	 * operando o sistema no momento da criação da instância do processo WF_TITULAR
	 * - Pessoa responsável (que responde) pelas ações realizadas no sistema no
	 * momento da criação da instância do processo WF_LOTA_TITULAR - Lotação da
	 * pessoa responsável (que responde) pelas ações realizadas no sistema no
	 * momento da criação da instância do processo
	 * 
	 * Essas variáveis são do banco de dados corporativo.
	 * 
	 * @param pdId
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param titular
	 * @param lotaTitular
	 * @param keys
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public WfProcedimento createProcessInstance(long pdId, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade, ArrayList<String> keys, ArrayList<String> values, boolean fCreateStartTask)
			throws Exception {

		// Create the process definition,
		WfDefinicaoDeProcedimento pd = WfDao.getInstance().consultar(pdId, WfDefinicaoDeProcedimento.class, false);

		// Create the process instance without responsible support
		HashMap<String, Object> variable = new HashMap<>();
//		variable.put(WF_CADASTRANTE, cadastrante.getSigla());
//		variable.put(WF_LOTA_CADASTRANTE, lotaCadastrante.getSiglaCompleta());
//		variable.put(WF_TITULAR, titular.getSigla());
//		variable.put(WF_LOTA_TITULAR, lotaTitular.getSiglaCompleta());

		if (keys != null && values != null) {
			for (int n = 0; n < keys.size(); n++) {
				variable.put(keys.get(n), values.get(n));
			}
		}

		WfProcedimento pi = new WfProcedimento(pd, variable);

		WfEngine engine = new WfEngine(dao(), new WfHandler(titular, lotaTitular, identidade));

		// Start the process instance
		engine.start(pi);

		return pi;
	}

	public void prosseguir(String event, Integer detourIndex, Map<String, Object> param, DpPessoa titular,
			DpLotacao lotaTitular, CpIdentidade identidade) throws Exception {
		WfEngine engine = new WfEngine(dao(), new WfHandler(titular, lotaTitular, identidade));
		engine.resume(event, detourIndex, param);
	}

	/**
	 * Retorna o conjunto de tarefas que estão na responsabilidade do usuário.
	 * 
	 * @throws AplicacaoException
	 */
	public SortedSet<WfTarefa> getTaskList(DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular)
			throws AplicacaoException {
		SortedSet<WfTarefa> tasks = WfDao.getInstance().consultarTarefasDeLotacao(lotaTitular);
		return tasks;
	}

	public SortedSet<WfTarefa> getTaskList(String siglaDoc) {
		List<WfProcedimento> pis = WfDao.getInstance().consultarProcedimentosAtivosPorEvento(siglaDoc);
		SortedSet<WfTarefa> tasks = new TreeSet<>();
		for (WfProcedimento pi : pis) {
			tasks.add(new WfTarefa(pi));
		}
		return tasks;
	}

	public static Boolean podePegarTarefa(DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaCadastrante,
			DpLotacao lotaTitular, WfTarefa ti) {
		return false;
	}

	private WfDao dao() {
		return (WfDao) getComp().getConfiguracaoBL().dao();
	}

	public void encerrarProcessInstance(Long id, Date consultarDataEHoraDoServidor) {
		// TODO Auto-generated method stub
	}

	public WfDefinicaoDeProcedimento getCopia(WfDefinicaoDeProcedimento original) {
		WfDefinicaoDeProcedimento copia = new WfDefinicaoDeProcedimento();
		try {

			PropertyUtils.copyProperties(copia, original);

			// novo id
			copia.setId(null);
			copia.setHisDtFim(null);
			copia.setHisDtIni(null);
			copia.updateAtivo();

		} catch (Exception e) {
			throw new AplicacaoException("Erro ao copiar as propriedades anteriores.");
		}

		return copia;
	}

	public void gravar(WfDefinicaoDeProcedimento novo, WfDefinicaoDeProcedimento antigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		if (novo.getNome() == null || novo.getNome().trim().length() == 0)
			throw new AplicacaoException("não é possível salvar sem informar o nome.");
		dao().gravarComHistorico(novo, antigo, dt, identidadeCadastrante);
	}

	private void gravarMovimentacao(final WfMov mov) throws AplicacaoException {
		dao().gravar(mov);
		if (mov.getProcedimento().getMovimentacoes() == null)
			mov.getProcedimento().setMovimentacoes(new TreeSet<WfMov>());
		mov.getProcedimento().getMovimentacoes().add(mov);
	}

	public void anotar(WfProcedimento pi, String descrMov, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade) {
		try {

			WfMovAnotacao mov = new WfMovAnotacao(pi, descrMov, dao().consultarDataEHoraDoServidor(), titular,
					lotaTitular, identidade);
			gravarMovimentacao(mov);
		} catch (final Exception e) {
			throw new AplicacaoException("Erro ao fazer anotação.", 0, e);
		}
	}

	public void excluirAnotacao(WfProcedimento pi, WfMovAnotacao mov) {
		pi.getMovimentacoes().remove(mov);
		mov.delete();
	}

	public void registrarTransicao(WfProcedimento pi, Integer de, Integer para, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade) {
		WfMovTransicao mov = new WfMovTransicao(pi, dao().consultarDataEHoraDoServidor(), titular, lotaTitular,
				identidade, de, para);
		gravarMovimentacao(mov);
	}

}
