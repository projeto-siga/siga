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

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;

public class Hierarquizador {

	private List<TopicoMaior> topicos;

	public Hierarquizador(ExDocumento docBIE) {
		
		//Edson: gerar os tópicos conforme a hierarquia de tópicos do órgão
		this.topicos = HierarquiaPorOrgao.valueOf(docBIE.getLotaCadastrante().getOrgaoUsuario().getAcronimoOrgaoUsu()).getEstruturaTopicos();
		
		//Edson: obter os documentos marcados na entrevista
		List<ExDocumento> docsJaNoBoletim = new ManipuladorEntrevista(docBIE).obterDocsMarcados();
		
		//Edson: transformar docs em matérias
		List<Materia> materias = new ArrayList<Materia>();
		for (ExDocumento doc : docsJaNoBoletim){
			for (TipoMateria t : TipoMateria.values()){
				for (ConversorDeExDocParaMateria conversor : t.getConversores())
					if (conversor.canHandle(doc))
						materias.addAll(conversor.converter(doc));
			}
		}
		
		//Edson: alocar matérias nos tópicos
		for (Materia m : materias){
			for (TopicoMaior t : topicos) {
				if (t.alocar(m)) {
					break;
				}
			}
		}
	}

	public List<TopicoMaior> getTopicos() {
		return topicos;
	}

}