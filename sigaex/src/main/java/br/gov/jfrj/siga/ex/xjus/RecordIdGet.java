package br.gov.jfrj.siga.ex.xjus;

import java.util.concurrent.TimeUnit;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.util.ExXjusRecordServiceEnum;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;
import br.jus.trf2.xjus.record.api.XjusRecordAPIContext;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI.IRecordIdGet.Response;

@AcessoPublico
public class RecordIdGet implements IXjusRecordAPI.IRecordIdGet {

	@Override
	public void run(Request req, Response resp, XjusRecordAPIContext ctx) throws Exception {
		String split[] = req.id.split("-");
		int i = Integer.valueOf(split[1]);
		ExXjusRecordServiceEnum service = ExXjusRecordServiceEnum.values()[i];

		String url = AllReferencesGet.serviceUrl(service);
		url = url.replaceFirst("/record/.+$", "/record/" + split[0]);

		SwaggerAsyncResponse<Response> o = SwaggerCall.callAsync(service.name().toLowerCase() + "-record-id",
				Prop.get("/xjus.password"), "GET", url, null, Response.class)
				.get(AllReferencesGet.TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
		if (o.getException() != null) {
		    if ("REMOVED".equals(o.getException().getMessage()))
		        throw new PresentableUnloggedException("REMOVED");
	        throw o.getException();
		}
		if ("REMOVED".equals(o.getResp().status)) {
		    removed(resp);
		    return;
		}
		resp.acl = o.getResp().acl;
		resp.code = o.getResp().code;
		resp.content = o.getResp().content;
		resp.facet = o.getResp().facet;
		resp.field = o.getResp().field;
		resp.id = o.getResp().id;
		resp.refresh = o.getResp().refresh;
		resp.status = o.getResp().status;
        resp.title = o.getResp().title;
        resp.dateref = o.getResp().dateref;
		resp.url = o.getResp().url;
		resp.id += "-" + service.ordinal();
	}

    private void removed(Response resp) {
        resp.status = "REMOVED";
        CurrentRequest.get().getResponse().setStatus(206);
    }
    
	public String getContext() {
		return "obter a lista de índices";
	}
}
