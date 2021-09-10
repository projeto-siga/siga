package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class OrgaoController extends SigaSelecionavelControllerSupport<CpOrgao, DaoFiltroSelecionavel>{


	/**
	 * @deprecated CDI eyes only
	 */
	public OrgaoController() {
		super();
	}

	@Inject
	public OrgaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		final CpOrgaoDaoFiltro flt = new CpOrgaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}
	
	@Get("app/orgao/listar")
	public void lista(Integer paramoffset) throws Exception {
		if(paramoffset == null) {
			paramoffset = 0;
		}
		setItens(CpDao.getInstance().consultarCpOrgaoOrdenadoPorNome(paramoffset, 15));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidadeOrgao());
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
	}
	
	protected void selecionarPorNome(){
		
	}
	
	@Transacional
	public void excluir(final Long id) throws Exception{
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		if (id != null) {
			try {
				dao().iniciarTransacao();
				CpOrgao orgao = daoOrgao(id);				
				dao().excluir(orgao);				
				dao().commitTransacao();				
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na exclusão de Orgão", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");
		this.result.redirectTo(this).lista(0);
	}
	
	@Get("/app/orgao/editar")
	public void edita(final Long id){
		if (id != null) {
			CpOrgao orgao = daoOrgao(id);
			result.include("nmOrgao",orgao.getNmOrgao());
			result.include("siglaOrgao",orgao.getSigla());
			result.include("ativo",orgao.getRegistroAtivo() != null && !orgao.getRegistroAtivo().isEmpty() ? orgao.getRegistroAtivo() : "N");
			result.include("idOrgaoUsu",orgao.getOrgaoUsuario() != null ? orgao.getOrgaoUsuario().getId() : null);
		}
		result.include("request",getRequest());
		result.include("id",id);
		result.include("orgaosUsu",this.getOrgaosUsu());
	}
	
	@Transacional
	@Post("/app/orgao/gravar")
	public void editarGravar(final Long id, 
							 final String nmOrgao,
							 final String siglaOrgao, 
							 final Long idOrgaoUsu, 
							 final String ativo) throws Exception{
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		
		if(nmOrgao == null)
			throw new AplicacaoException("Nome do órgão Externo não informado");
		
		if(siglaOrgao == null)
			throw new AplicacaoException("Sigla do órgão Externo não informada");
		
		CpOrgao orgao;		
		if (id == null)
			orgao = new CpOrgao();
		else
			orgao = daoOrgao(id);	
		
		orgao.setNmOrgao(nmOrgao);
		orgao.setSigla(siglaOrgao);
		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);	
			orgao.setOrgaoUsuario(orgaoUsuario);
		}else
			orgao.setOrgaoUsuario(null);
		
		orgao.setAtivo(String.valueOf(ativo));
		
		try {
			dao().iniciarTransacao();
			dao().gravar(orgao);
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		this.result.redirectTo(this).lista(0);
	}
	
	private CpOrgao daoOrgao(long id) {
		return dao().consultar(id, CpOrgao.class, false);
	}
	
	@Get
	@Post
	@Path({"/app/orgao/buscar","/orgao/buscar.action"})
	public void busca(final String sigla,
			     	  final Integer paramoffset,
			     	  final String postback,
			     	  final String propriedade) throws Exception {
		this.getP().setOffset(paramoffset);
		this.aBuscar(sigla, postback);
		
		result.include("itens",this.getItens());
		result.include("tamanho",this.getTamanho());
		result.include("request",getRequest());
		result.include("sigla",sigla);
		result.include("offset",paramoffset);
		result.include("postback",postback);
		result.include("propriedade",propriedade);
	}
	
	@Get
	@Post
	@Path({"/app/orgao/selecionar","/orgao/selecionar.action"})
	public void selecionar(final String sigla){
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}
	
}
