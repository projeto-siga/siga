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
/**
 * 
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import br.gov.jfrj.siga.model.Objeto;

/**
 * A class that represents a row in the EX_MOBIL table. You can customize the
 * behavior of this class by editing the class, {@link ExMobil()}.
 */
@MappedSuperclass
@NamedNativeQueries({ @NamedNativeQuery(name = "consultarMobilNoPeriodoLento", query = "select id_mobil " + "from"
		+ "    SIGA.ex_movimentacao m," + "    (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini ="
		+ "(select id_lotacao_ini  from corporativo.dp_lotacao where id_lotacao = :idLotacao)) " + "l " + "where "
		+ "    (m.dt_ini_mov >= to_date(:dataInicial,'dd/mm/yyyy') and "
		+ "m.dt_ini_mov <= to_date(:dataFinal,'dd/mm/yyyy'))" + "    and m.id_lota_resp = l.id_lotacao "
		+ "union select m3.id_mobil " + "from " + "    SIGA.ex_movimentacao m3,"
		+ "    (select id_lotacao from corporativo.dp_lotacao where id_lotacao_ini = "
		+ "(select id_lotacao_ini  from corporativo.dp_lotacao where id_lotacao = :idLotacao)) " + "L2, "
		+ "    (select max(id_mov) id_mov" + "    from" + "        SIGA.ex_movimentacao m2," + "        "
		+ "        (select id_mobil" + "        from" + "            SIGA.ex_movimentacao m,"
		+ "            (select id_lotacao from corporativo.dp_lotacao where "
		+ "id_lotacao_ini = (select id_lotacao_ini  from corporativo.dp_lotacao where " + "id_lotacao = :idLotacao)) L "
		+ "        where " + "            m.id_lota_resp = l.id_lotacao"
		+ "            and (M.DT_INI_MOV <= to_date(:dataInicial,'DD/MM/YYYY'))) mob" + "    where"
		+ "        m2.id_mobil = mob.id_mobil" + "        and (M2.DT_INI_MOV <= to_date(:dataInicial,'DD/MM/YYYY'))"
		+ "    group by m2.id_mobil) max_mov " + "where" + "    m3.id_mov = max_mov.id_mov"
		+ "    and m3.id_lota_resp = l2.id_lotacao"),
		@NamedNativeQuery(name = "consultarMobilNoPeriodo", query = "select " + "	("
				+ "		(select sigla_orgao_usu from corporativo.cp_orgao_usuario where id_orgao_usu = doc.id_orgao_usu)"
				+ "		|| '-' || "
				+ "		(select sigla_forma_doc from siga.ex_forma_documento where id_forma_doc = doc.id_forma_doc)"
				+ "		|| '-' ||" + "		doc.ano_emissao" + "		|| '/' ||" + "		doc.num_Expediente"
				+ "		|| '-' ||" + "		(case when (mob.id_Tipo_Mobil = 4) "
				+ "			then (to_char(mob.num_Sequencia)) else (chr(mob.num_Sequencia+64)) end)" + "	)," + "	("
				+ "		:URL" + "		||"
				+ "		(select sigla_orgao_usu from corporativo.cp_orgao_usuario where id_orgao_usu = doc.id_orgao_usu)"
				+ "		|| '-' || "
				+ "		(select sigla_forma_doc from siga.ex_forma_documento where id_forma_doc = doc.id_forma_doc)"
				+ "		|| '-' ||" + "		doc.ano_emissao" + "		|| '/' ||" + "		doc.num_Expediente"
				+ "		|| '-' ||" + "		(case when (mob.id_Tipo_Mobil = 4) "
				+ "			then (to_char(mob.num_Sequencia)) else (chr(mob.num_Sequencia+64)) end)" + "	),"
				+ "	(case when (doc.id_Nivel_Acesso <> 1 and doc.id_Nivel_Acesso <> 6) "
				+ "		then 'CONFIDENCIAL' else doc.descr_Documento end),"
				+ "	(select sigla_lotacao from corporativo.dp_lotacao where id_lotacao = doc.id_lota_cadastrante) "
				+ "from siga.ex_mobil mob " + "	inner join siga.ex_documento doc on mob.id_doc = doc.id_doc "
				+ "where mob.id_mobil in (" + "	select id_mobil" + "	from" + "		SIGA.ex_movimentacao m,"
				+ "		(" + "			select id_lotacao " + "			from corporativo.dp_lotacao "
				+ "			where id_lotacao_ini =("
				+ "				select id_lotacao_ini  from corporativo.dp_lotacao where id_lotacao = :idLotacao"
				+ "			)" + "		) l" + "	where(" + "		m.dt_ini_mov >= to_date(:dataInicial,'dd/mm/yyyy') "
				+ "		and m.dt_ini_mov <= to_date(:dataFinal,'dd/mm/yyyy')" + "	) and m.id_lota_resp = l.id_lotacao"
				+ "	" + "	union select m3.id_mobil" + "	from" + "		SIGA.ex_movimentacao m3," + "		("
				+ "			select id_lotacao " + "			from corporativo.dp_lotacao "
				+ "			where id_lotacao_ini =(" + "				select id_lotacao_ini  "
				+ "				from corporativo.dp_lotacao " + "				where id_lotacao = :idLotacao)"
				+ "		) L2," + "		(" + "			select max(id_mov) id_mov" + "			from"
				+ "			SIGA.ex_movimentacao m2," + "			(" + "				select id_mobil"
				+ "				from" + "				SIGA.ex_movimentacao m," + "				("
				+ "				select id_lotacao " + "					from corporativo.dp_lotacao "
				+ "					where id_lotacao_ini = (" + "						select id_lotacao_ini  "
				+ "						from corporativo.dp_lotacao "
				+ "						where id_lotacao = :idLotacao)" + "					) L"
				+ "				where m.id_lota_resp = l.id_lotacao"
				+ "				and (M.DT_INI_MOV <= to_date(:dataInicial,'DD/MM/YYYY'))" + "			) mob"
				+ "			where m2.id_mobil = mob.id_mobil"
				+ "			and (M2.DT_INI_MOV <= to_date(:dataInicial,'DD/MM/YYYY'))"
				+ "			group by m2.id_mobil" + "		) max_mov" + "	where" + "		m3.id_mov = max_mov.id_mov"
				+ "		and m3.id_lota_resp = l2.id_lotacao" + ")") })
public abstract class AbstractExMobil extends Objeto implements Serializable {
	@Id
	@SequenceGenerator(sequenceName = "EX_MOBIL_SEQ", name = "EX_MOBIL_SEQ")
	@GeneratedValue(generator = "EX_MOBIL_SEQ")
	@Column(name = "ID_MOBIL", unique = true, nullable = false)
	private java.lang.Long idMobil;

	@Column(name = "DNM_ULTIMA_ANOTACAO", length = 400)
	private java.lang.String dnmUltimaAnotacao;

	@Column(name = "DNM_NUM_PRIMEIRA_PAGINA")
	private Integer dnmNumPrimeiraPagina;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOC", nullable = false)
	private ExDocumento exDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_MOBIL", nullable = false)
	private ExTipoMobil exTipoMobil;

	@Column(name = "NUM_SEQUENCIA", nullable = false)
	private java.lang.Integer numSequencia;

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exMobil")
	@Sort(type = SortType.NATURAL)
	private SortedSet<ExMovimentacao> exMovimentacaoSet = new TreeSet<>();

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exMobilRef")
	@Sort(type = SortType.NATURAL)
	private SortedSet<ExMovimentacao> exMovimentacaoReferenciaSet;

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exMobilPai")
	private java.util.Set<ExDocumento> exDocumentoFilhoSet;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exMobil")
	@Sort(type = SortType.NATURAL)
	private SortedSet<ExMarca> exMarcaSet;

	public java.lang.Long getIdMobil() {
		return idMobil;
	}

	public void setIdMobil(java.lang.Long idMobil) {
		idMobil = idMobil;
	}

	public ExDocumento getExDocumento() {
		return exDocumento;
	}

	public void setExDocumento(ExDocumento exDocumento) {
		this.exDocumento = exDocumento;
	}

	public ExTipoMobil getExTipoMobil() {
		return exTipoMobil;
	}

	public void setExTipoMobil(ExTipoMobil exTipoMobil) {
		this.exTipoMobil = exTipoMobil;
	}

	public java.lang.Integer getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(java.lang.Integer numSequencia) {
		this.numSequencia = numSequencia;
	}

	public SortedSet<ExMovimentacao> getExMovimentacaoSet() {
		return exMovimentacaoSet;
	}

	public void setExMovimentacaoSet(SortedSet<ExMovimentacao> exMovimentacaoSet) {
		this.exMovimentacaoSet = exMovimentacaoSet;
	}

	public SortedSet<ExMovimentacao> getExMovimentacaoReferenciaSet() {
		return exMovimentacaoReferenciaSet;
	}

	public void setExMovimentacaoReferenciaSet(SortedSet<ExMovimentacao> exMovimentacaoReferenciaSet) {
		this.exMovimentacaoReferenciaSet = exMovimentacaoReferenciaSet;
	}

	public java.util.Set<ExDocumento> getExDocumentoFilhoSet() {
		return exDocumentoFilhoSet;
	}

	public void setExDocumentoFilhoSet(java.util.Set<ExDocumento> exDocumentoFilhoSet) {
		this.exDocumentoFilhoSet = exDocumentoFilhoSet;
	}

	public java.util.SortedSet<ExMarca> getExMarcaSet() {
		return exMarcaSet;
	}

	public void setExMarcaSet(java.util.SortedSet<ExMarca> exMarcaSet) {
		this.exMarcaSet = exMarcaSet;
	}

	public java.lang.String getDnmUltimaAnotacao() {
		return dnmUltimaAnotacao;
	}

	public void setDnmUltimaAnotacao(java.lang.String dnmUltimaAnotacao) {
		this.dnmUltimaAnotacao = dnmUltimaAnotacao;
	}

	public Integer getDnmNumPrimeiraPagina() {
		return dnmNumPrimeiraPagina;
	}

	public void setDnmNumPrimeiraPagina(Integer dnmNumPrimeiraPagina) {
		this.dnmNumPrimeiraPagina = dnmNumPrimeiraPagina;
	}

}
