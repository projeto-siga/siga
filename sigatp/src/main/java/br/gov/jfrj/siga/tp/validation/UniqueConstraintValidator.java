package br.gov.jfrj.siga.tp.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.model.TpModel;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import net.vidageek.mirror.dsl.Mirror;

/**
 * Validador de campo "unique" em entidades. A anotacao {@link Unique} deve ser inserida na entidade o campo a ser validado informado. Monta a consulta dinanicamente realizando um count para verificar
 * a existencia de duplicidade de registro com o campo.
 *
 * @author db1
 *
 */
public class UniqueConstraintValidator implements ConstraintValidator<Unique, TpModel> {
	private static final String QUERY_TEMPLATE = "SELECT count(t) FROM [MODEL_CLASS] t WHERE t.[FIELD] ";
	private Unique unique;

	@Override
	public void initialize(Unique unique) {
		this.unique = unique;
	}

	@Override
	public boolean isValid(TpModel tpModel, ConstraintValidatorContext context) {
		return true;
		/*		
		String sql = criarConsultaParaUnique(tpModel);
		
		try {
			Query q = ContextoPersistencia.em().createQuery(sql,Long.class);
			return contar(q, tpModel).equals(0L);
		} catch (SQLException e) {
			throw new RuntimeException("Erro no SQL \"" + sql + "\"", e);
		} */
	}

	private Long contar(Query q, TpModel tpModel) throws SQLException {
		atribuirParametros(q, tpModel);
		return  (Long) q.getSingleResult();
	}

	private void atribuirParametros(Query q, TpModel tpModel) throws SQLException {
		String field = unique.field();
		if (isUniqueColumn() && new Mirror().on(tpModel).get().field(field) instanceof DpPessoa) {
				DpPessoa tp = (DpPessoa) new Mirror().on(tpModel).get().field(field);
				q.setParameter(field, tp.getId());
				if (isNotNullAndGTZero(tpModel))
					q.setParameter("id", tpModel.getId());
		} else {
			q.setParameter(field, new Mirror().on(tpModel).get().field(field));
			if (isNotNullAndGTZero(tpModel))
				q.setParameter("id", tpModel.getId());
		}
	}

	private String criarConsultaParaUnique(TpModel tpModel) {
	    StringBuilder query = new StringBuilder();
	    query.append(QUERY_TEMPLATE.replace("[MODEL_CLASS]", obterNomeTabela(tpModel)));

	    query.append(hasUppercaseAnnotation(getField(tpModel)));

	    StringBuilder queryString = new StringBuilder();
	    queryString.append(query.toString().replace("[FIELD]", obterNomeColuna(tpModel)));

		if (isNotNullAndGTZero(tpModel))
		    queryString.append(" AND t.id != :id ");

		return queryString.toString();
	}

	private boolean isNotNullAndGTZero(TpModel tpModel) {
	    return tpModel.getId() != null && tpModel.getId() > 0;
	}

	private CharSequence obterNomeColuna(TpModel tpModel) {
		if(isUniqueColumn())
			return unique.uniqueColumn();

		Field field = getField(tpModel);
		Column column = field.getAnnotation(Column.class);

		if (column != null && column.name() != null && !column.name().isEmpty()) {
			return column.name();
		}
		return unique.field();
	}

    private Field getField(TpModel tpModel) {
        return new Mirror().on(tpModel.getClass()).reflect().field(unique.field());
    }

    private String hasUppercaseAnnotation(Field field) {
        boolean found = false;
        for (Annotation annotation : Arrays.asList(field.getDeclaredAnnotations())) {
		    if(annotation instanceof UpperCase) {
		        found = true;
		        break;
		    }
        }

        if(found)
            return "= UPPER(:" + field.getName() + ")";
        else
            return "= :" + field.getName();
    }

	private String obterNomeTabela(TpModel tpModel) {
		String retorno = null;
		Table table = tpModel.getClass().getAnnotation(Table.class);
		if (table != null && table.name() != null && !table.name().isEmpty()) {
			retorno = table.name();
		} else {
			retorno = tpModel.getClass().getSimpleName();
		}
		return retorno;
	}


	private boolean isUniqueColumn() {
		return !"".equals(unique.uniqueColumn());
	}
}