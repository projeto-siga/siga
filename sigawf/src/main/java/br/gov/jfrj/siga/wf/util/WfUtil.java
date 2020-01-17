package br.gov.jfrj.siga.wf.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.crivano.jflow.GraphViz;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.WfProcedimento;
import br.gov.jfrj.siga.wf.WfTarefa;
import br.gov.jfrj.siga.wf.WfTipoDePrincipal;

@RequestScoped
public class WfUtil {
	protected Map<String, String> mapDescricao = new TreeMap<String, String>();

	private SigaObjects so;

	/**
	 * @deprecated CDI eyes only
	 */
	protected WfUtil() {
		this(null);
	};

	@Inject
	public WfUtil(SigaObjects so) {
		super();
		this.so = so;
	}

	/**
	 * Retorna a sigla do ator a quem estï¿½ atribuida a tarefa, ou a sigla da
	 * lotaï¿½ï¿½o se a tarefa estiver no pool. Se a tarefa estiver com o titular,
	 * retorna "(minha)"
	 * 
	 * 
	 * @param ti
	 * @return
	 */
	public String getAtendente(WfTarefa ti) {
		if (ti.getInstanciaDeProcesso().getPessoa() != null) {
			if (ti.getInstanciaDeProcesso().getPessoa().getPessoaAtual().equals(so.getTitular()))
				return "(minha)";
			else
				return ti.getInstanciaDeProcesso().getPessoa().getPessoaAtual().getSiglaCompleta();
		}
		if (ti.getInstanciaDeProcesso().getLotacao() != null)
			return ti.getInstanciaDeProcesso().getLotacao().getSiglaCompleta();
		return "";
	}

	public String getDescricao(String sigla) {
		if (mapDescricao.containsKey(sigla)) {
			return mapDescricao.get(sigla);
		}

		DpLotacao lot = so.daoLot(so.getLotaTitular().getOrgaoUsuario().getSigla() + sigla);
		if (lot != null) {
			mapDescricao.put(sigla, lot.getDescricao());
			return lot.getDescricao();
		}

		DpPessoa pes = so.daoPes(sigla);
		if (pes != null) {
			mapDescricao.put(sigla, pes.getDescricao());
			return pes.getDescricao();
		}

		return "";
	}

	/**
	 * Retorna a sigla do primeiro ator que faï¿½a parte do pool de atores na task
	 * instance.
	 * 
	 * @param ti
	 * @return
	 */
//	public String getPooledActors(WfInstanciaDeTarefa ti) {
//		if (ti.getActorId() != null)
//			return ti.getActorId();
//		if (ti.getPooledActors().size() == 0)
//			return "";
//		String s = ((PooledActor) ti.getPooledActors().toArray()[0]).getActorId();
//		if (s.startsWith(so.getLotaTitular().getOrgaoUsuario().getSigla()))
//			s = s.substring(so.getLotaTitular().getOrgaoUsuario().getSigla().length());
//		return s;
//	}

	/**
	 * Inicializa a variï¿½vel "task" para que seus atributos possam ser
	 * visualizados pelas actions.
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 * @throws AplicacaoException
	 */
	public WfTaskVO inicializarTaskVO(WfTarefa taskInstance)
			throws IllegalAccessException, InvocationTargetException, Exception, AplicacaoException {
		WfTaskVO task = new WfTaskVO(taskInstance, so.getTitular(), so.getLotaTitular());
		task.setConhecimentoEditavel(
				so.getTitular().getPessoaAtual().equals(taskInstance.getInstanciaDeProcesso().getPessoa()));
		return task;
	}

//	private String designacao(String actorId, Set pooledActors) {
//		if (actorId == null && pooledActors == null)
//			return null;
//		if (pooledActors == null || pooledActors.size() == 0)
//			return actorId;
//		String pa = "";
//		for (Object a : pooledActors) {
//			if (pa.length() > 0)
//				pa += "/";
//			if (a instanceof PooledActor)
//				pa += ((PooledActor) a).getActorId();
//		}
//
//		if (actorId == null)
//			return pa;
//
//		return actorId + " - " + pa;
//	}

	public String getDot(WfTarefa taskInstance) throws UnsupportedEncodingException, Exception {
		WfProcedimento pi = taskInstance.getInstanciaDeProcesso();
		return GraphViz.getDot(pi, "Início", "Fim");
	}

	public static void transferirDocumentosVinculados(WfProcedimento pi, String siglaTitular)
			throws Exception {
		String principal = pi.getPrincipal();
		WfTipoDePrincipal tipo = pi.getTipoDePrincipal();
		if (principal == null || tipo == null)
			return;

		if (tipo != WfTipoDePrincipal.DOC)
			return;

		if (pi.getResponsible() == null)
			return;

		String destino = pi.getResponsible().getCodigo();

		ExService service = Service.getExService();
		service.transferir(principal, destino, siglaTitular, true);
	}

//	private static boolean atorDeveReexecutarTarefa(DpPessoa ator, WfInstanciaDeTarefa ti) {
//		if (ti.getPooledActors() != null && ti.getPooledActors().size() == 1) {
//			DpLotacaoDaoFiltro lotflt = new DpLotacaoDaoFiltro();
//			lotflt.setSiglaCompleta(((PooledActor) ti.getPooledActors().toArray()[0]).getActorId());
//			DpLotacao lotacao = (DpLotacao) WfDao.getInstance().consultarPorSigla(lotflt);
//			return !ator.isFechada() && ator.getLotacao().equivale(lotacao);
//		} else {
//			return false;
//		}
//	}

	public void assertPodeTransferirDocumentosVinculados(WfTarefa ti, String siglaTitular) throws Exception {
		String principal = ti.getInstanciaDeProcesso().getPrincipal();
		WfTipoDePrincipal tipo = ti.getInstanciaDeProcesso().getTipoDePrincipal();
		if (principal == null || tipo == null)
			return;

		if (tipo != WfTipoDePrincipal.DOC)
			return;

		ExService service = Service.getExService();
		if (!service.podeTransferir(principal, siglaTitular, false)) {
			throw new AplicacaoException("A tarefa não pode prosseguir porque o documento '" + principal
					+ "' não pode ser transferido. Por favor, verifique se o documento está em sua lotação e se está 'Aguardando andamento'.");
		}
	}

	public boolean assertLotacaoAscendenteOuDescendente(DpLotacao lotAtual, DpLotacao lotFutura)
			throws AplicacaoException {
		if (lotAtual.getIdInicial().equals(lotFutura.getIdInicial()))
			return true;

		// Linha ascendente
		DpLotacao lot = lotAtual;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotFutura.getIdInicial()))
				return true;
		}

		// Descendente direta
		lot = lotFutura;
		while (lot.getLotacaoPai() != null) {
			lot = lot.getLotacaoPai();
			if (lot.getIdInicial().equals(lotAtual.getIdInicial()))
				return true;
		}

		throw new AplicacaoException("A designaï¿½ï¿½o de '" + lotAtual.getSigla() + "' para '" + lotFutura.getSigla()
				+ "' nï¿½o ï¿½ permitida pois sï¿½ sï¿½o aceitas lotaï¿½ï¿½es ascendentes seguindo a linha do organograma ou descendentes diretas.");
	}

	public String getSiglaTitular() {
		return so.getTitular().getSigla() + "@" + so.getLotaTitular().getSiglaCompleta();
	}

	/**
	 * Monta o objeto WfTaskVO (view object, que serï¿½ usado na interface do
	 * usuï¿½rio).
	 * 
	 * @param taskInstance
	 * @param variableAccesses
	 * @param siglaDoc
	 * @throws Exception
	 */
	public void addTask(Map<String, List<WfTaskVO>> mobilMap, WfTarefa taskInstance, String siglaDoc,
			String sigla) throws Exception {
		WfTaskVO task = new WfTaskVO(taskInstance, sigla, so.getTitular(), so.getLotaTitular());
		if (!mobilMap.containsKey(siglaDoc)) {
			mobilMap.put(siglaDoc, new ArrayList<WfTaskVO>());
		}
		List<WfTaskVO> tasks = mobilMap.get(siglaDoc);
		tasks.add(task);
	}

}
