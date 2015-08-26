package br.gov.jfrj.siga.ex.gsa;
import java.util.Date;

import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.Request;

public class MockRequest implements Request {
	private DocId docId;

	@Override
	public boolean canRespondWithNoContent(Date arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date getLastAccessTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChangedSinceLastAccess(Date arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DocId getDocId() {
		return docId;
	}

	public void setDocId(DocId docId) {
		this.docId = docId;
	}

}
