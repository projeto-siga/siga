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

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;

public class ExTratamento extends AbstractExTratamento {
	
	ExTratamento(String autoridade, String genero, String formaDeTratamento, String abreviatura, 
			String vocativo, String formaDeEnderecamento) {
		this.autoridade = autoridade;
		this.genero = genero;
		this.formaDeTratamento = formaDeTratamento;
		this.abreviatura = abreviatura;
		this.vocativo = vocativo;
		this.formaDeEnderecamento = formaDeEnderecamento;
	}
	
	public static List<ExTratamento> todosTratamentos() {
		ArrayList<ExTratamento> l = new ArrayList<ExTratamento>();
		
		l.add(new ExTratamento("Presidente da República", "F", "Vossa Excelência", "V. Exª.", "Excelentíssima Senhora Presidenta da República", "Excelentíssima Senhora"));
		l.add(new ExTratamento("Presidente da República", "M", "Vossa Excelência", "V. Exª.", "Excelentíssimo Senhor Presidente da República", "Excelentíssimo Senhor"));
		l.add(new ExTratamento("Vice-Presidente da República", "F", "Vossa Excelência", "V. Exª." , "Excelêntissima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Vice-Presidente da República", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Chefe de Gabinete Civil", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Chefe de Gabinete Civil", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Chefe de Gabinete Militar da Presidência da República", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Chefe de Gabinete Militar da Presidência da República", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Consultor-Geral da República", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Consultor-Geral da República", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Corregedor do Tribunal Regional Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Corregedora do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Corregedor do Tribunal Regional Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Corregedor do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Ministro de Estado", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Ministro de Estado", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Consultor-Geral da República", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Consultor-Geral da República", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Oficial General das Forças Armadas", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Membro do Congresso Nacional", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª. Deputada"));
		l.add(new ExTratamento("Membro do Congresso Nacional", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr. Deputado"));
		l.add(new ExTratamento("Presidente do Supremo Tribunal Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Supremo Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Presidente do Supremo Tribunal Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Supremo Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Membro do Supremo Tribunal Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Membro do Supremo Tribunal", "Excelentíssima Senhora Juiza "));
		l.add(new ExTratamento("Membro do Supremo Tribunal Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Membro do Supremo Tribunal", "Excelentíssimo Senhor Juiz "));
		l.add(new ExTratamento("Presidente do Tribunal Superior", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Presidente do Tribunal Superior", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Membro do Tribunal Superior", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Membro do Tribunal Superior", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Presidente do Tribunal de Contas da União", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Presidente do Tribunal de Contas da União", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Membro do Tribunal de Contas da União", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Membro do Tribunal de Contas da União", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Presidente do Tribunal Regional Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Desembargadora Federal"));
		l.add(new ExTratamento("Presidente do Tribunal Regional Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Desembargador Federal"));
		l.add(new ExTratamento("Membro do Tribunal Regional Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Desembargadora Federal"));
		l.add(new ExTratamento("Membro do Tribunal Regional Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Desembargador Federal"));
		l.add(new ExTratamento("Presidente do Tribunal de Justiça", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Presidente do Tribunal de Justiça", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Membro do Tribunal de Justiça", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora Presidente do Tribunal", "Excelentíssima Senhora Juiza"));
		l.add(new ExTratamento("Membro do Tribunal de Justiça", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor Presidente do Tribunal", "Excelentíssimo Senhor Juiz"));
		l.add(new ExTratamento("Juiz Federal", "F", "Vossa Excelência", "V. Exª." , "Senhora Juiza", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Juiz Federal", "M", "Vossa Excelência", "V. Exª." , "Senhor Juiz", "Exmº. Sr. Dr."));
		l.add(new ExTratamento("Juiz em geral", "F", "Vossa Excelência", "V. Exª." , "Senhora Juiza", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Juiz em geral", "M", "Vossa Excelência", "V. Exª." , "Senhor Juiz", "Exmº. Srº. Drº."));
		l.add(new ExTratamento("Auditor da Justiça Militar", "F", "Vossa Excelência", "V. Exª." , "Senhora Juiza", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Auditor da Justiça Militar", "M", "Vossa Excelência", "V. Exª." , "Senhor Juiz", "Exmº. Srº. Drº."));
		l.add(new ExTratamento("Procurador-Geral da República", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Procurador-Geral da República", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Srº. Drº."));
		l.add(new ExTratamento("Procurador-Geral junto ao Tribunal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Procurador-Geral junto ao Tribunal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Srº. Drº."));
		l.add(new ExTratamento("Embaixador", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª. Drª."));
		l.add(new ExTratamento("Embaixador", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Srº. Drº."));
		l.add(new ExTratamento("Governador de Estado e do Distrito Federal", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Governador de Estado e do Distrito Federal", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Presidente da Assembléia Legislativa", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Presidente da Assembléia Legislativa", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Membro da Assembléia Legislativa", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Membro da Assembléia Legislativa", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Secretário de Estado do Governo Estadual", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Secretário de Estado do Governo Estadual", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Prefeito", "F", "Vossa Excelência", "V. Exª." , "Excelentíssima Senhora", "Exmª. Srª."));
		l.add(new ExTratamento("Prefeito", "M", "Vossa Excelência", "V. Exª." , "Excelentíssimo Senhor", "Exmº. Sr."));
		l.add(new ExTratamento("Reitor de Universidade", "F", "Vossa Magnificência", "V. Magª" , "Magnífico Reitor", "Exmª. Srª."));
		l.add(new ExTratamento("Reitor de Universidade", "M", "Vossa Magnificência", "V. Magª" , "Magnífico Reitor", "Exmº. Sr."));
		l.add(new ExTratamento("Cardeal", "M", "Vossa Eminência ou Vossa Eminência Reverendíssima", "V. Emª.ou V. Emª. Revmª" , "Eminentíssimo Senhor", "Eminentíssimo Senhor D."));
		l.add(new ExTratamento("Bispo e Arcebispo", "M", "Vossa Excelência Reverendíssima", "V. Exª. Revmª" , "Reverendíssimo Senhor", "Reverendíssimo Senhor D."));
		l.add(new ExTratamento("Monsenhor, Cônego", "M", "Vossa Senhoria Reverendíssima", "V. Sª. Revmª" , "Reverendíssimo Senhor", "Reverendíssimo SenhorPadre "));
		l.add(new ExTratamento("Dirigente administrativo e Procurador", "F", "Vossa Senhoria", "V. Sª." , "Senhora", "Srª."));
		l.add(new ExTratamento("Dirigente administrativo e Procurador", "M", "Vossa Senhoria", "V. Sª." , "Senhor", "Sr."));
		l.add(new ExTratamento("[Outros]", "F", "Vossa Senhoria", "V. Sª." , "Prezada Senhora", "Srª."));
		l.add(new ExTratamento("[Outros]", "M", "Vossa Senhoria", "V. Sª." , "Prezado Senhor", "Sr."));
		
		return l;
	}
	public static String genero(String autoridade){
		
		List <ExTratamento> todosTratamentos=ExTratamento.todosTratamentos(); 
		for (ExTratamento trat : todosTratamentos){
			if(trat.getAutoridade().equals(autoridade) && trat.getGenero().equals("F"))	
				return trat.getGenero();
		
			if(trat.getAutoridade().equals(autoridade) && trat.getGenero().equals("M"))	
				return trat.getGenero();
		}	
		 
		return null;
		
	}
	
	public static ExTratamento tratamento(String autoridade, String genero){
		
		List <ExTratamento> todosTratamentos=ExTratamento.todosTratamentos(); 
		
		for (ExTratamento trat : todosTratamentos) {
			if((trat.getAutoridade().equals(autoridade)) && (trat.getGenero().equals(genero)))
				return trat;

		}
		return null;
	}
}
