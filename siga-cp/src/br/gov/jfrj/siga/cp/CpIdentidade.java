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
package br.gov.jfrj.siga.cp;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

public class CpIdentidade extends AbstractCpIdentidade {

	public DpPessoa getPessoaAtual() {
		return CpDao.getInstance().consultarPorIdInicial(
				getDpPessoa().getIdInicial());
	}

	public String getDtExpiracaoDDMMYYYY() {
		if (getDtExpiracaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtExpiracaoIdentidade());
		}
		return "";
	}

	public String getDtCriacaoDDMMYYYY() {
		if (getDtCriacaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtCriacaoIdentidade());
		}
		return "";
	}

	public String getDtCancelamentoDDMMYYYY() {
		if (getDtCriacaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtCancelamentoIdentidade());
		}
		return "";
	}

	public boolean isBloqueada() throws AplicacaoException {
		return Cp.getInstance().getComp().isIdentidadeBloqueada(this);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((CpIdentidade) other)
				.getIdInicial().longValue();
	}

	public void setIdInicial(Long idInicial) {
		setHisIdIni(idInicial);
	}

	public Long getId() {
		return getIdIdentidade();
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}
	public boolean ativaNaData(Date dt) {
		return super.ativoNaData(dt);
	}
}
