package br.gov.jfrj.siga.model.dao.extended;

import static br.gov.jfrj.siga.model.dao.extended.QueryDslExtendedThreadScopedEntityManager.getScopedManager;
import static java.util.logging.Level.FINEST;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.persistence.EntityTransaction;

public class TxManager {

	private static final Logger log = Logger.getLogger(TxManager.class.getCanonicalName());

	static boolean debugLogging = false;
	static boolean longTxDebugLogging = false;
	static long longTxThreshold = 400l;

	private TxManager() {
	}

	/*
	 * Low-level data-access levels.
	 */

	private static <R> R execute(Function<QueryDslExtendedThreadScopedEntityManager, R> executor, boolean openTx) {
		TxTracer tracer = new TxTracer();
		try (QueryDslExtendedThreadScopedEntityManager manager = getScopedManager()) {
			EntityTransaction tx = manager.getTransaction();

			// Handling nested transactions: NULL means ALREADY NOT DEFINED.
			boolean txNested = false;
			if (openTx) {
				if (tx.isActive()) {
					txNested = true;
					if (debugLogging) {
						log.finest("Nested transaction detected: active transaction commit will only occur on last execution.");
					}
				} else {
					tx.begin();
					if (debugLogging) {
						log.finest("New database transactional operation detected.");
					}
				}
			}
			
			try {
				// Executing database scoped operation.
				R result = executor.apply(manager);
				// Handling transaction commitment.
				if (openTx && tx.isActive()) {
					if (txNested) {
						// Flushing changes on nested transactional methods.
						manager.flush();
						if (debugLogging) {
							log.finest("Flushing inner method transactional changes.");
						}
					} else {
						// If not nested (e.g.: outer transactional method), commit ending transaction.
						tx.commit();
						if (debugLogging) {
							log.finest("Ending database transactional operation: changes were commited.");
						}
					}
				}
				long elapsedTimeMillis = tracer.elapsedTime();
				if (longTxDebugLogging && elapsedTimeMillis >= longTxThreshold) {
					log.log(FINEST, "TX OPERATION ELAPSED TIME: " + elapsedTimeMillis + "ms", tracer);
				}
				return result;
			} catch (Throwable e) {
				// Rolling back changes if any.
				if (tx.isActive()) {
					tx.rollback();
					log.warning("Database operation changes were rolled back successfully.");
				}
				// Re-throwing exception.
				throw e;
			}
		}
	}

	public static <R> R execute(Function<QueryDslExtendedThreadScopedEntityManager, R> executor) {
		return execute(executor, false);
	}

	public static <R> R executeInTx(Function<QueryDslExtendedThreadScopedEntityManager, R> executor) {
		return execute(executor, true);
	}

	public static <R> R executeInNewTx(Function<QueryDslExtendedThreadScopedEntityManager, R> executor) {
		log.warning("Feature not yet implemented: using regular DataAccess#executeInTx method.");
		return executeInTx(executor);
	}

	public static void consumeInTx(Consumer<QueryDslExtendedThreadScopedEntityManager> consumer) {
		executeInTx(em -> {
			consumer.accept(em);
			return null;
		});
	}

}
