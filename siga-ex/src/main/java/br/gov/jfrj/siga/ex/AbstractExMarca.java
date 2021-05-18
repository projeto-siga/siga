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
package br.gov.jfrj.siga.ex;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import br.gov.jfrj.siga.dp.CpMarca;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
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
		"               mard.idFinalidade, "+
		"               mard.ordem, "+
		"               mard.idCor, "+
		"               mard.idIcone "+
		"        FROM   ExMarca marca "+
		"               JOIN marca.cpMarcador marcador "+
		"               JOIN CpMarcador mard on (mard.hisIdIni = marcador.hisIdIni and mard.hisAtivo = 1)"+
		"               JOIN marca.exMobil.exDocumento.exFormaDocumento.exTipoFormaDoc tpForma "+
		"        WHERE  ( marca.dtIniMarca IS NULL "+
		"                  OR marca.dtIniMarca < :amanha ) "+
		"               AND ( marca.dtFimMarca IS NULL "+
		"                      OR marca.dtFimMarca > CURRENT_DATE ) "+
		"               AND ( ( marca.dpPessoaIni.idPessoa = :idPessoaIni ) "+
		"                      OR ( marca.dpLotacaoIni.idLotacao = :idLotacaoIni ) ) "+
		"               AND marca.cpTipoMarca.idTpMarca = 1 "+
		"               AND (tpForma.idTipoFormaDoc = :idTipoForma)"+
		"        GROUP  BY mard.idMarcador, "+
		"                  mard.descrMarcador, "+
		"                  mard.idFinalidade, "+
		"                  mard.ordem, "+
		"                  mard.idCor, "+
		"                  mard.idIcone "+
		"ORDER  BY mard.idFinalidade, "+
		"          mard.ordem, "+
		"          mard.descrMarcador")
})
public class AbstractExMarca extends CpMarca {

	@ManyToOne
	@JoinColumn(name = "ID_REF")
	private ExMobil exMobil;

	@ManyToOne
	@JoinColumn(name = "ID_MOV")
	private ExMovimentacao exMovimentacao;

	public ExMobil getExMobil() {
		return exMobil;
	}

	public void setExMobil(ExMobil exMobil) {
		this.exMobil = exMobil;
	}

	public ExMovimentacao getExMovimentacao() {
		return exMovimentacao;
	}

	public void setExMovimentacao(ExMovimentacao exMovimentacao) {
		this.exMovimentacao = exMovimentacao;
	}

}
