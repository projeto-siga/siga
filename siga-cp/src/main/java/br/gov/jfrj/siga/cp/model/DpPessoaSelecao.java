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
package br.gov.jfrj.siga.cp.model;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class DpPessoaSelecao extends Selecao<DpPessoa> {

	public DpPessoaSelecao() {
		super();
	}

	public DpPessoaSelecao(DpPessoa titular) {
		super(titular);
	}

	@Override
	public DpPessoa buscarObjeto() throws AplicacaoException {
		if (getId() == null)
			return null;

		final DpPessoa o = CpDao.getInstance().consultar(getId(),
				DpPessoa.class, false);

		return o;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		final DpPessoa o = buscarObjeto();
		if (o == null)
			return false;

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		final DpPessoa oExemplo = new DpPessoa();
		oExemplo.setSigla(getSigla());

		final DpPessoa o = CpDao.getInstance().consultarPorSigla(oExemplo);

		if (o == null) {
			apagar();
			return false;
		}

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public String getAcaoBusca() {
		return "/pessoa";
	}

    public void carregarDadosParaView(DpPessoa atendente) {
        if (atendente != null) {
            this.setId(atendente.getId());
            this.setDescricao(atendente.getNomePessoa());
            this.setSigla(atendente.getSigla());
        }
    }
}
