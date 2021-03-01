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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.dao.WfStarter;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.service.WfService;
import br.gov.jfrj.siga.wf.util.WfEngine;
import br.gov.jfrj.siga.wf.util.WfHandler;

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
	private final static Logger log = Logger.getLogger(WfService.class);

	private static class SoapContext implements Closeable {
		EntityManager em;
		boolean transacional;
		long inicio = System.currentTimeMillis();

		public SoapContext(boolean transacional) {
			this.transacional = transacional;
			em = WfStarter.emf.createEntityManager();
			ContextoPersistencia.setEntityManager(em);

			ModeloDao.freeInstance();
			WfDao.getInstance();
			try {
				Wf.getInstance().getConf().limparCacheSeNecessario();
			} catch (Exception e1) {
				throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
			}
			if (this.transacional)
				em.getTransaction().begin();
		}

		public void rollback(Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			if (!RequestLoggerFilter.isAplicacaoException(e)) {
				RequestLoggerFilter.logException(null, inicio, e);
			}
		}

		@Override
		public void close() throws IOException {
			try {
				if (this.transacional)
					em.getTransaction().commit();
			} catch (Exception e) {
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
				throw new RuntimeException(e);
			} finally {
				em.close();
				ContextoPersistencia.setEntityManager(null);
				ContextoPersistencia.setDt(null);
			}
		}
	}

	/**
	 * Atualiza o workflow de um documento. Este método pesquisa todas as variáveis
	 * que começam com "doc_" em todas as instâncias de tarefas. Quando encontra a
	 * variável, passa a sigla do documento para o execution execution context e
	 * executa a ação da tarefa.
	 */
	public Boolean atualizarWorkflowsDeDocumento(String siglaDoc) throws Exception {
		try (SoapContext ctx = new SoapContext(true)) {
			try {
				List<WfProcedimento> pis = WfDao.getInstance().consultarProcedimentosAtivosPorEvento(siglaDoc);
				boolean f = false;
				ExService exSvc = Service.getExService();
				Boolean semEfeito = exSvc.isSemEfeito(siglaDoc);
				if (semEfeito == null)
					return false;
				for (WfProcedimento pi : pis) {
					if (semEfeito)
						Wf.getInstance().getBL().encerrarProcessInstance(pi.getId(),
								WfDao.getInstance().consultarDataEHoraDoServidor());
					else {
						new WfEngine(WfDao.getInstance(), new WfHandler(null, null, null)).resume(siglaDoc, null, null);
					}
					f = true;
				}
				return f;
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw ex;
			}
		}
	}

	/**
	 * Inicia um novo procedimento.
	 */
	public Boolean criarInstanciaDeProcesso(String nomeProcedimento, String siglaCadastrante, String siglaTitular,
			ArrayList<String> keys, ArrayList<String> values) throws Exception {
		try (SoapContext ctx = new SoapContext(true)) {
			try {

				if (nomeProcedimento == null)
					throw new RuntimeException("Nome do procedimento precisa ser informado.");
				WfDefinicaoDeProcedimento pd = WfDao.getInstance()
						.consultarWfDefinicaoDeProcedimentoPorNome(nomeProcedimento);
				if (pd == null)
					throw new RuntimeException(
							"Não foi encontrado um procedimento com o nome '" + nomeProcedimento + "'");
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				PessoaLotacaoParser titularParser = new PessoaLotacaoParser(siglaTitular);

				CpIdentidade identidade = null;
				List<CpIdentidade> l = WfDao.getInstance().consultaIdentidades(cadastranteParser.getPessoa());
				if (l.size() > 0)
					identidade = l.get(0);
				WfProcedimento pi = Wf.getInstance().getBL().createProcessInstance(pd.getId(),
						titularParser.getPessoa(), titularParser.getLotacao(), identidade, null, null, keys, values,
						false);

				WfBL.transferirDocumentosVinculados(pi, siglaTitular);
				return true;
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw ex;
			}
		}
	}

	private Object consultaIdentidades(DpPessoa pessoa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object variavelPorDocumento(String codigoDocumento, String nomeDaVariavel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
