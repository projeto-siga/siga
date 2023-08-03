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

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.util.ExXjusRecordServiceEnum;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;

@AcessoPublico
public class ChangedReferencesGet implements IXjusRecordAPI.IChangedReferencesGet {

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		resp.list = new ArrayList<>();

		if (req.lastdate == null)
			req.lastdate = new Date(0L);
		if (req.cursor == null)
			req.cursor = AllReferencesGet.defaultCursor();

		final CountDownLatch responseWaiter = new CountDownLatch(ExXjusRecordServiceEnum.values().length);
		Map<ExXjusRecordServiceEnum, Future<SwaggerAsyncResponse<Response>>> map = new HashMap<>();

		String[] aCursor = req.cursor.split(";");
		
		if (aCursor.length != ExXjusRecordServiceEnum.values().length) {
		    throw new RuntimeException("Cursor inválido: '" + req.cursor + "'. Deveria ser algo do tipo: '" + AllReferencesGet.defaultCursor() + "'.");
		}

		// Call Each System
		for (ExXjusRecordServiceEnum service : ExXjusRecordServiceEnum.values()) {
			String url = AllReferencesGet.serviceUrl(service);

			Request q = new Request();
			q.max = req.max;
			String split[] = aCursor[service.ordinal()].split("-");
			String lastid = split[0];
//			if (service.ordinal() > Integer.valueOf(split[1]))
//				lastid = Utils.formatId(Long.valueOf(lastid) - 1);
			q.lastdate = req.lastdate;
			q.lastid = lastid;
			Future<SwaggerAsyncResponse<Response>> future = SwaggerCall.callAsync(
					service.name().toLowerCase() + "-changed-references", Prop.get("/xjus.password"), "GET", url, q,
					Response.class);
			map.put(service, future);
		}

		Date dt1 = new Date();

		for (ExXjusRecordServiceEnum service : ExXjusRecordServiceEnum.values()) {
			long timeout = AllReferencesGet.TIMEOUT_MILLISECONDS - ((new Date()).getTime() - dt1.getTime());
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
				int i = o1.date.compareTo(o2.date);
				if (i != 0)
					return i;
				return o1.id.compareTo(o2.id);
			}
		});

		// Drop items that exceed the maximum size
		while (resp.list.size() > Integer.valueOf(req.max))
			resp.list.remove(resp.list.size() - 1);

		// Build a cursor by updating the previous one with new IDs that were returned
		// at the current result list
		for (Reference r : resp.list) {
		    ExXjusRecordServiceEnum service = ExXjusRecordServiceEnum.values()[Integer.valueOf(r.id.split("-")[1])];
			aCursor[service.ordinal()] = r.id;
		}

		resp.cursor = String.join(";", aCursor);

		if (resp.cursor == null || resp.cursor.isEmpty())
			resp.cursor = AllReferencesGet.defaultCursor();
	}

	public String getContext() {
		return "obter a lista de referências";
	}
}
