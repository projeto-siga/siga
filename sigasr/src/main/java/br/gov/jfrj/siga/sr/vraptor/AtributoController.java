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
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;
import br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/atributo")
public class AtributoController extends SrController {


	public AtributoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	@SuppressWarnings("unchecked")
	@Path("/listar")
//	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
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
		
		result.include("pessoaSel", new DpPessoaSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("funcaoSel", new DpFuncaoConfiancaSelecao());
		result.include("cargoSel", new DpCargoSelecao());
		result.include("cpGrupoSel", new CpPerfilSelecao());

		
//		result.include("acaoSel", new CpPerfilSelecao());
//		result.include("itemConfiguracaoSel", new CpPerfilSelecao());
	}

	@Path("/gravar")
//	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
	public void gravarAtributo(SrAtributo atributo) throws Exception {
		validarFormEditarAtributo(atributo);
		atributo.salvar();
		result.use(Results.http()).body(atributo.toVO(false).toJson());
	}

	@Path("/desativar")
//	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
	public void desativarAtributo(Long id) throws Exception {
		SrAtributo item = SrAtributo.AR.findById(id);
		item.finalizar();
		result.use(Results.http()).body(item.toJson());
	}

	@Path("/reativar")
//	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
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
	@AssertAcesso(SrSigaPermissaoPerfil.ADM_ADMINISTRAR)
    public void listarAssociacaoAtributo(Long idAtributo, boolean exibirInativos) throws Exception {
    	SrAtributo att = new SrAtributo();
    	if (idAtributo != null)
    		att = SrAtributo.AR.findById(idAtributo);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, exibirInativos);
        result.use(Results.http()).body(SrConfiguracao.convertToJSon(associacoes));
    }

	private void validarFormEditarAtributo(SrAtributo atributo) {
		if (atributo.getTipoAtributo() == SrTipoAtributo.VL_PRE_DEFINIDO && atributo.getDescrPreDefinido().equals("")) {
			srValidator.addError("att.descrPreDefinido", "Valores Pré-definido não informados");
		}

		if (srValidator.hasErrors()) {
			enviarErroValidacao();
		}
	}

}

