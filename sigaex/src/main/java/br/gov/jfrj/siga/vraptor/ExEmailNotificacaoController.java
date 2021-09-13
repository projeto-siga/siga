
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

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExEmailNotificacao;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExEmailNotificacaoController extends SigaController{

	private static final Logger LOGGER = Logger.getLogger(ExEmailNotificacaoController.class);
	private static final String VERIFICADOR_ACESSO = "DOC:Módulo de Documentos;FE:Ferramentas;EMAIL:Email de Notificação";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExEmailNotificacaoController() {
		super();
	}

	@Inject
	public ExEmailNotificacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}

	public ExEmailNotificacao daoEmail(long id) {
		return dao().consultar(id, ExEmailNotificacao.class, false);
	}

	@Get("app/expediente/emailNotificacao/listar")
	public void lista() {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			result.include("itens", ExDao.getInstance().listarTodos(ExEmailNotificacao.class, null));
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		}catch (Exception ex) { 
			LOGGER.error(ex.getMessage(), ex);
		}
	}
	
	@Transacional
	@Get("app/expediente/emailNotificacao/excluir")
	public void excluir(Long id){
		try {
			assertAcesso(VERIFICADOR_ACESSO);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		}catch (Exception ex) { 
			LOGGER.error(ex.getMessage(), ex);
		}
		
		if (id != null) {
			try {
				dao().iniciarTransacao();
				ExEmailNotificacao email = daoEmail(id);				
				dao().excluir(email);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão do email", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");
		result.redirectTo(ExEmailNotificacaoController.class).lista();
	}
		
	@Get("app/expediente/emailNotificacao/editar")
	public void edita(){	
		try {
			assertAcesso(VERIFICADOR_ACESSO);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		}catch (Exception ex) { 
			LOGGER.error(ex.getMessage(), ex);
		}
		
		result.include("listaTipoDest", getListaTipoDest());
		result.include("listaTipoEmail", getListaTipoEmail());
		result.include("strBuscarFechadas", "buscarFechadas=false");
		result.include("tipoDest", 1);
		result.include("tipoEmail", 1);
		result.include("lotaSel", new DpLotacaoSelecao());
		result.include("lotaEmailSel", new DpLotacaoSelecao());
		result.include("pessSel", new DpPessoaSelecao());
		result.include("pessEmailSel", new DpPessoaSelecao());
	}
	
	@Transacional
	@Get("app/expediente/emailNotificacao/editar_gravar")
	public void editarGravar(final DpLotacaoSelecao lotaSel, final DpLotacaoSelecao lotaEmailSel, final DpPessoaSelecao pessSel,
			final DpPessoaSelecao pessEmailSel, final Integer tipoDest, final Integer tipoEmail, final String emailTela){
		try {
			assertAcesso(VERIFICADOR_ACESSO);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) { 
			LOGGER.error(ex.getMessage(), ex);
		}
		
		final ExEmailNotificacao exEmail = new ExEmailNotificacao();	
		
		recuperaDestinatario(lotaSel, pessSel, tipoDest, exEmail);
	
		recuperaInteressado(lotaEmailSel, pessEmailSel, tipoEmail, emailTela,exEmail);
	
		try {
			dao().iniciarTransacao();
			dao().gravar(exEmail);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		result.redirectTo(ExEmailNotificacaoController.class).lista();
	}

	private void recuperaInteressado(final DpLotacaoSelecao lotaEmailSel,
			final DpPessoaSelecao pessEmailSel, final Integer tipoEmail,
			final String emailTela, final ExEmailNotificacao exEmail) {
		
		switch (tipoEmail) {
			case 2 :
				if (pessEmailSel.buscarObjeto() == null) {
					throw new AplicacaoException("Preencha Tipo de interessado na movimentação"); 
				}
				DpPessoa pessoaEmail = pessEmailSel.buscarObjeto();
				exEmail.setPessoaEmail(pessoaEmail.getPessoaInicial());
				break;
			case 3 :
				if (lotaEmailSel.buscarObjeto() == null) {
					throw new AplicacaoException("Preencha Tipo de interessado na movimentação"); 
				}
				DpLotacao lotacaoEmail = lotaEmailSel.buscarObjeto();
				exEmail.setLotacaoEmail(lotacaoEmail.getLotacaoInicial()); 
				break;
			case 4 : exEmail.setEmail(emailTela);
				break;
		}
	}

	private void recuperaDestinatario(final DpLotacaoSelecao lotaSel,
			final DpPessoaSelecao pessSel, final Integer tipoDest,
			final ExEmailNotificacao exEmail) {
		if (pessSel.buscarObjeto() == null && lotaSel.buscarObjeto() == null) {
			throw new AplicacaoException("Preencha Tipo de destinatário da movimentação");
		}
		if (tipoDest == 1) {
			DpPessoa dpPessoa = pessSel.buscarObjeto();
			exEmail.setDpPessoa(dpPessoa.getPessoaInicial());
		} else { /* destinatário da movimentação é uma lotação */
			DpLotacao dpLotacao = lotaSel.buscarObjeto();
			exEmail.setDpLotacao(dpLotacao.getLotacaoInicial());			
		}
	}

	private Map<Integer, String> getListaTipoDest() {
		return TipoResponsavelEnum.getListaMatriculaLotacao();
	}
	
	private Map<Integer, String> getListaTipoEmail() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Default");
		map.put(2, SigaMessages.getMessage("usuario.matricula"));
		map.put(3, SigaMessages.getMessage("usuario.lotacao"));		
		map.put(4, "Email");
		return map;
	}
}

