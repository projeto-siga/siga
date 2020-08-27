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
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedNativeQueries({ @NamedNativeQuery(name = "consultarPaginaInicial", query = "SELECT"
		+ " grps.id_marcador, grps.descr_marcador, grps.cont_pessoa, grps.cont_lota from ("
		+ "	SELECT mard.id_marcador, mard.descr_marcador, mard.ord_marcador, "
		+ "	SUM(CASE WHEN id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa,"
		+ "	SUM(CASE WHEN id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota"
		+ "	FROM corporativo.cp_marca marca"
		+ "	JOIN corporativo.cp_marcador mard on marca.id_marcador = mard.id_marcador"
		+ " WHERE(dt_ini_marca IS NULL OR dt_ini_marca < sysdate)"
		+ "		AND(dt_fim_marca IS NULL OR dt_fim_marca > sysdate)"
		+ "		AND((id_pessoa_ini = :idPessoaIni) OR (id_lotacao_ini = :idLotacaoIni))"
		+ "		AND (select id_tipo_forma_doc from siga.ex_forma_documento where id_forma_doc = ("
		+ "				select id_forma_doc from siga.ex_documento where id_doc = ("
		+ "			   		select id_doc from siga.ex_mobil where id_mobil = marca.id_ref ))"
		+ "			   			) = :idTipoForma"
		+ "	   	AND id_tp_marca = 1"
		+ "	GROUP BY mard.id_marcador, mard.descr_marcador, mard.ord_marcador) grps"
		+ "	ORDER BY grps.ord_marcador"),
@NamedNativeQuery(name = "quantidadeDocumentos", query = "SELECT"
		+ "		count(1)"
		+ "	FROM corporativo.cp_marca marca"
		+ "	WHERE(dt_ini_marca IS NULL OR dt_ini_marca < sysdate)"
		+ "		AND(dt_fim_marca IS NULL OR dt_fim_marca > sysdate)"
		+ "		AND(id_pessoa_ini = :idPessoaIni)"
		+ "		AND ("
		+ "				select id_tipo_forma_doc from siga.ex_forma_documento where id_forma_doc = ("
		+ "					select id_forma_doc from siga.ex_documento where id_doc = ("
		+ "						select id_doc from siga.ex_mobil where id_mobil = marca.id_ref"
		+ "					)"
		+ "				)"
		+ "			) in (1, 2)"
		+ "	AND id_tp_marca = 1"
		+ "	and id_marcador not in (9,8,10,11,12 ,13,16, 18, 20 , 21, 22, 24 ,26, 32, 62, 63, 64, 7, 50, 51)")})
public abstract class AbstractCpMarcador extends Objeto implements Serializable {

	private static final long serialVersionUID = 6436403895150961831L;

	@Id
	@Column(name = "ID_MARCADOR", nullable = false)
	private Long idMarcador;

	@Column(name = "DESCR_MARCADOR")
	private String descrMarcador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_MARCADOR", nullable = false)
	private CpTipoMarcador cpTipoMarcador;

	@Column(name = "GRUPO_MARCADOR")
	private Integer grupoMarcador;
	
	@Column(name = "ORD_MARCADOR")
	private Integer ordem;

	public Long getIdMarcador() {
		return idMarcador;
	}

	public void setIdMarcador(Long idMarcador) {
		this.idMarcador = idMarcador;
	}

	public String getDescrMarcador() {
		return descrMarcador;
	}

	public void setDescrMarcador(String descrMarcador) {
		this.descrMarcador = descrMarcador;
	}

	public CpTipoMarcador getCpTipoMarcador() {
		return cpTipoMarcador;
	}

	public void setCpTipoMarcador(CpTipoMarcador cpTipoMarcador) {
		this.cpTipoMarcador = cpTipoMarcador;
	}

	public Integer getGrupoMarcador() {
		return grupoMarcador;
	}

	public void setGrupoMarcador(Integer grupoMarcador) {
		this.grupoMarcador = grupoMarcador;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
