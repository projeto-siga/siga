package br.gov.jfrj.siga.sr.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.data.validation.Validation;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/itemConfiguracao")
public class ItemConfiguracaoController extends SrController {
	
	private static final String MOSTRAR_DESATIVADOS = "mostrarDesativados";
	private static final String ITENS = "itens";
	private static final String ORGAOS = "orgaos";
	private static final String LOCAIS = "locais";
	private static final String UNIDADES_MEDIDA = "unidadesMedida";
	private static final String PESQUISA_SATISFACAO = "pesquisaSatisfacao";

	public ItemConfiguracaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, dao, so, em, srValidator);
	}
	
	//@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrItemConfiguracao> itens = SrItemConfiguracao.listar(mostrarDesativados);
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();

		result.include(ITENS, itens);
		result.include(ORGAOS, orgaos);
		result.include(LOCAIS, locais);
		result.include(UNIDADES_MEDIDA, unidadesMedida);
		result.include(PESQUISA_SATISFACAO, pesquisaSatisfacao);
		result.include(MOSTRAR_DESATIVADOS, mostrarDesativados);
	}
	
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.finalizar();

		result.include("lista", lista.toJson());
	}

	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.salvar();
		
		result.include("lista", lista.toJson());
	}
	
	public void gravar(SrLista lista) throws Exception {
		lista.lotaCadastrante = getLotaTitular();
		validarFormEditarLista(lista);
		lista.salvar();
		
		result.include("lista", lista.toJson());
	}
	
	
	@Get
	@Path("/{id}/designacoes")
	public void buscarDesignacoes(Long id) throws Exception {
		List<SrConfiguracao> designacoes;

		if (id != null) {
			SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(id);
			designacoes = new ArrayList<SrConfiguracao>(itemConfiguracao.getDesignacoesAtivas());
			designacoes.addAll(itemConfiguracao.getDesignacoesPai());
		}
		else
			designacoes = new ArrayList<SrConfiguracao>();

		result.use(Results.json())
		.from(SrConfiguracao.convertToJSon(designacoes))
	    .serialize();
	}
	
	private void validarFormEditarLista(SrLista lista) {
		if (lista.nomeLista == null || lista.nomeLista.trim().equals("")) {
			Validation.addError("lista.nomeLista",
					"Nome da Lista não informados");
		}

		if (Validation.hasErrors()) {
			enviarErroValidacao();
		}
	}
	
	//@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarDesignacaoItem(SrConfiguracao designacao, Long idItemConfiguracao) throws Exception {
		designacao.salvarComoDesignacao();
		designacao.refresh();

		SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(idItemConfiguracao);
		itemConfiguracao.adicionarDesignacao(designacao);
		
		result.use(Results.json())
			.from(designacao.getSrConfiguracaoJson(itemConfiguracao))
		    .serialize();
	}

}
