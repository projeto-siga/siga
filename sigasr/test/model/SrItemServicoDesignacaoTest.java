package model;

import static org.junit.Assert.*;

import java.util.List;

import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSolicitacao;
import models.SrSubTipoConfiguracao;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

import play.db.jpa.JPA;
import play.test.UnitTest;

public class SrItemServicoDesignacaoTest extends UnitTest {

	private DpPessoa eeh() {
		return DpPessoa.find("bySiglaPessoa", "EEH").first();
	}

	private DpLotacao sesia() {
		return DpLotacao.find("bySiglaLotacao", "SESIA").first();
	}

	private SrItemConfiguracao software() {
		return SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.00.00").first();
	}

	private SrItemConfiguracao sysDoc() {
		return SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.00").first();
	}

	private SrItemConfiguracao sysTrab() {
		return SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.02.00").first();
	}

	private SrItemConfiguracao sigaDoc() {
		return SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.01").first();
	}

	private SrItemConfiguracao sigaWf() {
		return SrItemConfiguracao.find(
				"bySiglaItemConfiguracaoAndHisDtFimIsNull", "99.01.02").first();
	}

	private void prepararSessao() throws Exception {
		Session playSession = (Session) JPA.em().getDelegate();
		CpDao.freeInstance();
		CpDao.getInstance(playSession);
		HibernateUtil.setSessao(playSession);
		Cp.getInstance().getConf().limparCacheSeNecessario();
	}

	private SrConfiguracao design() throws Exception {
		try {
			return SrConfiguracao.getConfiguracoes(
					eeh(),
					null,
					null,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
					SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE,
					new int[] { SrConfiguracaoBL.ITEM_CONFIGURACAO,
							SrConfiguracaoBL.SERVICO }).get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void apagaCacheDesignacao() throws Exception {
		SrConfiguracaoBL
				.get()
				.limparCache(
						(CpTipoConfiguracao) CpTipoConfiguracao
								.findById(CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
	}

	@Test
	public void testarCriacaoItem() throws Exception {

		SrItemConfiguracao software = new SrItemConfiguracao();
		software.descrItemConfiguracao = "Software";
		software.siglaItemConfiguracao = "99.00.00";
		software.tituloItemConfiguracao = "Software";
		software.salvar();

		SrItemConfiguracao sysDoc = new SrItemConfiguracao();
		sysDoc.descrItemConfiguracao = "Sistema de Gestão Documental";
		sysDoc.siglaItemConfiguracao = "99.01.00";
		sysDoc.tituloItemConfiguracao = "Sistema de Gestão Documental";
		sysDoc.salvar();

		SrItemConfiguracao sysTrab = new SrItemConfiguracao();
		sysTrab.descrItemConfiguracao = "Sistema de Gestão do Trabalho";
		sysTrab.siglaItemConfiguracao = "99.02.00";
		sysTrab.tituloItemConfiguracao = "Sistema de Gestão do Trabalho";
		sysTrab.salvar();

		SrItemConfiguracao sigaDoc = new SrItemConfiguracao();
		sigaDoc.descrItemConfiguracao = "Siga - Módulo Documentos";
		sigaDoc.siglaItemConfiguracao = "99.01.01";
		sigaDoc.tituloItemConfiguracao = "Siga-Doc";
		sigaDoc.salvar();

		SrItemConfiguracao sigaWf = new SrItemConfiguracao();
		sigaWf.descrItemConfiguracao = "Siga - Módulo Workflow";
		sigaWf.siglaItemConfiguracao = "99.01.02";
		sigaWf.tituloItemConfiguracao = "Siga-WF";
		sigaWf.salvar();

		SrItemConfiguracao filtro = new SrItemConfiguracao();
		filtro.siglaItemConfiguracao = "99.";
		assertEquals(5, filtro.buscar().size());
		assertEquals(5, SrItemConfiguracao.listar().size());

		for (SrItemConfiguracao item : SrItemConfiguracao.listar()) {
			assertNull(item.itemInicial);
			item.refresh();
			assertNotNull(item.itemInicial);
		}
	}

	@Test
	public void reposicionarSigaWFNaHierarquiaETestarHistorico()
			throws Exception {
		SrItemConfiguracao sigaWf = sigaWf();
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
		SrItemConfiguracao sigaDoc = sigaDoc();
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

	@Test
	public void testarCriacaoServico() throws Exception {

		SrServico servSoft = new SrServico();
		servSoft.descrServico = "Ação para Software";
		servSoft.siglaServico = "99.00";
		servSoft.tituloServico = "Ação para Software";
		servSoft.salvar();

		SrServico criar = new SrServico();
		criar.descrServico = "Criar";
		criar.siglaServico = "99.01";
		criar.tituloServico = "Criar";
		criar.salvar();

		SrServico manter = new SrServico();
		manter.descrServico = "Manter";
		manter.siglaServico = "99.02";
		manter.tituloServico = "Manter";
		manter.salvar();

		SrServico filtro = new SrServico();
		filtro.siglaServico = "99.";
		assertEquals(3, filtro.buscar().size());
		assertEquals(3, SrServico.listar().size());

		for (SrServico servico : SrServico.listar()) {
			assertNull(servico.servicoInicial);
			servico.refresh();
			assertNotNull(servico.servicoInicial);
		}
	}

	@Test
	public void listarItemEServicoSemHaverDesignTrazVazio() throws Exception {
		prepararSessao();
		assertEquals(0, SrItemConfiguracao.listarPorPessoa(eeh()).size());
		assertEquals(0, SrServico.listarComAtendentePorPessoaEItem(eeh(), null).size());
	}

	@Test
	public void listarItemHavendoApenasDesignForaDoEscopoTrazVazio()
			throws Exception {
		SrConfiguracao designTRF = new SrConfiguracao();
		designTRF.atendente = DpLotacao.find("bySiglaLotacao", "SESUTI")
				.first();
		designTRF
				.setOrgaoUsuario((CpOrgaoUsuario) (CpOrgaoUsuario.findById(3L)));
		designTRF.salvarComoDesignacao();
		apagaCacheDesignacao();

		assertEquals(0, SrItemConfiguracao.listarPorPessoa(eeh()).size());

	}

	@Test
	public void listarItemHavendoApenasDesignSemItemTrazTodos()
			throws Exception {
		SrConfiguracao design = new SrConfiguracao();
		design.atendente = sesia();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();

		List<SrItemConfiguracao> itens = SrItemConfiguracao
				.listarPorPessoa(eeh());
		assertEquals(5, itens.size());
	}

	// Edson: Rever esta regra. Está trazendo os ascendentes de um item só para
	// não aparecer sem pai na tela de busca de item. Neste caso, está
	// retornando 09.00.00 sem haver designação correspondente
	@Test
	public void listarItemHavendoDesignQReferenciaUmItemTrazLinhagemDele()
			throws Exception {
		SrConfiguracao design = design();
		design.itemConfiguracao = sysDoc();
		design.atendente = sesia();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();

		List<SrItemConfiguracao> itens = SrItemConfiguracao
				.listarPorPessoa(eeh());

		// Edson: 99.00.00, 99.01.00, 99.01.01, 99.01.02
		assertEquals(4, itens.size());

	}

	@Test
	public void listarServicoPorItemHavendoDesignProItemSemServicoTrazTodosDeNivel2()
			throws Exception {
		// Edson: Traz 99.01, 99.02. Apesar de não haver designação diretamente
		// pro SigaDoc, há para sysDoc
		assertEquals(2, SrServico.listarComAtendentePorPessoaEItem(eeh(), sigaDoc()).size());
	}

	@Test
	public void listarServicoPorItemNaoHavendoDesignProItemTrazVazio()
			throws Exception {
		//Edson: vazio porque não há designação pro sysTrab
		assertEquals(0, SrServico.listarComAtendentePorPessoaEItem(eeh(), sysTrab()).size());
	}
	
	@Test
	public void fazerAlgunsOutrosTestesComDesignItemServico(){
		assertFalse(1==1);
	}

	@Test
	public void testarDesignacaoReferenciandoItemFechado() {
		assertFalse(1==1);
	}
	
	@Test
	public void verSeTesteiBemOQueEhProvavelQOUsuarioInsira() {
		assertFalse(1==1);
	}

}
