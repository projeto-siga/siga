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
 * Criado em  12/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractExTipoFormaDoc extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(sequenceName = "EX_TIPO_FORMA_DOCUMENTO_SEQ", name = "EX_TIPO_FORMA_DOCUMENTO_SEQ")
	@GeneratedValue(generator = "EX_TIPO_FORMA_DOCUMENTO_SEQ")
	@Column(name = "ID_TIPO_FORMA_DOC", unique = true, nullable = false)
	private Long idTipoFormaDoc;

	@Column(name = "DESC_TIPO_FORMA_DOC", length = 60)
	private String descTipoFormaDoc;

	@Column(name = "NUMERACAO_UNICA")
	private Integer numeracaoUnica;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exTipoFormaDoc")
	private Set<ExFormaDocumento> exFormaDocSet;

	public Long getIdTipoFormaDoc() {
		return idTipoFormaDoc;
	}

	public void setIdTipoFormaDoc(Long idTipoFormaDoc) {
		this.idTipoFormaDoc = idTipoFormaDoc;
	}

	public String getDescTipoFormaDoc() {
		return descTipoFormaDoc;
	}

	public void setDescTipoFormaDoc(String descTipoFormaDoc) {
		this.descTipoFormaDoc = descTipoFormaDoc;
	}

	public Set<ExFormaDocumento> getExFormaDocSet() {
		return exFormaDocSet;
	}

	public void setExFormaDocSet(Set<ExFormaDocumento> exFormaDocSet) {
		this.exFormaDocSet = exFormaDocSet;
	}

	public Integer getNumeracaoUnica() {
		return numeracaoUnica;
	}

	public void setNumeracaoUnica(Integer numeracaoUnica) {
		this.numeracaoUnica = numeracaoUnica;
	}
}
