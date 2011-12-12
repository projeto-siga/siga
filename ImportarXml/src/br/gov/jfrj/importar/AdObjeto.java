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
package br.gov.jfrj.importar;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.DesconsiderarParaSemelhanca;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

public abstract class AdObjeto extends SincronizavelSuporte implements
		Comparable {

	private String nome;
	private String dnDominio;
	
	@DesconsiderarParaSemelhanca
	private AdUnidadeOrganizacional grPai;

	@Desconsiderar
	private List<AdGrupo> membroDe = new ArrayList<AdGrupo>();

	/**
	 * Mantém as referências para os grupos do qual o objeto é membro
	 * @param g
	 */
	protected void addMembroDe(AdGrupo g){
		membroDe.add(g);
	}
	
	/**
	 * Remove a referência para o grupos do qual o objeto é membro
	 * @param g
	 */
	protected void delMembroDe(AdGrupo g){
		membroDe.remove(g);
	}
	
	/**
	 * Retorna os grupos ao qual o objeto pertence
	 * @return
	 */
	public List<AdGrupo> getGruposPertencentes(){
		return membroDe;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public void setIdExterna(String idExterna) {
		super.setIdExterna(idExterna);
	}

	public String getNomeCompleto() {
		return getPrefixo()
				+ getNome()
				+ ","
				+ (getGrupoPai() == null ? dnDominio : getGrupoPai()
						.getNomeCompleto());
	}

	public String getPrefixo() {
		return "CN=";
	}

	public AdObjeto(String nome, String idExterna, String dnDominio) {
		this.setNome(nome);
		this.setIdExterna(idExterna);
		this.setDnDominio(dnDominio);
	}

	public AdObjeto(String nome, String idExterna, AdUnidadeOrganizacional grPai, String dnDominio) {
		this(nome, idExterna,dnDominio);
		this.grPai = grPai;
		// grPai.acrescentarMembro(this);
	}

	public AdUnidadeOrganizacional getGrupoPai() {
		return grPai;
	}

	public AdUnidadeOrganizacional setGrupoPai(AdUnidadeOrganizacional g) {
		return grPai = g;
	}

	@Override
	public int compareTo(Object o) {
		AdObjeto ad = ((AdObjeto) o);
		return getNomeCompleto().compareTo(ad.getNomeCompleto());
	}

	public void setDnDominio(String dnDominio) {
		this.dnDominio = dnDominio;
	}

	public String getDnDominio() {
		return dnDominio;
	}
}
