package br.gov.jfrj.siga.gc.xjus;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {

	private static final String HQL = "select inf.id from GcInformacao inf where (inf.elaboracaoFim != null) and (inf.id > :id) order by inf.id";

	@Override
	@SuppressWarnings("unchecked")
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = XjusUtils.formatId(0L);
		Query q = ContextoPersistencia.em().createQuery(HQL, Long.class);
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
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
