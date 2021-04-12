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

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.jfrj.siga.base.util.Texto;

@XmlRootElement(name="topico")
@XmlAccessorType (XmlAccessType.FIELD)
public class Topico extends ListaDeTopicos{
	
	@XmlAttribute(name="descr")
	private String descr;
	
	@XmlAttribute(name="localidade")
	private String localidadeDasMaterias;
	
	@XmlAttribute(name="tipoMateria")
	private String tipoDasMaterias;
	
	private Set<Materia> materias;
	
	@XmlTransient
	private Comparator<Materia> ordenacao = new Comparator<Materia>(){
		public int compare(Materia o1, Materia o2) {
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
	};

	public String getDescr() {
		if (descr != null)
			return descr;
		if (getLocalidadeDasMaterias() != null)
			return getLocalidadeDasMaterias().toUpperCase();
		if (getTipoDasMaterias() != null)
			return Texto.pluralizar(getTipoDasMaterias().split("\\|")[1]).toUpperCase();
		return "";
	}

	public Topico setDescr(String descr) {
		this.descr = descr;
		return this;
	}
	
	public Set<Materia> getMaterias() {
		return materias;
	}

	public void addMateria(Materia materia){
		if (materias == null)
			materias = new TreeSet<Materia>(ordenacao);
		materias.add(materia);
	}

	public boolean isVazio() {
		if (!super.isVazio())
			return false;
		return (materias ==null || materias.size() == 0);
	}
	
	public boolean alocar(Materia m) {
		if (super.alocar(m))
			return true;
		
		if (getLocalidadeDasMaterias() != null && !m.getLocalidade().equalsIgnoreCase(getLocalidadeDasMaterias()))
			return false;
		if (getTipoDasMaterias() != null){
			if (!m.getTipoMateria().split("\\|")[0].equals(getTipoDasMaterias().split("\\|")[0]))
				return false;
			//Edson: abaixo, atualiza o tipo de matéria com o valor completo, pois pode ter sido
			//definido na variável xmlHierarquia do BIE apenas com a sigla em vez de sigla|nome
			setTipoDasMaterias(m.getTipoMateria());
		}
		addMateria(m);
		return true;
	}

	public String getTipoDasMaterias() {
		return tipoDasMaterias;
	}

	public Topico setTipoDasMaterias(String tipoMateria) {
		this.tipoDasMaterias = tipoMateria;
		return this;
	}

	public String getLocalidadeDasMaterias() {
		return localidadeDasMaterias;
	}

	public Topico setLocalidadeDasMaterias(String localidade) {
		this.localidadeDasMaterias = localidade;
		return this;
	}
	
};
