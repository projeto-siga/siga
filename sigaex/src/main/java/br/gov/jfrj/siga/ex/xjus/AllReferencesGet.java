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
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.AllReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class AllReferencesGet implements IXjusRecordAPI.IAllReferencesGet {
	static public final long TIMEOUT_MILLISECONDS = 15000;

	@Override
	public void run(AllReferencesGetRequest req, AllReferencesGetResponse resp)
			throws Exception {
		resp.list = new ArrayList<>();
		if (req.lastid == null)
			req.lastid = defaultLastId();

		final CountDownLatch responseWaiter = new CountDownLatch(
				RecordServiceEnum.values().length);
		Map<RecordServiceEnum, Future<SwaggerAsyncResponse<AllReferencesGetResponse>>> map = new HashMap<>();

		// Call Each System
		for (RecordServiceEnum service : RecordServiceEnum.values()) {
			String url = serviceUrl(service);

			AllReferencesGetRequest q = new AllReferencesGetRequest();
			q.max = req.max;
			String split[] = req.lastid.split("-");
			q.lastid = split[0];
			if (service.ordinal() > Integer.valueOf(split[1]))
				q.lastid = Utils.formatId(Long.valueOf(q.lastid) - 1);
			Future<SwaggerAsyncResponse<AllReferencesGetResponse>> future = SwaggerCall
					.callAsync(
							service.name().toLowerCase() + "-all-references",
							Prop.get("/xjus.password"), "GET",
							url, q, AllReferencesGetResponse.class);
			map.put(service, future);
		}

		Date dt1 = new Date();

		for (RecordServiceEnum service : RecordServiceEnum.values()) {
			long timeout = TIMEOUT_MILLISECONDS
					- ((new Date()).getTime() - dt1.getTime());
			if (timeout < 0L)
				timeout = 0;
			SwaggerAsyncResponse<AllReferencesGetResponse> futureresponse = map
					.get(service).get(timeout, TimeUnit.MILLISECONDS);
			AllReferencesGetResponse o = (AllReferencesGetResponse) futureresponse
					.getResp();

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

	static public String serviceUrl(RecordServiceEnum service) {
		String url = SwaggerServlet
				.getHttpServletRequest()
				.getRequestURL()
				.toString()
				.replace("/x-jus/v1/",
						"/x-jus/" + service.name().toLowerCase() + "/v1/");
		return url;
	}

	static public String defaultLastId() {
		return Utils.formatId(0L)
				+ "-"
				+ RecordServiceEnum.values()[RecordServiceEnum.values().length - 1]
						.ordinal();
	}

	public String getContext() {
		return "obter a lista de índices";
	}
}
