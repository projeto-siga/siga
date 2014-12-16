package br.gov.jfrj.siga.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.webwork.interceptor.PrincipalAware;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.gov.jfrj.siga.acesso.ConheceUsuario;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SigaController {
	public SigaObjects so;

	public Result result;

	public CpDao dao;

	private HttpServletRequest request;
	
	protected CpDao dao() {
		return CpDao.getInstance();
	}

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

	protected DpLotacao getLotaTitular() {
		// TODO Auto-generated method stub
		return so.getLotaTitular();
	}

	protected DpPessoa getTitular() {
		// TODO Auto-generated method stub
		return so.getTitular();
	}

	protected DpPessoa getCadastrante() {
		// TODO Auto-generated method stub
		return so.getCadastrante();
	}

	protected CpIdentidade getIdentidadeCadastrante() {
		return so.getIdentidadeCadastrante();
	}

	protected String param(final String parameterName) {
		final String[] as = getRequest().getParameterValues(parameterName);
		if (as == null || as[0].equals("null"))
			return null;
		return as[0];
	}

	protected Integer paramInteger(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Integer.parseInt(s);
	}

	protected Long paramLong(final String parameterName) {
		final String s = param(parameterName);
		try {
			return Long.parseLong(s);
		} catch (Throwable t) {
			return null;
		}
	}

	protected Short paramShort(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Short.parseShort(s);
	}

	protected Date paramDate(final String parameterName) {
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

	protected DpPessoa daoPes(long id) {
		return so.daoPes(id);
	}

	protected DpPessoa daoPes(String sigla) {
		return so.daoPes(sigla);
	}

	protected DpLotacao daoLot(long id) {
		return so.daoLot(id);
	}

	protected DpLotacao daoLot(String sigla) {
		return so.daoLot(sigla);
	}

	protected CpOrgaoUsuario daoOU(String sigla) {
		return so.daoOU(sigla);
	}

	protected int redirectToHome() {
		result.redirectTo("/../siga/principal.action");
		return 0;
	}

	protected void setMensagem(String mensagem) {
		result.include("mensagem", mensagem);
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
