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
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;
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

		result.include("atts", atts);
		result.include("objetivos", objetivos);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("tiposAtributo",SrTipoAtributo.values());
	}

	@Path("/gravar")
	public void gravarAtributo(SrAtributo atributo) throws Exception {
		// assertAcesso("ADM:Administrar");
		validarFormEditarAtributo(atributo);
		atributo.salvar();
		result.use(Results.http()).body(atributo.toVO(false).toJson());
	}

	@Path("/desativarAtributo")
	public void desativarAtributo(Long id) throws Exception {
		// assertAcesso("ADM:Administrar");
		SrAtributo item = SrAtributo.AR.findById(id);
		item.finalizar();
		result.use(Results.http()).body(item.toJson());
	}

	@Path("/reativarAtributo")
	public void reativarAtributo(Long id) throws Exception {
		// assertAcesso("ADM:Administrar");
		SrAtributo item = SrAtributo.AR.findById(id);
		item.salvar();
		result.use(Results.http()).body(item.toJson(false));
	}
	
	@Path("/associacaoAtributo")
	public void buscarAssociacaoAtributo(Long idAtributo) throws Exception {
		SrAtributo attr = SrAtributo.AR.findById(idAtributo);
		String ret = "";
		
		if (attr != null) {
			 ret = attr.toJson(true);
		}
		result.use(Results.http()).body(ret);
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
