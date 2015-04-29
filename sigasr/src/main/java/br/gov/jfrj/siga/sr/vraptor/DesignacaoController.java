package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.db.jpa.JPA;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.vraptor.SigaObjects;


@Resource
@Path("app/designacao")
public class DesignacaoController extends SrController {

	public DesignacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Path("/listar/{mostrarDesatuvados}")
	public void listar(boolean mostrarDesativados) {
//		assertAcesso("ADM:Administrar");
//		List<SrConfiguracao> designacoes = new ArrayList<SrConfiguracao>();
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(mostrarDesativados, null);
		List<CpOrgaoUsuario> orgaos = JPA.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.all().fetch();

		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.find("hisDtFim is null").fetch();

		result.include("modoExibicao", "designacao");
		result.include("designacoes", designacoes);
		result.include("locais", locais);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
	}

//	public static String desativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
////		assertAcesso("ADM:Administrar");
//		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
//		designacao.finalizar();
//
//		return designacao.getSrConfiguracaoJson();
//	}

//	public static String reativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
////		assertAcesso("ADM:Administrar");
//		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
//		designacao.salvar();
//
//		return designacao.getSrConfiguracaoJson();
//	}

//	public static String gravarDesignacao(SrConfiguracao designacao) throws Exception {
////		assertAcesso("ADM:Administrar");
////		validarFormEditarDesignacao(designacao);
//		designacao.salvarComoDesignacao();
//		designacao.refresh();
//		return designacao.getSrConfiguracaoJson();
//	}

//	@SuppressWarnings("static-access")
//	private void validarFormEditarDesignacao(SrConfiguracao designacao) throws Exception {
//		StringBuffer sb = new StringBuffer();
//
//		if (designacao.getDescrConfiguracao() == null || designacao.getDescrConfiguracao().isEmpty())
//			srValidator.addError("designacao.descrConfiguracao", "Descrição não informada");
//
//		for (SrError error: srValidator.getErros()) {
//			sb.append(error.getKey() + ";");
//		}
//
//		if (srValidator.hasErrors()) {
//			throw new Exception(sb.toString());
//		}
//	}

}
