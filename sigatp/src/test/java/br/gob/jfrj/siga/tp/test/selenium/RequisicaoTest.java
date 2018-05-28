package br.gob.jfrj.siga.tp.test.selenium;

import org.junit.Assert;
import org.junit.Test;

public class RequisicaoTest extends BasicoTest {

	private static final String _TEXTO_INICIO_PAGINA_INCLUIR = "Incluir Requisi";
	private static final String _TEXTO_MEIO_PAGINA_INCLUIR = "Passageiros";
	private static final String _TEXTO_FIM_PAGINA_INCLUIR = "gt-btn-medium gt-btn-left";

	private static final String _TEXTO_INICIO_PAGINA_INICIAL_LISTAR = "Lista de Requisi";
	private static final String _TEXTO_MEIO_PAGINA_INICIAL_LISTAR = "odas";
	private static final String _TEXTO_FIM_PAGINA_INICIAL_LISTAR = "gt-btn-medium gt-btn-left";

	private static final String _TEXTO_INICIO_PAGINA_LISTAR_TODAS = "Lista de Requisi";
	private static final String _TEXTO_MEIO_PAGINA_LISTAR_TODAS = "torizadas";
	private static final String _TEXTO_FIM_PAGINA_LISTAR_TODAS = "ejeitadas";

	private static final String _TEXTO_INICIO_PAGINA_LISTAR_TODAS_PARA_APROVACAO = "Lista de Requisi";
	private static final String _TEXTO_MEIO_PAGINA_LISTAR_TODAS_PARA_APROVACAO = "ertas";
	private static final String _TEXTO_FIM_PAGINA_LISTAR_TODAS_PARA_APROVACAO = "torizadas";

	private static final String _TEXTO_INICIO_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO = "Lista de Requisi";
	private static final String _TEXTO_MEIO_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO = "odas";
	private static final String _TEXTO_FIM_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO = "ejeitadas";

	@Test
	public void incluirRequisicaoTest() throws Exception {

		inicializaTeste();
		getLogin().acessarInclusaoRequisicao();
		Assert.assertTrue(getPagina().contemTextos(_TEXTO_INICIO_PAGINA_INCLUIR, _TEXTO_MEIO_PAGINA_INCLUIR, _TEXTO_FIM_PAGINA_INCLUIR));
	}

	@Test
	public void listarRequisicoesTest() throws Exception {
		inicializaTeste();
		getLogin().acessarListarRequisicoes();

		boolean resultadoTestes = getPagina().contemTextos(_TEXTO_INICIO_PAGINA_INICIAL_LISTAR, _TEXTO_MEIO_PAGINA_INICIAL_LISTAR, _TEXTO_FIM_PAGINA_INICIAL_LISTAR);

		getLogin().acessarMenuTodasListarRequisicoes();

		resultadoTestes = resultadoTestes && getPagina().contemTextos(_TEXTO_INICIO_PAGINA_LISTAR_TODAS, _TEXTO_MEIO_PAGINA_LISTAR_TODAS, _TEXTO_FIM_PAGINA_LISTAR_TODAS);

		Assert.assertTrue(resultadoTestes);
	}

	@Test
	public void listarRequisicoesParaAprovacaoTest() throws Exception {
		inicializaTeste();
		getLogin().acessarListarRequisicoesParaAprovacao();

		boolean resultadoTestes = getPagina().contemTextos(_TEXTO_INICIO_PAGINA_LISTAR_TODAS_PARA_APROVACAO, _TEXTO_MEIO_PAGINA_LISTAR_TODAS_PARA_APROVACAO,
				_TEXTO_FIM_PAGINA_LISTAR_TODAS_PARA_APROVACAO);

		getLogin().acessarMenuAbertasListarRequisicoesParaAprovacao();

		resultadoTestes = resultadoTestes
				&& getPagina().contemTextos(_TEXTO_INICIO_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO, _TEXTO_MEIO_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO,
						_TEXTO_FIM_PAGINA_INICIAL_LISTAR_ABERTAS_PARA_APROVACAO);

		Assert.assertTrue(resultadoTestes);
	}

}