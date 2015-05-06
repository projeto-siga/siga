package br.gov.jfrj.siga.sr.vraptor;


import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.db.jpa.JPA;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/acordo")
public class AcordoController extends SrController {

	private static final String ACORDOS = "acordos";

	public AcordoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	//TODO Alterado de buscarAcordo
	//@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(/*String nome, boolean popup,*/ boolean mostrarDesativados) throws Exception {

		List<SrAtributo> parametros = SrAtributo.listarParaAcordo(false);
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.findAll();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);
		
		result.include(ACORDOS, acordos);
		result.include("nome", "");
		result.include("popup", Boolean.FALSE);
		result.include("mostrarDesativados", mostrarDesativados);
		result.include("parametros", parametros);
		result.include("unidadesMedida", unidadesMedida);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
	}

	//TODO Já está Ok (Alterado de buscarAcordoDesativadas)
	public void listarDesativados() throws Exception {
		listar(/*null, Boolean.FALSE,*/ Boolean.TRUE);
	}

//	//@AssertAcesso(ADM_ADMINISTRAR)
//	@Path("/editar/{id}")
//	public void editar(Long id) throws Exception {
//		SrAcao acao = new SrAcao();
//		if (id != null)
//			acao = SrAcao.AR.findById(id);
//
//		result.include(ACAO, acao);
//	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarAcordo(SrAcordo acordo) throws Exception {
		acordo.salvar();
		result.use(Results.http()).body(acordo.toJson());
	}

	/*
	public void selecionarAcordo(String sigla) throws Exception {
		SrAcordo sel = new SrAcordo().selecionar(sigla);
		render("@selecionar", sel);
	}
	
	@SuppressWarnings("unchecked")
	//@AssertAcesso(ADM_ADMINISTRAR)
	public void buscarAcordo(String nome, boolean popup, boolean mostrarDesativados) throws Exception {

		List<SrAtributo> parametros = SrAtributo.listarParaAcordo(false);
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<CpOrgaoUsuario> orgaos = JPA.em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.all().fetch();
		List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);
		render(acordos, nome, popup, mostrarDesativados, parametros, unidadesMedida, orgaos, locais);
	}	

	*/
	
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM_ADMINISTRAR");
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.finalizar();

		result.use(Results.http()).body(acordo.toJson());
	}
	
	public void reativarAcordo(Long id, boolean mostrarDesativados) throws Exception {
		//assertAcesso("ADM_ADMINISTRAR");
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.salvar();

		result.use(Results.http()).body(acordo.toJson());
	}
	

//	private void validarFormEditarAcao(SrAcao acao) {
//		if ("".equals(acao.getSiglaAcao()))
//			srValidator.addError("siglaAcao", "Código não informado");
//
//		if ("".equals(acao.getTituloAcao()))
//			srValidator.addError("tituloAcao", "Titulo não informado");
//
//		if (srValidator.hasErrors())
//			enviarErroValidacao();
//	}
}
