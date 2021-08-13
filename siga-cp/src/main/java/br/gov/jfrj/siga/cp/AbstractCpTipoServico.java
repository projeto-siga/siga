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

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.cp.converter.CpSituacaoDeConfiguracaoEnumConverter;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public class AbstractCpTipoServico extends Objeto {

	@Id
	@Column(name = "ID_TP_SERVICO", unique = true, nullable = false)
	private Integer idCpTpServico;

	@Column(name = "DESC_TP_SERVICO", length = 60)
	private String dscTpServico;

//	@Convert(converter = CpSituacaoDeConfiguracaoEnumConverter.class)
	@Column(name = "ID_SIT_CONFIGURACAO")
	private CpSituacaoDeConfiguracaoEnum situacaoDefault;

	/**
	 * @return the idCpTpServico
	 */
	public Integer getIdCpTpServico() {
		return idCpTpServico;
	}

	/**
	 * @param idCpTpServico
	 *            the idCpTpServico to set
	 */
	public void setIdCpTpServico(Integer idCpTpServico) {
		this.idCpTpServico = idCpTpServico;
	}

	/**
	 * @return the dscTpServico
	 */
	public String getDscTpServico() {
		return dscTpServico;
	}

	/**
	 * @param dscTpServico
	 *            the dscTpServico to set
	 */
	public void setDscTpServico(String dscTpServico) {
		this.dscTpServico = dscTpServico;
	}

	/**
	 * @return the cpSituacoesConfiguracaoSet
	 */
	public Set<CpSituacaoDeConfiguracaoEnum> getCpSituacoesConfiguracaoSet() {
		Set<CpSituacaoDeConfiguracaoEnum> set = new TreeSet<>();
		set.add(CpSituacaoDeConfiguracaoEnum.PODE);
		set.add(CpSituacaoDeConfiguracaoEnum.NAO_PODE);
		return set;
	}

	/**
	 * @return the situacaoDefault
	 */
	public CpSituacaoDeConfiguracaoEnum getSituacaoDefault() {
		return situacaoDefault;
	}

	/**
	 * @param situacaoDefault
	 *            the situacaoDefault to set
	 */
	public void setSituacaoDefault(CpSituacaoDeConfiguracaoEnum situacaoDefault) {
		this.situacaoDefault = situacaoDefault;
	}

//	/**
//	 * @param cpSituacoesConfiguracaoSet
//	 *            the cpSituacoesConfiguracaoSet to set
//	 */
//	public void setCpSituacoesConfiguracaoSet(
//			Set<CpSituacaoConfiguracao> cpSituacoesConfiguracaoSet) {
//		this.cpSituacoesConfiguracaoSet = cpSituacoesConfiguracaoSet;
//	}

}
