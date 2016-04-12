/*******************************************************************************
 * Copyright (c) 2006 - 2015 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.gsa;
import java.io.ByteArrayOutputStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.enterprise.adaptor.DocId;

public class ExDocumentoAdaptorTest {

	@Ignore
	@Test
	public void testGetDocIds() throws Exception {
		ExDocumentoAdaptor da = new ExDocumentoAdaptor();
		da.init(new MockAdaptorContext());

		MockDocIdPusher p = new MockDocIdPusher();
		da.getDocIds(p);

		assertNotNull(p.getLastDocId());
	}

	@Ignore
	@Test
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
