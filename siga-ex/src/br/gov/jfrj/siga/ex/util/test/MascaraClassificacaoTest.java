package br.gov.jfrj.siga.ex.util.test;

import br.gov.jfrj.siga.ex.util.MascaraUtil;
import junit.framework.TestCase;

public class MascaraClassificacaoTest extends TestCase {
	
	private static final String MASK_IN_1 = "([0-9]{0,2})\\.?([0-9]{2})?\\.?([0-9]{2})?\\.?([0-9]{2})?([A-Z])?";
	private static final String MASK_OUT_1 = "%1$02d.%2$02d.%3$02d.%4$02d";

	private static final String MASK_IN_2 = "([0-9]{0,2})\\.?([0-9]{3})?\\.?([0-9]{2})?";
	private static final String MASK_OUT_2 = "%1$02d.%2$03d.%3$02d";
	
	private static final String MASK_IN_3 = "([0-9]{0,1})\\-?([0-9]{2})?\\-?([0-9]{3})?";
	private static final String MASK_OUT_3 = "%1$01d-%2$02d-%3$03d";



	public void testFormatar(){
		MascaraUtil m = MascaraUtil.getInstance();
		
		/*TESTE DE FORMATAÇÃO*/
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.formatar("11223344A"),"11.22.33.44");
		assertEquals(m.formatar("11223344"),"11.22.33.44");
		assertEquals(m.formatar("11.223344"),"11.22.33.44");
		assertEquals(m.formatar("11.22.33.44"),"11.22.33.44");
		assertEquals(m.formatar("11"),"11.00.00.00");
		assertEquals(m.formatar("1122"),"11.22.00.00");
		assertEquals(m.formatar("112233"),"11.22.33.00");
		assertEquals(m.formatar("0"),"00.00.00.00");
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
		
		/*TESTE MÁSCARA DE BUSCA DE FILHOS*/ 
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscFilho("11",1,false),"11.__.00.00");
		assertEquals(m.getMscFilho("11.00.00.00",1,false),"11.__.00.00");
		assertEquals(m.getMscFilho("11.22",2,false),"11.22.__.00");
		assertEquals(m.getMscFilho("11.22.33",3,false),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",4,false),"11.22.33.44");
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscFilho("11",1,false),"11.___.00");
		assertEquals(m.getMscFilho("11.000.00",1,false),"11.___.00");
		assertEquals(m.getMscFilho("11.222",2,false),"11.222.__");
		assertEquals(m.getMscFilho("11.222.33",2,false),"11.222.__");
		
		// TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscFilho("11",1,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.00.00.00",1,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",2,true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",3,true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",4,true),"11.22.33.44");
		
		assertEquals(m.getMscFilho("11",1,true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",2,true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",3,true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",4,true),"11.22.33.44");
		
		// TESTE MÁSCARA DE BUSCA DE FILHOS E DESCENDENTES COM DEDUÇÃO DE NÍVEL INCIAL
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscFilho("11223344A",true),"11.22.33.44");
		assertEquals(m.getMscFilho("11",true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.00.00.00",true),"11.__.__.__");
		assertEquals(m.getMscFilho("11.22",true),"11.22.__.__");
		assertEquals(m.getMscFilho("11.22.33",true),"11.22.33.__");
		assertEquals(m.getMscFilho("11.22.33.44",true),"11.22.33.44");
		
		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscFilho("11",true),"11.___.__");
		assertEquals(m.getMscFilho("11.000.00",true),"11.___.__");
		assertEquals(m.getMscFilho("11.222",true),"11.222.__");
		assertEquals(m.getMscFilho("11.222.33",true),"11.222.33");

		/*TESTE MÁSCARA DE BUSCA TODOS DE UM DETERMINADO NÍVEL*/
		m.setMascaraEntrada(MASK_IN_1);
		m.setMascaraSaida(MASK_OUT_1);
		assertEquals(m.getMscTodosDoNivel(0),"__.00.00.00");
		assertEquals(m.getMscTodosDoNivel(1),"__.__.00.00");
		assertEquals(m.getMscTodosDoNivel(2),"__.__.__.00");
		assertEquals(m.getMscTodosDoNivel(3),"__.__.__.__");

		m.setMascaraEntrada(MASK_IN_2);
		m.setMascaraSaida(MASK_OUT_2);
		assertEquals(m.getMscTodosDoNivel(0),"__.000.00");
		assertEquals(m.getMscTodosDoNivel(1),"__.___.00");
		assertEquals(m.getMscTodosDoNivel(2),"__.___.__");
		
		m.setMascaraEntrada(MASK_IN_3);
		m.setMascaraSaida(MASK_OUT_3);
		assertEquals(m.getMscTodosDoNivel(0),"_-00-000");
		assertEquals(m.getMscTodosDoNivel(1),"_-__-000");
		assertEquals(m.getMscTodosDoNivel(2),"_-__-___");

	
	}
}
