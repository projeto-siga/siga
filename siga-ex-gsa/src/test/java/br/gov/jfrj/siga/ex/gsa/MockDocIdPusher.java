package br.gov.jfrj.siga.ex.gsa;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.enterprise.adaptor.Acl;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.ExceptionHandler;
import com.google.enterprise.adaptor.GroupPrincipal;
import com.google.enterprise.adaptor.Principal;

public class MockDocIdPusher implements DocIdPusher {
	private DocId lastDocId;

	public DocId pushDocIds(Iterable<DocId> list) throws InterruptedException {
		Iterator<DocId> i = list.iterator();
		while (i.hasNext())
			lastDocId = i.next();
		return lastDocId;
	}

	public DocId getLastDocId() {
		return lastDocId;
	}

	public DocId pushDocIds(Iterable<DocId> arg0, ExceptionHandler arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public GroupPrincipal pushGroupDefinitions(
			Map<GroupPrincipal, ? extends Collection<Principal>> arg0,
			boolean arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public GroupPrincipal pushGroupDefinitions(
			Map<GroupPrincipal, ? extends Collection<Principal>> arg0,
			boolean arg1, ExceptionHandler arg2) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public DocId pushNamedResources(Map<DocId, Acl> arg0)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public DocId pushNamedResources(Map<DocId, Acl> arg0, ExceptionHandler arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record pushRecords(Iterable<Record> arg0)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	public Record pushRecords(Iterable<Record> arg0, ExceptionHandler arg1)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

}
