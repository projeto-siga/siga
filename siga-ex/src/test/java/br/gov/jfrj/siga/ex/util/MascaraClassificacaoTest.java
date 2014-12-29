package br.gov.jfrj.siga.ex.util;

import br.gov.jfrj.siga.ex.util.MascaraUtil;
import junit.framework.TestCase;

public class MascaraClassificacaoTest extends TestCase {
	
	private static final String MASK_IN_1 = "([0-9]{0,2})\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?-?([A-Z])?";
	private static final String MASK_OUT_1 = "%1$02d.%2$02d.%3$02d.%4$02d";

	private static final String MASK_IN_2 = "([0-9]{0,2})\\.?([0-9]{0,3})?\\.?([0-9]{0,2})?";
	private static final String MASK_OUT_2 = "%1$02d.%2$03d.%3$02d";
	
	private static final String MASK_IN_3 = "([0-9]{0,1})\\-?([0-9]{0,2})?\\-?([0-9]{0,3})?";
	private static final String MASK_OUT_3 = "%1$01d-%2$02d-%3$03d";

	private static final String MASK_IN_4 = "([0-9]{0,1})\\-?([0-9]{0,5})?";
	private static final String MASK_OUT_4 = "%1$01d-%2$05d";



	public void testFormatar(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		/*TESTE DE FORMATAÇÃO*/
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertNull(m.formatar(null));
		assertEquals(m.formatar("11223344A"),"11.22.33.44");
		assertEquals(m.formatar("11223344"),"11.22.33.44");
		assertEquals(m.formatar("11.223344"),"11.22.33.44");
		assertEquals(m.formatar("11.22.33.44"),"11.22.33.44");
		assertEquals(m.formatar("11"),"11.00.00.00");
		assertEquals(m.formatar("1122"),"11.22.00.00");
		assertEquals(m.formatar("112233"),"11.22.33.00");
		assertEquals(m.formatar("11223"),"11.22.03.00");
		assertEquals(m.formatar("1.2.3.4"),"01.02.03.04");
		assertEquals(m.formatar("1.2.3"),"01.02.03.00");
		assertEquals(m.formatar("1.20"),"01.20.00.00");
		assertEquals(m.formatar("1.21.22.1"),"01.21.22.01");
		assertEquals(m.formatar("1.21.22.1-A"),"01.21.22.01");
		assertEquals(m.formatar("0"),"00.00.00.00");
		assertEquals(m.formatar("1"),"01.00.00.00");
		assertEquals(m.formatar(""),"00.00.00.00");

		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.formatar("1122233"),"11.222.33");
		assertEquals(m.formatar("11"),"11.000.00");
		assertEquals(m.formatar("11222"),"11.222.00");
		assertEquals(m.formatar("0"),"00.000.00");
		assertEquals(m.formatar(""),"00.000.00");
		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		assertEquals(m.formatar("122333"),"1-22-333");
		assertEquals(m.formatar("1"),"1-00-000");
		assertEquals(m.formatar("122"),"1-22-000");
		assertEquals(m.formatar("0"),"0-00-000");
		assertEquals(m.formatar(""),"0-00-000");
		


	}
	
	public void testCampoDaMascara(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);

		assertNull(m.getCampoDaMascara(0, "11.22.33.44"));
		assertEquals("11",m.getCampoDaMascara(1, "11.22.33.44"));
		assertEquals("22",m.getCampoDaMascara(2, "11.22.33.44"));
		assertEquals("33",m.getCampoDaMascara(3, "11.22.33.44"));
		assertEquals("44",m.getCampoDaMascara(4, "11.22.33.44"));
		assertNull(m.getCampoDaMascara(5, "11.22.33.44"));
	}
	
	public void testCalcularNivel(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(-1, m.calcularNivel(null));
		assertEquals(-1, m.calcularNivel(""));
		assertEquals(1, m.calcularNivel("00.00.00.00"));
		assertEquals(1, m.calcularNivel("11.00.00.00"));
		assertEquals(2, m.calcularNivel("11.22.00.00"));
		assertEquals(3, m.calcularNivel("11.22.33.00"));
		assertEquals(4, m.calcularNivel("11.22.33.44"));

	}
	
	public void testTotalNiveisMascara(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		
		assertEquals(4, m.getTotalDeNiveisDaMascara());
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		
		assertEquals(3, m.getTotalDeNiveisDaMascara());

		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		
		assertEquals(3, m.getTotalDeNiveisDaMascara());
		
		m.setMascaraEntrada(MASK_IN_4);
		m.setMascaraSaida(MASK_OUT_4);
		
		assertEquals(2, m.getTotalDeNiveisDaMascara());


	}

	public void testBuscaDeFilhos() {
		MascaraUtil m = MascaraUtil.getInstance();
		/*TESTE MÁSCARA DE BUSCA DE FILHOS*/ 
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertNull(m.getMscFilho(null,0,false));
		assertNull(m.getMscFilho(null,1,false));
		assertEquals(m.getMscFilho("11",2,false),"11.__.00.00");
		assertEquals(m.getMscFilho("11.00.00.00",2,false),"11.__.00.00");
		assertEquals(m.getMscFilho("11.22",3,false),"11.22.__.00");
		assertEquals(m.getMscFilho("11.22.33",4,false),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",5,false),"11.22.33.44");
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscFilho("11",2,false),"11.___.00");
		assertEquals(m.getMscFilho("11.000.00",2,false),"11.___.00");
		assertEquals(m.getMscFilho("11.222",3,false),"11.222.__");
		
		// TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscFilho("11",2,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.00.00.00",2,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",3,true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",4,true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",5,true),"11.22.33.44");
		
		assertEquals(m.getMscFilho("11",2,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",3,true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",4,true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",5,true),"11.22.33.44");
		
		// TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES COM DEDUÇÃO DE NÍVEL INCIAL
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscFilho("11223344A",true),"11.22.33.44");
		assertEquals(m.getMscFilho("11",true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.00.00.00",true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",true),"11.22.33.44");
		assertEquals(m.getMscFilho("00.00.00.00",true),"00.__.__.__");
		assertEquals(m.getMscFilho("0",true),"00.__.__.__");
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscFilho("11",true),"11.___.__");
		assertEquals(m.getMscFilho("11.000.00",true),"11.___.__");
		assertEquals(m.getMscFilho("11.222",true),"11.222.__");
		assertEquals(m.getMscFilho("11.222.33",true),"11.222.33");
		assertEquals(m.getMscFilho("00.000.00",true),"00.___.__");
	}
	
	public void testBuscaDePais(){
		MascaraUtil m = MascaraUtil.getInstance();
		/*TESTE BUSCA DE PAIS*/
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertNull(m.getPais(null));

		String[] result = m.getPais("11.22.33.44");
		assertEquals(result.length, 3);
		assertEquals(result[0], "11.00.00.00");
		assertEquals(result[1], "11.22.00.00");
		assertEquals(result[2], "11.22.33.00");
		
		result = m.getPais("11.22.33.00");
		assertEquals(result.length, 2);
		assertEquals(result[0], "11.00.00.00");
		assertEquals(result[1], "11.22.00.00");
		
		result = m.getPais("11.22.33");
		assertEquals(result.length, 2);
		assertEquals(result[0], "11.00.00.00");
		assertEquals(result[1], "11.22.00.00");
		
		result = m.getPais("11.22.00.00");
		assertEquals(result.length, 1);
		assertEquals(result[0], "11.00.00.00");
		
		result = m.getPais("11.22");
		assertEquals(result.length, 1);
		assertEquals(result[0], "11.00.00.00");


		result = m.getPais("11.00.00.00");
		assertNull(result);
		
		result = m.getPais("11");
		assertNull(result);
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		result = m.getPais("11.222.33");
		assertEquals(result.length, 2);
		assertEquals(result[0], "11.000.00");
		assertEquals(result[1], "11.222.00");
		
		result = m.getPais("11.222.00");
		assertEquals(result.length, 1);
		assertEquals(result[0], "11.000.00");
		
		result = m.getPais("11.000.00");
		assertNull(result);
		
		result = m.getPais("11.222");
		assertEquals(result.length, 1);
		assertEquals(result[0], "11.000.00");

		result = m.getPais("11");
		assertNull(result);
		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		result = m.getPais("1-22-333");
		assertEquals(result.length, 2);
		assertEquals(result[0], "1-00-000");
		assertEquals(result[1], "1-22-000");

		
		result = m.getPais("1-22-000");
		assertEquals(result.length, 1);
		assertEquals(result[0], "1-00-000");
		
		result = m.getPais("1-00-000");
		assertNull(result);
		
		result = m.getPais("1-22");
		assertEquals(result.length, 1);
		assertEquals(result[0], "1-00-000");

		result = m.getPais("1");
		assertNull(result);

		m.setMascaraEntrada(MASK_IN_4);
		m.setMascaraSaida(MASK_OUT_4);
		result = m.getPais("1-22222");
		assertEquals(result.length, 1);
		assertEquals(result[0], "1-00000");
		
		result = m.getPais("1");
		assertNull(result);

	}
	
	public void testBuscaPorNivel(){
		MascaraUtil m = MascaraUtil.getInstance();
		/*TESTE MÁSCARA DE BUSCA TODOS DE UM DETERMINADO NÍVEL*/
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscTodosDoNivel(0),"__.__.__.__");
		assertEquals(m.getMscTodosDoNivel(1),"__.00.00.00");
		assertEquals(m.getMscTodosDoNivel(2),"__.__.00.00");
		assertEquals(m.getMscTodosDoNivel(3),"__.__.__.00");
		assertEquals(m.getMscTodosDoNivel(4),"__.__.__.__");
		assertEquals(m.getMscTodosDoNivel(-1),"__.__.__.__");
		assertEquals(m.getMscTodosDoNivel(99999),"__.__.__.__");
		assertEquals(m.getMscTodosDoMaiorNivel(),"__.__.__.__");


		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscTodosDoNivel(0),"__.___.__");
		assertEquals(m.getMscTodosDoNivel(1),"__.000.00");
		assertEquals(m.getMscTodosDoNivel(2),"__.___.00");
		assertEquals(m.getMscTodosDoNivel(3),"__.___.__");
		assertEquals(m.getMscTodosDoNivel(-1),"__.___.__");
		assertEquals(m.getMscTodosDoNivel(99999),"__.___.__");
		assertEquals(m.getMscTodosDoMaiorNivel(),"__.___.__");

		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		assertEquals(m.getMscTodosDoNivel(0),"_-__-___");
		assertEquals(m.getMscTodosDoNivel(1),"_-00-000");
		assertEquals(m.getMscTodosDoNivel(2),"_-__-000");
		assertEquals(m.getMscTodosDoNivel(3),"_-__-___");
		assertEquals(m.getMscTodosDoNivel(-1),"_-__-___");
		assertEquals(m.getMscTodosDoNivel(99999),"_-__-___");
		assertEquals(m.getMscTodosDoMaiorNivel(),"_-__-___");

	}
	public void testSubstituicao(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		
		assertEquals("01.02.03.04",m.substituir("01.02.03.04","__.__.__.__"));
		assertEquals("05.02.03.04",m.substituir("01.02.03.04","05.__.__.__"));
		assertEquals("05.06.03.04",m.substituir("01.02.03.04","05.06.__.__"));
		assertEquals("05.06.07.04",m.substituir("01.02.03.04","05.06.07.__"));
		assertEquals("05.06.07.08",m.substituir("01.02.03.04","05.06.07.08"));
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals("01.002.03",m.substituir("01.002.03","__.___.__"));
		assertEquals("05.002.03",m.substituir("01.002.03","05.___.__"));
		assertEquals("05.006.03",m.substituir("01.002.03","05.006.__"));
		assertEquals("05.006.07",m.substituir("01.002.03","05.006.07"));
		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		assertEquals("1-22-333",m.substituir("1-22-333","_-__-___"));
		assertEquals("5-22-333",m.substituir("1-22-333","5-__-___"));
		assertEquals("5-66-333",m.substituir("1-22-333","5-66-___"));
		assertEquals("5-66-777",m.substituir("1-22-333","5-66-777"));
		
		m.setMascaraEntrada(MASK_IN_4);
		m.setMascaraSaida(MASK_OUT_4);
		assertEquals("1-22222",m.substituir("1-22222","_-_____"));
		assertEquals("5-22222",m.substituir("1-22222","5-_____"));
		assertEquals("5-66666",m.substituir("1-22222","5-66666"));

		//máscaras e valores inválidos
		assertNull(m.substituir("01.02.03.04","__,__,__,__"));
		assertNull(m.substituir("01.02.03.04","__.__.__"));
		assertNull(m.substituir("01.02.03.04","abcde012345"));
		assertNull(m.substituir("01.02.03.04",""));
		assertNull(m.substituir("01.02.03.04",null));
		assertNull(m.substituir("","__.__.__.__"));
		assertNull(m.substituir(null,"__.__.__.__"));
		assertNull(m.substituir("1","__.__.__.__"));
	}
	
	public void testIsUltimoNivel(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		
		assertTrue(m.isUltimoNivel("00.00.00.01"));
		assertTrue(m.isUltimoNivel("01.02.03.04"));
		assertTrue(m.isUltimoNivel("1.2.3.4"));
		
		assertFalse(m.isUltimoNivel("00.00.00.00"));
		assertFalse(m.isUltimoNivel("01.02.03.00"));
		assertFalse(m.isUltimoNivel("01.00.03.00"));
		assertFalse(m.isUltimoNivel("1.2"));
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		
		assertTrue(m.isUltimoNivel("00.000.01"));
		assertTrue(m.isUltimoNivel("01.002.03"));
		assertTrue(m.isUltimoNivel("01.000.03"));
		assertTrue(m.isUltimoNivel("1.2.3"));
		
		assertFalse(m.isUltimoNivel("00.000.00"));
		assertFalse(m.isUltimoNivel("01.002.00"));
		assertFalse(m.isUltimoNivel("1.2"));
		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		
		assertTrue(m.isUltimoNivel("0-00-001"));
		assertTrue(m.isUltimoNivel("1-02-003"));
		assertTrue(m.isUltimoNivel("1-00-003"));
		assertTrue(m.isUltimoNivel("1-2-3"));
		
		assertFalse(m.isUltimoNivel("0-00-000"));
		assertFalse(m.isUltimoNivel("1-02-000"));
		assertFalse(m.isUltimoNivel("1-2"));

		m.setMascaraEntrada(MASK_IN_4);
		m.setMascaraSaida(MASK_OUT_4);
		
		assertTrue(m.isUltimoNivel("0-00001"));
		assertTrue(m.isUltimoNivel("1-00002"));
		assertTrue(m.isUltimoNivel("1-2"));
		
		assertFalse(m.isUltimoNivel("0-00000"));
		assertFalse(m.isUltimoNivel("1-00000"));
		assertFalse(m.isUltimoNivel("1"));


	}
}
