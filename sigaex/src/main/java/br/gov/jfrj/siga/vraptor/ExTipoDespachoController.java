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
package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.vraptor.builder.ExTipoDespachoBuilder;

@Controller
public class ExTipoDespachoController extends ExController {

	private static final String CAMINHO_ACESSO = "FE:Ferramentas;DESP:Tipos de despacho";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExTipoDespachoController() {
		super();
	}

	@Inject
	public ExTipoDespachoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
	}

	@Get("app/despacho/tipodespacho/listar")
	public void lista() {
		assertAcesso(CAMINHO_ACESSO);

		List<ExTipoDespacho> tiposDespacho = dao().listarExTiposDespacho();

		result.include("tiposDespacho", tiposDespacho);
	}

	@Get("app/despacho/tipodespacho/editar")
	public ExTipoDespacho edita(final Long id) {
		assertAcesso(CAMINHO_ACESSO);
		List<ExTipoDespacho> tiposDespacho = dao().listarExTiposDespacho();
		result.include("tiposDespacho", tiposDespacho);
		if (id != null) {
			return dao().consultar(id, ExTipoDespacho.class, false);
		} else {
			final ExTipoDespachoBuilder builder = ExTipoDespachoBuilder.novaInstancia();
			return builder.construir(dao());
		}
	}

	@Transacional
	@Get("app/despacho/tipodespacho/apagar")
	public void exclui(final Long id) {
		assertAcesso(CAMINHO_ACESSO);
		if (id != null) {
			ExTipoDespacho tipo = dao().consultar(id, ExTipoDespacho.class, false);
			try {
				dao().iniciarTransacao();
				dao().excluir(tipo);
				dao().commitTransacao();
				result.redirectTo(this).lista();
			} catch (Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Ocorreu um Erro durante a remoção.", 0, e);
			}
		}
	}

	@Transacional
	@Post("app/despacho/tipodespacho/gravar")
	public void gravar(final ExTipoDespacho exTipoDespacho) {
		assertAcesso(CAMINHO_ACESSO);
			
		if (exTipoDespacho.getDescTpDespacho() == null) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem("Favor informar uma descrição."));
			result.forwardTo(this).edita(exTipoDespacho.getIdTpDespacho());
			return;
		}
		
		final ExTipoDespachoBuilder builder = ExTipoDespachoBuilder.novaInstancia();
		builder.setIdTpDespacho(exTipoDespacho.getIdTpDespacho());
		builder.setDescTpDespacho(exTipoDespacho.getDescTpDespacho());
		builder.setAtivo(exTipoDespacho.getFgAtivo());
		
		try {
			dao().iniciarTransacao();
			dao().gravar(builder.construir(dao()));
			dao().commitTransacao();
			result.redirectTo(this).lista();
					
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a gravação", 0, e);
		}

	}
}
