package br.gov.jfrj.siga.ex.xjus.mov;

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

	private static final String HQL = "select mov.idMov from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov in (2, 5, 6, 7, 8, 18) and mov.exMobil.exDocumento.dtFinalizacao != null and (((mov.exMobil.exDocumento.dtAltDoc > :dt or mov.dtIniMov > :dt) and mov.idMov > :id) or (mov.exMobil.exDocumento.dtAltDoc > :dt or mov.dtIniMov > :dt)) order by mov.idIniMov";

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
				ref.id = "mov:" + id;
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
