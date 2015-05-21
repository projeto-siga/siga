/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 *
 *     This file is part of SIGA.
 *
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

/*
 * Criado em 23/11/2005
 */

package br.gov.jfrj.webwork.action;

import java.io.DataInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class PrincipalAction extends SigaActionSupport {

	// Nem será necessário herdar de Selecao
	public class GenericoSelecao {

		private Long id;
		private String sigla;
		private String matricula;
		private String descricao;

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public Long getId(){
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getSigla() {
			return sigla;
		}

		public void setSigla(String sigla) {
			this.sigla = sigla;
		}

		public String getMatricula() {
			return matricula;
		}

		public void setMatricula(String matricula) {
			this.matricula = matricula;
		}
	}

	private static final String OK = "<span style=\"color: green;\">OK</span>";
	private static final String ERRO = "<span style=\"color: red;\">ERRO</span>";
	private static final String SIGA_TESTES_ACTION = "/siga/testes/testes.action";
	private List listEstados;
	private String sigla;
	private String matricula;
	private GenericoSelecao sel;
	private String idp;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		try{
			Map<String, Object> map = (Map<String, Object>) getRequest().getSession().getAttribute("SESSION_ATTRIBUTE_MAP");
			String idpSessionID = (String) ((List<Object>) map.get("IDPsessionID")).get(0);
			setIdp(idpSessionID);
		}catch(NullPointerException e){}

		return Action.SUCCESS;
	}

	public String aSelecionar() throws Exception {
		try {
			DpPessoa pes = getTitular();
			DpLotacao lot = getLotaTitular();
			String testes = "";
			String incluirMatricula = "";
			if (matricula != null) {
				pes = daoPes(param("matricula"));
				lot = pes.getLotacao();
				testes = "/testes";
				incluirMatricula = "&matricula=" + matricula;
			}else{
				if (getTitular() != null)
					incluirMatricula = "&matricula=" + getTitular().getSiglaCompleta();
			}

			// TODO n�o precisa pegar isso de um properties, isso existe no proprio request getServerName, getPort...

			//String urlBase = "http://"+ SigaBaseProperties.getString(SigaBaseProperties.getString("ambiente") + ".servidor.principal")+ getRequest().getServerPort();
			String urlBase = getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort();

			String URLSelecionar = "";
			String uRLExibir = "";

			List<String> orgaos = new ArrayList<String>();
			String copiaSigla = getSigla().toUpperCase();
			for (CpOrgaoUsuario o : dao().consultaCpOrgaoUsuario()) {
				orgaos.add(o.getSiglaOrgaoUsu());
				orgaos.add(o.getAcronimoOrgaoUsu());
			}
			for (String s : orgaos)
				if (copiaSigla.startsWith(s)) {
					copiaSigla = copiaSigla.substring(s.length());
					break;
				}
			if (copiaSigla.startsWith("-"))
				copiaSigla = copiaSigla.substring(1);

			//alterada a condi��o que verifica se � uma solicita��o do siga-sr
			//dessa forma a regex verifica se a sigla come�a com SR ou sr e termina com n�meros
			//necess�rio para n�o dar conflito caso exista uma lota��o que inicie com SR
			if (copiaSigla.startsWith("SR")) {
//			if (copiaSigla.matches("^[SR|sr].*[0-9]+$")) {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;SR"))
					URLSelecionar = urlBase + "/sigasr" + testes
							+ "/solicitacao/selecionar?sigla=" + getSigla()
							+ incluirMatricula;
			}
 			//alterado formato da sigla de requisições, missões e serviços
			//else if (copiaSigla.startsWith("MTP")
			//		|| copiaSigla.startsWith("RTP")
			//		|| copiaSigla.startsWith("STP")) {
			else if (copiaSigla.startsWith("TP") &&
					(copiaSigla.endsWith("M") ||
					 copiaSigla.endsWith("S") ||
					 copiaSigla.endsWith("R"))) {
				if (Cp.getInstance()
						.getConf()
						.podeUtilizarServicoPorConfiguracao(pes, lot, "SIGA;TP")) {
					URLSelecionar = urlBase + "/sigatp"
							+ "/selecionar.action?sigla=" + getSigla()
							+ incluirMatricula;
				}
			}
			else
				URLSelecionar = urlBase + "/sigaex"
						+ (testes.length() > 0 ? testes : "/expediente")
						+ "/selecionar.action?sigla=" + getSigla()
						+ incluirMatricula;

			SigaHTTP http = new SigaHTTP();
			String[] response = http.get(URLSelecionar, getRequest(), null).split(";");

			if (response.length == 1 && Integer.valueOf(response[0]) == 0) {
				//verificar se ap�s a retirada dos prefixos referente
				//ao org�o (sigla_orgao_usu = RJ ou acronimo_orgao_usu = JFRJ) e n�o achar resultado com as op��es anteriores
				//a string copiaSigla somente possui n�meros
				if (copiaSigla.matches("(^[0-9]+$)")) {
					URLSelecionar = urlBase + "/siga"
							+ (testes.length() > 0 ? testes : "/pessoa")
							+ "/selecionar.action?sigla=" + getSigla()
							+ incluirMatricula;
				}
				//encontrar lota��es
				else {
					URLSelecionar = urlBase + "/siga"
						+ (testes.length() > 0 ? testes : "/lotacao")
						+ "/selecionar.action?sigla=" + getSigla()
						+ incluirMatricula;
				}

				response = http.get(URLSelecionar, getRequest(), null).split(";");

				if (copiaSigla.matches("(^[0-9]+$)"))
					uRLExibir = "/siga/pessoa/exibir.action?sigla="
							+ response[2];
				else
					uRLExibir = "/siga/lotacao/exibir.action?sigla="
							+ response[2];
			}
			else {
				if (copiaSigla.startsWith("SR"))
//					if (copiaSigla.matches("^[SR|sr].*[0-9]+$"))
						uRLExibir = "/sigasr/solicitacao/exibir/" + response[1];
				//alterado formato da sigla de requisições, missões e serviços
				//else if (copiaSigla.startsWith("MTP")
				//		|| copiaSigla.startsWith("STP")
				//		|| copiaSigla.startsWith("RTP"))
				else if (copiaSigla.startsWith("TP") &&
						(copiaSigla.endsWith("M") ||
						 copiaSigla.endsWith("S") ||
						 copiaSigla.endsWith("R")))
					uRLExibir = "/sigatp/exibir.action?sigla=" + response[2];
				else
					uRLExibir = "/sigaex/expediente/doc/exibir.action?sigla="
							+ response[2];
			}

			sel.setId(Long.valueOf(response[1]));
			sel.setSigla(response[2]);
			sel.setDescricao(uRLExibir);

			return "ajax_retorno";

		} catch (Exception e) {
			return "ajax_vazio";
		}
	}

	public String test() throws Exception {
		DpPessoa pes = daoPes(param("matricula"));

		super.getRequest().setAttribute("siga_test_url", "#");
		super.getRequest().setAttribute("siga_test", ERRO);
		if (pes != null)
			super.getRequest().setAttribute("siga_test", OK);

		String url = super.getRequest().getRequestURL().toString()
				+ "?matricula=" + super.getRequest().getParameter("matricula");

		String siga_ex_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigaex/testes/gadgetTest.action");
		super.getRequest().setAttribute("siga_ex_test_url", siga_ex_test_url);
		super.getRequest().setAttribute("siga_ex_test", ERRO);
		if (httpTest(siga_ex_test_url, "Atendente"))
			super.getRequest().setAttribute("siga_ex_test", OK);

		String siga_wf_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigawf/testes/gadgetTest.action");
		super.getRequest().setAttribute("siga_wf_test_url", siga_wf_test_url);
		super.getRequest().setAttribute("siga_wf_test", ERRO);
		if (httpTest(siga_ex_test_url, "Atendente"))
			super.getRequest().setAttribute("siga_wf_test", OK);

		String siga_cd_test_url = url.replace(SIGA_TESTES_ACTION,
				"/sigacd/testes/CdTestesServlet");
		super.getRequest().setAttribute("siga_cd_test_url", siga_cd_test_url);
		super.getRequest().setAttribute("siga_cd_test", ERRO);
		if (httpTest(siga_ex_test_url, "OK!"))
			super.getRequest().setAttribute("siga_cd_test", OK);

		return Action.SUCCESS;
	}

	private boolean httpTest(String url, String mustHave) {
		try {
			String thisLine;
			URL u = new URL(url);
			DataInputStream theHTML = new DataInputStream(u.openStream());
			boolean fHas = false;
			while ((thisLine = theHTML.readLine()) != null) {
				if (mustHave != null && thisLine.contains(mustHave))
					fHas = true;
			}
			if (mustHave != null && fHas)
				return fHas;
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public GenericoSelecao getSel() {
		return sel;
	}

	public void setSel(GenericoSelecao sel) {
		this.sel = sel;
	}

	public PrincipalAction() {
		sel = new GenericoSelecao();
	}

	public String getIdp() {
		return idp;
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

}
