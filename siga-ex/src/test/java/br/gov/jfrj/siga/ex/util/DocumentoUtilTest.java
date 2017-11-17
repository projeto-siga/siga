package br.gov.jfrj.siga.ex.util;

import java.util.Date;

import junit.framework.TestCase;

public class DocumentoUtilTest extends TestCase {

	public void testUFNoFinal(){
		Date dt17Nov2017 = new Date(1510884000000l);
		assertEquals("Campo dos Goytacazes-RJ, 17 de novembro de 2017.", DocumentoUtil.obterDataExtenso("CAMPO DOS GOYTACAZES-RJ",dt17Nov2017));
		assertEquals("Campo dos Goytacazes-RJ, 17 de novembro de 2017.", DocumentoUtil.obterDataExtenso("campo dos goyTACAZes-RJ",dt17Nov2017));
		
		assertEquals("Campo dos Goytacazes, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("campo dos goyTACAZes",dt17Nov2017));
		assertEquals("Campo dos Goytacazes, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("CAMPO DOS GOYTACAZES",dt17Nov2017));
		assertEquals("Campo dos Goytacazes, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("campo dos goytacazes",dt17Nov2017));
		
		assertEquals("Qualquer Coisa-RJ, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("qualquer coisa-rj",dt17Nov2017));
		assertEquals("Duque de Caxias-RJ, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("DuQuE dE cAxIaS-rJ",dt17Nov2017));
		
		assertEquals("Ji-Paraná-RO, 17 de novembro de 2017.",DocumentoUtil.obterDataExtenso("jI-pArAnÁ-RO",dt17Nov2017));

	}
}
