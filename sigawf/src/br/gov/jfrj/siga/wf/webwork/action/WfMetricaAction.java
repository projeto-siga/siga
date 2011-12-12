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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.wf.relatorio.RelEstatisticaProcedimento;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDocDetalhado;
import br.gov.jfrj.siga.wf.relatorio.RelTempoDoc;

/**
 * Classe responsável pelas exibições das estatísticas dos procedimentos.
 * 
 * @author kpf
 * 
 */
public class WfMetricaAction extends WfSigaActionSupport {

	private InputStream inputStream;
	private String orgao;
	private String procedimento;
	private String secaoUsuario;
	private Map parametrosRelatorio;

	/**
	 * Retorna o órgão.
	 * 
	 * @return
	 */
	public String getOrgao() {
		return orgao;
	}

	/**
	 *Define o órgão.
	 * 
	 * @param orgao
	 */
	public void setOrgao(String orgao) {
		this.orgao = orgao;
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

		parametrosRelatorio.put("secaoUsuario", getRequest().getParameter(
				"secaoUsuario"));
		parametrosRelatorio.put("dataInicialDe", getRequest().getParameter(
				"dataInicialDe"));
		parametrosRelatorio.put("dataInicialAte", getRequest().getParameter(
		"dataInicialAte"));
		parametrosRelatorio.put("dataFinalDe", getRequest().getParameter("dataFinalDe"));
		parametrosRelatorio.put("dataFinalAte", getRequest().getParameter("dataFinalAte"));
		parametrosRelatorio.put("nomeProcedimento", getRequest().getParameter(
				"procedimento"));
		
		String relatorioEscolhido = (String)getRequest().getParameter(
		"selecaoRelatorio");
		
		if (relatorioEscolhido.equals("Estatísticas gerais")){
			gerarRelEstGeral();
		}
		if (relatorioEscolhido.equals("Tempo de documentos")){
			gerarRelTempoDoc();
		}
		if (relatorioEscolhido.equals("Tempo de documentos detalhado")){
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

	/**
	 * Retorna a seção do usuário.
	 * 
	 * @return
	 */
	public String getSecaoUsuario() {
		return secaoUsuario;
	}

	/**
	 *Define a seção do usuário.
	 * 
	 * @param secaoUsuario
	 */
	public void setSecaoUsuario(String secaoUsuario) {
		this.secaoUsuario = secaoUsuario;
	}
	
	public List<String> getRelatoriosMetricas(){
		List<String> lista = new ArrayList<String>();
		lista.add("Estatísticas gerais");
		lista.add("Tempo de documentos");
		lista.add("Tempo de documentos detalhado");
		return lista; 
	}
}
