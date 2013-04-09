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
 * Created Mon Nov 14 13:36:35 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_CLASSIFICACAO' table. This class may
 * be customized as it is never re-generated after being created.
 */
public class ExClassificacao extends AbstractExClassificacao implements
		Serializable, Selecionavel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5783951238385826556L;

	private String descrClassificacaoAI;

	private String descrAssuntoPrincipalAI;

	private String descrAssuntoSecundarioAI;

	private String descrAssuntoAI;

	private String descrClasseAI;

	private String descrSubclasseAI;

	private String descrAssuntoPrincipal;

	private String descrAssuntoSecundario;

	private String descrAssunto;

	private String descrClasse;

	private String descrSubclasse;

	/**
	 * Simple constructor of ExClassificacao instances.
	 */
	public ExClassificacao() {
	}


	public Long getId() {
		return getIdClassificacao();
	}

	private String Zeros(final long l, final int i) {
		String s = Long.toString(l);
		while (s.length() < i)
			s = "0" + s;
		return s;
	}

	/**
	 * Verifica se uma classificação é do tipo intermediária.
	 * 
	 * @return Verdadeiro caso a classificação seja do tipo intermediária e Falso caso a classificação não seja do tipo intermediária.
	 * 
	 */
	public boolean isIntermediaria() throws AplicacaoException {
		if (getExViaSet() == null) {
			return true;
		}
		if (getExViaSet() != null && getExViaSet().size() == 1) {
			ExVia via = (ExVia) (getExViaSet().toArray()[0]);
			if (via.getCodVia() == null) {
				return true;
			}
		}
		if (getExViaSet() != null && getExViaSet().size() == 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * Retorna a sigla de uma classificação.
	 * 
	 */
	public String getSigla() {
		return getCodificacao();
	}

	/**
	 * Informa a sigla de uma classificação.
	 * 
	 * @param sigla
	 * 
	 */
	public void setSigla(final String sigla) {
		setCodificacao(sigla);
	}

	/**
	 * Retorna a descrição de uma classificação. A descrição de uma classificação
	 * é formada pela descrição do Assunto, da classe e da subclasse.
	 * 
	 * @param sigla
	 * 
	 */
	public String getDescricao() {
		String descr = "";
//		if (getCodAssunto() != null) {
//			if (getDescrAssunto() != null)
//				descr += getDescrAssunto();
//			if (getDescrClasse() != null)
//				descr += ": " + getDescrClasse();
//			if (getDescrSubclasse() != null)
//				descr += ": " + getDescrSubclasse();
//			if (getDescrClassificacao() != null)
//				descr += ": " + getDescrClassificacao();
//		} else {
//			if (getDescrAssuntoPrincipal() != null)
//				descr += getDescrAssuntoPrincipal();
//			if (getDescrAssuntoSecundario() != null)
//				descr += ": " + getDescrAssuntoSecundario();
//			if (getDescrClassificacao() != null)
//				descr += ": " + getDescrClassificacao();
//		}
		
		return getDescrClassificacao();
	}

	public String getDescricaoCompleta() {
		return getSigla() + " - " + getDescricao();
	}

	public String getDescrClassificacaoAI() {
		return descrClassificacaoAI;
	}

	public void setDescrClassificacaoAI(String descrClassificacaoAI) {
		this.descrClassificacaoAI = descrClassificacaoAI;
	}

	public String getNome() {
		return getDescrClassificacao();
	}

	public String getDescrAssuntoPrincipal() {
		return descrAssuntoPrincipal;
	}

	public String getDescrAssuntoSecundario() {
		return descrAssuntoSecundario;
	}

	public void setDescrAssuntoPrincipal(String descrAssuntoPrincipal) {
		this.descrAssuntoPrincipal = descrAssuntoPrincipal;
	}

	public void setDescrAssuntoSecundario(String descrAssuntoSecundario) {
		this.descrAssuntoSecundario = descrAssuntoSecundario;
	}

	public Integer getNumVias() {
		return getExViaSet().size();
	}

	public String getDestinacoesFinais() {
		return "";
	}

	public String getDescrAssuntoPrincipalAI() {
		return descrAssuntoPrincipalAI;
	}

	public void setDescrAssuntoPrincipalAI(String descrAssuntoPrincipalAI) {
		this.descrAssuntoPrincipalAI = descrAssuntoPrincipalAI;
	}

	public String getDescrAssuntoSecundarioAI() {
		return descrAssuntoSecundarioAI;
	}

	public void setDescrAssuntoSecundarioAI(String descrAssuntoSecundarioAI) {
		this.descrAssuntoSecundarioAI = descrAssuntoSecundarioAI;
	}

	public String getDescrAssuntoAI() {
		return descrAssuntoAI;
	}

	public void setDescrAssuntoAI(String descrAssuntoAI) {
		this.descrAssuntoAI = descrAssuntoAI;
	}

	public String getDescrClasseAI() {
		return descrClasseAI;
	}

	public void setDescrClasseAI(String descrClasseAI) {
		this.descrClasseAI = descrClasseAI;
	}

	public String getDescrSubclasseAI() {
		return descrSubclasseAI;
	}

	public void setDescrSubclasseAI(String descrSubclasseAI) {
		this.descrSubclasseAI = descrSubclasseAI;
	}

	public String getDescrAssunto() {
		return descrAssunto;
	}

	public void setDescrAssunto(String descrAssunto) {
		this.descrAssunto = descrAssunto;
	}

	public String getDescrClasse() {
		return descrClasse;
	}

	public void setDescrClasse(String descrClasse) {
		this.descrClasse = descrClasse;
	}

	public String getDescrSubclasse() {
		return descrSubclasse;
	}

	public void setDescrSubclasse(String descrSubclasse) {
		this.descrSubclasse = descrSubclasse;
	}

	/* Add customized code below */
	
	/**
	 * Verifica se uma classificação está fechada.
	 * 
	 * @return Verdadeiro se a classificação está fechado e falso caso contrário.
	 * 
	*/
	public boolean isFechada() {
		if(this.getHisDtFim() != null)
			return true;
		
		return false;
	}

	public void setId(Long id) {
		setIdClassificacao(id);
		
	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}
}
