package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.dao.SrDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrError;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;


@Resource
@Path("app/designacao")
public class DesignacaoController extends SrController {

	public DesignacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

//	@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) {
//		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(mostrarDesativados, null);
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais =  CpComplexo.AR.all().fetch();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();

		result.include("modoExibicao", "designacao");
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("designacoes", designacoes);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
	}

	@Path("/listar")
	public void listar() {
		result.redirectTo(DesignacaoController.class).listar(Boolean.FALSE);
	}

	@Path("/listarDesativados")
	public void listarDesativados() throws Exception {
		result.redirectTo(DesignacaoController.class).listar(Boolean.TRUE);
	}

//	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.findById(id);
		designacao.finalizar();

		result.use(Results.http()).body(designacao.toJson());
	}

//	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.em().find(SrConfiguracao.class, id);
		designacao.salvar();
//
		result.use(Results.http()).body(designacao.toJson());
	}

//	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrConfiguracao designacao) throws Exception {
		validarFormEditarDesignacao(designacao);
		designacao.salvarComoDesignacao();
		designacao.refresh();

		result.use(Results.http()).body(designacao.toJson());
	}

	private void validarFormEditarDesignacao(SrConfiguracao designacao) throws Exception {
		StringBuffer sb = new StringBuffer();

		if (designacao.getDescrConfiguracao() == null || designacao.getDescrConfiguracao().isEmpty())
			srValidator.addError("designacao.descrConfiguracao", "Descrição não informada");

		for (SrError error: srValidator.getErros())
			sb.append(error.getKey() + ";");

		if (srValidator.hasErrors())
			throw new Exception(sb.toString());
	}

}
