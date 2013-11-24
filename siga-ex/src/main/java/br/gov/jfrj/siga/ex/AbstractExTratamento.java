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
package br.gov.jfrj.siga.ex;

import br.gov.jfrj.siga.model.Objeto;

public class AbstractExTratamento extends Objeto {
	Integer idTratamento;
	String autoridade;
	String formaDeTratamento;
	String abreviatura;
	String vocativo;
	String formaDeEnderecamento;
	String genero;
	
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getAutoridade() {
		return autoridade;
	}
	public void setAutoridade(String autoridade) {
		this.autoridade = autoridade;
	}
	public String getFormaDeEnderecamento() {
		return formaDeEnderecamento;
	}
	public void setFormaDeEnderecamento(String formaDeEnderecamento) {
		this.formaDeEnderecamento = formaDeEnderecamento;
	}
	public String getFormaDeTratamento() {
		return formaDeTratamento;
	}
	public void setFormaDeTratamento(String formaDeTratamento) {
		this.formaDeTratamento = formaDeTratamento;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public Integer getIdTratamento() {
		return idTratamento;
	}
	public void setIdTratamento(Integer idTratamento) {
		this.idTratamento = idTratamento;
	}
	public String getVocativo() {
		return vocativo;
	}
	public void setVocativo(String vocativo) {
		this.vocativo = vocativo;
	}
}
