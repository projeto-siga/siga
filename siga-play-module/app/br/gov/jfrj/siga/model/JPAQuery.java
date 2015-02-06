package br.gov.jfrj.siga.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

/**
 * A JPAQuery
 */
public class JPAQuery {

	public Query query;
	public String sq;

	public JPAQuery(String sq, Query query) {
		this.query = query;
		this.sq = sq;
	}

	public JPAQuery(Query query) {
		this.query = query;
		this.sq = query.toString();
	}

	public <T> T first() {
		try {
			List<T> results = query.setMaxResults(1).getResultList();
			if (results.isEmpty()) {
				return null;
			}
			return results.get(0);
		} catch (Exception e) {
			throw new JPAQueryException("Error while executing query <strong>"
					+ sq + "</strong>", JPAQueryException.findBestCause(e));
		}
	}

	/**
	 * Bind a JPQL named parameter to the current query. Careful, this will also
	 * bind count results. This means that Integer get transformed into long so
	 * hibernate can do the right thing. Use the setParameter if you just want
	 * to set parameters.
	 */
	public JPAQuery bind(String name, Object param) {
		if (param.getClass().isArray()) {
			param = Arrays.asList((Object[]) param);
		}
		if (param instanceof Integer) {
			param = ((Integer) param).longValue();
		}
		query.setParameter(name, param);
		return this;
	}

	/**
	 * Set a named parameter for this query.
	 **/
	public JPAQuery setParameter(String name, Object param) {
		query.setParameter(name, param);
		return this;
	}

	/**
	 * Retrieve all results of the query
	 * 
	 * @return A list of entities
	 */
	public <T> List<T> fetch() {
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAQueryException("Error while executing query <strong>"
					+ sq + "</strong>", JPAQueryException.findBestCause(e));
		}
	}

	/**
	 * Retrieve results of the query
	 * 
	 * @param max
	 *            Max results to fetch
	 * @return A list of entities
	 */
	public <T> List<T> fetch(int max) {
		try {
			query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAQueryException("Error while executing query <strong>"
					+ sq + "</strong>", JPAQueryException.findBestCause(e));
		}
	}

	/**
	 * Set the position to start
	 * 
	 * @param position
	 *            Position of the first element
	 * @return A new query
	 */
	public <T> JPAQuery from(int position) {
		query.setFirstResult(position);
		return this;
	}

	/**
	 * Retrieve a page of result
	 * 
	 * @param page
	 *            Page number (start at 1)
	 * @param length
	 *            (page length)
	 * @return a list of entities
	 */
	public <T> List<T> fetch(int page, int length) {
		if (page < 1) {
			page = 1;
		}
		query.setFirstResult((page - 1) * length);
		query.setMaxResults(length);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new JPAQueryException("Error while executing query <strong>"
					+ sq + "</strong>", JPAQueryException.findBestCause(e));
		}
	}
}