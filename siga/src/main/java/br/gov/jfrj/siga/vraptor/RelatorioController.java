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
package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.libs.rpc.FaultMethodResponseRPC;
import br.gov.jfrj.siga.libs.rpc.SimpleMethodResponseRPC;
import br.gov.jfrj.siga.relatorio.AcessoServicoRelatorio;
import br.gov.jfrj.siga.relatorio.AlteracaoDireitosRelatorio;
import br.gov.jfrj.siga.relatorio.HistoricoUsuarioRelatorio;
import br.gov.jfrj.siga.relatorio.PermissaoUsuarioRelatorio;
import net.sf.jasperreports.engine.JRException;

@Controller
public class RelatorioController extends SigaController {

	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioController() {
		super();
	}

	@Inject
	public RelatorioController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		
		setCpServicos(obterServicos());
		setCpOrgaosUsuario(obterOrgaosUsuario());		
	}

	protected String streamRel = "inputStream";
	private String contentType = "image/pdf";
	private InputStream inputStream;
	private String idServico;
	private String situacoesSelecionadas;
	private String idOrgaoUsuario;
	private String idPessoa;
	private String dataInicio;
	private String dataFim; 
	private List<CpServico> cpServicos;
	private List<CpOrgaoUsuario> cpOrgaosUsuario;
	private String respostaXMLStringRPC;


	@Get("/app/gi/relatorio/selecionar_acesso_servico")
	public void selecionarAcessoServico() throws Exception {
		result.include("cpServicos", cpServicos);	
		result.include("cpOrgaosUsuario", cpOrgaosUsuario);	
	}

	
	@Post("/app/gi/relatorio/emitir_acesso_servico")
	public Download emitirAcessoServico(String idServico, String idOrgaoUsuario, String situacoesSelecionadas) throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idServico", idServico);
		listaParametros.put("situacoesSelecionadas", situacoesSelecionadas);
		listaParametros.put("idOrgaoUsuario", idOrgaoUsuario);
		
		AcessoServicoRelatorio rel = new AcessoServicoRelatorio(listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		
		return new InputStreamDownload(inputStream, "application/pdf", "relatorio.pdf");
	}

	@Get("app/gi/relatorio/selecionar_permissao_usuario")
	public void permissao_usuario_selecionar() {
		System.out.println("Filtro do Relatório foi chamado.");
	}

	@Get("app/gi/relatorio/emitir_permissao_usuario")
	public Download emitirRelPermissaoUsuario(String idPessoa) throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idPessoa", idPessoa);
		PermissaoUsuarioRelatorio rel = new PermissaoUsuarioRelatorio(listaParametros);
		extracted(rel);		
		return new InputStreamDownload(getInputStream(), "application/pdf", "relatorio.pdf");
	}
	
	private String extracted(PermissaoUsuarioRelatorio rel) throws Exception,JRException {
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return "relatorio";
	}	
	
	@Get("app/gi/relatorio/selecionar_historico_usuario")
	public void historico_usuario_selecionar() {
		System.out.println("Filtro do Relatório foi chamado.");
	}

	
	@Get("app/gi/relatorio/emitir_historico_usuario")
	public Download emitirRelHistoricoUsuario(String idPessoa) throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("idPessoa", idPessoa);
		HistoricoUsuarioRelatorio rel = new HistoricoUsuarioRelatorio(listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(getInputStream(), "application/pdf", "relatorio.pdf");		
	}

	@Get("app/gi/relatorio/selecionar_alteracao_direitos")
	public void alteracao_direitos_selecionar() {
		result.include("cpOrgaosUsuario", getCpOrgaosUsuario());
	}

	@Get("app/gi/relatorio/emitir_alteracao_direitos")
	public Download emitirRelAlteracaoDireitos(String dataInicio
			                                  ,String dataFim
			                                  ,String idOrgaoUsuario) throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("dataInicio", dataInicio);
		listaParametros.put("dataFim", dataFim);
		listaParametros.put("idOrgaoUsuario", idOrgaoUsuario);
		AlteracaoDireitosRelatorio rel = new AlteracaoDireitosRelatorio(listaParametros);
		rel.gerar();
		this.setInputStream(new ByteArrayInputStream(rel.getRelatorioPDF()));
		return new InputStreamDownload(getInputStream(), "application/pdf", "relatorio.pdf");
	}

	@Get("/app/gi/relatorio/obter_situacoes_servico")
	public void obterSituacoesServico(String idServico) {
		try {
			Long t_lngIdServico = Long.parseLong(idServico);
			CpServico t_cpsServico = dao().consultar(t_lngIdServico,
					CpServico.class, false);
			Set<CpSituacaoDeConfiguracaoEnum> t_setSituacao = t_cpsServico
					.getCpTipoServico().getCpSituacoesConfiguracaoSet();
			HashMap<String, String> t_hmpRetorno = new HashMap<String, String>();
			t_hmpRetorno.put("idservico",
					String.valueOf(t_cpsServico.getIdServico()));
			if (t_setSituacao.size() < 1) {
				throw new AplicacaoException(
						"Serviço sem situação! >> Avise sistemas");
			}
			CpSituacaoDeConfiguracaoEnum t_scfSituacaoDefault = t_cpsServico
					.getCpTipoServico().getSituacaoDefault();
			t_hmpRetorno.put("quantas", String.valueOf(t_setSituacao.size()));
			Integer t_intContaServico = new Integer(0);
			for (CpSituacaoDeConfiguracaoEnum t_scfSituacao : t_setSituacao) {
				t_hmpRetorno.put("idsituacao_" + t_intContaServico,
						String.valueOf(t_scfSituacao.getId()));
				t_hmpRetorno.put("descsituacao_" + t_intContaServico,
						t_scfSituacao.getDescr());
				t_hmpRetorno.put(
						"sedefault_" + t_intContaServico,
						t_scfSituacaoDefault.getId().equals(
								t_scfSituacao.getId()) ? "true"
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
	
		result.use(Results.http()).body(this.respostaXMLStringRPC);
	}

	private List<CpServico> obterServicos() {
		return dao().listarServicos();
	}


	private List<CpOrgaoUsuario> obterOrgaosUsuario() {
		return dao().listarOrgaosUsuarios();
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getIdServico() {
		return idServico;
	}

	public void setIdServico(String idServico) {
		this.idServico = idServico;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public String getStreamRel() {
		return streamRel;
	}

	public void setStreamRel(String streamRel) {
		this.streamRel = streamRel;
	}

	public List<CpServico> getCpServicos() {
		return cpServicos;
	}

	public void setCpServicos(List<CpServico> cpServicos) {
		this.cpServicos = cpServicos;
	}

	public String getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(String idPessoa) {
		this.idPessoa = idPessoa;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getRespostaXMLStringRPC() {
		return respostaXMLStringRPC;
	}

	public void setRespostaXMLStringRPC(String respostaXMLStringRPC) {
		this.respostaXMLStringRPC = respostaXMLStringRPC;
	}

	public List<CpOrgaoUsuario> getCpOrgaosUsuario() {
		return cpOrgaosUsuario;
	}

	public void setCpOrgaosUsuario(List<CpOrgaoUsuario> cpOrgaosUsuario) {
		this.cpOrgaosUsuario = cpOrgaosUsuario;
	}

	public String getSituacoesSelecionadas() {
		return situacoesSelecionadas;
	}

	public void setSituacoesSelecionadas(String situacoesSelecionadas) {
		this.situacoesSelecionadas = situacoesSelecionadas;
	}

	public String getIdOrgaoUsuario() {
		return idOrgaoUsuario;
	}

	public void setIdOrgaoUsuario(String idOrgaoUsuario) {
		this.idOrgaoUsuario = idOrgaoUsuario;
	}

}
