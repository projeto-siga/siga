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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "consultarPaginaInicial", query = "SELECT mard.idMarcador, "+
		"               mard.descrMarcador, "+
		"               Sum(CASE "+
		"                     WHEN marca.dpPessoaIni.idPessoa = :idPessoaIni THEN 1 "+
		"                     ELSE 0 "+
		"                   END) as cont_pessoa, "+
		"               Sum(CASE "+
		"                     WHEN marca.dpLotacaoIni.idLotacao = :idLotacaoIni THEN 1 "+
		"                     ELSE 0 "+
		"                   END) as cont_lota, "+
		"               mard.cpTipoMarcador, "+
		"               mard.ordem "+
		"        FROM   ExMarca marca "+
		"               JOIN marca.cpMarcador mard "+
		"               JOIN marca.exMobil.exDocumento.exFormaDocumento.exTipoFormaDoc tpForma "+
		"        WHERE  ( marca.dtIniMarca IS NULL "+
		"                  OR marca.dtIniMarca < CURRENT_TIMESTAMP ) "+
		"               AND ( marca.dtFimMarca IS NULL "+
		"                      OR marca.dtFimMarca > CURRENT_TIMESTAMP ) "+
		"               AND ( ( marca.dpPessoaIni.idPessoa = :idPessoaIni ) "+
		"                      OR ( marca.dpLotacaoIni.idLotacao = :idLotacaoIni ) ) "+
		"               AND marca.cpTipoMarca.idTpMarca = 1 "+
		"               AND tpForma.idTipoFormaDoc = :idTipoForma "+
		"        GROUP  BY mard.idMarcador, "+
		"                  mard.descrMarcador, "+
		"                  mard.cpTipoMarcador, "+
		"                  mard.ordem "+
		"ORDER  BY mard.cpTipoMarcador, "+
		"          mard.ordem, "+
		"          mard.descrMarcador"),
	@NamedQuery(name = "quantidadeDocumentos", query = "SELECT count(1)"
			+ "	FROM CpMarca marca"
			+ "	WHERE (marca.dtIniMarca IS NULL OR marca.dtIniMarca < CURRENT_TIMESTAMP)"
			+ "		AND (marca.dtFimMarca IS NULL OR marca.dtFimMarca > CURRENT_TIMESTAMP)"
			+ "		AND marca.dpPessoaIni.idPessoa = :idPessoaIni"
			+ "     AND marca.cpTipoMarca.idTpMarca = 1 "
			+ "	    AND marca.cpMarcador.hisIdIni not in (9,8,10,11,12 ,13,16, 18, 20 , 21, 22, 24 ,26, 32, 62, 63, 64, 7, 50, 51)") })
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
