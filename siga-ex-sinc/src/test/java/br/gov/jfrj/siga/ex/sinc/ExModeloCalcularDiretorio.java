package br.gov.jfrj.siga.ex.sinc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExModeloCalcularDiretorio {

	@Test
	public void testSoEspecie() {
		assertNull(SigaExSinc.CalcularDiretorio(
				"/Users/x/siga-ex-sinc/modelos",
				"/Users/x/siga-ex-sinc/modelos/anexo/anexo.xml", "anexo"));
	}

	@Test
	public void test1SubdiretorioSoEspecie() {
		assertEquals(
				"modelos-basicos",
				SigaExSinc
						.CalcularDiretorio(
								"/Users/x/siga-ex-sinc/modelos",
								"/Users/x/siga-ex-sinc/modelos/modelos-basicos/anexo/anexo.xml",
								"anexo"));
	}

	@Test
	public void test1SubdiretorioEspecie2Subdiretorios() {
		assertEquals(
				"modelos-basicos",
				SigaExSinc
						.CalcularDiretorio(
								"/Users/x/siga-ex-sinc/modelos",
								"/Users/x/siga-ex-sinc/modelos/modelos-basicos/formulario/sgp/ferias/ferias.xml",
								"formulario/sgp/ferias"));
	}

	@Test
	public void test2SubdiretorioEspecie2Subdiretorios() {
		assertEquals(
				"modelos-basicos/rh",
				SigaExSinc
						.CalcularDiretorio(
								"/Users/x/siga-ex-sinc/modelos",
								"/Users/x/siga-ex-sinc/modelos/modelos-basicos/rh/formulario/sgp/ferias/ferias.xml",
								"formulario/sgp/ferias"));
	}

}
