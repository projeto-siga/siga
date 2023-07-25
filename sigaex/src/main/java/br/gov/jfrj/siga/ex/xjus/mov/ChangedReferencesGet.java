package br.gov.jfrj.siga.ex.xjus.mov;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

@AcessoPublico
public class ChangedReferencesGet implements IXjusRecordAPI.IChangedReferencesGet {

    private static final String HQL = "select mov.idMov, case when mov.exMobil.exDocumento.dtAltDoc > mov.dtIniMov then mov.exMobil.exDocumento.dtAltDoc else mov.dtIniMov end as dt from ExMovimentacao mov where mov.exTipoMovimentacao in :tpmovs and mov.exMobil.exDocumento.dtFinalizacao != null and (((mov.exMobil.exDocumento.dtAltDoc = :dt or mov.dtIniMov = :dt) and mov.idMov > :id) or (mov.exMobil.exDocumento.dtAltDoc > :dt or mov.dtIniMov > :dt)) order by dt, mov.idMov";
//    private static final String HQL = "select mov.idMov, mob.dnmDtUltMov from ExMobil mob inner join mob.exDocumento doc inner join doc.exMobilSet mobAll inner join mobAll.exMovimentacaoSet mov where mov.exTipoMovimentacao in :tpmovs and ((mob.dnmDtUltMov > :dt) or (mob.dnmDtUltMov = :dt and mov.idMov > :id)) order by mob.dnmDtUltMov, mov.idMov";

    private static final String MYSQL = ""
            + "select "
            + "    exmoviment3_.ID_MOV as col_0_0_, "
            + "    doc.DNM_DT_ULT_MOV as col_10 "
            + "from "
            + "    (select exmobil0_.ID_DOC, exmobil0_.DNM_DT_ULT_MOV "
            + "    from siga.ex_mobil exmobil0_ "
            + "    where exmobil0_.DNM_DT_ULT_MOV >= :dt "
            + "    order by exmobil0_.DNM_DT_ULT_MOV limit :maxdocs) doc "
            + "    inner join siga.ex_mobil exmobilset2_ on doc.ID_DOC=exmobilset2_.ID_DOC "
            + "    inner join siga.ex_movimentacao exmoviment3_ on exmobilset2_.ID_MOBIL=exmoviment3_.id_mobil "
            + "where (exmoviment3_.id_tp_mov in (:tpmovs)) "
            + "    and (doc.DNM_DT_ULT_MOV>:dt or doc.DNM_DT_ULT_MOV=:dt and exmoviment3_.ID_MOV>:id) "
            + "order by doc.DNM_DT_ULT_MOV, exmoviment3_.ID_MOV limit :maxmovs;";

    @Override
    @SuppressWarnings("unchecked")
    public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
        resp.list = new ArrayList<>();
        if (req.lastdate == null)
            req.lastdate = new Date(0L);
        if (req.lastid == null)
            req.lastid = XjusUtils.formatId(0L);
        try {
            ExDao dao = ExDao.getInstance();
            EnumSet<ExTipoDeMovimentacao> tpmovs = EnumSet.of(ExTipoDeMovimentacao.ANEXACAO,
                    ExTipoDeMovimentacao.DESPACHO,
                    ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA, ExTipoDeMovimentacao.DESPACHO_INTERNO,
                    ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA,
                    ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA);
            Integer maxRows = Integer.valueOf(req.max);
            Query q;
            if (dao.isMySQL()) {
                String MYSQL = ""
                        + "SELECT  "
                        + "    exmoviment3_.ID_MOV AS col_0_0_, "
                        + "    doc.DNM_DT_ULT_MOV AS col_10 "
                        + "FROM "
                        + "    (SELECT  "
                        + "        ID_DOC, MAX(DNM_DT_ULT_MOV) DNM_DT_ULT_MOV "
                        + "    FROM "
                        + "        (SELECT  "
                        + "        exmobil0_.ID_DOC, exmobil0_.DNM_DT_ULT_MOV "
                        + "        FROM "
                        + "            siga.ex_mobil exmobil0_ "
                        + "        WHERE "
                        + "            exmobil0_.DNM_DT_ULT_MOV >= :dt "
                        + "        ORDER BY exmobil0_.DNM_DT_ULT_MOV "
                        + "        LIMIT :maxdocs) doclimited "
                        + "    GROUP BY ID_DOC) doc "
                        + "        INNER JOIN "
                        + "    siga.ex_mobil exmobilset2_ ON doc.ID_DOC = exmobilset2_.ID_DOC "
                        + "        INNER JOIN "
                        + "    siga.ex_movimentacao exmoviment3_ ON exmobilset2_.ID_MOBIL = exmoviment3_.id_mobil "
                        + "WHERE "
                        + "    (exmoviment3_.id_tp_mov IN (:tpmovs)) "
                        + "        AND (doc.DNM_DT_ULT_MOV > :dt "
                        + "        OR doc.DNM_DT_ULT_MOV = :dt "
                        + "        AND exmoviment3_.ID_MOV > :id) "
                        + "ORDER BY doc.DNM_DT_ULT_MOV , exmoviment3_.ID_MOV "
                        + "LIMIT :maxmovs";

                q = dao.em().createNativeQuery(MYSQL
                        .replace(":maxmovs", Integer.toString(maxRows))
                        .replace(":maxdocs", Integer.toString(maxRows * 200)));
                Set<Integer> setTpMovs = new HashSet<>();
                for (ExTipoDeMovimentacao tpm : tpmovs)
                    setTpMovs.add(tpm.getId());
                q.setParameter("tpmovs", setTpMovs);
            } else {
                q = dao.em().createQuery(HQL);
                q.setParameter("tpmovs", tpmovs);
                q.setMaxResults(maxRows);
            }
            Date first = req.lastdate;
            Long lastid = Long.valueOf(req.lastid);
            // System.out.println("req.last: " + SwaggerUtils.format(first));
            q.setParameter("dt", first);
            q.setParameter("id", (Long) lastid);
            // System.out.println(q.getQueryString());
            List<Object[]> list = (List<Object[]>) q.getResultList();
            for (Object rs[] : list) {
                Reference ref = new Reference();
                if (rs[0] instanceof Integer)
                    rs[0] = new Long((Integer) rs[0]);
                else if (rs[0] instanceof BigInteger)
                    rs[0] = ((BigInteger) rs[0]).longValue();
                Long id = (Long) rs[0];
                Date dt = (Date) rs[1];
                // System.out.println(SwaggerUtils.format(dt));
                ref.id = XjusUtils.formatId(id);
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
