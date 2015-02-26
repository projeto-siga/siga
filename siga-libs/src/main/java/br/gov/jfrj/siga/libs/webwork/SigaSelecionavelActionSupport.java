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
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public abstract class SigaSelecionavelActionSupport<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel>
		extends SigaActionSupport {

	private static final long serialVersionUID = 1L;

	private Integer itemPagina;

	private List itens;

	private String nome;

	private String propriedade;

	private Selecionavel sel;

	private String sigla;

	private Integer tamanho;

	public String aBuscar() throws Exception {
		if (param("postback") == null) {

		}
		if (param("sigla") != null) {
			setNome(param("sigla").toUpperCase());
		}

		int offset = 0;
		int itemPagina = 0;
		if (getP().getOffset() != null) {
			offset = getP().getOffset();
		}
		if (getItemPagina() != null) {
			itemPagina = getItemPagina();
		}

		final DaoFiltroT flt = createDaoFiltro();

		tamanho = dao().consultarQuantidade(flt);

		itens = dao().consultarPorFiltro(flt, offset, itemPagina);

		return "busca";
	}

	public String aSelecionar() throws Exception {
		final DaoFiltroT flt = createDaoFiltro();
		
		try {
			flt.setSigla(param("sigla"));
			sel = dao().consultarPorSigla(flt);
		} catch (final Exception ex) {
			sel = null;
		}
		
		
		if (sel == null) {
			try {
				sel = selecionarPorNome(flt);
			} catch (final Exception ex) {
				sel = null;
			}
		}
				
		if (sel != null)
			sel = selecionarVerificar(sel);

		if (sel == null)
			return "ajax_vazio";

		return "ajax_retorno";
	}

	abstract public DaoFiltroT createDaoFiltro() throws Exception;

	public Integer getItemPagina() {
		return itemPagina;
	}

	public List getItens() {
		return itens;
	}

	public String getNome() {
		return nome;
	}

	public String getPropriedade() {
		return propriedade;
	}

	public Selecionavel getSel() {
		return sel;
	}

	public String getSigla() {
		return sigla;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public Selecionavel selecionarPorNome(final DaoFiltroT flt)
			throws AplicacaoException {
		return null;
	}

	public Selecionavel selecionarVerificar(final Selecionavel sel)
			throws AplicacaoException {
		return sel;
	}

	public void setItemPagina(final Integer itemPagina) {
		this.itemPagina = itemPagina;
	}

	public void setItens(final List orgaos) {
		this.itens = orgaos;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public void setPropriedade(final String propriedade) {
		this.propriedade = propriedade;
	}

	public void setSel(final Selecionavel sel) {
		this.sel = sel;
	}

	public void setSigla(final String sigla) {
		this.sigla = sigla;
	}

	public void setTamanho(final Integer tamanho) {
		this.tamanho = tamanho;
	}

}
