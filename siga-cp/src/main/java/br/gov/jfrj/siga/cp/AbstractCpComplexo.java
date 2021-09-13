/*******************************************************************************
] * Copyright (c) 2006 - 2011 SJRJ.
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
package br.gov.jfrj.siga.cp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractCpComplexo extends Objeto implements Serializable {

	@Id
	@Column(name = "ID_COMPLEXO", unique = true, nullable = false)
	@Desconsiderar
	private Long idComplexo;

	@Column(name = "NOME_COMPLEXO", length = 100)
	private String nomeComplexo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOCALIDADE")
	@NaoRecursivo
	private CpLocalidade localidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public Long getIdComplexo() {
		return idComplexo;
	}

	public void setIdComplexo(Long idConfiguracao) {
		this.idComplexo = idConfiguracao;
	}

	public String getNomeComplexo() {
		return nomeComplexo;
	}

	public void setNomeComplexo(String nomeComplexo) {
		this.nomeComplexo = nomeComplexo;
	}

	public CpLocalidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(CpLocalidade localidade) {
		this.localidade = localidade;
	}

}
