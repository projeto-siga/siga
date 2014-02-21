package model;

import static org.junit.Assert.*;

import java.util.List;

import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrItemConfiguracao;
import models.SrAcao;
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

public class SrItemAcaoDesignacaoTest extends UnitTest {

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
					null,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
					SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE,
					new int[] { SrConfiguracaoBL.ITEM_CONFIGURACAO,
							SrConfiguracaoBL.ACAO }).get(0);
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
	public void testarCriacaoAcao() throws Exception {

		SrAcao acaoSoft = new SrAcao();
		acaoSoft.descrAcao = "Ação para Software";
		acaoSoft.siglaAcao = "99.00";
		acaoSoft.tituloAcao = "Ação para Software";
		acaoSoft.salvar();

		SrAcao criar = new SrAcao();
		criar.descrAcao = "Criar";
		criar.siglaAcao = "99.01";
		criar.tituloAcao = "Criar";
		criar.salvar();

		SrAcao manter = new SrAcao();
		manter.descrAcao = "Manter";
		manter.siglaAcao = "99.02";
		manter.tituloAcao = "Manter";
		manter.salvar();

		SrAcao filtro = new SrAcao();
		filtro.siglaAcao = "99.";
		assertEquals(3, filtro.buscar().size());
		assertEquals(3, SrAcao.listar().size());

		for (SrAcao acao : SrAcao.listar()) {
			assertNull(acao.acaoInicial);
			acao.refresh();
			assertNotNull(acao.acaoInicial);
		}
	}

	@Test
	public void listarItemEAcaoSemHaverDesignTrazVazio() throws Exception {
		prepararSessao();
		assertEquals(0, SrItemConfiguracao.listarPorPessoaELocal(eeh(), null)
				.size());
		assertEquals(0,
				SrAcao.listarComAtendentePorPessoaLocalEItem(eeh(), null, null)
						.size());
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

		assertEquals(0, SrItemConfiguracao.listarPorPessoaELocal(eeh(), null)
				.size());

	}

	@Test
	public void listarItemHavendoApenasDesignSemItemTrazTodos()
			throws Exception {
		SrConfiguracao design = new SrConfiguracao();
		design.atendente = sesia();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();

		List<SrItemConfiguracao> itens = SrItemConfiguracao
				.listarPorPessoaELocal(eeh(), null);
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
				.listarPorPessoaELocal(eeh(), null);

		// Edson: 99.00.00, 99.01.00, 99.01.01, 99.01.02
		assertEquals(4, itens.size());

	}

	@Test
	public void listarAcaoPorItemHavendoDesignProItemSemAcaoTrazTodosDeNivel2()
			throws Exception {
		// Edson: Traz 99.01, 99.02. Apesar de não haver designação diretamente
		// pro SigaDoc, há para sysDoc
		assertEquals(
				2,
				SrAcao.listarComAtendentePorPessoaLocalEItem(eeh(), null,
						sigaDoc()).size());
	}

	@Test
	public void listarAcaoPorItemNaoHavendoDesignProItemTrazVazio()
			throws Exception {
		// Edson: vazio porque não há designação pro sysTrab
		assertEquals(
				0,
				SrAcao.listarComAtendentePorPessoaLocalEItem(eeh(), null,
						sysTrab()).size());
	}

	@Test
	public void fazerAlgunsOutrosTestesComDesignItemAcao() {
		assertFalse(1 == 1);
	}

	@Test
	public void testarDesignacaoReferenciandoItemFechado() {
		assertFalse(1 == 1);
	}

	@Test
	public void verSeTesteiBemOQueEhProvavelQOUsuarioInsira() {
		assertFalse(1 == 1);
	}

}
