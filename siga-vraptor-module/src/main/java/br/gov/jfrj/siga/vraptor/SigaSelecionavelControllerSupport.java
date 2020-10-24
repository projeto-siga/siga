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
package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public abstract class SigaSelecionavelControllerSupport<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel>
		extends SigaController {

	/**
	 * @deprecated CDI eyes only
	 */
	public SigaSelecionavelControllerSupport() {
		super();
	}

	public SigaSelecionavelControllerSupport(HttpServletRequest request,
			Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	private static final long serialVersionUID = 1L;

	private Integer itemPagina;

	private List itens;

	private String nome;

	private String propriedade;

	private Selecionavel sel;

	private String sigla;

	private Integer tamanho;
	
	
	protected void aBuscarJson(String sigla) throws Exception{
		Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
	
		if (sigla == null)
			sigla = "";
		
		aBuscar(sigla, "");
		
		try {
			RetornoJson l = new RetornoJson();
			for (Selecionavel s : (List<Selecionavel>) getItens()) {
				RetornoJsonItem i = new RetornoJsonItem();
				i.key = Long.toString(s.getId());
				i.firstLine = s.getSigla();
				i.secondLine = s.getDescricao();
				l.list.add(i);
			}
			jsonSuccess(l);
		} catch (Exception e) {
			jsonError(e);
		}
	}
	
	protected static class RetornoJson {
		List<RetornoJsonItem> list = new ArrayList<>();
	}

	protected static class RetornoJsonItem {
		String key;
		String firstLine;
		String secondLine;
	}

	protected String aBuscar(String sigla, String postback) throws Exception {
		if (sigla != null) 
			setNome(sigla.toUpperCase());

		int offset = 0;
		int itemPagina = 0;
		if (getP().getOffset() != null) 
			offset = getP().getOffset();

		if (getItemPagina() != null) 
			itemPagina = getItemPagina();

		final DaoFiltroT flt = createDaoFiltro();

		tamanho = dao().consultarQuantidade(flt);
		itens = dao().consultarPorFiltro(flt, offset, itemPagina);
		
		result.include("currentPageNumber", calculaPaginaAtual(offset));
		return "busca";
	}

	protected String aSelecionar(String sigla) {
		final DaoFiltroT flt = createDaoFiltro();
		
		try {
			flt.setSigla(sigla);
		} catch (final Exception ex) {
			sel = null;
		}
		
		try {
			/*
			 * Essa condição é necessário porque o retorno do método getSigla para o ExMobil e DpPessoa
			 * são as siglas completas, ex: JFRJ-MEM-2014/00003 e RJ14723. No caso da lotação o getSigla
			 * somente retorna SESIA. No entanto é necessário que o método selecionar retorne a sigla completa, ex:
			 * RJSESIA, pois esse retorno é o parametro de entrada para o método aExibir, que necessita da sigla completa.
			 * */
/*			if (this instanceof DpLotacaoAction) {
				DpLotacao lotacao = new DpLotacao();
				lotacao = (DpLotacao) dao().consultarPorSigla(flt);
				setSigla(lotacao.getSiglaCompleta());
				sel = (Selecionavel) lotacao;
			}
			else*/
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
	
	protected Integer calculaPaginaAtual(Integer offset) {
		if(getItemPagina() == null)
			setItemPagina(10);
		
		return offset == null ? 1 : (offset / getItemPagina()) + 1;
	}

	protected abstract DaoFiltroT createDaoFiltro();

	protected Integer getItemPagina() {
		return itemPagina;
	}

	protected List getItens() {
		return itens;
	}

	protected String getNome() {
		return nome;
	}

	protected String getPropriedade() {
		return propriedade;
	}

	protected Selecionavel getSel() {
		return sel;
	}

	protected String getSigla() {
		return sigla;
	}

	protected Integer getTamanho() {
		return tamanho;
	}

	protected Selecionavel selecionarPorNome(final DaoFiltroT flt)
			throws AplicacaoException {
		return null;
	}

	protected Selecionavel selecionarVerificar(final Selecionavel sel)
			throws AplicacaoException {
		return sel;
	}

	protected void setItemPagina(final Integer itemPagina) {
		this.itemPagina = itemPagina;
	}

	protected void setItens(final List orgaos) {
		this.itens = orgaos;
	}

	protected void setNome(final String nome) {
		this.nome = nome;
	}

	protected void setPropriedade(final String propriedade) {
		this.propriedade = propriedade;
	}

	protected void setSel(final Selecionavel sel) {
		this.sel = sel;
	}

	protected void setSigla(final String sigla) {
		this.sigla = sigla;
	}

	protected void setTamanho(final Integer tamanho) {
		this.tamanho = tamanho;
	}

}
