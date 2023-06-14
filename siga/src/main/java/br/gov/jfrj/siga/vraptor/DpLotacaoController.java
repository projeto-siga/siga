package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.model.GenericoSelecao;
import br.gov.jfrj.siga.model.Selecionavel;

@Controller
public class DpLotacaoController extends SigaSelecionavelControllerSupport<DpLotacao, DpLotacaoDaoFiltro> {

	private Long orgaoUsu;

	/**
	 * @deprecated CDI eyes only
	 */
	public DpLotacaoController() {
		super();
	}
 
	@Inject
	public DpLotacaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);

		setSel(new DpPessoa());
		setItemPagina(10);
	}

	protected boolean temPermissaoParaExportarDados() {
		return Boolean.valueOf(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(),
				getTitular().getLotacao(), "SIGA;GI;CAD_LOTACAO;EXP_DADOS"));
	}

	@Get
	@Path({ "/app/lotacao/buscar-json/{sigla}" })
	public void busca(String sigla) throws Exception {
		aBuscarJson(sigla);
	}

	@Get
	@Path({ "/app/lotacao/buscar-json-todos-os-orgaos/{sigla}"  })
	public void buscaTodosOsOrgaos(String sigla) throws Exception {
		getRequest().setAttribute("buscarTodosOrgaos", "true");
		aBuscarJson(sigla);
	}

	protected void aBuscarJson(String sigla) throws Exception {
		Long orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();

		if (sigla == null)
			sigla = "";

		aBuscar(sigla, "", false);

		try {
			RetornoJson l = new RetornoJson();
			for (DpLotacao s : (List<DpLotacao>) getItens()) {
				RetornoJsonItem i = new RetornoJsonItem();
				i.key = Long.toString(s.getId());
				i.firstLine = s.getSiglaCompleta();
				i.secondLine = s.getDescricao();
				l.list.add(i);
			}
			Collections.sort(l.list);
			jsonSuccess(l);
		} catch (Exception e) {
			jsonError(e);
		}
	}

	@Get
	@Post
	@Path({ "/app/lotacao/buscar", "/lotacao/buscar.action" })
	public void busca(String sigla, Long idOrgaoUsu, Integer paramoffset, String postback) throws Exception {
		if (postback == null)
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		else
			orgaoUsu = idOrgaoUsu;

		this.getP().setOffset(paramoffset);

		if (sigla == null)
			sigla = "";

		super.aBuscar(sigla, postback);

		result.include("param", getRequest().getParameterMap());
		result.include("request", getRequest());
		result.include("itens", getItens());
		result.include("tamanho", getTamanho());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("idOrgaoUsu", orgaoUsu);
		result.include("sigla", sigla);
		result.include("postbak", postback);
		result.include("offset", paramoffset);
	}

	@Override
	public DpLotacaoDaoFiltro createDaoFiltro() {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		/*
		 * if (param("postback")==null)
		 * flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu()); else
		 * flt.setIdOrgaoUsu(paramInteger("orgaoUsu"));
		 */
		
		String paramBuscarTodosOrgaos = param("buscarTodosOrgaos");
		boolean buscarTodosOrgaos =  paramBuscarTodosOrgaos != null ? Boolean.valueOf(paramBuscarTodosOrgaos) : false;
		
		// Pesquisa também nos atributos do request, pois é a maneira como é passado quando
		// vem pela consulta do Angular
		paramBuscarTodosOrgaos = (String) getRequest().getAttribute("buscarTodosOrgaos");
		buscarTodosOrgaos =  paramBuscarTodosOrgaos != null ? Boolean.valueOf(paramBuscarTodosOrgaos) : false;
		
		flt.setIdOrgaoUsu(orgaoUsu);
		if (flt.getIdOrgaoUsu() == null && !buscarTodosOrgaos && getLotaTitular() != null ) {
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		}

		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean.valueOf(buscarFechadas) : false);

		return flt;
	}

	@Override
	protected Selecionavel selecionarPorNome(final DpLotacaoDaoFiltro flt) throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpLotacao) l.get(0);
		return null;
	}

	@Get
	@Post
	@Path({ "/public/app/lotacao/selecionar", "app/lotacao/selecionar", "/lotacao/selecionar.action" })
	public String selecionar(String sigla, String matricula) {
		if (matricula != null && orgaoUsu == null) {
			DpPessoa pessoa = CpDao.getInstance().getPessoaFromSigla(matricula);
			if (pessoa != null)
				orgaoUsu = pessoa.getOrgaoUsuario().getId();
		}
		String resultado = super.aSelecionar(sigla);
		if (getSel() != null) {
			try {
				/*
				 * Essa condição é necessário porque o retorno do método getSigla para o ExMobil
				 * e DpPessoa são as siglas completas, ex: JFRJ-MEM-2014/00003 e RJ14723. No
				 * caso da lotação o getSigla somente retorna SESIA. No entanto é necessário que
				 * o método selecionar retorne a sigla completa, ex: RJSESIA, pois esse retorno
				 * é o parametro de entrada para o método aExibir, que necessita da sigla
				 * completa.
				 */
				DpLotacao lotacao = new DpLotacao();
				lotacao = (DpLotacao) dao().consultar(getSel().getId(), DpLotacao.class, false);
				GenericoSelecao gs = new GenericoSelecao();
				gs.setId(getSel().getId());
				gs.setSigla(lotacao.getSiglaCompletaFormatada());
				gs.setDescricao(getSel().getDescricao());
				setSel(gs);
			} catch (final Exception ex) {
				setSel(null);
			}
		}

		if (resultado == "ajax_retorno") {
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		} else {
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
		return resultado;
	}

	@Get
	@Path("/app/lotacao/isSuspensa")
	public void isSuspensa(String sigla, String matricula) {
		if (matricula != null && !matricula.isEmpty()) {
			DpPessoa pessoa = CpDao.getInstance().getPessoaFromSigla(matricula);
			if (pessoa != null)
				sigla = pessoa.getLotacao().getSiglaCompleta();
		}

		super.aSelecionar(sigla);
		if (getSel() != null) {
			try {
				String isSuspensa = ( (DpLotacao) getSel() ).getIsSuspensa().toString();
				result.use(Results.http()).body(isSuspensa);
			} catch (Exception e) {
				result.use(Results.status()).forbidden(e.getMessage());
			}
		}
	}
	
	@Get("/app/lotacao/exibir")
	public void exibi(String sigla) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		if (sigla == null)
			throw new Exception("sigla deve ser informada.");
		DpLotacao lot = dao().getLotacaoFromSigla(sigla);

		sb.append("digraph G {");

		sb.append("graph [dpi = 60]; node [shape = rectangle];");

		graphAcrescentarLotacao(sb, lot);
		sb.append("}");

		result.include("lotacao", lot);
		result.include("graph", sb.toString());
		
		String substituicao = "false";
		DpLotacao lotacaoDoCadastrante = getCadastrante().getLotacao();
		DpLotacao lotacaoDoTitular = getLotaTitular();
		DpPessoa titular =  getTitular();
		
		if (!getCadastrante().getId().equals(titular.getId())
				|| !lotacaoDoCadastrante.getId().equals(lotacaoDoTitular.getId())) {
			if(podeCadastrarQualquerSubstituicao()){
				substituicao = "true";		
				result.include("itensTitular", buscarSubstitutos(substituicao, titular, lotacaoDoTitular));
			}	
		}
		result.include("isSubstituicao", substituicao);
		result.include("itens", buscarSubstitutos(substituicao, getCadastrante(), lot));
	}	
	
	private List<DpSubstituicao> buscarSubstitutos(String substituicao, DpPessoa pessoa, DpLotacao lotacao) 
			throws SQLException, ServletException {
		
		Boolean isSubstLotacao = false;
		List<DpSubstituicao> todasSubst = new ArrayList<DpSubstituicao>();
		List<DpSubstituicao> substVigentes = new ArrayList<DpSubstituicao>();
		DpSubstituicao dpSubstituicao = new DpSubstituicao();
		dpSubstituicao.setTitular(pessoa);
		dpSubstituicao.setLotaTitular(lotacao);			
	    todasSubst = dao.consultarOrdemData(dpSubstituicao);
	    
	    if (getCadastrante().getId().equals(getTitular().getId())
				&& !getCadastrante().getLotacao().getId().equals(getLotaTitular().getId()))
	    	    	isSubstLotacao = true;	
	    
	    for (DpSubstituicao subst : todasSubst) {	
	    	if (substituicao == "true") {
	    		if (isSubstLotacao && subst.getTitular() != null)
	    			continue;	    		
	    	}
	    		
	    	if (subst.getLotaSubstituto() != null && subst.getLotaSubstituto().isFechada()
	    			&& substituicao == "false")	    		
	    		continue;
	    	if (subst.getSubstituto() != null && (subst.getSubstituto().isFechada() 
	    			|| subst.getSubstituto().isBloqueada()) && substituicao == "false")
	    		continue;
	    	if (subst.isEmVoga() || subst.isFutura()) {
	    		subst.setLotaSubstituto(subst.getLotaSubstituto() != null ? subst.getLotaSubstituto().getLotacaoAtual() : null);
	    		subst.setLotaTitular(subst.getLotaTitular() != null ? subst.getLotaTitular().getLotacaoAtual() : null);
	    		substVigentes.add(subst);	    		
	    	}
	    }
		return substVigentes;
	}

	private boolean podeCadastrarQualquerSubstituicao() throws Exception {
		return Cp.getInstance().getConf().podePorConfiguracao(
				getCadastrante(), getCadastrante().getLotacao(), 
				CpTipoDeConfiguracao.CADASTRAR_QUALQUER_SUBST);
	}
	
	private void graphAcrescentarLotacao(StringBuilder sb, DpLotacao lot) {
		for (DpLotacao lotsub : lot.getDpLotacaoSubordinadosSet()) {
			if (lotsub.getDataFimLotacao() != null)
				continue;
			sb.append("\"" + lot.getSigla() + "\" -> \"" + lotsub.getSigla() + "\";");
			graphAcrescentarLotacao(sb, lotsub);
		}
	}
	

	@Get("app/lotacao/listar")
	public void lista(Integer paramoffset, Long idOrgaoUsu, String nome, Boolean apenasAptasInativacao) throws Exception {
		
		result.include("podeInativarLotacaoLote", Cp.getInstance().getBL().podeInativarLotacaoLote(getTitular(),getLotaCadastrante()));
		
		if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		
		boolean paginaInicial = false;

		if (apenasAptasInativacao == null) {
			apenasAptasInativacao = false;
		}
		
		result.include("maxIndices", 10);
		result.include("itemPagina", 15);
		result.include("quantidadeLista", 15);
		

		
		if (idOrgaoUsu != null) {
			DpLotacaoDaoFiltro dpLotacao = new DpLotacaoDaoFiltro();
			if (paramoffset == null) {
				paramoffset = 0;
			}
			dpLotacao.setIdOrgaoUsu(idOrgaoUsu);
			dpLotacao.setNome(nome);
			dpLotacao.setBuscarFechadas(Boolean.TRUE);
			
			if (apenasAptasInativacao) { //Inativação em Lote
				
				dpLotacao.setBuscarFechadas(Boolean.FALSE);
				List<DpLotacao> consultaLotacaoAtivas = null;
				List<DpLotacao> listaLotacaoAtivas = new ArrayList<DpLotacao>();
				
				if (paramoffset == 0) {
					paginaInicial = true;
					result.include("paginaInicial", paginaInicial);
				}
				
				while (listaLotacaoAtivas.size() < 15) { //Navega nas páginas da query para manter uma listagem próxima à 15 itens por página
					consultaLotacaoAtivas = CpDao.getInstance().consultarPorFiltro(dpLotacao, paramoffset, 15);
					
					if (consultaLotacaoAtivas.isEmpty()) {
						break;
					}
					
					removeLotacoesInaptaParaInativacao(consultaLotacaoAtivas); //mantém aptas na listagem
					listaLotacaoAtivas.addAll(consultaLotacaoAtivas);
					
					paramoffset = paramoffset + 15;
				}
				
				setItens(listaLotacaoAtivas); 
				result.include("maxIndices", 0);
				result.include("itemPagina", 15);
				result.include("quantidadeLista", 30);
				result.include("tamanho",  dao().consultarQuantidade(dpLotacao) );
				
			} else { //Listagem Normal
				setItens(CpDao.getInstance().consultarPorFiltro(dpLotacao, paramoffset, 15)); 
				result.include("tamanho", dao().consultarQuantidade(dpLotacao));
			}

			result.include("itens", getItens());
			
			result.include("idOrgaoUsu", idOrgaoUsu);
			result.include("nome", nome);
			result.include("apenasAptasInativacao", apenasAptasInativacao);

			if (getItens().size() == 0)
				result.include("mensagemPesquisa", "Nenhum resultado encontrado.");
			
			
			//Fim da listagem de itens aptos para inativação em Lote
			if (apenasAptasInativacao && !paginaInicial && getItens().size() == 0) 
				result.include("mensagemPesquisa", "Não há mais registros aptos para inativação.");
		}
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.include("temPermissaoParaExportarDados", temPermissaoParaExportarDados());
	}

	private void removeLotacoesInaptaParaInativacao(List<DpLotacao> listaLotacaoAtivas) throws Exception {
		Iterator<DpLotacao> iterator = listaLotacaoAtivas.iterator();
		while (iterator.hasNext()) {
			DpLotacao lotacao = iterator.next();
			try {
				Cp.getInstance().getBL().podeInativarLotacao(lotacao,getTitular(),getLotaTitular());
			} catch (AplicacaoException ex) {
				iterator.remove();
			}
		}	
	}
	
	@Post
	@Path("app/lotacao/exportarCsv")
	public Download exportarCsv(Long idOrgaoUsu, String nome) throws Exception {

		if (idOrgaoUsu != null) {
			DpLotacaoDaoFiltro dpLotacao = new DpLotacaoDaoFiltro(nome, idOrgaoUsu);

			dpLotacao.setBuscarFechadas(Boolean.TRUE);
			List<DpLotacao> lista = CpDao.getInstance().consultarPorFiltro(dpLotacao, 0, 0);

			if (lista.size() > 0) {
				InputStream inputStream = null;
				StringBuffer texto = new StringBuffer();

				texto.append(SigaMessages.getMessage("usuario.lotacao") + ";");
				texto.append("Sigla;");
				texto.append("Localidade;");
				texto.append(SigaMessages.getMessage("usuario.lotacao") + " Pai;");
				texto.append("Externa;");
				texto.append("Suspensa;");
				texto.append("Ativa/Inativa;");
				texto.append(System.lineSeparator());
				for (DpLotacao lotacao : lista) {
					texto.append(lotacao.getNomeLotacao() + ";");
					texto.append(lotacao.getSiglaLotacao() + ";");
					texto.append((lotacao.getLocalidade() != null ? lotacao.getLocalidadeString() : "") + ";");
					texto.append(
							(lotacao.getLotacaoPai() != null ? lotacao.getLotacaoPai().getSiglaLotacao() : "") + ";");
					texto.append((lotacao.getIsExternaLotacao() == null
							|| ((Integer) 0).equals(lotacao.getIsExternaLotacao()) ? "Não" : "Sim") + ";");
					texto.append(
							(lotacao.getIsSuspensa() == null || ((Integer) 0).equals(lotacao.getIsSuspensa()) ? "Não"
									: "Sim") + ";");
					texto.append((lotacao.getDataFim() == null ? "Ativa" : "Inativa") + ";");
					texto.append(System.lineSeparator());
				}

				inputStream = new ByteArrayInputStream(texto.toString().getBytes("ISO-8859-1"));

				return new InputStreamDownload(inputStream, "text/csv", "unidades.csv");

			} else {

				if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
					result.include("orgaosUsu", dao().listarOrgaosUsuarios());
				} else {
					CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
					List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
					list.add(ou);
					result.include("orgaosUsu", list);
				}
				result.include("idOrgaoUsu", idOrgaoUsu);
				result.include("nome", nome);
				result.include("mensagemPesquisa", "Nenhum resultado encontrado.");
				result.include("temPermissaoParaExportarDados", temPermissaoParaExportarDados());
				result.use(Results.page()).forwardTo("/WEB-INF/page/dpLotacao/lista.jsp");
			}
		}

		return null;
	}
	
	@Get("/app/lotacao/listaLocalidades")
	public void listaLocalidades(Integer idUf) {
		CpUF uf = new CpUF();
		uf.setIdUF(Long.valueOf(idUf));
		result.include("listaLocalidades", dao().consultarLocalidadesPorUF(uf));
	}
	
	@Get("/app/lotacao/editar")
	public void edita(final Long id){
		Long idUf = null;

		List<DpLotacao> listaLotacaoPai = new ArrayList<DpLotacao>();
		if (id != null) {
			DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
			result.include("nmLotacao", lotacao.getDescricao());
			result.include("siglaLotacao", lotacao.getSiglaLotacao());
			result.include("idOrgaoUsu", lotacao.getOrgaoUsuario().getId());
			result.include("nmOrgaousu", lotacao.getOrgaoUsuario().getNmOrgaoUsu());
			result.include("dtFimLotacao", lotacao.getDataFimLotacao());
			result.include("isExternaLotacao", lotacao.getIsExternaLotacao());
			if (lotacao.getLotacaoPai() != null)
				result.include("lotacaoPai", lotacao.getLotacaoPai().getIdLotacao());

			result.include("idLocalidade", lotacao.getLocalidade() != null ? lotacao.getLocalidade().getIdLocalidade() : Long.valueOf(0));
			idUf = lotacao.getLocalidade() != null ? lotacao.getLocalidade().getUF().getId() : Long.valueOf(0);

			List<DpPessoa> list = CpDao.getInstance().pessoasPorLotacao(id, Boolean.TRUE, Boolean.FALSE);
			if (list.size() == 0) {
				result.include("podeAlterarOrgao", Boolean.TRUE);
			}
			listaLotacaoPai = carregaLotacao(lotacao.getOrgaoUsuario());
			listaLotacaoPai.remove(lotacao);
		} else {
			listaLotacaoPai = carregaLotacao(getTitular().getOrgaoUsuario());
			idUf = Long.valueOf(Prop.get("uf.padrao") != null ? Prop.get("uf.padrao") : "0");
		}
		result.include("listaLotacao", listaLotacaoPai);

		if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}		
		
		result.include("listaUF",dao().consultarUF());
		result.include("idUf", idUf);
		CpUF uf = new CpUF();
		uf.setIdUF(Long.valueOf(idUf));
		result.include("listaLocalidades", dao().consultarLocalidadesPorUF(uf));
				
		result.include("request",getRequest());
		result.include("id",id);

	}

	@Transacional
	@Post("/app/lotacao/gravar")
	public void editarGravar(final Long id, final String nmLotacao, final Long idOrgaoUsu, final String siglaLotacao,
			final String situacao, final Boolean isExternaLotacao, final Long lotacaoPai, final Long idLocalidade)
			throws Exception {

		assertAcesso("GI:Módulo de Gestão de Identidade;CAD_LOTACAO: Cadastrar Lotação");

		Cp.getInstance().getBL().criarLotacao(getIdentidadeCadastrante(), getTitular(), getTitular().getLotacao(), id,
				nmLotacao, idOrgaoUsu, siglaLotacao, situacao, isExternaLotacao, lotacaoPai, idLocalidade);
		this.result.redirectTo(this).lista(0, idOrgaoUsu, siglaLotacao,false);
	}
		
	@Get("/app/lotacao/ativar")
	public void ativar(final Long id) throws Exception {
		DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);

		if (Cp.getInstance().getBL().podeAtivarLotacao(lotacao, getTitular(), getLotaTitular())) {
			result.include("lotacao",lotacao);
		} 
	}
	
	@Get("/app/lotacao/inativar")
	public void inativar(final Long id) throws Exception {
		DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
		
		if (Cp.getInstance().getBL().podeInativarLotacao(lotacao, getTitular(),getLotaTitular())) {
			result.include("lotacao",lotacao);
		}	
	}
	
	@Transacional
	@Post("/app/lotacao/inativarLote")
	public void inativarLoteGravar(final List<Long> idLotacoesSelecionadas, final String motivo ) throws Exception {
		
		Long idOrgaoUsuarioParaRedirect = null;
		for (Long id : idLotacoesSelecionadas) {
			DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);

			if (Cp.getInstance().getBL().podeInativarLotacao(lotacao, getTitular(), getLotaTitular())) {

				lotacao.setDataFimLotacao(dao.consultarDataEHoraDoServidor());
				lotacao.setMotivoInativacao(motivo);

				try {
					dao().gravarComHistorico(lotacao, getIdentidadeCadastrante());
				} catch (final Exception e) {
					throw new AplicacaoException("Erro na gravação", 0, e);
				}
			}
			idOrgaoUsuarioParaRedirect = lotacao.getIdOrgaoUsuario();
		}
		this.result.redirectTo(this).lista(0, idOrgaoUsuarioParaRedirect, "",false);
	}
	
	@Transacional
	@Post("/app/lotacao/ativar_gravar")
	public void ativarGravar(final Long id, final String motivo) throws Exception {	
		DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
		
		if (Cp.getInstance().getBL().podeAtivarLotacao(lotacao, getTitular(), getLotaTitular())) {
			DpLotacao lotacaoNova = new DpLotacao();
			lotacao.setDataFimLotacao(null);
			try {
				Cp.getInstance().getBL().copiaLotacao(lotacao, lotacaoNova);
				lotacao.setMotivoAtivacao(motivo);
				
				dao().gravarComHistorico(lotacaoNova, lotacao, null, getIdentidadeCadastrante());
				
				this.result.redirectTo(this).lista(0, lotacao.getIdOrgaoUsuario(), "",false);
			} catch (final Exception e) {
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		}	
		
	}
	
	@Transacional
	@Post("/app/lotacao/inativar_gravar")
	public void inativarGravar(final Long id, final String motivo) throws Exception {
		DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);		

		if (Cp.getInstance().getBL().podeInativarLotacao(lotacao, getTitular(), getLotaTitular())) {

			lotacao.setDataFimLotacao(dao.consultarDataEHoraDoServidor());
			lotacao.setMotivoInativacao(motivo);

			try {
				dao().gravarComHistorico(lotacao, getIdentidadeCadastrante());
				this.result.redirectTo(this).lista(0, lotacao.getIdOrgaoUsuario(), "",false);
			} catch (final Exception e) {
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		}

	}
	
	@Transacional
	@Post("/app/lotacao/suspender")
	public void suspender(final Long id) throws Exception {

		DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
		DpLotacao lotacaoNova = new DpLotacao();
		DpLotacao lotacaoFilhoNova = new DpLotacao();
		DpLotacao lotacaoFilhoAntiga = new DpLotacao();
		Date data = new Date(System.currentTimeMillis());
		DpPessoa pessoaNova = null;
		List<DpPessoa> listPessoa = CpDao.getInstance().pessoasPorLotacao(id, Boolean.TRUE, Boolean.FALSE);

		Cp.getInstance().getBL().copiaLotacao(lotacao, lotacaoNova);

		// ativar
		if (lotacao.getIsSuspensa() != null && lotacao.getIsSuspensa().equals(1)) {
			lotacaoNova.setIsSuspensa(0);
		} else {// suspender
			lotacaoNova.setIsSuspensa(1);
		}

		dao().gravarComHistorico(lotacaoNova, lotacao, null, getIdentidadeCadastrante());

		// movimentar as pessoas e lotacoes filhos para nova lotacao
		for (DpPessoa dpPessoa : listPessoa) {
			pessoaNova = new DpPessoa();
			if (dpPessoa.getLotacao().getIdInicial().equals(lotacaoNova.getIdLotacaoIni())) {
				pessoaNova.setLotacao(lotacaoNova);
			} else {
				if (dpPessoa.getLotacao().getLotacaoPai() != null && lotacaoNova.getId()
						.equals(dpPessoa.getLotacao().getLotacaoAtual().getLotacaoPai().getId())) {
					pessoaNova.setLotacao(dpPessoa.getLotacao().getLotacaoAtual());
				} else {
					// grava nova lotacao filho e setar na pessoa
					lotacaoFilhoNova = new DpLotacao();
					lotacaoFilhoAntiga = dpPessoa.getLotacao().getLotacaoAtual();

					lotacaoFilhoNova.setDataInicio(data);
					Cp.getInstance().getBL().copiaLotacao(lotacaoFilhoAntiga, lotacaoFilhoNova);

					dao().gravarComHistorico(lotacaoFilhoNova, lotacaoFilhoAntiga, data, getIdentidadeCadastrante());
					pessoaNova.setLotacao(lotacaoFilhoNova);
				}
			}
			Cp.getInstance().getBL().copiarPessoa(dpPessoa, pessoaNova);
			dao().gravarComHistorico(pessoaNova, dpPessoa, data, getIdentidadeCadastrante());
		}
		this.result.redirectTo(this).lista(0, null, "",false);
	}

	@Get("/app/lotacao/carregarExcel")
	public void carregarExcel() {
		if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			result.include("nmOrgaousu", getTitular().getOrgaoUsuario().getNmOrgaoUsu());
		}

		result.use(Results.page()).forwardTo("/WEB-INF/page/dpLotacao/cargaLotacao.jsp");
	}

	@Transacional
	@Post("/app/lotacao/carga")
	public Download carga(final UploadedFile arquivo, Long idOrgaoUsu) throws Exception {
		InputStream inputStream = null;
		try {
			String nomeArquivo = arquivo.getFileName();
			String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());

			File file = new File("arq" + extensao);

			file.createNewFile();
			FileUtils.copyInputStreamToFile(arquivo.getFile(), file);

			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			if (idOrgaoUsu != null && !"".equals(idOrgaoUsu)) {
				orgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
			} else {
				orgaoUsuario = getTitular().getOrgaoUsuario();
			}

			CpBL cpbl = new CpBL();
			inputStream = cpbl.uploadLotacao(file, orgaoUsuario, extensao, getIdentidadeCadastrante());
		} catch (Exception e) {
			throw new AplicacaoException("Problemas ao salvar unidades", 0, e);
		}
		if (inputStream == null) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem("Arquivo processado com sucesso!").titulo("Sucesso"));
			carregarExcel();
		} else {
			return new InputStreamDownload(inputStream, "application/text", "inconsistencias.txt");
		}
		return null;

	}

	@Post("/app/lotacao/carregarCombos")
	public void carregarCombos(final Long idOrgaoUsu, String nmLotacao, String siglaLotacao, Long idLocalidade, Long idUf) {
		result.include("request", getRequest());
		result.include("nmLotacao", nmLotacao);
		result.include("siglaLotacao", siglaLotacao);

		
//		if(idUf != null && !Long.valueOf("0").equals(idUf)) {

        CpUF uf = new CpUF();
        uf.setIdUF(Long.valueOf(idUf));
        result.include("listaLocalidades", dao().consultarLocalidadesPorUF(uf));
//		}
        result.include("idLocalidade", idLocalidade);

        result.include("idUf", idUf);
        result.include("listaUF", dao().consultarUF());

        if ("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {

            result.include("orgaosUsu", dao().listarOrgaosUsuarios());
        } else {
            CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
            List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
            list.add(ou);
            result.include("orgaosUsu", list);
        }
        result.include("idOrgaoUsu", idOrgaoUsu);

        CpOrgaoUsuario u = CpDao.getInstance().consultarOrgaoUsuarioPorId(idOrgaoUsu);
        List<DpLotacao> lista = new ArrayList<DpLotacao>(CpDao.getInstance().consultarLotacaoPorOrgao(u));
        result.include("listaLotacao", lista);
        result.use(Results.page()).forwardTo("/WEB-INF/page/dpLotacao/edita.jsp");
    }

    protected List<DpLotacao> carregaLotacao(CpOrgaoUsuario orgaoUsuario) {
        CpOrgaoUsuario u = CpDao.getInstance().consultarOrgaoUsuarioPorId(orgaoUsuario.getIdOrgaoUsu());
        return (List<DpLotacao>) CpDao.getInstance().consultarLotacaoPorOrgao(u);
    }

}
