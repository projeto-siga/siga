package controllers;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.DadosRH;
import models.DadosRH.Cargo;
import models.DadosRH.Funcao;
import models.DadosRH.Lotacao;
import models.DadosRH.Pessoa;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import play.mvc.Controller;

public class Corporativo extends Controller {
	public static void dadosrh() throws ParserConfigurationException {
		Map<Long, Cargo> mc = new TreeMap<Long, Cargo>();
		Map<Long, Lotacao> ml = new TreeMap<Long, Lotacao>();
		Map<Long, Funcao> mf = new TreeMap<Long, Funcao>();
		Map<Long, Pessoa> mp = new TreeMap<Long, Pessoa>();

		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		List<DadosRH> l = DadosRH.all().fetch();
		for (DadosRH d : l) {
			Pessoa p = d.getPessoa();
			if (p != null
					&& !mp.containsKey(p.pessoa_id)
					&& (p.pessoa_situacao.equals(1)
							|| p.pessoa_situacao.equals(2) || p.pessoa_situacao
							.equals(31)))
				mp.put(p.pessoa_id, p);

			Lotacao x = d.getLotacao();
			if (x != null && !ml.containsKey(x.lotacao_id))
				ml.put(x.lotacao_id, x);

			Cargo c = d.getCargo();
			if (c != null && !mc.containsKey(c.cargo_id))
				mc.put(c.cargo_id, c);

			Funcao f = d.getFuncao();
			if (f != null && !mf.containsKey(f.funcao_id))
				mf.put(f.funcao_id, f);
		}

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("base");
		rootElement.setAttribute("orgaoUsuario", "RJ");
		doc.appendChild(rootElement);

		Element cargos = doc.createElement("cargos");
		rootElement.appendChild(cargos);
		for (Cargo c : mc.values()) {
			Element e = doc.createElement("cargo");
			setAttr(e, "id", c.cargo_id.toString());
			setAttr(e, "nome", c.cargo_nome);
			//setAttr(e, "sigla", c.cargo_sigla);
			cargos.appendChild(e);
		}

		Element funcoes = doc.createElement("funcoes");
		rootElement.appendChild(funcoes);
		for (Funcao f : mf.values()) {
			Element e = doc.createElement("funcao");
			setAttr(e, "id", f.funcao_id.toString());
			setAttr(e, "nome", f.funcao_nome);
			setAttr(e, "sigla", f.funcao_sigla);
			funcoes.appendChild(e);
		}

		Element lotacoes = doc.createElement("lotacoes");
		rootElement.appendChild(lotacoes);
		for (Lotacao x : ml.values()) {
			Element e = doc.createElement("lotacao");
			setAttr(e, "id", x.lotacao_id.toString());
			setAttr(e, "nome", x.lotacao_nome);
			setAttr(e, "sigla", x.lotacao_sigla);
			setAttr(e, "idPai", x.lotacao_id_pai);
			setAttr(e, "tipo", x.lotacao_tipo);
			setAttr(e, "papel", x.lotacao_tipo_papel);
			lotacoes.appendChild(e);
		}

		Element pessoas = doc.createElement("pessoas");
		rootElement.appendChild(pessoas);
		for (Pessoa p : mp.values()) {
			Element e = doc.createElement("pessoa");
			setAttr(e, "id", p.pessoa_id);
			setAttr(e, "cpf", p.pessoa_cpf);
			setAttr(e, "nome", p.pessoa_nome);
			setAttr(e, "sexo", p.pessoa_sexo);
			setAttr(e, "dtNascimento", p.pessoa_data_nascimento);
			setAttr(e, "atoNomeacao", p.pessoa_ato_nomeacao);
			setAttr(e, "dtAtoPublicacao", p.pessoa_dt_publ_nomeacao);
			setAttr(e, "dtInicioExercicio", p.pessoa_data_inicio_exercicio);
			setAttr(e, "dtNomeacao", p.pessoa_data_nomeacao);
			setAttr(e, "dtPosse", p.pessoa_data_posse);
			setAttr(e, "email", p.pessoa_email);
			setAttr(e, "estCivil", p.pessoa_estado_civil);
			setAttr(e, "grauInstrucao", p.pessoa_grau_de_instrucao);
			setAttr(e, "matricula", p.pessoa_matricula);
			setAttr(e, "padraoReferencia", p.pessoa_padrao_referencia);
			setAttr(e, "rg", p.pessoa_rg);
			setAttr(e, "rgDtExp", p.pessoa_data_expedicao_rg);
			setAttr(e, "rgOrgao", p.pessoa_rg_orgao);
			setAttr(e, "rgUf", p.pessoa_rg_uf);
			setAttr(e, "sigla", p.pessoa_sigla);
			setAttr(e, "situacao", p.pessoa_situacao);
			setAttr(e, "lotacao", p.lotacao_id);
			setAttr(e, "cargo", p.cargo_id);
			setAttr(e, "funcaoConfianca", p.funcao_id);
			pessoas.appendChild(e);
		}

		renderXml(doc);
	}

	private static void setAttr(Element e, String name, Object value) {
		if (value == null)
			return;
		if (value instanceof Date) {
			String s = value.toString();
			s = s.substring(8, 10) + s.substring(5, 7) + s.substring(0, 4);
			e.setAttribute(name, s);
			return;
		}
		e.setAttribute(name, value.toString());
	}
}
