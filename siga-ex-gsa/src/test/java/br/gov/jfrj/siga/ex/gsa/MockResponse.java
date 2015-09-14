package br.gov.jfrj.siga.ex.gsa;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;

import com.google.enterprise.adaptor.Acl;
import com.google.enterprise.adaptor.Response;

public class MockResponse implements Response {

	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	@Override
	public void addAnchor(URI arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMetadata(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.baos;
	}

	@Override
	public void putNamedResource(String arg0, Acl arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void respondNoContent() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void respondNotFound() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void respondNotModified() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAcl(Acl arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCrawlOnce(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisplayUrl(URI arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastModified(Date arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLock(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoArchive(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoFollow(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoIndex(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSecure(boolean arg0) {
		// TODO Auto-generated method stub

	}

}
