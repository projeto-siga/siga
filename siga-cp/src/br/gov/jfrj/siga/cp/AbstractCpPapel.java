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
package br.gov.jfrj.siga.cp;

import java.io.Serializable;
import java.util.Date;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

public abstract class AbstractCpPapel extends HistoricoSuporte implements
		Serializable {
	@Desconsiderar
	private Long idCpPapel;
	private DpPessoa dpPessoa;
	private DpLotacao dpLotacao;
	private DpFuncaoConfianca dpFuncaoConfianca;
	private DpCargo dpCargo;
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;
	private String idePapel;
	// private Date dataInicioPapel;
	// private Date dataFimPapel;
	// private Long idInicial;
	private CpTipoPapel cpTipoPapel;

	/**
	 * @return the idPapel
	 */
	public Long getIdCpPapel() {
		return idCpPapel;
	}

	/**
	 * @param idPapel
	 *            the idPapel to set
	 */
	public void setIdCpPapel(Long idPapel) {
		this.idCpPapel = idPapel;
	}

	/**
	 * @return the dpPessoa
	 */
	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	/**
	 * @param dpPessoa
	 *            the dpPessoa to set
	 */
	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	/**
	 * @return the dpLotacao
	 */
	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}

	/**
	 * @param dpLotacao
	 *            the dpLotacao to set
	 */
	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}

	/**
	 * @return the funcaoConfianca
	 */
	public DpFuncaoConfianca getDpFuncaoConfianca() {
		return dpFuncaoConfianca;
	}

	/**
	 * @param funcaoConfianca
	 *            the funcaoConfianca to set
	 */
	public void setDpFuncaoConfianca(DpFuncaoConfianca dpFuncaoConfianca) {
		this.dpFuncaoConfianca = dpFuncaoConfianca;
	}

	/**
	 * @return the dpCargo
	 */
	public DpCargo getDpCargo() {
		return dpCargo;
	}

	/**
	 * @param dpCargo
	 *            the dpCargo to set
	 */
	public void setDpCargo(DpCargo dpCargo) {
		this.dpCargo = dpCargo;
	}

	/**
	 * @return the orgaoUsuario
	 */
	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	/**
	 * @param orgaoUsuario
	 *            the orgaoUsuario to set
	 */
	public void setOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.orgaoUsuario = cpOrgaoUsuario;
	}

	/**
	 * @return the idePapel
	 */
	public String getIdePapel() {
		return idePapel;
	}

	/**
	 * @param idePapel
	 *            the idePapel to set
	 */
	public void setIdePapel(String idePapel) {
		this.idePapel = idePapel;
	}

	/**
	 * @return the cpTipoPapel
	 */
	public CpTipoPapel getCpTipoPapel() {
		return cpTipoPapel;
	}

	/**
	 * @param cpTipoPapel
	 *            the cpTipoPapel to set
	 */
	public void setCpTipoPapel(CpTipoPapel cpTipoPapel) {
		this.cpTipoPapel = cpTipoPapel;
	}

	/**
	 * @param dpPapelInicial
	 *            the dpPapelInicial to set
	 */

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

}
