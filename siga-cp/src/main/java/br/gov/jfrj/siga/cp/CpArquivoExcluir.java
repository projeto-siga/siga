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
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A class that represents a row in the CP_ARQUIVO_EXCLUIR table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivoExcluir()}.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_arquivo_excluir")
public class CpArquivoExcluir implements Serializable {

	@Id
	@Column(name = "ID_ARQ_EXC")
	private Long idArqExc;

	@Column(name = "CAMINHO")
	private String caminho;

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public Long getIdArqExc() {
		return idArqExc;
	}

	public void setIdArqExc(Long idArqExc) {
		this.idArqExc = idArqExc;
	}
	
}