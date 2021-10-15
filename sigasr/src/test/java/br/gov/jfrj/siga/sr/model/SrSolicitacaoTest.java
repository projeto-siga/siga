package br.gov.jfrj.siga.sr.model;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.sr.util.TestUtil;
import junit.framework.TestCase;

public class SrSolicitacaoTest {

	private static int DUAS_HORAS = 60 * 60 * 2;
	private static int UM_MINUTO = 60;

	@BeforeClass
	public static void setUpClass() throws Exception {
		TestUtil.configurarEntityManager();
		TestUtil.beginTransaction();
		TestUtil.criarDadosBasicos();
		TestUtil.commit();
	}

	@Before
	public void setUp() throws Exception {
		TestUtil.beginTransaction();
	}

	@Test
	public void testBuscaSolicitacaoDeOutroOrgaoEOutroAnoPorCodigoCompleto()
			throws Exception {
		SrSolicitacao solSpLastYear = TestUtil.solicitacaoSpLastYear();
		SrSolicitacao solBusca = (SrSolicitacao) new SrSolicitacao()
				.setLotaTitular(TestUtil.lotaMenor()).selecionar(
						solSpLastYear.getSigla());
		assertEquals(solBusca, solSpLastYear);
	}

	@Test
	public void testBuscaSolicitacaoFilhaDoMesmoOrgaoEMesmoAnoPorCodigoSemPonto()
			throws Exception {
		SrSolicitacao solRj = TestUtil.solicitacaoRj();
		SrSolicitacao solFilha = solRj.escalonarCriandoFilha(
				solRj.getCadastrante(), solRj.getLotaCadastrante(),
				solRj.getTitular(), solRj.getLotaTitular(),
				solRj.getItemConfiguracao(), solRj.getAcao(),
				solRj.getDesignacao(), null, false, "Escalonamento para teste", null);
		String sigla = solRj.getSigla() + "01";
		SrSolicitacao solBusca = (SrSolicitacao) new SrSolicitacao()
				.setLotaTitular(TestUtil.lotaMenor()).selecionar(sigla);
		assertEquals(solBusca, solFilha);
	}

	@Test
	public void testBuscaSolicitacaoFilhaDoMesmoOrgaoEMesmoAnoPorCodigoSemOrgaoAnoHifenBarraNemZeros()
			throws Exception {
		SrSolicitacao solRj = TestUtil.solicitacaoRj();
		SrSolicitacao solFilha = solRj.escalonarCriandoFilha(
				solRj.getCadastrante(), solRj.getLotaCadastrante(),
				solRj.getTitular(), solRj.getLotaTitular(),
				solRj.getItemConfiguracao(), solRj.getAcao(),
				solRj.getDesignacao(), null, false, "Escalonamento para teste", null);
		String sigla = solRj.getSigla().replace(
				solRj.getOrgaoUsuario().getAcronimoOrgaoUsu(), "");
		sigla = sigla.replace(
				String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), "");
		sigla = sigla.replaceAll("-", "").replaceAll("/", "");
		sigla = sigla.replaceAll("0", "");
		sigla += ".01";
		assertEquals(solFilha, (SrSolicitacao) new SrSolicitacao()
				.setLotaTitular(solRj.getLotaTitular()).selecionar(sigla));
	}

	@Test
	public void testAtribuicaoPrazoCadastro() throws Exception {

		SrAcordo a = new SrAcordo();
		a.setNomeAcordo("Acordo para teste");
		a.salvarComHistorico();

		SrParametroAcordo param = new SrParametroAcordo();
		param.setAcordo(a);
		param.setParametro(SrParametro.CADASTRO);
		param.setOperador(SrOperador.MENOR_OU_IGUAL);
		param.setValor(2L);
		param.setUnidadeMedida(TestUtil.hora());
		param.salvarComHistorico();
		a.refresh(); // Edson: necessário para o a.parametroAcordoSet passar a
						// conter este parâmetro

		SrConfiguracao abrangencia = new SrConfiguracao();
		abrangencia.setAcordo(a);
		abrangencia.setDescrConfiguracao("1stAcordo");
		abrangencia.salvarComoAbrangenciaAcordo();

		TestUtil.reiniciarCacheConfiguracoes();
		
		SrSolicitacao s = new SrSolicitacao();
		s.setCadastrante(TestUtil.funcionarioMenor());
		s.setItemConfiguracao(TestUtil.sigadoc());
		s.setAcao(TestUtil.acaoCriarSoft());
		s.setDesignacao(TestUtil.designacaoBasica());
		s.setRascunho(true);
		s.salvarComHistorico();

		int restante = s.getCadastro().getRestanteEmSegundos().intValue();
		TestCase.assertTrue(restante > DUAS_HORAS - UM_MINUTO && restante <= DUAS_HORAS);
	}

	// Edson: o método escalonarCriandoFilha deveria testar se a pessoa
	// informada como cadastrante da movimentação tem permissão para isso.
	// Também, deveria verificar se a solicitação já é filha
	@Test
	public void testFechamentoAutomaticoAoFecharUltimaFilha() throws Exception {

		SrSolicitacao s = TestUtil.solicitacaoRj();

		SrSolicitacao filha = s.escalonarCriandoFilha(s.getCadastrante(),
				s.getLotaCadastrante(), s.getTitular(), s.getLotaTitular(),
				s.getItemConfiguracao(), s.getAcao(), s.getDesignacao(), null,
				true, "Escalonamento para teste", null);
		
		TestCase.assertTrue(s.isFechadoAutomaticamente());
		
		SrSolicitacao filha2 = s.escalonarCriandoFilha(s.getCadastrante(),
				s.getLotaCadastrante(), s.getTitular(), s.getLotaTitular(),
				s.getItemConfiguracao(), s.getAcao(), s.getDesignacao(), null,
				true, "Segundo escalonamento para teste", null);

		filha.fechar(filha.getCadastrante(), filha.getLotaCadastrante(),
				filha.getTitular(), filha.getLotaTitular(),
				filha.getItemConfiguracao(), filha.getAcao(),
				"Fechando para teste",
				SrTipoMotivoFechamento.ATENDIMENTO_CONCLUÍDO, null, null);
		TestCase.assertFalse(s.isFechado());

		filha2.fechar(filha2.getCadastrante(), filha2.getLotaCadastrante(),
				filha2.getTitular(), filha2.getLotaTitular(),
				filha2.getItemConfiguracao(), filha2.getAcao(),
				"Fechando para teste",
				SrTipoMotivoFechamento.ATENDIMENTO_CONCLUÍDO, null, null);
		TestCase.assertTrue(s.isFechado());

	}

	@Test
	public void testAFecharENecessitaProvidencia() throws Exception {
		SrSolicitacao principal = TestUtil.solicitacaoRj();
		SrSolicitacao filha = principal.escalonarCriandoFilha(principal.getCadastrante(),
				principal.getLotaCadastrante(), principal.getTitular(), principal.getLotaTitular(),
				principal.getItemConfiguracao(), principal.getAcao(), principal.getDesignacao(), null,
				false, "Escalonamento para teste", null);

		for (SrTipoMotivoFechamento m : SrTipoMotivoFechamento.values()) {
			filha.fechar(filha.getCadastrante(), filha.getLotaCadastrante(),
					filha.getTitular(), filha.getLotaTitular(),
					filha.getItemConfiguracao(), filha.getAcao(),
					"Fechando para teste", m, null, null);
			long marcadorEsperado = m.equals(SrTipoMotivoFechamento.ATENDIMENTO_CONCLUÍDO) || m.equals(SrTipoMotivoFechamento.TAREFA_CONCLUÍDA)
					? CpMarcadorEnum.SOLICITACAO_FECHADO_PARCIAL.getId()
					: CpMarcadorEnum.SOLICITACAO_NECESSITA_PROVIDENCIA.getId();
			TestCase.assertTrue(principal.isMarcada(marcadorEsperado));
			filha.desfazerUltimaMovimentacao(filha.getCadastrante(), filha.getLotaCadastrante(),
					filha.getTitular(), filha.getLotaTitular());
		}

	}

	public void testAtribuicaoPrazoAtendimentoGeral() throws Exception {

	}

	public void testAtribuicaoPrazoAtendimentoPertoDoFimDoExpediente()
			throws Exception {

	}

	public void testAtribuicaoPrazoAtendimentoAposFimDoExpediente()
			throws Exception {

	}

	public void testAtribuicaoPrazoAtendimentoComPendencias() {

	}

	@After
	public void tearDown() {
		TestUtil.commit();
	}

}
