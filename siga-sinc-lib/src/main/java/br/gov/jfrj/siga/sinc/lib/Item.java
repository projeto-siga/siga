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
package br.gov.jfrj.siga.sinc.lib;

public class Item {
	public enum Operacao {
		incluir, alterar, excluir
	}

	private Sincronizavel novo;
	private Sincronizavel antigo;
	private Operacao operacao;
	private int nivel = -1;

	public Item(Operacao operacao, Sincronizavel novo, Sincronizavel antigo) {
		this.operacao = operacao;
		this.novo = novo;
		this.antigo = antigo;
	}

	public Sincronizavel getNovo() {
		return novo;
	}

	public void setNovo(Sincronizavel novo) {
		this.novo = novo;
	}

	public Sincronizavel getAntigo() {
		return antigo;
	}

	public void setAntigo(Sincronizavel antigo) {
		this.antigo = antigo;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	public int getNivelDeDependencia() {
		if (nivel != -1)
			return nivel;
		if (getOperacao() == Operacao.incluir)
			nivel = Integer.valueOf(getNovo().getNivelDeDependencia());
		if (getOperacao() == Operacao.alterar)
			nivel = Integer.valueOf(getNovo().getNivelDeDependencia());
		if (getOperacao() == Operacao.excluir)
			nivel = Integer.valueOf(getAntigo().getNivelDeDependencia());
		if (nivel == -1)
			throw new Error("Invalid operation.");
		return nivel;
	}

	public String getDescricao() {
		if (getOperacao() == Operacao.incluir) {
			return "Incluindo: " + getNovo().getDescricaoExterna() + " ("
					+ getNivelDeDependencia() + ")";
		}
		if (getOperacao() == Operacao.excluir) {
			return "Excluindo: " + getAntigo().getDescricaoExterna() + " ("
					+ getNivelDeDependencia() + ")";
		}
		if (getOperacao() == Operacao.alterar) {
			return "Atualizando: " + getNovo().getDescricaoExterna() + " ("
					+ getNivelDeDependencia() + ")";
		}
		return null;
	}
}
