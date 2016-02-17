package br.gov.jfrj.siga.sr.model;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.jfrj.siga.sr.util.TestUtil;

public class SrSolicitacaoTest{
	
	private static int DUAS_HORAS = 1000 * 60 * 60 * 2;
	
	@BeforeClass
	public static void setUpClass() throws Exception{
		TestUtil.configurarEntityManager();
		TestUtil.beginTransaction();
		TestUtil.criarDadosBasicos();
		TestUtil.commit();
	}
	
	@Before
	public void setUp() throws Exception{
		TestUtil.beginTransaction();		
	}
	
	@Test
	public void testBuscaSolicitacaoDeOutroOrgaoEOutroAnoPorCodigoCompleto() throws Exception {
		SrSolicitacao solSpLastYear = TestUtil.solicitacaoSpLastYear();
		SrSolicitacao solBusca = (SrSolicitacao)new SrSolicitacao().setLotaTitular(TestUtil.lotaMenor()).selecionar(solSpLastYear.getSigla());
		assertEquals(solBusca, solSpLastYear);
	}
	
	@Test
	public void testBuscaSolicitacaoFilhaDoMesmoOrgaoEMesmoAnoPorCodigoSemPonto() throws Exception {
		SrSolicitacao solRj = TestUtil.solicitacaoRj();
		SrSolicitacao solFilha = solRj.escalonarCriandoFilha(solRj.getCadastrante(), solRj.getLotaCadastrante(), solRj.getTitular(), solRj.getLotaTitular(), solRj.getItemConfiguracao(), solRj.getAcao(), solRj.getDesignacao(), null, false, "Escalonamento para teste");
		String sigla = solRj.getSigla() + "01";
		SrSolicitacao solBusca = (SrSolicitacao)new SrSolicitacao().setLotaTitular(TestUtil.lotaMenor()).selecionar(sigla);
		assertEquals(solBusca, solFilha);
	}
	
	@Test
	public void testBuscaSolicitacaoFilhaDoMesmoOrgaoEMesmoAnoPorCodigoSemOrgaoAnoHifenBarraNemZeros() throws Exception {
		SrSolicitacao solRj = TestUtil.solicitacaoRj();
		SrSolicitacao solFilha = solRj.escalonarCriandoFilha(solRj.getCadastrante(), solRj.getLotaCadastrante(), 
				solRj.getTitular(), solRj.getLotaTitular(), solRj.getItemConfiguracao(), solRj.getAcao(), 
				solRj.getDesignacao(), null, false, "Escalonamento para teste");
		String sigla = solRj.getSigla().replace(solRj.getOrgaoUsuario().getAcronimoOrgaoUsu(), "");
		sigla = sigla.replace(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), "");
		sigla = sigla.replaceAll("-", "").replaceAll("/", "");
		sigla = sigla.replaceAll("0", "");
		sigla += ".01";
		assertEquals(solFilha, (SrSolicitacao)new SrSolicitacao().setLotaTitular(solRj.getLotaTitular()).selecionar(sigla));
	}
	
	@Test
	public void testAtribuicaoPrazoCadastro() throws Exception{
		
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
		a.refresh(); //Edson: necessário para o a.parametroAcordoSet passar a conter este parâmetro 
		
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
		
		TestCase.assertTrue(s.getCadastro().getFimPrevisto().getTime() - s.getDtInicioPrimeiraEdicao().getTime() == DUAS_HORAS);
	}
	
	public void testAtribuicaoPrazoAtendimentoGeral() throws Exception{
		
	}
	
	public void testAtribuicaoPrazoAtendimentoPertoDoFimDoExpediente() throws Exception{
		
	}
	
	public void testAtribuicaoPrazoAtendimentoAposFimDoExpediente() throws Exception{
		
	}
	
	public void testAtribuicaoPrazoAtendimentoComPendencias(){
		
	}
	
	@After
	public void tearDown(){
		TestUtil.commit();
	}
		
}
