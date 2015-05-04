package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/atributo")
public class AtributoController extends SrController {

	public AtributoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@SuppressWarnings("unchecked")
	@Path("/listar")
	public void listar(boolean mostrarDesativados) {
		// assertAcesso("ADM:Administrar");
		List<SrAtributo> atts = SrAtributo.listar(null, mostrarDesativados);
		List<SrObjetivoAtributo> objetivos = SrObjetivoAtributo.AR.all().fetch();
		List<CpOrgaoUsuario> orgaos = em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		
		result.include("atts",atts);
		result.include("objetivos",objetivos);
		result.include("orgaos",orgaos);
		result.include("locais",locais);
		result.include("mostrarDesativados",mostrarDesativados);
	}

}
