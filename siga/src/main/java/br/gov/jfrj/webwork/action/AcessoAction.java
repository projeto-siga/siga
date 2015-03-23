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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.acesso.ConfiguracaoAcesso;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import com.opensymphony.xwork.Action;

public class AcessoAction extends GiActionSupport {

	private Long idOrgaoUsu;
	private DpPessoaSelecao pessoaSel;
	private DpLotacaoSelecao lotacaoSel;
	private CpPerfilSelecao perfilSel;
	private String nomeOrgaoUsu;

	private Long idServico;
	private Long idSituacao;
	private int idAbrangencia;
	private List<ConfiguracaoAcesso> itens;
	private CpSituacaoConfiguracao situacao;
	private String itensHTML;
	private String itemHTML;

	public String getItemHTML() {
		return itemHTML;
	}

	public void setItemHTML(String itemHTML) {
		this.itemHTML = itemHTML;
	}

	public String getItensHTML() {
		return itensHTML;
	}

	public void setItensHTML(String itensHTML) {
		this.itensHTML = itensHTML;
	}

	public AcessoAction() {
		pessoaSel = new DpPessoaSelecao();
		lotacaoSel = new DpLotacaoSelecao();
		perfilSel = new CpPerfilSelecao();
	}

	public String aGravar() throws Exception {
		assertAcesso("PERMISSAO:Gerenciar permissões");
		CpPerfil perfil = null;
		DpPessoa pessoa = null;
		DpLotacao lotacao = null;
		CpOrgaoUsuario orgao = null;

		if (paramLong("pessoaSel.id") != null) {
			pessoa = daoPes(paramLong("pessoaSel.id"));
		} else if (paramLong("lotacaoSel.id") != null) {
			lotacao = daoLot(paramLong("lotacaoSel.id"));
		} else if (paramLong("perfilSel.id") != null) {
			perfil = dao().consultar(paramLong("perfilSel.id"), CpPerfil.class,
					false);
		} else if (paramLong("idOrgaoUsu") != null) {
			orgao = dao().consultar(paramLong("idOrgaoUsu"),
					CpOrgaoUsuario.class, false);
		} else {
			throw new AplicacaoException(
					"Não foi informada pessoa, lotação ou órgão usuário.");
		}

		CpServico servico = dao().consultar(idServico, CpServico.class, false);
		CpSituacaoConfiguracao situacao = dao().consultar(idSituacao,
				CpSituacaoConfiguracao.class, false);
		CpTipoConfiguracao tpConf = dao().consultar(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO
				, CpTipoConfiguracao.class, false);
		Cp.getInstance().getBL().configurarAcesso(perfil, orgao, lotacao,
				pessoa, servico, situacao,tpConf, getIdentidadeCadastrante());
		ConfiguracaoAcesso ac = ConfiguracaoAcesso.gerar(perfil, pessoa,
				lotacao, orgao, servico,null);
		StringBuilder sb = new StringBuilder();
		acessoHTML(sb, ac);
		setItemHTML(sb.toString());
		return Action.SUCCESS;
	}

	public String aListar() throws Exception {
		assertAcesso("PERMISSAO:Gerenciar permissões");
		if (getIdAbrangencia() == 0) {
			setIdAbrangencia(1);
			setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getId());
		}

		CpPerfil perfil = getIdAbrangencia() == 4 ? perfilSel.buscarObjeto()
				: null;
		DpPessoa pessoa = getIdAbrangencia() == 3 ? pessoaSel.buscarObjeto()
				: null;
		DpLotacao lotacao = getIdAbrangencia() == 2 ? lotacaoSel.buscarObjeto()
				: null;
		CpOrgaoUsuario orgao = getIdAbrangencia() == 1 ? (getIdOrgaoUsu() != null ? dao()
				.consultar(getIdOrgaoUsu(), CpOrgaoUsuario.class, false)
				: null)
				: null;
		if (orgao != null)
			setNomeOrgaoUsu(orgao.getDescricao());

		if (perfil != null || pessoa != null || lotacao != null
				|| orgao != null) {
			List<CpServico> l = dao().listarServicos();

			HashMap<CpServico, ConfiguracaoAcesso> achm = new HashMap<CpServico, ConfiguracaoAcesso>();
			for (CpServico srv : l) {
				ConfiguracaoAcesso ac = ConfiguracaoAcesso.gerar(perfil, pessoa,
						lotacao, orgao, srv, null);

				// ConfiguracaoAcesso ac = new ConfiguracaoAcesso();
				// ac.setServico(srv);
				// ac.setPessoa(pessoa);
				// ac.setLotacao(lotacao);
				// ac.setOrgao(orgao);
				// CpConfiguracao cfgFiltro = new CpConfiguracao();
				// cfgFiltro.setDpPessoa(pessoa);
				// cfgFiltro.setLotacao(lotacao);
				// cfgFiltro.setOrgaoUsuario(orgao);
				// cfgFiltro.setCpServico(srv);
				// CpTipoConfiguracao tipo = dao().consultar(
				// CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO,
				// CpTipoConfiguracao.class, false);
				// cfgFiltro.setCpTipoConfiguracao(tipo);
				// CpConfiguracao cfg = Cp.getInstance().getConf()
				// .buscaConfiguracao(cfgFiltro, new int[0]);
				// ac.setDefault(cfg == null);
				// if (ac.isDefault()) {
				// ac.setSituacao(tipo.getSituacaoDefault());
				// } else {
				// ac.setSituacao(cfg.getCpSituacaoConfiguracao());
				// }
				System.out.print(ac.getSituacao().getDscSitConfiguracao());
				achm.put(ac.getServico(), ac);
			}

			SortedSet<ConfiguracaoAcesso> acs = new TreeSet<ConfiguracaoAcesso>();
			for (ConfiguracaoAcesso ac : achm.values()) {
				if (ac.getServico().getCpServicoPai() == null) {
					acs.add(ac);
				} else {
					achm.get(ac.getServico().getCpServicoPai()).getSubitens()
							.add(ac);
				}
			}

			setItens(new ArrayList<ConfiguracaoAcesso>(acs));
			setItensHTML(arvoreHTML());
		}
		return Action.SUCCESS;
	}

	private String arvoreHTML() {
		StringBuilder sb = new StringBuilder();
		acrescentarHTML(getItens(), sb);
		return sb.toString();
	}

	private void acrescentarHTML(Collection<ConfiguracaoAcesso> l,
			StringBuilder sb) {
		sb.append("<ul style=\"list-style-type:disc;\">");
		for (ConfiguracaoAcesso ac : l) {
			sb.append("<span id=\"SPAN-" + ac.getServico().getId() + "\">");
			acessoHTML(sb, ac);
			sb.append("</span>");
			if (ac.getSubitens() != null && ac.getSubitens().size() > 0)
				acrescentarHTML(ac.getSubitens(), sb);
		}
		sb.append("</ul>");
	}

	private void acessoHTML(StringBuilder sb, ConfiguracaoAcesso ac) {
		boolean fPode = ac.getSituacao().getDscSitConfiguracao().equals("Pode");
		boolean fNaoPode = ac.getSituacao().getDscSitConfiguracao().equals(
				"Não Pode");
		sb.append("<li style=\"color:"
				+ (fPode ? "green" : (fNaoPode ? "red" : "black")) + ";\">");
		if (ac.getServico().getCpServicoPai() != null
				&& ac.getServico().getSigla().startsWith(
						ac.getServico().getCpServicoPai().getSigla() + "-")) {
			sb.append(ac.getServico().getSigla().substring(
					ac.getServico().getCpServicoPai().getSigla().length() + 1));
		} else {
			sb.append(ac.getServico().getSigla());
		}
		sb.append(": <span style=\"\">");
		sb.append(ac.getServico().getDescricao());
		if (!(fPode || fNaoPode)) {
			sb.append(" (");
			sb.append(ac.getSituacao().getDscSitConfiguracao());
			sb.append(")");
		}
		sb.append("</span>");
		if (fNaoPode)
			sb.append(" - <a style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",1)\">Permitir</a>");
		if (fPode)
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",2)\">Proibir</a>");
		if (!ac.isDefault())
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",9)\">Default</a>");
		if (ac.getOrgao() != null)
			sb
					.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=1&idOrgaoUsu="
							+ ac.getOrgao().getId()
							+ "\">"
							+ ac.getOrgao().getSigla() + "</a>");
		if (ac.getPerfil() != null)
			sb
					.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=4&perfilSel.id="
							+ ac.getPerfil().getId()
							+ "&perfilSel.descricao="
							+ ac.getPerfil().getDescricao()
							+ "&perfilSel.sigla="
							+ ac.getPerfil().getSigla()
							+ "\">" + ac.getPerfil().getSigla() + "</a>");
		if (ac.getLotacao() != null)
			sb
					.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=2&lotacaoSel.id="
							+ ac.getLotacao().getId()
							+ "&lotacaoSel.descricao="
							+ ac.getLotacao().getDescricao()
							+ "&lotacaoSel.sigla="
							+ ac.getLotacao().getSigla()
							+ "\">" + ac.getLotacao().getSigla() + "</a>");
		if (ac.getPessoa() != null)
			sb
					.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=3&pessoaSel.id="
							+ ac.getPessoa().getId()
							+ "&pessoaSel.descricao="
							+ ac.getPessoa().getDescricao()
							+ "&pessoaSel.sigla="
							+ ac.getPessoa().getSigla()
							+ "\">" + ac.getPessoa().getSigla() + "</a>");
		sb.append("</li>");
	}

	private void acrescentarHTMLOld(Collection<ConfiguracaoAcesso> l,
			StringBuilder sb) {
		sb.append("<ul style=\"list-style-type:disc;\">");
		for (ConfiguracaoAcesso ac : l) {
			sb.append("<span id=\"SPAN-" + ac.getServico().getId() + "\">");
			acessoHTML(sb, ac);
			sb.append("</span>");
			if (ac.getSubitens() != null && ac.getSubitens().size() > 0)
				acrescentarHTML(ac.getSubitens(), sb);
		}
		sb.append("</ul>");
	}

	private void acessoHTMLOld(StringBuilder sb, ConfiguracaoAcesso ac) {
		boolean fPode = ac.getSituacao().getDscSitConfiguracao().equals("Pode");
		boolean fNaoPode = ac.getSituacao().getDscSitConfiguracao().equals(
				"Não Pode");
		sb.append("<li style=\"color:"
				+ (fPode ? "green" : (fNaoPode ? "red" : "black")) + ";\">");
		if (ac.getServico().getCpServicoPai() != null
				&& ac.getServico().getSigla().startsWith(
						ac.getServico().getCpServicoPai().getSigla() + "-")) {
			sb.append(ac.getServico().getSigla().substring(
					ac.getServico().getCpServicoPai().getSigla().length() + 1));
		} else {
			sb.append(ac.getServico().getSigla());
		}
		sb.append(": <span style=\"\">");
		sb.append(ac.getServico().getDescricao());
		if (!(fPode || fNaoPode)) {
			sb.append(" (");
			sb.append(ac.getSituacao().getDscSitConfiguracao());
			sb.append(")");
		}
		sb.append("</span>");
		if (fNaoPode)
			sb.append(" - <a style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",1)\">Permitir</a>");
		if (fPode)
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",2)\">Proibir</a>");
		if (!ac.isDefault())
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("
					+ ac.getServico().getId() + ",9)\">Default</a>");
		sb.append("</li>");
	}

	public Long getIdServico() {
		return idServico;
	}

	public Long getIdSituacao() {
		return idSituacao;
	}

	public List<ConfiguracaoAcesso> getItens() {
		return itens;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public CpSituacaoConfiguracao getSituacao() {
		return situacao;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public void setIdSituacao(Long idSituacao) {
		this.idSituacao = idSituacao;
	}

	public void setItens(List<ConfiguracaoAcesso> itens) {
		this.itens = itens;
	}

	public void setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
	}

	public void setSituacao(CpSituacaoConfiguracao situacao) {
		this.situacao = situacao;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public void setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public int getIdAbrangencia() {
		return idAbrangencia;
	}

	public void setIdAbrangencia(int idAbrangencia) {
		this.idAbrangencia = idAbrangencia;
	}

	public CpPerfilSelecao getPerfilSel() {
		return perfilSel;
	}

	public void setPerfilSel(CpPerfilSelecao perfilSel) {
		this.perfilSel = perfilSel;
	}

	public String getNomeOrgaoUsu() {
		return nomeOrgaoUsu;
	}

	public void setNomeOrgaoUsu(String nomeOrgaoUsu) {
		this.nomeOrgaoUsu = nomeOrgaoUsu;
	}

}
