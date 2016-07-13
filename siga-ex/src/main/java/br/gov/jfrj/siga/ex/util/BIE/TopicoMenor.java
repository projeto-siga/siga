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
package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TopicoMenor extends Topico {
	
	private Set<Materia> materias;
	
	public TopicoMenor(){
		this(null, null, null, null, null);
	}
	
	public TopicoMenor(TipoMateria... tipoMateria){
		this(null, null, null, Arrays.asList(tipoMateria), null);
	}
	
	public TopicoMenor(Localidade... localidade){
		this(null, null, Arrays.asList(localidade), null, null);
	}
	
	public TopicoMenor(Ordenacao ordenacao, TipoMateria... tiposMateria){
		this(null, ordenacao, null, Arrays.asList(tiposMateria), null);
	}
	
	public TopicoMenor(Ordenacao ordenacao, Localidade... localidades){
		this(null, ordenacao, Arrays.asList(localidades), null, null);
	}
	
	public TopicoMenor(String descr, Ordenacao ordenacao, List<Localidade> localidades, List<TipoMateria> tiposMateria, List<Unidade> unidades) {
		super(descr, localidades, tiposMateria, unidades);
		if (ordenacao == null)
			ordenacao = Ordenacao.DATA;
		materias = new TreeSet<Materia>(ordenacao.getComparator());
	}
	
	public boolean isVazio() {
		return (materias.size() == 0);
	}

	public Set<Materia> getMaterias() {
		return materias;
	}

	public void addMateria(Materia materia){
		materias.add(materia);
	}
	
	public boolean alocar(Materia m){
		if (!podeAlocar(m))
			return false;
		getMaterias().add(m);
		return true;
	}

};
