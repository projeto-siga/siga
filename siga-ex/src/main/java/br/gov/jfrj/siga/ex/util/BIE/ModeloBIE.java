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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import br.gov.jfrj.siga.ex.ExDocumento;

public class ModeloBIE {

	private Raiz hierarquia;

	public ModeloBIE(ExDocumento docBIE, String xmlHierarquia)
			throws Exception {

		this.hierarquia = (Raiz) JAXBContext.newInstance(Raiz.class).createUnmarshaller()
				.unmarshal(new StringReader(xmlHierarquia));

		List<ExDocumento> docsJaNoBoletim = new ManipuladorEntrevista(docBIE)
				.obterDocsMarcados();

		for (Materia m : new ConversorDeExDocParaMateria().converter(docsJaNoBoletim))
			hierarquia.alocar(m);
	}

	public List<Topico> getTopicos() {
		return hierarquia.getTopicos();
	}

}