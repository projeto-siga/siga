/*******************************************************************************
 * Copyright (c) 2006 - 2013 SJRJ.
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

import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

public class ExTemporalidadeAction extends SigaActionSupport {


	private String acao;

	private ExTemporalidade exTemporal;
	private Long idTemporalidade;
	private String descTemporalidade;
	private Integer valorTemporalidade;
	private Long idCpUnidade;


	public ExTemporalidadeAction() {
		// TODO Auto-generated constructor stub
	}
	
	public String aListar() throws AplicacaoException, Exception{
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade");
		return SUCCESS;
	}


	public String aEditar() throws Exception{
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade");
		if (exTemporal==null){
			exTemporal = buscarExTemporalidade(getIdTemporalidade());
		}
		if (exTemporal == null && !getAcao().equals("nova_temporalidade")){
			throw new AplicacaoException("A temporalidade não está disponível. ID = " + getIdTemporalidade());
		}
		return SUCCESS;
	}

	private ExTemporalidade buscarExTemporalidade(Long id) {
		if (id!=null){
			return ExDao.getInstance().consultar(id, ExTemporalidade.class, false);	
		}
		return null;
	}

	public String aGravar() throws Exception{
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade");
		dao().iniciarTransacao();

		validarDados();

		exTemporal = buscarExTemporalidade(getIdTemporalidade());
		if (getAcao().equals("nova_temporalidade")){
			if (exTemporal!=null){
				throw new AplicacaoException("A temporalidade já existe: " + exTemporal.getDescTemporalidade());
			}else{
				exTemporal = new ExTemporalidade();
				lerForm(exTemporal);
				Ex.getInstance().getBL().incluirExTemporalidade(exTemporal,getIdentidadeCadastrante());
			}
		}else{
			alterarTemporalidadeExistente(exTemporal);
		}
		
		dao().commitTransacao();
		
		setMensagem("Classificação salva!");
		return SUCCESS;
	}

	private void validarDados() throws AplicacaoException {
		if (getDescTemporalidade()==null || getDescTemporalidade().trim().length()==0){
			throw new AplicacaoException("Você deve especificar uma descrição!");
		}
		if (getValorTemporalidade()!=null && getIdCpUnidade()<=0){
			throw new AplicacaoException("Você deve especificar a unidade de medida do valor informado!");
		}
		if (getValorTemporalidade()==null && getIdCpUnidade()>0){
			throw new AplicacaoException("Você deve especificar um valor para a unidade de medida informada!");
		}
	}

	private void alterarTemporalidadeExistente(ExTemporalidade exTempAntiga) throws AplicacaoException {
		
		ExTemporalidade exTempNovo = Ex.getInstance().getBL().getCopia(exTempAntiga);
		lerForm(exTempNovo);

		Ex.getInstance().getBL().alterarExTemporalidade(exTempNovo, exTempAntiga, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		exTemporal = exTempNovo;
	}
	
	public String aExcluir() throws Exception{
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;TT:Tabela de Temporalidade");
		dao().iniciarTransacao();
		exTemporal = buscarExTemporalidade(getIdTemporalidade());
		Date dt = dao().consultarDataEHoraDoServidor();
		
		if (exTemporal.getExViaArqCorrenteSet().size() >0){
			StringBuffer sb = new StringBuffer();
			for(ExVia v: exTemporal.getExViaArqCorrenteSet()){
				sb.append("(id: ");
				sb.append(v.getId());
				sb.append(") (Arquivo Corrente)");
				sb.append("<br/> Classificação: ");
				sb.append(v.getExClassificacao().getDescricaoCompleta());
				sb.append(" (Via ");
				sb.append(v.getLetraVia());
				sb.append(")<br/><br/>");
			}
			ExVia v = exTemporal.getExViaArqCorrenteSet().iterator().next();
			
			throw new AplicacaoException("Não é possível excluir a temporalidade documental, pois está associada às seguintes classificações documentais:<br/><br/>" +
					sb.toString() );
		}
		
		if (exTemporal.getExViaArqIntermediarioSet().size() >0){
			StringBuffer sb = new StringBuffer();
			for(ExVia v: exTemporal.getExViaArqIntermediarioSet()){
				sb.append("(id: ");
				sb.append(v.getId());
				sb.append(") (Arquivo Intermediario)");
				sb.append("<br/> Classificação: ");
				sb.append(v.getExClassificacao().getDescricaoCompleta());
				sb.append(" (Via ");
				sb.append(v.getLetraVia());
				sb.append(")<br/><br/>");
			}
			ExVia v = exTemporal.getExViaArqCorrenteSet().iterator().next();
			
			throw new AplicacaoException("Não é possível excluir a temporalidade documental, pois está associada às seguintes classificações documentais:<br/><br/>" +
					sb.toString() );
		}

		
		dao().excluirComHistorico(exTemporal, dt, getIdentidadeCadastrante());
		dao().commitTransacao();
		return SUCCESS;
	}
	
	
	private void lerForm(ExTemporalidade t) {
		t.setDescTemporalidade(getDescTemporalidade());
		t.setValorTemporalidade(getValorTemporalidade());

		CpUnidadeMedida cpUnidade =null;
		if (getIdCpUnidade()>0){
			cpUnidade = ExDao.getInstance().consultar(getIdCpUnidade(), CpUnidadeMedida.class, false);	

		}
		t.setCpUnidadeMedida(cpUnidade);	
	}
	
	public List<ExTemporalidade> getTemporalidadeVigente(){
		return ExDao.getInstance().listarAtivos(ExTemporalidade.class,"descTemporalidade");
	}
	
	public List<CpUnidadeMedida> getListaCpUnidade(){
		return ExDao.getInstance().listarUnidadesMedida();
	}
	
	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getAcao() {
		return acao;
	}
	
	public Integer getTotalDeNiveis(){
		return MascaraUtil.getInstance().getTotalDeNiveisDaMascara();
	}

	public ExTemporalidade getExTemporal() {
		return exTemporal;
	}

	public void setExTemporal(ExTemporalidade exTemporal) {
		this.exTemporal = exTemporal;
	}

	public Long getIdTemporalidade() {
		return idTemporalidade;
	}

	public void setIdTemporalidade(Long idTemporalidade) {
		this.idTemporalidade = idTemporalidade;
	}

	public String getDescTemporalidade() {
		return descTemporalidade;
	}

	public void setDescTemporalidade(String descTemporalidade) {
		this.descTemporalidade = descTemporalidade;
	}

	public Long getIdCpUnidade() {
		return idCpUnidade;
	}

	public void setIdCpUnidade(Long idCpUnidade) {
		this.idCpUnidade = idCpUnidade;
	}

	public Integer getValorTemporalidade() {
		return valorTemporalidade;
	}

	public void setValorTemporalidade(Integer valor) {
		if(valor<0){
			valor = null;
		}
		this.valorTemporalidade = valor;
	}

}
