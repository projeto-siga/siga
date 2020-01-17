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
import br.gov.jfrj.siga.wf.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.WfProcedimento;
import br.gov.jfrj.siga.wf.WfResponsavel;
import br.gov.jfrj.siga.wf.WfTarefa;
import br.gov.jfrj.siga.wf.WfTarefaComparator;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfEngine;
import br.gov.jfrj.siga.wf.util.WfHandler;

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
	public WfProcedimento createProcessInstance(long pdId, DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular, ArrayList<String> keys, ArrayList<String> values,
			boolean fCreateStartTask) throws Exception {

		// Create the process definition,
		WfDefinicaoDeProcedimento pd = WfDao.getInstance().consultar(pdId, WfDefinicaoDeProcedimento.class, false);

		// Create the process instance without responsible support
		HashMap<String, Object> variable = new HashMap<>();
		variable.put(WF_CADASTRANTE, cadastrante.getSigla());
		variable.put(WF_LOTA_CADASTRANTE, lotaCadastrante.getSiglaCompleta());
		variable.put(WF_TITULAR, titular.getSigla());
		variable.put(WF_LOTA_TITULAR, lotaTitular.getSiglaCompleta());

		if (keys != null && values != null) {
			for (int n = 0; n < keys.size(); n++) {
				variable.put(keys.get(n), values.get(n));
			}
		}

		WfProcedimento pi = new WfProcedimento(pd, variable) {
			@Override
			public WfResponsavel calcResponsible(WfDefinicaoDeTarefa tarefa) {
				return null;
			}
		};

		WfEngine engine = new WfEngine(dao(), new WfHandler());

		// Start the process instance
		engine.start(pi);

		return pi;
	}

	public void prosseguir(String event, Integer detourIndex, Map<String, Object> param) throws Exception {

		WfEngine engine = new WfEngine(dao(), new WfHandler());

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
		List<WfProcedimento> pis = WfDao.getInstance().consultarProcedimentosAtivosPorDocumento(siglaDoc);
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

}
