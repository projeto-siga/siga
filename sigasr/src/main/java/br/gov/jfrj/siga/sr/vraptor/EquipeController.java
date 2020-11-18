package br.gov.jfrj.siga.sr.vraptor;

import java.util.ArrayList;
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
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrEquipe;
import br.gov.jfrj.siga.sr.model.SrExcecaoHorario;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrSemana;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("app/equipe")
public class EquipeController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public EquipeController() {
		super();
	}
	
	@Inject
	public EquipeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) throws Throwable {
		super(request, result, CpDao.getInstance(), so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@Path("/listar")
	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
	public void listar(boolean mostrarDesativados) {
		List<SrEquipe> listaEquipe = SrEquipe.listar(mostrarDesativados);
		List<CpOrgaoUsuario> orgaos = dao.listarOrgaosUsuarios();

		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = dao.listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();
		DpLotacao lotaTitular = getLotaTitular();
		SelecionavelVO lotacaoUsuario = SelecionavelVO.createFrom(lotaTitular);

		DpLotacaoSelecao lotacaoSel = new DpLotacaoSelecao();
		lotacaoSel.setId(lotaTitular.getId());
		lotacaoSel.buscar();

		result.include("listaEquipe", listaEquipe);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("unidadesMedida", unidadesMedida);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
		result.include("lotacaoUsuario", lotacaoUsuario);
		result.include("lotacaoEquipeSel", lotacaoSel);
		result.include("diasSemana", SrSemana.values());
		
		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
		
		result.include("itemConfiguracao", new SelecionavelVO(null,null));
		result.include("acao", new SelecionavelVO(null,null));
	}

	@Path("/gravar")
	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
	public void gravarEquipe(SrEquipe equipe, List<SrExcecaoHorario> excecaoHorarioSet, DpLotacaoSelecao lotacaoEquipeSel) throws Exception {
		equipe.setExcecaoHorarioSet(excecaoHorarioSet);
		if (equipe.getLotacaoEquipe() == null) {
			if (lotacaoEquipeSel == null) {
				equipe.setLotacaoEquipe(getLotaTitular());
			} else {
				equipe.setLotacaoEquipe(lotacaoEquipeSel.buscarObjeto());
			}
		}
		equipe.salvarComHistorico();
		result.use(Results.http()).body(equipe.toJson());
	}

	@Path("/{id}/designacoes")
	public void buscarDesignacoesEquipe(Long id) throws Exception {
		List<SrConfiguracao> designacoes;

		if (id != null) {
			SrEquipe equipe = SrEquipe.AR.findById(id);
			designacoes = new ArrayList<SrConfiguracao>(equipe.getDesignacoes());
		} else {
			designacoes = new ArrayList<SrConfiguracao>();
		}

		result.use(Results.http()).body(SrConfiguracao.convertToJSon(designacoes));
	}

}
