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
package br.gov.jfrj.ldap;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.DesconsiderarParaSemelhanca;

public class AdUsuario extends AdObjeto {

	private String sigla;
	@DesconsiderarParaSemelhanca
	private List<String> listaEmails = new ArrayList<String>();
	private String senhaCripto;
	private String nomeExibicao;

	@DesconsiderarParaSemelhanca
	private String chaveCripto;
	
	private String homeMDB;
	private String templateLink;
	
	/**
	 * Nome utilizado para resolução do email.
	 */
	@Desconsiderar
	private String nomeResolucaoEmail;

	public AdUsuario(String nome, String idExterna, String dnDominio) {
		super(nome, idExterna, dnDominio);
	}

	public AdUsuario(String nome, String idExterna,
			AdUnidadeOrganizacional grPai, String dnDominio) {
		super(nome, idExterna, grPai, dnDominio);
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSenhaCripto(String senhaCripto) {
		this.senhaCripto = senhaCripto;
	}

	public String getSenhaCripto() {
		return senhaCripto;
	}

	public void setChaveCripto(String chaveCripto) {
		this.chaveCripto = chaveCripto;
	}

	public String getChaveCripto() {
		return chaveCripto;
	}

	/**
	 * Define o nome de exibição (apelido ou nome com pronome de tratamento, por
	 * exemplo). Esse valor é o DisplayName no AD.
	 * 
	 * @param nomeExibicao
	 */
	public void setNomeExibicao(String nomeExibicao) {
		this.nomeExibicao = nomeExibicao;
	}

	/**
	 * Retorna o nome de exibição (apelido ou nome com pronome de tratamento,
	 * por exemplo).Esse valor é o DisplayName no AD.
	 * 
	 * @return
	 */
	public String getNomeExibicao() {
		return nomeExibicao;
	}
	
	public void addEmail(String email){
		this.listaEmails.add(email);
	}

	public void removerEmail(String email){
		this.listaEmails.remove(email);
	}

	public List<String> getEmails() {
		return listaEmails;
	}

	public void setNomeResolucaoEmail(String nome) {
		this.nomeResolucaoEmail = nome;
	}

	public String getNomeResolucaoEmail() {
		return nomeResolucaoEmail;
	}

	public void setHomeMDB(String homeMDB) {
		this.homeMDB = homeMDB;
	}

	public String getHomeMDB() {
		return homeMDB;
	}

	public void setTemplateLink(String templateLink) {
		this.templateLink = templateLink;
	}

	public String getTemplateLink() {
		return templateLink;
	}

}
