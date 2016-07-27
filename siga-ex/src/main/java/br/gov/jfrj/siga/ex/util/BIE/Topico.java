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

public abstract class Topico {
	private String descr;
	private List<String> localidades;
	private List<String> tiposMateria;
	
	public Topico(String descr, List<String> localidades, List<String> tiposMateria) {
		setDescr(descr);
		this.localidades = localidades != null ? localidades : new ArrayList<String>();
		this.tiposMateria = tiposMateria != null ? tiposMateria : new ArrayList<String>();
	}

	public String getDescr() {
		if (descr != null)
			return descr;
		if (localidades.size() == 1)
			return localidades.get(0);
		if (tiposMateria.size() == 1){
			return tiposMateria.get(0);
		}
		return null;
	}

	public Topico setDescr(String descr) {
		this.descr = descr;
		return this;
	}

	public abstract boolean alocar(Materia m);

	public List<String> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<String> localidades) {
		this.localidades = localidades;
	}

	public List<String> getTiposMateria() {
		return tiposMateria;
	}

	public void setTiposMateria(List<String> tiposMateria) {
		this.tiposMateria = tiposMateria;
	}

	/*public boolean podeAlocar(Materia m){
		if (localidades.size() > 0){
			for (Localidade l : localidades)
				if (l.equals(m.getLocalidade()))
					return true;
			return false;
		}
		if (unidades.size() > 0){
			for (Unidade u : unidades)
				if (u.equals(m.getUnidade()))
					return true;
			return false;
		}
		if (tiposMateria.size() > 0){
			for (TipoMateria t : tiposMateria)
				if (t.equals(m.getTipoMateria()))
					return true;
			return false;
		}
		return true;
	}*/

};
