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
package br.gov.jfrj.siga.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.WfProcedimento;
import br.gov.jfrj.siga.wf.WfTarefa;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.service.WfService;
import br.gov.jfrj.siga.wf.util.WfEngine;
import br.gov.jfrj.siga.wf.util.WfHandler;
import br.gov.jfrj.siga.wf.util.WfUtil;

/**
 * Classe que representa o webservice do workflow. O SIGA-DOC faz a chamada
 * remota dos métodos dessa classe para atualizar o workflow automaticamente,
 * baseando-se nas operações sobre documentos.
 * 
 * @author kpf
 * 
 */
@WebService(serviceName = "WfService", endpointInterface = "br.gov.jfrj.siga.wf.service.WfService", targetNamespace = "http://impl.service.wf.siga.jfrj.gov.br/")
public class WfServiceImpl implements WfService {

	private boolean hideStackTrace = false;

	public boolean isHideStackTrace() {
		return hideStackTrace;
	}

	public void setHideStackTrace(boolean hideStackTrace) {
		this.hideStackTrace = hideStackTrace;
	}

	/**
	 * Atualiza o workflow de um documento. Este método pesquisa todas as variáveis
	 * que começam com "doc_" em todas as instâncias de tarefas. Quando encontra a
	 * variável, passa a sigla do documento para o execution execution context e
	 * executa a ação da tarefa.
	 */
	public Boolean atualizarWorkflowsDeDocumento(String siglaDoc) throws Exception {
		List<WfProcedimento> pis = WfDao.getInstance().consultarProcedimentosAtivosPorDocumento(siglaDoc);
		boolean f = false;
		ExService exSvc = Service.getExService();
		Boolean semEfeito = exSvc.isSemEfeito(siglaDoc);
		for (WfProcedimento pi : pis) {
			if (semEfeito)
				Wf.getInstance().getBL().encerrarProcessInstance(pi.getId(),
						WfDao.getInstance().consultarDataEHoraDoServidor());
			else {
				new WfEngine(WfDao.getInstance(), new WfHandler()).resume(siglaDoc, null, null);
			}
			f = true;
		}
		return f;
	}

	/**
	 * Inicia um novo procedimento.
	 */
	public Boolean criarInstanciaDeProcesso(String nomeProcedimento, String siglaCadastrante, String siglaTitular,
			ArrayList<String> keys, ArrayList<String> values) throws Exception {

		if (nomeProcedimento == null)
			throw new RuntimeException("Nome do procedimento precisa ser informado.");
		WfDefinicaoDeProcedimento pd = WfDao.getInstance().consultarWfDefinicaoDeProcedimentoPorNome(nomeProcedimento);
		if (pd == null)
			throw new RuntimeException("Não foi encontrado um procedimento com o nome '" + nomeProcedimento + "'");
		PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
		PessoaLotacaoParser titularParser = new PessoaLotacaoParser(siglaTitular);

		WfProcedimento pi = Wf.getInstance().getBL().createProcessInstance(pd.getId(), cadastranteParser.getPessoa(),
				cadastranteParser.getLotacao(), titularParser.getPessoa(), titularParser.getLotacao(), keys, values,
				false);

		WfUtil.transferirDocumentosVinculados(pi, siglaTitular);
		return true;
	}

	@Override
	public Object variavelPorDocumento(String codigoDocumento, String nomeDaVariavel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
