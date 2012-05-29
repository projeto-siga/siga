package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.persistence.Query;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import models.*;

public class Application extends Controller {

	// @Before(only = { "buscarSrServico" })
	// static void parseParams() {
	// String[] items = params.getAll("f");
	// params.remove("items");
	// for (int i = 0; i < items.length; i++) {
	// Item item = Item.findById(Long.parseLong(items[i]));
	// params.put("items[" + i + "].id", items[i]);
	// params.put("items[" + i + "].propertyA", item.propertyA);
	// params.put("items[" + i + "].propertyB", item.propertyB);
	// }
	// }

	@Before
	static void addDefaults() {
		renderArgs.put("_base", "http://10.34.5.90:8080");
		// renderArgs.put("blogBaseline",
		// Play.configuration.getProperty("blog.baseline"));
	}

	public static void index() {
		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.all()
				.fetch();
		SrFormaAcompanhamento[] formasAcompanhamento = SrFormaAcompanhamento
				.values();
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		SrSolicitacao solicitacao = new SrSolicitacao();
		render(itensConfiguracao, formasAcompanhamento, gravidades, urgencias,
				tendencias, solicitacao);
	}

	public static void gravar(SrSolicitacao solicitacao) {
		Long idPessoa = Long.valueOf(params.get("pessoaSolicitanteSel.id"));
		String idItem = params.get("SrItemConfiguracaoItemSel.id");
		String idServico = params.get("SrServicoServicoSel.id");

		DpPessoa solicitante = JPA.em().find(DpPessoa.class, idPessoa);
		DpLotacao lotaSolicitante = solicitante.getLotacao();
		SrItemConfiguracao item = SrItemConfiguracao.findById(Integer
				.parseInt(idItem));
		SrServico servico = SrServico.findById(Integer.parseInt(idServico));

		solicitacao.solicitante = solicitante;
		solicitacao.lotaSolicitante = lotaSolicitante;
		solicitacao.itemConfiguracao = item;
		solicitacao.servico = servico;
		solicitacao.dtReg = new Date();
		solicitacao.save();

		listarTudo(null);

	}

	public static void listarTudo(SrSolicitacaoFiltro filtro) {

		// Usando o filtro...
		String idAtendente = params.get("pessoaAtendenteSel.id");
		String idSolicitante = params.get("pessoaSolicitanteSel.id");
		String idCadastrante = params.get("pessoaCadastranteSel.id");
		String idLotaAtendente = params.get("lotacaoAtendenteSel.id");
		String idLotaSolicitante = params.get("lotacaoSolicitanteSel.id");
		String idLotaCadastrante = params.get("lotacaoCadastranteSel.id");
		String idItem = params.get("SrItemConfiguracaoItemSel.id");
		String idServico = params.get("SrServicoServicoSel.id");

		if (idCadastrante != null && !idCadastrante.trim().equals(""))
			filtro.cadastrante = JPA.em().find(DpPessoa.class, Long.valueOf(idCadastrante));
		if (idSolicitante != null && !idSolicitante.trim().equals(""))
			filtro.solicitante = JPA.em().find(DpPessoa.class, Long.valueOf(idSolicitante));
		if (idAtendente != null && !idAtendente.trim().equals(""))
			filtro.atendente = JPA.em().find(DpPessoa.class, Long.valueOf(idAtendente));
		if (idLotaCadastrante != null && !idLotaCadastrante.trim().equals(""))
			filtro.lotaCadastrante = JPA.em().find(DpLotacao.class,
					Long.valueOf(idLotaCadastrante));
		if (idLotaSolicitante != null && !idLotaSolicitante.trim().equals(""))
			filtro.lotaSolicitante = JPA.em().find(DpLotacao.class,
					Long.valueOf(idLotaSolicitante));
		if (idLotaAtendente != null && !idLotaAtendente.trim().equals(""))
			filtro.lotaAtendente = JPA.em().find(DpLotacao.class,
					Long.valueOf(idLotaAtendente));
		if (idItem != null && !idItem.trim().equals(""))
			filtro.itemConfiguracao = SrItemConfiguracao.findById(Integer
					.parseInt(idItem));
		if (idServico != null  && !idServico.trim().equals(""))
			filtro.servico = SrServico.findById(Integer.parseInt(idServico));

		List<SrSolicitacao> listaSolicitacao = filtro.buscarPorFiltro();

		// Montando o filtro...
		SrUrgencia[] urgencias = SrUrgencia.values();
		SrTendencia[] tendencias = SrTendencia.values();
		SrGravidade[] gravidades = SrGravidade.values();
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = JPA
				.em()
				.createQuery(
						"select distinct cpMarcador.descrMarcador from SrMarca")
				.getResultList();

		if (filtro == null)
			filtro = new SrSolicitacaoFiltro();

		render(listaSolicitacao, urgencias, tendencias, gravidades, tipos,
				marcadores, filtro);
	}

	public static void proxy(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		for (Http.Header h : request.headers.values())
			conn.setRequestProperty(h.name, h.value());

		// BufferedReader in = new BufferedReader(new InputStreamReader(u
		// .openStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();

		renderHtml(sb.toString());

	}

	public static void selecionar(String sigla, String tipo) {
		SrSelecionavel sel = null;
		try {
			sel = (SrSelecionavel) Class.forName("models." + tipo)
					.newInstance();
			sel = sel.selecionar(sigla);
		} catch (IllegalAccessException e) {

		} catch (ClassNotFoundException cnfe) {

		} catch (InstantiationException ie) {
			int a = 0;
		}
		render(sel);
	}

	public static void buscar(String sigla, String tipo, String nome,
			SrServico filtro) {
		render();
	}

	public static void buscarSrServico(String sigla, String tipo, String nome,
			SrServico filtro, SrServico categoria) {
		List<SrSelecionavel> itens;

		try {
			if (filtro == null)
				filtro = new SrServico();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			else if (categoria != null && categoria.siglaServico != null
					&& !categoria.siglaServico.trim().equals(""))
				filtro.siglaServico = categoria.siglaServico;

			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrSelecionavel>();
		}

		categoria = (SrServico) filtro.obterPorHierarquia(1);
		if (categoria == null)
			categoria = new SrServico();
		List<SrCodigoHierarquicoSuporte> opcoesNivel1 = filtro
				.obterOpcoesPorHierarquia(1);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria, opcoesNivel1);
	}

	public static void buscarSrItemConfiguracao(String sigla, String tipo,
			String nome, SrItemConfiguracao filtro,
			SrItemConfiguracao categoria1, SrItemConfiguracao categoria2,
			SrItemConfiguracao categoria3, String alterou) {
		List<SrSelecionavel> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			else {
				String nivelAlterado = params.get("alterou");
				if (nivelAlterado != null)
					if (nivelAlterado.equals("3"))
						filtro.siglaItemConfiguracao = categoria3.siglaItemConfiguracao;
					else if (nivelAlterado.equals("2"))
						filtro.siglaItemConfiguracao = categoria2.siglaItemConfiguracao;
					else if (nivelAlterado.equals("1"))
						filtro.siglaItemConfiguracao = categoria1.siglaItemConfiguracao;
			}
			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrSelecionavel>();
		}

		categoria1 = (SrItemConfiguracao) filtro.obterPorHierarquia(1);
		if (categoria1 == null)
			categoria1 = new SrItemConfiguracao();
		categoria2 = (SrItemConfiguracao) filtro.obterPorHierarquia(2);
		if (categoria2 == null)
			categoria2 = new SrItemConfiguracao();
		categoria3 = (SrItemConfiguracao) filtro.obterPorHierarquia(3);
		if (categoria3 == null)
			categoria3 = new SrItemConfiguracao();
		List<SrCodigoHierarquicoSuporte> opcoesNivel1 = filtro
				.obterOpcoesPorHierarquia(1);
		List<SrCodigoHierarquicoSuporte> opcoesNivel2 = filtro
				.obterOpcoesPorHierarquia(2);
		List<SrCodigoHierarquicoSuporte> opcoesNivel3 = filtro
				.obterOpcoesPorHierarquia(3);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria1, opcoesNivel1, categoria2, opcoesNivel2,
				categoria3, opcoesNivel3);
	}

}