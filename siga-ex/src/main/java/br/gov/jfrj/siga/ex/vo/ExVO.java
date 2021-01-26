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
package br.gov.jfrj.siga.ex.vo;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.VO;
import br.gov.jfrj.siga.base.util.Utils;

public class ExVO extends VO {

	public void addAcao(String icone, String nome, String nameSpace, String action, boolean pode, String msgConfirmacao,
			String parametros, String pre, String pos, String classe) {
		addAcao(icone, nome, nameSpace, action, pode, null, msgConfirmacao, parametros, pre, pos, classe, null);
	}

	@Override
	public void addAcao(String icone, String nome, String nameSpace, String action, boolean pode, String tooltip,
			String msgConfirmacao, String parametros, String pre, String pos, String classe, String modal) {
		TreeMap<String, String> params = new TreeMap<String, String>();

		if (this instanceof ExMovimentacaoVO) {
			params.put("id", Long.toString(((ExMovimentacaoVO) this).getIdMov()));
			params.put("sigla", ((ExMovimentacaoVO) this).getMobilVO().getSigla());
		} else if (this instanceof ExMobilVO) {
			params.put("sigla", ((ExMobilVO) this).getSigla());
		} else if (this instanceof ExDocumentoVO) {
			params.put("sigla", ((ExDocumentoVO) this).getSigla());
		}

		if (parametros != null) {
			if (parametros.startsWith("&"))
				parametros = parametros.substring(1);
			else
				params.clear();
			try {
				Utils.mapFromUrlEncodedForm(params, parametros.getBytes("iso-8859-1"));
			} catch (UnsupportedEncodingException e) {
			}
		}

		if (pode) {
			AcaoVO acao = new AcaoVO(icone, nome, nameSpace, action, pode, msgConfirmacao, params, pre, pos, classe,
					modal);
			getAcoes().add(acao);
		}
	}
}