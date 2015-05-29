package br.gov.jfrj.siga.base.auditoria.hibernate.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;

import org.jboss.logging.Logger;

public class SigaHibernateAuditorLogUtil {
	
	private static final Logger log = Logger.getLogger( "[AUDITORIA]" );
	
	private static long tempoInicial;
	private static long tempoFinal;
	private static long tempoGasto;
	private static long tempoGastoMilliSegundos;
	
	private static Boolean ultrapassouLimiteEstipulado;
	private static String categoriaLogada;
	
	public static String extractNameEntity(Object entity) {
		
		String nomeEntidade = null;
		if ( entity != null ) {
			String canonicalName = entity.getClass().getCanonicalName();
			int idxNomeEntidade = canonicalName.lastIndexOf( "." );
			nomeEntidade = canonicalName.substring( idxNomeEntidade + 1 );
		}
		
		return nomeEntidade;
	}
	
	public static void iniciaMarcacaoDeTempoGasto() {
		tempoInicial = System.currentTimeMillis();	
		ultrapassouLimiteEstipulado = false;
	}

	public static void logaTempoGasto(String categoriaDeLog, int tempoLimiteParaLogarEmSegundos, String categoriaLogTempoLimiteUltrapassado) {
		
		tempoFinal = System.currentTimeMillis();
		
		if ( tempoInicial != 0 ) { 
			tempoGasto = ( tempoFinal - tempoInicial ) / 1000;
			
			if ( tempoLimiteParaLogarEmSegundos <= tempoGasto ) {
				
				ultrapassouLimiteEstipulado = true;
				
				long totalDeMinutos = obtemTotalDeMinutos( tempoGasto );                             // 00:80:00
				
				long segundosRestantes = obtemSegundosQueNaoFormamUmMinuto( tempoGasto );            // 00:00:45 <--
				long minutosQueNaoFormamUmaHora = obtemMinutosQueNaoFormamUmaHora( totalDeMinutos ); // 00:20:00 <--
				long totalDeHoras = converteTotalDeMinutosEmHoras( totalDeMinutos );                 // 01:00:00 <--
				
				String tempoGasto = formataPadraoDeHorasMinutosESegundos( totalDeHoras, minutosQueNaoFormamUmaHora, segundosRestantes ); // 01:20:40s
				
				loga( categoriaLogTempoLimiteUltrapassado, "[TEMPO GASTO] - [" + tempoGasto + "]" );
				
				categoriaLogada = categoriaDeLog;
				
				tempoInicial = 0;
			} else {
				loga( categoriaDeLog, "O tempo limite estipulado de " + tempoLimiteParaLogarEmSegundos + " segundos não foi atingido." );
			}
		}
	}

	private static long converteTotalDeMinutosEmHoras(long minutos) {
		long hora = minutos / 60;
		return hora;
	}

	private static long obtemMinutosQueNaoFormamUmaHora(long minutos) {
		long minuto = minutos % 60;
		return minuto;
	}

	private static long obtemTotalDeMinutos( long millis ) {
		return millis / 60;
	}

	private static long obtemSegundosQueNaoFormamUmMinuto(long millis) {
		return millis % 60;
	}

	private static String formataPadraoDeHorasMinutosESegundos(long hora, long minuto, long segundo ) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		String tempoGasto = nf.format(hora) + ":" + nf.format(minuto) + ":" + nf.format(segundo);
		return tempoGasto;
	}
	
	/**
	 * 
	 * @param categoriaDeLogEnum: Categoria de Log a ser utilizada para gerar o Log.
	 * @param mensagem: mensagem a ser exibida no Log.
	 * @return
	 */
	public static String loga(String categoriaDeLog, String mensagem) {
		
		Method[] methods = log.getClass().getMethods();
		
		for ( Method m : methods ) {
			if ( m.getName().equals( categoriaDeLog.toLowerCase() ) ) {
				m.setAccessible( true );
				Object [] msg = { mensagem };
				try {
					m.invoke(log, msg);
					break;
				} catch (IllegalArgumentException e) {
					// log.error("Ocorreu um erro no log durante a auditoria.", e.getCause());
				} catch (IllegalAccessException e) {
					log.error("Ocorreu um erro no log durante a auditoria.", e.getCause());
				} catch (InvocationTargetException e) {
					log.error("Ocorreu um erro no log durante a auditoria.", e.getCause());
				}
			}
		}
		return categoriaDeLog;
	}
	
	/**
	 * Método criado somente para propósito de testes.
	 * @return true caso o limite de tempo estipulado para logar tenha sido atingido.
	 */
	protected static Boolean auditou() {
		return ultrapassouLimiteEstipulado;
	}
	
	/**
	 * Método criado somente para propósito de testes.
	 */
	protected static long getTempoInicial() {
		return tempoInicial;
	}

	/**
	 * Método criado somente para propósito de testes.
	 */
	protected static long getTempoFinal() {
		return tempoFinal;
	}

	protected static Object getCategoriaLogada() {
		return categoriaLogada;
	}

	/**
	 * 
	 * @return Tempo gasto formatado
	 * 
	 */
	public static String getTempoGastoFormatado() {
		
		tempoFinal = System.currentTimeMillis();
		
		if ( tempoInicial != 0 ) { 
			tempoGastoMilliSegundos = tempoFinal - tempoInicial;
			tempoGasto = tempoGastoMilliSegundos / 1000;
		}	
		
		long totalDeMinutos = obtemTotalDeMinutos( tempoGasto );                             // 00:80:00
		
		long segundosRestantes = obtemSegundosQueNaoFormamUmMinuto( tempoGasto );            // 00:00:45 <--
		long minutosQueNaoFormamUmaHora = obtemMinutosQueNaoFormamUmaHora( totalDeMinutos ); // 00:20:00 <--
		long totalDeHoras = converteTotalDeMinutosEmHoras( totalDeMinutos );                 // 01:00:00 <--
		
		String tempoGastoFormatado = formataPadraoDeHorasMinutosESegundos( totalDeHoras, minutosQueNaoFormamUmaHora, segundosRestantes ); // 01:20:40s
		
		return tempoGastoFormatado;
	}

	public static long getTempoGastoMilliSegundos() {
		return tempoGastoMilliSegundos;
	}

}
