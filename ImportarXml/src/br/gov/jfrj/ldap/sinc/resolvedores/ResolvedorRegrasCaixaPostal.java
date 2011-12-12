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
package br.gov.jfrj.ldap.sinc.resolvedores;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;

public class ResolvedorRegrasCaixaPostal {

	private SincProperties conf;
	private final List<RegraCaixaPostal> listaRegras = new ArrayList<RegraCaixaPostal>();
	private static ResolvedorRegrasCaixaPostal instancia;
	

	public static ResolvedorRegrasCaixaPostal getInstancia(SincProperties conf) throws AplicacaoException {
		if(instancia==null){
			instancia = new ResolvedorRegrasCaixaPostal(conf);
		}
		return instancia;
	}
	
//	public static ResolvedorRegrasCaixaPostal getInstancia() throws AplicacaoException {
//		return ResolvedorRegrasCaixaPostal.getInstancia(SincProperties.getInstancia());
//	}
	
	private ResolvedorRegrasCaixaPostal(SincProperties conf) throws AplicacaoException{
		this.conf = conf;
		List<DpPessoa> listaPessoas = consultarPessoas();
		for(String r:conf.getListaRegrasNovaCaixa()){
			RegraCaixaPostal regra = new RegraCaixaPostal(listaPessoas);
			String homeMDB = conf.getListaHomeMdb().get(Integer.valueOf(r.substring(0, r.indexOf(".")))-1);
			String templateLink = conf.getListaTemplateLink().get(Integer.valueOf(r.substring(r.indexOf(".")+1, r.indexOf("@")))-1);
			String expressao = r.substring(r.indexOf("@")+1);
			regra.setHomeMDB(homeMDB);
			regra.setTemplateLink(templateLink);
			regra.setExpressao(expressao);
			listaRegras.add(regra);
		}
			
	}
	
	public RegraCaixaPostal getRegra(DpPessoa p) throws AplicacaoException {
		for (RegraCaixaPostal r : listaRegras) {
			if (r.isAfetado(p)){
				return r;
			}
		}
		return null;
	}
	
	public RegraCaixaPostal getRegraPorIdInicial(Long idInicialPessoa) throws AplicacaoException{
		for (RegraCaixaPostal r : listaRegras) {
			if (r.isAfetadoPorIdInicial(idInicialPessoa)){
				return r;
			}
		}
		return null;
	}
	
	public RegraCaixaPostal getRegraPorMatricula(Long matricula) throws AplicacaoException{
		for (RegraCaixaPostal r : listaRegras) {
			if (r.isAfetadoPorMatricula(matricula)){
				return r;
			}
		}
		return null;
	}
	
	private List<DpPessoa> consultarPessoas() {
		CpDao dao = CpDao.getInstance();
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome("");
		flt.setSigla("");
		flt.setLotacao(null);
		flt.setSituacaoFuncionalPessoa(conf.getIdSituacaoFuncionalAtivo()
				.toString());
		flt.setBuscarFechadas(false);
		flt.setIdOrgaoUsu(Long.valueOf(conf.getIdLocalidade()));

		List<DpPessoa> list = dao.consultarPorFiltro(flt);
		return list;
	}

}

