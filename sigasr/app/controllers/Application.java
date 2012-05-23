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
		render(itensConfiguracao, formasAcompanhamento, gravidades, urgencias,
				tendencias);
	}

	public static void gravar(long idItemConfiguracao,
			Long idFormaAcompanhamento, String telPrincipal,
			String descrSolicitacao, String local, String motivoFechamento) {
		Long idPessoa = Long.valueOf(params.get("pessoaSolicitanteSel.id"));
		// DpPessoa solicitante = DpPessoa.findById(idPessoa);
		DpPessoa solicitante = JPA.em().find(DpPessoa.class, idPessoa);
		// query.getSingleResult();
		DpLotacao lotaSolicitante = solicitante.getLotacao();

		SrFormaAcompanhamento acompanhamento = SrFormaAcompanhamento.ANDAMENTO;
		/*
		 * SrFormaAcompanhamento acompanhamento = SrFormaAcompanhamento
		 * .findById(idFormaAcompanhamento);
		 */
		SrItemConfiguracao itemConfig = SrItemConfiguracao
				.findById(idItemConfiguracao);

		SrSolicitacao solicitacao = new SrSolicitacao(solicitante,
				lotaSolicitante, solicitante, lotaSolicitante, null,
				acompanhamento, itemConfig, null,
				SrGravidade.EXTREMAMENTE_GRAVE, SrUrgencia.AGIR_IMEDIATO,
				SrTendencia.NAO_PIORA, descrSolicitacao, telPrincipal, local,
				motivoFechamento).save();

		listarTudo();

	}

	public static void listarTudo() {
		List<SrSolicitacao> listaSolicitacao = SrSolicitacao.all().fetch();
		render(listaSolicitacao);
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

		categoria = filtro.getPorNivelHierarquia(1);
		List<SrServico> opcoesNivel1 = filtro.getOpcoesPorNivel(1);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria, opcoesNivel1);
	}

	public static void buscarSrItemConfiguracao(String sigla, String tipo,
			String nome, SrItemConfiguracao filtro,
			SrItemConfiguracao categoria1, SrItemConfiguracao categoria2,
			SrItemConfiguracao categoria3) {
		List<SrSelecionavel> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			else if (categoria3 != null
					&& categoria3.siglaItemConfiguracao != null
					&& !categoria3.siglaItemConfiguracao.trim().equals(""))
				filtro.siglaItemConfiguracao = categoria3.siglaItemConfiguracao;
			else if (categoria2 != null
					&& categoria2.siglaItemConfiguracao != null
					&& !categoria2.siglaItemConfiguracao.trim().equals(""))
				filtro.siglaItemConfiguracao = categoria2.siglaItemConfiguracao;
			else if (categoria1 != null
					&& categoria1.siglaItemConfiguracao != null
					&& !categoria1.siglaItemConfiguracao.trim().equals(""))
				filtro.siglaItemConfiguracao = categoria1.siglaItemConfiguracao;

			itens = filtro.buscar();
		} catch (Exception e) {
			itens = new ArrayList<SrSelecionavel>();
		}

		categoria1 = filtro.getPorNivelHierarquia(1);
		categoria2 = filtro.getPorNivelHierarquia(2);
		categoria3 = filtro.getPorNivelHierarquia(3);
		List<SrItemConfiguracao> opcoesNivel1 = filtro.getOpcoesPorNivel(1);
		List<SrItemConfiguracao> opcoesNivel2 = filtro.getOpcoesPorNivel(2);
		List<SrItemConfiguracao> opcoesNivel3 = filtro.getOpcoesPorNivel(3);

		render("Application/buscar" + tipo + ".html", itens, filtro, tipo,
				nome, categoria1, opcoesNivel1, categoria2, opcoesNivel2,
				categoria3, opcoesNivel3);
	}

}