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
package br.gov.jfrj.webwork.action;

import java.util.List;

import com.opensymphony.xwork.Action;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;
import br.gov.jfrj.siga.ex.ExEmailNotificacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.libs.webwork.SigaAnonimoActionSupport;
import br.gov.jfrj.siga.libs.webwork.SigaSelecionavelActionSupport;
import br.gov.jfrj.siga.model.Selecionavel;

public class ExEmailNotificacaoAction extends SigaAnonimoActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098577621835510117L;	
	
	private DpLotacao dpLotacao;	
	private DpPessoa dpPessoa;	
	private String email;	
	private DpLotacao lotacaoAlvo;	
	private DpPessoa pessoaAlvo;
	private DpLotacaoSelecao lotaSel;
	private DpLotacaoSelecao lotaAlvoSel;
	private DpPessoaSelecao pessSel;
	private DpPessoaSelecao pessAlvoSel;
	private Long id;
	private List itens;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}

	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}

	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DpLotacao getLotacaoAlvo() {
		return lotacaoAlvo;
	}

	public void setLotacaoAlvo(DpLotacao lotacaoAlvo) {
		this.lotacaoAlvo = lotacaoAlvo;
	}

	public DpPessoa getPessoaAlvo() {
		return pessoaAlvo;
	}

	public void setPessoaAlvo(DpPessoa pessoaAlvo) {
		this.pessoaAlvo = pessoaAlvo;
	}

	public DpLotacaoSelecao getLotaSel() {
		return lotaSel;
	}

	public void setLotaSel(DpLotacaoSelecao lotaSel) {
		this.lotaSel = lotaSel;
	}

	public DpLotacaoSelecao getLotaAlvoSel() {
		return lotaAlvoSel;
	}

	public void setLotaAlvoSel(DpLotacaoSelecao lotaAlvoSel) {
		this.lotaAlvoSel = lotaAlvoSel;
	}

	public DpPessoaSelecao getPessSel() {
		return pessSel;
	}

	public void setPessSel(DpPessoaSelecao pessSel) {
		this.pessSel = pessSel;
	}

	public DpPessoaSelecao getPessAlvoSel() {
		return pessAlvoSel;
	}

	public void setPessAlvoSel(DpPessoaSelecao pessAlvoSel) {
		this.pessAlvoSel = pessAlvoSel;
	}
	
	public List getItens() {
		return itens;
	}

	public void setItens(List itens) {
		this.itens = itens;
	}

	public String aListar() throws Exception {				
		setItens(ExDao.getInstance().consultar(new ExEmailNotificacao(), null));		
		return Action.SUCCESS;
	}
	
/*	public String aExcluir() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				CpOrgao orgao = daoOrgao(getId());				
				dao().excluir(orgao);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de Orgão", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}
	
	public String aEditar() throws Exception {

		if (getId() != null) {
			CpOrgao orgao = daoOrgao(getId());	
			this.setNmOrgao(orgao.getNmOrgao());
			this.setSiglaOrgao(orgao.getSigla());
			if (orgao.getAtivo() != null && !orgao.getAtivo().isEmpty())
				this.setAtivo(orgao.getAtivo().charAt(0));
			else
				this.setAtivo('N');
			this.setIdOrgaoUsu(orgao.getOrgaoUsuario().getId());
		}
		
		return Action.SUCCESS;
	}
	
	public String aEditarGravar() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		
		if(this.getNmOrgao() == null)
			throw new AplicacaoException("Nome do Órgão Externo não informado");
		
		if(this.getSiglaOrgao() == null)
			throw new AplicacaoException("Sigla do Órgão Externo não informada");
		
		CpOrgao orgao;		
		if (getId() == null)
			orgao = new CpOrgao();
		else
			orgao = daoOrgao(getId());	
		
		orgao.setNmOrgao(this.getNmOrgao());
		orgao.setSigla(this.getSiglaOrgao());
		if (this.getIdOrgaoUsu() != null && this.getIdOrgaoUsu() != 0) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(this.getIdOrgaoUsu(), CpOrgaoUsuario.class, false);	
			orgao.setOrgaoUsuario(orgaoUsuario);
		}else
			orgao.setOrgaoUsuario(null);
		
		orgao.setAtivo(String.valueOf(this.getAtivo()));
		
		try {
			dao().iniciarTransacao();
			dao().gravar(orgao);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		return Action.SUCCESS;
	}
	*/
}
