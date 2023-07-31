package br.gov.jfrj.siga.ex.xjus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.ex.util.ExXjusRecordServiceEnum;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

@AcessoPublico
public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {
    static public final long TIMEOUT_MILLISECONDS = 50000;

    @Override
    public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
        resp.list = new ArrayList<>();
        if (req.lastid == null)
            req.lastid = defaultLastId();

        final CountDownLatch responseWaiter = new CountDownLatch(ExXjusRecordServiceEnum.values().length);
        Map<ExXjusRecordServiceEnum, Future<SwaggerAsyncResponse<Response>>> map = new HashMap<>();

        // Call Each System
        for (ExXjusRecordServiceEnum service : ExXjusRecordServiceEnum.values()) {
            String url = serviceUrl(service);

            Request q = new Request();
            q.max = req.max;
            String split[] = req.lastid.split("-");
            q.lastid = split[0];
            if (service.ordinal() > Integer.valueOf(split[1]))
                q.lastid = XjusUtils.formatId(Long.valueOf(q.lastid) - 1);
            Future<SwaggerAsyncResponse<Response>> future = SwaggerCall.callAsync(
                    service.name().toLowerCase() + "-all-references", Prop.get("/xjus.password"), "GET", url, q,
                    Response.class);
            map.put(service, future);
        }

        Date dt1 = new Date();

        for (ExXjusRecordServiceEnum service : ExXjusRecordServiceEnum.values()) {
            long timeout = TIMEOUT_MILLISECONDS - ((new Date()).getTime() - dt1.getTime());
            if (timeout < 0L)
                timeout = 0;
            SwaggerAsyncResponse<Response> futureresponse = map.get(service).get(timeout, TimeUnit.MILLISECONDS);
            Response o = (Response) futureresponse.getResp();

            SwaggerException ex = futureresponse.getException();
            if (ex != null)
                throw ex;

            for (Reference r : o.list) {
                r.id += "-" + service.ordinal();
                resp.list.add(r);
            }
        }

        // Sort items by compound ID
        Collections.sort(resp.list, new Comparator<Reference>() {
            @Override
            public int compare(Reference o1, Reference o2) {
                return o1.id.compareTo(o2.id);
            }
        });

        // Drop items that exceed the maximum size
        while (resp.list.size() > Integer.valueOf(req.max))
            resp.list.remove(resp.list.size() - 1);
    }

    static public String serviceUrl(ExXjusRecordServiceEnum service) {
        String url = SwaggerServlet.getHttpServletRequest().getRequestURL().toString().replace("/x-jus/v1/",
                "/x-jus/" + service.name().toLowerCase() + "/v1/");
        return url;
    }

    static public String defaultLastId() {
        return XjusUtils.formatId(0L) + "-"
                + ExXjusRecordServiceEnum.values()[ExXjusRecordServiceEnum.values().length - 1].ordinal();
    }

    static public String defaultCursor() {
        String s = "";
        for (ExXjusRecordServiceEnum service : ExXjusRecordServiceEnum.values()) {
            if (!s.isEmpty())
                s += ";";
            s += XjusUtils.formatId(0L) + "-" + service.ordinal();
        }
        return s;
    }

    public String getContext() {
        return "obter a lista de índices";
    }
}
