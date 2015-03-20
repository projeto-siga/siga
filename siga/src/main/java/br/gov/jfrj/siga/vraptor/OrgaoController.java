package br.gov.jfrj.siga.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Resource
public class OrgaoController extends SigaSelecionavelControllerSupport<CpOrgao, DaoFiltroSelecionavel>{

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
	
	public void aListar(){
		
	}
	
	public void selecionarPorNome(){
		
	}
	
	public void excluir(final Long id){
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
//		this.result.redirectTo(this).aListar();
	}
	
	@Get("/app/orgao/editar")
	public void edita(final Long id){
		if (id != null) {
			CpOrgao orgao = daoOrgao(id);
			result.include("nmOrgao",orgao.getNmOrgao());
			result.include("siglaOrgao",orgao.getSigla());
			result.include("ativo",orgao.getAtivo() != null && !orgao.getAtivo().isEmpty() ? orgao.getAtivo().charAt(0) : 'N');
			result.include("idOrgaoUsu",orgao.getOrgaoUsuario() != null ? orgao.getOrgaoUsuario().getId() : null);
		}
		result.include("request",getRequest());
		result.include("id",id);
		result.include("orgaosUsu",this.getOrgaosUsu());
	}
	
	@Post("/app/orgao/gravar")
	public void editarGravar(final Long id, 
							 final String nmOrgao,
							 final String siglaOrgao, 
							 final Long idOrgaoUsu, 
							 final String ativo){
		assertAcesso("FE:Ferramentas;CAD_ORGAO: Cadastrar Orgãos");
		
		if(nmOrgao == null)
			throw new AplicacaoException("Nome do Órgão Externo não informado");
		
		if(siglaOrgao == null)
			throw new AplicacaoException("Sigla do Órgão Externo não informada");
		
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
//		this.result.redirectTo(this).aListar();
	}
	
	public CpOrgao daoOrgao(long id) {
		return dao().consultar(id, CpOrgao.class, false);
	}
	
}
