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

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
/**
 *  Configuração de grupo para uma pessoa 
 */
public class ConfiguracaoGrupoPessoa extends ConfiguracaoGrupo {
	private DpPessoa dpPessoa;
	/**
	 * @return the dpPessoa
	 */
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	/**
	 * @param dpPessoa the dpPessoa to set
	 */
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}
	public void atualizarCpConfiguracao() {
		getCpConfiguracao().setCpGrupo(getCpGrupo());
		getCpConfiguracao().setDpPessoa(getDpPessoa());
	}
	@Override
	public void atualizarDeCpConfiguracao() {
		setCpGrupo(getCpConfiguracao().getCpGrupo()) ;
		setDpPessoa(getCpConfiguracao().getDpPessoa());
	}

	@Override
	public TipoConfiguracaoGrupoEnum obterTipoPadrao() {
		return TipoConfiguracaoGrupoEnum.PESSOA;
	}
	
	public void setConteudoConfiguracao(String p_strConteudo) {
		/*DpPessoa t_pesPessoaExemplo = new DpPessoa();
		String t_strUpper = p_strConteudo.toUpperCase();
		String t_strSesb = t_strUpper.substring(0, 2);
		t_pesPessoaExemplo.setSesbPessoa(t_strSesb);
		String t_strMatricula = t_strUpper.substring(2, 7);
		t_pesPessoaExemplo.setMatricula(Long.parseLong(t_strMatricula));
		DpPessoa t_pesPessoa =  CpDao.getInstance().consultarPorSigla(t_pesPessoaExemplo); */
		
		Long t_lngId = Long.parseLong(p_strConteudo);
		DpPessoa t_pesPessoa =  CpDao.getInstance().consultar(t_lngId,(new DpPessoa()).getClass(), false).getPessoaInicial();
		setDpPessoa(t_pesPessoa);
		conteudoConfiguracao = p_strConteudo;
	}
	public String getConteudoConfiguracao() {
		if (getDpPessoa() == null) {
			conteudoConfiguracao = new String();
		} else {
			/*conteudoConfiguracao =  getDpPessoa().getSesbPessoa() 
				+ String.valueOf(getDpPessoa().getMatricula()); */
			conteudoConfiguracao =  String.valueOf(getDpPessoa().getId());
		}
		return conteudoConfiguracao;
	}
	public void setSiglaConteudoConfiguracao(String p_strSigla) {
		siglaConteudoConfiguracao = p_strSigla;
		if (getDpPessoa() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = getDpPessoa().getSesbPessoa() 
				+ String.valueOf(getDpPessoa().getMatricula());
		}
	}
	public String getSiglaConteudoConfiguracao() {
		if (getDpPessoa() == null) {
			siglaConteudoConfiguracao = new String();
		} else {
			siglaConteudoConfiguracao = getDpPessoa().getSesbPessoa() 
				+ String.valueOf(getDpPessoa().getMatricula());
		}
		return siglaConteudoConfiguracao;
	}
	public void  setIdConteudoConfiguracao(Long p_lngId) {
		idConteudoConfiguracao = p_lngId;
		if (getDpPessoa() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao =  getDpPessoa().getId();
		}
	}
	public Long getIdConteudoConfiguracao() {
		if (getDpPessoa() == null) {
			idConteudoConfiguracao = new Long(-1);
		} else {
			idConteudoConfiguracao =  getDpPessoa().getId();
		}
		return idConteudoConfiguracao;
	}
	public void setDescricaoConteudoConfiguracao(String p_strDescricao) {
		descricaoConteudoConfiguracao = p_strDescricao;
		if (getDpPessoa() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao =  getDpPessoa().getDescricao();
		}
	}
	public String getDescricaoConteudoConfiguracao() {
		if (getDpPessoa() == null) {
			descricaoConteudoConfiguracao = new String();
		} else {
			descricaoConteudoConfiguracao =  getDpPessoa().getDescricao();
		}
		return descricaoConteudoConfiguracao;
	}
}
