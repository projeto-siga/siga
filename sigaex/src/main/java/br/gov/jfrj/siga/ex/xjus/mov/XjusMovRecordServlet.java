package br.gov.jfrj.siga.ex.xjus.mov;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Prop;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;

public class XjusMovRecordServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void initialize(ServletConfig config) throws ServletException {
		setAPI(IXjusRecordAPI.class);

		setActionPackage("br.gov.jfrj.siga.ex.xjus.mov");

		String xjusPassword = null;
		try {
			xjusPassword = Prop.get("/xjus.password");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		if (xjusPassword == null)
			xjusPassword = UUID.randomUUID().toString();
		super.setAuthorization(xjusPassword);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	public String getService() {
		return "siga-doc movement record api";
	}
}
