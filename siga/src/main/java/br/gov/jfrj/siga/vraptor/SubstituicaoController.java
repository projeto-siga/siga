package br.gov.jfrj.siga.vraptor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import com.opensymphony.xwork.Action;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Resource
public class SubstituicaoController extends SigaController {

	public SubstituicaoController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
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
	
	private boolean podeCadastrarQualquerSubstituicao() throws Exception {
		return Cp.getInstance().getConf().podePorConfiguracao(
				getCadastrante(), getCadastrante().getLotacao(), 
				CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST);
	}
}
