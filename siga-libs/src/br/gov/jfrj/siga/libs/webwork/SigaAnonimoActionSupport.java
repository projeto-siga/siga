/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.libs.webwork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.libs.util.Paginador;

import com.opensymphony.webwork.interceptor.ParameterAware;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.opensymphony.webwork.util.ServletContextAware;
import com.opensymphony.xwork.ActionSupport;

public class SigaAnonimoActionSupport extends ActionSupport implements
		ParameterAware, ServletRequestAware, ServletContextAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4186578355554697523L;

	private static Log log = LogFactory.getLog(SigaAnonimoActionSupport.class);

	public static Log getLog() {
		return SigaAnonimoActionSupport.log;
	}

	private Paginador p = new Paginador();

	private Map<String, String[]> par;

	private Integer postback;

	private HttpServletRequest request;
	private HttpServletResponse response;

	private ServletContext context;

	private String mensagemAguarde = null;

	protected CpDao dao() {
		return CpDao.getInstance();
	}

	public DpPessoa daoPes(long id) {
		return dao().consultar(id, DpPessoa.class, false);
	}

	public DpPessoa daoPes(String sigla) {
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setSigla(sigla);
		return (DpPessoa) dao().consultarPorSigla(flt);
	}

	public DpLotacao daoLot(long id) {
		return dao().consultar(id, DpLotacao.class, false);
	}

	public DpLotacao daoLot(String sigla) {
		DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setSiglaCompleta(sigla);
		return (DpLotacao) dao().consultarPorSigla(flt);
	}

	// public Usuario daoUsu(String matricula) {
	// return dao().consultar(matricula, Usuario.class, false);
	// }

	protected void carregaPerfil() throws Exception {
	}

	public Paginador getP() {
		return p;
	}

	public Map<String, String[]> getPar() {
		return par;
	}

	public Integer getPostback() {
		return postback;
	}

	public HttpServletRequest getRequest() {
		return request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public String getDtAtualDDMMYYHHMMSS() {
		final Date dt = new Date();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		return df.format(dt);
	}

	public List<CpOrgaoUsuario> getOrgaosUsu() throws AplicacaoException {
		return dao().listarTodos(CpOrgaoUsuario.class);
	}

	public String param(final String parameterName) {
		final String[] as = getPar().get(parameterName);
		if (as == null || as[0].equals("null"))
			return null;
		return as[0];
	}

	public Integer paramInteger(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals("") || !StringUtils.isNumeric( s ))
			return null;
		return Integer.parseInt(s);
	}

	public Long paramLong(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals("") || !StringUtils.isNumeric( s ))
			return null;
		return Long.parseLong(s);
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

	public void setP(final Paginador p) {
		this.p = p;
	}

	public void setPar(final Map par) {
		this.par = par;
	}

	public void setParam(final String parameterName, final String parameterValue) {
		final String as[] = { parameterValue };
		getPar().put(parameterName, as);
		return;
	}

	public void setParameters(final Map parameters) {
		this.par = parameters;

	}

	public void setPostback(final Integer postback) {
		this.postback = postback;
	}

	public void setRequest(final HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}
	
	
	public void setServletResponse(final HttpServletResponse response) {
		this.response = response;
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

	public String getMensagemAguarde() {
		return mensagemAguarde;
	}

	public void setMensagemAguarde(String mensagemAguarde) {
		this.mensagemAguarde = mensagemAguarde;
	}

	public long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	public void setServletContext(ServletContext context) {
		this.context = context;
		try {
			carregaPerfil();
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public ServletContext getContext() {
		return context;
	}

	public void setSharedContextAttribute(String name, Object attr) {
		ServletContext sigaContext = getContext().getContext("/siga");
		if (sigaContext == null)
			return;
		String sessionId = getRequest().getSession().getId();
		sigaContext.setAttribute(sessionId + "-" + name, attr);
	}

	public Object getSharedContextAttribute(String name) {
		ServletContext sigaContext = getContext().getContext("/siga");
		if (sigaContext == null)
			return null;
		String sessionId = getRequest().getSession().getId();
		return sigaContext.getAttribute(sessionId + "-" + name);
	}

	public void assertAcesso(String pathServico) throws AplicacaoException,
			Exception {
	}

}
