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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.jfrj.siga.dp.dao.CpDao;

/**
 * A class that represents a row in the CP_ARQUIVO_BLOB table. You can customize
 * the behavior of this class by editing the class, {@link CpArquivoBlob()}.
 */
@SuppressWarnings("serial")
@Entity
@Immutable
@Table(name = "corporativo.cp_arquivo_blob")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
public class CpArquivoBlob implements Serializable {

	@Id
	@Column(name = "ID_ARQ_BLOB")
	private java.lang.Long idArqBlob;

	@MapsId
	@OneToOne(mappedBy = "arquivoBlob", fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQ_BLOB", referencedColumnName = "ID_ARQ") // same name as id @Column
	private CpArquivo arquivo;

	@Lob
	@Column(name = "CONTEUDO_ARQ_BLOB")
	private byte[] conteudoBlobArq;

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public CpArquivoBlob() {
	}

	public java.lang.Long getIdArqBlob() {
		return idArqBlob;
	}

	public void setIdArqBlob(java.lang.Long idArqBlob) {
		this.idArqBlob = idArqBlob;
	}

	public CpArquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(CpArquivo arquivo) {
		this.arquivo = arquivo;
	}

	public byte[] getConteudoBlobArq() {
		return conteudoBlobArq;
	}

	public void setConteudoBlobArq(byte[] conteudoBlobArq) {
		this.conteudoBlobArq = conteudoBlobArq;
	}

}