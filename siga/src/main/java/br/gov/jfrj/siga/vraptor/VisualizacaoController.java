package br.gov.jfrj.siga.vraptor;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class VisualizacaoController extends SigaController {
	private String dtIniDeleg;
	private String dtFimDeleg;	
	private DpPessoaSelecao titularSel;
	private DpPessoaSelecao delegadoSel;
	

	/**
	 * @deprecated CDI eyes only
	 */
	public VisualizacaoController() {
		super();
	}

	@Inject
	public VisualizacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		titularSel = new DpPessoaSelecao();	
		
		delegadoSel = new DpPessoaSelecao();		
		
	}
	
	private List<DpVisualizacao> buscarVisualizacoes(DpPessoa pessoa) 
			throws SQLException, AplicacaoException {
		List<DpVisualizacao> todasVis = new ArrayList<DpVisualizacao>();
		List<DpVisualizacao> visVigentes = new ArrayList<DpVisualizacao>();
		DpVisualizacao dpVisualizacao = new DpVisualizacao();
		dpVisualizacao.setTitular(pessoa);
	    todasVis = dao.consultarOrdemData(dpVisualizacao);
	    
	    for (DpVisualizacao vis : todasVis) {	

	    	if (vis.getDelegado() != null && (vis.getDelegado().isFechada() 
	    			|| vis.getDelegado().isBloqueada()))
	    		continue;
	    	if (vis.isEmVoga() || vis.isFutura()) {
	    		visVigentes.add(vis);	    		
	    	}
	    }
		return visVigentes;
	}
		
	@Get("/app/visualizacao/listar")
	public void lista() throws Exception {
		result.include("itens", buscarVisualizacoes(getCadastrante()));
	}	
	
	@Get("/app/visualizacao/editar")
	public void edita(Long id) throws Exception {
		String buscarFechadas = "buscarFechadas=false";
		result.include("strBuscarFechadas", buscarFechadas);
		
		if (id != null) {
			DpVisualizacao visualizacao = dao().consultar(id, DpVisualizacao.class, false);

			titularSel.buscarPorObjeto(visualizacao.getTitular());
			delegadoSel.buscarPorObjeto(visualizacao.getDelegado());
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				dtFimDeleg = df.format(visualizacao.getDtFimDeleg());
			} catch (final Exception e) {
			}

			try {
				dtIniDeleg = df.format(visualizacao.getDtIniDeleg());
			} catch (final Exception e) {
			}
			result.include("visualizacao", visualizacao);
			result.include("dtIniDeleg", dtIniDeleg);
			result.include("dtFimDeleg", dtFimDeleg);				
		}
		
		//Dados do Titular
		result.include("titularSel", getCadastrante());//tipoTitular=1
		
		//Dados do Substituto
		result.include("delegadoSel", delegadoSel);//tipoSubstituto=1
	}
	
	@Post("/app/visualizacao/gravar")
	@Transacional
	public void gravar(DpVisualizacao visualizacao
			          ,DpPessoaSelecao delegadoSel
				      ,String dtIniDeleg
				      ,String dtFimDeleg				          
					  ) throws Exception {

		DpVisualizacao vis = new DpVisualizacao();

		this.titularSel = titularSel;//tipoTitular=1
		
		this.delegadoSel = delegadoSel;//tipoSubstituto=1
		
		try {
			dao().iniciarTransacao();
			
			vis.setTitular(getCadastrante());
				
			if (this.delegadoSel.getId() == null)
				throw new AplicacaoException("Delegado não informado"); 
			
			DpPessoa delegado = new DpPessoa();
			delegado = daoPes(this.delegadoSel.getId());
			vis.setDelegado(delegado);
			
			if(!delegado.getOrgaoUsuario().getId().equals(getCadastrante().getOrgaoUsuario().getId())) {
				throw new AplicacaoException("Delegado e titular devem pertencer ao mesmo órgão"); 
			}

			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				vis.setDtIniDeleg(df.parse(dtIniDeleg + " 00:00"));
			} catch (final ParseException e) {
				vis.setDtIniDeleg(null);
			} catch (final NullPointerException e) {
				vis.setDtIniDeleg(null);
			}
			try {
				vis.setDtFimDeleg(df.parse(dtFimDeleg + " 23:59"));
			} catch (final ParseException e) {
				vis.setDtFimDeleg(null);
			} catch (final NullPointerException e) {
				vis.setDtFimDeleg(null);
			}
			
			if (vis.getDtIniDeleg() == null)
				vis.setDtIniDeleg(new Date());
			else {
				if (!Data.dataDentroSeculo21(vis.getDtIniDeleg()))
					throw new AplicacaoException("Data inicial inválida, deve estar entre o ano 2000 e ano 2100");
			}
				
			if(vis.getDtFimDeleg() != null && !Data.dataDentroSeculo21(vis.getDtFimDeleg()))
				throw new AplicacaoException("Data final inválida, deve estar entre o ano 2000 e ano 2100");	

			vis.setDtIniRegistro(new Date());

			if (visualizacao != null && visualizacao.getIdVisualizacao() != null) {
				DpVisualizacao visAntiga = dao().consultar(visualizacao.getIdVisualizacao(), DpVisualizacao.class, false);
				visAntiga.setDtFimRegistro(new Date());
				visAntiga = dao().gravar(visAntiga);
				vis.setIdRegistroInicial(visAntiga.getIdRegistroInicial());
			}

			vis = dao().gravar(vis);

			if (vis.getIdRegistroInicial() == null)
				vis.setIdRegistroInicial(vis.getIdVisualizacao());

			vis = dao().gravar(vis);
			result.redirectTo(this).lista();

			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Gravar", 0, e);			
		}

	}
	
	@Get("/app/visualizacao/visualizacaoGravar")
	public void visualizacaoGravar(Long idVisualizacao) throws Exception {
		if (idVisualizacao == null)
			throw new AplicacaoException("Dados não informados");
			
		result.redirectTo(Contexto.urlBase(request) + "/sigaex/app/mesa" 
				+ Prop.get("/siga.mesa.versao") + "?idVisualizacao="+idVisualizacao);
	}	
	
	
	@Get("/app/visualizacao/exclui")
	@Transacional
	public void exclui(Long id) throws Exception {
		
		try{
		
		if (id != null) {
			DpVisualizacao dpVis = dao().consultar(id, DpVisualizacao.class, false);
			
			if ((dpVis.getDelegado() != null && dpVis.getDelegado().equivale(getCadastrante()))					
					||(dpVis.getTitular() != null && dpVis.getTitular().equivale(getCadastrante()))) {
				dao().iniciarTransacao();		
				dpVis.setDtFimRegistro(new Date());
				dpVis = dao().gravar(dpVis);
				dao().commitTransacao();
				String referer = getRequest().getHeader("Referer");
				if (referer != null)
					result.redirectTo(referer);
				else
					result.redirectTo(PrincipalController.class).principal(false, null);
			} else
				throw new AplicacaoException("Usuário não tem permissão para excluir esta delegação");	
		} else
			throw new AplicacaoException("Não foi informado id");
		
		}catch (Exception e) {
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Excluir", 0, e);
		}
		
	}	

}