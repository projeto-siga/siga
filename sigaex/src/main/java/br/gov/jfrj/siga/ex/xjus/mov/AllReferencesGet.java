package br.gov.jfrj.siga.ex.xjus.mov;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Query;

import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {

	private static final String HQL = "select mov.idMov from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov in (2, 5, 6, 7, 8, 18) and mov.exMobil.exDocumento.dtFinalizacao != null and (mov.idMov > :id) order by mov.idMov";

	@Override
	public void run(AllReferencesGetRequest req, AllReferencesGetResponse resp)
			throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = "mov:0";
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(HQL);
			q.setMaxResults(Integer.valueOf(req.max));
			Long first = Long.valueOf(req.lastid.split(":")[1]);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setLong("id", first);
			// System.out.println(q.getQueryString());
			@SuppressWarnings("rawtypes")
			Iterator i = q.iterate();
			while (i.hasNext()) {
				Reference ref = new Reference();
				Long id = (Long) (i.next());
				// System.out.println(SwaggerUtils.format(dt));
				ref.id = "mov:" + id;
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
