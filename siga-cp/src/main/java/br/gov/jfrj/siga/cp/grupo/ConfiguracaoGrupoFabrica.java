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
package br.gov.jfrj.siga.cp.grupo;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;

/**
 * Fábrica para a geração de uma Configuração de grupo
 */
public class ConfiguracaoGrupoFabrica {
	/**
	 * Cria uma configuração de grupo (ConfiguracaoGrupo)
	 * 
	 * @param p_enmTipo
	 *            TipoConfiguracaoGrupoEnum O tipo de configuração de grupo que
	 *            deseja criar. @ return ConfiguracaoGrupo uma nova instância de
	 *            configuração de grupo
	 * @throws Exception
	 */
	public ConfiguracaoGrupo getInstance(TipoConfiguracaoGrupoEnum p_enmTipo)
			throws Exception {
		if (p_enmTipo == TipoConfiguracaoGrupoEnum.EMAIL) {
			return new ConfiguracaoGrupoEmail();
		} else if (p_enmTipo == TipoConfiguracaoGrupoEnum.FORMULA) {
			return new ConfiguracaoGrupoFormula();
		} else if (p_enmTipo == TipoConfiguracaoGrupoEnum.LOTACAO) {
			return new ConfiguracaoGrupoLotacao();
		} else if (p_enmTipo == TipoConfiguracaoGrupoEnum.PESSOA) {
			return new ConfiguracaoGrupoPessoa();
		} else if (p_enmTipo == TipoConfiguracaoGrupoEnum.CARGO) {
			return new ConfiguracaoGrupoCargo();
		} else if (p_enmTipo == TipoConfiguracaoGrupoEnum.FUNCAOCONFIANCA) {
			return new ConfiguracaoGrupoFuncao();
		} else {
			throw new AplicacaoException(
					"Parâmetro TipoConfiguracaoGrupoEmailEnum."
							+ p_enmTipo.getDescricao() + " não tratado !");
		}
	}

	/**
	 * Cria uma configuração de grupo (ConfiguracaoGrupo)
	 * 
	 * @param p_enmTipo
	 *            TipoConfiguracaoGrupoEnum O tipo de configuração de grupo que
	 *            deseja criar. @ return ConfiguracaoGrupo uma nova instância de
	 *            configuração de grupo
	 * @throws Exception
	 */
	public ConfiguracaoGrupo getInstance(CpConfiguracao p_cpcConfiguracao)
			throws Exception {
		if (p_cpcConfiguracao.getCpGrupo() == null) {
			throw new Exception("Grupo nulo.");
		}
		ConfiguracaoGrupo t_cgpConfiguracao = null;
		if (p_cpcConfiguracao.getNmEmail() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoEmail();
		}
		if (p_cpcConfiguracao.getDscFormula() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoFormula();
		}
		if (p_cpcConfiguracao.getLotacao() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoLotacao();
		}
		if (p_cpcConfiguracao.getDpPessoa() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoPessoa();
		}
		if (p_cpcConfiguracao.getCargo() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoCargo();
		}
		if (p_cpcConfiguracao.getFuncaoConfianca() != null) {
			t_cgpConfiguracao = new ConfiguracaoGrupoFuncao();
		}
		if (t_cgpConfiguracao == null) {
			throw new Exception("CpConfiguracao incompatível.");
		}
		t_cgpConfiguracao.setCpConfiguracao(p_cpcConfiguracao);
		t_cgpConfiguracao.atualizarDeCpConfiguracao();
		return t_cgpConfiguracao;
	}
}
