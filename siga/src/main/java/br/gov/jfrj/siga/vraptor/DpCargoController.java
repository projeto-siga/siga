package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpCargoDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Resource
public class DpCargoController extends
		SigaSelecionavelControllerSupport<DpCargo, DpCargoDaoFiltro> {
	
	private Long orgaoUsu;

	public DpCargoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	@Get
	@Post
	@Path({"/app/cargo/buscar","/cargo/buscar.action"})
	public void busca(String nome, Long idOrgaoUsu, Integer offset, String postback) throws Exception{
		if (postback == null)
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		else
			orgaoUsu = idOrgaoUsu;
		
		this.getP().setOffset(offset);
		
		if (nome == null)
			nome = "";
		
		super.aBuscar(nome, postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("nome",nome);
		result.include("postbak",postback);
		result.include("offset",offset);
	}

	@Override
	public DpCargoDaoFiltro createDaoFiltro() {
		final DpCargoDaoFiltro flt = new DpCargoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setIdOrgaoUsu(orgaoUsu);
		try{
			flt.setIdCargoIni(Long.valueOf(getNome()));
		}catch(Exception e){
			flt.setIdCargoIni(null);
		}
		if (flt.getIdOrgaoUsu() == null)
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		return flt;
	}
	
	@Override
	public Selecionavel selecionarPorNome(final DpCargoDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpCargo) l.get(0);
		return null;
	}

	@Get
	@Post
	@Path({"/app/cargo/selecionar","/cargo/selecionar.action"})
	public void selecionar(String sigla) {
		this.setNome(sigla);
		String resultado =  super.aSelecionar(sigla);
		
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}
	
	@Get("app/cargo/listar")
	public void lista(Integer offset, Long idOrgaoUsu, String nome) throws Exception {
		
		if("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		if(idOrgaoUsu != null) {
			DpCargoDaoFiltro dpCargo = new DpCargoDaoFiltro();
			if(offset == null) {
				offset = 0;
			}
			dpCargo.setIdOrgaoUsu(idOrgaoUsu);
			dpCargo.setNome(nome);
			setItens(CpDao.getInstance().consultarPorFiltro(dpCargo, offset, 10));
			result.include("itens", getItens());
			result.include("tamanho", dao().consultarQuantidade(dpCargo));
			
			result.include("idOrgaoUsu", idOrgaoUsu);
			result.include("nome", nome);
		}
	}
	
	@Get("/app/cargo/editar")
	public void edita(final Long id){
		if (id != null) {
			DpCargo cargo = dao().consultar(id, DpCargo.class, false);
			result.include("nmCargo",cargo.getNomeCargo());
			result.include("idOrgaoUsu", cargo.getOrgaoUsuario().getId());
			result.include("nmOrgaousu", cargo.getOrgaoUsuario().getNmOrgaoUsu());
			
			List<DpPessoa> list = dao().getInstance().consultarPessoasComCargo(id);
			if(list.size() == 0) {
				result.include("podeAlterarOrgao", Boolean.TRUE);
			}
		}
		
		if("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		result.include("request",getRequest());
		result.include("id",id);
	}
	
	@Post("/app/cargo/gravar")
	public void editarGravar(final Long id, 
							 final String nmCargo, 
							 final Long idOrgaoUsu) throws Exception{
		assertAcesso("FE:Ferramentas;CAD_CARGO: Cadastrar Cargo");
		
		if(nmCargo == null)
			throw new AplicacaoException("Nome do cargo não informado");
		
		if(idOrgaoUsu == null)
			throw new AplicacaoException("Órgão não informada");
		
		List<DpPessoa> listPessoa = null;
		
		DpCargo cargo;		
		if (id == null) {
			cargo = new DpCargo();
			Date data = new Date(System.currentTimeMillis());
			cargo.setDataInicio(data);
		} else {
			cargo = dao().consultar(id, DpCargo.class, false);
			listPessoa = dao().getInstance().consultarPessoasComCargo(id);
			
		}
		cargo.setDescricao(nmCargo);
		
		if (idOrgaoUsu != null && idOrgaoUsu != 0 && (listPessoa == null || listPessoa.size() == 0)) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);	
			cargo.setOrgaoUsuario(orgaoUsuario);
		}
		
		try {
			dao().iniciarTransacao();
			dao().gravar(cargo);
			if(cargo.getIdCargoIni() == null && cargo.getId() != null) {
				cargo.setIdCargoIni(cargo.getId());
				cargo.setIdeCargo(cargo.getId().toString());
				dao().gravar(cargo);
			}
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		this.result.redirectTo(this).lista(0, null, "");
	}
}