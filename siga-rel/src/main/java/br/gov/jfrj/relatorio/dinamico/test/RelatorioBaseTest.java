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
package br.gov.jfrj.relatorio.dinamico.test;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.view.JasperViewer;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;

public class RelatorioBaseTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//HibernateUtil.configurarHibernate("/br/gov/jfrj/siga/hibernate/hibernate.cfg.xml");
		
		RelatorioRapido rel = new RelatorioRapido(null);
		rel.setTitle("Relatório de Modelos");		
		rel.addColuna("Forma", 20,RelatorioRapido.ESQUERDA,false);
		rel.addColuna("Modelo", 50,RelatorioRapido.ESQUERDA,false);
		rel.addColuna("Class Documental", 15,RelatorioRapido.CENTRO,false);
		rel.addColuna("Class Criação", 15,RelatorioRapido.CENTRO,false,true);
		
		List<String> dados = new ArrayList<String>();
		for (int i=0; i<100; i++){
			dados.add("Forma_aaaaa" + i);
			dados.add("Modelo_bbbbb" + i);
			dados.add("Class Documental_ccccc"+ i);
			dados.add("Class Criação_ddddd"+ i);
			dados.add("siga/sigaex/app/expediente/exibir?sigla=\"000000 " + i + "-A\"");
		}
		dados.add("Forma_aaaaa" + 1);
		dados.add("Modelo_bbbbb" + 1);
		dados.add("Class Documental_ccccc"+ 1);
		dados.add("Class Criação_ddddd"+ 1);
		dados.add("siga/sigaex/app/expediente/exibir?sigla=\"000000 " + 1 + "-A\"");

		rel.setDados(dados);
		
		
		JasperViewer.viewReport(rel.getRelatorioJasperPrint());

	}

}
