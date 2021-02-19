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
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfConfiguracao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

/**
 * Classe que representa a configuração do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfConfiguracaoBL extends CpConfiguracaoBL {

	public static int DEFINICAO_DE_PROCEDIMENTO = 100;

	/**
	 * Cria uma nova configuração.
	 */
	@Override
	public WfConfiguracao createNewConfiguracao() {
		return new WfConfiguracao();
	}

	/**
	 * Verifica se a configuração é uma configuração válida.
	 */
	@Override
	public boolean atendeExigencias(CpConfiguracao cfgFiltro, Set<Integer> atributosDesconsiderados, CpConfiguracao cfg,
			SortedSet<CpPerfil> perfis) {
		if (!super.atendeExigencias(cfgFiltro, atributosDesconsiderados, cfg, perfis))
			return false;

		if (cfg instanceof WfConfiguracao && cfgFiltro instanceof WfConfiguracao) {
			WfConfiguracao wfCfg = (WfConfiguracao) cfg;
			WfConfiguracao wfCfgFiltro = (WfConfiguracao) cfgFiltro;

			if (wfCfg.getDefinicaoDeProcedimento() != null && ((wfCfgFiltro.getDefinicaoDeProcedimento() != null
					&& !wfCfg.getDefinicaoDeProcedimento().equivale(wfCfgFiltro.getDefinicaoDeProcedimento())
					|| ((wfCfgFiltro.getDefinicaoDeProcedimento() == null)
							&& !atributosDesconsiderados.contains(DEFINICAO_DE_PROCEDIMENTO)))))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * Método com implementação completa, chamado pelas outras sobrecargas
	 * 
	 */
	public boolean podePorConfiguracao(CpOrgaoUsuario cpOrgaoUsu, DpLotacao dpLotacao, DpCargo cargo,
			DpFuncaoConfianca dpFuncaoConfianca, DpPessoa dpPessoa, long idTpConf,
			WfDefinicaoDeProcedimento definicaoDeProcedimento) throws Exception {
		WfConfiguracao cfgFiltro = createNewConfiguracao();

		cfgFiltro.setCargo(cargo);
		cfgFiltro.setOrgaoUsuario(cpOrgaoUsu);
		cfgFiltro.setFuncaoConfianca(dpFuncaoConfianca);
		cfgFiltro.setLotacao(dpLotacao);
		cfgFiltro.setDpPessoa(dpPessoa);
		cfgFiltro.setCpTipoConfiguracao(CpDao.getInstance().consultar(idTpConf, CpTipoConfiguracao.class, false));

		cfgFiltro.setDefinicaoDeProcedimento(definicaoDeProcedimento);

		CpConfiguracao cfg = (CpConfiguracao) buscaConfiguracao(cfgFiltro, new int[] { 0 }, null);

		CpSituacaoConfiguracao situacao;
		if (cfg != null)
			situacao = cfg.getCpSituacaoConfiguracao();
		else
			situacao = cfgFiltro.getCpTipoConfiguracao().getSituacaoDefault();

		if (situacao != null && situacao.getIdSitConfiguracao() == CpSituacaoConfiguracao.SITUACAO_PODE)
			return true;
		return false;
	}

	/**
	 * 
	 * Retorna uma lista de (ex)configurações vigentes de acordo com um certo tipo
	 * 
	 * @param WfConfiguracao
	 * 
	 */
	public List<WfConfiguracao> buscarConfiguracoesVigentes(final WfConfiguracao exemplo) {
		Date hoje = new Date();
		List<WfConfiguracao> todasConfig = WfDao.getInstance().consultar(exemplo);
		List<WfConfiguracao> configVigentes = new ArrayList<WfConfiguracao>();

		for (WfConfiguracao cfg : todasConfig) {
			if (!cfg.ativaNaData(hoje))
				continue;
			configVigentes.add(cfg);
		}
		return (configVigentes);
	}

	/**
	 * Varre as entidades definidas na configuração para evitar que o hibernate
	 * guarde versões lazy delas.
	 * 
	 * @param listaCfg - lista de configurações que podem ter objetos lazy
	 */
	protected void evitarLazy(List<CpConfiguracao> provResults) {
		for (CpConfiguracao cpCfg : provResults) {
			if (cpCfg instanceof WfConfiguracao) {
				WfConfiguracao cfg = (WfConfiguracao) cpCfg;
				if (cfg.getDefinicaoDeProcedimento() != null) {
					cfg.getDefinicaoDeProcedimento().getNome();
				}
			}
		}
	}
}
