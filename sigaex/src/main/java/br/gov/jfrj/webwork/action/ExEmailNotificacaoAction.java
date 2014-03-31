
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
import java.util.Map;
import java.util.TreeMap;

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
import br.gov.jfrj.siga.ex.ExMovimentacao;
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
	private DpLotacao lotacaoEmail;	
	private DpPessoa pessoaEmail;	
	private String email;
	private DpLotacaoSelecao lotaSel;
	private DpLotacaoSelecao lotaEmailSel;
	private DpPessoaSelecao pessSel;
	private DpPessoaSelecao pessEmailSel;
	private Long id;
	private List itens;
	private String strBuscarFechadas;
	private Integer tipoDest;
	private Integer tipoEmail;

	
	
	public ExEmailNotificacaoAction() {
	
		lotaSel = new DpLotacaoSelecao();
		lotaEmailSel = new DpLotacaoSelecao();
		pessSel = new DpPessoaSelecao();
		pessEmailSel = new DpPessoaSelecao();
		tipoDest = 1;
		tipoEmail = 1;
		setStrBuscarFechadas("buscarFechadas=false");
	
	}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public Integer getTipoEmail() {
		return tipoEmail;
	}

	public void setTipoEmail(Integer tipoEmail) {
		this.tipoEmail = tipoEmail;
	}

	public Integer getTipoDest() {
		return tipoDest;
	}

	public void setTipoDest(Integer tipoDest) {
		this.tipoDest = tipoDest;
	}

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
	
	public DpLotacao getLotacaoEmail() {
		return lotacaoEmail;
	}

	public void setLotacaoEmail(DpLotacao lotacaoEmail) {
		this.lotacaoEmail = lotacaoEmail;
	}

	public DpPessoa getPessoaEmail() {
		return pessoaEmail;
	}

	public void setPessoaEmail(DpPessoa pessoaEmail) {
		this.pessoaEmail = pessoaEmail;
	}

	public DpLotacaoSelecao getLotaSel() {
		return lotaSel;
	}

	public void setLotaSel(DpLotacaoSelecao lotaSel) {
		this.lotaSel = lotaSel;
	}

	public DpLotacaoSelecao getLotaEmailSel() {
		return lotaEmailSel;
	}

	public void setLotaEmailSel(DpLotacaoSelecao lotaEmailSel) {
		this.lotaEmailSel = lotaEmailSel;
	}

	public DpPessoaSelecao getPessSel() {
		return pessSel;
	}

	public void setPessSel(DpPessoaSelecao pessSel) {
		this.pessSel = pessSel;
	}

	public DpPessoaSelecao getPessEmailSel() {
		return pessEmailSel;
	}

	public void setPessEmailSel(DpPessoaSelecao pessEmailSel) {
		this.pessEmailSel = pessEmailSel;
	}
	
	public List getItens() {
		return itens;
	}

	public void setItens(List itens) {
		this.itens = itens;
	}
	
	
	public String getStrBuscarFechadas() {
		return strBuscarFechadas;
	}

	public void setStrBuscarFechadas(String strBuscarFechadas) {
		this.strBuscarFechadas = strBuscarFechadas;
	}

	public ExEmailNotificacao daoEmail(long id) {
		return dao().consultar(id, ExEmailNotificacao.class, false);
	}

	public String aListar() throws Exception {
		assertAcesso("FE:Ferramentas;EMAIL:Email de Notificação");
		
		setItens(ExDao.getInstance().consultar(new ExEmailNotificacao(), null));		
		return Action.SUCCESS;
	}
	
	public Map<Integer, String> getListaTipoDest() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}
	
	public Map<Integer, String> getListaTipoEmail() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Default");
		map.put(2, "Matrícula");
		map.put(3, "Órgão Integrado");		
		map.put(4, "Email");
		return map;
	}
	
	public String aExcluir() throws Exception {
		assertAcesso("FE:Ferramentas;EMAIL:Email de Notificação");
		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				ExEmailNotificacao email = daoEmail(getId());				
				dao().excluir(email);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão do email", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}
		
	public String aEditar() throws Exception {	
		assertAcesso("FE:Ferramentas;EMAIL:Email de Notificação");
		
		return Action.SUCCESS;
	}
	
	public String aEditarGravar() throws Exception {
		assertAcesso("FE:Ferramentas;EMAIL:Email de Notificação");
		
		ExEmailNotificacao exEmail = new ExEmailNotificacao();	
		exEmail.setDpPessoa(null);
		exEmail.setDpLotacao(null);
		exEmail.setPessoaEmail(null);
		exEmail.setEmail(null);
		
		if (tipoDest == 1) {
			dpPessoa = getPessSel().buscarObjeto();
			exEmail.setDpPessoa(dpPessoa.getPessoaInicial());
		}else { /* destinatário da movimentação é uma lotação */
			dpLotacao = getLotaSel().buscarObjeto();
			exEmail.setDpLotacao(dpLotacao.getLotacaoInicial());			
		}
		
		switch (tipoEmail) {
			case 2 : pessoaEmail = getPessEmailSel().buscarObjeto();
					 exEmail.setPessoaEmail(pessoaEmail.getPessoaInicial());
					 break;
			case 3 : lotacaoEmail = getLotaEmailSel().buscarObjeto();
					 exEmail.setLotacaoEmail(lotacaoEmail.getLotacaoInicial());
					 break;
			case 4 : exEmail.setEmail(email);
		}
		
		
		try {
			dao().iniciarTransacao();
			dao().gravar(exEmail);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		return Action.SUCCESS;
	}
}
