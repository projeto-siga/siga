package model;

import static model.TestUtil.*;
import models.SrConfiguracao;
import models.SrSolicitacao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.UnitTest;

public class SrSolicitacaoTest extends UnitTest {
	@BeforeClass
	public static void criarDadosBasicos() throws Exception {
		sigadoc();
		sigawf();
		systrab();
		manterSoft();
		criarSoft();
		attNumDoc();
		attPrazo();
		tipoConfigDesignacao();
		
		prepararSessao();
	}

	@AfterClass
	public static void limparDadosCriados() throws Exception {
		limparBase();
	}

	@Test
	public void listarItensSelecionaveisConformeSolicitanteEDesignacao()
			throws Exception {

		// Não traz nenhum pois não tem designação
		SrSolicitacao sol = new SrSolicitacao();
		sol.solicitante = eeh();
		assertEquals(0, sol.getItensDisponiveis().size());
		
		// Não traz nenhum, pois está fora do escopo
		SrConfiguracao designTRF = new SrConfiguracao();
		designTRF.atendente = sesuti();
		designTRF.setOrgaoUsuario(t2());
		designTRF.salvarComoDesignacao();
		apagaCacheDesignacao();
		assertEquals(0, sol.getItensDisponiveis().size());
		
		// Traz todos, pois designação não define item
		sol.solicitante = funcionarioTRF();
		assertEquals(5, sol.getItensDisponiveis().size());
		
		// Traz item designado e sua linhagem acima e abaixo
		designTRF.itemConfiguracao = sysdoc();
		designTRF.salvar();
		apagaCacheDesignacao();
		assertEquals(4, sol.getItensDisponiveis().size());

		designTRF.delete();
		apagaCacheDesignacao();
	}

	@Test
	public void listarAcoesSelecionaveisConformeSolicitanteItemEDesignacao() throws Exception {
		SrSolicitacao sol = new SrSolicitacao();
		sol.itemConfiguracao = null;
		sol.solicitante = eeh();

		assertEquals(0, sol.getAcoesDisponiveis().size());

		sol.delete();
	}

	@Test
	public void listarAcoesHavendoDesignSohProItemEscolhido() throws Exception {

		SrConfiguracao design = new SrConfiguracao();
		design.itemConfiguracao = sysdoc();
		design.acao = null;
		design.atendente = sesia();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.itemConfiguracao = sigadoc();
		sol.solicitante = eeh();

		assertEquals(2, sol.getAcoesDisponiveis().size());

		sol.delete();
		design.delete();
		apagaCacheDesignacao();
	}

	@Test
	public void listarAcoesHavendoDesignMasNaoProItemEscolhido()
			throws Exception {

		SrConfiguracao design = new SrConfiguracao();
		design.itemConfiguracao = sysdoc();
		design.atendente = sesia();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.itemConfiguracao = systrab();
		sol.solicitante = eeh();

		assertEquals(0, sol.getAcoesDisponiveis().size());

		sol.delete();
		design.delete();
		apagaCacheDesignacao();
	}
	
	@Test
	public void testarAtributos1(){
		
	}
	
	@Test
	public void testarAtributos2(){
		
	}
	
	@Test
	public void testarAtributos3(){
		
	}
	
	@Test
	public void trocarAcaoEVerSeAtualizamAtributos(){
		
	}
	
	@Test
	public void testarMudancaDeItemEVerSeMudamAtributosEAcoes(){
		
	}
	
	@Test
	public void testarMudancaDeLocalEVerSeMudamItemAtributosEAcoes(){
		
	}
	
	@Test
	public void testarMudancaDeSolicitanteEVerSeMudamLocaisItensAtributosEAcoes(){
		
	}
	
	@Test
	public void testarCargaDeLocalERamalInclusiveComMudanca(){
		
	}

}
