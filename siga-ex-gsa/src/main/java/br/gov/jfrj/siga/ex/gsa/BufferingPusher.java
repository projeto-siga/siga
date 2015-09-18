package br.gov.jfrj.siga.ex.gsa;

import java.util.ArrayList;
import java.util.Collection;
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
	private int maxIdsPerFeedFile = 10000;
	private static final Logger log = Logger.getLogger(BufferingPusher.class
			.getName());

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
		log.fine("sent " + saved.size() + " doc ids to pusher");
		saved.clear();
	}

	protected void finalize() throws Throwable {
		if (0 != saved.size()) {
			log.severe("still have saved ids that weren't sent");
		}
	}
}
