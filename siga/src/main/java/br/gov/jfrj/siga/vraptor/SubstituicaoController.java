package br.gov.jfrj.siga.vraptor;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class SubstituicaoController extends SigaController {
	
	private Integer tipoTitular;
	private Integer tipoSubstituto;
	private String dtIniSubst;
	private String dtFimSubst;	
	private DpPessoaSelecao titularSel;
	private DpLotacaoSelecao lotaTitularSel;
	private DpPessoaSelecao substitutoSel;
	private DpLotacaoSelecao lotaSubstitutoSel;	
	
	private Map<Integer, String> getListaTipo() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}	
		

	public SubstituicaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();

		titularSel = new DpPessoaSelecao();	
		lotaTitularSel = new DpLotacaoSelecao();
		
		lotaSubstitutoSel = new DpLotacaoSelecao();
		substitutoSel = new DpPessoaSelecao();		
		
		tipoTitular = 1;
		tipoSubstituto = 1;		
	}
	
	private boolean podeCadastrarQualquerSubstituicao() throws Exception {
		return Cp.getInstance().getConf().podePorConfiguracao(
				getCadastrante(), getCadastrante().getLotacao(), 
				CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST);
	}
	
	private List<DpSubstituicao> buscarSubstitutos(String substituicao, DpPessoa pessoa, DpLotacao lotacao) 
			throws SQLException, AplicacaoException {
		
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
	    		substVigentes.add(subst);	    		
	    	}
	    }
		return substVigentes;
	}
		
	@Get("/app/substituicao/listar")
	public void lista() throws Exception {
		String substituicao = "false";
		
		if (!getCadastrante().getId().equals(getTitular().getId())
				|| !getCadastrante().getLotacao().getId().equals(getLotaTitular().getId())) {
			if(podeCadastrarQualquerSubstituicao()){
				substituicao = "true";					
				result.include("itensTitular", buscarSubstitutos(substituicao, getTitular(), getLotaTitular()));
			}		
		}			
		result.include("isSubstituicao", substituicao);
		result.include("itens", buscarSubstitutos(substituicao, getCadastrante(), getCadastrante().getLotacao()));
	}	
	
	@Get("/app/substituicao/editar")
	public void edita(Long id) throws Exception {
		String buscarFechadas = "buscarFechadas="+podeCadastrarQualquerSubstituicao();
		result.include("strBuscarFechadas", buscarFechadas);
		
		if (id != null) {
			DpSubstituicao subst = dao().consultar(id, DpSubstituicao.class, false);

			if (subst.getTitular() == null) {
				tipoTitular = 2;
				lotaTitularSel.buscarPorObjeto(subst.getLotaTitular());
				
			} else {
				tipoTitular = 1;
				titularSel.buscarPorObjeto(subst.getTitular());
			}
			
			if (subst.getSubstituto() == null) {
				tipoSubstituto = 2;
				lotaSubstitutoSel.buscarPorObjeto(subst.getLotaSubstituto());
				
			} else {
				tipoSubstituto = 1;
				substitutoSel.buscarPorObjeto(subst.getSubstituto());
				
			}
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				dtFimSubst = df.format(subst.getDtFimSubst());
			} catch (final Exception e) {
			}

			try {
				dtIniSubst = df.format(subst.getDtIniSubst());
			} catch (final Exception e) {
			}
			result.include("substituicao", subst);
			result.include("dtIniSubst", dtIniSubst);
			result.include("dtFimSubst", dtFimSubst);				
		}
		
		//Dados do Titular
		result.include("listaTipoTitular", getListaTipo());
		result.include("tipoTitular", tipoTitular);
		result.include("titularSel", titularSel);//tipoTitular=1
		result.include("lotaTitularSel", lotaTitularSel);//tipoTitular=2
		
		//Dados do Substituto
		result.include("listaTipoSubstituto", getListaTipo());
		result.include("tipoSubstituto", tipoSubstituto);
		result.include("substitutoSel", substitutoSel);//tipoSubstituto=1
		result.include("lotaSubstitutoSel", lotaSubstitutoSel);//tipoSubstituto=2
	}
	
	@Post("/app/substituicao/gravar")
	public void gravar(DpSubstituicao substituicao
					  ,Integer tipoTitular
					  ,Integer tipoSubstituto			
			          ,DpPessoaSelecao titularSel
			          ,DpLotacaoSelecao lotaTitularSel
			          
			          ,DpPessoaSelecao substitutoSel
			          ,DpLotacaoSelecao lotaSubstitutoSel
				      ,String dtIniSubst
				      ,String dtFimSubst				          
					  ) throws Exception {
		
		
		

		DpSubstituicao subst = new DpSubstituicao();
		
		this.tipoTitular = tipoTitular;
		this.titularSel = titularSel;//tipoTitular=1
		this.lotaTitularSel = lotaTitularSel;//tipoTitular=2
		
		this.tipoSubstituto = tipoSubstituto;
		this.substitutoSel = substitutoSel;//tipoSubstituto=1
		this.lotaSubstitutoSel = lotaSubstitutoSel;//tipoSubstituto=2
		
		try {
			dao().iniciarTransacao();
			if (tipoTitular == 1) {
				if (this.titularSel.getId() == null)
					throw new AplicacaoException("Titular não informado");
				
				subst.setTitular(dao().consultar(this.titularSel.getId(),DpPessoa.class, false));
				
				if (!subst.getTitular().getIdPessoa().equals(getCadastrante().getIdPessoa())  
						&& !podeCadastrarQualquerSubstituicao())
					throw new AplicacaoException("Titular não permitido. Apenas o próprio usuário pode se definir como titular.");
				
				subst.setLotaTitular(subst.getTitular().getLotacao());
			} else {
				subst.setTitular(null);
				if (this.lotaTitularSel.getId() == null)
					throw new AplicacaoException("A lotação titular não foi informada");
				
				subst.setLotaTitular(dao().consultar(this.lotaTitularSel.getId(), DpLotacao.class, false));
				
				if (!subst.getLotaTitular().getIdLotacao().equals(getCadastrante().getIdLotacao()) 
						&& !podeCadastrarQualquerSubstituicao())
					throw new AplicacaoException("Lotação titular não permitida. Apenas um usuário da própria lotação pode defini-la como titular.");
			}
			if (tipoSubstituto == 1) {
				if (this.substitutoSel.getId() == null)
					throw new AplicacaoException("Substituto não informado");
				
				subst.setSubstituto(daoPes(this.substitutoSel.getId()));
				subst.setLotaSubstituto(subst.getSubstituto().getLotacao());
			} else {
				subst.setSubstituto(null);
				if (this.lotaSubstitutoSel.getId() == null)
					throw new AplicacaoException("A lotação do substituto não foi informada");
				
				subst.setLotaSubstituto(daoLot(this.lotaSubstitutoSel.getId()));
			}
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				subst.setDtIniSubst(df.parse(dtIniSubst + " 00:00"));
			} catch (final ParseException e) {
				subst.setDtIniSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtIniSubst(null);
			}
			try {
				subst.setDtFimSubst(df.parse(dtFimSubst + " 23:59"));
			} catch (final ParseException e) {
				subst.setDtFimSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtFimSubst(null);
			}

			if (subst.getDtIniSubst() == null)
				subst.setDtIniSubst(new Date());

			subst.setDtIniRegistro(new Date());

			if (substituicao != null && substituicao.getIdSubstituicao() != null) {
				DpSubstituicao substAntiga = dao().consultar(substituicao.getIdSubstituicao(), DpSubstituicao.class, false);
				substAntiga.setDtFimRegistro(new Date());
				substAntiga = dao().gravar(substAntiga);
				subst.setIdRegistroInicial(substAntiga.getIdRegistroInicial());
			}

			subst = dao().gravar(subst);

			if (subst.getIdRegistroInicial() == null)
				subst.setIdRegistroInicial(subst.getIdSubstituicao());

			subst = dao().gravar(subst);
			result.redirectTo(this).lista();

			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Gravar", 0, e);			
		}

	}
	
	@Get("/app/substituicao/finalizar")
	public void finalizar() throws Exception {
		try {
			dao().iniciarTransacao();
			gravarFinalizar();
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
		}
		String referer = getRequest().getHeader("Referer");
		if (referer != null)
			result.redirectTo(referer);
		else
			result.redirectTo(PrincipalController.class).principal();
	}	
	
	private void gravarFinalizar() {
		CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());

		if (per == null) {
			per = new CpPersonalizacao();
			per.setPessoa(getCadastrante());
		}

		per.setPesSubstituindo(null);
		per.setLotaSubstituindo(null);
		dao().gravar(per);
	}
	
	@Get("/app/substituicao/substituirGravar")
	public void substituirGravar(Long idTitular, Long idLotaTitular) throws Exception {
		try {
			dao().iniciarTransacao();
			gravarFinalizar();
			
			if (idTitular != null) {
				setTitular(daoPes(idTitular));
				setTitular(dao().consultarPorIdInicial(getTitular().getIdInicial()));
				setLotaTitular(getTitular().getLotacao());
				setLotaTitular(dao().consultarPorIdInicial(DpLotacao.class,getTitular().getLotacao().getIdInicial()));
			} else {
				setLotaTitular(daoLot(idLotaTitular));
				setLotaTitular(dao().consultarPorIdInicialInclusiveLotacaoFechada(DpLotacao.class, getLotaTitular().getIdInicial()));
			}
	
			CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());
			if (per == null) {
				per = new CpPersonalizacao();
			}
			per.setPessoa(getCadastrante());
			per.setPesSubstituindo(getTitular() != getCadastrante() ? getTitular(): null);
			per.setLotaSubstituindo(getLotaTitular() != getCadastrante().getLotacao() ? getLotaTitular() : null);
		
			dao().gravar(per);
			dao().commitTransacao();
//			result.use(Results.referer()).redirect();
		} catch (Exception e) {
			e.printStackTrace();
			dao().rollbackTransacao();
		}
		String referer = getRequest().getHeader("Referer");
		if (referer != null)
			result.redirectTo(referer);
		else
			result.redirectTo(PrincipalController.class).principal();
	}	
	
	public void exclui(Long id) throws Exception {
		
		try{
		
		if (id != null) {
			DpSubstituicao dpSub = dao().consultar(id, DpSubstituicao.class, false);
			
			if ((dpSub.getSubstituto() != null && dpSub.getSubstituto().equivale(getCadastrante()))					
				|| (dpSub.getSubstituto() == null && dpSub.getLotaSubstituto().equivale(getCadastrante().getLotacao()))
				||(dpSub.getTitular() != null && dpSub.getTitular().equivale(getCadastrante()))					
				|| (dpSub.getTitular() == null && dpSub.getLotaTitular().equivale(getCadastrante().getLotacao()))
				|| podeCadastrarQualquerSubstituicao()) {
				dao().iniciarTransacao();		
				dpSub.setDtFimRegistro(new Date());
				dpSub = dao().gravar(dpSub);
				dao().commitTransacao();
				String referer = getRequest().getHeader("Referer");
				if (referer != null)
					result.redirectTo(referer);
				else
					result.redirectTo(PrincipalController.class).principal();
			} else
				throw new AplicacaoException("Usuário não tem permissão para excluir esta substituição");	
		} else
			throw new AplicacaoException("Não foi informado id");
		
		}catch (Exception e) {
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Excluir", 0, e);
		}
		
	}	

}
