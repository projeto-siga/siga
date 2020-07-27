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
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.cp.CpConvertableEntity;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@Table(name = "CP_ORGAO_USUARIO", schema = "CORPORATIVO")
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
public class CpOrgaoUsuario extends AbstractCpOrgaoUsuario implements
		Serializable, Selecionavel, Assemelhavel, CpConvertableEntity {
	public static ActiveRecord<CpOrgaoUsuario> AR = new ActiveRecord<>(
			CpOrgaoUsuario.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5119023571728936131L;

	@Formula(value = "REMOVE_ACENTO(NM_ORGAO_USU)")
	private String nmOrgaoAI;

	public CpOrgaoUsuario() {
		super();
	}

	public String iniciais(String s) {
		final StringBuilder sb = new StringBuilder(10);
		boolean f = true;

		s = s.replace(" E ", " ");
		s = s.replace(" DA ", " ");
		s = s.replace(" DE ", " ");
		s = s.replace(" DO ", " ");

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (f) {
				sb.append(c);
				f = false;
			}
			if (c == ' ') {
				f = true;
			}
		}
		return sb.toString();
	}

	public String getIniciais() {
		return iniciais(getNmOrgaoUsu());
	}

	public Long getId() {
		return getIdOrgaoUsu();
	}

	public String getSigla() {
		return getSiglaOrgaoUsu();
	}

	public void setSigla(final String sigla) {
		setSiglaOrgaoUsu(sigla);
	}

	public String getDescricao() {
		return getNmOrgaoUsu();
	}

	public String getDescricaoMaiusculas() {
		return getNmOrgaoUsu().toUpperCase();
	}

	public String getNmOrgaoAI() {
		return nmOrgaoAI;
	}

	public void setNmOrgaoAI(String nmOrgaoAI) {
		this.nmOrgaoAI = nmOrgaoAI;
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdOrgaoUsu().longValue() == ((CpOrgaoUsuario) other)
				.getIdOrgaoUsu().longValue();
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	@Override
	public String toString() {
		return getSigla();
	}

}
