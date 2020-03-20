package br.gov.jfrj.siga.ex.xjus.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.ex.xjus.Utils;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class ChangedReferencesGet implements IXjusRecordAPI.IChangedReferencesGet {

	private static final String HQL = "select doc.idDoc, doc.dtAltDoc from ExDocumento doc where (doc.dtFinalizacao != null) and ((doc.dtAltDoc = :dt and doc.idDoc > :id) or (doc.dtAltDoc > :dt)) order by doc.dtAltDoc";

	@Override
	@SuppressWarnings("unchecked")
	public void run(ChangedReferencesGetRequest req, ChangedReferencesGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastdate == null)
			req.lastdate = new Date(0L);
		if (req.lastid == null)
			req.lastid = Utils.formatId(0L);
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.em().createQuery(HQL);
			q.setMaxResults(Integer.valueOf(req.max));
			Date first = req.lastdate;
			Long lastid = Long.valueOf(req.lastid);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setParameter("dt", first);
			q.setParameter("id", lastid);
			// System.out.println(q.getQueryString());
			List<Object[]> list = (List<Object[]>) q.getResultList();
			for (Object rs[] : list) {
				Reference ref = new Reference();
				Long id = (Long) rs[0];
				Date dt = (Date) rs[1];
				// System.out.println(SwaggerUtils.format(dt));
				ref.id = Utils.formatId(id);
				ref.date = dt;
				resp.list.add(ref);
			}
		} finally {
			ExDao.freeInstance();
		}

	}

	public String getContext() {
		return "obter a lista de referências";
	}
}
