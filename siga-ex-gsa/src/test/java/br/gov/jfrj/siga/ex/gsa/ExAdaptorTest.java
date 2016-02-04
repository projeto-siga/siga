package br.gov.jfrj.siga.ex.gsa;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

public class ExAdaptorTest {

	@Test
	public void testGetDateWithSuccess() {
		File file = new File("last_modified");
		file.delete();
		TestAdaptor adaptor = new TestAdaptor();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date t = new Date();
		String d = dateFormat.format(t);
		adaptor.getLastModified(t,"last_modified");
		String r = dateFormat.format(adaptor.dateLastUpdated);
		assertEquals(d, r );
		assertTrue(file.delete());
	}
	
	
	
	
	class TestAdaptor extends ExAdaptor{
		@Override
		public void getDocContent(Request arg0, Response arg1) throws IOException, InterruptedException {
		}

		@Override
		public String getIdsHql() {
			return null;
		}

		@Override
		public void getModifiedDocIds(DocIdPusher arg0) throws IOException, InterruptedException {
			// TODO Auto-generated method stub	
		}
	}

}
