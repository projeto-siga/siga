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

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoPropagar;

public class AdGrupo extends AdObjeto {
	public AdGrupo(String nome, String idExterna, String dnDominio) {
		super(nome, idExterna, dnDominio);
	}

	public AdGrupo(String nome, String idExterna, AdUnidadeOrganizacional grPai, String dnDominio) {
		super(nome, idExterna, grPai, dnDominio);
	}

	@NaoPropagar
	private SortedSet<AdObjeto> membros = new TreeSet<AdObjeto>();

//	public boolean contemObjetoPorNome(String nomeObjeto) {
//		return mapMembros.containsKey(nomeObjeto);
//	}
//
//	public AdObjeto objetoPorNome(String nomeObjeto) {
//		return mapMembros.get(nomeObjeto);
//	}

	public void acrescentarMembro(AdObjeto objeto) {
//		mapMembros.put(objeto.getNome(), objeto);
		membros.add(objeto);
		objeto.addMembroDe(this);
		// objeto.setGrupoPai(this);
	}

	public void removerMembros() {
		for (AdObjeto o: membros) {
			o.delMembroDe(this);
		}
//		mapMembros.clear();
		membros.clear();
	}

	public SortedSet<AdObjeto> getMembros() {
		return membros;
	}

	public boolean contemMembro(AdObjeto objeto) {
		return membros.contains(objeto);
	}

	public void removerMembro(AdObjeto objeto) {
//		mapMembros.remove(objeto.getNome());
		membros.remove(objeto);
	}
}
