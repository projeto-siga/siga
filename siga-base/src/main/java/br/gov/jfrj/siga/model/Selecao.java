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
package br.gov.jfrj.siga.model;

import br.gov.jfrj.siga.base.AplicacaoException;

/**
 * Classe abstrata que provê suporte à caixa de seleção da interface do usuário
 * do SIGA.<br/>
 * 
 * 
 * Procedimentos para usar a tag siga:selecao
 * 
 * 1) Na action da página, crie um atributo de seleção com o final "Sel" (TEM
 * QUE TER SEL NO FINAL)
 * 
 * obs: Os tipos de seleção estão em:
 * 
 * projeto siga-libs: br.gov.jfrj.siga.libs.webwork
 * 
 * Ex: <code>
public class ExRelatorioAction extends SigaActionSupport {

	...
	private DpLotacaoSelecao lotacaoDestinatarioSel;
	...
	
	public get/set()...
	
}
</code> 2) Insira a tag em sua página, SEM O FINAL "SEL"
 * 
 * Ex: <code>
<siga:selecao propriedade="lotacaoDestinatario" tema="simple" />
</code> 3) No exemplo acima, para pegar o valor selecionado, use <code>
    getRequest().getParameter("lotacaoDestinatarioSel.sigla") 
</code>
 * 4) Inclua o postback na página para selecionar o órgão automaticamante <code>
<ww:hidden name="postback" value="1" />
</code>
 * 
 * @author kpf
 * 
 * @param <T>
 */
public abstract class Selecao<T extends Selecionavel> {
	private Integer buscar;

	private String descricao;

	private Long id;

	private String sigla;

	final static public String TAMANHO_MEDIO = "medio";

	final static public String TAMANHO_GRANDE = "grande";

	public abstract String getAcaoBusca();

	public abstract boolean buscarPorId() throws AplicacaoException;

	public abstract T buscarObjeto() throws AplicacaoException;
	
	public Selecao() {
		super();
	}

	public Selecao(T o) {
		super();
		buscarPorObjeto(o);
	}

	public T getObjeto() throws AplicacaoException {
		return buscarObjeto();
	}

	public void buscarPorObjeto(final T o) throws AplicacaoException {
		apagar();
		if (o != null) {
			setId(o.getId());
			setSigla(o.getSigla());
			setDescricao(o.getDescricao());
		}
	}

	public abstract boolean buscarPorSigla() throws AplicacaoException;

	public String getDescricao() {
		if (descricao == null)
			return null;
		if (descricao.length() == 0)
			return null;
		return descricao;
	}

	public Long getId() {
		if (id == null)
			return null;
		if (id == 0)
			return null;
		return id;
	}

	public String getTamanho() {
		return TAMANHO_MEDIO;
	}

	public String getSigla() {
		if (sigla == null)
			return null;
		if (sigla.length() == 0)
			return null;
		return sigla;
	}

	public void setDescricao(final String desc) {
		this.descricao = desc;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setSigla(final String sigla) {
		this.sigla = sigla;
	}

	public boolean buscar() throws AplicacaoException {
		if (getId() != null && (getSigla() == null || getDescricao() == null))
			return buscarPorId();
		if (getSigla() != null && (getId() == null || getDescricao() == null))
			return buscarPorSigla();
		if (buscar == null)
			return false;
		apagar();
		return false;
	}

	public void apagar() {
		setId(null);
		setSigla(null);
		setDescricao(null);
		setBuscar(null);
	}

	public Integer getBuscar() {
		return buscar;
	}

	public void setBuscar(final Integer buscar) {
		this.buscar = buscar;
	}

	public boolean empty() {
		return id == null;
	}
}
