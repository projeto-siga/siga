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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.hibernate.ExDao;

import com.opensymphony.xwork.Action;

@Resource
public class ExGadgetController extends ExController {
	
	
	public ExGadgetController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so) {
		super(request, response, context, result, ExDao.getInstance(), so);;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1630775520737927455L;
	private List listEstados;
	private ExMobilSelecao documentoViaSel;
	private Integer idTpFormaDoc;
	private Boolean apenasQuadro;

	public Boolean getApenasQuadro() {
		return apenasQuadro;
	}

	public void setApenasQuadro(Boolean apenasQuadro) {
		this.apenasQuadro = apenasQuadro;
	}

	public Integer getIdTpFormaDoc() {
		return idTpFormaDoc;
	}

	public void setIdTpFormaDoc(Integer idTpMarca) {
		this.idTpFormaDoc = idTpMarca;
	}

	public ExMobilSelecao getDocumentoViaSel() {
		return documentoViaSel;
	}

	public void setDocumentoViaSel(final ExMobilSelecao documentoViaSel) {
		this.documentoViaSel = documentoViaSel;
	}

	@Get("app/expediente/gadget")
	public void execute(String idTpMarcadorExcluir, Integer idTpFormaDoc) throws Exception {
		this.setIdTpFormaDoc(idTpFormaDoc);
		if (this.getIdTpFormaDoc() == null || this.getIdTpFormaDoc() == 0)
			throw new Exception(
					"Código do tipo de marca (Processos ou Expedientes) não foi informado");
		listEstados = dao().consultarPaginaInicial(getTitular(),
				getLotaTitular(), getIdTpFormaDoc());
		
		if (idTpMarcadorExcluir != null) {
			String as[] = idTpMarcadorExcluir.split(",");
			Set<Integer> excluir = new HashSet<Integer>();
			for (String s : as) {
				excluir.add(Integer.valueOf(s));
			}
			List listEstadosReduzida = new ArrayList<Object[]>();
			for (Object o : listEstados) {
				if (!excluir.contains((Integer) ((Object[]) o)[0])) {
					listEstadosReduzida.add(o);
				}
			}
			listEstados = listEstadosReduzida;
		}

		if (super.getTitular() == null) 
			throw new Exception(
					"Titular nulo, verificar se usuário está ativo no RH");
		super.getRequest().setAttribute(
				"_cadastrante",
				super.getTitular().getSigla()
						+ "@"
						+ super.getLotaTitular().getOrgaoUsuario()
								.getSiglaOrgaoUsu()
						+ super.getLotaTitular().getSigla());
		
		result.include("listEstados", this.getListEstados());
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("idTpFormaDoc", this.getIdTpFormaDoc());
	}

	public void test() throws Exception {
		DpPessoa pes = daoPes(param("matricula"));
		if (getIdTpFormaDoc() == null || getIdTpFormaDoc() == 0)
			setIdTpFormaDoc(1);
		setTitular(pes);
		setLotaTitular(pes.getLotacao());
		execute(null, null);
	}

	public List getListEstados() {
		return listEstados;
	}

	public void setListEstados(final List listEstados) {
		this.listEstados = listEstados;
	}

}
