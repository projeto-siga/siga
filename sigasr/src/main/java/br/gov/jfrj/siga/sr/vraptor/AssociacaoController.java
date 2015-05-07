package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
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
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/associacao")
public class AssociacaoController extends SrController {


	public AssociacaoController(HttpServletRequest request, Result result,  SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@Path("/desativar")
	public void desativarAssociacao(Long idAssociacao) throws Exception {
		// assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = SrConfiguracao.AR.findById(idAssociacao);
		associacao.finalizar();
		result.use(Results.http()).body(associacao.toJson());
	}

	@Path("/gravar")
	public void gravarAssociacao(SrConfiguracao associacao,SrAtributo atributo, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, CpComplexo complexo, CpOrgaoUsuario orgaoUsuario,
			DpLotacaoSelecao lotacaoSel, DpPessoaSelecao pessoaSel, DpFuncaoConfiancaSelecao funcaoSel, DpCargoSelecao cargoSel, CpPerfilSelecao perfilSel) throws Exception {
		// assertAcesso("ADM:Administrar");
		associacao.setAtributo(SrAtributo.AR.findById(atributo.getIdAtributo()));
		
		associacao.setItemConfiguracaoSet(itemConfiguracaoSet);
		associacao.setAcoesSet(acoesSet);
		
		associacao.setComplexo(CpComplexo.AR.findById(complexo.getIdComplexo()));
		associacao.setOrgaoUsuario(CpOrgaoUsuario.AR.findById(orgaoUsuario.getIdOrgaoUsu()));

		if (pessoaSel != null)
			associacao.setDpPessoa(pessoaSel.buscarObjeto());

		if (lotacaoSel != null)
			associacao.setLotacao(lotacaoSel.buscarObjeto());

		if (funcaoSel != null)
			associacao.setFuncaoConfianca(funcaoSel.buscarObjeto());

		if (cargoSel != null)
			associacao.setCargo(cargoSel.buscarObjeto());

		if (perfilSel != null){
			associacao.setCpGrupo(perfilSel.buscarObjeto());
		}

		associacao.salvarComoAssociacaoAtributo();
		// SrConfiguracao.AR.em().refresh(associacao);
		SrConfiguracao refresh = SrConfiguracao.AR.findById(associacao.getId());
		result.use(Results.http()).body(refresh.toVO().toJson());
	}

	@Path("/gravarComoPesquisa")
	public void gravarAssociacaoPesquisa(SrConfiguracao associacao, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, CpComplexo complexo, CpOrgaoUsuario orgao) throws Exception {
		// assertAcesso("ADM:Administrar");
		associacao.setAtributo(SrAtributo.AR.findById(associacao.getId()));
		associacao.setItemConfiguracaoSet(itemConfiguracaoSet);
		associacao.setAcoesSet(acoesSet);
		associacao.salvarComoAssociacaoPesquisa();
		SrConfiguracao refresh = SrConfiguracao.AR.findById(associacao.getId());
		result.use(Results.http()).body(refresh.toVO().toJson());
	}

}
