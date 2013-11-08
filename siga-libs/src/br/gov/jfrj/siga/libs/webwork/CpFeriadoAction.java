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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork.Action;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;


public class CpFeriadoAction extends SigaAnonimoActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098577621835510117L;	
	
	private Long id;
	private String dscFeriado;
	private Date dtIniFeriado;
	private Date dtFimFeriado;
	
	private List itens;
	
	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDscFeriado() {
		return dscFeriado;
	}

	public void setDscFeriado(String dscFeriado) {
		this.dscFeriado = dscFeriado;	
	}
	
	
	public List getItens() {
		return itens;
	}

	public void setItens(List feriados) {
		this.itens = feriados;
	}	

	public Date getDtIniFeriado() {
		return dtIniFeriado;
	}

	public void setDtIniFeriado(Date dtIniFeriado) {
		this.dtIniFeriado = dtIniFeriado;
	}

	public Date getDtFimFeriado() {
		return dtFimFeriado;
	}

	public void setDtFimFeriado(Date dtFimFeriado) {
		this.dtFimFeriado = dtFimFeriado;
	}

	public String aListar() throws Exception {
		
		setItens(CpDao.getInstance().listarFeriados());
		
		return Action.SUCCESS;
	}
	
	public CpFeriado daoFeriado(long id) {
		return dao().consultar(id, CpFeriado.class, false);
	}
	
	public CpOcorrenciaFeriado daoOcorrenciaFeriado(long id) {
		return dao().consultar(id, CpOcorrenciaFeriado.class, false);
	}


	
	public String aExcluir() throws Exception {
//		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				CpFeriado feriado = daoFeriado(getId());				
				dao().excluir(feriado);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de Feriado", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}
	
	public String aExcluirOcorrencia() throws Exception {
//		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				CpOcorrenciaFeriado ocorrencia = daoOcorrenciaFeriado(getId());				
				dao().excluir(ocorrencia);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de ocorrencia de feriado", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}
	
	public Date stringToDate(String data) throws Exception {   
        if (data == null || data.equals(""))  
            return null;            
        Date date = null;  
        try {  
            DateFormat strDate = new SimpleDateFormat("dd/MM/yyyy");  
            date = (java.util.Date)strDate.parse(data);  
        } catch (Exception e) {              
            throw e;  
        }  
        return date;  
    }  
	
	public String aEditarOcorrencia() throws Exception {

		if (getId() != null) {
			CpOcorrenciaFeriado ocorrencia = daoOcorrenciaFeriado(getId());	
			this.setDscFeriado(ocorrencia.getCpFeriado().getDescricao());		
			this.setDtIniFeriado(stringToDate(ocorrencia.getDtRegIniDDMMYY()));
			this.setDtFimFeriado(stringToDate(ocorrencia.getDtRegFimDDMMYY()));			
		}
		
		return Action.SUCCESS;
	}
	
	public String aEditarOcorrenciaGravar() throws Exception {
//		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		
		if(this.getDtIniFeriado() == null)
			throw new AplicacaoException("Data de início do feriado não informada");
		
		if (getId() == null)
			CpOcorrenciaFeriado ocorrencia = new CpOcorrenciaFeriado();
		else
			ocorrencia = daoOcorrenciaFeriado(getId());	
		
		ocorrencia.setNmOrgao(this.getNmOrgao());
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
		return Action.SUCCESS;
	}
	
	
	
/*	
	
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
