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
/**
 * 
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_MOBIL table. You can customize the
 * behavior of this class by editing the class, {@link ExMobil()}.
 */
@MappedSuperclass
public abstract class AbstractExMobil extends Objeto implements Serializable {
	@Id
	@SequenceGenerator(name = "EX_MOBIL_SEQ")
	@GeneratedValue(generator = "EX_MOBIL_SEQ")
	@Column(name = "ID_MOBIL")
	private java.lang.Long IdMobil;

	@Column(name = "DNM_ULTIMA_ANOTACAO")
	private java.lang.String dnmUltimaAnotacao;

	@Column(name = "DNM_NUM_PRIMEIRA_PAGINA")
	private Integer dnmNumPrimeiraPagina;

	@ManyToOne
	@JoinColumn(name = "ID_DOC")
	private ExDocumento exDocumento;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_MOBIL")
	private ExTipoMobil exTipoMobil;

	@Column(name = "NUM_SEQUENCIA")
	private java.lang.Integer numSequencia;

	@OneToMany(mappedBy = "exMobil")
	private SortedSet<ExMovimentacao> exMovimentacaoSet;

	@OneToMany(mappedBy = "exMobilRef")
	private SortedSet<ExMovimentacao> exMovimentacaoReferenciaSet;

	@OneToMany(mappedBy = "exMobilPai")
	private java.util.Set<ExDocumento> exDocumentoFilhoSet;

	@OneToMany(mappedBy = "exMobil")
	private SortedSet<ExMarca> exMarcaSet;

	public java.lang.Long getIdMobil() {
		return IdMobil;
	}

	public void setIdMobil(java.lang.Long idMobil) {
		IdMobil = idMobil;
	}

	public ExDocumento getExDocumento() {
		return exDocumento;
	}

	public void setExDocumento(ExDocumento exDocumento) {
		this.exDocumento = exDocumento;
	}

	public ExTipoMobil getExTipoMobil() {
		return exTipoMobil;
	}

	public void setExTipoMobil(ExTipoMobil exTipoMobil) {
		this.exTipoMobil = exTipoMobil;
	}

	public java.lang.Integer getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(java.lang.Integer numSequencia) {
		this.numSequencia = numSequencia;
	}

	public SortedSet<ExMovimentacao> getExMovimentacaoSet() {
		return exMovimentacaoSet;
	}

	public void setExMovimentacaoSet(SortedSet<ExMovimentacao> exMovimentacaoSet) {
		this.exMovimentacaoSet = exMovimentacaoSet;
	}

	public SortedSet<ExMovimentacao> getExMovimentacaoReferenciaSet() {
		return exMovimentacaoReferenciaSet;
	}

	public void setExMovimentacaoReferenciaSet(
			SortedSet<ExMovimentacao> exMovimentacaoReferenciaSet) {
		this.exMovimentacaoReferenciaSet = exMovimentacaoReferenciaSet;
	}

	public java.util.Set<ExDocumento> getExDocumentoFilhoSet() {
		return exDocumentoFilhoSet;
	}

	public void setExDocumentoFilhoSet(
			java.util.Set<ExDocumento> exDocumentoFilhoSet) {
		this.exDocumentoFilhoSet = exDocumentoFilhoSet;
	}

	public java.util.SortedSet<ExMarca> getExMarcaSet() {
		return exMarcaSet;
	}

	public void setExMarcaSet(java.util.SortedSet<ExMarca> exMarcaSet) {
		this.exMarcaSet = exMarcaSet;
	}

	public java.lang.String getDnmUltimaAnotacao() {
		return dnmUltimaAnotacao;
	}

	public void setDnmUltimaAnotacao(java.lang.String dnmUltimaAnotacao) {
		this.dnmUltimaAnotacao = dnmUltimaAnotacao;
	}

	public Integer getDnmNumPrimeiraPagina() {
		return dnmNumPrimeiraPagina;
	}

	public void setDnmNumPrimeiraPagina(Integer dnmNumPrimeiraPagina) {
		this.dnmNumPrimeiraPagina = dnmNumPrimeiraPagina;
	}

}
