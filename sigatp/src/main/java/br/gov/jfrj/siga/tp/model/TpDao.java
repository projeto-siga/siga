package br.gov.jfrj.siga.tp.model;

import java.util.List;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.JPAQuery;
import br.gov.jfrj.siga.model.Objeto;

public class TpDao extends CpDao {

	public static <T extends Objeto> T findById(Class<T> entityClass, Long id) {
		try {
			ActiveRecord<T> ar = new ActiveRecord<T>(entityClass);
			return ar.findById(id);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao recuperar o registro", e);
		}
	}

	public static <T extends Objeto> JPAQuery find(Class<T> entityClass, String query, Object... params) {
		try {
			ActiveRecord<T> ar = new ActiveRecord<T>(entityClass);
			return ar.find(query, params);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao recuperar o registro", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Objeto> List<T> findAll(Class<T> entityClass) {
		ActiveRecord<T> ar = new ActiveRecord<T>(entityClass);
		return ar.findAll();
	}
}
