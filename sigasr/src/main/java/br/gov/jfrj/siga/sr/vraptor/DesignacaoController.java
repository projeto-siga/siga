package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;

@Controller
@Path("/app/designacao")
public class DesignacaoController extends SrController {

	/**
	 * @deprecated CDI eyes only
	 */
	public DesignacaoController() {
		super();
	}
	
	@Inject
	public DesignacaoController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}

	@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar")
	public void listar(boolean mostrarDesativados) {
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(
				mostrarDesativados, null);
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find(
				"hisDtFim is null").fetch();

		result.include("modoExibicao", "designacao");
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("designacoes", designacoes);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);

		result.include("atendenteSel", new DpLotacaoSelecao());
		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
	}
	 
	@Transacional
	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.findById(id);
		designacao.finalizar();

		result.use(Results.http()).body(designacao.toJson());
	}

	@Transacional
	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrConfiguracao designacao = SrConfiguracao.AR.em().find(
				SrConfiguracao.class, id);
		designacao.salvarComHistorico();

		result.use(Results.http()).body(designacao.toJson());
	}

	@Transacional
	@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravar(SrConfiguracao designacao, Long cpGrupoId, 
			List<SrItemConfiguracao> itemConfiguracaoSet, 
			List<SrAcao> acoesSet) throws Exception {	
		
			
		designacao.setItemConfiguracaoSet(setupItemConfiguracao(itemConfiguracaoSet));
		designacao.setAcoesSet(setupAcoes(acoesSet));
		setupDesignacao(designacao, cpGrupoId);
	
		
		validarFormEditarDesignacao(designacao);

		if (srValidator.hasErrors())
			return;

		designacao.salvarComoDesignacao();
		
		result.use(Results.http()).body(designacao.toJson());
	}
		
	
	/**
	 * Utilizado para ajustar o objeto recebido devido a mudanca do vraptor 3 para o 4.
	 */
	private void setupDesignacao(SrConfiguracao designacao, Long cpGrupoId) {
		if(designacao.getCargo() != null && designacao.getCargo().getIdCargoIni() == null) designacao.setCargo(null);
		if(designacao.getFuncaoConfianca() != null && designacao.getFuncaoConfianca().getIdFuncao() == null) designacao.setFuncaoConfianca(null);
		if(designacao.getComplexo() != null && designacao.getComplexo().getIdComplexo() == null) designacao.setComplexo(null);
		if(designacao.getDpPessoa() != null && designacao.getDpPessoa().getIdPessoa() == null) designacao.setDpPessoa(null);
		if(designacao.getLotacao() != null && designacao.getLotacao().getIdLotacao() == null) designacao.setLotacao(null);
		if(designacao.getOrgaoUsuario() != null && designacao.getOrgaoUsuario().getIdOrgaoUsu() == null) designacao.setOrgaoUsuario(null);
		
		if(cpGrupoId != null) {		
			EntityManager em = ContextoPersistencia.em();
			designacao.setCpGrupo(em.find(CpGrupo.class, cpGrupoId));
		}
		
	}
	
	/**
	 * Utilizado para ajustar o objeto recebido devido a mudanca do vraptor 3 para o 4.
	 */
	private List<SrItemConfiguracao> setupItemConfiguracao(List<SrItemConfiguracao> itemConfiguracaoSet) {
		if(itemConfiguracaoSet == null || itemConfiguracaoSet.size() == 0) return null;
		List<SrItemConfiguracao> result = new ArrayList<>();
		for(SrItemConfiguracao item : itemConfiguracaoSet) {
			if(item.getIdItemConfiguracao() != null)
				result.add(SrItemConfiguracao.AR.findById(item.getIdItemConfiguracao()));
		}
		return result;
	}

	/**
	 * Utilizado para ajustar o objeto recebido devido a mudanca do vraptor 3 para o 4.
	 */
	private List<SrAcao> setupAcoes(List<SrAcao> acoesSet) {
		if(acoesSet == null || acoesSet.size() == 0) return null;
		
		List<SrAcao> result = new ArrayList<>();
		for(SrAcao acao : acoesSet) {
			if(acao.getIdAcao() != null)
				result.add(SrAcao.AR.findById(acao.getIdAcao()));
		}
		return result;
	}

	private void validarFormEditarDesignacao(SrConfiguracao designacao) {
		if (designacao.getDescrConfiguracao() == null
				|| designacao.getDescrConfiguracao().isEmpty())
			srValidator.addError("designacao.descrConfiguracao",
					"Descrição não informada");

		if (srValidator.hasErrors())
			enviarErroValidacao();
	}
}
