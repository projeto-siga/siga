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
package br.gov.jfrj.siga.gc.service;

import br.gov.jfrj.siga.Remote;

import javax.jws.WebMethod;
import javax.jws.WebService;

import java.util.ArrayList;

@WebService(targetNamespace = "http://impl.service.gc.siga.jfrj.gov.br/")
public interface GcService extends Remote {

	/**
	 * Método que atualiza tags hierárquicos baseado em uma id imutável.
	 * 
	 * @param categoria
	 *            Identificador da hierarquia de tags
	 * @param id
	 *            Identificador imutável do contexto
	 * @param titulos
	 *            String contendo lista de tags separados por ponto-e-virgula
	 * @return Retorna o número de registros atualizados, se tudo ocorrer corretamente.
	 * @throws Exception
	 */
//    @WebMethod
//	Integer atualizarTags(String categoria,
//			String id, String titulos) throws Exception;

    @WebMethod
	Integer atualizarTag(String tag) throws Exception;

	

}
