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
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
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
	public void busca(String sigla, Long idOrgaoUsu, Integer offset, String postback) throws Exception{
		if (postback == null)
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		else
			orgaoUsu = idOrgaoUsu;
		
		this.getP().setOffset(offset);
		
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
		result.include("offset",offset);
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
}
