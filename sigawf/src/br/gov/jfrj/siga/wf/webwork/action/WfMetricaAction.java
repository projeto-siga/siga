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
package br.gov.jfrj.siga.wf.webwork.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.relatorio.RelEstatisticaProcedimento;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDoc;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDocDetalhado;

/**
 * Classe responsável pelas exibições das estatísticas dos procedimentos.
 * 
 * @author kpf
 * 
 */
public class WfMetricaAction extends WfSigaActionSupport {

	
	private static final Long REL_ESTATISTICAS_GERAIS = 1L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS = 2L;
	private static final Long REL_TEMPO_DE_DOCUMENTOS_DETALHADO = 3L;

	private Long pdId;
	private InputStream inputStream;
	private String procedimento;
	private Map parametrosRelatorio;
	private String incluirAbertos;

	/**
	 * Retorna o órgão.
	 * 
	 * @return
	 */
	public String getOrgao() {
		return getLotaTitular().getOrgaoUsuario().getNmOrgaoUsu();
	}

	/**
	 * Retorna o procedimento.
	 * 
	 * @return
	 */
	public String getProcedimento() {
		return procedimento;
	}

	/**
	 *Define o procedimento.
	 * 
	 * @param procedimento
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	/**
	 * Este método é executado quando a action "medir.action" é chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aMedir() throws Exception {
		assertAcesso("MEDIR:Analisar métricas");
		return "SUCESS";
	}

	public String aEmitirRelatorio() throws Exception{
		parametrosRelatorio = new HashMap<String, String>();

		parametrosRelatorio.put("secaoUsuario", getLotaTitular().getOrgaoUsuario().getNmOrgaoUsu());
		parametrosRelatorio.put("dataInicialDe", getRequest().getParameter(
				"dataInicialDe"));
		parametrosRelatorio.put("dataInicialAte", getRequest().getParameter(
		"dataInicialAte"));
		parametrosRelatorio.put("dataFinalDe", getRequest().getParameter("dataFinalDe"));
		parametrosRelatorio.put("dataFinalAte", getRequest().getParameter("dataFinalAte"));
		
		
		String nomeProcedimento = WfDao.getInstance().getProcessDefinition(getPdId()).getName();
		
		parametrosRelatorio.put("nomeProcedimento", nomeProcedimento);
		parametrosRelatorio.put("incluirAbertos",incluirAbertos==null?false:incluirAbertos );

		
		Long relatorioEscolhido = Long.valueOf(getRequest().getParameter("selecaoRelatorio"));
		
		if (relatorioEscolhido.equals(REL_ESTATISTICAS_GERAIS)){
			gerarRelEstGeral();
		}
		if (relatorioEscolhido.equals(REL_TEMPO_DE_DOCUMENTOS)){
			gerarRelTempoDoc();
		}
		if (relatorioEscolhido.equals(REL_TEMPO_DE_DOCUMENTOS_DETALHADO)){
			gerarRelTempoDocDetalhado();
		}
		
		return "relatorioPDF";
	}
	
	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
	private void gerarRelTempoDocDetalhado() throws Exception {
		try {
			RelTempoDocDetalhado rel = new RelTempoDocDetalhado(
					parametrosRelatorio);
			rel.gerar();

			this
					.setInputStream(new ByteArrayInputStream(rel
							.getRelatorioPDF()));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}	}

	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private void gerarRelEstGeral() throws Exception {
		try {


			RelEstatisticaProcedimento rel = new RelEstatisticaProcedimento(
					parametrosRelatorio);
			rel.gerar();

			this
					.setInputStream(new ByteArrayInputStream(rel
							.getRelatorioPDF()));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	/**
	 * Cria um relatório estatístico de um determinado procedimento. Este método
	 * é executado quando a action "emiteRelEstatisticaProcedimento.action" é
	 * chamada.
	 * 
	 * @return
	 * @throws Exception
	 */
	private void gerarRelTempoDoc() throws Exception {
		try {
			RelTempoDoc rel = new RelTempoDoc(
					parametrosRelatorio);
			rel.gerar();

			this
					.setInputStream(new ByteArrayInputStream(rel
							.getRelatorioPDF()));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Retorna o inputStream com o PDF do relatório.
	 * 
	 * @return
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 *Define o inputStream com o PDF do relatório
	 * 
	 * @param inputStream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	
	public void setIncluirAbertos(String incluirAbertos) {
		this.incluirAbertos = incluirAbertos;
	}

	public String getIncluirAbertos() {
		return incluirAbertos;
	}

	public void setPdId(Long pdId) {
		this.pdId = pdId;
	}

	public Long getPdId() {
		return pdId;
	}
}
