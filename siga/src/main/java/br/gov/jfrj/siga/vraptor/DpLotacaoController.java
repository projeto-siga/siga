package br.gov.jfrj.siga.vraptor;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.CpLocalidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpUF;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.model.GenericoSelecao;
import br.gov.jfrj.siga.model.Selecionavel;

@Resource
public class DpLotacaoController extends SigaSelecionavelControllerSupport<DpLotacao, DpLotacaoDaoFiltro> {

	private Long orgaoUsu;

	public DpLotacaoController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		
		setSel(new DpPessoa());
		setItemPagina(10);
	}
	
	@Get
	@Post
	@Path({"/app/lotacao/buscar", "/lotacao/buscar.action"})
	public void busca(String sigla, Long idOrgaoUsu, Integer paramoffset, String postback) throws Exception{
		if (postback == null)
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		else
			orgaoUsu = idOrgaoUsu;
		
		this.getP().setOffset(paramoffset);
		
		if (sigla == null)
			sigla = "";
		
		super.aBuscar(sigla, postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("sigla",sigla);
		result.include("postbak",postback);
		result.include("offset",paramoffset);
	}

	@Override
	public DpLotacaoDaoFiltro createDaoFiltro() {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		/*
		 * if (param("postback")==null)
		 * flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		 * else flt.setIdOrgaoUsu(paramInteger("orgaoUsu"));
		 */
		flt.setIdOrgaoUsu(orgaoUsu);
		if (flt.getIdOrgaoUsu() == null && getLotaTitular() != null ) {
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		}
		
		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean
				.valueOf(buscarFechadas) : false);

		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpLotacaoDaoFiltro flt)
			throws AplicacaoException {
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
	@Path({"/public/app/lotacao/selecionar","app/lotacao/selecionar","/lotacao/selecionar.action"})
	public String selecionar(String sigla) {
		String resultado = super.aSelecionar(sigla);
		if (getSel() != null) {
			try {
				/*
				 * Essa condição é necessário porque o retorno do método getSigla para o ExMobil e DpPessoa
				 * são as siglas completas, ex: JFRJ-MEM-2014/00003 e RJ14723. No caso da lotação o getSigla
				 * somente retorna SESIA. No entanto é necessário que o método selecionar retorne a sigla completa, ex:
				 * RJSESIA, pois esse retorno é o parametro de entrada para o método aExibir, que necessita da sigla completa.
				 * */
				DpLotacao lotacao = new DpLotacao();
				lotacao = (DpLotacao) dao().consultar(getSel().getId(), DpLotacao.class, false);
				GenericoSelecao gs = new GenericoSelecao();
				gs.setId(getSel().getId());
				gs.setSigla(lotacao.getSiglaCompleta());
				gs.setDescricao(getSel().getDescricao());
				setSel(gs);
			} catch (final Exception ex) {
				setSel(null);
			}
		}
		
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
		return resultado;
	}
	
	@Get("app/lotacao/exibir")
	public void exibi(String sigla) throws Exception {
		StringBuilder sb = new StringBuilder();
        if(sigla == null) 
        	throw new Exception("sigla deve ser informada.");
    	DpLotacao lot = dao().getLotacaoFromSigla(sigla);
    	
    	sb.append("digraph G {");
    	
    	sb.append("graph [dpi = 60]; node [shape = rectangle];"); 
    	
    	graphAcrescentarLotacao(sb, lot);
    	sb.append("}");
	
		result.include("lotacao", lot);
		result.include("graph", sb.toString());
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
	public void lista(Integer paramoffset, Long idOrgaoUsu, String nome) throws Exception {
		
		if("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		if(idOrgaoUsu != null) {
			DpLotacaoDaoFiltro dpLotacao = new DpLotacaoDaoFiltro();
			if(paramoffset == null) {
				paramoffset = 0;
			}
			dpLotacao.setIdOrgaoUsu(idOrgaoUsu);
			dpLotacao.setNome(Texto.removeAcento(nome));
			dpLotacao.setBuscarFechadas(Boolean.TRUE);
			setItens(CpDao.getInstance().consultarPorFiltro(dpLotacao, paramoffset, 15));
			result.include("itens", getItens());
			result.include("tamanho", dao().consultarQuantidade(dpLotacao));
			
			result.include("idOrgaoUsu", idOrgaoUsu);
			result.include("nome", nome);
		}
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
	}
	
	@Get("/app/lotacao/editar")
	public void edita(final Long id){
		if (id != null) {
			DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
			result.include("nmLotacao",lotacao.getDescricao());
			result.include("siglaLotacao", lotacao.getSiglaLotacao());
			result.include("idOrgaoUsu", lotacao.getOrgaoUsuario().getId());
			result.include("nmOrgaousu", lotacao.getOrgaoUsuario().getNmOrgaoUsu());
			result.include("dtFimLotacao", lotacao.getDataFimLotacao());
			result.include("idLocalidade", lotacao.getLocalidade() != null ? lotacao.getLocalidade().getIdLocalidade() : Long.valueOf(0));
			
			List<DpPessoa> list = dao().getInstance().pessoasPorLotacao(id, Boolean.TRUE, Boolean.FALSE);
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
		
		CpUF uf = new CpUF();
		uf.setIdUF(Long.valueOf(26));
		result.include("listaLocalidades", dao().consultarLocalidadesPorUF(uf));
				
		result.include("request",getRequest());
		result.include("id",id);
	}
	
	@Post("/app/lotacao/gravar")
	public void editarGravar(final Long id, 
							 final String nmLotacao, 
							 final Long idOrgaoUsu,
							 final String siglaLotacao,
							 final String situacao,
							 final Long idLocalidade) throws Exception{
		assertAcesso("GI:Módulo de Gestão de Identidade;CAD_LOTACAO: Cadastrar Lotação");
		
		if(nmLotacao == null)
			throw new AplicacaoException("Nome da lotação não informado");
		
		if(idOrgaoUsu == null)
			throw new AplicacaoException("Órgão não informado");
		
		if(siglaLotacao == null)
			throw new AplicacaoException("Sigla de lotação não informado");
		
		if(Long.valueOf(0).equals(idLocalidade))
			throw new AplicacaoException("Localidade da lotação não informado");
		
		if(nmLotacao != null && !nmLotacao.matches("[a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9.,/-]+")) 
			throw new AplicacaoException("Nome com caracteres não permitidos");
		
		if(siglaLotacao != null && !siglaLotacao.matches("[a-zA-ZçÇ0-9,/-]+")) 
			throw new AplicacaoException("Sigla com caracteres não permitidos");
		
		DpLotacao lotacao;
		lotacao = new DpLotacao();
		lotacao.setSiglaLotacao(siglaLotacao);
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		ou.setIdOrgaoUsu(idOrgaoUsu);
		lotacao.setOrgaoUsuario(ou);
		lotacao = dao().getInstance().consultarPorSigla(lotacao);
		
		if(lotacao != null && lotacao.getId() != null && !lotacao.getId().equals(id)) {
			throw new AplicacaoException("Sigla já cadastrada para outra lotação");
		}
		
		List<DpPessoa> listPessoa = null;
		
		lotacao = new DpLotacao();	
		if (id == null) {
			lotacao = new DpLotacao();
			Date data = new Date(System.currentTimeMillis());
			lotacao.setDataInicio(data);
			
		} else {
			lotacao = dao().consultar(id, DpLotacao.class, false);
			listPessoa = dao().getInstance().pessoasPorLotacao(id, Boolean.TRUE, Boolean.FALSE);
			
		}
		
		//valida se pode inativar lotação
		if(lotacao.getDataFim() == null && "false".equals(situacao)) {
			DpPessoaDaoFiltro pessoaFiltro = new DpPessoaDaoFiltro();
	        pessoaFiltro.setLotacao(lotacao);
	        pessoaFiltro.setNome("");
	        Integer qtdePessoa = dao().consultarQuantidade(pessoaFiltro);
	        Integer qtdeDocumento = dao().consultarQuantidadeDocumentosPorDpLotacao(lotacao);
	               
	        if(qtdePessoa > 0 || qtdeDocumento > 0) {
	        	throw new AplicacaoException("Inativação não permitida. Existem documentos e usuários vinculados nessa Lotação", 0);
	        } else {
	        	Calendar calendar = new GregorianCalendar();
	            Date date = new Date();
	            calendar.setTime(date);
	            lotacao.setDataFimLotacao(calendar.getTime());
	        }
		} else if(lotacao.getDataFim() != null && "true".equals(situacao)){
			lotacao.setDataFimLotacao(null);
		}
		
        	
		lotacao.setNomeLotacao(Texto.removerEspacosExtra(nmLotacao).trim());
		lotacao.setSiglaLotacao(siglaLotacao.toUpperCase());
		
		CpLocalidade localidade = new CpLocalidade();
		localidade.setIdLocalidade(idLocalidade);
		lotacao.setLocalidade(dao().consultarLocalidade(localidade));
		
		if (idOrgaoUsu != null && idOrgaoUsu != 0 && (listPessoa == null || listPessoa.size() == 0)) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);	
			lotacao.setOrgaoUsuario(orgaoUsuario);
		}
		
		try {
			dao().iniciarTransacao();
			dao().gravar(lotacao);
			if(lotacao.getIdLotacaoIni() == null && lotacao.getId() != null) {
				lotacao.setIdLotacaoIni(lotacao.getId());
				lotacao.setIdeLotacao(lotacao.getId().toString());
				dao().gravar(lotacao);
			}
			dao().commitTransacao();			
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		this.result.redirectTo(this).lista(0, null, "");
	}
	
    @Get("/app/lotacao/ativarInativar")
    public void ativarInativar(final Long id) throws Exception{

        DpLotacao lotacao = dao().consultar(id, DpLotacao.class, false);
        
        //ativar
        if(lotacao.getDataFimLotacao() != null && !"".equals(lotacao.getDataFimLotacao())) {
            lotacao.setDataFimLotacao(null);
        } else {//inativar            
            DpPessoaDaoFiltro pessoaFiltro = new DpPessoaDaoFiltro();
            pessoaFiltro.setLotacao(lotacao);
            pessoaFiltro.setNome("");
            Integer qtdePessoa = dao().consultarQuantidade(pessoaFiltro);
            Integer qtdeDocumento = dao().consultarQuantidadeDocumentosPorDpLotacao(lotacao);
                   
            if(qtdePessoa > 0 || qtdeDocumento > 0) {
            	throw new AplicacaoException("Inativação não permitida. Existem documentos e usuários vinculados nessa Lotação", 0);
            } else {
	            Calendar calendar = new GregorianCalendar();
	            Date date = new Date();
	            calendar.setTime(date);
	            lotacao.setDataFimLotacao(calendar.getTime());
	            
	            try {
		              dao().iniciarTransacao();
		              dao().gravar(lotacao);
		              dao().commitTransacao();            
		          } catch (final Exception e) {
		              dao().rollbackTransacao();
		              throw new AplicacaoException("Erro na gravação", 0, e);
		          }
            }
        }
        this.result.redirectTo(this).lista(0, null, "");
    }
    
    @Get("/app/lotacao/carregarExcel")
	public void carregarExcel() {
		if("ZZ".equals(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			result.include("nmOrgaousu", getTitular().getOrgaoUsuario().getNmOrgaoUsu());	
		}
		
		result.use(Results.page()).forwardTo("/WEB-INF/page/dpLotacao/cargaLotacao.jsp");
	}
	
	@Post("/app/lotacao/carga")
	public Download carga( final UploadedFile arquivo, Long idOrgaoUsu) throws Exception {
		InputStream inputStream = null;
		try {
			String nomeArquivo = arquivo.getFileName();
			String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
			
			File file = new File("arq" + extensao);

			file.createNewFile();
			FileUtils.copyInputStreamToFile(arquivo.getFile(), file);
			
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			if(idOrgaoUsu != null && !"".equals(idOrgaoUsu)) {
				orgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
			} else {
				orgaoUsuario = getTitular().getOrgaoUsuario();
			}
			
			CpBL cpbl = new CpBL();
			inputStream = cpbl.uploadLotacao(file, orgaoUsuario, extensao);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		if(inputStream == null) {
			result.include("msg", "Arquivo processado com sucesso!");
			carregarExcel();
		} else {
			result.include("msg", "");
			return new InputStreamDownload(inputStream, "application/text", "inconsistencias.txt");	
		}
		return null;

	}

}
