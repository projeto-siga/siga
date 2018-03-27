package br.gov.jfrj.siga.ex.elastic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ExAdaptorTest {

	private static final String LAST_MODIFIED_FILENAME_TEST = "last_modified_test";

	@Test
	public void testGetDateWithSuccess() {
		File file = new File(LAST_MODIFIED_FILENAME_TEST);
		file.delete();
		TestAdaptor adaptor = new TestAdaptor();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date t = new Date();
		String d = dateFormat.format(t);
		Date t2 = adaptor.getLastModified(LAST_MODIFIED_FILENAME_TEST, t);
		String r = dateFormat.format(t2);
		assertEquals(d, r);
		assertTrue(file.delete());
	}

	class TestAdaptor extends ExAdaptor {

		@Override
		public String getIdsHql() {
			return null;
		}

		@Override
		public Response pushItem(Long id) throws IOException {
			return null;
		}

		@Override
		protected String getLastModifiedFileName() {
			return LAST_MODIFIED_FILENAME_TEST;
		}
	}

}
