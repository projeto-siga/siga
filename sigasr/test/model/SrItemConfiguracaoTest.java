package model;

import java.util.List;

import models.SrAcao;
import models.SrConfiguracao;
import models.SrItemConfiguracao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static model.TestUtil.*;
import play.test.UnitTest;

public class SrItemConfiguracaoTest extends UnitTest {

	@BeforeClass
	public static void criarDadosBasicos() throws Exception{
		sigadoc();
		sigawf();
		systrab();
		assertEquals(5, SrItemConfiguracao.count());
		
		tipoConfigDesignacao();
		
		prepararSessao();
	}
	
	@AfterClass
	public static void limparDadosCriados() throws Exception{
		limparBase();
	}
	
	@Test
	public void reposicionarNaHierarquiaETestarHistorico()
			throws Exception {
		SrItemConfiguracao sigaWf = sigawf();
		sigaWf.siglaItemConfiguracao = "99.02.01";
		sigaWf.salvar();
		assertEquals(sigaWf.getPai().siglaItemConfiguracao, "99.02.00");

		sigaWf.siglaItemConfiguracao = "99.01.02";
		sigaWf.salvar();
		assertEquals(sigaWf.getPai().siglaItemConfiguracao, "99.01.00");

		sigaWf.refresh();
		List<SrItemConfiguracao> historico = sigaWf
				.getHistoricoItemConfiguracao();
		assertEquals(3, historico.size());

		for (SrItemConfiguracao i : historico)
			if (i.idItemConfiguracao == sigaWf.idItemConfiguracao)
				assertNull(i.getHisDtFim());
			else
				assertNotNull(i.getHisDtFim());
	}

	@Test
	public void testarDesativacaoItemEReativacao() throws Exception {
		SrItemConfiguracao sigaDoc = sigadoc();
		sigaDoc.finalizar();
		assertNotNull(sigaDoc.getHisDtFim());
		sigaDoc.salvar();
		sigaDoc.refresh();

		SrItemConfiguracao itemAnterior = sigaDoc.itemInicial;
		// Edson: entender por que, sem o refresh abaixo, ocorre LazyException.
		itemAnterior.refresh();
		assertNotNull(itemAnterior.getHisDtFim());
		assertEquals(itemAnterior.getAtual(), sigaDoc);
		assertEquals(sigaDoc.getAtual(), sigaDoc);
	}
	
}
