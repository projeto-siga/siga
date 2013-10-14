package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;

import models.SrArquivo;
import models.SrAtributo;
import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrFormaAcompanhamento;
import models.SrGravidade;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrMovimentacao;
import models.SrServico;
import models.SrSolicitacao;
import models.SrTendencia;
import models.SrTipoAtributo;
import models.SrTipoMovimentacao;
import models.SrUrgencia;

import org.joda.time.LocalDate;

import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import reports.SrRelLocal;
import reports.SrRelPrazo;
import reports.SrRelPrazoDetail;
import reports.SrRelSolicitacoes;
import reports.SrRelTransferencias;
import util.SrSolicitacaoAtendidos;
import util.SrSolicitacaoFiltro;
import util.SrSolicitacaoItem;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Application extends SigaApplication {

	@Before
	public static void addDefaultsAlways() throws Exception {
		prepararSessao();
		SrConfiguracaoBL.get().limparCacheSeNecessario();
	}

	@Before(unless = { "exibirAtendente", "exibirAtributos",
			"exibirLocalERamal", "exibirItemConfiguracao", "exibirServico" })
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

	public static void editar(Long id) throws Exception {
		SrSolicitacao solicitacao;
		if (id == null) {
			solicitacao = new SrSolicitacao();
			solicitacao.solicitante = titular();
		} else
			solicitacao = SrSolicitacao.findById(id);

		formEditar(solicitacao.deduzirLocalERamal());
	}

	public static void exibirLocalERamal(SrSolicitacao solicitacao)
			throws Exception {
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		if (solicitacao.solicitante != null)
			locais = JPA
					.em()
					.createQuery(
							"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
									+ solicitacao.solicitante.getOrgaoUsuario()
											.getIdOrgaoUsu()).getResultList();
		render(solicitacao.deduzirLocalERamal(), locais);
	}

	public static void exibirLocal(SrSolicitacao solicitacao) throws Exception {
		List<CpComplexo> locais = new ArrayList<CpComplexo>();
		if (solicitacao.solicitante != null)
			locais = JPA
					.em()
					.createQuery(
							"from CpComplexo where orgaoUsuario.idOrgaoUsu = "
									+ lotaTitular().getOrgaoUsuario()
											.getIdOrgaoUsu()).getResultList();
		render(locais);
	}

	public static void exibirAtributos(SrSolicitacao solicitacao)
			throws Exception {
		render(solicitacao);
	}

	public static void exibirItemConfiguracao(SrSolicitacao solicitacao)
			throws Exception {
		if (!SrItemConfiguracao.listarPorPessoa(solicitacao.solicitante)
				.contains(solicitacao.itemConfiguracao))
			solicitacao.itemConfiguracao = null;

		solicitacao.servico = null;

		render(solicitacao);
	}

	public static void exibirServico(SrSolicitacao solicitacao)
			throws Exception {
		List<SrServico> servicos = SrServico
				.listarPorPessoaEItemEmOrdemAlfabetica(solicitacao.solicitante,
						solicitacao.itemConfiguracao);
		if (solicitacao.servico == null
				|| !servicos.contains(solicitacao.servico)) {
			if (servicos.size() > 0)
				solicitacao.servico = servicos.get(0);
			else
				solicitacao.servico = null;
		}

		render(solicitacao, servicos);
	}

	private static void formEditar(SrSolicitacao solicitacao) throws Exception {

		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();

		List<SrServico> servicos = new ArrayList<SrServico>();
		if (solicitacao.itemConfiguracao != null) {
			servicos = SrServico.listarPorPessoaEItemEmOrdemAlfabetica(
					solicitacao.solicitante, solicitacao.itemConfiguracao);
			if (solicitacao.servico == null
					|| !servicos.contains(solicitacao.servico)) {
				if (servicos.size() > 0)
					solicitacao.servico = servicos.get(0);
				else
					solicitacao.servico = null;
			}
		}

		render("@editar", solicitacao, locais, servicos);
	}

	private static void validarFormEditar(SrSolicitacao solicitacao)
			throws Exception {

		if (solicitacao.itemConfiguracao == null) {
			validation.addError("solicitacao.itemConfiguracao",
					"Item não informado");
		}

		if (solicitacao.servico == null) {
			validation.addError("solicitacao.servico", "Serviço não informado");
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
				.find("select sol.gravidade, count(*) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao and movimentacao.estado <> 2 "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId() + " "
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
				.find("select extract (month from sol.hisDtIni), extract (year from sol.hisDtIni), count(*) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId()
						+ " "
						+ "group by extract (month from sol.hisDtIni), extract (year from sol.hisDtIni)")
				.fetch();

		for (Object[] sols : listaEvolSols) {
			set.add(new SrSolicitacaoItem((Integer) sols[0], (Integer) sols[1],
					(Long) sols[2], 0, 0));
		}

		List<Object[]> listaFechados = SrSolicitacao
				.find("select extract (month from sol.hisDtIni), extract (year from sol.hisDtIni), count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.estado = 2 "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId()
						+ " "
						+ "group by extract (month from sol.hisDtIni), extract (year from sol.hisDtIni)")
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
				.find("select sol.itemConfiguracao.tituloItemConfiguracao, count(*) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId()
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
				.find("select sol.gravidade, sol.urgencia, count(*) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ lotaTitular().getId() + " "
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
		SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);

		render(solicitacao, movimentacao, completo);
	}

	// public static void exibirSolicitacaoLista(Long id) {
	public static void exibirLista(Long id) throws Exception {
		SrLista lista = SrLista.findById(id);
		lista.getMovimentacaoSet(false);
		// TreeSet<SrSolicitacao> solicitacao = lista.getSolicitacaoAssociada();
		TreeSet<SrSolicitacao> solicitacao = lista.getSolicSet();
		boolean editar = true;
		render(solicitacao, lista, editar);
	}

	/*
	 * Onde estou usando esses métodos??
	 * ------------------------------------------
	 */
	public static void exibirListaAssoc(Long id) {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		SrLista lista = new SrLista();
		boolean editar = solicitacao.podeEditar(lotaTitular(), cadastrante());
		List<SrLista> listas = solicitacao.getListaAssociada();
		render(solicitacao, editar, listas);
	}

	public static void exibirListaAssoc(Long id, Long idLista) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		SrLista lista = SrLista.findById(idLista);
		boolean editar = solicitacao.podeEditar(lotaTitular(), cadastrante());
		List<SrLista> listas = solicitacao.getListaAssociada();
		render(solicitacao, editar, listas);
	}

	public static void associarLista(Long id) {
		SrSolicitacao solicitacao = SrSolicitacao.findById(id);
		boolean editar = solicitacao.podeEditar(lotaTitular(), cadastrante());
		List<SrLista> listas = solicitacao.getListaDisponivel();
		render(solicitacao, editar, listas);
	}

	public static void associarListaGravar(Long idSolicitacao, Long idLista)
			throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.findById(idSolicitacao);
		SrLista lista = SrLista.findById(idLista);
		solicitacao.associarLista(solicitacao, lista);
		List<SrLista> listas = solicitacao.getListaDisponivel();
		render(solicitacao, listas);
	}

	public static void desassociarLista(Long idSolicitacao, Long idLista)
			throws Exception {

		SrSolicitacao solicitacao = SrSolicitacao.findById(idSolicitacao);
		SrLista lista = SrLista.findById(idLista);
		solicitacao.desassociarLista(solicitacao, lista);
		// Set<SrSolicitacao> sols = lista.getSolicitacaoAssociada();
		Set<SrSolicitacao> sols = lista.getSolicSet();
		// lista.recalcularPrioridade(idLista);
		boolean editar = solicitacao.podeEditar(lotaTitular(), cadastrante());
		boolean vazio = lista.isEmpty();
		render(sols, editar, lista, vazio);
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

	public static void fechar(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		if (movimentacao.solicitacao.temPosAtendenteDesignado()) {
			movimentacao.tipoMov = SrTipoMovimentacao
					.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_POS_ATENDIMENTO);
			movimentacao.lotaAtendente = movimentacao.solicitacao
					.getPosAtendenteDesignado();
		} else {
			movimentacao.tipoMov = SrTipoMovimentacao
					.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
		}
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void cancelar(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO);
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void finalizarPreAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_ATENDIMENTO);
		movimentacao.lotaAtendente = movimentacao.solicitacao
				.getAtendenteDesignado();
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void finalizarPosAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO);
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void deixarPendente(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		SrMovimentacao movimentacao = new SrMovimentacao(sol);

		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_INICIO_PENDENCIA);
		movimentacao.salvar(cadastrante(), lotaTitular());
		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void terminarPendencia(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_FIM_PENDENCIA);
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
	}

	public static void reabrir(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);

		SrMovimentacao movimentacao = new SrMovimentacao(sol);
		movimentacao.tipoMov = SrTipoMovimentacao
				.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_REABERTURA);
		movimentacao.salvar(cadastrante(), lotaTitular());

		exibir(movimentacao.solicitacao.idSolicitacao, completo());
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
		List<CpComplexo> orgaos = JPA.em().createQuery("from CpOrgaoUsuario")
				.getResultList();
		SrConfiguracao designacao = new SrConfiguracao();
		if (id != null)
			designacao = JPA.em().find(SrConfiguracao.class, id);
		render(designacao, orgaos);
	}

	public static void gravarDesignacao(SrConfiguracao designacao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		designacao.salvarComoDesignacao();
		listarDesignacao();
	}

	public static void desativarDesignacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.finalizar();
		listarDesignacao();
	}

	public static void listarAssociacao() throws Exception {
		assertAcesso("ADM:Administrar");
		List<List<SrConfiguracao>> listasAssociacoes = SrConfiguracao
				.listarAssociacoesTipoAtributo();
		render(listasAssociacoes);
	}

	public static void editarAssociacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = new SrConfiguracao();
		if (id != null)
			associacao = (SrConfiguracao) JPA.em()
					.find(SrConfiguracao.class, id).getConfiguracaoAtual();
		List<SrTipoAtributo> tiposAtributo = SrTipoAtributo.listar();
		render(associacao, tiposAtributo);
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
		SrItemConfiguracao item = new SrItemConfiguracao();
		if (id != null)
			item = SrItemConfiguracao.findById(id);
		render(item);
	}

	public static void gravarItem(SrItemConfiguracao item) throws Exception {
		assertAcesso("ADM:Administrar");
		item.salvar();
		listarItem();
	}

	public static void desativarItem(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrItemConfiguracao item = SrItemConfiguracao.findById(id);
		item.finalizar();
		listarItem();
	}

	public static void selecionarItem(String sigla, Long pessoa)
			throws Exception {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla,
				pessoa != null ? JPA.em().find(DpPessoa.class, pessoa) : null);
		render("@selecionar", sel);
	}

	public static void buscarItem(String sigla, String nome,
			SrItemConfiguracao filtro, Long pessoa) {

		List<SrItemConfiguracao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(pessoa != null ? JPA.em().find(
					DpPessoa.class, pessoa) : null);
		} catch (Exception e) {
			itens = new ArrayList<SrItemConfiguracao>();
		}

		render(itens, filtro, nome, pessoa);
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
		att.salvar();
		listarTipoAtributo();
	}

	public static void desativarTipoAtributo(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrTipoAtributo item = SrTipoAtributo.findById(id);
		item.finalizar();
		listarTipoAtributo();
	}

	public static void listarServico() throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrServico> servicos = SrServico.listar();
		render(servicos);
	}

	public static void editarServico(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrServico servico = new SrServico();
		if (id != null)
			servico = SrServico.findById(id);
		render(servico);
	}

	public static void gravarServico(SrServico servico) throws Exception {
		assertAcesso("ADM:Administrar");
		servico.salvar();
		listarServico();
	}

	public static void desativarServico(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrServico servico = SrServico.findById(id);
		servico.finalizar();
		listarServico();
	}

	public static void selecionarServico(String sigla, Long pessoa, Long item)
			throws Exception {
		SrServico sel = new SrServico().selecionar(
				sigla,
				pessoa != null ? JPA.em().find(DpPessoa.class, pessoa) : null,
				item != null ? (SrItemConfiguracao) SrItemConfiguracao
						.findById(item) : null);
		render("@selecionar", sel);
	}

	public static void buscarServico(String sigla, String nome,
			SrServico filtro, Long pessoa, Long item) {
		List<SrServico> itens = null;
		try {
			if (filtro == null)
				filtro = new SrServico();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(
					pessoa != null ? JPA.em().find(DpPessoa.class, pessoa)
							: null,
					item != null ? (SrItemConfiguracao) SrItemConfiguracao
							.findById(item) : null);
		} catch (Exception e) {
			itens = new ArrayList<SrServico>();
		}

		render(itens, filtro, nome, pessoa, item);
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
		lista.save();
		listarLista();
	}

	public static void desativarLista(Long id) throws Exception {
		SrLista lista = SrLista.findById(id);
		lista.finalizar();
		listarLista();
	}

	public static void relSolicitacoes() throws Exception {
		assertAcesso("REL:Relatorio");

		render();
	}

	public static void relTransferencias(SrSolicitacao solicitacao)
			throws Exception {
		assertAcesso("REL:Relatorio");

		render();
	}

	public static void relLocal() throws Exception {
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

	public static void grelSolicitacoes(String secaoUsuario, String lotacao,
			String situacao, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("situacao", situacao);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelSolicitacoes rel = new SrRelSolicitacoes(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Solicitações", pdf.length,
				"application/pdf", false);
	}

	public static void grelTransferencias(String secaoUsuario, String lotacao,
			String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelTransferencias rel = new SrRelTransferencias(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Transferências", pdf.length,
				"application/pdf", false);
	}

	public static void grelLocal(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelLocal rel = new SrRelLocal(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Solicitações por Localidade",
				pdf.length, "application/pdf", false);
	}

	public static void grelPrazo(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPrazo rel = new SrRelPrazo(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório de Prazos", pdf.length, "application/pdf",
				false);
	}

	public static void grelPrazoDetail(String secaoUsuario, String lotacao,
			String local, String dtIni, String dtFim) throws Exception {

		assertAcesso("REL:Relatorio");

		Map<String, String> parametros = new HashMap<String, String>();

		parametros.put("secaoUsuario", secaoUsuario);
		parametros.put("lotacao", lotacao);
		parametros.put("local", local);
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);

		SrRelPrazoDetail rel = new SrRelPrazoDetail(parametros);

		rel.gerar();

		byte[] pdf = rel.getRelatorioPDF();
		InputStream is = new ByteArrayInputStream(pdf);

		renderBinary(is, "Relatório Detalhado de Prazos", pdf.length,
				"application/pdf", false);
	}

	private static Map<String, Object> map = new HashMap<String, Object>();

	// setter
	public static void setValue(String key, Object value) {
		map.put(key, value);
	}

	// general getter would work well with String, also numeric types (only for
	// displaying purposes! - not for calculations or comparisons!)
	public static Object getValue(String key) {
		return map.get(key);
	}

	public static Boolean isTrue(String key) {
		return Boolean.valueOf(map.get(key).toString());
	}

	public static Double getDouble(String key) {
		return Double.valueOf(map.get(key).toString());
	}

}
