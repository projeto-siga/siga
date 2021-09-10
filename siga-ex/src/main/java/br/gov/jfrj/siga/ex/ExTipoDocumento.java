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
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_TIPO_DOCUMENTO' table. This class
 * may be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_tipo_documento")
public class ExTipoDocumento extends AbstractExTipoDocumento implements
		Serializable, Selecionavel {

	final static public long TIPO_DOCUMENTO_INTERNO = 1;
	final static public long TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO = 2;
	final static public long TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO = 3;
	final static public long TIPO_DOCUMENTO_EXTERNO_CAPTURADO = 4;
	final static public long TIPO_DOCUMENTO_INTERNO_CAPTURADO = 5;	
	
	/**
	 * Simple constructor of ExTipoDocumento instances.
	 */
	public ExTipoDocumento() {
	}

	/**
	 * Constructor of ExTipoDocumento instances given a simple primary key.
	 * 
	 * @param idTpDoc
	 */
	public ExTipoDocumento(final java.lang.Long idTpDoc) {
		super(idTpDoc);
	}

	public Long getId() {
		return getIdTpDoc();
	}

	public String getSigla() {
		return getDescrTipoDocumento();
	}

	public void setSigla(final String sigla) {
		setDescrTipoDocumento(sigla);
	}

	public String getDescricao() {
		return getDescrTipoDocumento();
	}
	
	public String getDescricaoSimples() {
		String descricao = "";
		
		switch (getIdTpDoc().intValue()) {
		case 1:		
			descricao = "Interno Produzido";
			break;
		case 2:
			descricao = "Interno Importado";
			break;
		case 3:
			descricao = "Externo";
			break;
		case 4:
			descricao =  "Externo Capturado";
			break;
		case 5:
			descricao = "Interno Capturado";
			break;
		}
		
		return descricao;
	}

}
