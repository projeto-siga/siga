package br.gov.jfrj.siga.base.auditoria.hibernate.auditor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.internal.DefaultPostLoadEventListener;
import org.hibernate.event.internal.DefaultPreLoadEventListener;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.SaveOrUpdateEvent;

import br.gov.jfrj.siga.base.auditoria.hibernate.util.SigaHibernateAuditorLogUtil;

/**
 * Classe responsável por configurar o interceptor e os listeners que serão 
 * responsáveis por auditar o tempo decorrido durante as operações de 
 * load, update, insert e delete de cada entidade configurada na
 * configuração do Hibernate. 
 * 
 * @author bruno.lacerda@avantiprima.com.br
 *
 */
public class SigaHibernateChamadaAuditor extends SigaAuditor {

	public static final String HABILITA_AUDITORIA_PROPERTY_NAME = "habilita.auditoria.tempo.chamada.hibernate.por.entidade";
	public static final String HABILITA_AUDITORIA_LISTENERS_PROPERTY_NAME = "habilita.auditoria.chamada.listeners";
	public static final String HABILITA_AUDITORIA_INTERCEPTOR_PROPERTY_NAME = "habilita.auditoria.chamada.interceptor";
	public static final String TEMPO_LIMITE_LOG_LISTENER_PROPERTY_NAME = "auditoria.tempo.limite.duracao.chamada.listener";
	public static final String CATEGORIA_LOG_LISTENER_PROPERTY_NAME = "auditoria.categoria.log.chamada.listeners";
	public static final String CATEGORIA_LOG_INTERCEPTOR_PROPERTY_NAME = "auditoria.categoria.log.chamada.interceptor";
	public static final String CATEGORIA_LOG_TEMPO_LIMITE_ULTRAPASSADO_PROPERTY_NAME = "auditoria.categoria.log.tempo.limite.duracao.chamada.ultrapassado";

	public SigaHibernateChamadaAuditor(Configuration cfg) {
		super( cfg );
	}

	public SigaAuditor configuraListenersDeAuditoria() {
		log.info("##############################################################################");
		log.info("###### ADICIONANDO LISTENERS DE AUDITORIA PARA CHAMADAS DO HIBERNATE ");
		log.info("###### Tempo limite para logar listeners    : " + getTempoLimite( TEMPO_LIMITE_LOG_LISTENER_PROPERTY_NAME ) + "s" );
		log.info("###### Categoria de Log para logar listeners: " + getCategoriaDeLogParaListeners() );
		log.info("##############################################################################");
		
		//TODO 
//		super.cfg.setListener( SigaPreLoadEventListener.TYPE, new SigaPreLoadEventListener() );
//		super.cfg.setListener( SigaPostLoadEventListener.TYPE, new SigaPostLoadEventListener() );
		return this;
	}

	@Override
	public SigaAuditor configuraInterceptorDeAuditoria() {
		log.info("##############################################################################");
		log.info("###### ADICIONANDO INTERCEPTORS DE AUDITORIA PARA CHAMADAS DO HIBERNATE ######");
		log.info("###### Categoria de Log para logar Interceptors: " + getCategoriaDeLogParaInterceptor());
		log.info("##############################################################################");
		super.cfg.setInterceptor( new SigaAuditorInterceptor() );
		return this;
	}

	protected class SigaSaveOrUpdateListener extends DefaultSaveOrUpdateEventListener {
		private static final long serialVersionUID = 1L;
		public static final String TYPE = "save-update";
		@Override
		public void onSaveOrUpdate(SaveOrUpdateEvent event) {
			if ( isAuditoriaHabilitada() ) {
				logaListener( getCategoriaDeLogParaListeners(), TYPE.toUpperCase(), SigaHibernateAuditorLogUtil.extractNameEntity( event.getEntity() ) );
				SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
				super.onSaveOrUpdate( event );
				SigaHibernateAuditorLogUtil.logaTempoGasto( getCategoriaDeLogParaListeners(), getTempoLimiteParaLogarListeners(), getCategoriaLogTempoLimiteUltrapassado() );
			} else {
				super.onSaveOrUpdate( event );
			}
		}
	}

	protected class SigaDeleteEventListener extends DefaultDeleteEventListener {
		private static final long serialVersionUID = 1L;
		public static final String TYPE = "delete";
		@Override
		public void onDelete(DeleteEvent event) throws HibernateException {
			if ( isAuditoriaHabilitada() ) {
				logaListener( getCategoriaDeLogParaListeners(), TYPE.toUpperCase(), event.getEntityName() );
				SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
				super.onDelete( event );
				SigaHibernateAuditorLogUtil.logaTempoGasto( getCategoriaDeLogParaListeners(), getTempoLimiteParaLogarListeners(), getCategoriaLogTempoLimiteUltrapassado() );
			} else {
				super.onDelete( event );
			}
		}
	}

	protected class SigaPreLoadEventListener extends DefaultPreLoadEventListener {
		private static final long serialVersionUID = 1L;
		public static final String TYPE = "pre-load";

		@Override
		public void onPreLoad(PreLoadEvent event) {
			if ( isAuditoriaHabilitada() ) {
				logaListener( getCategoriaDeLogParaListeners(), TYPE.toUpperCase(), SigaHibernateAuditorLogUtil.extractNameEntity( event.getEntity() ) );
				SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
			}
			super.onPreLoad( event );
		}
	}

	protected class SigaPostLoadEventListener extends DefaultPostLoadEventListener{
		private static final long serialVersionUID = 1L;
		public static final String TYPE = "post-load";
		@Override
		public void onPostLoad(PostLoadEvent event) {
			if ( isAuditoriaHabilitada() ) {
				logaListener( getCategoriaDeLogParaListeners(), TYPE.toUpperCase(), SigaHibernateAuditorLogUtil.extractNameEntity( event.getEntity() ) );
				SigaHibernateAuditorLogUtil.logaTempoGasto( getCategoriaDeLogParaListeners(), getTempoLimiteParaLogarListeners(), getCategoriaLogTempoLimiteUltrapassado() );
			}
			super.onPostLoad( event );
		}
	}

	protected class SigaAuditorInterceptor extends EmptyInterceptor{
		private static final long serialVersionUID = 1L;
		@Override
		public String onPrepareStatement(String sql) {
			// Evitando logar execucão do jobExecutor do JBPM que roda a cada 5 segundos
			if ( isAuditoriaHabilitada() && !sql.contains( "select * from ( select job0_.ID" ) ) {
				SigaHibernateAuditorLogUtil.loga( getCategoriaDeLogParaInterceptor(), "[ON PREPARED STATEMENT] " + "SQL: " + sql );
			}
			return super.onPrepareStatement( sql );
		}
	}

	protected void logaListener( String categoriaDeLog, String tipoEvento, String nomeEntidade ){
		SigaHibernateAuditorLogUtil.loga( getCategoriaDeLogParaListeners(), "[" + tipoEvento + "] - [" + nomeEntidade + "]");
	}

	private String getCategoriaLogTempoLimiteUltrapassado() {
		return System.getProperty( CATEGORIA_LOG_TEMPO_LIMITE_ULTRAPASSADO_PROPERTY_NAME );
	}

	@Override
	public boolean isAuditoriaHabilitada() {
		return new Boolean( System.getProperty( HABILITA_AUDITORIA_PROPERTY_NAME ) );
	}

	@Override
	public boolean isListenersAuditoriaHabilitados() {
		return new Boolean( System.getProperty( HABILITA_AUDITORIA_LISTENERS_PROPERTY_NAME ) );
	}

	@Override
	public boolean isInterceptorAuditoriaHabilitado() {
		return new Boolean( System.getProperty( HABILITA_AUDITORIA_INTERCEPTOR_PROPERTY_NAME ) );
	}

	@Override
	public int getTempoLimiteParaLogarListeners() {
		return super.getTempoLimite( TEMPO_LIMITE_LOG_LISTENER_PROPERTY_NAME ); 
	}

	@Override
	public String getCategoriaDeLogParaListeners() {
		return super.getCategoriaDeLog( CATEGORIA_LOG_LISTENER_PROPERTY_NAME ); 
	}

	@Override
	public String getCategoriaDeLogParaInterceptor() {
		return super.getCategoriaDeLog( CATEGORIA_LOG_INTERCEPTOR_PROPERTY_NAME ); 
	}

}
