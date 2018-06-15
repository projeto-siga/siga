package br.gov.jfrj.siga.ex.xjus.mov;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.jus.trf2.xjus.record.api.IXjusRecordAPI;

import com.crivano.swaggerservlet.SwaggerServlet;

public class XjusMovRecordServlet extends SwaggerServlet {
	private static final long serialVersionUID = 1756711359239182178L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		super.setAPI(IXjusRecordAPI.class);

		super.setActionPackage("br.gov.jfrj.siga.ex.xjus.mov");

		String xjusPassword = null;
		try {
			xjusPassword = Cp.getInstance().getProp().xjusPassword();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		if (xjusPassword == null)
			xjusPassword = UUID.randomUUID().toString();
		super.setAuthorization(xjusPassword);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

	// @Override
	// public String getService() {
	// return "siga-doc movement record api";
	// }
}
