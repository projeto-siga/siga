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
/*
 * Criado em  24/08/2005
 *
 *
 */
package br.gov.jfrj.siga.rh;

public class Lotacao {

	private Integer codLotacao;

	private String descricaoLotacao;

	private Lotacao lotacaoPai;

	private String siglaLotacao;

	private String tipoLotacao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Integer#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if ((obj == null) || !(obj instanceof Lotacao))
			return false;
		final Lotacao l = (Lotacao) obj;
		// se o codLotacao for nulo e o outro tambem
		if ((this.codLotacao == null ? l.codLotacao == null : this.codLotacao.equals(l.codLotacao))) {
			if ((this.descricaoLotacao == null ? l.descricaoLotacao == null : this.descricaoLotacao
					.equals(l.descricaoLotacao)))
				return true;

		}
		return false;
	}

	/**
	 * @return Retorna o atributo codLotacao.
	 */
	public Integer getCodLotacao() {
		return codLotacao;
	}

	/**
	 * @return Retorna o atributo descricaoLotacao.
	 */
	public String getDescricaoLotacao() {
		return descricaoLotacao;
	}

	/**
	 * @return Retorna o atributo lotacaoPai.
	 */
	public Lotacao getLotacaoPai() {
		return lotacaoPai;
	}

	/**
	 * @return Retorna o atributo siglaLotacao.
	 */
	public String getSiglaLotacao() {
		return siglaLotacao;
	}

	/**
	 * @return Retorna o atributo tipoLotacao.
	 */
	public String getTipoLotacao() {
		return tipoLotacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Integer#hashCode()
	 */
	@Override
	public int hashCode() {
		final int inicial = 17;
		int codigo = inicial; // Semente
		if (codLotacao != null) {
			codigo = codigo * inicial + codLotacao.hashCode();
		}
		if (descricaoLotacao != null) {
			codigo = codigo * inicial + descricaoLotacao.hashCode();
		}
		return codigo;
	}

	/**
	 * @param codLotacao
	 *            Atribui a codLotacao o valor.
	 */
	public void setCodLotacao(final Integer codLotacao) {
		this.codLotacao = codLotacao;
	}

	/**
	 * @param descricaoLotacao
	 *            Atribui a descricaoLotacao o valor.
	 */
	public void setDescricaoLotacao(final String descricaoLotacao) {
		this.descricaoLotacao = descricaoLotacao;
	}

	/**
	 * @param lotacaoPai
	 *            Atribui a lotacaoPai o valor.
	 */
	public void setLotacaoPai(final Lotacao lotacaoPai) {
		this.lotacaoPai = lotacaoPai;
	}

	/**
	 * @param siglaLotacao
	 *            Atribui a siglaLotacao o valor.
	 */
	public void setSiglaLotacao(final String siglaLotacao) {
		this.siglaLotacao = siglaLotacao;
	}

	/**
	 * @param tipoLotacao
	 *            Atribui a tipoLotacao o valor.
	 */
	public void setTipoLotacao(final String tipoLotacao) {
		this.tipoLotacao = tipoLotacao;
	}

}
