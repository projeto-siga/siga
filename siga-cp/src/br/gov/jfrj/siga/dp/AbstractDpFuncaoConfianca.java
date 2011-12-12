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
 * Criado em  20/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

/**
 * Classe que representa uma linha na tabela DP_FUNCAO_CONFIANCA. Você pode
 * customizar o comportamento desta classe editando a classe
 * {@link DpFuncaoConfianca}.
 */
public abstract class AbstractDpFuncaoConfianca implements Serializable {

	@Desconsiderar
	private Date dataFimFuncao;

	@Desconsiderar
	private Date dataInicioFuncao;

	private String categoriaFuncao;

	private Integer codFolhaFuncao;

	@Desconsiderar
	private Long idFuncao;
	
	@Desconsiderar
	private Long idFuncaoIni;

	private String ideFuncao;

	private Long idFuncaoPai;

	private Integer nivelFuncao;

	private String nomeFuncao;
	
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;
	
	@Desconsiderar
	private String siglaFuncaoConfianca;

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpFuncaoConfianca))
			return false;
		final DpFuncaoConfianca that = (DpFuncaoConfianca) rhs;

		if ((this.getIdFuncao() == null ? that.getIdFuncao() == null : this.getIdFuncao().equals(that.getIdFuncao()))) {
			if ((this.getNomeFuncao() == null ? that.getNomeFuncao() == null : this.getNomeFuncao().equals(
					that.getNomeFuncao())))
				return true;

		}
		return false;

	}

	public String getCategoriaFuncao() {
		return categoriaFuncao;
	}

	public Integer getCodFolhaFuncao() {
		return codFolhaFuncao;
	}

	/**
	 * @return Retorna o atributo idFuncao.
	 */
	public Long getIdFuncao() {
		return idFuncao;
	}

	public Long getIdFuncaoPai() {
		return idFuncaoPai;
	}

	public Integer getNivelFuncao() {
		return nivelFuncao;
	}

	/**
	 * @return Retorna o atributo nomeFuncao.
	 */
	public String getNomeFuncao() {
		return nomeFuncao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdFuncao() == null ? 0 : this.getIdFuncao().hashCode();
		result = result * 37 + idValue;
		idValue = this.getNomeFuncao() == null ? 0 : this.getNomeFuncao().hashCode();
		result = result * 37 + idValue;

		return result;
	}

	public void setCategoriaFuncao(final String categoriaFuncao) {
		this.categoriaFuncao = categoriaFuncao;
	}

	public void setCodFolhaFuncao(final Integer codFolhaFuncao) {
		this.codFolhaFuncao = codFolhaFuncao;
	}

	/**
	 * @param idFuncao
	 *            Atribui a idFuncao o valor.
	 */
	public void setIdFuncao(final Long idFuncao) {
		this.idFuncao = idFuncao;
	}

	public void setIdFuncaoPai(final Long idFuncaoPai) {
		this.idFuncaoPai = idFuncaoPai;
	}

	public void setNivelFuncao(final Integer nivelFuncao) {
		this.nivelFuncao = nivelFuncao;
	}

	/**
	 * @param nomeFuncao
	 *            Atribui a nomeFuncao o valor.
	 */
	public void setNomeFuncao(final String nomeFuncao) {
		this.nomeFuncao = nomeFuncao;
	}

	public Date getDataFimFuncao() {
		return dataFimFuncao;
	}

	public void setDataFimFuncao(Date dataFimFuncao) {
		this.dataFimFuncao = dataFimFuncao;
	}

	public Date getDataInicioFuncao() {
		return dataInicioFuncao;
	}

	public void setDataInicioFuncao(Date dataInicioFuncao) {
		this.dataInicioFuncao = dataInicioFuncao;
	}

	public Long getIdFuncaoIni() {
		return idFuncaoIni;
	}

	public void setIdFuncaoIni(Long idFuncaoIni) {
		this.idFuncaoIni = idFuncaoIni;
	}

	public String getIdeFuncao() {
		return ideFuncao;
	}

	public void setIdeFuncao(String ideFuncao) {
		this.ideFuncao = ideFuncao;
	}

	/**
	 * @return the siglaFuncaoConfianca
	 */
	public String getSiglaFuncaoConfianca() {
		return siglaFuncaoConfianca;
	}

	/**
	 * @param siglaFuncaoConfianca the siglaFuncaoConfianca to set
	 */
	public void setSiglaFuncaoConfianca(String siglaFuncaoConfianca) {
		this.siglaFuncaoConfianca = siglaFuncaoConfianca;
	}

}
