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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_orgao")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class CpOrgao extends AbstractCpOrgao implements Serializable,
		Selecionavel, Historico, Sincronizavel {

	@Formula(value = "REMOVE_ACENTO(NM_ORGAO)")
	@Desconsiderar
	private String nmOrgaoAI;

	public CpOrgao() {
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
		return iniciais(getNmOrgao());
	}

	public Long getId() {
		return Long.valueOf(getIdOrgao());
	}
	
	public void setId(Long id){
		setIdOrgao(id);
	}

	public String getSigla() {
		if (getSiglaOrgao() != null)
			return getSiglaOrgao();
		return getIdOrgao().toString();
	}

	public void setSigla(final String sigla) {
		setSiglaOrgao(sigla);
	}

	public String getDescricao() {
		return getNmOrgao();
	}
	
	public String getRegistroAtivo(){
		return this.getAtivo();
	}

	public String getNmOrgaoAI() {
		return nmOrgaoAI;
	}

	public void setNmOrgaoAI(String nmOrgaoAI) {
		this.nmOrgaoAI = nmOrgaoAI;
	}

	public Date getDataFim() {
		return super.getHisDtFim();

	}

	public Date getDataInicio() {
		return super.getHisDtIni();

	}

	public String getDescricaoExterna() {
		return this.getDescricao();
	}

	public String getIdExterna() {
		return super.getIdeOrgao();
	}

	public String getLoteDeImportacao() {
		return String.valueOf(super.getIdOrgao());
		// return getOrgaoUsuario().getId().toString();
	}

	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	public void setDataFim(Date dataFim) {
		super.setHisDtFim(dataFim);

	}

	public void setDataInicio(Date dataInicio) {
		super.setHisDtIni(dataInicio);

	}

	public void setIdExterna(String idExterna) {
		super.setIdeOrgao(idExterna);

	}

	public void setIdInicial(Long idInicial) {
		super.setHisIdIni(idInicial);
	}

	public Long getIdInicial() {
		return super.getHisIdIni();
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	/*
	 * public boolean semelhante(Assemelhavel obj, int profundidade) { return
	 * SincronizavelSuporte.semelhante(this, obj, profundidade); }
	 */
	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((CpOrgao) other)
				.getIdInicial().longValue();
	}


	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}
	
	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}
}
