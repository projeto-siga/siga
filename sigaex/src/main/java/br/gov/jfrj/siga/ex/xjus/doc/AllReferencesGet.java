package br.gov.jfrj.siga.ex.xjus.doc;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Query;

import br.gov.jfrj.siga.ex.xjus.Utils;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {

	private static final String HQL = "select doc.idDoc from ExDocumento doc where (doc.dtFinalizacao != null) and (doc.idDoc > :id) order by doc.idDoc";

	@Override
	public void run(AllReferencesGetRequest req, AllReferencesGetResponse resp)
			throws Exception {
		resp.list = new ArrayList<>();
		if (req.last == null)
			req.last = Utils.formatId(0L);
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(HQL);
			q.setMaxResults(Integer.valueOf(req.max));
			Long first = Long.valueOf(req.last);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setLong("id", first);
			// System.out.println(q.getQueryString());
			@SuppressWarnings("rawtypes")
			Iterator i = q.iterate();
			while (i.hasNext()) {
				Reference ref = new Reference();
				Long id = (Long) (i.next());
				// System.out.println(SwaggerUtils.format(dt));
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
