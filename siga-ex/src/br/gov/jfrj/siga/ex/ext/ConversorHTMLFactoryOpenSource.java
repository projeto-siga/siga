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
package br.gov.jfrj.siga.ex.ext;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.FOP;
import br.gov.jfrj.itextpdf.Nheengatu;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;

public class ConversorHTMLFactoryOpenSource extends AbstractConversorHTMLFactory {

	@Override
	public ConversorHtml getConversor(final ExConfiguracaoBL conf,
			final ExDocumento doc, final String strHtml) throws Exception {
		ConversorHtml conversor;
		if (strHtml.contains("<!-- FOP -->"))
			conversor = new FOP();
		else
			conversor = new Nheengatu();
		return conversor;
	}

	@Override
	public ConversorHtml getConversor(final int conversor) throws Exception {

		if (conversor == CONVERSOR_FOP) {
			return new FOP();
		}

		if (conversor == CONVERSOR_NHEENGATU) {
			return new Nheengatu();
		}

		return null;

	}

	@Override
	public ConversorHtml getConversor() throws Exception {
		return new Nheengatu();
	}

}
