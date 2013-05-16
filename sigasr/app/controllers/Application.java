package controllers;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import models.SrAndamento;
import models.SrArquivo;
import models.SrAtributo;
import models.SrConfiguracao;
import models.SrConfiguracaoBL;
import models.SrEstado;
import models.SrFormaAcompanhamento;
import models.SrGravidade;
import models.SrItemConfiguracao;
import models.SrServico;
import models.SrSolicitacao;
import models.SrTendencia;
import models.SrTipoAtributo;
import models.SrUrgencia;
import play.db.jpa.JPA;
import play.mvc.Before;
import play.mvc.Catch;
import util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpMarcador;
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
		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();
		render(solicitacao.deduzirLocalERamal(), locais);
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
		List<SrServico> servicos = SrServico.listarPorPessoaEItemEmOrdemAlfabetica(
				solicitacao.solicitante, solicitacao.itemConfiguracao);
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

		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		List<CpComplexo> locais = JPA.em().createQuery("from CpComplexo")
				.getResultList();
		boolean abrirFechando = solicitacao.podeAbrirJaFechando(lotaTitular(),
				cadastrante());
		boolean priorizar = solicitacao.podePriorizar(lotaTitular(),
				cadastrante());

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

		render("@editar", solicitacao, formasAcompanhamento, gravidades,
				urgencias, tendencias, priorizar, abrirFechando, locais,
				servicos);
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
		exibir(id, false);
	}

	public static void listar(SrSolicitacaoFiltro filtro) throws Exception {

		List<SrSolicitacao> listaSolicitacao;

		if (filtro.pesquisar)
			listaSolicitacao = filtro.buscar();
		else
			listaSolicitacao = new ArrayList<SrSolicitacao>();

		// Montando o filtro...
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA.em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();

		render(listaSolicitacao, urgencias, tendencias, gravidades, tipos,
				marcadores, filtro);
	}

	public static void exibir(Long id, boolean considerarCancelados) throws Exception {
		// antes: 8 queries
		SrSolicitacao solicitacao = SrSolicitacao.findById(id); // 3 queries
		SrAndamento andamento = new SrAndamento(solicitacao); // 1query
		andamento.deduzirProxAtendente(); // 318 queries
		// = true;
		
		boolean criarFilha = solicitacao.podeCriarFilha(lotaTitular(),
				cadastrante());
		boolean desfazerAndamento = solicitacao.podeDesfazerAndamento(
				lotaTitular(), cadastrante());
		boolean editar = solicitacao.podeEditar(lotaTitular(), cadastrante());
		boolean movimentarPlenamente = solicitacao.estaCom(lotaTitular(),
				cadastrante());
		
		List<SrEstado> estados = solicitacao.getEstadosSelecionaveis();

		render(solicitacao, editar, desfazerAndamento, movimentarPlenamente,
				andamento, criarFilha, estados, considerarCancelados);
	}

	public static void selecionar(String sigla) throws Exception {
		SrSolicitacao sel = new SrSolicitacao();
		sel.cadastrante = cadastrante();
		sel = (SrSolicitacao) sel.selecionar(sigla);
		render("@selecionar", sel);
	}

	public static void exibirAtendente(SrAndamento andamento) throws Exception {
		render(andamento.deduzirProxAtendente());
	}

	public static void baixar(Long idArquivo) {
		SrArquivo arq = SrArquivo.findById(idArquivo);
		if (arq != null)
			renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
	}

	public static void andamento(SrAndamento andamento) throws Exception {
		andamento.solicitacao.darAndamento(andamento, cadastrante(),
				lotaTitular());
		Long id = andamento.solicitacao.idSolicitacao;
		exibir(id, false);
	}

	public static void desfazerUltimoAndamento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.findById(id);
		sol.desfazerUltimoAndamento(cadastrante(), lotaTitular());
		exibir(id, false);
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

}
