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
 * Criado em  13/03/2020
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

public class UsuarioEmailAction {

	private String emailAtual;

	private String emailConfirma;

	private String emailNovo;

	private String titulo;

	private String tit;

	private String ajaxMsgErro;

	private String teste;

	private boolean trocarTodos;

	public String getEmailAtual() {
		return emailAtual;
	}

	public void setEmailAtual(String emailAtual) {
		this.emailAtual = emailAtual;
	}

	public String getEmailConfirma() {
		return emailConfirma;
	}

	public void setEmailConfirma(String emailConfirma) {
		this.emailConfirma = emailConfirma;
	}

	public String getEmailNovo() {
		return emailNovo;
	}

	public void setEmailNovo(String emailNovo) {
		this.emailNovo = emailNovo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTit() {
		return tit;
	}

	public void setTit(String tit) {
		this.tit = tit;
	}

	public String getAjaxMsgErro() {
		return ajaxMsgErro;
	}

	public void setAjaxMsgErro(String ajaxMsgErro) {
		this.ajaxMsgErro = ajaxMsgErro;
	}

	public boolean isTrocarTodos() {
		return trocarTodos;
	}

	public void setTrocarTodos(boolean trocarTodos) {
		this.trocarTodos = trocarTodos;
	}

	public String getTeste() {
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

}
