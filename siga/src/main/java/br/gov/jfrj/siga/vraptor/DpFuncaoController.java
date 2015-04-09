package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpFuncaoConfiancaDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Resource
public class DpFuncaoController extends SigaSelecionavelControllerSupport<DpFuncaoConfianca, DpFuncaoConfiancaDaoFiltro>{

	private Long orgaoUsu;
	
	public DpFuncaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		
		setSel(new DpFuncaoConfianca());
		setItemPagina(10);
	}

	@Override
	protected DpFuncaoConfiancaDaoFiltro createDaoFiltro() {
		final DpFuncaoConfiancaDaoFiltro flt = new DpFuncaoConfiancaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setIdOrgaoUsu(orgaoUsu);
		if (flt.getIdOrgaoUsu() == null){
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		}
		return flt;
	}
	
	@Override
	public Selecionavel selecionarPorNome(final DpFuncaoConfiancaDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List<DpFuncaoConfianca> l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpFuncaoConfianca) l.get(0);
		return null;
	}
	
	@Get
	@Post
	@Path("/app/funcao/buscar")
	public void buscar(String nome, String postback, Integer offset, Long idOrgaoUsu) throws Exception {
		if (postback == null){
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		}else{
			orgaoUsu = idOrgaoUsu;
		}
		this.getP().setOffset(offset);
		super.aBuscar(nome,postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu",getOrgaosUsu());
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("nome",nome);
		result.include("postbak",postback);
		result.include("offset",offset);
	}

	@Get("/app/funcao/selecionar")
	public void selecionar(String sigla) {
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}
}
