package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.db.jpa.JPA;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrOperador;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/acordo")
public class AcordoController extends SrController {

	private static final String ACORDOS = "acordos";

	public AcordoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, dao, so, em, srValidator);
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {

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
		result.include("operadores", SrOperador.values());
		result.include("prioridades", SrPrioridade.values());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/gravar")
	public void gravarAcordo(SrAcordo acordo) throws Exception {
		acordo.salvar();
		result.use(Results.http()).body(acordo.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/desativar")
	public void desativar(Long id) throws Exception {
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.finalizar();

		result.use(Results.http()).body(acordo.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	@Path("/reativar")
	public void reativar(Long id) throws Exception {
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.salvar();

		result.use(Results.http()).body(acordo.toJson());
	}

	@Path("/abrangencias")
	public void buscarAbrangenciasAcordo(Long id) throws Exception {
		SrAcordo acordo = new SrAcordo();

		if (id != null)
			acordo = SrAcordo.AR.findById(id);
		List<SrConfiguracao> abrangencias = SrConfiguracao.listarAbrangenciasAcordo(Boolean.FALSE, acordo);

		result.use(Results.http()).body(SrConfiguracao.convertToJSon(abrangencias));
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarAbrangencia(SrConfiguracao associacao) throws Exception {
		associacao.salvarComoAbrangenciaAcordo();
		associacao.refresh();

		result.use(Results.http()).body(associacao.toJson());
	}

	//@AssertAcesso(ADM_ADMINISTRAR)
	public void desativarAbrangenciaEdicao(Long idAssociacao) throws Exception {
		SrConfiguracao abrangencia = SrConfiguracao.AR.findById(idAssociacao);
		abrangencia.finalizar();
	}
}
