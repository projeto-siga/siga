package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.sr.model.SrEquipe;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/equipe")
public class EquipeController extends SrController{

	public EquipeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, SrDao.getInstance(), so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	
	@Path("/listar")
	public void listar(boolean mostrarDesativados) {
//		assertAcesso("ADM:Administrar");
		List<SrEquipe> listaEquipe = SrEquipe.listar(mostrarDesativados);
		List<CpOrgaoUsuario> orgaos = dao.listarOrgaosUsuarios();
//		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = dao.listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();
		SelecionavelVO lotacaoUsuario = SelecionavelVO.createFrom(getLotaTitular());

		result.include("listaEquipe", listaEquipe);
		result.include("orgaos",orgaos);
//		result.include("locais",locais);
		result.include("unidadesMedida",unidadesMedida);
		result.include("pesquisaSatisfacao",pesquisaSatisfacao);
		result.include("lotacaoUsuario",lotacaoUsuario);
		
	}

}
