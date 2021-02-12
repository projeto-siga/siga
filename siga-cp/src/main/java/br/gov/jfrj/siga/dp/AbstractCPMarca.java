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
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
@MappedSuperclass
public abstract class AbstractCPMarca extends Objeto implements Serializable {

	@Id
	@SequenceGenerator(name = "my_seq", sequenceName = "CORPORATIVO"
			+ ".CP_MARCA_SEQ")
	@GeneratedValue(generator = "my_seq")
	@Column(name = "ID_MARCA")
	private java.lang.Long idMarca;

	@Column(name = "DT_INI_MARCA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniMarca;

	@Column(name = "DT_FIM_MARCA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFimMarca;

//	@Column(name = "DT_REF1_MARCA")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dtRef1Marca;
//
//	@Column(name = "DT_REF2_MARCA")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date dtRef2Marca;

	@ManyToOne
	@JoinColumn(name = "ID_MARCADOR")
	private CpMarcador cpMarcador;

	@ManyToOne
	@JoinColumn(name = "ID_PESSOA_INI")
	private DpPessoa dpPessoaIni;

	@ManyToOne
	@JoinColumn(name = "ID_LOTACAO_INI")
	private DpLotacao dpLotacaoIni;

	@ManyToOne
	@JoinColumn(name = "ID_TP_MARCA", insertable = false, updatable = false)
	private CpTipoMarca cpTipoMarca;

	public CpTipoMarca getCpTipoMarca() {
		return cpTipoMarca;
	}

	public void setCpTipoMarca(CpTipoMarca cpTipoMarca) {
		this.cpTipoMarca = cpTipoMarca;
	}

	public java.lang.Long getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(java.lang.Long idMarca) {
		this.idMarca = idMarca;
	}

	public Date getDtIniMarca() {
		return dtIniMarca;
	}

	public void setDtIniMarca(Date dtIniMarca) {
		this.dtIniMarca = dtIniMarca;
	}

	public Date getDtFimMarca() {
		return dtFimMarca;
	}

	public void setDtFimMarca(Date dtFimMarca) {
		this.dtFimMarca = dtFimMarca;
	}

//	public Date getDtRef1Marca() {
//		return dtRef1Marca;
//	}
//
//	public void setDtRef1Marca(Date dtRef1Marca) {
//		this.dtRef1Marca = dtRef1Marca;
//	}
//
//	public Date getDtRef2Marca() {
//		return dtRef2Marca;
//	}
//
//	public void setDtRef2Marca(Date dtRef2Marca) {
//		this.dtRef2Marca = dtRef2Marca;
//	}

	public CpMarcador getCpMarcador() {
		return cpMarcador;
	}

	public void setCpMarcador(CpMarcador cpMarcador) {
		this.cpMarcador = cpMarcador;
	}

	public DpPessoa getDpPessoaIni() {
		return dpPessoaIni;
	}

	public void setDpPessoaIni(DpPessoa dpPessoaIni) {
		this.dpPessoaIni = dpPessoaIni;
	}

	public DpLotacao getDpLotacaoIni() {
		return dpLotacaoIni;
	}

	public void setDpLotacaoIni(DpLotacao dpLotacaoIni) {
		this.dpLotacaoIni = dpLotacaoIni;
	}

}
