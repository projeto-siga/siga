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
 * Created Mon Nov 14 13:33:07 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.dao.CpDao;

/**
 * A class that represents a row in the 'EX_TIPO_DESPACHO' table. This class may
 * be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_tipo_despacho")
public class ExTipoDespacho extends AbstractExTipoDespacho implements
		Serializable {

	/**
	 * Simple constructor of ExTipoDespacho instances.
	 */
	public ExTipoDespacho(final long idTpDespacho, final String descTpDespacho,
			final String fgAtivo) {
		super();
		super.setDescTpDespacho(descTpDespacho);
		super.setFgAtivo(fgAtivo);
		super.setIdTpDespacho(idTpDespacho);
	}

	/**
	 * Simple constructor of ExTipoDespacho instances.
	 */
	public ExTipoDespacho() {
	}

	/**
	 * Constructor of ExTipoDespacho instances given a simple primary key.
	 * 
	 * @param idTpDespacho
	 */
	public ExTipoDespacho(final java.lang.Long idTpDespacho) {
		super(idTpDespacho);
	}

	public String getIdTpDespachoString() {
		return "a" + String.valueOf(getIdTpDespacho());
	}

}
