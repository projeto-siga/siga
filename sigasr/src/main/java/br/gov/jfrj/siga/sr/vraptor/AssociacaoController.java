package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("app/associacao")
public class AssociacaoController extends SrController {


	/**
	 * @deprecated CDI eyes only
	 */
	public AssociacaoController() {
		super();
	}
	
	@Inject
	public AssociacaoController(HttpServletRequest request, Result result,  SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@Path("/desativar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void desativarAssociacao(Long idAssociacao) throws Exception {
		SrConfiguracao associacao = SrConfiguracao.AR.findById(idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

	@Path("/gravar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarAssociacao(SrConfiguracao associacao,SrAtributo atributo, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, CpComplexo complexo, CpOrgaoUsuario orgaoUsuario,
			DpLotacaoSelecao lotacaoSel, DpPessoaSelecao dpPessoaSel, DpFuncaoConfiancaSelecao funcaoConfiancaSel, DpCargoSelecao cargoSel, CpPerfilSelecao cpGrupoSel, SrPesquisa pesquisaSatisfacao) throws Exception {
		if (associacao == null)
			associacao = new SrConfiguracao();
		setDadosAssociacao(associacao, atributo, itemConfiguracaoSet, acoesSet, complexo, orgaoUsuario, lotacaoSel, dpPessoaSel, funcaoConfiancaSel, cargoSel, cpGrupoSel, pesquisaSatisfacao);
		associacao.salvarComoAssociacaoAtributo();
		result.use(Results.http()).body(associacao.toVO().toJson());
	}

	@Path("/gravarComoPesquisa")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarAssociacaoPesquisa(SrConfiguracao associacao, SrPesquisa pesquisa, SrAtributo atributo, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, CpComplexo complexo, CpOrgaoUsuario orgaoUsuario,
			DpLotacaoSelecao lotacaoSel, DpPessoaSelecao dpPessoaSel, DpFuncaoConfiancaSelecao funcaoConfiancaSel, DpCargoSelecao cargoSel, CpPerfilSelecao cpGrupoSel) throws Exception {

		setDadosAssociacao(associacao, atributo, itemConfiguracaoSet, acoesSet, complexo, orgaoUsuario, lotacaoSel, dpPessoaSel, funcaoConfiancaSel, cargoSel, cpGrupoSel, pesquisa);
		associacao.salvarComoAssociacaoPesquisa();
		result.use(Results.http()).body(associacao.toVO().toJson());
	}

	private void setDadosAssociacao(SrConfiguracao associacao, SrAtributo atributo, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, CpComplexo complexo, CpOrgaoUsuario orgaoUsuario,
			DpLotacaoSelecao lotacaoSel, DpPessoaSelecao pessoaSel, DpFuncaoConfiancaSelecao funcaoSel, DpCargoSelecao cargoSel, CpPerfilSelecao perfilSel, SrPesquisa pesquisaSatisfacao) throws Exception {
		associacao.setItemConfiguracaoSet(itemConfiguracaoSet);
		associacao.setAcoesSet(acoesSet);
		associacao.setAtributo(atributo == null ? null : SrAtributo.AR.findById(atributo.getId()));
		associacao.setPesquisaSatisfacao(pesquisaSatisfacao == null ? null : SrPesquisa.AR.findById(pesquisaSatisfacao.getId()));
		associacao.setComplexo(complexo == null ? null : CpComplexo.AR.findById(complexo.getIdComplexo()));
		associacao.setOrgaoUsuario(orgaoUsuario == null ? null : CpOrgaoUsuario.AR.findById(orgaoUsuario.getIdOrgaoUsu()));
		associacao.setDpPessoa(pessoaSel.buscarObjeto());
		associacao.setLotacao(lotacaoSel.buscarObjeto());
		associacao.setFuncaoConfianca(funcaoSel.buscarObjeto());
		associacao.setCargo(cargoSel.buscarObjeto());
		associacao.setCpGrupo(perfilSel.buscarObjeto());
	}

}
