package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.ADM_ADMINISTRAR;

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
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.util.Util;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.uteis.PessoaLotaFuncCargoSelecaoHelper;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;

@Controller
@Path("app/atributo")
public class AtributoController extends SrController {


	/**
	 * @deprecated CDI eyes only
	 */
	public AtributoController() {
		super();
	}
	
	@Inject
	public AtributoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) throws Throwable {
		super(request, result, CpDao.getInstance(), so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@SuppressWarnings("unchecked")
	@Path("/listar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void listar(boolean mostrarDesativados) {
		List<SrAtributo> atts = SrAtributo.listar(null, mostrarDesativados);
		List<SrObjetivoAtributo> objetivos = SrObjetivoAtributo.AR.all().fetch();
		List<CpOrgaoUsuario> orgaos = em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();

		result.include("atts", atts);
		result.include("objetivos", objetivos);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("tiposAtributo",SrTipoAtributo.values());
		
		result.include("pessoa", new DpPessoaSelecao());
		PessoaLotaFuncCargoSelecaoHelper.adicionarCamposSelecao(result);
		
		result.include("itemConfiguracao", new SelecionavelVO(null,null));
		result.include("acao", new SelecionavelVO(null,null));
	}

	@Transacional
	@Path("/gravar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarAtributo(SrAtributo atributo, Long objetivoAtributoId) throws Exception {
		setupObjetivoAtributo(atributo, objetivoAtributoId);
		if (validarFormEditarAtributo(atributo)) {
			atributo.salvar();
			result.use(Results.http()).body(atributo.toVO(false).toJson());
		}
	}
	
	private void setupObjetivoAtributo(SrAtributo atributo, Long objetivoAtributoId) {
		if(objetivoAtributoId != null) {
			EntityManager em = ContextoPersistencia.em();
			atributo.setObjetivoAtributo(em.find(SrObjetivoAtributo.class, objetivoAtributoId));
		}
	}

	@Transacional
	@Path("/desativar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void desativarAtributo(Long id) throws Exception {
		SrAtributo item = SrAtributo.AR.findById(id);
		item.finalizar();
		result.use(Results.http()).body(item.toJson());
	}

	@Transacional
	@Path("/reativar")
	@AssertAcesso(ADM_ADMINISTRAR)
	public void reativarAtributo(Long id) throws Exception {
		SrAtributo item = SrAtributo.AR.findById(id);
		item.salvar();
		result.use(Results.http()).body(item.toJson(false));
	}

	@Path("/associacoes")
	public void buscarAssociacaoAtributo(Long idAtributo) throws Exception {
		SrAtributo attr = SrAtributo.AR.findById(idAtributo);
		String ret = "";

		if (attr != null) {
			 ret = attr.toJson(true);
		}
		result.use(Results.http()).body(ret);
	}

	@Path("/atributos")
	@AssertAcesso(ADM_ADMINISTRAR)
    public void listarAssociacaoAtributo(Long idAtributo, boolean exibirInativos) throws Exception {
    	SrAtributo att = new SrAtributo();
    	if (idAtributo != null)
    		att = SrAtributo.AR.findById(idAtributo);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, exibirInativos);
        result.use(Results.http()).body(SrConfiguracao.convertToJSon(associacoes));
    }

	private boolean validarFormEditarAtributo(SrAtributo atributo) {
		if (atributo.getTipoAtributo() == SrTipoAtributo.VL_PRE_DEFINIDO && (atributo.getDescrPreDefinido() == null || atributo.getDescrPreDefinido().isEmpty() ) ) {
			srValidator.addError("atributo.descrPreDefinido", "Valores Pré-definido não informados");
		}

		if (srValidator.hasErrors()) {
			enviarErroValidacao();
			return false;
		}
		
		return true;
	}
	
	@Path("/selecionar")
	public void selecionar(String sigla)throws Exception {
		SrAtributo atributo = new SrAtributo().selecionar(sigla);
		result
			.forwardTo(SelecaoController.class)
			.ajaxRetorno(atributo);
	}

	@Path("/buscar")
	public void buscar(String sigla, String nomeAtributo, String propriedade) {
		List<SrAtributo> atributos = null;
		SrAtributo filtro = new SrAtributo();
		
		if (Util.notNullAndEmpty(sigla))
			filtro.setSigla(sigla);
		if (Util.notNullAndEmpty(nomeAtributo))
			filtro.setNomeAtributo(nomeAtributo);
		
		try {
			atributos = filtro.buscar();			
		} catch (Exception e) {
			atributos = new ArrayList<SrAtributo>();
		}
		
		result.include("atributos", atributos);
		result.include("filtro", filtro);
		result.include("propriedade", propriedade);
	}


}

