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
