package br.gov.jfrj.siga.ex.xjus.mov;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.ex.xjus.Utils;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {

	private static final String HQL = "select mov.idMov from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov in (2, 5, 6, 7, 8, 18) and mov.exMobil.exDocumento.dtFinalizacao != null and (mov.idMov > :id) order by mov.idMov";

	@Override
	@SuppressWarnings("unchecked")
	public void run(AllReferencesGetRequest req, AllReferencesGetResponse resp)
			throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = Utils.formatId(0L);
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.em().createQuery(HQL);
			q.setMaxResults(Integer.valueOf(req.max));
			Long first = Long.valueOf(req.lastid);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setParameter("id", first);
			// System.out.println(q.getQueryString());
			for (Long id : (List<Long>) q.getResultList()) {
				Reference ref = new Reference();
				ref.id = Utils.formatId(id);
				resp.list.add(ref);
			}
		} finally {
			ExDao.freeInstance();
		}

	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
