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
import br.gov.jfrj.siga.wf.model.WfConfiguracao;

/**
 * Classe que representa a configuração do sistema de workflow.
 * 
 * @author kpf
 * 
 */
public class WfConfiguracaoBL extends CpConfiguracaoBL {

	public static int PROCEDIMENTO = 100;
	public static int RAIA = 101;
	public static int TAREFA = 102;

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
	public boolean atendeExigencias(CpConfiguracao cfgFiltro,
			Set<Integer> atributosDesconsiderados, CpConfiguracao cfg,
			SortedSet<CpPerfil> perfis) {
		if (!super.atendeExigencias(cfgFiltro, atributosDesconsiderados, cfg,
				perfis))
			return false;

		if (cfg instanceof WfConfiguracao
				&& cfgFiltro instanceof WfConfiguracao) {
			WfConfiguracao wfCfg = (WfConfiguracao) cfg;
			WfConfiguracao wfCfgFiltro = (WfConfiguracao) cfgFiltro;

			if (wfCfg.getProcedimento() != null
					&& ((wfCfgFiltro.getProcedimento() != null
							&& !wfCfg.getProcedimento().equals(
									wfCfgFiltro.getProcedimento()) || ((wfCfgFiltro
							.getProcedimento() == null) && !atributosDesconsiderados
							.contains(PROCEDIMENTO)))))
				return false;

			if (wfCfg.getRaia() != null
					&& ((wfCfgFiltro.getRaia() != null
							&& !wfCfg.getRaia().equals(wfCfgFiltro.getRaia()) || ((wfCfgFiltro
							.getRaia() == null) && !atributosDesconsiderados
							.contains(RAIA)))))
				return false;

			if (wfCfg.getTarefa() != null
					&& ((wfCfgFiltro.getTarefa() != null
							&& !wfCfg.getTarefa().equals(
									wfCfgFiltro.getTarefa()) || ((wfCfgFiltro
							.getTarefa() == null) && !atributosDesconsiderados
							.contains(TAREFA)))))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * Método com implementação completa, chamado pelas outras sobrecargas
	 * 
	 */
	public boolean podePorConfiguracao(CpOrgaoUsuario cpOrgaoUsu,
			DpLotacao dpLotacao, DpCargo cargo,
			DpFuncaoConfianca dpFuncaoConfianca, DpPessoa dpPessoa,
			long idTpConf, String procedimento, String raia, String tarefa)
			throws Exception {

		
		if (isUsuarioRoot(dpPessoa)){
			return true;
		}

		WfConfiguracao cfgFiltro = createNewConfiguracao();

		cfgFiltro.setCargo(cargo);
		cfgFiltro.setOrgaoUsuario(cpOrgaoUsu);
		cfgFiltro.setFuncaoConfianca(dpFuncaoConfianca);
		cfgFiltro.setLotacao(dpLotacao);
		cfgFiltro.setDpPessoa(dpPessoa);
		cfgFiltro.setCpTipoConfiguracao(CpDao.getInstance().consultar(idTpConf,
				CpTipoConfiguracao.class, false));

		cfgFiltro.setProcedimento(procedimento);
		cfgFiltro.setRaia(raia);
		cfgFiltro.setTarefa(tarefa);

		CpConfiguracao cfg = (CpConfiguracao) buscaConfiguracao(cfgFiltro,
				new int[] { 0 }, null);

		CpSituacaoConfiguracao situacao;
		if (cfg != null)
			situacao = cfg.getCpSituacaoConfiguracao();
		else
			situacao = cfgFiltro.getCpTipoConfiguracao().getSituacaoDefault();

		if (situacao != null
				&& situacao.getIdSitConfiguracao() == CpSituacaoConfiguracao.SITUACAO_PODE)
			return true;
		return false;
	}
}
