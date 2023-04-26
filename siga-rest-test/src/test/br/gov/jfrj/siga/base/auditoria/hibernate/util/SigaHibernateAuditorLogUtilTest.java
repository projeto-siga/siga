package br.gov.jfrj.siga.base.auditoria.hibernate.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.gov.jfrj.siga.base.auditoria.hibernate.enums.SigaLogCategoryEnum;


public class SigaHibernateAuditorLogUtilTest {
	
	@Test
	public void deveLogarSomenteSeOTempoDeEsperaUltrapasarOLimite() throws Exception {
		
		SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
		Thread.sleep( 2000 ); //2 segundos
		SigaHibernateAuditorLogUtil.logaTempoGasto( SigaLogCategoryEnum.WARN.getValue(), 1, SigaLogCategoryEnum.ERROR.getValue() );
		
		assertTrue( SigaHibernateAuditorLogUtil.auditou() );
	}
	
	@Test
	public void deveLogarNaCategoriaDeLogRecebidaComoParametro() throws Exception {

		SigaHibernateAuditorLogUtil.iniciaMarcacaoDeTempoGasto();
		Thread.sleep( 2000 ); //2 segundos
		SigaHibernateAuditorLogUtil.logaTempoGasto( SigaLogCategoryEnum.WARN.getValue(), 1, SigaLogCategoryEnum.ERROR.getValue() );
		
		assertEquals( SigaLogCategoryEnum.WARN.getValue(), SigaHibernateAuditorLogUtil.getCategoriaLogada() );
	}
	
	@Test
	public void deveRetornarOEnumQueRepresentaACategoriaLogada() throws Exception {
		System.setProperty( "chamada.categoria.log", SigaLogCategoryEnum.DEBUG.getValue() );
		assertEquals( SigaLogCategoryEnum.DEBUG.getValue(), SigaHibernateAuditorLogUtil.loga( SigaLogCategoryEnum.DEBUG.getValue(), "mensagem" ) );
	}
	
}
