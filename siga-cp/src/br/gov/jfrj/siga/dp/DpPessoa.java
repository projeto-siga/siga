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
 * Criado em  21/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.ColumnResult;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Table(name = "DP_PESSOA", schema = "CORPORATIVO")
@Entity
@SqlResultSetMapping(name="scalar", columns=@ColumnResult(name="dt"))
@NamedNativeQuery(name="consultarDataEHoraDoServidor", query="SELECT sysdate dt FROM dual", resultSetMapping="scalar")
public class DpPessoa extends AbstractDpPessoa implements Serializable,
		Selecionavel, Historico, Sincronizavel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5743631829922578717L;

	@Formula(value = "REMOVE_ACENTO(NOME_PESSOA)")
	@Desconsiderar
	private String nomePessoaAI;

	public DpPessoa() {

	}

	public Long getIdLotacao() {
		if (getLotacao() == null)
			return null;
		return getLotacao().getIdLotacao();
	}

	public boolean isFechada() {
		if (getDataFimPessoa() == null)
			return false;
		Set<DpPessoa> setPessoas = getPessoaInicial().getPessoasPosteriores();
		if (setPessoas != null)
			for (DpPessoa l : setPessoas)
				if (l.getDataFimPessoa() == null)
					return false;

		return true;
	}

	public Long getIdCargo() {
		if (getCargo() == null)
			return null;
		return getCargo().getIdCargo();
	}

	public Long getIdFuncaoConfianca() {
		if (getFuncaoConfianca() == null)
			return null;
		return getFuncaoConfianca().getIdFuncao();
	}

	public String iniciais(String s) {
		final StringBuilder sb = new StringBuilder(10);
		boolean f = true;

		s = s.replace(" E ", " ");
		s = s.replace(" DA ", " ");
		s = s.replace(" DE ", " ");
		s = s.replace(" DO ", " ");

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (f) {
				sb.append(c);
				f = false;
			}
			if (c == ' ') {
				f = true;
			}
		}
		return sb.toString();
	}

	public Long getId() {
		return super.getIdPessoa();
	}
	
	public void setId(Long id){
		setIdPessoa(id);
	}

	public String getSigla() {
		return getSesbPessoa() + getMatricula().toString();
	}

	public String getIniciais() {
		// return iniciais(getNomePessoa());
		return getSigla();
	}

	public String getDescricao() {
		return getNomePessoa();
	}

	public String getDescricaoIniciaisMaiusculas() {
		return Texto.maiusculasEMinusculas(getDescricao());
	}

	public void setSigla(final String sigla) {
		final Pattern p1 = Pattern.compile("^([A-Za-z][A-Za-z0-9])([0-9]+)");
		final Matcher m = p1.matcher(sigla);
		if (m.find()) {
			setSesbPessoa(m.group(1).toUpperCase());
			setMatricula(Long.parseLong(m.group(2)));
		}
	}

	public String getNomePessoaAI() {
		return nomePessoaAI;
	}

	public void setNomePessoaAI(String nomePessoaAI) {
		this.nomePessoaAI = nomePessoaAI;
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((DpPessoa) other)
				.getIdInicial().longValue();
	}

	public Long getIdInicial() {
		return getIdPessoaIni();
	}

	public String getFuncaoString() {
		if (getFuncaoConfianca() != null)
			return getFuncaoConfianca().getNomeFuncao();
		return getCargo().getNomeCargo();
	}

	public String getPadraoReferenciaInvertido() {
		if (getPadraoReferencia() != null && !getPadraoReferencia().equals("")) {
			String partes[] = getPadraoReferencia().split("-");
			String partesConcat = partes[1] + "-" + partes[0];
			if (partes.length > 2)
				partesConcat += "-" + partes[2];
			return partesConcat;
		} else
			return "";
	}

	@Override
	public String getSiglaCompleta() {
		return getSigla();
	}

	// Métodos necessários para ser "Sincronizavel"
	//
	public Date getDataFim() {
		return getDataFimPessoa();
	}

	public Date getDataInicio() {
		return getDataInicioPessoa();
	}

	public String getDescricaoExterna() {
		return getDescricao();
	}

	public String getIdExterna() {
		return getIdePessoa();
	}

	public String getLoteDeImportacao() {
		return getOrgaoUsuario().getId().toString();
	}

	public int getNivelDeDependencia() {
		return SincronizavelSuporte.getNivelDeDependencia(this);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public void setDataFim(Date dataFim) {
		setDataFimPessoa(dataFim);
	}

	public void setDataInicio(Date dataInicio) {
		setDataInicioPessoa(dataInicio);
	}

	public void setIdExterna(String idExterna) {
		setIdePessoa(idExterna);
	}

	public void setIdInicial(Long idInicial) {
		setIdPessoaIni(idInicial);
	}

	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	public boolean isBloqueada() throws AplicacaoException {
		return Cp.getInstance().getComp().isPessoaBloqueada(this);
	}

	//
	// Funções utilizadas nas fórmulas de inclusão em grupos de email.
	//

	public boolean tipoLotacaoSiglaIgual(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao()
				.toLowerCase().equals(s.toLowerCase());
	}

	public boolean tipoLotacaoSiglaContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem(this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao(),
				"", s);
	}

	public boolean tipoLotacaoDescricaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem("", this.getLotacao().getCpTipoLotacao()
				.getDscTpLotacao(), s);
	}

	public boolean tipoLotacaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		if (this.getLotacao().getCpTipoLotacao() == null)
			return false;
		return contem(this.getLotacao().getCpTipoLotacao().getSiglaTpLotacao(),
				this.getLotacao().getCpTipoLotacao().getDscTpLotacao(), s);
	}

	public boolean lotacaoSiglaIgual(String s) {
		if (this.getLotacao() == null)
			return false;
		return this.getLotacao().getSigla().toLowerCase()
				.equals(s.toLowerCase());
	}

	public boolean lotacaoSiglaContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem(this.getLotacao().getSigla(), "", s);
	}

	public boolean lotacaoDescricaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem("", this.getLotacao().getDescricao(), s);
	}

	public boolean lotacaoContem(String s) {
		if (this.getLotacao() == null)
			return false;
		return contem(this.getLotacao().getSigla(), this.getLotacao()
				.getDescricao(), s);
	}

	public boolean cargoSiglaIgual(String s) {
		if (this.getCargo() == null)
			return false;
		return this.getCargo().getSigla().toLowerCase().equals(s.toLowerCase());
	}

	public boolean cargoSiglaContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem(this.getCargo().getSigla(), "", s);
	}

	public boolean cargoDescricaoContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem("", this.getCargo().getDescricao(), s);
	}

	public boolean cargoContem(String s) {
		if (this.getCargo() == null)
			return false;
		return contem(this.getCargo().getSigla(), this.getCargo()
				.getDescricao(), s);
	}

	public boolean funcaoSiglaIgual(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return this.getFuncaoConfianca().getSigla().toLowerCase()
				.equals(s.toLowerCase());
	}

	public boolean funcaoSiglaContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem(this.getFuncaoConfianca().getSigla(), "", s);
	}

	public boolean funcaoDescricaoContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem("", this.getFuncaoConfianca().getDescricao(), s);
	}

	public boolean funcaoContem(String s) {
		if (this.getFuncaoConfianca() == null)
			return false;
		return contem(this.getFuncaoConfianca().getSigla(), this
				.getFuncaoConfianca().getDescricao(), s);
	}

	private boolean contem(String sigla, String descricao, String s) {
		s = s.toLowerCase();
		return sigla.toLowerCase().contains(s)
				|| descricao.toLowerCase().contains(s);
	}
	
	public DpPessoa getPessoaAtual () {
		DpPessoa pesIni = getPessoaInicial();
		Set<DpPessoa> setPessoas = pesIni.getPessoasPosteriores();
		if (setPessoas != null)
			for (DpPessoa p : setPessoas)
				return p;
		return this;
	}
	
	/**
	 * retorna se ativo na data
	 * 
	 * @param dt
	 *            * data quando ser saber se estava ativa
	 * @return true or false
	 */
	public boolean ativaNaData(Date dt) {
		if (dt == null)
			return this.getDataFim() == null;
		if (dt.before(this.getDataInicio()))
			return false;
		// dt >= DtIni
		if (this.getDataFim() == null)
			return true;
		if (dt.before(this.getDataFim()))
			return true;
		return false;
	}

	public String getNomeAbreviado() {
		if (getNomePessoa() == null)
			return "";
		String a[] = getNomePessoa().split(" ");

		String nomeAbreviado = "";
		for (String n : a) {
			if (nomeAbreviado.length() == 0)
				nomeAbreviado = n.substring(0, 1).toUpperCase()
						+ n.substring(1).toLowerCase();
			// else if (!"|DA|DE|DO|DAS|DOS|E|".contains("|" + n + "|"))
			// nomeAbreviado += " " + n.substring(0, 1) + ".";
		}
		return nomeAbreviado;
	}

	public Long getHisIdIni() {
		return getIdPessoaIni();
	}

	public void setHisIdIni(Long hisIdIni) {
		setIdPessoaIni(hisIdIni);
	}

	public Date getHisDtIni() {
		return getDataInicioPessoa();
	}

	public void setHisDtIni(Date hisDtIni) {
		setDataInicioPessoa(hisDtIni);
	}

	public Date getHisDtFim() {
		return getDataFimPessoa();
	}

	public void setHisDtFim(Date hisDtFim) {
		setDataFimPessoa(hisDtFim);
	}

	public int getHisAtivo() {
		return getHisDtFim() != null ? 1 : 0;
	}

	public void setHisAtivo(int hisAtivo) {
		//
	}

}
