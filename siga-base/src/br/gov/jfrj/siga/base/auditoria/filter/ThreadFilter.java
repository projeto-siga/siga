package br.gov.jfrj.siga.base.auditoria.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.base.auditoria.hibernate.util.SigaHibernateAuditorLogUtil;

/**
 * Filtro base para implementação dos ThreadFilters 
 * @author bruno.lacerda@avantiprima.com.br
 *
 */
public abstract class ThreadFilter implements Filter {
	
	private static final String ASPAS = "\"";
	private static final String SEPARADOR = ";";
	private boolean isAuditaThreadFilter;
	private static final Logger log = Logger.getLogger( ThreadFilter.class );
	
	public ThreadFilter() {
		this.isAuditaThreadFilter = SigaBaseProperties.getBooleanValue( "audita.thread.filter" );
	}
	
	/**
	 * Marca o momento em que o ThreadFilter iniciou a execução do método doFilter e grava a URL que está sendo executada.
	 * Estes dados serão utilizados para gerar o Log do tempo gastu durante a execução do filtro para a URL em questão.</br>
	 * <b>Obs:</b> Para que funcione, é necessário que a propriedade <i>audita.thread.filter</i> esteja definida como <i>true</i> no arquivo <i>siga.properties</i>.
	 * 
	 * @param request
	 */
	protected StringBuilder iniciaAuditoria(final ServletRequest request) {
		
		StringBuilder csv = null;
		
		if ( this.isAuditaThreadFilter ) {
			
			csv = new StringBuilder();
			
			HttpServletRequest r = (HttpServletRequest) request;
			
			String serverName = r.getServerName();
			String contexto = r.getContextPath();
			String uri = r.getRequestURI();
			String action = uri.substring( uri.indexOf( contexto ) );
			String queryString = r.getQueryString();
			
			csv.append( SEPARADOR )
					.append( ASPAS )
					.append( serverName )
					.append( ASPAS )
					.append( SEPARADOR )
					.append( contexto.substring(1) )
					.append( SEPARADOR )
					.append( ASPAS )
					.append( action.substring(1) )
					.append( ASPAS )
					.append( SEPARADOR );
					
			
			if ( StringUtils.isNotBlank( queryString ) ) {				
				csv.append( ASPAS )
				   .append( queryString )
				   .append( ASPAS );
			} 
			
			
			SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
		}
		
		return csv;
	}
	
	/**
	 * Marca o momento em que o ThreadFilter terminou a execução do método doFilter loga a URL que está sendo executada e o tempo gasto durante o processo.</br>
	 * <b>Obs:</b> Para que funcione, é necessário que a propriedade <i>audita.thread.filter</i> esteja definida como <i>true</i> no arquivo <i>siga.properties</i>.	
	 */
	protected void terminaAuditoria(StringBuilder csv) {
		if ( this.isAuditaThreadFilter && csv != null ) {
			String tempoGasto = SigaHibernateAuditorLogUtil.getTempoGasto();
			log.info( csv.append( SEPARADOR )
							  .append( ASPAS )
							  .append( tempoGasto )
							  .append( ASPAS )
							  .append( SEPARADOR ));
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		log.info("INIT THREAD FILTER");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
	}
	
	public void destroy() {
		log.info("DESTROY THREAD FILTER");
	}	

}
