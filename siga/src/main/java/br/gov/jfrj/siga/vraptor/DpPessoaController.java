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

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Resource
public class DpPessoaController extends SigaSelecionavelControllerSupport<DpPessoa, DpPessoaDaoFiltro> {
	
	private Long orgaoUsu;
	private DpLotacaoSelecao lotacaoSel;
	
	public DpPessoaController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		
		setSel(new DpPessoa());
		setItemPagina(10);
	}

	@Get
	@Post
	@Path({"/app/pessoa/buscar","/app/cosignatario/buscar", "/pessoa/buscar.action", "/cosignatario/buscar.action"})
	public void buscar(String sigla, String postback, Integer offset, Long idOrgaoUsu, DpLotacaoSelecao lotacaoSel) throws Exception {
		final DpLotacao lotacaoTitular = getLotaTitular();
		if ( postback == null && lotacaoTitular != null ) {
			orgaoUsu = lotacaoTitular.getIdOrgaoUsuario();
		}else{
			orgaoUsu = idOrgaoUsu;
		}
		if (lotacaoSel != null && lotacaoSel.getId() != null && lotacaoSel.getId() > 0){
			this.lotacaoSel = lotacaoSel;
		}
		this.getP().setOffset(offset);
		
		if (sigla == null){
			sigla = "";
		}
		
		super.aBuscar(sigla, postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu",getOrgaosUsu());
		result.include("lotacaoSel",lotacaoSel == null ? new DpLotacaoSelecao() : lotacaoSel);
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("sigla",sigla);
		result.include("postbak",postback);
		result.include("offset",offset);
	}
	
	@Get("/app/pessoa/exibir")
	public void exibi(String sigla) {
		 if(sigla != null) {
			 result.include("pessoa", dao().getPessoaPorPrincipal(sigla));
		 }
	 }

	@Override
	public DpPessoaDaoFiltro createDaoFiltro() {
		final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		if (lotacaoSel != null){
			flt.setLotacao(lotacaoSel.buscarObjeto());
		}
		flt.setIdOrgaoUsu(orgaoUsu);
		
		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean
				.valueOf(buscarFechadas) : false);
		flt.setSituacaoFuncionalPessoa("");
		
		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpPessoaDaoFiltro flt)
			throws AplicacaoException {
		Selecionavel sel = null;

		// Acrescenta o sesb e repete a busca
		final String sigla = flt.getSigla();
		flt.setSigla(getTitular().getSesbPessoa() + sigla);
		sel = dao().consultarPorSigla(flt);
		if (sel != null)
			return sel;
		flt.setSigla(sigla);

		// Procura por nome
		flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List pessoas = dao().consultarPorFiltro(flt);
		if (pessoas != null)
			if (pessoas.size() == 1)
				return (DpPessoa) pessoas.get(0);
		return null;
	}
	
	@Get
	@Post
	@Path({"/public/app/pessoa/selecionar","/app/pessoa/selecionar","/app/cosignatario/selecionar", "/pessoa/selecionar.action","/cosignatario/selecionar.action"})
	public void selecionar(String sigla, boolean exibeEmail) {
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			if(exibeEmail)
				result.include("email", dao().getPessoaPorPrincipal(sigla).getEmailPessoaAtual());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

}
