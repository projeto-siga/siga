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
