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
 * Created Mon Nov 14 13:33:06 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.model.Assemelhavel;

/**
 * A class that represents a row in the 'EX_TEMPORALIDADE' table. This class may
 * be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "siga.ex_temporalidade")
public class ExTemporalidade extends AbstractExTemporalidade implements
		Comparable {

	/**
	 * Simple constructor of ExTemporalidade instances.
	 */
	public ExTemporalidade() {
	}

	public Long getId() {
		return getIdTemporalidade();
	}

	public void setId(Long id) {
		setIdTemporalidade(id);
	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public int getValorEmDias() {
		if (getValorTemporalidade() == null || getCpUnidadeMedida() == null)
			return 0;
		int valor = getValorTemporalidade();
		switch (getCpUnidadeMedida().getIdUnidadeMedida().intValue()) {
		case CpUnidadeMedida.ANO:
			valor *= 365;
			break;
		case CpUnidadeMedida.MES:
			valor *= 30;
			break;
		case CpUnidadeMedida.DIA:
			valor *= 1;
			break;
		}
		return valor;
	}

	public Date getPrazoAPartirDaData(Date dt) {
		if (getCpUnidadeMedida() == null || getValorTemporalidade() == null)
			return dt;
		Calendar calFuturo = Calendar.getInstance();
		calFuturo.setTime(dt);
		int calendarField = 0;
		switch (getCpUnidadeMedida().getIdUnidadeMedida().intValue()) {
		case CpUnidadeMedida.ANO:
			calendarField = Calendar.YEAR;
			break;
		case CpUnidadeMedida.MES:
			calendarField = Calendar.MONTH;
			break;
		case CpUnidadeMedida.DIA:
			calendarField = Calendar.DAY_OF_MONTH;
			break;
		}
		calFuturo.add(calendarField, getValorTemporalidade());
		return calFuturo.getTime();

	}

	public int compareTo(Object o) {
		if (o == null)
			return 1;
		int a = getValorEmDias();
		int b = ((ExTemporalidade) o).getValorEmDias();
		return a > b ? 1 : a < b ? -1 : 0;
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