package test.model;

//import static test.model.TestUtil.*;
//
//import java.util.List;
//
//import br.gov.jfrf.siga.sr.models.SrItemConfiguracao;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import play.test.UnitTest;

//public class SrItemConfiguracaoTest extends UnitTest {
	public class SrItemConfiguracaoTest  {

//	@BeforeClass
//	public static void antesDeTudo() throws Exception {
//		criarDadosBasicos();
//		prepararSessao();
//	}
//
//	@AfterClass
//	public static void depoisDeTudo() throws Exception {
//		limparBase();
//	}
//
//	@Test
//	public void reposicionarNaHierarquiaETestarHistorico() throws Exception {
//		SrItemConfiguracao sigaWf = sigawf();
//		sigaWf.siglaItemConfiguracao = "99.02.01";
//		sigaWf.salvar();
//		assertEquals(sigaWf.pai.siglaItemConfiguracao, "99.02.00");
//
//		sigaWf.siglaItemConfiguracao = "99.01.02";
//		sigaWf.salvar();
//		assertEquals(sigaWf.pai.siglaItemConfiguracao, "99.01.00");
//
//		sigaWf.refresh();
//		List<SrItemConfiguracao> historico = sigaWf
//				.getHistoricoItemConfiguracao();
//		assertEquals(3, historico.size());
//
//		for (SrItemConfiguracao i : historico)
//			if (i.idItemConfiguracao == sigaWf.idItemConfiguracao)
//				assertNull(i.getHisDtFim());
//			else
//				assertNotNull(i.getHisDtFim());
//	}
//
//	@Test
//	public void testarDesativacaoItemEReativacao() throws Exception {
//		SrItemConfiguracao sigaDoc = sigadoc();
//		sigaDoc.finalizar();
//		assertNotNull(sigaDoc.getHisDtFim());
//		sigaDoc.salvar();
//		sigaDoc.refresh();
//
//		SrItemConfiguracao itemAnterior = sigaDoc.itemInicial;
//		// Edson: entender por que, sem o refresh abaixo, ocorre LazyException.
//		itemAnterior.refresh();
//		assertNotNull(itemAnterior.getHisDtFim());
//		assertEquals(itemAnterior.getAtual(), sigaDoc);
//		assertEquals(sigaDoc.getAtual(), sigaDoc);
//	}

}
