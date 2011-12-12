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
package br.gov.jfrj.siga.wf.service;

import java.util.ArrayList;

import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService
public interface WfService extends Remote {

	/**
	 * Método que cria uma instancia da ultima versão de um workflow com o nome
	 * 'nomeProcesso'. Essa instância é da classe ProcessInstance, que é
	 * armazenado em seu contexto as variaveis mapeada no parametro
	 * 'mapContextInicial'.
	 * 
	 * @param nomeProcesso
	 *            Nome do Workflow
	 * @param mapContextoInicial
	 *            Variaveis iniciais do Workflow
	 * @return Retorna 'true' se tudo ocorrer corretamente.
	 * @throws Exception
	 */
	Boolean criarInstanciaDeProcesso(String nomeProcesso,
			String siglaCadastrante, String siglaTitular,
			ArrayList<String> keys, ArrayList<String> values) throws Exception;

	/**
	 * Varre todas as instancias de todos os Workflows, procurando um tarefa que
	 * contenha uma action e possua uma variavel de controle mapiada para uma
	 * variavel de contexto iniciada por "doc_" e seja somente leitura. Se essa
	 * variavel de controle começar com o valor do parametro 'codigoDocumento',
	 * é executado a action buscada.
	 * 
	 * @param codigoDocumento
	 *            Codigo do ducumento das actions que serão executadas.
	 * @return Retorna Action.SUCCESS se tudo ocorrer corretamente.
	 * @throws Exception
	 */
	Boolean atualizarWorkflowsDeDocumento(String codigoDocumento)
			throws Exception;

}
