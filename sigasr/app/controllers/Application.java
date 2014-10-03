package controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.Query;
import javax.xml.parsers.ParserConfigurationException;

import models.Sr;
import models.SrAcao;
import models.SrArquivo;
import models.SrAtributo;
import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrGravidade;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrMovimentacao;
import models.SrPergunta;
import models.SrPesquisa;
import models.SrResposta;
import models.SrSolicitacao;
import models.SrTipoAtributo;
import models.SrTipoMovimentacao;
import models.SrTipoPergunta;
import models.SrUrgencia;

import org.joda.time.LocalDate;

import play.Logger;
import play.Play;
import play.data.binding.As;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http;
import reports.SrRelAgendado;
import reports.SrRelLocal;
import reports.SrRelPesquisa;
import reports.SrRelPrazo;
import reports.SrRelPrazoDetail;
import reports.SrRelPrazoTRF;
import reports.SrRelSolicitacoes;
import reports.SrRelTransferencias;
import util.SrSolicitacaoAtendidos;
import util.SrSolicitacaoFiltro;
import util.SrSolicitacaoItem;
import br.gov.jfrj.siga.base.ConexaoHTTP;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.test.Lotacao;

public class Application extends SigaApplication {

	@Before(priority = 1)
	public static void addDefaultsAlways() throws Exception {
		prepararSessao();
		SrConfiguracaoBL.get().limparCacheSeNecessario();
	}

	@Before(priority = 2, unless = { "exibirAtendente", "exibirLocalERamal",
			"exibirItemConfiguracao" })
	public static void addDefaults() throws Exception {

		try {
			obterCabecalhoEUsuario("rgb(235, 235, 232)");
			assertAcesso("");
		} catch (Exception e) {
			tratarExcecoes(e);
		}

		try {
			assertAcesso("ADM:Administrar");
			renderArgs.put("exibirMenuAdministrar", true);
		} catch (Exception e) {
			renderArgs.put("exibirMenuAdministrar", false);
		}

		try {
			assertAcesso("REL:Relatorios");
			renderArgs.put("exibirMenuRelatorios", true);
		} catch (Exception e) {
			renderArgs.put("exibirMenuRelatorios", false);
		}
	}

	public static boolean completo() {
		return Boolean.parseBoolean(params.get("completo"));
	}

	protected static void assertAcesso(String path) throws Exception {
		SigaApplication.assertAcesso("SR:Módulo de Serviços;" + path);
	}

	@Catch()
	public static void tratarExcecoes(Exception e) {
		SigaApplication.tratarExcecoes(e);
	}

	public static void index() throws Exception {
		editar(null);
	}

	public static void gadget() {
		Query query = JPA.em().createNamedQuery("contarSrMarcas");
		query.setParameter("idPessoaIni", titular().getIdInicial());
		query.setParameter("idLotacaoIni", lotaTitular().getIdInicial());
		List contagens = query.getResultList();
		render(contagens);
	}

	public void corporativo() {
		try {
			Corporativo.dadosrh();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void editar(Long id) throws Exception {
		SrSolicitacao solicitacao;
		if (id == null) {
			solicitacao = new SrSolicitacao();
			solicitacao.solicitante = titular();
		} else
			solicitacao = SrSolicitacao.findById(id);

		formEditar(solicitacao.deduzirLocalERamal());
	}

	@SuppressWarnings("unchecked")
	public static void exibirLocalERamal(SrSolicitacao solicitacao)
			throws Exception {
		render(solicitacao.deduzirLocalERamal());
	}

	public static void exibirAtributos(SrSolicitacao solicitacao)
			throws Exception {
		render(solicitacao);
	}

	public static void exibirItemConfiguracao(SrSolicitacao solicitacao)
			throws Exception {
		if (solicitacao.getItensDisponiveis().contains(
				solicitacao.itemConfiguracao))
			solicitacao.itemConfiguracao = null;

		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao
				.getAcoesDisponiveisComAtendenteOrdemTitulo();
		if (solicitacao.acao == null
				|| !acoesEAtendentes.containsKey(solicitacao.acao)) {
			if (acoesEAtendentes.size() > 0)
				solicitacao.acao = acoesEAtendentes.keySet().iterator().next();
			else
				solicitacao.acao = null;
		}
		render(solicitacao, acoesEAtendentes);
	}

	public static void exibirAcao(SrSolicitacao solicitacao) throws Exception {
		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao
				.getAcoesDisponiveisComAtendenteOrdemTitulo();
		if (solicitacao.acao == null
				|| !acoesEAtendentes.containsKey(solicitacao.acao)) {
			if (acoesEAtendentes.size() > 0)
				solicitacao.acao = acoesEAtendentes.keySet().iterator().next();
			else
				solicitacao.acao = null;
		}

		render(solicitacao, acoesEAtendentes);
	}

	public static void exibirConhecimentosRelacionados(SrSolicitacao solicitacao)
			throws Exception {
		render(solicitacao);
	}

	private static void formEditar(SrSolicitacao solicitacao) throws Exception {

		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();

		Map<SrAcao, DpLotacao> acoesEAtendentes = new TreeMap<SrAcao, DpLotacao>();
		if (solicitacao.itemConfiguracao != null) {
			acoesEAtendentes = solicitacao
					.getAcoesDisponiveisComAtendenteOrdemTitulo();
			if (solicitacao.acao == null
					|| !acoesEAtendentes.containsKey(solicitacao.acao)) {
				if (acoesEAtendentes.size() > 0)
					solicitacao.acao = acoesEAtendentes.keySet().iterator()
							.next();
				else
					solicitacao.acao = null;
			}
		}

		render("@editar", solicitacao, locais, acoesEAtendentes);
	}

	private static void validarFormEditar(SrSolicitacao solicitacao)
			throws Exception {

		if (solicitacao.itemConfiguracao == null) {
			validation.addError("solicitacao.itemConfiguracao",
					"Item não informado");
		}
		if (solicitacao.acao == null) {
			validation.addError("solicitacao.acao", "Ação não informada");
		}

		if (solicitacao.descrSolicitacao == null
				|| solicitacao.descrSolicitacao.trim().equals("")) {
			validation.addError("solicitacao.descrSolicitacao",
					"Descrição não informada");
		}

		HashMap<Long, Boolean> obrigatorio = solicitacao
				.getObrigatoriedadeTiposAtributoAssociados();
		for (SrAtributo att : solicitacao.getAtributoSet()) {
			if (att.valorAtributo.trim().equals("")
					&& obrigatorio.get(att.tipoAtributo.idTipoAtributo))
				validation.addError("solicitacao.atributoMap["
						+ att.tipoAtributo.idTipoAtributo + "]",
						att.tipoAtributo.nomeTipoAtributo + " não informado");
		}

		if (validation.hasErrors()) {
			formEditar(solicitacao);
		}
	}

	private static void validarFormEditarItem(
			SrItemConfiguracao itemConfiguracao) throws Exception {

		if (itemConfiguracao.siglaItemConfiguracao.equals("")) {
			validation.addError("itemConfiguracao.siglaItemConfiguracao",
					"Código não informado");
		}

		if (itemConfiguracao.tituloItemConfiguracao.equals("")) {
			validation.addError("itemConfiguracao.tituloItemConfiguracao",
					"Título não informado");
		}

		for (play.data.validation.Error error : validation.errors()) {
			System.out.println(error.message());
		}

		if (validation.hasErrors()) {
			render("@editarItem", itemConfiguracao);
		}
	}

	private static void validarFormEditarAcao(SrAcao acao) {

		if (acao.siglaAcao.equals("")) {
			validation.addError("acao.siglaAcao", "Código não informado");
		}

		if (acao.tituloAcao.equals("")) {
			validation.addError("acao.tituloAcao", "Título não informado");
		}

		if (validation.hasErrors()) {
			render("@editarAcao", acao);
		}

	}

	private static void validarFormEditarDesignacao(SrConfiguracao designacao) {

		if ((designacao.atendente == null) && (designacao.preAtendente == null)
				&& (designacao.posAtendente == null)
				&& (designacao.equipeQualidade == null)) {
			validation.addError("designacao.atendente",
					"Atendente não informado.");
			validation.addError("designacao.preAtendente",
					"Pré-atendente não informado.");
			validation.addError("designacao.posAtendente",
					"Pós-atendente não informado.");
			validation.addError("designacao.equipeQualidade",
					"Equipe de qualidade não informada.");
		}

		if ((designacao.itemConfiguracao == null) && (designacao.acao == null)) {
			validation.addError("designacao.itemConfiguracao",
					"Código não informado.");
			validation.addError("designacao.acao", "Código não informado.");
		}

		for (play.data.validation.Error error : validation.errors()) {
			System.out.println(error.message());
		}

		if (validation.hasErrors()) {
			List<CpOrgaoUsuario> orgaos = JPA.em()
					.createQuery("from CpOrgaoUsuario").getResultList();
			render("@editarDesignacao", designacao, orgaos);
		}
	}

	private static void validarFormEditarPermissaoUsoLista(
			SrConfiguracao designacao) {
	}

	public static void gravar(SrSolicitacao solicitacao) throws Exception {
		validarFormEditar(solicitacao);
		solicitacao.salvar(cadastrante(), lotaTitular());
		Long id = solicitacao.idSolicitacao;
		exibir(id, completo());
	}

	public static void listar(SrSolicitacaoFiltro filtro) throws Exception {

		List<SrSolicitacao> listaSolicitacao;

		if (filtro.pesquisar)
			listaSolicitacao = filtro.buscar();
		else
			listaSolicitacao = new ArrayList<SrSolicitacao>();

		// Montando o filtro...
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();

		render(listaSolicitacao, tipos, marcadores, filtro);
	}

	public static void estatistica() throws Exception {
		assertAcesso("REL:Relatorios");
		List<SrSolicitacao> lista = SrSolicitacao.all().fetch();

		List<String[]> listaSols = SrSolicitacao
				.find("select sol.gravidade, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao and movimentacao.tipoMov <> 7 "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.gravidade").fetch();

		LocalDate ld = new LocalDate();
		ld = new LocalDate(ld.getYear(), ld.getMonthOfYear(), 1);

		// Header
		StringBuilder sb = new StringBuilder();
		sb.append("['Prioridade','Total'],");

		// Values
		for (Iterator<String[]> it = listaSols.iterator(); it.hasNext();) {
			Object[] obj = (Object[]) it.next();
			SrGravidade gravidade = (SrGravidade) obj[0];
			Long total = (Long) obj[1];
			sb.append("['");
			sb.append(gravidade.descrGravidade.toString());
			sb.append("',");
			sb.append(total.toString());
			sb.append(",");
			sb.append("],");
		}

		String estat = sb.toString();

		List<SrSolicitacao> listaEvol = SrSolicitacao.all().fetch();

		SrSolicitacaoAtendidos set = new SrSolicitacaoAtendidos();
		List<Object[]> listaEvolSols = SrSolicitacao
				.find("select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getIdLotacao()
						+ " "
						+ "group by extract (month from sol.dtReg), extract (year from sol.dtReg)")
				.fetch();

		for (Object[] sols : listaEvolSols) {
			set.add(new SrSolicitacaoItem((Integer) sols[0], (Integer) sols[1],
					(Long) sols[2], 0, 0));
		}

		List<Object[]> listaFechados = SrSolicitacao
				.find("select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.tipoMov = 7 "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId()
						+ " "
						+ "group by extract (month from sol.dtReg), extract (year from sol.dtReg)")
				.fetch();
		for (Object[] fechados : listaFechados) {
			set.add(new SrSolicitacaoItem((Integer) fechados[0],
					(Integer) fechados[1], 0, (Long) fechados[2], 0));
		}

		LocalDate ldt = new LocalDate();
		ldt = new LocalDate(ldt.getYear(), ldt.getMonthOfYear(), 1);

		// Header
		StringBuilder sbevol = new StringBuilder();
		sbevol.append("['Mês','Fechados','Abertos'],");

		// Values
		for (int i = -6; i <= 0; i++) {
			LocalDate ldl = ldt.plusMonths(i);
			sbevol.append("['");
			sbevol.append(ldl.toString("MMM/yy"));
			sbevol.append("',");
			long abertos = 0;
			long fechados = 0;
			SrSolicitacaoItem o = new SrSolicitacaoItem(ldl.getMonthOfYear(),
					ldl.getYear(), 0, 0, 0);
			if (set.contains(o)) {
				o = set.floor(o);
				abertos = o.abertos;
				fechados = o.fechados;
			}
			sbevol.append(fechados);
			sbevol.append(",");
			sbevol.append(abertos);
			sbevol.append(",");
			sbevol.append("],");
		}
		String evolucao = sbevol.toString();

		List<SrSolicitacao> top = SrSolicitacao.all().fetch();

		List<String[]> listaTop = SrSolicitacao
				.find("select sol.itemConfiguracao.tituloItemConfiguracao, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.itemConfiguracao.tituloItemConfiguracao")
				.fetch();

		// Header
		StringBuilder sbtop = new StringBuilder();
		sbtop.append("['Item de Configuração','Total'],");

		// Values
		for (Iterator<String[]> itop = listaTop.iterator(); itop.hasNext();) {
			Object[] obj = (Object[]) itop.next();
			String itensconf = (String) obj[0];
			Long total = (Long) obj[1];
			sbtop.append("['");
			sbtop.append(itensconf);
			sbtop.append("',");
			sbtop.append(total.toString());
			sbtop.append(",");
			sbtop.append("],");
		}

		String top10 = sbtop.toString();

		List<SrSolicitacao> lstgut = SrSolicitacao.all().fetch();

		List<String[]> listaGUT = SrSolicitacao
				.find("select sol.gravidade, sol.urgencia, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.gravidade, sol.urgencia").fetch();

		// Header
		StringBuilder sbGUT = new StringBuilder();
		sbGUT.append("['Gravidade','Urgência','Total'],");

		// Values
		for (Iterator<String[]> itgut = listaGUT.iterator(); itgut.hasNext();) {
			Object[] obj = (Object[]) itgut.next();
			SrGravidade gravidade = (SrGravidade) obj[0];
			SrUrgencia urgencia = (SrUrgencia) obj[1];
			Long total = (Long) obj[2];
			sbGUT.append("['");
			sbGUT.append(gravidade.descrGravidade.toString());
			sbGUT.append("','");
			sbGUT.append(urgencia.descrUrgencia.toString());
			sbGUT.append("',");
			sbGUT.append(total.toString());
			sbGUT.append(",");
			sbGUT.append("],");
		}

		String gut = sbGUT.toString();
		render(lista, evolucao, top10);
	}

	public static void exibir(Long id, boolean completo) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		if (solicitacao == null)
			throw new Exception("Solicitação não encontrada");
		else
			solicitacao = solicitacao.getSolicitacaoAtual();
		SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);
		render(solicitacao, movimentacao, completo);
	}

	public static void exibirLista(Long id) throws Exception {
		SrLista lista = SrLista.findById(id);
		render(lista);
	}
	
	public static void associarLista(Long idSolicitacao) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(idSolicitacao);
		solicitacao = solicitacao.getSolicitacaoAtual();
		render(solicitacao);
	}

	public static void associarListaGravar(Long idSolicitacao, Long idLista)
			throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(idSolicitacao);
		SrLista lista = SrLista.findById(idLista);
		solicitacao.associarLista(lista, cadastrante(), lotaTitular());
		exibir(idSolicitacao, completo());
	}

	public static void desassociarLista(Long idSolicitacao, Long idLista)
			throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(idSolicitacao);
		SrLista lista = SrLista.findById(idLista);
		solicitacao.desassociarLista(lista, cadastrante(), lotaTitular());
		exibirLista(idLista);
	}

	public static void priorizarLista(@As(",") List<Long> ids, Long id)
			throws Exception {

		// Edson: as 3 linhas abaixo nao deveriam estar sendo necessarias, mas o
		// Play
		// nao estah fazendo o binding direito caso o parametro seja
		// List<SrSolicitacao>
		// em vez de List<Long>. Ver o que estah havendo.
		List<SrSolicitacao> sols = new ArrayList<SrSolicitacao>();
		for (Long l : ids)
			sols.add((SrSolicitacao) SrSolicitacao.findById(l));

		SrLista lista = SrLista.findById(id);
		lista.priorizar(cadastrante(), lotaTitular(), sols);
		exibirLista(id);
	}

	public static void selecionar(String sigla) throws Exception {
		SrSolicitacao sel = new SrSolicitacao();
		sel.cadastrante = cadastrante();
		sel = (SrSolicitacao) sel.selecionar(sigla);
		render("@selecionar", sel);
	}

	public static void baixar(Long idArquivo) {
		SrArquivo arq = SrArquivo.findById(idArquivo);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
	}

	public static void exibirPesquisa(Long idMovimentacao) throws Exception {
		SrMovimentacao movimentacao = SrMovimentacao.findById(idMovimentacao);
		// exibir(mov.solicitacao.idSolicitacao, completo());
		SrPesquisa pesquisa = movimentacao.pesquisa;
		Set<SrPergunta> perguntas = movimentacao.pesquisa
				.getPerguntaSetAtivas();
		render(movimentacao, pesquisa, perguntas);
	}

	public static void darAndamento(SrMovimentacao movimentacao)
			throws Exception {
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
		movimentacao.salvar(cadastrante(), lotaTitular());
		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void anexarArquivo(SrMovimentacao movimentacao)
			throws Exception {
		movimentacao.salvar(cadastrante(), lotaTitular());
		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void fechar(Long id, String motivo) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.fechar(lotaTitular(), cadastrante(), motivo);
		exibir(sol.idSolicitacao, completo());
	}

	public static void responderPesquisa(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		if (sol.getPesquisaDesignada() == null)
			throw new Exception(
					"Não foi encontrada nenhuma pesquisa designada para esta solicitação.");
		render(sol);
	}

	public static void responderPesquisaGravar(Long id,
			HashMap<Long, Object> respostaMap) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		SrMovimentacao movimentacao = new SrMovimentacao();
		List<SrResposta> respostas = movimentacao.setRespostaMap(respostaMap);
		sol.responderPesquisa(lotaTitular(), cadastrante(), respostas);
	}

	public static void retornarAoAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.retornarAoAtendimento(lotaTitular(), cadastrante());
		exibir(id, completo());
	}

	public static void termoAtendimento(Long id) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		render(solicitacao);
	}

	public static void cancelar(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.cancelar(lotaTitular(), cadastrante());
		exibir(id, completo());
	}

	public static void finalizarPreAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.finalizarPreAtendimento(lotaTitular(), cadastrante());
		exibir(sol.idSolicitacao, completo());
	}

	public static void retornarAoPreAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.retornarAoPreAtendimento(lotaTitular(), cadastrante());
		exibir(sol.idSolicitacao, completo());
	}

	public static void deixarPendente(Long id, String motivo,
			String calendario, String horario) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.deixarPendente(lotaTitular(), cadastrante(), motivo, calendario,
				horario);
		exibir(id, completo());
	}

	public static void terminarPendencia(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.terminarPendencia(lotaTitular(), cadastrante());
		exibir(id, completo());
	}

	public static void reabrir(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.reabrir(lotaTitular(), cadastrante());
		exibir(id, completo());
	}

	public static void desfazerUltimaMovimentacao(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.desfazerUltimaMovimentacao(cadastrante(), lotaTitular());
		exibir(id, completo());
	}

	public static void criarFilha(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		SrSolicitacao filha = sol.criarFilhaSemSalvar();
		formEditar(filha);
	}

	public static void listarDesignacao() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes();
		render(designacoes);
	}

	public static void editarDesignacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		List<CpOrgaoUsuario> orgaos = JPA.em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.all().fetch();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.find(
				"hisDtFim is null").fetch();
		SrConfiguracao designacao = new SrConfiguracao();
		if (id != null)
			designacao = JPA.em().find(SrConfiguracao.class, id);
		render(designacao, orgaos, locais, pesquisaSatisfacao);
	}

	public static void gravarDesignacao(SrConfiguracao designacao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarDesignacao(designacao);
		designacao.salvarComoDesignacao();
		listarDesignacao();
	}

	public static void desativarDesignacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.finalizar();
		listarDesignacao();
	}

	public static void listarPermissaoUsoLista() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> permissoes = SrConfiguracao
				.listarPermissoesUsoLista(lotaTitular());
		render(permissoes);
	}

	public static void editarPermissaoUsoLista(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		List<CpOrgaoUsuario> orgaos = JPA.em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.all().fetch();
		List<SrLista> listasPrioridade = SrLista
				.getCriadasPelaLotacao(lotaTitular());
		SrConfiguracao permissao = new SrConfiguracao();
		if (id != null)
			permissao = JPA.em().find(SrConfiguracao.class, id);
		render(permissao, orgaos, locais, listasPrioridade);
	}

	public static void gravarPermissaoUsoLista(SrConfiguracao permissao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarPermissaoUsoLista(permissao);
		permissao.salvarComoPermissaoUsoLista();
		listarPermissaoUsoLista();
	}

	public static void desativarPermissaoUsoLista(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.finalizar();
		listarDesignacao();
	}

	public static void listarAssociacao() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> listaAssociacao = SrConfiguracao
				.listarAssociacoesTipoAtributo();
		render(listaAssociacao);
	}

	public static void editarAssociacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = new SrConfiguracao();
		if (id != null)
			associacao = (SrConfiguracao) JPA.em()
					.find(SrConfiguracao.class, id).getConfiguracaoAtual();
		render(associacao);
	}

	public static void gravarAssociacao(SrConfiguracao associacao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		associacao.salvarComoAssociacaoTipoAtributo();
		listarAssociacao();
	}

	public static void desativarAssociacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = JPA.em().find(SrConfiguracao.class, id);
		associacao.finalizar();
		listarAssociacao();
	}

	public static void listarItem() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrItemConfiguracao> itens = SrItemConfiguracao.listar();
		render(itens);
	}

	public static void editarItem(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrItemConfiguracao itemConfiguracao = new SrItemConfiguracao();
		if (id != null)
			itemConfiguracao = SrItemConfiguracao.findById(id);
		render(itemConfiguracao);
	}

	public static void gravarItem(SrItemConfiguracao itemConfiguracao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarItem(itemConfiguracao);
		itemConfiguracao.salvar();

		// Atualiza os conhecimentos relacionados
		// Edson: deveria ser feito por webservice. Nao estah sendo coberta
		// a atualizacao da classificacao quando ocorre mudanca de posicao na
		// hierarquia, pois isso eh mais complexo de acertar.
		try {
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			SrItemConfiguracao anterior = itemConfiguracao
					.getHistoricoItemConfiguracao().get(0);
			if (anterior != null
					&& !itemConfiguracao.tituloItemConfiguracao
							.equals(anterior.tituloItemConfiguracao))
				ConexaoHTTP.get("http://"
						+ Play.configuration.getProperty("servidor.principal")
						+ ":8080/sigagc/app/updateTag?before="
						+ anterior.getTituloSlugify() + "&after="
						+ itemConfiguracao.getTituloSlugify(), atributos);
		} catch (Exception e) {
			Logger.error("Item " + itemConfiguracao.idItemConfiguracao
					+ " salvo, mas nao foi possivel atualizar conhecimento");
			e.printStackTrace();
		}
		
		listarItem();
	}

	public static void desativarItem(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrItemConfiguracao item = SrItemConfiguracao.findById(id);
		item.finalizar();
		listarItem();
	}

	public static void selecionarItem(String sigla, SrSolicitacao sol)
			throws Exception {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla,
				sol.getItensDisponiveis());
		render("@selecionar", sel);
	}

	public static void buscarItem(String sigla, String nome,
			SrItemConfiguracao filtro, SrSolicitacao sol) {

		List<SrItemConfiguracao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(sol != null
					&& (sol.solicitante != null || sol.local != null) ? sol
					.getItensDisponiveis() : null);
		} catch (Exception e) {
			itens = new ArrayList<SrItemConfiguracao>();
		}

		render(itens, filtro, nome, sol);
	}

	public static void listarTipoAtributo() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrTipoAtributo> atts = SrTipoAtributo.listar();
		render(atts);
	}

	public static void editarTipoAtributo(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrTipoAtributo att = new SrTipoAtributo();
		if (id != null)
			att = SrTipoAtributo.findById(id);
		render(att);
	}

	public static void gravarTipoAtributo(SrTipoAtributo att) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarTipoAtributo(att);
		att.salvar();
		listarTipoAtributo();
	}

	private static void validarFormEditarTipoAtributo(SrTipoAtributo att) {
		if (att.nomeTipoAtributo.equals("")) {
			validation.addError("att.nomeTipoAtributo",
					"Nome de atributo não informado");
		}

		for (play.data.validation.Error error : validation.errors()) {
			System.out.println(error.message());
		}

		if (validation.hasErrors()) {
			render("@editarTipoAtributo", att);
		}
	}

	public static void desativarTipoAtributo(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrTipoAtributo item = SrTipoAtributo.findById(id);
		item.finalizar();
		listarTipoAtributo();
	}

	public static void listarPesquisa() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrPesquisa> pesquisas = SrPesquisa.listar();
		render(pesquisas);
	}

	public static void editarPesquisa(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrPesquisa pesq = new SrPesquisa();
		if (id != null)
			pesq = SrPesquisa.findById(id);
		List<SrTipoPergunta> tipos = SrTipoPergunta.all().fetch();
		render(pesq, tipos);
	}

	public static void gravarPesquisa(SrPesquisa pesq) throws Exception {
		assertAcesso("ADM:Administrar");
		pesq.salvar();
		listarPesquisa();
	}

	public static void desativarPesquisa(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrPesquisa pesq = SrPesquisa.findById(id);
		pesq.finalizar();
		listarPesquisa();
	}

	public static void listarAcao() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrAcao> acoes = SrAcao.listar();
		render(acoes);
	}

	public static void editarAcao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.findById(id);
		render(acao);
	}

	public static void gravarAcao(SrAcao acao) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarAcao(acao);
		acao.salvar();
		
		// Atualiza os conhecimentos relacionados. 
		// Edson: deveria ser feito por webservice. Nao estah sendo coberta
		// a atualizacao da classificacao quando ocorre mudanca de posicao na
		// hierarquia, pois isso eh mais complexo de acertar.
		try {
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());
			
			SrAcao anterior = acao
					.getHistoricoAcao().get(0);
			if (anterior != null
					&& !acao.tituloAcao
							.equals(anterior.tituloAcao))
				ConexaoHTTP.get("http://"
						+ Play.configuration.getProperty("servidor.principal")
						+ ":8080/sigagc/app/updateTag?before="
						+ anterior.getTituloSlugify() + "&after="
						+ acao.getTituloSlugify(), atributos);
		} catch (Exception e) {
			Logger.error("Acao " + acao.idAcao
					+ " salva, mas nao foi possivel atualizar conhecimento");
			e.printStackTrace();
		}
				
		listarAcao();
	}

	public static void desativarAcao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcao acao = SrAcao.findById(id);
		acao.finalizar();
		listarAcao();
	}

	public static void selecionarAcao(String sigla, SrSolicitacao sol)
			throws Exception {

		SrAcao sel = new SrAcao().selecionar(sigla, sol.getAcoesDisponiveis());
		render("@selecionar", sel);
	}

	public static void buscarAcao(String sigla, String nome, SrAcao filtro,
			SrSolicitacao sol) {
		List<SrAcao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrAcao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(sol != null
					&& (sol.solicitante != null || sol.local != null) ? sol
					.getAcoesDisponiveis() : null);
		} catch (Exception e) {
			itens = new ArrayList<SrAcao>();
		}

		render(itens, filtro, nome, sol);
	}

	public static void selecionarSiga(String sigla, String tipo, String nome)
			throws Exception {
		redirect("/siga/" + tipo + "/selecionar.action?" + "propriedade="
				+ tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void buscarSiga(String sigla, String tipo, String nome)
			throws Exception {
		redirect("/siga/" + tipo + "/buscar.action?" + "propriedade=" + tipo
				+ nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public static void listarLista() throws Exception {
		List<SrLista> lista = SrLista.listar();
		render(lista);
	}

	public static void editarLista(Long id) throws Exception {
		SrLista lista = new SrLista();
		if (id != null)
			lista = SrLista.findById(id);
		render(lista);
	}

	public static void gravarLista(SrLista lista) throws Exception {
		lista.salvar();
		listarLista();
	}

	public static void desativarLista(Long id) throws Exception {
		SrLista lista = SrLista.findById(id);
		lista.finalizar();
		listarLista();
	}

	public static void relSolicitacoes(SrSolicitacaoFiltro filtro)
			throws Exception {
		assertAcesso("REL:Relatorio");
		// Montando o filtro...
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();
		render(tipos, marcadores, filtro);
	}

	public static void relTransferencias(SrSolicitacao solicitacao)
			throws Exception {
		assertAcesso("REL:Relatorio");
		render();
	}

	public static void relLocal(Long orgao) throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> orgaos = new ArrayList<CpComplexo>();
		orgaos = JPA.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		List<CpComplexo> locOrgaos = new ArrayList<CpComplexo>();
		if (orgao != null) {
			locOrgaos = JPA
					.em()
					.createQuery(
							"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
									+ orgao).getResultList();
			render("app/views/Application/locais.html", locOrgaos);
		}
		render(locais, orgaos);
	}

	public static void relPrazo() throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void relPrazoTRF() throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void relPrazoDetail() throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void relPesquisa() throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void relAgendado() throws Exception {
		assertAcesso("REL:Relatorio");
		List<CpComplexo> orgaos = new ArrayList<CpComplexo>();
		orgaos = JPA.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		locais = JPA
				.em()
				.createQuery(
						"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
								+ lotaTitular().getOrgaoUsuario()
										.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void grelSolicitacoes(String secaoUsuario, String lotacao,
			String situacao, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		if (!lotacao.equals(""))
			parametros.put("lotacao", lotacao);
		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("situacao", situacao);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelSolicitacoes rel = new SrRelSolicitacoes(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Solicitações", pdf.length,
				"application/pdf", true);
	}

	public static void grelTransferencias(String secaoUsuario, String lotacao,
			String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		if (!lotacao.equals(""))
			parametros.put("lotacao", lotacao);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelTransferencias rel = new SrRelTransferencias(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Transferências", pdf.length,
				"application/pdf", true);
	}

	public static void grelLocal(String secaoUsuario, String orgao,
			String lotacao, String locOrgao, String dtIni, String dtFim,
			String atendente) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("orgao", orgao);
		parametros.put("lotacao", lotacao);
		parametros.put("local", locOrgao);
		parametros.put("atendente", atendente);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelLocal rel = new SrRelLocal(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Solicitações por Localidade",
				pdf.length, "application/pdf", true);
	}

	public static void grelPrazo(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim, String atendente)
			throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		// if (!lotacao.equals("")) parametros.put("lotacao", lotacao);
		parametros.put("atendente", atendente);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPrazo rel = new SrRelPrazo(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Prazos", pdf.length, "application/pdf",
				true);
	}

	public static void grelPrazoTRF(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim, String atendente)
			throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("atendente", atendente);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPrazoTRF rel = new SrRelPrazoTRF(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Prazos - TRF", pdf.length,
				"application/pdf", true);
	}

	public static void grelPrazoDetail(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		// parametros.put("lotacao", lotacao);
		if (!lotacao.equals(""))
			parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPrazoDetail rel = new SrRelPrazoDetail(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório Detalhado de Prazos", pdf.length,
				"application/pdf", true);
	}

	public static void grelPesquisa(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPesquisa rel = new SrRelPesquisa(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Indíces de Satisfação", pdf.length,
				"application/pdf", true);
	}

	public static void grelAgendado(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim, String atendente)
			throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("atendente", atendente);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelAgendado rel = new SrRelAgendado(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Solicitações por Localidade",
				pdf.length, "application/pdf", true);
	}

	public static void concluirAutomatico() throws Exception {

		List<SrMovimentacao> movs = SrMovimentacao
				.find("select mov from SrMovimentacao mov "
						+ "where mov.tipoMov = 15 and exists (select 1 from SrMovimentacao movfc  "
						+ "where movfc.solicitacao = mov.solicitacao and movfc.tipoMov = 15 "
						+ "and movfc.dtIniMov = (select max(movm.dtIniMov) from SrMovimentacao movm "
						+ "where movm.solicitacao = movfc.solicitacao and movm.tipoMov = 15)) "
						+ "and not exists (select 1 from SrMovimentacao movrev where movrev.solicitacao = mov.solicitacao "
						+ "and movrev.tipoMov in (7,14,16) and movrev.dtIniMov > mov.dtIniMov)")
				.fetch();

		Set<SrSolicitacao> solsnaoconcluidas = new HashSet<SrSolicitacao>();
		for (int k = 0; k < movs.size(); k++) {
			solsnaoconcluidas.add(movs.get(k).solicitacao);
			System.out.println(movs.get(k).solicitacao.idSolicitacao);
		}

		Iterator it = solsnaoconcluidas.iterator();
		while (it.hasNext()) {
			SrSolicitacao sol = (SrSolicitacao) it.next();
			sol.fechar(null, null, "Conclusão Automática");
		}

		render(solsnaoconcluidas);
	}
}
