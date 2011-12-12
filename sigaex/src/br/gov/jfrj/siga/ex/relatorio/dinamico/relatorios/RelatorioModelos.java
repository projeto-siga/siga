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
package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelatorioModelos extends RelatorioTemplate{


	public RelatorioModelos(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	public AbstractRelatorioBaseBuilder configurarRelatorio() throws DJBuilderException {
		this.setTitle("Relatório de Modelos");
		this.addColuna("Forma", 20,RelatorioRapido.ESQUERDA,false);
		this.addColuna("Modelo", 50,RelatorioRapido.ESQUERDA,false);
		this.addColuna("Class Documental", 15,RelatorioRapido.CENTRO,false);
		this.addColuna("Class Criação", 15,RelatorioRapido.CENTRO,false);
		return this;
	}

	@Override
	public byte[] getRelatorioPDF() throws JRException {
//		JasperViewer.viewReport(relatorio.getRelatorio());
		return JasperExportManager.exportReportToPdf(relatorio.getRelatorioJasperPrint());
	}

	@Override
	public Collection processarDados() {
		
		List<String> d = new LinkedList<String>();
		Query q = HibernateUtil.getSessao().createQuery("from ExModelo order by exFormaDocumento.descrFormaDoc");
		
		for (Iterator<ExModelo> iterator = q.list().iterator(); iterator.hasNext();) {
			ExModelo modelo = (ExModelo) iterator.next();
			if (modelo.getExFormaDocumento() != null){
				d.add(modelo.getExFormaDocumento().getDescrFormaDoc());
			} else{
				d.add("");
			}
			if (modelo.getNmMod() != null){
				d.add(modelo.getNmMod());
			} else{
				d.add("");
			}
			
			if (modelo.getExClassificacao() != null){
				d.add(modelo.getExClassificacao().getSigla());
			}else{
				d.add("");
			}
			if (modelo.getExClassCriacaoVia()!= null){
				d.add(modelo.getExClassCriacaoVia().getSigla());
			}else{
				d.add("");
			}
		}

		return d;
	}

}
