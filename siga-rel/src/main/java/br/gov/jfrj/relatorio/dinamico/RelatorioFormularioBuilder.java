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
package br.gov.jfrj.relatorio.dinamico;

import java.util.Collection;
import java.util.Map;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

/**
 * Classe de Exemplo para criação de um Relatório Builder.
 * 
 * Relatório personalizado de formulário do SIGA-EX criado por herança
 * do AbstractRelatorioBaseBuilder. 
 */
public class RelatorioFormularioBuilder extends AbstractRelatorioBaseBuilder {

	int tamanhoBase=25;
	
	public RelatorioFormularioBuilder(Map parametros) throws DJBuilderException{
		super(parametros);
		
		Style estiloColunaCentro = copiarEstilo(estiloColuna);
		estiloColunaCentro.setHorizontalAlign(HorizontalAlign.CENTER);

		Style estiloTituloColunaCentro = copiarEstilo(estiloTituloColuna);
		estiloTituloColunaCentro.setHorizontalAlign(HorizontalAlign.CENTER);
		
		try {
		
			this.setTitle("SIGA-DOC - Relação de Formulários");
			
			AbstractColumn colForma = ColumnBuilder.getInstance()
				.setTitle("Forma")
				.setWidth(tamanhoBase * 4)
				.setColumnProperty("exFormaDocumento.descrFormaDoc", String.class.getName())
			.build();

			AbstractColumn colModelo = ColumnBuilder.getInstance()
				.setTitle("Modelo")
				.setWidth(tamanhoBase * 12)
				.setColumnProperty("nmMod", String.class.getName())
			.build();
			
			AbstractColumn colClasse = ColumnBuilder.getInstance()
				.setTitle("Classe")
				.setWidth(tamanhoBase * 2)
				.setColumnProperty("exClassificacao.codAssuntoPrincipal", Byte.class.getName())
				.setStyle(estiloColunaCentro)
				.setHeaderStyle(estiloTituloColunaCentro)
			.build();
			
			AbstractColumn colClasseDasVias = ColumnBuilder.getInstance()
				.setTitle("Classe das Vias")
				.setWidth(tamanhoBase * 4)
				.setColumnProperty("exClassificacao.codAssuntoSecundario", Byte.class.getName())
				.setStyle(estiloColunaCentro)
				.setHeaderStyle(estiloTituloColunaCentro)
			.build();


			this.addColumn(colForma);
			this.addColumn(colModelo);
			this.addColumn(colClasse);
			this.addColumn(colClasseDasVias);
		} catch (ColumnBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setDados(Collection dados) {
		
	}

}
