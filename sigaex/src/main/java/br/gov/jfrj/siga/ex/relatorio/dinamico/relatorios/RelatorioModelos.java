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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class RelatorioModelos extends RelatorioTemplate {

	public RelatorioModelos(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório de Modelos");
		this.addColuna("Forma", 27, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Modelo", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Class Documental", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Class Criação", 15, RelatorioRapido.CENTRO, false);
		return this;
	}

	@Override
	public Collection processarDados() {

		List<String> d = new LinkedList<String>();
		Query q = ContextoPersistencia.em().createQuery(
				"from ExModelo order by exFormaDocumento.descrFormaDoc");

		for (ExModelo modelo : (List<ExModelo>) q.getResultList()) {
			if (modelo.getExFormaDocumento() != null) {
				d.add(modelo.getExFormaDocumento().getDescrFormaDoc());
			} else {
				d.add("");
			}
			if (modelo.getNmMod() != null) {
				d.add(modelo.getNmMod());
			} else {
				d.add("");
			}

			if (modelo.getExClassificacao() != null) {
				d.add(modelo.getExClassificacao().getSigla());
			} else {
				d.add("");
			}
			if (modelo.getExClassCriacaoVia() != null) {
				d.add(modelo.getExClassCriacaoVia().getSigla());
			} else {
				d.add("");
			}
		}
		return d;
	}

}