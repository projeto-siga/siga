package br.gov.jfrj.siga.model;

import java.sql.SQLException;

public class JPAQueryException extends RuntimeException {
	private static final long serialVersionUID = 4805056019406778972L;

	public JPAQueryException(String message) {
		super(message);
	}

	public JPAQueryException(String message, Throwable e) {
		super(message + ": " + e.getMessage(), e);
	}

	public static Throwable findBestCause(Throwable e) {
		Throwable best = e;
		Throwable cause = e;
		int it = 0;
		while ((cause = cause.getCause()) != null && it++ < 10) {
			if (cause instanceof ClassCastException) {
				best = cause;
				break;
			}
			if (cause instanceof SQLException) {
				best = cause;
				break;
			}
		}
		return best;
	}
}