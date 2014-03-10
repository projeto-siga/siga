package model;

import static model.TestUtil.apagaCacheAssociacao;
import static model.TestUtil.apagaCacheDesignacao;
import static model.TestUtil.attNumDoc;
import static model.TestUtil.attPrazo;
import static model.TestUtil.barroso;
import static model.TestUtil.criarDadosBasicos;
import static model.TestUtil.criarSoft;
import static model.TestUtil.csis;
import static model.TestUtil.eeh;
import static model.TestUtil.funcionarioTRF;
import static model.TestUtil.limparBase;
import static model.TestUtil.manterSoft;
import static model.TestUtil.prepararSessao;
import static model.TestUtil.tipoPerguntaNota1A5;
import static model.TestUtil.tipoPerguntaTexto;
import static model.TestUtil.sesia;
import static model.TestUtil.sesuti;
import static model.TestUtil.sigadoc;
import static model.TestUtil.sysdoc;
import static model.TestUtil.systrab;
import static model.TestUtil.t2;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.SrAcao;
import models.SrArquivo;
import models.SrConfiguracao;
import models.SrLista;
import models.SrMarca;
import models.SrMovimentacao;
import models.SrPergunta;
import models.SrPesquisa;
import models.SrResposta;
import models.SrSolicitacao;
import models.SrTipoAtributo;
import models.SrTipoMovimentacao;
import models.SrTipoPergunta;

import org.apache.commons.collections.ListUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.test.UnitTest;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrSolicitacaoTest extends UnitTest {

	@BeforeClass
	public static void antesDeTudo() throws Exception {
		criarDadosBasicos();
		prepararSessao();
	}

	@After
	public void depoisDeCadaMetodo() throws Exception {
		SrMarca.deleteAll();
		CpMarca.deleteAll();
		for (SrMovimentacao m : SrMovimentacao.find("order by dtIniMov asc")
				.<SrMovimentacao> fetch())
			m.delete();
		SrLista.deleteAll();
		for (SrSolicitacao s : SrSolicitacao.find("order by hisDtIni desc")
				.<SrSolicitacao> fetch())
			s.delete();
		for (SrConfiguracao c : SrConfiguracao.find("order by hisDtIni desc")
				.<SrConfiguracao> fetch())
			c.delete();
		for (SrPergunta p : SrPergunta.find("order by hisDtIni desc")
				.<SrPergunta> fetch())
			p.delete();
		for (SrPesquisa p : SrPesquisa.find("order by hisDtIni desc")
				.<SrPesquisa> fetch())
			p.delete();
		apagaCacheDesignacao();
	}

	@AfterClass
	public static void depoisDeTudo() throws Exception {
		limparBase();
	}

	@Test
	public void listarItensSelecionaveis() throws Exception {

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

	}

	@Test
	public void listarAcoesSelecionaveis() throws Exception {

		// Nao traz nenhuma, pois nao ha item escolhido
		SrSolicitacao sol = new SrSolicitacao();
		sol.itemConfiguracao = null;
		sol.solicitante = eeh();
		assertEquals(0, sol.getAcoesDisponiveis().size());

		// Nao traz nenhum, pois item escolhido esta fora do escopo da
		// designacao
		SrConfiguracao design = new SrConfiguracao();
		design.itemConfiguracao = sysdoc();
		design.atendente = csis();
		design.salvarComoDesignacao();
		apagaCacheDesignacao();
		sol.itemConfiguracao = systrab();
		assertEquals(0, sol.getAcoesDisponiveis().size());

		// Traz todas as acoes, mas associadas à mesma lotacao, que eh a
		// definida na designacao
		sol.itemConfiguracao = sigadoc();
		Map<SrAcao, DpLotacao> acoesELotas = sol
				.getAcoesDisponiveisComAtendente();
		assertEquals(2, acoesELotas.size());
		assertTrue(acoesELotas.get(manterSoft()).equivale(csis()));
		assertTrue(acoesELotas.get(criarSoft()).equivale(csis()));

		// Traz todas as ações,ainda associadas à mesma lotacao, visto que a
		// nova design criada abaixo nao fica visivel por ser de menor
		// prioridade do que a designacao anterior, que define item
		SrConfiguracao design2 = new SrConfiguracao();
		design2.acao = manterSoft();
		design2.atendente = sesia();
		design2.salvarComoDesignacao();
		apagaCacheDesignacao();
		acoesELotas = sol.getAcoesDisponiveisComAtendente();
		assertEquals(2, acoesELotas.size());
		assertTrue(acoesELotas.get(manterSoft()).equivale(csis()));
		assertTrue(acoesELotas.get(criarSoft()).equivale(csis()));

		// Cada acao com uma lotacao - design2 fica visivel por passar a ter
		// especificidade maior
		design2.itemConfiguracao = sigadoc();
		design2.salvar();
		apagaCacheDesignacao();
		acoesELotas = sol.getAcoesDisponiveisComAtendente();
		assertEquals(2, acoesELotas.size());
		assertTrue(acoesELotas.get(manterSoft()).equivale(sesia()));
		assertTrue(acoesELotas.get(criarSoft()).equivale(csis()));
	}

	@Test
	public void listarAtributos() throws Exception {

		SrConfiguracao design = new SrConfiguracao();
		design.tipoAtributo = attPrazo();
		design.itemConfiguracao = sysdoc();
		design.salvarComoAssociacaoTipoAtributo();
		apagaCacheAssociacao();

		SrConfiguracao design2 = new SrConfiguracao();
		design2.tipoAtributo = attNumDoc();
		design2.acao = manterSoft();
		design2.salvarComoAssociacaoTipoAtributo();
		apagaCacheAssociacao();

		// Nao traz nenhum, pois nao ha item escolhido
		SrSolicitacao sol = new SrSolicitacao();
		sol.solicitante = eeh();
		assertEquals(0, sol.getTiposAtributoAssociados().size());

		// Nao traz nenhum, pois item estah fora do escopo das designacoes
		sol.itemConfiguracao = systrab();
		assertEquals(0, sol.getTiposAtributoAssociados().size());

		// Traz um
		sol.itemConfiguracao = sigadoc();
		assertEquals(1, sol.getTiposAtributoAssociados().size());

		// Traz os dois
		sol.acao = manterSoft();
		assertEquals(2, sol.getTiposAtributoAssociados().size());

		// Garantir que a mudanca no nome do tipo de atributo funciona
		SrTipoAtributo attNumDoc = attNumDoc();
		attNumDoc.nomeTipoAtributo = "Numero do Expediente";
		attNumDoc.salvar();
		boolean mudouMesmo = false;
		for (SrTipoAtributo t : sol.getTiposAtributoAssociados())
			if (t.nomeTipoAtributo.contains("Expediente"))
				mudouMesmo = true;
		assertTrue(mudouMesmo);
	}

	@Test
	public void editarEVerSeHistoricoFicaOk() throws Exception {
		// Ver se a lista de movimentações é a mesma
		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();

		SrMovimentacao mov = new SrMovimentacao();
		mov.solicitacao = sol;
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
		mov.salvar(eeh(), sesia());

		sol.descrSolicitacao = "Mudei descricao";
		sol.salvar();
		sol.refresh();
		sol.solicitacaoInicial.refresh();
		assertFalse(sol.equals(sol.solicitacaoInicial));

		SrMovimentacao mov2 = new SrMovimentacao();
		mov2.solicitacao = sol;
		mov2.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
		mov2.salvar();
		sol.refresh();
		assertEquals(3, sol.getMovimentacaoSet().size());
	}

	@Test
	public void carregarLocalERamal() throws Exception {

		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.local = barroso();
		sol.telPrincipal = "ramal 9700";
		sol.salvar();

		SrSolicitacao sol2 = new SrSolicitacao();
		sol2.solicitante = eeh();
		sol2.cadastrante = eeh();
		sol2.deduzirLocalERamal();
		assertEquals(barroso(), sol2.local);
		assertEquals("ramal 9700", sol2.telPrincipal);
		sol2.local = null;
		sol2.salvar();

		SrSolicitacao sol3 = new SrSolicitacao();
		sol3.solicitante = eeh();
		sol3.cadastrante = eeh();
		sol3.deduzirLocalERamal();
		assertEquals(null, sol3.local);
		assertEquals("ramal 9700", sol3.telPrincipal);
	}

	@Test
	public void preAtendimento() throws Exception {
		SrConfiguracao d = new SrConfiguracao();
		d.preAtendente = csis();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();
		assertTrue(sol.isMarcada(
				CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO, csis()));

		assertTrue(sol.isEmPreAtendimento());
		assertTrue(sol.podeFinalizarPreAtendimento(csis(), eeh()));
		assertFalse(sol.podeFinalizarPreAtendimento(sesia(), eeh()));
	}

	@Test
	public void transferenciaECancelamentoDaTransferencia() throws Exception {
		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();

		SrMovimentacao mov = new SrMovimentacao();
		mov.lotaAtendente = sesuti();
		mov.solicitacao = sol;
		mov.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
		mov.salvar(eeh(), sesia());
		assertTrue(sol.estaCom(sesuti(), funcionarioTRF()));

		sol.desfazerUltimaMovimentacao(eeh(), sesia());
		assertTrue(sol.estaCom(sesia(), eeh()));

		// Nao pode desfazer a primeira mov da solicitacao
		assertFalse(sol.podeDesfazerMovimentacao(sesia(), eeh()));

	}

	@Test
	public void deixarPendente() throws Exception {
		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();

		sol.deixarPendente(sesia(), eeh());
		assertTrue(sol.isPendente());
		assertTrue(sol.isMarcada(CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
				sesia()));

		sol.terminarPendencia(sesia(), eeh());
		assertFalse(sol.isPendente());
		assertFalse(sol.isMarcada(CpMarcador.MARCADOR_SOLICITACAO_PENDENTE,
				sesia()));
	}

	@Test
	public void listaPrioridade() throws Exception {
		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.salvarComoDesignacao();

		SrLista lista = new SrLista();
		lista.nomeLista = "Lista de Teste";
		lista.lotaCadastrante = sesia();
		lista.salvar();
		lista.refresh();

		SrLista lista2 = new SrLista();
		lista2.nomeLista = "Lista de Teste de Outra Lotação";
		lista2.lotaCadastrante = csis();
		lista2.salvar();
		lista2.refresh();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();

		SrSolicitacao sol2 = new SrSolicitacao();
		sol2.cadastrante = eeh();
		sol2.solicitante = eeh();
		sol2.salvar();

		SrSolicitacao sol3 = new SrSolicitacao();
		sol3.cadastrante = eeh();
		sol3.solicitante = eeh();
		sol3.salvar();

		assertEquals(1, sol.getListasDisponiveisParaInclusao(sesia()).size());

		// Incluir as solicitacoes na lista e checar ordem
		sol.associarLista(lista, eeh(), sesia());
		sol2.associarLista(lista, eeh(), sesia());
		sol3.associarLista(lista, eeh(), sesia());
		assertEquals(1, sol.getPrioridadeNaLista(lista));
		assertEquals(2, sol2.getPrioridadeNaLista(lista));
		assertEquals(3, sol3.getPrioridadeNaLista(lista));

		// Tirando a sol nº1, a 2 e 3 mudam de ordem para ocupar a vaga
		sol.desassociarLista(lista, eeh(), sesia());
		assertEquals(1, sol2.getPrioridadeNaLista(lista));
		assertEquals(2, sol3.getPrioridadeNaLista(lista));
		assertFalse(sol.isEmLista(lista));

		// Editar a lista e garantir que continua ok, retirando
		// a sol nº 3 e checando ordem
		lista.nomeLista = "Lista de Teste alterada";
		lista.salvar();
		lista.refresh();
		lista.listaInicial.refresh();
		assertTrue(sol2.isEmLista(lista));
		assertTrue(sol2.isEmLista(lista.listaInicial));
		sol3.desassociarLista(lista, eeh(), sesia());
		assertEquals(1, sol2.getPrioridadeNaLista(lista));
		assertFalse(sol3.isEmLista(lista));

		// reincluir as solicitações retiradas (fica 2-1-3), mandar reordenar
		// pra 3-1-2 e checar ordem, conferindo se a nº 1 ficou sem mov
		// de priorização, visto que não saiu da posição
		sol.associarLista(lista, eeh(), sesia());
		sol3.associarLista(lista, eeh(), sesia());
		lista.priorizar(eeh(), sesia(),
				Arrays.asList(new SrSolicitacao[] { sol3, sol, sol2 }));
		assertEquals(1, sol3.getPrioridadeNaLista(lista));
		assertEquals(2, sol.getPrioridadeNaLista(lista));
		assertEquals(3, sol2.getPrioridadeNaLista(lista));
		assertNull(sol
				.getUltimaMovimentacaoPorTipo(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ALTERACAO_PRIORIDADE_LISTA));
	}

	@Test
	public void posAtendimento() throws Exception {
		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.posAtendente = csis();
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = eeh();
		sol.salvar();

		sol.fechar(sesia(), eeh(), "Fechando. Iniciar pós-atendimento.");
		assertTrue(sol.isEmPosAtendimento());
		assertTrue(sol.isMarcada(
				CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO, csis()));
		assertFalse(sol.podeFechar(sesia(), null));
		assertTrue(sol.podeFechar(csis(), null));

		sol.fechar(csis(), eeh(), "Fechando em definitivo");
		assertFalse(sol.isEmPosAtendimento());
		assertTrue(sol.isFechado());
	}

	@Test
	public void pesquisaSatisfacao() throws Exception {

		SrPesquisa pesq = new SrPesquisa();
		pesq.salvar();

		SrPergunta pergunta1 = new SrPergunta();
		pergunta1.ordemPergunta = 2L;
		pergunta1.pesquisa = pesq;
		pergunta1.tipoPergunta = tipoPerguntaTexto();
		pergunta1.salvar();

		SrPergunta pergunta2 = new SrPergunta();
		pergunta2.ordemPergunta = 1L;
		pergunta2.pesquisa = pesq;
		pergunta2.tipoPergunta = tipoPerguntaNota1A5();

		SrConfiguracao d = new SrConfiguracao();
		d.atendente = sesia();
		d.pesquisaSatisfacao = pesq;
		d.salvarComoDesignacao();

		SrSolicitacao sol = new SrSolicitacao();
		sol.cadastrante = eeh();
		sol.solicitante = funcionarioTRF();
		sol.salvar();

		sol.fechar(sesia(), eeh(), "Fechando. Enviar pesquisa...");
		assertTrue(sol.isFechadoParcialmente());
		assertTrue(sol.isMarcada(
				CpMarcador.MARCADOR_SOLICITACAO_FECHADO_PARCIAL, sesia()));

		// Não pode fechar diretamente nesse estágio
		assertFalse(sol.podeFechar(sesia(), eeh()));

		// Soh quem pode responder a pesquisa eh o subscritor
		assertFalse(sol.podeResponderPesquisa(sesia(), eeh()));
		assertTrue(sol.podeResponderPesquisa(sesuti(), funcionarioTRF()));
		
		// Responder pesquisa
		// ...
		
		// Controle de qualidade
		// ...
		
		// Fechar definitivamente
		// ...
	}

}
