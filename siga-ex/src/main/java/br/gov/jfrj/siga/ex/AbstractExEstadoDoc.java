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
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_ESTADO_DOC table. You can customize
 * the behavior of this class by editing the class, {@link ExEstadoDoc()}.
 */
@MappedSuperclass
@NamedNativeQueries({
		@NamedNativeQuery(name = "consultarPorResponsavel", query = "select * from ("
				+ "		select est.id_Estado_Doc id, est.desc_Estado_Doc desc,"
				+ "		(select count(*) from SIGA.ex_movimentacao movR where (movR.id_resp = :resp || (movR.id_estado_doc = 11 && (movR.cadastrante.idPessoa = :resp || movR.subscritor.idPessoa = :resp))) and movR.dt_Fim_Mov is null and movR.id_estado_doc = est.id_estado_doc) c1 ,"
				+ "		(select count(*) from SIGA.ex_movimentacao movL where (movL.id_lota_resp = :lotaResp || (movL.id_estado_doc = 11 && (movL.lotaCadastrante.idlotacao = :lotaresp || movL.lotaSubscritor.idlotacao = :lotaresp))) and movL.dt_Fim_Mov is null and movL.id_estado_doc = est.id_estado_doc) c2"
				+ "		from SIGA.Ex_Estado_Doc est)"
				+ "		where c1 > 0 or c2 > 0"
				+ "		and id <> 9"),
		@NamedNativeQuery(name = "consultarPorResponsavelSQL", query = "select * from "
				+ "	(select * from "
				+ "		(select est.id_Estado_Doc id, est.desc_Estado_Doc descr,  est.ordem_estado_doc ordem,"
				+ "			(select count(*) from SIGA.ex_movimentacao movR where "
				+ "			(movR.id_resp in (select id_pessoa from corporativo.dp_pessoa where id_pessoa_inicial = :pessoa) "
				+ "			or (movR.id_estado_doc = 11 and movR.id_cadastrante in (select id_pessoa from corporativo.dp_pessoa where id_pessoa_inicial = :pessoa))) "
				+ "			and movR.dt_Fim_Mov is null and movR.id_estado_doc =  est.id_estado_doc) "
				+ "			+ (select count(*) from SIGA.ex_documento docR where "
				+ "			docR.id_cadastrante in (select id_pessoa from corporativo.dp_pessoa where id_pessoa_inicial = :pessoa) "
				+ "			and docR.dt_finalizacao is null and 1 = est.id_estado_doc) c1,"
				+ "			(select count(*) from SIGA.ex_movimentacao movL where"
				+ "			(movL.id_lota_resp in (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = :lotacao) "
				+ "			or (movL.id_estado_doc = 11 and movL.id_lota_cadastrante in (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = :lotacao))) "
				+ "			and movL.dt_Fim_Mov is null and movL.id_estado_doc = est.id_estado_doc) "
				+ "			+ (select count(*) from SIGA.ex_documento docL where "
				+ "			docL.id_lota_cadastrante in (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = :lotacao)"
				+ "			and docL.dt_finalizacao is null and 1 = est.id_estado_doc) c2 "
				+ "		from SIGA.Ex_Estado_Doc est)"
				+ "	where (c1 > 0 or c2 > 0) and id <> 9"
				+ "	union select * from"
				+ "		(select -2 id, 'Transferido(Eletrônico)' descr, 4,"
				+ "			(select count(*) from SIGA.ex_movimentacao movR where "
				+ "			movR.id_subscritor in (select id_pessoa from corporativo.dp_pessoa where id_pessoa_inicial = :pessoa)  "
				+ "			and movR.dt_Fim_Mov is null and movR.id_estado_doc = est.id_estado_doc)  c1, "
				+ "			(select count(*) from SIGA.ex_movimentacao movL where "
				+ "			movL.id_lota_subscritor in (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = :lotacao) "
				+ "			and movL.dt_Fim_Mov is null and movL.id_estado_doc = est.id_estado_doc) c2 "
				+ "		from SIGA.Ex_Estado_Doc est where est.id_Estado_Doc=3 )"
				+ "	where (c1 > 0 or c2 > 0)"
				+ "	union select * from"
				+ "		(select -1 id, 'Transferido' descr, 3,"
				+ "			(select count(*) from SIGA.ex_movimentacao movR where "
				+ "			movR.id_subscritor in (select id_pessoa from corporativo.dp_pessoa where id_pessoa_inicial = :pessoa)  "
				+ "			and movR.dt_Fim_Mov is null and movR.id_estado_doc = est.id_estado_doc)  c1, "
				+ "			(select count(*) from SIGA.ex_movimentacao movL where "
				+ "			movL.id_lota_subscritor in (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = :lotacao) "
				+ "			and movL.dt_Fim_Mov is null and movL.id_estado_doc = est.id_estado_doc) c2 "
				+ "		from SIGA.Ex_Estado_Doc est where est.id_Estado_Doc=3 )"
				+ "	where (c1 > 0 or c2 > 0))" + "order by ordem") })
public abstract class AbstractExEstadoDoc extends Objeto implements
		Serializable {
	/** The composite primary key value. */
	@Id
	@SequenceGenerator(sequenceName = "EX_ESTADO_DOC_SEQ", name = "EX_ESTADO_DOC_SEQ")
	@GeneratedValue(generator = "EX_ESTADO_DOC_SEQ")
	@Column(name = "ID_ESTADO_DOC", unique = true, nullable = false)
	private java.lang.Long idEstadoDoc;

	/** The value of the simple descEstadoDoc property. */
	@Column(name = "DESC_ESTADO_DOC", nullable = false, length = 128)
	private java.lang.String descEstadoDoc;

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "exEstadoDoc")
	// private Set<ExMovimentacao> exMovimentacaoSet;

	@Column(name = "ORDEM_ESTADO_DOC")
	private Integer ordemEstadoDoc;

	/**
	 * Simple constructor of AbstractExEstadoDoc instances.
	 */
	public AbstractExEstadoDoc() {
	}

	/**
	 * Constructor of AbstractExEstadoDoc instances given a simple primary key.
	 * 
	 * @param idEstadoDoc
	 */
	public AbstractExEstadoDoc(final java.lang.Long idEstadoDoc) {
		this.setIdEstadoDoc(idEstadoDoc);
	}

	/**
	 * Implementation of the equals comparison on the basis of equality of the
	 * primary key values.
	 * 
	 * @param rhs
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof ExEstadoDoc))
			return false;
		final ExEstadoDoc that = (ExEstadoDoc) rhs;

		if ((this.getIdEstadoDoc() == null ? that.getIdEstadoDoc() == null
				: this.getIdEstadoDoc().equals(that.getIdEstadoDoc())))
			return true;
		return false;

	}

	/**
	 * Return the value of the DESC_ESTADO_DOC column.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDescEstadoDoc() {
		return this.descEstadoDoc;
	}

	/**
	 * Return the simple primary key value that identifies this object.
	 * 
	 * @return java.lang.Long
	 */
	public java.lang.Long getIdEstadoDoc() {
		return idEstadoDoc;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		final int idValue = this.getIdEstadoDoc() == null ? 0 : this
				.getIdEstadoDoc().hashCode();
		return result * 37 + idValue;
	}

	/**
	 * Set the value of the DESC_ESTADO_DOC column.
	 * 
	 * @param descEstadoDoc
	 */
	public void setDescEstadoDoc(final java.lang.String descEstadoDoc) {
		this.descEstadoDoc = descEstadoDoc;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idEstadoDoc
	 */
	public void setIdEstadoDoc(final java.lang.Long idEstadoDoc) {
		this.idEstadoDoc = idEstadoDoc;
	}
}
