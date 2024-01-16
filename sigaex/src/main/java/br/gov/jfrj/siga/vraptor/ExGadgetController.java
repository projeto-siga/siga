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
 * Criado em 23/11/2005
 */

package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeOtimizarQuadroDeExpedientes;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExGadgetController extends ExController {

	/**
	 * @deprecated CDI eyes only
	 */
	public ExGadgetController() {
		super();
	}

	@Inject
	public ExGadgetController(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Get("app/expediente/gadget")
	public void execute(final String idTpMarcadorExcluir,     boolean apenasQuadro)
			throws Exception {

		List listEstados = dao().consultarPaginaInicial(getTitular(), getLotaTitular(), Ex.getInstance().getComp().pode(ExPodeOtimizarQuadroDeExpedientes.class, getTitular(), getLotaTitular()) );
		
		if (!listEstados.isEmpty()){
		
			String idTpMarcadorIgnoradosQuadroQuantitativo = "7,8,9,10,11,12,13,16,18,20,21,22,26,32,50,51,62,63,64";
			String idTpMarcadorExcluidos = (StringUtils.isNotBlank(idTpMarcadorExcluir) ? idTpMarcadorExcluir+"," : "" )+ idTpMarcadorIgnoradosQuadroQuantitativo;
		
			final String as[] = idTpMarcadorExcluidos.split(",");
			final Set<Long> excluir = new HashSet<Long>();

			for (final String s : as) {
				excluir.add(Long.valueOf(s));
			}
		
			final List listEstadosReduzida = new ArrayList<Object[]>();
			
			for (Object o : listEstados) {
				if (!excluir.contains(  ((Object[]) o)[0] 	)
				   ) {
					listEstadosReduzida.add(o);
				}
			}

			listEstados = listEstadosReduzida;
		}

		if (super.getTitular() == null) {
			throw new AplicacaoException("Titular nulo, verificar se usuário está ativo no RH");
		}

		super.getRequest().setAttribute("_cadastrante", super.getTitular().getSigla() + "@"
				+ super.getLotaTitular().getOrgaoUsuario().getSiglaOrgaoUsu() + super.getLotaTitular().getSigla());

		List <Integer> listIdTpFormaDoc = new ArrayList<Integer>() {{add(1); add(2);}};
		
		result.include("listEstados", listEstados);
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("listIdTpFormaDoc", listIdTpFormaDoc);
		result.include("documentoVia", new ExMobilSelecao());
		result.include("apenasQuadro", apenasQuadro);
	}
	
	@Get("/public/app/testes/gadgetTest")
	public void test(final String matricula, final Integer idTpFormaDoc) throws Exception {
		if (matricula == null) {
			result.use(Results.http()).body("ERRO: É necessário especificar o parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		final DpPessoa pes = daoPes(matricula);

		if (pes == null) {
			result.use(Results.http())
					.body("ERRO: Não foi localizada a pessoa referenciada pelo parâmetro 'matricula'.")
					.setStatusCode(400);
			return;
		}

		setTitular(pes);
		setLotaTitular(pes.getLotacao());
		this.execute(null,  Boolean.FALSE);
	}
}