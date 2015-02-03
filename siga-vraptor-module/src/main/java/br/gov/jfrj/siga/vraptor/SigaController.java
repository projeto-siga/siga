package br.gov.jfrj.siga.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Paginador;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SigaController {
	public SigaObjects so;

	public Result result;

	private HttpServletRequest request;

	private EntityManager em;
	protected CpDao dao;

	private Paginador p = new Paginador();
		
	private Integer postback;
	
	
	private String mensagemAguarde = null;
	
	//Todo: verificar se após a migração do vraptor se ainda necessita deste atributo "par"
	private Map<String, String[]> par = new HashMap<>();
	
	public Map<String, String[]> getPar() {
		return par;
	}
	
	public void setParam(final String parameterName, final String parameterValue) {
		final String as[] = { parameterValue };
		getPar().put(parameterName, as);
		return;
	}	
	
	public String getUrlEncodedParameters()
			throws UnsupportedEncodingException, IOException {
		if (getPar() != null) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (final String key : getPar().keySet()) {
				final String as[] = getPar().get(key);
				for (final String val : as) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(URLEncoder.encode(key, "utf-8").getBytes());
					baos.write('=');
					baos.write(URLEncoder.encode(val, "utf-8").getBytes());
				}
			}
			return new String(baos.toByteArray());
		}
		return null;
	}
	
	
	
	
	protected CpDao dao() {
		return CpDao.getInstance();
	}

	public Integer getPostback() {
		return postback;
	}
	
	public SigaController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super();
		this.setRequest(request);
		this.dao = dao;
		this.result = result;
		this.so = so;
		this.em = em;

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
	
	protected void setLotaTitular(DpLotacao lotaTitular) {
		// TODO Auto-generated method stub
		so.setLotaTitular(lotaTitular);
	}	

	protected DpPessoa getTitular() {
		// TODO Auto-generated method stub
		return so.getTitular();
	}
	
	protected void setTitular(DpPessoa titular) {
		// TODO Auto-generated method stub
		so.setTitular(titular);
	}
	

	protected DpPessoa getCadastrante() {
		return so.getCadastrante();
	}
	
	protected void setCadastrante(DpPessoa cadastrante) {
		so.setCadastrante(cadastrante);		
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

	public EntityManager em() {
		return this.em;
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
	
	public void assertAcesso(String pathServico) throws AplicacaoException,Exception {
		so.assertAcesso(pathServico);
	}	
	
	public void setP(Paginador p) {
		this.p = p;
	}
	
	public void setPar(final Map par) {
		this.par = par;
	}

	public void setPostback(final Integer postback) {
		this.postback = postback;
	}
	
	public String getMensagemAguarde() {
		return mensagemAguarde;
	}
	
	public void setMensagemAguarde(String mensagemAguarde) {
		this.mensagemAguarde = mensagemAguarde;
	}

	public List<CpOrgaoUsuario> getOrgaosUsu() throws AplicacaoException {
		return dao().listarOrgaosUsuarios();
	}
	
	public Paginador getP() {
		return p;
	}

}
