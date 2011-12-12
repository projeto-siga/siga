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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;

import com.opensymphony.xwork.Action;

public class IdentidadeAction extends GiActionSupport {

	private List<CpIdentidade> itens;

	private Long id;

	private DpPessoaSelecao pessoaSel;

	private String dtExpiracao;

	public IdentidadeAction() {
		pessoaSel = new DpPessoaSelecao();
	}

	public CpIdentidade daoId(long id) {
		return dao().consultar(id, CpIdentidade.class, false);
	}

	public String aListar() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = pessoaSel.buscarObjeto();

		if (pes != null) {
			setItens(dao().consultaIdentidades(pes));
		}
		return Action.SUCCESS;
	}

	public String aEditarGravar() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (getId() == null)
			throw new AplicacaoException("Não foi informada id");

		Date dtExpiracao = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			dtExpiracao = df.parse(getDtExpiracao() + " 00:00");
		} catch (final ParseException e) {
		} catch (final NullPointerException e) {
		}

		CpIdentidade ident = daoId(getId());
		Cp.getInstance().getBL().alterarIdentidade(ident, dtExpiracao,
				getIdentidadeCadastrante());
		return Action.SUCCESS;
	}

	public String aCancelar() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (getId() == null)
			throw new AplicacaoException("Não foi informada id");

		CpIdentidade ident = daoId(getId());
		Cp.getInstance().getBL().cancelarIdentidade(ident,
				getIdentidadeCadastrante());
		return Action.SUCCESS;
	}

	public String aBloquear() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (getId() != null) {
			CpIdentidade ident = daoId(getId());
			Cp.getInstance().getBL().bloquearIdentidade(ident,
					getIdentidadeCadastrante(), true);
		} else
			throw new AplicacaoException("Não foi informada id");
		return Action.SUCCESS;
	}

	public String aDesbloquear() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (getId() != null) {
			CpIdentidade ident = daoId(getId());
			Cp.getInstance().getBL().bloquearIdentidade(ident,
					getIdentidadeCadastrante(), false);
		} else
			throw new AplicacaoException("Não foi informada id");
		return Action.SUCCESS;
	}

	public String aBloquearPessoa() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = pessoaSel.buscarObjeto();

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,
					getIdentidadeCadastrante(), true);
		} else
			throw new AplicacaoException("Não foi informada a pessoa");
		return Action.SUCCESS;
	}

	public String aDesbloquearPessoa() throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = pessoaSel.buscarObjeto();

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,
					getIdentidadeCadastrante(), false);
		} else
			throw new AplicacaoException("Não foi informada a pessoa");
		return Action.SUCCESS;
	}

	public String getDtExpiracao() {
		return dtExpiracao;
	}

	public Long getId() {
		return id;
	}

	public List<CpIdentidade> getItens() {
		return itens;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public void setDtExpiracao(String dtExpiracao) {
		this.dtExpiracao = dtExpiracao;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setItens(List<CpIdentidade> itens) {
		this.itens = itens;
	}

	public void setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
	}

}
