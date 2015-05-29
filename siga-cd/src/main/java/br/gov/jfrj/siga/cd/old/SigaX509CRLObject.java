/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
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
package br.gov.jfrj.siga.cd.old;

import java.security.cert.CRLException;
import java.util.Date;

import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.jce.provider.X509CRLObject;

public class SigaX509CRLObject extends X509CRLObject {

	public SigaX509CRLObject(CertificateList cl) throws CRLException {
		super(cl);
	}

	@Override
	public Date getThisUpdate() {
		//return new Date(106 , 0, 1);
		return super.getThisUpdate();
	}


	@Override
	public Date getNextUpdate() {
		//return new Date(120 , 0, 1);
		return super.getThisUpdate();
	}
}
