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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.DateUtils;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Selecionavel;

@Entity
@Table(name = "corporativo.cp_feriado")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class CpFeriado extends AbstractCpFeriado implements Serializable,
		Selecionavel {
	
	public static final ActiveRecord<CpFeriado> AR = new ActiveRecord<>(CpFeriado.class);

	public String getDescricao() {
		return getDscFeriado();
	}

	public Long getId() {
		return new Long(getIdFeriado());
	}

	public String getSigla() {
		return getDscFeriado();
	}

	public void setSigla(String sigla) {
		setDscFeriado(sigla);
	}

	public boolean abrange(Date data) {

		Calendar calendarFeriado = new GregorianCalendar();
		Calendar calendarData = new GregorianCalendar();
		calendarData.setTime(DateUtils.clearTime(data));
		
		for (CpOcorrenciaFeriado ocorrencia : getCpOcorrenciaFeriadoSet()) {
			calendarFeriado.setTime(ocorrencia.getDtIniFeriado());
			if (calendarFeriado.get(Calendar.YEAR) <= calendarData.get(Calendar.YEAR)) {
				calendarFeriado.set(Calendar.YEAR, calendarData.get(Calendar.YEAR));
				if (!calendarFeriado.getTime().after(calendarData.getTime())) {
					if (ocorrencia.getDtFimFeriado() != null) 
						calendarFeriado.setTime(ocorrencia.getDtFimFeriado());	
					if (!calendarFeriado.getTime().before(calendarData.getTime()))
						return true;
				}
			}
		}
		return false;
	}
	
	public int getQuantidadeOcorrencias(){
		if (getCpOcorrenciaFeriadoSet() != null)
			return getCpOcorrenciaFeriadoSet().size();
		return 1;
	}

	/*
	 * public boolean abrange(Date data) {
	 * 
	 * Date dataFimNaoNula = getDtFimFeriado(); if (getDtFimFeriado() == null)
	 * dataFimNaoNula = getDtIniFeriado();
	 * 
	 * Calendar calInicio = new GregorianCalendar();
	 * calInicio.setTime(getDtIniFeriado());
	 * 
	 * Calendar calFim = new GregorianCalendar();
	 * calFim.setTime(dataFimNaoNula);
	 * 
	 * Calendar calParam = new GregorianCalendar(); calParam.setTime(data);
	 * 
	 * if (calInicio.get(Calendar.YEAR) == calFim.get(Calendar.YEAR)) return
	 * comparaSemAno(calParam, calInicio) <= 0 && comparaSemAno(calParam,
	 * calFim) >= 0; // Exceção(else): recesso else return
	 * comparaSemAno(calParam, calInicio) <= 0 || comparaSemAno(calParam,
	 * calFim) >= 0; }
	 * 
	 * private int comparaSemAno(Calendar cal1, Calendar cal2) { int mes1 =
	 * cal1.get(Calendar.MONTH); int dia1 = cal1.get(Calendar.DAY_OF_MONTH);
	 * 
	 * int mes2 = cal2.get(Calendar.MONTH); int dia2 =
	 * cal2.get(Calendar.DAY_OF_MONTH);
	 * 
	 * if (mes1 > mes2) return -1; else if (mes2 > mes1) return 1; else { if
	 * (dia1 > dia2) return -1; else if (dia2 > dia1) return 1; else return 0; } }
	 */
}
