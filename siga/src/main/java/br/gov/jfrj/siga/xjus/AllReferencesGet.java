package br.gov.jfrj.siga.xjus;

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

import br.gov.jfrj.siga.base.DisableableEnum;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.XjusRecordServiceEnum;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {
	static public final long TIMEOUT_MILLISECONDS = 50000;

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = defaultLastId();

		final CountDownLatch responseWaiter = new CountDownLatch(XjusRecordServiceEnum.values().length);
		Map<XjusRecordServiceEnum, Future<SwaggerAsyncResponse<Response>>> map = new HashMap<>();

		// Call Each System
		for (XjusRecordServiceEnum service : DisableableEnum.enabledValues(XjusRecordServiceEnum.values())) {
			Request q = new Request();
			q.max = req.max;
			String split[] = req.lastid.split("-");
			q.lastid = split[0];
			if (service.ordinal() > Integer.valueOf(split[1]))
				q.lastid = Utils.formatId(Long.valueOf(q.lastid) - 1);
			Future<SwaggerAsyncResponse<Response>> future = SwaggerCall.callAsync(
					service.name().toLowerCase() + "-all-references", Prop.get("/xjus.password"), "GET",
					Utils.buildUrl(service), q, Response.class);
			map.put(service, future);
		}

		Date dt1 = new Date();

		for (XjusRecordServiceEnum service : DisableableEnum.enabledValues(XjusRecordServiceEnum.values())) {
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

	static public String defaultLastId() {
		return Utils.formatId(0L) + "-"
				+ XjusRecordServiceEnum.values()[XjusRecordServiceEnum.values().length - 1].ordinal();
	}

	static public String defaultCursor() {
		String s = "";
		for (XjusRecordServiceEnum service : XjusRecordServiceEnum.values()) {
			if (!s.isEmpty())
				s += ";";
			s += Utils.formatId(0L) + "-" + service.ordinal();
		}
		return s;
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
