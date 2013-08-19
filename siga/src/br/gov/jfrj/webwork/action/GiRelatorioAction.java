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
package br.gov.jfrj.webwork.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.libs.rpc.FaultMethodResponseRPC;
import br.gov.jfrj.siga.libs.rpc.SimpleMethodResponseRPC;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;
import br.gov.jfrj.siga.relatorio.AcessoServicoRelatorio;
import br.gov.jfrj.siga.relatorio.AlteracaoDireitosRelatorio;
import br.gov.jfrj.siga.relatorio.HistoricoUsuarioRelatorio;
import br.gov.jfrj.siga.relatorio.PermissaoUsuarioRelatorio;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.Preparable;

public class GiRelatorioAction extends SigaActionSupport implements Preparable {
	ServletActionContext ctx;
	ActionInvocation invocation;
	protected String streamRel = "inputStream";
	private String contentType = "image/pdf";
	private InputStream inputStream;
	// Parâmetros de entrada para para o Relatório de Acesso à Utilização de
	// Serviço
	private String idServico;
	private String situacoesSelecionadas;
	private String idOrgaoUsuario;
	//
	private String idPessoa; // Parâmetro de entrada para para os Relatórios de
								// Permissão e de histórico
	private String dataInicio; // Parâmetro de entrada para para o Relatório de
								// Alteração de Direitos
	private String dataFim; // Parâmetro de entrada para para o Relatório de
							// Alteração de Direitos
	// preparação
	private List<CpServico> cpServicos;
	private List<CpOrgaoUsuario> cpOrgaosUsuario;
	// retorno Ajax
	private String respostaXMLStringRPC;

	public void prepare() {
		setCpServicos(obterServicos());
		setCpOrgaosUsuario(obterOrgaosUsuario());
	}

	/**
	 * Seleciona o serviço para o Relatório de Acesso à Utilização de Serviço
	 */
	public String aSelecionarRelAcessoServico() throws Exception {
		return Action.SUCCESS;
	}

	/**
	 * emite o Relatório de Acesso à Utilização de Serviço
	 */
	public String aEmitirRelAcessoServico() throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idServico", getIdServico());
		listaParametros
				.put("situacoesSelecionadas", getSituacoesSelecionadas());
		listaParametros.put("idOrgaoUsuario", getIdOrgaoUsuario());
		/*
		 * === Verificação dôs parâmetros === Enumeration params =
		 * ServletActionContext.getRequest().getParameterNames(); while
		 * (params.hasMoreElements()) { String oPar = (String)
		 * params.nextElement(); if (oPar.startsWith("situacao_")) {
		 * listaParametros.put(oPar,(String)
		 * (ServletActionContext.getRequest().getParameter(oPar))); } }
		 */
		AcessoServicoRelatorio rel = new AcessoServicoRelatorio(listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return "relatorio";
	}

	/**
	 * Seleciona o usuário para o Relatório de Permissão de Usuários
	 */
	public String aSelecionarPermissaoUsuario() {
		return Action.SUCCESS;
	}

	/**
	 * emite o Relatório de Permissão de Usuários
	 */
	public String aEmitirRelPermissaoUsuario() throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idPessoa", idPessoa);
		PermissaoUsuarioRelatorio rel = new PermissaoUsuarioRelatorio(
				listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return "relatorio";
	}

	/**
	 * Seleciona o usuário para o Relatório de Permissão de Usuários
	 */
	public String aSelecionarHistoricoUsuario() {
		return Action.SUCCESS;
	}

	/**
	 * emite o Relatório de Permissão de Usuários
	 */
	public String aEmitirRelHistoricoUsuario() throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idPessoa", idPessoa);
		HistoricoUsuarioRelatorio rel = new HistoricoUsuarioRelatorio(
				listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return "relatorio";
	}

	/**
	 * Seleciona o período para a emissão do relatório de Alteração de direitos
	 */
	public String aSelecionarAlteracaoDireitos() {
		return Action.SUCCESS;
	}

	/**
	 * emite o Relatório de Alteração de direitos
	 */
	public String aEmitirRelAlteracaoDireitos() throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("dataInicio", dataInicio);
		listaParametros.put("dataFim", dataFim);
		listaParametros.put("idOrgaoUsuario", idOrgaoUsuario);
		AlteracaoDireitosRelatorio rel = new AlteracaoDireitosRelatorio(
				listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return "relatorio";
	}

	/**
	 * Obtém as situações do Servico via Ajax
	 */
	public String aObterSituacoesServico() {
		try {
			Long t_lngIdServico = Long.parseLong(idServico);
			CpServico t_cpsServico = dao().consultar(t_lngIdServico,
					CpServico.class, false);
			Set<CpSituacaoConfiguracao> t_setSituacao = t_cpsServico
					.getCpTipoServico().getCpSituacoesConfiguracaoSet();
			HashMap<String, String> t_hmpRetorno = new HashMap<String, String>();
			t_hmpRetorno.put("idservico",
					String.valueOf(t_cpsServico.getIdServico()));
			if (t_setSituacao.size() < 1) {
				throw new AplicacaoException(
						"Serviço sem situação! >> Avise sistemas");
			}
			CpSituacaoConfiguracao t_scfSituacaoDefault = t_cpsServico
					.getCpTipoServico().getSituacaoDefault();
			t_hmpRetorno.put("quantas", String.valueOf(t_setSituacao.size()));
			Integer t_intContaServico = new Integer(0);
			for (CpSituacaoConfiguracao t_scfSituacao : t_setSituacao) {
				t_hmpRetorno.put("idsituacao_" + t_intContaServico,
						String.valueOf(t_scfSituacao.getIdSitConfiguracao()));
				t_hmpRetorno.put("descsituacao_" + t_intContaServico,
						t_scfSituacao.getDscSitConfiguracao());
				t_hmpRetorno.put(
						"sedefault_" + t_intContaServico,
						t_scfSituacaoDefault.getIdSitConfiguracao().equals(
								t_scfSituacao.getIdSitConfiguracao()) ? "true"
								: "false");
				t_intContaServico++;
			}

			SimpleMethodResponseRPC t_smrResposta = new SimpleMethodResponseRPC();
			t_smrResposta.setMembersFrom(t_hmpRetorno);
			setRespostaXMLStringRPC(t_smrResposta.toXMLString());
		} catch (Exception e) {
			FaultMethodResponseRPC t_fmrRetorno = new FaultMethodResponseRPC();
			t_fmrRetorno.set(0, e.getMessage());
			setRespostaXMLStringRPC(t_fmrRetorno.toXMLString());
		}
		return "situacoesServico";
	}

	/**
	 * obtém todos os serviços
	 */
	private List<CpServico> obterServicos() {
		return dao().listarServicos();
	}

	/**
	 * obtém os órgãos de usuário
	 */
	private List<CpOrgaoUsuario> obterOrgaosUsuario() {
		return dao().listarOrgaosUsuarios();
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * @return the idServico
	 */
	public String getIdServico() {
		return idServico;
	}

	/**
	 * @param idServico
	 *            the idServico to set
	 */
	public void setIdServico(String idServico) {
		this.idServico = idServico;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the ctx
	 */
	public ServletActionContext getCtx() {
		return ctx;
	}

	/**
	 * @param ctx
	 *            the ctx to set
	 */
	public void setCtx(ServletActionContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * @return the invocation
	 */
	public ActionInvocation getInvocation() {
		return invocation;
	}

	/**
	 * @param invocation
	 *            the invocation to set
	 */
	public void setInvocation(ActionInvocation invocation) {
		this.invocation = invocation;
	}

	/**
	 * @return the streamRel
	 */
	public String getStreamRel() {
		return streamRel;
	}

	/**
	 * @param streamRel
	 *            the streamRel to set
	 */
	public void setStreamRel(String streamRel) {
		this.streamRel = streamRel;
	}

	/**
	 * @return the cpServicos
	 */
	public List<CpServico> getCpServicos() {
		return cpServicos;
	}

	/**
	 * @param cpServicos
	 *            the cpServicos to set
	 */
	public void setCpServicos(List<CpServico> cpServicos) {
		this.cpServicos = cpServicos;
	}

	/**
	 * @return the idPessoa
	 */
	public String getIdPessoa() {
		return idPessoa;
	}

	/**
	 * @param idPessoa
	 *            the idPessoa to set
	 */
	public void setIdPessoa(String idPessoa) {
		this.idPessoa = idPessoa;
	}

	/**
	 * @return the dataInicio
	 */
	public String getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public String getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the respostaXMLStringRPC
	 */
	public String getRespostaXMLStringRPC() {
		return respostaXMLStringRPC;
	}

	/**
	 * @param respostaXMLStringRPC
	 *            the respostaXMLStringRPC to set
	 */
	public void setRespostaXMLStringRPC(String respostaXMLStringRPC) {
		this.respostaXMLStringRPC = respostaXMLStringRPC;
	}

	/**
	 * @return the cpOrgaosUsuario
	 */
	public List<CpOrgaoUsuario> getCpOrgaosUsuario() {
		return cpOrgaosUsuario;
	}

	/**
	 * @param cpOrgaosUsuario
	 *            the cpOrgaosUsuario to set
	 */
	public void setCpOrgaosUsuario(List<CpOrgaoUsuario> cpOrgaosUsuario) {
		this.cpOrgaosUsuario = cpOrgaosUsuario;
	}

	/**
	 * @return the situacoesSelecionadas
	 */
	public String getSituacoesSelecionadas() {
		return situacoesSelecionadas;
	}

	/**
	 * @param situacoesSelecionadas
	 *            the situacoesSelecionadas to set
	 */
	public void setSituacoesSelecionadas(String situacoesSelecionadas) {
		this.situacoesSelecionadas = situacoesSelecionadas;
	}

	/**
	 * @return the idOrgaoUsuario
	 */
	public String getIdOrgaoUsuario() {
		return idOrgaoUsuario;
	}

	/**
	 * @param idOrgaoUsuario
	 *            the idOrgaoUsuario to set
	 */
	public void setIdOrgaoUsuario(String idOrgaoUsuario) {
		this.idOrgaoUsuario = idOrgaoUsuario;
	}

}
