package br.gob.jfrj.siga.tp.test.selenium;

import org.junit.Assert;
import org.junit.Test;

public class CondutorTest extends BasicoTest {

	private static final String _TEXTO_INICIO_PAGINA_INCLUIR_CONDUTOR = "Incluir Condutor";
	private static final String _TEXTO_MEIO_PAGINA_INCLUIR_CONDUTOR = "condutor.telefonePessoal";
	private static final String _TEXTO_FIM_PAGINA_INCLUIR_CONDUTOR = "gt-btn-medium gt-btn-left";

	@Test
	public void incluirCondutorTest() throws Exception {
		inicializaTeste();
		getLogin().acessarInclusaoCondutor();

		Assert.assertTrue(getPagina().contemTextos(_TEXTO_INICIO_PAGINA_INCLUIR_CONDUTOR, _TEXTO_MEIO_PAGINA_INCLUIR_CONDUTOR, _TEXTO_FIM_PAGINA_INCLUIR_CONDUTOR));
	}
}