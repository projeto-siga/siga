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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;

import br.gov.jfrj.siga.wf.SigaWfProperties;
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
	
	private Long grpIni;
	private Long grpFim;

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
		try{
			return new String(procedimento.getBytes("iso8859-1"));
		}catch (Exception e) {

		}
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

		for (Object o: getRequest().getParameterMap().keySet()) {
			String k = (String)o;
			String v = (String) getRequest().getParameter(k);
			parametrosRelatorio.put(k,v);
		}
		
		parametrosRelatorio.put("secaoUsuario", getLotaTitular().getOrgaoUsuario().getNmOrgaoUsu());
		parametrosRelatorio.put("nomeProcedimento", getProcedimentoEscolhido().getName());
		parametrosRelatorio.put("incluirAbertos",incluirAbertos==null?false:incluirAbertos);
		parametrosRelatorio.put("inicioGrupo",(grpIni==null || grpIni < 0)?null:getNomeGrupo(grpIni));
		parametrosRelatorio.put("fimGrupo",(grpFim==null || grpFim < 0)?null:getNomeGrupo(grpFim));
		
		if (getRelatorioEscolhido().equals(REL_ESTATISTICAS_GERAIS)){
			gerarRelEstGeral();
		}
		if (getRelatorioEscolhido().equals(REL_TEMPO_DE_DOCUMENTOS)){
			gerarRelTempoDoc();
		}
		if (getRelatorioEscolhido().equals(REL_TEMPO_DE_DOCUMENTOS_DETALHADO)){
			gerarRelTempoDocDetalhado();
		}
		
		return "relatorioPDF";
	}

	/**
	 * Método utilizado para 
	 * @param idGrupo
	 * @return
	 */
	private String getNomeGrupo(Long idGrupo) {
		Task t = WfDao.getInstance().consultar(idGrupo, Task.class, false);
		return t.getName();
		
	}

	private ProcessDefinition getProcedimentoEscolhido() {
		return WfDao.getInstance().getProcessDefinition(getPdId());
	}

	private Long getRelatorioEscolhido() {
		return Long.valueOf(getRequest().getParameter("selecaoRelatorio"));
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
	
	public String getMinMediaTruncada(){
		return SigaWfProperties.getRelEstatGeraisMinMediaTrunc().toString().replace(".", ",");
	}
	public String getMaxMediaTruncada(){
		return SigaWfProperties.getRelEstatGeraisMaxMediaTrunc().toString().replace(".", ",");
	}
	
	public Set<Task> getLstGruposIni(){
		
		return WfDao.getInstance().getTodasAsTarefas(getProcedimentoEscolhido().getName());
	}


	public Set<Task>  getLstGruposFim(){
		return getLstGruposIni();
	}

	public void setGrpFim(Long grpFim) {
		this.grpFim = grpFim;
	}

	public Long getGrpFim() {
		return grpFim;
	}

	public void setGrpIni(Long grpIni) {
		this.grpIni = grpIni;
	}

	public Long getGrpIni() {
		return grpIni;
	}

}
