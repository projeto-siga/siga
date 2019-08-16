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
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

/**
 * A class that represents a row in the CP_ARQUIVO table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivo()}.
 */
@Entity
@Table(name = "CP_ARQUIVO", schema = "CORPORATIVO")
public class CpArquivo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "CORPORATIVO.CP_ARQUIVO_SEQ", name = "CP_ARQUIVO_SEQ")
	@GeneratedValue(generator = "CP_ARQUIVO_SEQ") 
	@Column(name = "ID_ARQ")
	private java.lang.Long idArq;
	
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	@Column(name = "CONTEUDO_TP_ARQ", length = 128)
	private java.lang.String conteudoTpArq;

	@Lob
	@Column(name = "CONTEUDO_BLOB_ARQ")
	private byte[] conteudoBlobArq;

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public CpArquivo() {
	}

	public java.lang.Long getIdArq() {
		return idArq;
	}

	public void setIdArq(java.lang.Long idArq) {
		this.idArq = idArq;
	}

	public java.lang.String getConteudoTpArq() {
		return conteudoTpArq;
	}

	public void setConteudoTpArq(java.lang.String conteudoTpArq) {
		this.conteudoTpArq = conteudoTpArq;
	}

	public byte[] getConteudoBlobArq() {
		return conteudoBlobArq;
	}

	public void setConteudoBlobArq(byte[] conteudoBlobArq) {
		this.conteudoBlobArq = conteudoBlobArq;
	}

}