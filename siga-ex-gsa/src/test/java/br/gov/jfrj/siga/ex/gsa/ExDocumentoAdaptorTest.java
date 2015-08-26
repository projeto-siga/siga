package br.gov.jfrj.siga.ex.gsa;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import br.gov.jfrj.siga.cp.bl.CpPropriedadeBL;
import br.gov.jfrj.siga.ex.bl.Ex;

import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;

public class ExDocumentoAdaptorTest extends TestCase {

	public void testGetDocIds() throws Exception {
		ExDocumentoAdaptor da = new ExDocumentoAdaptor();
		da.init(new MockAdaptorContext());

		MockDocIdPusher p = new MockDocIdPusher();
		da.getDocIds(p);

		assertNotNull(p.getLastDocId());
	}

	public void testGetDocContent() throws Exception {
		ExDocumentoAdaptor da = new ExDocumentoAdaptor();
		da.init(new MockAdaptorContext());

		MockDocIdPusher p = new MockDocIdPusher();
		da.getDocIds(p);

		final DocId docId = p.getLastDocId();

		MockRequest req = new MockRequest();
		req.setDocId(docId);

		MockResponse resp = new MockResponse();
		da.getDocContent(req, resp);

		// Tem um PDF
		assertNotNull(((ByteArrayOutputStream) resp.getOutputStream())
				.toByteArray());
	}
}
