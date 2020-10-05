package br.gov.jfrj.siga.sr.auditoria.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Configuration;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.sr.prop.SigaBaseProperties;
import br.gov.jfrj.siga.base.auditoria.hibernate.util.SigaHibernateAuditorLogUtil;
import br.gov.jfrj.siga.model.ContextoPersistencia;

/**
 * Filtro base para implementação dos ThreadFilters
 * 
 * @author bruno.lacerda@avantiprima.com.br
 * 
 */
public abstract class ThreadFilter implements Filter {

	private static final String ASPAS = "\"";
	private static final String SEPARADOR = ";";
	private boolean isAuditaThreadFilter;
	private static final Logger log = Logger.getLogger(ThreadFilter.class);

	public ThreadFilter() {
		this.isAuditaThreadFilter = SigaBaseProperties
				.getBooleanValue("audita.thread.filter");
	}

	/**
	 * Marca o momento em que o ThreadFilter iniciou a execução do método
	 * doFilter e grava a URL que está sendo executada. Estes dados serão
	 * utilizados para gerar o Log do tempo gastu durante a execução do filtro
	 * para a URL em questão.</br> <b>Obs:</b> Para que funcione, é necessário
	 * que a propriedade <i>audita.thread.filter</i> esteja definida como
	 * <i>true</i> no arquivo <i>siga.properties</i>.
	 * 
	 * @param request
	 */
	protected StringBuilder iniciaAuditoria(final ServletRequest request) {

		final StringBuilder csv = new StringBuilder();

		if (this.isAuditaThreadFilter) {

			HttpServletRequest r = (HttpServletRequest) request;

			String hostName = this.getHostName();
			String contexto = this.getContexto(r);
			String uri = r.getRequestURI();
			String action = this.getAction(uri, contexto);
			String queryString = r.getQueryString();
			String userPrincipalName = this.getUserPrincipalName(r);

			appendEntreAspas(csv, hostName);
			appendEntreAspas(csv, contexto);
			appendEntreAspas(csv, action);
			appendEntreAspas(csv, queryString);
			appendEntreAspas(csv, userPrincipalName);
			appendEntreAspas(csv, r.getRequestURL());

			SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
		}

		return csv;
	}

	/**
	 * 
	 * @param request
	 * @return Matrícula do Usuário obtida através do método getName da
	 *         implementação da interface Principal
	 */
	protected String getUserPrincipalName(HttpServletRequest request) {
		return ContextoPersistencia.getUserPrincipal() != null ? ContextoPersistencia.getUserPrincipal() : "";
	}

	/**
	 * Marca o momento em que o ThreadFilter terminou a execução do método
	 * doFilter loga a URL que está sendo executada e o tempo gasto durante o
	 * processo.</br> <b>Obs:</b> Para que funcione, é necessário que a
	 * propriedade <i>audita.thread.filter</i> esteja definida como <i>true</i>
	 * no arquivo <i>siga.properties</i>.
	 */
	protected void terminaAuditoria(final StringBuilder csv) {

		if (this.isAuditaThreadFilter && csv != null) {

			String tempoGastoFormatado = SigaHibernateAuditorLogUtil
					.getTempoGastoFormatado();
			long tempoGastoMillisegundos = SigaHibernateAuditorLogUtil
					.getTempoGastoMilliSegundos();

			appendEntreAspas(csv, tempoGastoFormatado);
			appendEntreAspas(csv, tempoGastoMillisegundos);

			log.info(csv);
		}
	}

	/**
	 * Extrai o contexto da aplicação a partir da requisição
	 * 
	 * @param request
	 * @return Contexto da aplicação
	 */
	private String getContexto(HttpServletRequest request) {
		String contexto = request.getContextPath();
		if (StringUtils.isNotBlank(contexto)) {
			contexto = contexto.substring(1);
		}
		return contexto;
	}

	protected String getAction(String uri, String contexto) {
		String action = null;
		if (StringUtils.isNotBlank(uri) && StringUtils.isNotBlank(contexto)) {
			action = uri.replaceFirst(contexto, "");
		}
		return StringUtils.isNotBlank(action) ? action.substring(1) : action;
	}

	protected String getHostName() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn(
					"Não foi possível identificar o nome do Host para adicioná-lo ao Log por CSV",
					e);
			e.printStackTrace();
		}
		return hostName;
	}

	private StringBuilder appendEntreAspas(StringBuilder csv, Object o) {
		return csv.append(SEPARADOR).append(ASPAS).append(o).append(ASPAS);
	}

	/**
	 * Loga como error as exceções que vierem a acontecer durante a execução dos
	 * ThreadFiltesr
	 * 
	 * @param request
	 * @param ex
	 */
	protected void logaExcecaoAoExecutarFiltro(final ServletRequest request,
			final Exception ex) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String url = httpRequest.getRequestURL().toString();
		String queryString = httpRequest.getQueryString() != null ? "?"
				+ httpRequest.getQueryString() : "";
		String principalName = ContextoPersistencia.getUserPrincipal() != null ? ContextoPersistencia.getUserPrincipal() : "convidado";

		String mensagemErro = this.montaMensagemErroExcecoes(ex);

		// Não logar AplicacaoException, pois é erro de lógica de negócio e não falha do sistema
		if (ex instanceof AplicacaoException)
			return;
		if (ex.getCause() != null) {
			if (ex.getCause() instanceof AplicacaoException)
				return;
			if (ex.getCause().getCause() != null
					&& ex.getCause() instanceof AplicacaoException)
				return;
		}

		log.error(mensagemErro + "\nURL: " + url + queryString + "\nUser: "
				+ principalName, ex);
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			doFiltro(request,response,chain);
		} catch (Exception e) {
			throw e;
		}

		
	}
	
	protected abstract void doFiltro(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException;
	
	protected abstract String getLoggerName();

	protected String montaMensagemErroExcecoes(Exception ex) {
		String mensagemErro = "";
		if (ex != null) {
			mensagemErro += ex.getMessage();
			if (ex instanceof AplicacaoException && ex.getCause() != null) {
				mensagemErro += " Causa: " + ex.getCause().getMessage();
			}
		}

		return mensagemErro;
	}

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}
