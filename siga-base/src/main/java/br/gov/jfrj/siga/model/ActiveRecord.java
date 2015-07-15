package br.gov.jfrj.siga.model;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.jfrj.siga.model.Objeto;

public class ActiveRecord<T extends Objeto> {
	private Class<? extends Objeto> clazz;

	public ActiveRecord(Class<? extends Objeto> clazz) {
		this.clazz = clazz;
	}

	public EntityManager em() {
		return ContextoPersistencia.em();
	}

	public long count() {
		return Long.parseLong(em()
				.createQuery(
						"select count(*) from " + clazz.getSimpleName() + " e")
				.getSingleResult().toString());
	}

	public long count(String query, Object[] params) {
		return Long.parseLong(bindParameters(
				em().createQuery(createCountQuery(query, params)), params)
				.getSingleResult().toString());
	}

	public List findAll() {
		return em()
				.createQuery("select e from " + clazz.getSimpleName() + " e")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public T findById(Object id) {
		return (T) em().find(clazz, id);
	}

	public List findBy(String query, Object... params) {
		Query q = em().createQuery(createFindByQuery(query, params));
		return bindParameters(q, params).getResultList();
	}

	public JPAQuery find(String query, Object... params) {
		Query q = em().createQuery(createFindByQuery(query, params));
		return new JPAQuery(createFindByQuery(query, params), bindParameters(q,
				params));
	}

	public JPAQuery find() {
		Query q = em().createQuery(createFindByQuery(null));
		return new JPAQuery(createFindByQuery(null), bindParameters(q));
	}

	public JPAQuery all() {
		Query q = em().createQuery(createFindByQuery(null));
		return new JPAQuery(createFindByQuery(null), bindParameters(q));
	}

	public int delete(String query, Object[] params) {
		Query q = em().createQuery(createDeleteQuery(query, params));
		return bindParameters(q, params).executeUpdate();
	}

	public int deleteAll() {
		Query q = em().createQuery(createDeleteQuery(null));
		return bindParameters(q).executeUpdate();
	}

	public Objeto findOneBy(String query, Object[] params) {
		Query q = em().createQuery(createFindByQuery(query, params));
		List results = bindParameters(q, params).getResultList();
		if (results.size() == 0) {
			return null;
		}
		return (Objeto) results.get(0);
	}

	// public Objeto create(String entity, String name, Params params)
	// throws Exception {
	// Object o =
	// ActiveRecord.class.getClassLoader().loadClass(entity).newInstance();
	//
	// RootParamNode rootParamNode = ParamNode.convert(params.all());
	//
	// return ((GenericModel) o).edit(rootParamNode, name);
	// }

	public String createFindByQuery(String query, Object... params) {
		String entityName = clazz.getSimpleName();
		String entityClass = clazz.getCanonicalName();

		if (query == null || query.trim().length() == 0) {
			return "from " + entityName;
		}
		if (query.matches("^by[A-Z].*$")) {
			return "from " + entityName + " where " + findByToJPQL(query);
		}
		if (query.trim().toLowerCase().startsWith("select ")) {
			return query;
		}
		if (query.trim().toLowerCase().startsWith("from ")) {
			return query;
		}
		if (query.trim().toLowerCase().startsWith("order by ")) {
			return "from " + entityName + " " + query;
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params != null && params.length == 1) {
			query += " = ?1";
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params == null) {
			query += " = null";
		}
		return "from " + entityName + " where " + query;
	}

	public String createDeleteQuery(String query, Object... params) {
		String entityName = clazz.getSimpleName();
		String entityClass = clazz.getCanonicalName();

		if (query == null) {
			return "delete from " + entityName;
		}
		if (query.trim().toLowerCase().startsWith("delete ")) {
			return query;
		}
		if (query.trim().toLowerCase().startsWith("from ")) {
			return "delete " + query;
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params != null && params.length == 1) {
			query += " = ?1";
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params == null) {
			query += " = null";
		}
		return "delete from " + entityName + " where " + query;
	}

	public String createCountQuery(String query, Object... params) {
		String entityName = clazz.getSimpleName();
		String entityClass = clazz.getCanonicalName();

		if (query.trim().toLowerCase().startsWith("select ")) {
			return query;
		}
		if (query.matches("^by[A-Z].*$")) {
			return "select count(*) from " + entityName + " where "
					+ findByToJPQL(query);
		}
		if (query.trim().toLowerCase().startsWith("from ")) {
			return "select count(*) " + query;
		}
		if (query.trim().toLowerCase().startsWith("order by ")) {
			return "select count(*) from " + entityName;
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params != null && params.length == 1) {
			query += " = ?1";
		}
		if (query.trim().indexOf(' ') == -1 && query.trim().indexOf('=') == -1
				&& params == null) {
			query += " = null";
		}
		if (query.trim().length() == 0) {
			return "select count(*) from " + entityName;
		}
		return "select count(*) from " + entityName + " e where " + query;
	}

	@SuppressWarnings("unchecked")
	public Query bindParameters(Query q, Object... params) {
		if (params == null) {
			return q;
		}
		if (params.length == 1 && params[0] instanceof Map) {
			return bindParameters(q, (Map<String, Object>) params[0]);
		}
		for (int i = 0; i < params.length; i++) {
			q.setParameter(i + 1, params[i]);
		}
		return q;
	}

	public Query bindParameters(Query q, Map<String, Object> params) {
		if (params == null) {
			return q;
		}
		for (String key : params.keySet()) {
			q.setParameter(key, params.get(key));
		}
		return q;
	}

	public String findByToJPQL(String findBy) {
		findBy = findBy.substring(2);
		StringBuilder jpql = new StringBuilder();
		String subRequest;
		if (findBy.contains("OrderBy"))
			subRequest = findBy.split("OrderBy")[0];
		else
			subRequest = findBy;
		String[] parts = subRequest.split("And");
		int index = 1;
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.endsWith("NotEqual")) {
				String prop = extractProp(part, "NotEqual");
				jpql.append(prop).append(" <> ?").append(index++);
			} else if (part.endsWith("Equal")) {
				String prop = extractProp(part, "Equal");
				jpql.append(prop).append(" = ?").append(index++);
			} else if (part.endsWith("IsNotNull")) {
				String prop = extractProp(part, "IsNotNull");
				jpql.append(prop).append(" is not null");
			} else if (part.endsWith("IsNull")) {
				String prop = extractProp(part, "IsNull");
				jpql.append(prop).append(" is null");
			} else if (part.endsWith("LessThan")) {
				String prop = extractProp(part, "LessThan");
				jpql.append(prop).append(" < ?").append(index++);
			} else if (part.endsWith("LessThanEquals")) {
				String prop = extractProp(part, "LessThanEquals");
				jpql.append(prop).append(" <= ?").append(index++);
			} else if (part.endsWith("GreaterThan")) {
				String prop = extractProp(part, "GreaterThan");
				jpql.append(prop).append(" > ?").append(index++);
			} else if (part.endsWith("GreaterThanEquals")) {
				String prop = extractProp(part, "GreaterThanEquals");
				jpql.append(prop).append(" >= ?").append(index++);
			} else if (part.endsWith("Between")) {
				String prop = extractProp(part, "Between");
				jpql.append(prop).append(" < ?").append(index++)
						.append(" AND ").append(prop).append(" > ?")
						.append(index++);
			} else if (part.endsWith("Like")) {
				String prop = extractProp(part, "Like");
				// HSQL -> LCASE, all other dbs lower
				if (isHSQL()) {
					jpql.append("LCASE(").append(prop).append(") like ?")
							.append(index++);
				} else {
					jpql.append("LOWER(").append(prop).append(") like ?")
							.append(index++);
				}
			} else if (part.endsWith("Ilike")) {
				String prop = extractProp(part, "Ilike");
				if (isHSQL()) {
					jpql.append("LCASE(").append(prop).append(") like LCASE(?")
							.append(index++).append(")");
				} else {
					jpql.append("LOWER(").append(prop).append(") like LOWER(?")
							.append(index++).append(")");
				}
			} else if (part.endsWith("Elike")) {
				String prop = extractProp(part, "Elike");
				jpql.append(prop).append(" like ?").append(index++);
			} else {
				String prop = extractProp(part, "");
				jpql.append(prop).append(" = ?").append(index++);
			}
			if (i < parts.length - 1) {
				jpql.append(" AND ");
			}
		}
		// ORDER BY clause
		if (findBy.contains("OrderBy")) {
			jpql.append(" ORDER BY ");
			String orderQuery = findBy.split("OrderBy")[1];
			parts = orderQuery.split("And");
			for (int i = 0; i < parts.length; i++) {
				String part = parts[i];
				String orderProp;
				if (part.endsWith("Desc"))
					orderProp = extractProp(part, "Desc") + " DESC";
				else
					orderProp = part.toLowerCase();
				if (i > 0)
					jpql.append(", ");
				jpql.append(orderProp);
			}
		}
		return jpql.toString();
	}

	private boolean isHSQL() {
		return false;
	}

	protected static String extractProp(String part, String end) {
		String prop = part.substring(0, part.length() - end.length());
		prop = (prop.charAt(0) + "").toLowerCase() + prop.substring(1);
		return prop;
	}
}
