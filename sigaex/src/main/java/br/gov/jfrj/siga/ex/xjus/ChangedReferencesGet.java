package br.gov.jfrj.siga.ex.xjus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.TemporalType;

import org.hibernate.Query;

import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

import com.crivano.swaggerservlet.SwaggerUtils;

public class ChangedReferencesGet implements
		IXjusRecordAPI.IChangedReferencesGet {

	private static final String HQL = "select doc.idDoc, doc.dtAltDoc from ExDocumento doc where (doc.dtFinalizacao != null) and ((doc.dtAltDoc = :dt and doc.idDoc > :id) or (doc.dtAltDoc > :dt)) order by doc.dtAltDoc";

	@Override
	public void run(ChangedReferencesGetRequest req,
			ChangedReferencesGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastdate == null)
			req.lastdate = SwaggerUtils.format(new Date(0L)) + ";0";
		try {
			ExDao dao = ExDao.getInstance();
			Query q = dao.getSessao().createQuery(HQL);
			q.setMaxResults(Integer.valueOf(req.max));
			String[] split = req.lastdate.split(";");
			Date first = SwaggerUtils.isoFormatter.parse(split[0]);
			Long lastid = Long.valueOf(split[1]);
			// System.out.println("req.last: " + SwaggerUtils.format(first));
			q.setTimestamp("dt", first);
			q.setLong("id", lastid);
			// System.out.println(q.getQueryString());
			@SuppressWarnings("rawtypes")
			Iterator i = q.iterate();
			while (i.hasNext()) {
				Reference ref = new Reference();
				Object rs = i.next();
				Long id = (Long) ((Object[]) rs)[0];
				Date dt = (Date) ((Object[]) rs)[1];
				// System.out.println(SwaggerUtils.format(dt));
				ref.id = "doc:" + id;
				resp.list.add(ref);

				resp.last = SwaggerUtils.format(dt) + ";" + id;
			}
		} finally {
			ExDao.freeInstance();
		}

	}

	public String getContext() {
		return "obter a lista de referências";
	}
}
