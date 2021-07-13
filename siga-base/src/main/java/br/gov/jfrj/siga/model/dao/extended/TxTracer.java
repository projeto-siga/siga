package br.gov.jfrj.siga.model.dao.extended;

import static java.lang.System.currentTimeMillis;

public class TxTracer extends Throwable {

	private static final long serialVersionUID = -3378480992565972840L;

	private long startMillis;

	public TxTracer() {
		super("TxManager execution tracer - NOT an exception!");
		startMillis = currentTimeMillis();
	}

	public long elapsedTime() {
		return currentTimeMillis() - startMillis;
	}

}
