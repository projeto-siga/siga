package br.gov.jfrj.siga.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class CpOrgaoController extends SigaSelecionavelControllerSupport<CpOrgao, DaoFiltroSelecionavel>{

	public CpOrgaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		final CpOrgaoDaoFiltro flt = new CpOrgaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}
	
	public void aListar(){
		
	}
	
	public void selecionarPorNome(){
		
	}
	
	public void aExcluir(){
		
	}
	
	public void aEditar(){
		
	}
	
	public void aEditarGravar(){
		
	}

	

}
