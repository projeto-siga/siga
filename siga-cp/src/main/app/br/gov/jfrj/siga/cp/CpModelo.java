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

import java.io.UnsupportedEncodingException;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.jfrj.siga.cp.util.Blob;
import br.gov.jfrj.siga.dp.dao.CpDaoUtil;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@Table(name = "CP_MODELO", schema = "CORPORATIVO")
@NamedQuery(name = "consultarCpModelos", query = "select u from CpModelo u where u.hisDtFim is null")
public class CpModelo extends AbstractCpModelo {
	private static final long serialVersionUID = 1256614496347273713L;

	@Transient
	private byte[] cacheConteudoBlobMod;
	@Transient
	private String cacheConteudo;

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((CpModelo) other)
				.getIdInicial().longValue();
	}

	public void setIdInicial(Long idInicial) {
		setHisIdIni(idInicial);
	}

	public Long getId() {
		return getIdMod();
	}

	public void setId(Long id) {
		setIdMod(id);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public String getConteudoBlobString() throws UnsupportedEncodingException {
		if (cacheConteudo != null)
			return cacheConteudo;
		updateCache();
		return cacheConteudo;
	}

	//**
	public void setConteudoBlobString(String conteudo)
			throws UnsupportedEncodingException {
		if (conteudo != null && conteudo.trim().length() == 0)
			conteudo = null;
		setConteudoBlobMod(conteudo != null ? CpDaoUtil.createBlob(conteudo) : null);
		cacheConteudo = conteudo;
	}

	@PostLoad
	private void updateCache() throws UnsupportedEncodingException {
		if (getConteudoBlobMod() == null)
			return;
		cacheConteudo = new String(Blob.toByteArray(getConteudoBlobMod()),
				"ISO-8859-1");
	}
}
