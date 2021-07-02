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
package br.gov.jfrj.siga.relatorio;

import java.util.Date;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

public class AlteracaoDireitosItem extends SincronizavelSuporte implements
		Sincronizavel, Comparable {
	@Desconsiderar
	DpPessoa pessoa;
	@Desconsiderar
	CpServico servico;
	@Desconsiderar
	CpConfiguracao cfg;
	@Desconsiderar
	CpSituacaoDeConfiguracaoEnum situacao;
	long idPessoaIni;
	long idServico;
	long idSituacao;
	/**
	 * @return the situacao
	 */
	public CpSituacaoDeConfiguracaoEnum getSituacao() {
		return situacao;
	}

	/**
	 * @param situacao
	 *            the situacao to set
	 */
	public void setSituacao(CpSituacaoDeConfiguracaoEnum situacao) {
		this.situacao = situacao;
		idSituacao = situacao == null ? null: situacao.getId();
	}

	/**
	 * @return the pessoa
	 */
	public DpPessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
		idPessoaIni = pessoa.getIdInicial();
	}

	/**
	 * @return the servico
	 */
	public CpServico getServico() {
		return servico;
	}

	/**
	 * @param servico
	 *            the servico to set
	 */
	public void setServico(CpServico servico) {
		this.servico = servico;
		idServico = servico.getIdServico();
	}

	/**
	 * @return the cfg
	 */
	public CpConfiguracao getCfg() {
		return cfg;
	}

	/**
	 * @param cfg
	 *            the cfg to set
	 */
	public void setCfg(CpConfiguracao cfg) {
		this.cfg = cfg;
	}

	public String getDescricaoExterna() {
		StringBuffer stb = new StringBuffer();
		if (servico != null)
			stb.append(servico.getSigla());
		stb.append(':');
		if (pessoa != null)
			stb.append(pessoa.getSigla());
		stb.append(':');
		if (cfg != null
				&& cfg.getCpSituacaoConfiguracao() != null
				&& cfg.getCpSituacaoConfiguracao().getDescr() != null) {
			stb.append(cfg.getCpSituacaoConfiguracao().getDescr());
		}
		return stb.toString();
	}

	public String getIdExterna() {
		return servico.getId() + ": " + pessoa.getIdInicial();
	}

	public int compareTo(Object o) {
		return getIdExterna().compareTo(
				((AlteracaoDireitosItem) o).getIdExterna());
	}

	public String printOrigem() {
		try {
			return getCfg().printOrigem();
		} catch (Exception e) {
			return new String();
		}
	}

	public String printOrigemCurta() {
		try {
			if (getCfg() == null) return "DEFAULT";
			return getCfg().printOrigemCurta();
		} catch (Exception e) {
			return new String();
		}
	}

	public Date getInicio() {
		try {
			return getCfg().getHisDtIni();
		} catch (Exception e) {
			return null;
		}
	}

	public Date getFim() {
		try {
			return getCfg().getHisDtFim();
		} catch (Exception e) {
			return null;
		}
	}

	public DpPessoa getCadastrante() {
		try {
			return getCfg().getHisIdcIni().getDpPessoa();
		} catch (Exception e) {
			return null;
		}
	}
}
