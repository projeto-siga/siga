package br.gov.jfrj.siga.tp.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.sql.DataSource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.TpModel;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;

/**
 * Validador de campo "unique" em entidades. A anotacao {@link Unique} deve ser inserida na entidade o campo a ser validado informado. Monta a consulta dinanicamente realizando um count para verificar
 * a existencia de duplicidade de registro com o campo.
 *
 * @author db1
 *
 */
public class UniqueConstraintValidator implements ConstraintValidator<Unique, TpModel> {
	private static final String QUERY_TEMPLATE = "SELECT count(*) FROM [MODEL_CLASS] t WHERE t.[FIELD] ";

	private static final String _OWNER = "sigatp";
	
	private static Session session = null;
	private static SessionFactoryImpl factory = null;
	private static DatasourceConnectionProviderImpl provider = null;
	private static DataSource dataSource = null;

	private Unique unique;

	@Override
	public void initialize(Unique unique) {
		this.unique = unique;
	}

	@Override
	public boolean isValid(TpModel tpModel, ConstraintValidatorContext context) {

		Connection connection = getConnection();
		
		String sql = criarConsultaParaUnique(tpModel);
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			return contar(statement, tpModel).equals(0L);
		} catch (SQLException e) {
			throw new RuntimeException("Erro no SQL \"" + sql + "\"", e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Long contar(PreparedStatement statement, TpModel tpModel) throws SQLException {
		atribuirParametros(statement, tpModel);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getLong(1);
	}

	private void atribuirParametros(PreparedStatement statement, TpModel tpModel) throws SQLException {
		String field = unique.field();
		if (isUniqueColumn() && new Mirror().on(tpModel).get().field(field) instanceof DpPessoa) {
				DpPessoa tp = (DpPessoa) new Mirror().on(tpModel).get().field(field);
				statement.setObject(1, tp.getId());
				if (isNotNullAndGTZero(tpModel))
					statement.setObject(2, tpModel.getId());
		} else {
			statement.setObject(1, new Mirror().on(tpModel).get().field(field));
			if (isNotNullAndGTZero(tpModel))
				statement.setObject(2, tpModel.getId());
		}
	}

	private String criarConsultaParaUnique(TpModel tpModel) {
	    StringBuilder query = new StringBuilder();
	    query.append(QUERY_TEMPLATE.replace("[MODEL_CLASS]", obterNomeTabela(tpModel)));

	    query.append(hasUppercaseAnnotation(getField(tpModel)));

	    StringBuilder queryString = new StringBuilder();
	    queryString.append(query.toString().replace("[FIELD]", obterNomeColuna(tpModel)));

		if (isNotNullAndGTZero(tpModel))
		    queryString.append(" AND t.id != ? ");

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
            return "= UPPER(?)";
        else
            return "= ?";
    }

	private String obterNomeTabela(TpModel tpModel) {
		String retorno = null;
		Table table = tpModel.getClass().getAnnotation(Table.class);
		if (table != null && table.name() != null && !table.name().isEmpty()) {
			retorno = table.name();
		} else {
			retorno = tpModel.getClass().getSimpleName();
		}
		return _OWNER + "." + retorno;
	}

	private Connection getConnection() {

		if(UniqueConstraintValidator.dataSource == null) {
			UniqueConstraintValidator.session = ContextoPersistencia.em().unwrap(Session.class);
			UniqueConstraintValidator.factory = (SessionFactoryImpl) session.getSessionFactory();
			UniqueConstraintValidator.provider = (DatasourceConnectionProviderImpl)factory.getConnectionProvider();
			UniqueConstraintValidator.dataSource = provider.getDataSource();
		}

		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			UniqueConstraintValidator.session = null;
			UniqueConstraintValidator.factory = null;
			UniqueConstraintValidator.provider = null;
			UniqueConstraintValidator.dataSource = null;
			throw new RuntimeException(e);
		}
	}

	private boolean isUniqueColumn() {
		return !"".equals(unique.uniqueColumn());
	}
}