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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_papel")
public class CpPapel extends AbstractCpPapel implements Serializable,
		Selecionavel, Historico, Sincronizavel {

	public String getDescricao() {
		return (((getDpPessoa() == null) ? new String() : getDpPessoa()
				.getNomePessoa())
				+ ((getId() == null) ? new String() : "["
						+ String.valueOf(getId()) + "]")
				+ " (Papel "
				+ ((getCpTipoPapel() == null) ? new String() : getCpTipoPapel()
						.getDscTpPapel()) + ")");
	}

	public Long getId() {
		return getIdCpPapel();
	}

	public void setId(Long id) {
		setIdCpPapel(id);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((CpPapel) other)
				.getIdInicial().longValue();
	}

	public Date getDataFim() {
		return super.getHisDtFim();
	}

	public String getDescricaoExterna() {
		// return "papel id " + String.valueOf(super.getIdCpPapel());
		return getDescricao();
	}

	public String getIdExterna() {
		// return String.valueOf(super.getIdCpPapel());
		return super.getIdePapel();
	}

	public String getLoteDeImportacao() {
		return String.valueOf(super.getIdCpPapel());
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
		super.setIdePapel(idExterna);

	}

	public void setIdInicial(Long idInicial) {
		super.setHisIdIni(idInicial);

	}

	public void setLoteDeImportacao(String loteDeImportacao) {

	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public String getSigla() {
		return String.valueOf(super.getIdCpPapel());
	}

	public void setSigla(String sigla) {

	}

	public Date getDataInicio() {
		return super.getHisDtIni();
	}

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
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
