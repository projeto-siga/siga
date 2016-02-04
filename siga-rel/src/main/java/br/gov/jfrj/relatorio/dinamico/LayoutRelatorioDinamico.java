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

import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.SimpleColumn;
/**
 * Possibilita a definição de hyperlinks nos relatórios dinâmicos.<br/ >
 * A definição do layout é feita no método getRelatorio() da classe AbstractRelatorioBaseBuilder.
 * 
 * @author kpf
 *
 */
public class LayoutRelatorioDinamico extends ClassicLayoutManager {

	@Override
	protected JRDesignTextField generateTextFieldFromColumn(AbstractColumn col,
			int height, DJGroup group) {
		
		JRDesignTextField textfield =  super.generateTextFieldFromColumn(col, height, group);
		
		if (col instanceof SimpleColumn){
			SimpleColumn sc =(SimpleColumn) col;
			if (sc.getColumnProperty().getFieldProperties().size() > 0){
				
				Object[] lista = (Object[]) sc.getColumnProperty().getFieldProperties().keySet().toArray();
				for (int i=0;i<lista.length;i++){
					String link = (String) lista[i];
					
					if (link.compareTo(IConstantes.PREFIXO_HYPERLINK_PARAMETER) > 0){
						JRDesignExpression dsnExpTooltip = new JRDesignExpression();
						JRDesignExpression dsnExpLink = new JRDesignExpression();
						dsnExpLink.setValueClassName(String.class.getName());
						dsnExpLink.addFieldChunk(link);

						textfield.setHyperlinkType(HyperlinkTypeEnum.REFERENCE);
						textfield.setHyperlinkTooltipExpression(dsnExpLink);
						textfield.setHyperlinkReferenceExpression(dsnExpLink);
					}
					
					break;
				}
			}
		}
		return textfield;
	}
	


}
