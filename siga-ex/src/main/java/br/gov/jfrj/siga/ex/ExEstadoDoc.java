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
 * Created Mon Nov 14 13:28:04 GMT-03:00 2005 by MyEclipse Hibernate Tool.
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
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_ESTADO_DOC' table. This class may be
 * customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Immutable
@Cacheable
@Cache(region = CpDao.CACHE_HOURS, usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "siga.ex_estado_doc")
public class ExEstadoDoc extends AbstractExEstadoDoc implements Serializable,
		Selecionavel {

	final static public long ESTADO_DOC_EM_ELABORACAO = 1;

	final static public long ESTADO_DOC_EM_ANDAMENTO = 2;

	final static public long ESTADO_DOC_EM_TRANSITO = 3;

	final static public long ESTADO_DOC_EXTRAVIADO = 4;

	final static public long ESTADO_DOC_A_ARQUIVAR = 5;

	final static public long ESTADO_DOC_ARQUIVADO_CORRENTE = 6;

	final static public long ESTADO_DOC_A_DESCARTAR = 7;

	final static public long ESTADO_DOC_DESCARTADO = 8;

	final static public long ESTADO_DOC_JUNTADO = 9;

	final static public long ESTADO_DOC_JUNTADO_EXTERNO = 16;

	final static public long ESTADO_CANCELADO = 10;

	final static public long ESTADO_DOC_TRANSFERIDO_A_ORGAO_EXTERNO = 11;

	final static public long ESTADO_DOC_ARQUIVADO_INTERMEDIARIO = 12;

	final static public long ESTADO_DOC_CAIXA_DE_ENTRADA = 14;

	final static public long ESTADO_DOC_ARQUIVADO_PERMANENTE = 13;

	final static public long ESTADO_DOC_PENDENTE_DE_ASSINATURA = 15;

	final static public long ESTADO_DOC_JUNTADO_A_DOCUMENTO_EXTERNO = 16;

	final static public long ESTADO_DOC_A_REMETER_PARA_PUBLICACAO = 17;

	final static public long ESTADO_DOC_REMETIDO_PARA_PUBLICACAO = 18;

	final static public long ESTADO_DOC_A_REMETER_MANUALMENTE = 19;

	final static public long ESTADO_DOC_PUBLICADO = 20;

	final static public long ESTADO_DOC_PUBLICACAO_SOLICITADA = 21;

	final static public long ESTADO_DOC_DISPONIBILIZADO = 22;

	/**
	 * Simple constructor of ExEstadoDoc instances.
	 */
	public ExEstadoDoc() {
	}

	/**
	 * Constructor of ExEstadoDoc instances given a simple primary key.
	 * 
	 * @param idEstadoDoc
	 */
	public ExEstadoDoc(final java.lang.Long idEstadoDoc) {
		super(idEstadoDoc);
	}

	public Long getId() {
		return getIdEstadoDoc();
	}

	public String getIdString() {
		return String.valueOf(getId());
	}

	public String getSigla() {
		return getDescEstadoDoc();
	}

	public void setSigla(final String sigla) {
		// TODO Auto-generated method stub

	}

	public String getDescricao() {
		return getDescEstadoDoc();
	}
}