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
package br.gov.jfrj.siga.wf.model;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Classe que representa um configuração do sistema de workflow.
 * @author kpf
 *
 */
public class WfConfiguracao extends CpConfiguracao {
	private String procedimento;
	private String raia;
	private String tarefa;
	private String expressao;
	private DpPessoa ator;
	private DpLotacao lotaAtor;

	/**
	 * Retorna a lotação do ator.
	 * @return
	 */
	public DpLotacao getLotaAtor() {
		return lotaAtor;
	}

	/**
	 * Define a lotação do ator.
	 * @param lotaAtor
	 */
	public void setLotaAtor(DpLotacao lotaAtor) {
		this.lotaAtor = lotaAtor;
	}

	/**
	 * Retorna o procedimento.
	 * @return
	 */
	public String getProcedimento() {
		return procedimento;
	}

	/**
	 * Define o procedimento.
	 * @param procedimento
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	/**
	 * Retorna a raia.
	 * @return
	 */
	public String getRaia() {
		return raia;
	}

	/**
	 * Define a raia.
	 * @param raia
	 */
	public void setRaia(String raia) {
		this.raia = raia;
	}

	/**
	 * Retorna a tarefa.
	 * @return
	 */
	public String getTarefa() {
		return tarefa;
	}

	/**
	 * Define a tarefa.
	 * @param tarefa
	 */
	public void setTarefa(String tarefa) {
		this.tarefa = tarefa;
	}

	/**
	 * Retorna a expressão.
	 * @return
	 */
	public String getExpressao() {
		return expressao;
	}

	/**
	 * Define a expressão.
	 * @param expressao
	 */
	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

	/**
	 * Retorna o ator.
	 * @return
	 */
	public DpPessoa getAtor() {
		return ator;
	}

	/**
	 * Define o ator.
	 * @param ator
	 */
	public void setAtor(DpPessoa ator) {
		this.ator = ator;
	}
}
