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

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;

/**
 * Mechanism that accepts stream of DocId instances, bufferes them, and sends
 * them when it has accumulated maximum allowed amount per feed file.
 */
public class BufferingPusher {
	DocIdPusher wrapped;
	ArrayList<DocId> saved;
	private int maxIdsPerFeedFile = 5000;
	private static final Logger log = Logger.getLogger(BufferingPusher.class.getName());

	BufferingPusher(DocIdPusher underlying) {
		wrapped = underlying;
		saved = new ArrayList<DocId>(maxIdsPerFeedFile);
	}

	void add(DocId id) throws InterruptedException {
		saved.add(id);
		if (saved.size() >= maxIdsPerFeedFile) {
			forcePush();
		}
	}

	void forcePush() throws InterruptedException {
		wrapped.pushDocIds(saved);
		log.info("sent " + saved.size() + " doc ids to pusher");
		saved.clear();
	}

	protected void finalize() throws Throwable {
		if (0 != saved.size()) {
			log.severe("still have saved ids that weren't sent");
		}
	}
}
