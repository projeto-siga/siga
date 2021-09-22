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
import java.util.Date;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.cp.CpConvertableEntity;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_orgao_usuario")
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.READ_ONLY)
public class CpOrgaoUsuario extends AbstractCpOrgaoUsuario implements
		Serializable, Selecionavel, Assemelhavel, CpConvertableEntity {
	public static ActiveRecord<CpOrgaoUsuario> AR = new ActiveRecord<>(
			CpOrgaoUsuario.class);

	@Formula(value = "REMOVE_ACENTO(NM_ORGAO_USU)")
	private String nmOrgaoAI;

	@Column(name = "HIS_DT_INI")
	@Desconsiderar
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	@Desconsiderar
	private Date hisDtFim;

	@Desconsiderar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@Desconsiderar
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;

	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

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

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public Date getHisDtFim() {
		return hisDtFim;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	public Integer getHisAtivo() {
		return hisAtivo;
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

	@PrePersist
	private void inserirComoAtivo() {
		if(Objects.isNull(hisAtivo)) {
			hisAtivo = 1;
		}
	}

}
