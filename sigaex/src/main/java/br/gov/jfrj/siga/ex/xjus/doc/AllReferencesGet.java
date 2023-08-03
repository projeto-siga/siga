package br.gov.jfrj.siga.ex.xjus.doc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

@AcessoPublico
public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {

	private static final String HQL = "select doc.idDoc from ExDocumento doc where (doc.dtFinalizacao != null) and (doc.idDoc > :id) order by doc.idDoc";

	@Override
	@SuppressWarnings("unchecked")
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = XjusUtils.formatId(0L);
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.em().createQuery(HQL, Long.class);
			q.setMaxResults(Integer.valueOf(req.max));
			Long first = Long.valueOf(req.lastid);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setParameter("id", first);
			// System.out.println(q.getQueryString());
			for (Long id : (List<Long>) q.getResultList()) {
				Reference ref = new Reference();
				ref.id = XjusUtils.formatId(id);
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
