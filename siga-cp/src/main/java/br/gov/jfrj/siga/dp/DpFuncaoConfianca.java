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
 * Criado em  20/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.dp_funcao_confianca")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpFuncaoConfianca extends AbstractDpFuncaoConfianca implements
		Serializable, Historico, Selecionavel, Sincronizavel, DpConvertableEntity {

	@Formula(value = "REMOVE_ACENTO(NOME_FUNCAO_CONFIANCA)")
	@Desconsiderar
	private String nmFuncaoConfiancaAI;

	public DpFuncaoConfianca() {
		super();
	}

	// @Override
	// public String getNomeFuncao() {
	// String nome = super.getNomeFuncao();
	// if (super.getNomeFuncao().indexOf("(") > 0)
	// nome = super.getNomeFuncao().substring(0,
	// super.getNomeFuncao().indexOf("(")).trim();
	// return nome;
	// }

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
		return iniciais(getNomeFuncao());
	}
	
	public String getDescricaoIniciaisMaiusculas() {
		return Texto.maiusculasEMinusculas(getDescricao());
	}

	public Long getId() {
		return Long.valueOf(getIdFuncao());
	}

	public String getSigla() {
		return getSiglaFuncaoConfianca();
	}

	public void setSigla(final String sigla) {
		setSiglaFuncaoConfianca(sigla);
	}

	public String getDescricao() {
		return getNomeFuncao();
	}

	public String getNmFuncaoConfiancaAI() {
		return nmFuncaoConfiancaAI;
	}

	public void setNmFuncaoConfiancaAI(String nmFuncaoConfiancaAI) {
		this.nmFuncaoConfiancaAI = nmFuncaoConfiancaAI;
	}

	// Metodos necessarios para ser "Sincronizavel"
	//
	public Date getDataFim() {
		return getDataFimFuncao();
	}

	public Date getDataInicio() {
		return getDataInicioFuncao();
	}

	public String getDescricaoExterna() {
		return getDescricao();
	}

	public String getIdExterna() {
		return getIdeFuncao();
	}

	public Long getIdInicial() {
		return getIdFuncaoIni();
	}

	public String getLoteDeImportacao() {
		return getOrgaoUsuario().getId().toString();
	}

	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	public void setDataFim(Date dataFim) {
		setDataFimFuncao(dataFim);
	}

	public void setDataInicio(Date dataInicio) {
		setDataInicioFuncao(dataInicio);
	}

	public void setIdExterna(String idExterna) {
		setIdeFuncao(idExterna);
	}

	public void setIdInicial(Long idInicial) {
		setIdFuncaoIni(idInicial);
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((DpFuncaoConfianca) other)
				.getIdInicial().longValue();
	}

	@Override
	public void setId(Long id) {
		setIdFuncao(id);
	}

	@Override
	public Long getHisIdIni() {
		return getIdFuncaoIni();
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		setIdFuncaoIni(hisIdIni);
		
	}

	@Override
	public Date getHisDtIni() {
		return getDataInicioFuncao();
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		setDataInicioFuncao(hisDtIni);
		
	}

	@Override
	public Date getHisDtFim() {
		return getDataFimFuncao();
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		setDataFimFuncao(hisDtFim);
		
	}

	@Override
	public Integer getHisAtivo() {
		return getDataFimFuncao() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		// TODO Auto-generated method stub
		
	}
}
