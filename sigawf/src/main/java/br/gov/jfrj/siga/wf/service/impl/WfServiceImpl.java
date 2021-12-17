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
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.xml.ws.WebServiceContext;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.jee.SoapContext;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.dao.WfStarter;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;
import br.gov.jfrj.siga.wf.service.WfProcedimentoWSTO;
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

	private class WfSoapContext extends SoapContext {
		EntityManager em;
		boolean transacional;
		long inicio = System.currentTimeMillis();

		public WfSoapContext(boolean transacional) {
			super(context, WfStarter.emf, transacional);
		}

		@Override
		public void initDao() {
			WfDao.getInstance();
			try {
				Wf.getInstance().getConf().limparCacheSeNecessario();
			} catch (Exception e1) {
				throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
			}
		}
	}

	@Resource
	private WebServiceContext context;

	/**
	 * Atualiza o workflow de um documento. Este método pesquisa todas as variáveis
	 * que começam com "doc_" em todas as instâncias de tarefas. Quando encontra a
	 * variável, passa a sigla do documento para o execution execution context e
	 * executa a ação da tarefa.
	 */
	public Boolean atualizarWorkflowsDeDocumento(String siglaDoc) throws Exception {
		// System.out.println("*** atualizarWorkflowsDeDocumento: " + siglaDoc);
		try (SoapContext ctx = new WfSoapContext(true)) {
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
	@Override
	public Boolean criarInstanciaDeProcesso(String nomeProcedimento, String siglaCadastrante, String siglaTitular,
			ArrayList<String> keys, ArrayList<String> values, String tipoDePrincipal, String principal) throws Exception {
		try (SoapContext ctx = new WfSoapContext(true)) {
			try {

				if (nomeProcedimento == null)
					throw new RuntimeException("Nome do procedimento precisa ser informado.");
				WfDefinicaoDeProcedimento pd = null;
				try {
					pd = WfDao.getInstance().consultarPorSigla(nomeProcedimento, WfDefinicaoDeProcedimento.class, null);
				} catch (Exception ex) {
					// Engolir a exceção que é gerada quando a sigla do processo é considerada
					// inválida. Isso precisa ser feito porque também é permitido criar instância de
					// procediemnto pelo título do diagrama.
				}
				if (pd == null)
					pd = WfDao.getInstance().consultarWfDefinicaoDeProcedimentoPorNome(nomeProcedimento);
				if (pd == null)
					throw new RuntimeException(
							"Não foi encontrado um procedimento com o nome '" + nomeProcedimento + "'");
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				PessoaLotacaoParser titularParser = new PessoaLotacaoParser(siglaTitular);

				CpIdentidade identidade = null;
				List<CpIdentidade> l = WfDao.getInstance().consultaIdentidades(cadastranteParser.getPessoa());
				if (l.size() > 0)
					identidade = l.get(0);
				WfProcedimento pi = Wf.getInstance().getBL().createProcessInstance(pd.getId(), null,
						titularParser.getPessoa(), titularParser.getLotacao(), identidade, WfTipoDePrincipal.valueOf(tipoDePrincipal), principal, keys, values,
						false);

				WfBL.transferirDocumentosVinculados(pi, siglaTitular);
				return true;
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw ex;
			}
		}
	}

	public WfProcedimentoWSTO consultarProcedimento(String siglaProcedimento) throws Exception {
		try (SoapContext ctx = new WfSoapContext(false)) {
			if (siglaProcedimento == null)
				throw new RuntimeException("Sigla do procedimento precisa ser informada.");
			WfProcedimento pi = WfDao.getInstance().consultarPorSigla(siglaProcedimento, WfProcedimento.class, null);
			if (pi == null)
				throw new RuntimeException(
						"Não foi encontrado um procedimento com a sigla '" + siglaProcedimento + "'");
			WfProcedimentoWSTO r = new WfProcedimentoWSTO();
			r.setSigla(pi.getSigla());
			r.setPrincipal(pi.getPrincipal());
			r.setTitular(pi.getTitular().getDescricao());
			r.setLotaTitular(pi.getLotaTitular().getDescricao());
			r.setVar(pi.getVariable());
			return r;
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
