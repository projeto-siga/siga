package br.gob.jfrj.siga.tp.test;

import org.junit.Assert;
import org.junit.Test;

import br.gov.jfrj.siga.tp.util.SigaProperties;

public class SigaPropertiesTest {

	@Test
	public void getValue_ImagemFileSizeHappyDay() {
		Assert.assertEquals("1", SigaProperties.getValue("imagem.filesize"));
	}

	@Test
	public void getValue_null() {
		Assert.assertNull(SigaProperties.getValue("chave.que.nao.existe"));
	}
}
