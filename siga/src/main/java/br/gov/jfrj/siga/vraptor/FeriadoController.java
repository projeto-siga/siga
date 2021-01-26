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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpAplicacaoFeriado;
import br.gov.jfrj.siga.dp.CpFeriado;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOcorrenciaFeriado;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class FeriadoController extends SigaController {
	

	/**
	 * @deprecated CDI eyes only
	 */
	public FeriadoController() {
		super();
	}

	@Inject
	public FeriadoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}
	
	@Get("/app/feriado/listar")
	public void lista(Integer id) throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		if (id != null){
			CpFeriado feriado = daoFeriado(id);
			result.include("id", feriado.getId());
			result.include("dscFeriado", feriado.getDescricao());
		}			
		result.include("itens", CpDao.getInstance().listarCpFeriadoPorDescricao());
	}
	
	@Transacional
	@Post("/app/feriado/salvar")
	public void aEditarGravar(String dscFeriado, Integer id) throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");

		if (StringUtils.isBlank(dscFeriado)) {
			throw new AplicacaoException("Descrição do feriado não informada");
		}

		CpFeriado feriado = (id == null) 
			? new CpFeriado() 
			: daoFeriado(id);

		feriado.setDscFeriado(dscFeriado);
		
		try {
			dao().iniciarTransacao();
			dao().gravar(feriado);
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		result.redirectTo(this).lista(null);
	}
	
	@Transacional
	@Get("/app/feriado/excluir")
	public void excluirFeriado(Integer id) throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		if (id != null) {
			try {
				dao().iniciarTransacao();
				CpFeriado feriado = daoFeriado(id);				
				dao().excluir(feriado);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de Feriado", 0, e);
			}
		} else {
			throw new AplicacaoException("ID não informada");
		}
		
		result.redirectTo(this).lista(null);
	}
	
	@Transacional
	@Get("/app/feriado/excluir-ocorrencia")
	public void excluirOcorrencia(Long idOcorrencia) throws Exception {
		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		if (idOcorrencia != null) {
			try {
				dao().iniciarTransacao();
				dao().excluir(daoOcorrenciaFeriado(idOcorrencia));				
				dao().commitTransacao();				
			} catch (final Exception e) {
				getCadastrante().getOrgaoUsuario().getId();
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de ocorrencia de feriado", 0, new AplicacaoException(e.getMessage()));
			}
		} else {
			throw new AplicacaoException("ID da ocorrencia não informada");
		}
		result.redirectTo(this).lista(null);
	}
	
	
	
	@Get("/app/feriado/editar-ocorrencia")
	public void editaOcorrencia(Integer id, Long idOcorrencia) throws Exception {
//		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");

		result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		result.include("listaUF", dao().consultarUF());
		result.include("listaAplicacoes", new ArrayList<>());
		
		if (idOcorrencia != null) {
			CpOcorrenciaFeriado ocorrencia = daoOcorrenciaFeriado(idOcorrencia);	
		
			result.include("id", ocorrencia.getCpFeriado().getIdFeriado());
			result.include("idOcorrencia", idOcorrencia);
			result.include("dscFeriado", ocorrencia.getCpFeriado().getDescricao());
			result.include("dtIniFeriado", ocorrencia.getDtRegIniDDMMYYYY());
			result.include("dtFimFeriado", ocorrencia.getDtRegFimDDMMYYYY());
			result.include("listaAplicacoes", getListaAplicacoes(idOcorrencia));
		} else {
			if (id != null) {
				CpFeriado feriado = daoFeriado(id);
				result.include("id", feriado.getId());
				result.include("dscFeriado", feriado.getDescricao());
			} else {
				throw new AplicacaoException("ID não informado");
			}
		}	
		
	}
	
	@Transacional
	@Post("/app/feriado/gravar-ocorrencia")
	public void gravarOcorrencia(Date dtIniFeriado, Date dtFimFeriado, Long idOcorrencia,
			Integer id, DpLotacaoSelecao lotacao_lotacaoSel, Long idOrgaoUsu, Long idLocalidade) throws Exception {
		
//		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");

		DpLotacaoSelecao lotacaoSel = lotacao_lotacaoSel;
		CpAplicacaoFeriado aplicacao = new CpAplicacaoFeriado();
		CpOcorrenciaFeriado ocorrencia = (idOcorrencia == null)
				? new CpOcorrenciaFeriado()
				: daoOcorrenciaFeriado(idOcorrencia);

		if (dtIniFeriado == null) {
			throw new AplicacaoException("Data de início do feriado não informada");
		}

		CpFeriado feriado = daoFeriado(id);
		ocorrencia.setCpFeriado(feriado);
		ocorrencia.setDtIniFeriado(dtIniFeriado);
		ocorrencia.setDtFimFeriado(dtFimFeriado);

		if ((lotacaoSel.getId() != null && lotacaoSel.getId() != 0)
				|| (idOrgaoUsu != null && idOrgaoUsu != 0)
				|| (idLocalidade != null && idLocalidade != 0)) {
			
			HashSet<CpAplicacaoFeriado> apls = new HashSet<CpAplicacaoFeriado>();

			if (lotacaoSel.getId() != null && lotacaoSel.getId() != 0) {
				DpLotacao lotacao = dao().consultar(lotacaoSel.getId(), DpLotacao.class, false);
				aplicacao.setDpLotacao(lotacao);
			}

			if (idOrgaoUsu != null && idOrgaoUsu != 0) {
				CpOrgaoUsuario orgao = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);
				aplicacao.setOrgaoUsu(orgao);
			}

			if (idLocalidade != null && idLocalidade != 0) {
				CpLocalidade localidade = dao().consultar(idLocalidade, CpLocalidade.class, false);
				aplicacao.setLocalidade(localidade);
			}
			
			aplicacao.setCpOcorrenciaFeriado(ocorrencia);
			aplicacao.setFgFeriado(null);

			apls.add(aplicacao);

			ocorrencia.setCpAplicacaoFeriadoSet(apls);
		}

		try {
			dao().iniciarTransacao();
			dao().gravar(ocorrencia);
			dao().gravar(aplicacao);
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		result.redirectTo(this).lista(null);
	}
	
	
	@Get("/app/feriado/localidades")
	public void listaLocalidades(String nmUF) {
		List<CpLocalidade> localidades = new ArrayList<>();

		if (StringUtils.isBlank(nmUF)) {
			localidades = dao().consultarLocalidades();
		} else {
			localidades = dao().consultarLocalidadesPorUF(nmUF);
		}

		result.include("listaLocalidades", localidades);
	}
	
	@Transacional
	@Get("/app/feriado/excluir-aplicacao")
	public void excluirAplicacao(Integer idAplicacao) throws Exception {
//		assertAcesso("FE:Ferramentas;CAD_FERIADO: Cadastrar Feriados");
		
		if (idAplicacao != null) {
			try {
				dao().iniciarTransacao();
				CpAplicacaoFeriado aplicacao = daoAplicacaoFeriado(idAplicacao);
				dao().excluir(aplicacao);
				dao().commitTransacao();
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de ocorrencia de feriado", 0, e);
			}
		} else {
			throw new AplicacaoException("ID da ocorrencia não informada");
		}
	}

	protected CpFeriado daoFeriado(Integer id) {
		return dao().consultar(id, CpFeriado.class, false);
	}
	
	protected CpOcorrenciaFeriado daoOcorrenciaFeriado(long id) {
		return dao().consultar(id, CpOcorrenciaFeriado.class, false);
	}
	
	protected CpAplicacaoFeriado daoAplicacaoFeriado(long id) {
		return dao().consultar(id, CpAplicacaoFeriado.class, false);
	}
	
	protected Date stringToDate(String data) throws Exception {   
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


	protected List<CpAplicacaoFeriado> getListaAplicacoes(Long idOcorrencia) {
		List<CpAplicacaoFeriado> aplicacoes = new ArrayList<CpAplicacaoFeriado>();
		CpAplicacaoFeriado apl = new CpAplicacaoFeriado();
		CpOcorrenciaFeriado ocorrencia = new CpOcorrenciaFeriado();
		ocorrencia = dao().consultar(idOcorrencia, CpOcorrenciaFeriado.class, false);
		apl.setCpOcorrenciaFeriado(ocorrencia);
		aplicacoes = dao().listarAplicacoesFeriado(apl);

		return aplicacoes;
	}
}
