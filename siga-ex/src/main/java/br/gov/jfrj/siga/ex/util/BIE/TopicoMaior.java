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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopicoMaior extends Topico {
	
	public TopicoMaior() {
		this(null, null, null, null);
	}
	
	public TopicoMaior(String descr) {
		this(descr, null, null, null);
	}
	
	
	public TopicoMaior(Localidade... localidade){
		this(null, localidade);
	}
	
	public TopicoMaior(Unidade... unidade){
		this(null, unidade);
	}
	
	public TopicoMaior(TipoMateria... tipoMateria){
		this(null, tipoMateria);
	}
	public TopicoMaior(String descr, Localidade... localidade) {
		this(descr, Arrays.asList(localidade), null, null);
	}
	public TopicoMaior(String descr, Unidade... unidade) {
		this(descr, null, null, Arrays.asList(unidade));
	}
	
	public TopicoMaior(String descr, TipoMateria... tipoMateria) {
		this(descr, null, Arrays.asList(tipoMateria), null);
	}
	
	public TopicoMaior(String descr, List<Localidade> localidades, List<TipoMateria> tiposMateria, List<Unidade> unidades) {
		super(descr, localidades, tiposMateria, unidades);
	}
	
	public List<TopicoMenor> subTopicos = new ArrayList<>();

	public boolean isVazio() {
		for (TopicoMenor subtopico : subTopicos)
			if (!subtopico.isVazio())
				return false;
		return true;
	}

	public List<TopicoMenor> getSubTopicos() {
		return subTopicos;
	}

	public void addSubTopico(TopicoMenor subtopico) {
		subTopicos.add(subtopico);
	}
	
	public boolean alocar(Materia m) {
		if (!podeAlocar(m))
			return false;
		if (isVazio())
			addSubTopico(new TopicoMenor());
		for (TopicoMenor sub : subTopicos)
			if (sub.alocar(m))
				return true;
		return false;
	}
};
