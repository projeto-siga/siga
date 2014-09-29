package br.gov.jfrj.siga.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class SigaController {
	public SigaObjects so;

	public Result result;

	public CpDao dao;

	private HttpServletRequest request;

	public SigaController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so) {
		super();
		this.setRequest(request);
		this.so = so;
		this.result = result;
		this.dao = dao;
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
		
		result.include("cadastrante", getCadastrante());
		result.include("lotaCadastrante", getLotaTitular());
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
	}
	
	public void appexception() {
		HttpResult res = this.result.use(http());
		res.setStatusCode(400);
		result.forwardTo("/sigalibs/erroGeral.jsp");
	}

	public void exception() {
		HttpResult res = this.result.use(http());
		res.setStatusCode(500);
		result.forwardTo("/sigalibs/erroGeral.jsp");
	}

	public DpLotacao getLotaTitular() {
		// TODO Auto-generated method stub
		return so.getLotaTitular();
	}

	public DpPessoa getTitular() {
		// TODO Auto-generated method stub
		return so.getTitular();
	}

	public DpPessoa getCadastrante() {
		// TODO Auto-generated method stub
		return so.getCadastrante();
	}
	
	public CpIdentidade getIdentidadeCadastrante() {
		return so.getIdentidadeCadastrante();
	}



	public String param(final String parameterName) {
		final String[] as = getRequest().getParameterValues(parameterName);
		if (as == null || as[0].equals("null"))
			return null;
		return as[0];
	}

	public Integer paramInteger(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Integer.parseInt(s);
	}

	public Long paramLong(final String parameterName) {
		final String s = param(parameterName);		
		try{ 
			return Long.parseLong(s); 
		} catch(Throwable t){ 
			return null; 
		} 
	}

	public Short paramShort(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Short.parseShort(s);
	}

	public Date paramDate(final String parameterName) {
		Date dt = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			dt = df.parse(param(parameterName));
		} catch (final ParseException e) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}
		return dt;
	}
	
	public DpPessoa daoPes(long id) {
		return so.daoPes(id);
	}

	public DpPessoa daoPes(String sigla) {
		return so.daoPes(sigla);
	}

	public DpLotacao daoLot(long id) {
		return so.daoLot(id);
	}

	public DpLotacao daoLot(String sigla) {
		return so.daoLot(sigla);
	}
	
	public CpOrgaoUsuario daoOU(String sigla) {
		return so.daoOU(sigla);
	}
	
	public int redirectToHome() {
		result.redirectTo("../siga/principal.action");
		return 0;
	}
	
	public void setMensagem(String mensagem) {
		result.include("mensagem", mensagem);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
