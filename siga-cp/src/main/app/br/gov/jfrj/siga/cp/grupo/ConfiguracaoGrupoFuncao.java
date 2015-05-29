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

import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class ConfiguracaoGrupoFuncao extends ConfiguracaoGrupo {
	private DpFuncaoConfianca funcao;

	public DpFuncaoConfianca getFuncao() {
		return funcao;
	}

	public void setFuncao(DpFuncaoConfianca funcao) {
		this.funcao = funcao;
	}

	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setFuncaoConfianca(getFuncao());
	}

	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo());
		setFuncao(getCpConfiguracao().getFuncaoConfianca());
	}

	@Override
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.FUNCAOCONFIANCA;
	}

	public void setConteudoConfiguracao(String p_strConteudo) {
		/*
		 * Funcao t_pesPessoaExemplo = new Funcao(); String t_strUpper =
		 * p_strConteudo.toUpperCase(); String t_strSesb =
		 * t_strUpper.substring(0, 2);
		 * t_pesPessoaExemplo.setSesbPessoa(t_strSesb); String t_strMatricula =
		 * t_strUpper.substring(2, 7);
		 * t_pesPessoaExemplo.setMatricula(Long.parseLong(t_strMatricula));
		 * Funcao t_pesPessoa =
		 * CpDao.getInstance().consultarPorSigla(t_pesPessoaExemplo);
		 */

		Long t_lngId = Long.parseLong(p_strConteudo);
		DpFuncaoConfianca t_pesPessoa = CpDao.getInstance().consultar(t_lngId,
				DpFuncaoConfianca.class, false);
		setFuncao(t_pesPessoa);
		conteudoConfiguracao = p_strConteudo;
	}

	public String getConteudoConfiguracao() {
		if (getFuncao() == null) {
			conteudoConfiguracao = new String();
		} else {
			/*
			 * conteudoConfiguracao = getFuncao().getSesbPessoa() +
			 * String.valueOf(getFuncao().getMatricula());
			 */
			conteudoConfiguracao = String.valueOf(getFuncao().getId());
		}
		return conteudoConfiguracao;
	}

	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao = p_strSigla;
		if (getFuncao() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = String.valueOf(getFuncao().getId());
		}
	}

	public String getSiglaConteudoConfiguracao() {
		if (getFuncao() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = String.valueOf(getFuncao().getId());
		}
		return siglaConteudoConfiguracao;
	}

	public void setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		if (getFuncao() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getFuncao().getId();
		}
	}

	public Long getIdConteudoConfiguracao() {
		if (getFuncao() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getFuncao().getId();
		}
		return idConteudoConfiguracao;
	}

	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		if (getFuncao() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao = getFuncao().getDescricao();
		}
	}

	public String getDescricaoConteudoConfiguracao() {
		if (getFuncao() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao = getFuncao().getDescricao();
		}
		return descricaoConteudoConfiguracao;
	}
}
