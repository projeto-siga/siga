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
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecao;

public class DpLotacaoSelecao extends Selecao<DpLotacao> {

	public DpLotacaoSelecao() {
		super();
	}

	public DpLotacaoSelecao(DpLotacao titular) {
		super(titular);
	}

	@Override
	public DpLotacao buscarObjeto() throws AplicacaoException {
		if (getId() == null)
			return null;

		final DpLotacao o = CpDao.getInstance().consultar(getId(),
				DpLotacao.class, false);

		return o;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		final DpLotacao o = CpDao.getInstance().consultar(getId(),
				DpLotacao.class, false);
		if (o == null)
			return false;

		buscarPorObjeto(o);
		return true;
	}

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		if (buscarPorSiglaCompleta())
			return true;

		final DpLotacao oExemplo = new DpLotacao();
		oExemplo.setSigla(getSigla());

		final DpLotacao o = CpDao.getInstance().consultarPorSigla(oExemplo);

		if (o == null) {
			apagar();
			return false;
		}

		buscarPorObjeto(o);
		return true;
	}

	public boolean buscarPorSiglaCompleta() throws AplicacaoException {

		CpOrgaoUsuario ouFiltro = new CpOrgaoUsuario();
		ouFiltro.setSigla(getSigla().substring(0, 2));
		CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(ouFiltro);

		final DpLotacao oExemplo = new DpLotacao();
		oExemplo.setOrgaoUsuario(ou);
		oExemplo.setSigla(getSigla().substring(2));

		try{
			final DpLotacao o = CpDao.getInstance().consultarPorSigla(oExemplo);

			if (o == null) {
				return false;
			}

			buscarPorObjeto(o);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public String getAcaoBusca() {
		return "/lotacao";
	}

    public void carregarDadosParaView(DpLotacao lotaAtendente) {
        if (lotaAtendente != null) {
            this.setId(lotaAtendente.getId());
            this.setDescricao(lotaAtendente.getNomeLotacao());
            this.setSigla(lotaAtendente.getSigla());
        }
    }
}
