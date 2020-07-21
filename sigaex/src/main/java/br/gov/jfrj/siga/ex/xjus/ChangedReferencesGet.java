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
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetRequest;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.ChangedReferencesGetResponse;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.Reference;

public class ChangedReferencesGet implements
		IXjusRecordAPI.IChangedReferencesGet {

	@Override
	public void run(ChangedReferencesGetRequest req,
			ChangedReferencesGetResponse resp) throws Exception {
		resp.list = new ArrayList<>();

		if (req.lastdate == null)
			req.lastdate = new Date(0L);
		if (req.lastid == null)
			req.lastid = AllReferencesGet.defaultLastId();

		final CountDownLatch responseWaiter = new CountDownLatch(
				RecordServiceEnum.values().length);
		Map<RecordServiceEnum, Future<SwaggerAsyncResponse<ChangedReferencesGetResponse>>> map = new HashMap<>();

		// Call Each System
		for (RecordServiceEnum service : RecordServiceEnum.values()) {
			String url = AllReferencesGet.serviceUrl(service);

			ChangedReferencesGetRequest q = new ChangedReferencesGetRequest();
			q.max = req.max;
			String split[] = req.lastid.split("-");
			String lastid = split[0];
			if (service.ordinal() > Integer.valueOf(split[1]))
				lastid = Utils.formatId(Long.valueOf(lastid) - 1);
			q.lastdate = req.lastdate;
			q.lastid = lastid;
			Future<SwaggerAsyncResponse<ChangedReferencesGetResponse>> future = SwaggerCall
					.callAsync(service.name().toLowerCase()
							+ "-changed-references", Prop.get("/xjus.password"), "GET", url, q,
							ChangedReferencesGetResponse.class);
			map.put(service, future);
		}

		Date dt1 = new Date();

		for (RecordServiceEnum service : RecordServiceEnum.values()) {
			long timeout = AllReferencesGet.TIMEOUT_MILLISECONDS
					- ((new Date()).getTime() - dt1.getTime());
			if (timeout < 0L)
				timeout = 0;
			SwaggerAsyncResponse<ChangedReferencesGetResponse> futureresponse = map
					.get(service).get(timeout, TimeUnit.MILLISECONDS);
			ChangedReferencesGetResponse o = (ChangedReferencesGetResponse) futureresponse
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
				int i = o1.date.compareTo(o2.date);
				if (i != 0)
					return i;
				return o1.id.compareTo(o2.id);
			}
		});

		// Drop items that exceed the maximum size
		while (resp.list.size() > Integer.valueOf(req.max))
			resp.list.remove(resp.list.size() - 1);
	}

	public String getContext() {
		return "obter a lista de referências";
	}
}
