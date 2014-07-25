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
 * Criado em  13/09/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.libs.webwork.DpCargoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

import com.opensymphony.xwork.Action;

public class ExFormaDocumentoAction extends ExSelecionavelActionSupport {

	public Integer id;
	public String descricao;
	public String sigla;
	public Long idTipoFormaDoc;
	public String tipoModelo;
	public String script;
	
	private boolean origemExterno;
	private boolean origemInternoProduzido;
	private boolean origemInternoImportado;

	public ExFormaDocumento forma;

	public Integer postback;

	private ExClassificacaoSelecao classificacaoSel;
	
	private DpPessoaSelecao pessoaSel;
	private DpLotacaoSelecao lotacaoSel;
	private DpCargoSelecao cargoSel;

	private DpFuncaoConfiancaSelecao funcaoSel;
	
	private String mensagem;
	
	public ExFormaDocumentoAction() {
		classificacaoSel = new ExClassificacaoSelecao();
		pessoaSel = new DpPessoaSelecao();
		lotacaoSel = new DpLotacaoSelecao();
		cargoSel = new DpCargoSelecao();
		funcaoSel = new DpFuncaoConfiancaSelecao();
	}

	public List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		return dao().listarOrdemNivel();
	}

	public List<ExTipoFormaDoc> getListaTiposFormaDoc() throws Exception {
		return dao().listarExTiposFormaDoc();
	}

	public String aListar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		setItens(dao().listarTodosOrdenarPorDescricao());
		return Action.SUCCESS;
	}

	public String aEditar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		if (getPostback() == null) {
			lerForm();
			escreverForm();
		}
		return Action.SUCCESS;
	}

	private void lerForm() throws AplicacaoException,
			UnsupportedEncodingException {
		if (id == null) {
			forma = new ExFormaDocumento();
		} else {
			forma = dao().consultar(getId(), ExFormaDocumento.class, false);
		}

		if (getPostback() != null) {
			forma.setDescrFormaDoc(getDescricao());
			forma.setSigla(getSigla());
			forma.setExTipoFormaDoc(dao().consultar(getIdTipoFormaDoc(), ExTipoFormaDoc.class, false));
			
			if(forma.getExTipoDocumentoSet() == null)
				forma.setExTipoDocumentoSet(new TreeSet<ExTipoDocumento>());
			
			if(isOrigemInternoProduzido())
				forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));

			if(isOrigemInternoImportado())
				forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_ANTIGO, ExTipoDocumento.class, false));
			
			if(isOrigemExterno())
				forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO, ExTipoDocumento.class, false));
		}
	}

	private void escreverForm() throws AplicacaoException,
			UnsupportedEncodingException {
		descricao = forma.getDescrFormaDoc();
		sigla = forma.getSigla();
		idTipoFormaDoc =  forma.getExTipoFormaDoc() != null ? forma.getExTipoFormaDoc()
				.getIdTipoFormaDoc() : null;
				
		setOrigemExterno(false);
		setOrigemInternoImportado(false);
		setOrigemInternoProduzido(false);

		if(forma.getExTipoDocumentoSet() != null) {
			for (ExTipoDocumento origem : forma.getExTipoDocumentoSet()) {
				if(origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO)
					setOrigemExterno(true);
				
				if(origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO)
					setOrigemInternoProduzido(true);
				
				if(origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_ANTIGO)
					setOrigemInternoImportado(true);
			}
		}
	}

	public String aEditarGravar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		lerForm();
		Ex.getInstance().getBL().gravarForma(forma);;
		setId(forma.getIdFormaDoc());
		Map<String, String[]> l = getPar();
		if ("Aplicar".equals(param("submit")))
			return "aplicar";
		return Action.SUCCESS;
	}
	
	public String aVerificarSigla() throws Exception {
		ExFormaDocumento formaConsulta = new ExFormaDocumento();
		
		if(getSigla() != null && !getSigla().isEmpty()) {
		
			if (!formaConsulta.isSiglaValida(getSigla()))   {
				setMensagem("Sigla inválida. A sigla deve ser formada por 3 letras.");
			}
			
			formaConsulta.setSigla(getSigla());
			formaConsulta = dao().consultarPorSigla(formaConsulta);
			
			if(formaConsulta != null)
				setMensagem("Esta sigla já está sendo utilizada");
		}
		
		return Action.SUCCESS;
	}
	
	@Override
	public DaoFiltroSelecionavel createDaoFiltro() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoModelo() {
		return tipoModelo;
	}

	public void setTipoModelo(String tipoModelo) {
		this.tipoModelo = tipoModelo;
	}

	public Integer getPostback() {
		return postback;
	}

	public void setPostback(Integer postback) {
		this.postback = postback;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public void setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public void setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public void setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	public DpCargoSelecao getCargoSel() {
		return cargoSel;
	}

	public void setCargoSel(DpCargoSelecao cargoSel) {
		this.cargoSel = cargoSel;
	}

	public DpFuncaoConfiancaSelecao getFuncaoSel() {
		return funcaoSel;
	}

	public void setFuncaoSel(DpFuncaoConfiancaSelecao funcaoSel) {
		this.funcaoSel = funcaoSel;
	}

	public Long getIdTipoFormaDoc() {
		return idTipoFormaDoc;
	}

	public void setIdTipoFormaDoc(Long idTipoFormaDoc) {
		this.idTipoFormaDoc = idTipoFormaDoc;
	}

	public boolean isOrigemExterno() {
		return origemExterno;
	}

	public void setOrigemExterno(boolean origemExterno) {
		this.origemExterno = origemExterno;
	}

	public boolean isOrigemInternoProduzido() {
		return origemInternoProduzido;
	}

	public void setOrigemInternoProduzido(boolean origemInternoProduzido) {
		this.origemInternoProduzido = origemInternoProduzido;
	}

	public boolean isOrigemInternoImportado() {
		return origemInternoImportado;
	}

	public void setOrigemInternoImportado(boolean origemInternoImportado) {
		this.origemInternoImportado = origemInternoImportado;
	}

	public ExFormaDocumento getForma() {
		return forma;
	}

	public void setForma(ExFormaDocumento forma) {
		this.forma = forma;
	}

	public String getSigla() {
		return sigla.toUpperCase();
	}

	public void setSigla(String sigla) {
		this.sigla = sigla.toUpperCase();
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}