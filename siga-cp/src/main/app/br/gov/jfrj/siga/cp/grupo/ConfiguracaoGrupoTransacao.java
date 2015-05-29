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
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.dao.CpDao;

/**
 * Fachada para obtenção das instâncias de ConfiguracaoGrupoEmail via query
 */
public class ConfiguracaoGrupoTransacao {
	/**
	 * grava (inclui ou altera) a CpConfiguracao a partir da
	 * ConfiguracaoGrupoEmail
	 * 
	 * @param ConfiguracaoAcesso
	 *            p_cgeConfiguracao - A configuração de grupo de email.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void gravarConfiguracao(ConfiguracaoGrupo p_cgeConfiguracao,
			CpIdentidade identidadeCadastrante) throws Exception {
		if (p_cgeConfiguracao.getCpConfiguracao() == null) {
			p_cgeConfiguracao.setCpConfiguracao(new CpConfiguracao());
		}
		p_cgeConfiguracao.atualizarCpConfiguracao();
		CpDao.getInstance().iniciarTransacao();
		try {
			CpDao.getInstance().gravarComHistorico( 
					p_cgeConfiguracao.getCpConfiguracao(),
					identidadeCadastrante);
		} catch (Exception e) {
			CpDao.getInstance().rollbackTransacao();
			throw new AplicacaoException("Erro ao gravar: ", 0, e);
		}
		CpDao.getInstance().commitTransacao();
	}

	/**
	 * exclui a CpConfiguracao a partir da ConfiguracaoGrupoEmail
	 * 
	 * @param ConfiguracaoAcesso
	 *            p_cgeConfiguracao - A configuração de grupo de email.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void excluirConfiguracao(ConfiguracaoGrupo p_cgeConfiguracao)
			throws Exception {
		if (p_cgeConfiguracao.getCpConfiguracao() == null) {
			return;
		}
		CpDao.getInstance().iniciarTransacao();
		try {
			CpDao.getInstance().excluir(p_cgeConfiguracao.getCpConfiguracao());
			p_cgeConfiguracao.setCpConfiguracao(null);
		} catch (Exception e) {
			CpDao.getInstance().rollbackTransacao();
			throw new AplicacaoException("Erro ao excluir: ", 0, e);
		}
		CpDao.getInstance().commitTransacao();
	}
}
