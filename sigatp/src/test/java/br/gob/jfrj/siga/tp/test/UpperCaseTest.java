package br.gob.jfrj.siga.tp.test;
import net.sf.oval.Validator;

import org.junit.Assert;
import org.junit.Test;

import br.gov.jfrj.siga.tp.model.UsuarioTeste;

public class UpperCaseTest {
	private UsuarioTeste user = new UsuarioTeste();
	private String nome = "baylon".toUpperCase();
	private String endereco = "rua.voluntarios da patria".toUpperCase();
	private String bairro = "botafogo".toUpperCase();

	@Test
	public void testaUpperCase() {
		user.nome = "baylon";
		user.endereco = "rua.voluntarios da patria";
		user.bairro = "botafogo";
		user.numero = 11111;

		Validator ovalValidator = new Validator();
		ovalValidator.validate(user);

		Assert.assertEquals(user.nome, nome);
		Assert.assertEquals(user.endereco, endereco);
		Assert.assertNotSame(user.bairro, bairro);
	}
}