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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.libs.webwork;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

public class DpPessoaAction extends
		SigaSelecionavelActionSupport<DpPessoa, DpPessoaDaoFiltro> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6403726630339838734L;

	private DpLotacaoSelecao lotacaoSel;

	private String nome;

	private Long orgaoUsu;

	public String aBuscar() throws Exception {
		DpLotacao lotacaoTitular = getLotaTitular();
		if ( param("postback") == null 
				&& lotacaoTitular != null ) {
			setOrgaoUsu( lotacaoTitular.getIdOrgaoUsuario() );
		}
		return super.aBuscar();
	}

	public Long getOrgaoUsu() {
		return orgaoUsu;
	}

	public void setOrgaoUsu(Long orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	public DpPessoaAction() {
		setSel(new DpPessoa());
		setItemPagina(10);
		lotacaoSel = new DpLotacaoSelecao();
	}

	@Override
	public DpPessoaDaoFiltro createDaoFiltro() throws Exception {
		final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setLotacao(lotacaoSel.buscarObjeto());
		flt.setIdOrgaoUsu(getOrgaoUsu());
		
		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean
				.valueOf(buscarFechadas) : false);
		flt.setSituacaoFuncionalPessoa("");
		
		return flt;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpPessoaDaoFiltro flt)
			throws AplicacaoException {
		Selecionavel sel = null;

		// Acrescenta o sesb e repete a busca
		final String sigla = flt.getSigla();
		flt.setSigla(getTitular().getSesbPessoa() + sigla);
		sel = dao().consultarPorSigla(flt);
		if (sel != null)
			return sel;
		flt.setSigla(sigla);

		// Procura por nome
		flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List pessoas = dao().consultarPorFiltro(flt);
		if (pessoas != null)
			if (pessoas.size() == 1)
				return (DpPessoa) pessoas.get(0);
		return null;
	}

	public void setLotacaoSel(final DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	@Override
	public void setNome(final String nome) {
		this.nome = nome;
	}

}
