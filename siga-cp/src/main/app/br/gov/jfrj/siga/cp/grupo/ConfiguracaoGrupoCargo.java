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

import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class ConfiguracaoGrupoCargo extends ConfiguracaoGrupo {
	private DpCargo cargo;

	public DpCargo getCargo() {
		return cargo;
	}

	public void setCargo(DpCargo cargo) {
		this.cargo = cargo;
	}

	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setCargo(getCargo());
	}

	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo());
		setCargo(getCpConfiguracao().getCargo());
	}

	@Override
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.CARGO;
	}

	public void setConteudoConfiguracao(String p_strConteudo) {
		/*
		 * Cargo t_pesPessoaExemplo = new Cargo(); String t_strUpper =
		 * p_strConteudo.toUpperCase(); String t_strSesb =
		 * t_strUpper.substring(0, 2);
		 * t_pesPessoaExemplo.setSesbPessoa(t_strSesb); String t_strMatricula =
		 * t_strUpper.substring(2, 7);
		 * t_pesPessoaExemplo.setMatricula(Long.parseLong(t_strMatricula));
		 * Cargo t_pesPessoa =
		 * CpDao.getInstance().consultarPorSigla(t_pesPessoaExemplo);
		 */

		Long t_lngId = Long.parseLong(p_strConteudo);
		DpCargo t_pesPessoa = CpDao.getInstance().consultar(t_lngId,
				DpCargo.class, false);
		setCargo(t_pesPessoa);
		conteudoConfiguracao = p_strConteudo;
	}

	public String getConteudoConfiguracao() {
		if (getCargo() == null) {
			conteudoConfiguracao = new String();
		} else {
			/*
			 * conteudoConfiguracao = getCargo().getSesbPessoa() +
			 * String.valueOf(getCargo().getMatricula());
			 */
			conteudoConfiguracao = String.valueOf(getCargo().getId());
		}
		return conteudoConfiguracao;
	}

	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao = p_strSigla;
		if (getCargo() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = String.valueOf(getCargo().getId());
		}
	}

	public String getSiglaConteudoConfiguracao() {
		if (getCargo() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = String.valueOf(getCargo().getId());
		}
		return siglaConteudoConfiguracao;
	}

	public void setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		if (getCargo() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getCargo().getId();
		}
	}

	public Long getIdConteudoConfiguracao() {
		if (getCargo() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao = getCargo().getId();
		}
		return idConteudoConfiguracao;
	}

	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		if (getCargo() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao = getCargo().getDescricao();
		}
	}

	public String getDescricaoConteudoConfiguracao() {
		if (getCargo() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao = getCargo().getDescricao();
		}
		return descricaoConteudoConfiguracao;
	}
}
