package br.gov.jfrj.siga.gc.xjus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.gc.model.GcTipoMovimentacao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

public class ChangedReferencesGet implements IXjusRecordAPI.IChangedReferencesGet {

	private static final String HQL = "select inf.id, inf.dtAltDoc from GcInformacao inf where (inf.elaboracaoFim != null) and ((inf.dtAltDoc = :dt and inf.id > :id) or (inf.dtAltDoc > :dt)) order by inf.dtAltDoc, inf.id";

	@Override
	@SuppressWarnings("unchecked")
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastdate == null)
			req.lastdate = new Date(0L);
		if (req.lastid == null)
			req.lastid = XjusUtils.formatId(0L);

//		String HQL = "select inf.id, inf.dtAltDoc from (select inf2.id, max(mov.hisDtIni) as dtAltDoc from GcInformacao as inf2 join inf2.movs as mov where inf2.elaboracaoFim != null and mov.tipo.id in :tipos group by inf2) as inf where ((inf.dtAltDoc = :dt and inf.id > :id) or (inf.dtAltDoc > :dt)) order by inf.dtAltDoc, inf.id";
//		String HQL = "select inf2.id, max(mov.hisDtIni) as dtAltDoc from GcInformacao as inf2 join inf2.movs as mov where inf2.elaboracaoFim != null and mov.tipo.id in :tipos group by inf2";
		String HQL = "select inf.id id, inf.dtAltDoc dtAltDoc from (select inf2.id_informacao id, max(mov.his_dt_ini) dtAltDoc from sigagc.gc_informacao inf2 join sigagc.gc_movimentacao mov on (inf2.id_informacao = mov.id_informacao) where inf2.dt_elaboracao_fim is not null and mov.id_tipo_movimentacao in (:tipos) group by inf2.id_informacao) inf where ((inf.dtAltDoc = :dt and inf.id > :id) or (inf.dtAltDoc > :dt)) order by inf.dtAltDoc, inf.id";
//		String HQL = "select inf2.id_informacao, max(mov.his_dt_ini) from sigagc.gc_informacao inf2 join sigagc.gc_movimentacao mov on (inf2.id_informacao = mov.id_informacao) where inf2.dt_elaboracao_fim is not null and mov.id_tipo_movimentacao in (:tipos) group by inf2.id_informacao";

		Query q = ContextoPersistencia.em().createNativeQuery(HQL);
		q.setMaxResults(Integer.valueOf(req.max));
		Date first = req.lastdate;
		Long lastid = Long.valueOf(req.lastid);
		// System.out.println("req.last: " + SwaggerUtils.format(first));
		List<Long> tipos = new ArrayList<>();
		tipos.add(GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO);
		tipos.add(GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO);
		q.setParameter("tipos", tipos);
//		q.setParameter("tipos", new Long[] { GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO,
//				GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO });
		q.setParameter("dt", first);
		q.setParameter("id", lastid);
		// System.out.println(q.getQueryString());
		List<Object[]> list = (List<Object[]>) q.getResultList();
		for (Object rs[] : list) {
			Reference ref = new Reference();
			Long id = null;
			if (rs[0] instanceof BigDecimal)
				id = ((BigDecimal) rs[0]).longValue();
			else
				id = (Long) rs[0];
			Date dt = (Date) rs[1];
			// System.out.println(SwaggerUtils.format(dt));
			ref.id = XjusUtils.formatId(id);
			ref.date = dt;
			resp.list.add(ref);
		}
	}

	public String getContext() {
		return "obter a lista de referências";
	}
}
