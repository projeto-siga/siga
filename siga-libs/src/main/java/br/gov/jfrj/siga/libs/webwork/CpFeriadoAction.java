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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.opensymphony.webwork.components.Set;
import com.opensymphony.xwork.Action;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpAplicacaoFeriado;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;



public class CpFeriadoAction extends SigaAnonimoActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098577621835510117L;	
	
	private Integer id;
	private Long idOcorrencia;
	private String dscFeriado;
	private Date dtIniFeriado;
	private Date dtFimFeriado;	
	private List itens;
	private Long idOrgaoUsu;
	private Long idLocalidade;
	private Long idAplicacao;
	private List localidades;	
	private String nmUF;
	private DpLotacaoSelecao lotacaoSel;
	
	
		
	public CpFeriadoAction() {
		lotacaoSel = new DpLotacaoSelecao();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
	

	public Long getIdOcorrencia() {
		return idOcorrencia;
	}

	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
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
	
	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public void setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	public List getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List localidades) {
		this.localidades = localidades;
	}
	
	public Long getIdLocalidade() {
		return idLocalidade;
	}

	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

		public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}


	public String getNmUF() {
		return nmUF;
	}

	public void setNmUF(String nmUF) {
		this.nmUF = nmUF;
	}

	public Long getIdAplicacao() {
		return idAplicacao;
	}

	public void setIdAplicacao(Long idAplicacao) {
		this.idAplicacao = idAplicacao;
	}

	public CpFeriado daoFeriado(Integer id) {
		return dao().consultar(id, CpFeriado.class, false);
	}
	
	public CpOcorrenciaFeriado daoOcorrenciaFeriado(long id) {
		return dao().consultar(id, CpOcorrenciaFeriado.class, false);
	}
	
	public CpAplicacaoFeriado daoAplicacaoFeriado(long id) {
		return dao().consultar(id, CpAplicacaoFeriado.class, false);
	}

	public String aListar() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		CpFeriado feriado;
		if (this.getId() != null){
			feriado = daoFeriado(getId());
			this.setDscFeriado(feriado.getDescricao());
		}			
		
		setItens(CpDao.getInstance().listarCpFeriadoPorDescricao());		
		return Action.SUCCESS;
	}
	
	public String aEditarGravar() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		if(this.getDscFeriado() == null)
			throw new AplicacaoException("Descrição do feriado não informada");
		
		CpFeriado feriado;		
			
		if (getId() == null)
			feriado = new CpFeriado();
		else
			feriado = daoFeriado(getId());	
		
		feriado.setDscFeriado(this.getDscFeriado());		
		try {
			dao().iniciarTransacao();
			dao().gravar(feriado);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}		
		return Action.SUCCESS;
	}
	
	
	public String aExcluir() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
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
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		if (getIdOcorrencia() != null) {
			try {
				dao().iniciarTransacao();
				CpOcorrenciaFeriado ocorrencia = daoOcorrenciaFeriado(getIdOcorrencia());				
				dao().excluir(ocorrencia);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de ocorrencia de feriado", 0, e);
			}
		} else
			throw new AplicacaoException("ID da ocorrencia não informada");

		return Action.SUCCESS;
	}
	
	public String aExcluirAplicacao() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		if (getIdAplicacao() != null) {
			try {
				dao().iniciarTransacao();
				CpAplicacaoFeriado aplicacao = daoAplicacaoFeriado(getIdAplicacao());				
				dao().excluir(aplicacao);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de ocorrencia de feriado", 0, e);
			}
		} else
			throw new AplicacaoException("ID da ocorrencia não informada");

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
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");

		if (getIdOcorrencia() != null) {
			CpOcorrenciaFeriado ocorrencia = daoOcorrenciaFeriado(getIdOcorrencia());	
			this.setId(ocorrencia.getCpFeriado().getIdFeriado());
			this.setDscFeriado(ocorrencia.getCpFeriado().getDescricao());		
			this.setDtIniFeriado(stringToDate(ocorrencia.getDtRegFimDDMMYYYY()));
			this.setDtFimFeriado(stringToDate(ocorrencia.getDtRegFimDDMMYYYY()));	
		} else {
			if (getId() != null) {
				CpFeriado feriado = daoFeriado(getId());		
				this.dscFeriado = feriado.getDescricao();				
			} else		
				throw new AplicacaoException("ID não informado");
		}	
		
		return Action.SUCCESS;
	}
	
	public String aEditarOcorrenciaGravar() throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		CpOcorrenciaFeriado ocorrencia;
		CpAplicacaoFeriado aplicacao = new CpAplicacaoFeriado();			
		
		if(this.getDtIniFeriado() == null)
			throw new AplicacaoException("Data de início do feriado não informada");
		
		if (getIdOcorrencia() == null)
			 ocorrencia = new CpOcorrenciaFeriado();
		else
			ocorrencia = daoOcorrenciaFeriado(getIdOcorrencia());	
		
		CpFeriado feriado = daoFeriado(getId());	
		ocorrencia.setCpFeriado(feriado);		
		ocorrencia.setDtIniFeriado(this.dtIniFeriado);
		ocorrencia.setDtFimFeriado(this.dtFimFeriado);	
		
		if ((getLotacaoSel().getId() != null && getLotacaoSel().getId()!= 0) ||
				(getIdOrgaoUsu() != null && getIdOrgaoUsu() != 0)	||
				(getIdLocalidade() != null && getIdLocalidade() != 0)) {	
			HashSet<CpAplicacaoFeriado> apls = new HashSet<CpAplicacaoFeriado>(); 
		
			if (getLotacaoSel().getId() != null && getLotacaoSel().getId() != 0) {
				DpLotacao lotacao = dao().consultar(getLotacaoSel().getId(), DpLotacao.class, false);
				aplicacao.setDpLotacao(lotacao);
			}
		
			if(getIdOrgaoUsu() != null && getIdOrgaoUsu() != 0){
				CpOrgaoUsuario orgao = dao().consultar(getIdOrgaoUsu(), CpOrgaoUsuario.class, false);
				aplicacao.setOrgaoUsu(orgao);
			}	
		
			if (getIdLocalidade() != null && getIdLocalidade() != 0) {
				CpLocalidade localidade = dao().consultar(getIdLocalidade(), CpLocalidade.class, false);
				aplicacao.setLocalidade(localidade);		
			}
			aplicacao.setCpOcorrenciaFeriado(ocorrencia);
			aplicacao.setFgFeriado(null);
		
			apls.add(aplicacao);
		
			ocorrencia.setCpAplicacaoFeriadoSet(apls);	
		}
		
		try 
		{
			dao().iniciarTransacao();
			dao().gravar(ocorrencia);
			dao().gravar(aplicacao);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		return Action.SUCCESS;		
	}
	
	public String aListarLocalidades() {

			
		return Action.SUCCESS;
	}
	
	public List<CpLocalidade> getListaLocalidades(){
		
		List<CpLocalidade> localidades = new ArrayList<CpLocalidade>();
		
		if (this.nmUF == null || this.nmUF == ""){
			localidades = dao().consultarLocalidades();
		}else
			localidades = dao().consultarLocalidadesPorUF(this.nmUF);
		
		return localidades;
		
	}	
	
	public List<CpUF> getListaUF(){
		
		List<CpUF> uf = new ArrayList<CpUF>();
		uf = dao().consultarUF();
		
		return uf;
		
	}	
	
	public List<CpAplicacaoFeriado> getListaAplicacoes(){
		List<CpAplicacaoFeriado> aplicacoes = new ArrayList<CpAplicacaoFeriado>();
		CpAplicacaoFeriado apl = new CpAplicacaoFeriado();
		CpOcorrenciaFeriado ocorrencia = new CpOcorrenciaFeriado();
		ocorrencia = dao().consultar(this.idOcorrencia, CpOcorrenciaFeriado.class, false);	
		apl.setCpOcorrenciaFeriado(ocorrencia);		
		aplicacoes = dao().listarAplicacoesFeriado(apl);
		
		return aplicacoes;
		
	}		
}
