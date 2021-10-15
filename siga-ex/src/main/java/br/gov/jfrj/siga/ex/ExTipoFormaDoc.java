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
 * Created Mon Nov 14 13:33:08 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_TIPO_MOVIMENTACAO' table. This class
 * may be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_tipo_forma_documento")
public class ExTipoFormaDoc extends AbstractExTipoFormaDoc implements
		Serializable, Selecionavel {

	final static public long TIPO_FORMA_DOC_EXPEDIENTE = 1;

	final static public long TIPO_FORMA_DOC_PROCESSO_ADMINISTRATIVO = 2;

	/**
	 * Simple constructor of ExTipoMovimentacao instances.
	 */
	public ExTipoFormaDoc() {
	}

	public Long getId() {
		return this.getIdTipoFormaDoc();
	}

	public String getSigla() {
		return getDescTipoFormaDoc();
	}

	public void setSigla(final String sigla) {
	}

	public String getDescricao() {
		return getDescTipoFormaDoc();
	}

	public boolean isProcesso() {
		return getIdTipoFormaDoc() != TIPO_FORMA_DOC_EXPEDIENTE;
	}

	public boolean isExpediente() {
		return getIdTipoFormaDoc() == TIPO_FORMA_DOC_EXPEDIENTE;
	}

	/* Add customized code below */

}
