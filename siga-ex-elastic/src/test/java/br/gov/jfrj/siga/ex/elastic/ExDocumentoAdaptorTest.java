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
package br.gov.jfrj.siga.ex.elastic;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.junit.Ignore;
import org.junit.Test;

import br.gov.jfrj.siga.hibernate.ExDao;

public class ExDocumentoAdaptorTest {

	// @Ignore
	@Test
	public void testGetAllItems() throws Exception {
		ExDocumentoAdaptor da = new ExDocumentoAdaptor();
		da.pushAllItems();
		assertNotNull(da.dateLastUpdated);
	}

	@Ignore
	@Test
	public void testGetDocContent() throws Exception {
		ExDocumentoAdaptor da = new ExDocumentoAdaptor();

		ExDao dao = ExDao.getInstance();
		Query q = dao.getSessao().createQuery(da.getIdsHql());
		q.setDate("dt", new Date(0L));
		@SuppressWarnings("rawtypes")
		Iterator i = q.iterate();
		while (i.hasNext()) {
			Long id = (Long) i.next();
			Response resp = new Response();
			try {
				resp = da.pushItem(id);
				da.updateIndex(id, resp);
				// Tem um PDF
				assertNotNull(resp.content);
				return;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
