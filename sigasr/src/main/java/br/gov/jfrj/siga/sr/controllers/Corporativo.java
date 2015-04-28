package br.gov.jfrj.siga.sr.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import controllers.SigaApplication;
import br.gov.jfrj.siga.sr.model.DadosRH;
import br.gov.jfrj.siga.sr.model.DadosRH.Cargo;
import br.gov.jfrj.siga.sr.model.DadosRH.Funcao;
import br.gov.jfrj.siga.sr.model.DadosRH.Lotacao;
import br.gov.jfrj.siga.sr.model.DadosRH.Orgao;
import br.gov.jfrj.siga.sr.model.DadosRH.Papel;
import br.gov.jfrj.siga.sr.model.DadosRH.Pessoa;
import play.mvc.Controller;

public class Corporativo extends SigaApplication {
	public static void dadosrh() throws ParserConfigurationException {
		Map<Long, Cargo> mc = new TreeMap<Long, Cargo>();
		Map<Long, Lotacao> ml = new TreeMap<Long, Lotacao>();
		Map<Long, Funcao> mf = new TreeMap<Long, Funcao>();
		Map<Long, Pessoa> mp = new TreeMap<Long, Pessoa>();
		Map<Long, List<Papel>> mpp = new TreeMap<Long, List<Papel>>();
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		List<DadosRH> l = DadosRH.all().fetch();
		for (DadosRH d : l) {
			Pessoa p = d.getPessoa();
			if (p != null
					&& (!mp.containsKey(p.pessoa_id) || p.lotacao_tipo_papel.equals("Principal"))
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

			Papel pp = d.getPapel();
			if (pp != null && !mpp.containsKey(pp.papel_pessoa_id))
				mpp.put(pp.papel_pessoa_id, new ArrayList<Papel>());
			if (pp != null)
				mpp.get(pp.papel_pessoa_id).add(pp);
			
			Orgao org = d.getOrgao();

		}

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("base");
		rootElement.setAttribute("orgaoUsuario", "RJ");
		rootElement.setAttribute("versao", "2"); // forcei a versao para testar
		doc.appendChild(rootElement);

		Element cargos = doc.createElement("cargos");
		rootElement.appendChild(cargos);
		for (Cargo c : mc.values()) {
			Element e = doc.createElement("cargo");
			setAttr(e, "id", c.cargo_id.toString());
			setAttr(e, "nome", c.cargo_nome);
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
			setAttr(e, "rua", p.pessoa_rua);
			setAttr(e, "bairro", p.pessoa_bairro);
			setAttr(e, "cidade", p.pessoa_cidade);
			setAttr(e, "uf", p.pessoa_uf);
			setAttr(e, "cep", p.pessoa_cep);
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
			setAttr(e, "tipo", p.tipo_rh);
			setAttr(e, "tipoSanguineo", p.pessoa_tp_sanguineo);
			setAttr(e, "naturalidade", p.pessoa_naturalidade);
			setAttr(e, "nacionalidade", p.pessoa_nacionalidade);
			pessoas.appendChild(e);
			if (mpp.containsKey(p.pessoa_id)) {
				for (Papel papeis : mpp.get(p.pessoa_id))	{
					Element papel = doc.createElement("papel"); 
					setAttr(papel, "id", papeis.papel_id); 
					setAttr(papel, "tipo",papeis.papel_lotacao_tipo);
					setAttr(papel, "cargo", papeis.papel_cargo_id); 
					setAttr(papel,"funcaoConfianca", papeis.papel_funcao_id); 
					setAttr(papel, "lotacao", papeis.papel_lotacao_id); 
					e.appendChild(papel);	
				}
			}
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
