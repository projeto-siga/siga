package br.gob.jfrj.siga.tp.test.selenium;

import org.junit.After;
import org.junit.Assert;

import br.gob.jfrj.siga.tp.test.selenium.ferramentas.Browser;
import br.gob.jfrj.siga.tp.test.selenium.ferramentas.Pagina;
import br.gob.jfrj.siga.tp.test.selenium.sigatp.NavegacaoSigaTp;

import com.google.common.base.Optional;

public class BasicoTest {

	private Pagina pagina;
	private NavegacaoSigaTp login;

	public NavegacaoSigaTp getLogin() {
		return login;
	}

	public Pagina getPagina() {
		return pagina;
	}

	public void inicializaTeste() throws Exception {
		Browser browser = Browser.valueOf(Optional.fromNullable(System.getProperty("nomeBrowser")).or("FIREFOX"));
		inicializaTeste(browser);
	}

	public void inicializaTeste(Browser browser) throws Exception {
		pagina = new Pagina(browser);
		login = new NavegacaoSigaTp(pagina);
		login.logar(BasicoTest.getProperty("usuarioSiga"), BasicoTest.getProperty("senhaSiga"));
	}

	public static String getProperty(String nome) {
		String property = System.getenv(nome);
		if (property == null) {
			property = System.getProperty(nome);
		}
		return property;
	}

	// @Test
	public void acessarSigaTpChromeTest() throws Exception {
		inicializaTeste(Browser.CHROME);
		Assert.assertTrue(pagina.contemTexto("Lista de Requisi"));
	}

	// @Test
	public void acessarSigaTpFirefoxTest() throws Exception {
		inicializaTeste(Browser.FIREFOX);
		Assert.assertTrue(pagina.contemTexto("Lista de Requisi"));
	}

	// @Test
	public void acessarSigaTpIETest() throws Exception {
		inicializaTeste(Browser.INTERNETEXPLORER);
		Assert.assertTrue(pagina.contemTexto("Lista de Requisi"));
	}

	@After
	public void finaliza() {
		pagina.finalizar();
	}

}