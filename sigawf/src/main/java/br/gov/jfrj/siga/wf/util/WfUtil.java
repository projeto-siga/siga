package br.gov.jfrj.siga.wf.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.crivano.jflow.GraphViz;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

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
		if (ti.getInstanciaDeProcedimento().getEventoPessoa() != null) {
			if (ti.getInstanciaDeProcedimento().getEventoPessoa().getPessoaAtual().equals(so.getTitular()))
				return "(minha)";
			else
				return ti.getInstanciaDeProcedimento().getEventoPessoa().getPessoaAtual().getSiglaCompleta();
		}
		if (ti.getInstanciaDeProcedimento().getEventoLotacao() != null)
			return ti.getInstanciaDeProcedimento().getEventoLotacao().getSiglaCompleta();
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

	// private String designacao(String actorId, Set pooledActors) {
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
		WfProcedimento pi = taskInstance.getInstanciaDeProcedimento();
		return GraphViz.getDot(pi, "Início", "Fim");
	}

	public String getDot(WfDefinicaoDeProcedimento pd) throws UnsupportedEncodingException, Exception {
		return GraphViz.getDot(pd, "Início", "Fim");
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

	public String getSiglaTitular() {
		return so.getTitular().getSigla() + "@" + so.getLotaTitular().getSiglaCompleta();
	}

}
