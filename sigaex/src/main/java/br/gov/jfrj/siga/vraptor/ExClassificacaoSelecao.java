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
package br.gov.jfrj.siga.vraptor;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecao;

public class ExClassificacaoSelecao extends Selecao<ExClassificacao> {

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		final ExClassificacao oExemplo = new ExClassificacao();
		oExemplo.setSigla(getSigla());

		final ExClassificacao o = ExDao.getInstance().consultarPorSigla(
				oExemplo);

		if (o == null) {
			apagar();
			return false;
		}

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		final ExClassificacao o = ExDao.getInstance().consultar(getId(),
				ExClassificacao.class, false);
		if (o == null)
			return false;

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public String getAcaoBusca() {
		return "/classificacao";
	}

	@Override
	public String getTamanho() {
		return TAMANHO_GRANDE;
	}

	@Override
	public ExClassificacao buscarObjeto() throws AplicacaoException {
		if (getId() == null)
			return null;

		final ExClassificacao o = ExDao.getInstance().consultar(getId(),
				ExClassificacao.class, false);

		return o;
	}
}
