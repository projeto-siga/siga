package br.gov.jfrj.siga.vraptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.CpContrato;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

@Controller
public class OrgaoUsuarioController extends SigaSelecionavelControllerSupport<CpOrgaoUsuario, DaoFiltroSelecionavel>{


	/**
	 * @deprecated CDI eyes only
	 */
	public OrgaoUsuarioController() {
		super();
	}

	@Inject
	public OrgaoUsuarioController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		final CpOrgaoUsuarioDaoFiltro flt = new CpOrgaoUsuarioDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		return flt;
	}
	
	@Get("app/orgaoUsuario/listar")
	public void lista(Integer paramoffset, String nome) throws Exception {
		if(paramoffset == null) {
			paramoffset = 0;
		}
		CpOrgaoUsuarioDaoFiltro orgaoUsuario = new CpOrgaoUsuarioDaoFiltro();
		orgaoUsuario.setNome(nome);
		setItens(CpDao.getInstance().consultarPorFiltroComContrato(orgaoUsuario, paramoffset, 15));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidade(orgaoUsuario));
		result.include("nome", nome);
		if(!"ZZ".equalsIgnoreCase(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaoUsuarioSiglaLogado", getTitular().getOrgaoUsuario().getSigla());
		}
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
	}
	
	@Get("/app/orgaoUsuario/editar")
	public void edita(final Long id){
		if (id != null) {
			CpContrato contrato = daoContrato(id);
			CpOrgaoUsuario orgaoUsuario = daoOrgaoUsuario(id);
			result.include("nmOrgaoUsuario",orgaoUsuario.getDescricao());
			result.include("siglaOrgaoUsuario",orgaoUsuario.getSigla());
			result.include("isExternoOrgaoUsu",orgaoUsuario.getIsExternoOrgaoUsu());
			try {
				result.include("dtContrato",contrato.getDtContratoDDMMYYYY());
			} catch (final Exception e) {
				result.include("dtContrato","");
			}
		}
		
		List<DpPessoa> listaPessoa = CpDao.getInstance().consultarPorMatriculaEOrgao(null,id,Boolean.FALSE,Boolean.FALSE);
		
		if(listaPessoa.size() == 0) {
			result.include("podeAlterarSigla", Boolean.TRUE);
		}
		result.include("request",getRequest());
		result.include("id",id);
	}
	
	private void atualizarContrato(Long id, Date dataContrato) {
		CpContrato contrato = daoContrato(id);

		if ((contrato == null) && (dataContrato == null)) {
			// Faz nada
		} else if ((contrato == null) && (dataContrato != null)) {
			// Insere
			contrato = new CpContrato();
			contrato.setIdOrgaoUsu(id);
			contrato.setDtContrato(dataContrato);
			dao().gravar(contrato);
		} else if ((contrato != null) && (dataContrato == null)) {
			dao.excluir(contrato);
		} else if (contrato.getDtContrato().compareTo(dataContrato) != 0) {
			// Atualiza se a data foi alterada.
			contrato.setDtContrato(dataContrato);
			dao().gravar(contrato);
		}
	}

	@Transacional
	@Post("/app/orgaoUsuario/gravar")
	public void editarGravar(final Long id, 
							 final String nmOrgaoUsuario,
							 final String siglaOrgaoUsuario,
							 final String dtContrato,
							 final String acao,
							 final Boolean isExternoOrgaoUsu
						) throws Exception{
		assertAcesso("GI:Módulo de Gestão de Identidade;CAD_ORGAO_USUARIO: Cadastrar Orgãos Usuário");
		
		if(nmOrgaoUsuario == null)
			throw new AplicacaoException("Nome do órgão usuário não informado");
		
		if(siglaOrgaoUsuario == null)
			throw new AplicacaoException("Sigla do órgão usuário não informada");
		
		if(!siglaOrgaoUsuario.matches("[a-zA-Z]{1,10}"))
			throw new AplicacaoException("Sigla do órgão inválida");

		if(dtContrato != null && !dtContrato.matches("(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/(19|20)\\d{2,2}"))
			throw new AplicacaoException("Data do contrato inválida");

		Date dataContrato = null;
		if(dtContrato != null) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			dataContrato = formatter.parse(dtContrato);
		}
		
		CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
		
//		CpContrato contrato = new CpContrato();
		
		orgaoUsuario.setSiglaOrgaoUsu(Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase().trim()));
		try {
			orgaoUsuario = dao().consultarPorSigla(orgaoUsuario);
		} catch (final Exception e) {}
		
		if((orgaoUsuario != null &&
				!orgaoUsuario.getIdOrgaoUsu().equals(id)) || (orgaoUsuario != null &&
				orgaoUsuario.getIdOrgaoUsu().equals(id) && acao.equalsIgnoreCase("i"))) {
			throw new AplicacaoException("Sigla já cadastrada para outro órgão");
		}
		
		orgaoUsuario = new CpOrgaoUsuario();
		orgaoUsuario.setNmOrgaoUsu(Texto.removeAcento(Texto.removerEspacosExtra(nmOrgaoUsuario).trim()));
		orgaoUsuario = dao().consultarPorNome(orgaoUsuario);
		
		if(orgaoUsuario != null && 
				!orgaoUsuario.getIdOrgaoUsu().equals(id)) {
			throw new AplicacaoException("Nome já cadastrado para outro órgão");
		}
		
		orgaoUsuario = new CpOrgaoUsuario();
		orgaoUsuario.setIdOrgaoUsu(id);
		orgaoUsuario = dao().consultarPorId(orgaoUsuario);
		
		if(orgaoUsuario != null && !orgaoUsuario.getSiglaOrgaoUsu().equalsIgnoreCase(siglaOrgaoUsuario) &&
				((!orgaoUsuario.getIdOrgaoUsu().equals(id) && acao.equalsIgnoreCase("a")) || (orgaoUsuario.getIdOrgaoUsu().equals(id) && acao.equalsIgnoreCase("i")))  ) {
			throw new AplicacaoException("ID já cadastrado para outro órgão");
		}
		
		if(orgaoUsuario != null && acao.equalsIgnoreCase("i") && orgaoUsuario.getIdOrgaoUsu().equals(id) &&
				orgaoUsuario.getSiglaOrgaoUsu().equalsIgnoreCase(siglaOrgaoUsuario) && orgaoUsuario.getNmOrgaoUsu().equals(nmOrgaoUsuario)) {
			throw new AplicacaoException("ID já cadastrado para outro órgão");

		}

		if(orgaoUsuario == null || orgaoUsuario.getIdOrgaoUsu() == null) {
			orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario.setIdOrgaoUsu(id);
			orgaoUsuario.setNmOrgaoUsu(Texto.removerEspacosExtra(nmOrgaoUsuario.trim()));
			orgaoUsuario.setSigla(Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase()).trim());
			orgaoUsuario.setAcronimoOrgaoUsu(Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase()).trim());
		} else {
			orgaoUsuario = daoOrgaoUsuario(id);
			orgaoUsuario.setNmOrgaoUsu(Texto.removerEspacosExtra(nmOrgaoUsuario.trim()));
			orgaoUsuario.setSigla(Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase()).trim());
			orgaoUsuario.setAcronimoOrgaoUsu(Texto.removerEspacosExtra(siglaOrgaoUsuario.toUpperCase()).trim());
		}
		
		if (isExternoOrgaoUsu != null) {
			orgaoUsuario.setIsExternoOrgaoUsu(1);
		} else {
			orgaoUsuario.setIsExternoOrgaoUsu(0);	
		}
		
		
//		contrato.setIdOrgaoUsu(id);
//		contrato.setDtContrato(dataContrato);

		try {
			dao().iniciarTransacao();
			dao().gravar(orgaoUsuario);
			atualizarContrato(id, dataContrato);
			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}

		
		this.result.redirectTo(this).lista(0, "");
	}
	
	private CpOrgaoUsuario daoOrgaoUsuario(long id) {
		return dao().consultar(id, CpOrgaoUsuario.class, false);
	}	
	
	private CpContrato daoContrato(long idOrgaoUsu) {
		return dao().consultar(idOrgaoUsu, CpContrato.class, false);
	}	
}
