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
 * Criado em  21/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarVisualizacoesPermitidas", query = "from DpVisualizacao as dps "
				+ " where (dps.dtIniDeleg < sysdate or dps.dtIniDeleg is null) "
				+ " and (dps.dtFimDeleg > sysdate or dps.dtFimDeleg is null) "
				+ " and dps.delegado.idPessoa in (select pes.idPessoa from DpPessoa as pes where pes.idPessoaIni = :idDelegadoIni) "
				+ " and dps.dtFimRegistro is null "),
		@NamedQuery(name = "consultarOrdem", query = "from DpVisualizacao as dps "
				+ " where "
				+ " dps.titular.idPessoa in (select pes.idPessoa from DpPessoa as pes where pes.idPessoaIni = :idTitularIni) "
				+ " and dps.dtFimRegistro is null "
				+ " order by dps.dtIniDeleg, dps.dtFimDeleg ")
		})
public abstract class AbstractDpVisualizacao extends Objeto implements
		Serializable {

	@SequenceGenerator(name = "generator", sequenceName = "CORPORATIVO.DP_VISUALIZACAO_SEQ")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID_VISUALIZACAO", unique = true, nullable = false)
	private Long idVisualizacao;

	@Column(name = "DT_FIM_DELEG")
	@Temporal(TemporalType.DATE)
	private Date dtFimDeleg;

	@Column(name = "DT_INI_DELEG")
	@Temporal(TemporalType.DATE)
	private Date dtIniDeleg;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DELEG")
	private DpPessoa delegado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TITULAR")
	private DpPessoa titular;

	@Column(name = "DT_FIM_REG", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFimRegistro;

	@Column(name = "DT_INI_REG", nullable = false, length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniRegistro;

	@Column(name = "ID_REG_INI")
	private Long idRegistroInicial;

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpVisualizacao))
			return false;
		final DpVisualizacao that = (DpVisualizacao) rhs;

		if ((this.getIdVisualizacao() == null ? that.getIdVisualizacao() == null
				: this.getIdVisualizacao().equals(that.getIdVisualizacao())))
			return true;
		return false;
	}

	public Long getIdVisualizacao() {
		return idVisualizacao;
	}

	public void setIdVisualizacao(Long idVisualizacao) {
		this.idVisualizacao = idVisualizacao;
	}

	public Date getDtFimDeleg() {
		return dtFimDeleg;
	}

	public void setDtFimDeleg(Date dtFimDeleg) {
		this.dtFimDeleg = dtFimDeleg;
	}

	public Date getDtIniDeleg() {
		return dtIniDeleg;
	}

	public void setDtIniDeleg(Date dtIniDeleg) {
		this.dtIniDeleg = dtIniDeleg;
	}

	public DpPessoa getDelegado() {
		return delegado;
	}

	public void setDelegado(DpPessoa delegado) {
		this.delegado = delegado;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public Date getDtFimRegistro() {
		return dtFimRegistro;
	}

	public void setDtFimRegistro(Date dtFimRegistro) {
		this.dtFimRegistro = dtFimRegistro;
	}

	public Date getDtIniRegistro() {
		return dtIniRegistro;
	}

	public void setDtIniRegistro(Date dtIniRegistro) {
		this.dtIniRegistro = dtIniRegistro;
	}

	public Long getIdRegistroInicial() {
		return idRegistroInicial;
	}

	public void setIdRegistroInicial(Long idRegistroInicial) {
		this.idRegistroInicial = idRegistroInicial;
	}
}