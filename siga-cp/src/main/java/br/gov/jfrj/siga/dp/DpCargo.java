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
@Table(name = "corporativo.dp_cargo")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpCargo extends AbstractDpCargo implements Serializable,
		Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	@Formula(value = "REMOVE_ACENTO(NOME_CARGO)")
	@Desconsiderar
	private String nomeCargoAI;

	public String getNomeCargoAI() {
		return nomeCargoAI;
	}

	public void setNomeCargoAI(String nomeCargoAI) {
		this.nomeCargoAI = nomeCargoAI;
	}

	public DpCargo() {

	}

	// Nato: Essas linha precisaram ser comentadas para que a rotina de
	// sincronismo funcione corretamente. Se existe a necessidade de eliminar o
	// que esta escrito entre parentesis, essa alteracao deve ser realizada no
	// programa que gera o XML. No caso, atualmente, no MUMPS.
	//
	// @Override
	// public String getNomeCargo() {
	// String nome = super.getNomeCargo();
	// if (super.getNomeCargo().indexOf("(") > 0)
	// nome = super.getNomeCargo().substring(0,
	// super.getNomeCargo().indexOf("(")).trim();
	// return nome;
	// }

	public String getSigla() {
		return getSiglaCargo();
	}

	public Long getId() {
		return new Long(getIdCargo());
	}

	public String getDescricao() {
		return getNomeCargo();
	}
	
	public String getDescricaoIniciaisMaiusculas() {
		return Texto.maiusculasEMinusculas(getDescricao());
	}

	public void setSigla(String sigla) {
		setSiglaCargo(sigla);
	}

	public void setId(Long id) {
		setIdCargo(id);
	}

	public void setDescricao(String descricao) {
		setNomeCargo(descricao);

	}

	// Metodos necessarios para ser "Sincronizavel"
	//
	public Date getDataFim() {
		return getDataFimCargo();
	}

	public Date getDataInicio() {
		return getDataInicioCargo();
	}

	public String getDescricaoExterna() {
		return getDescricao();
	}

	public String getIdExterna() {
		return getIdeCargo();
	}

	public Long getIdInicial() {
		return getIdCargoIni();
	}

	public String getLoteDeImportacao() {
		return getOrgaoUsuario().getId().toString();
	}

	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	public void setDataFim(Date dataFim) {
		setDataFimCargo(dataFim);
	}

	public void setDataInicio(Date dataInicio) {
		setDataInicioCargo(dataInicio);
	}

	public void setIdExterna(String idExterna) {
		setIdeCargo(idExterna);
	}

	public void setIdInicial(Long idInicial) {
		setIdCargoIni(idInicial);
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((DpCargo) other)
				.getIdInicial().longValue();
	}

	@Override
	public Long getHisIdIni() {
		return getIdCargoIni();
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		setIdCargoIni(hisIdIni);
		
	}

	@Override
	public Date getHisDtIni() {
		return getDataInicioCargo();
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		setDataInicioCargo(hisDtIni);
		
	}

	@Override
	public Date getHisDtFim() {
		return getDataFimCargo();
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		setDataFimCargo(hisDtFim);
		
	}

	@Override
	public Integer getHisAtivo() {
		return getDataFimCargo() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		// TODO Auto-generated method stub
		
	}

}
