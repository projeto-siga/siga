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
package br.gov.jfrj.siga.cd;

import java.util.Date;
import java.util.HashMap;

public class CRLCache {
	private static final long ONE_DAY_IN_MILISECONDS = (1000L * 60L * 60L * 24L);
	static HashMap<String, CRLCacheItem> hm = new HashMap<String, CRLCacheItem>();

	public static byte[] getCRL(String uri) {
		CRLCacheItem ci = hm.get(uri);
		// Older than one day
		if (ci == null
				|| ci.dtLastUpdate.getTime() < (new Date().getTime() - ONE_DAY_IN_MILISECONDS))
			return null;
		return ci.crl;
	}

	public static void putCRL(String uri, byte[] crl) {
		synchronized (CRLCache.class) {
			hm.put(uri, new CRLCacheItem(uri, new Date(), crl));
		}
	}
}

class CRLCacheItem {
	String uri;
	Date dtLastUpdate;
	byte crl[];

	public CRLCacheItem(String uri, Date dtLastUpdate, byte[] crl) {
		super();
		this.uri = uri;
		this.dtLastUpdate = dtLastUpdate;
		this.crl = crl;
	}

}
