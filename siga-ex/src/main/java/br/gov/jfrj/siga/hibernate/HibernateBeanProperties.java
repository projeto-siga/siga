package br.gov.jfrj.siga.hibernate;

import java.util.Set;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.mapping.Collection;
import org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.PropertyAccess;

public class HibernateBeanProperties {

	public static void setQueryProperties(Query query, Object bean) {
		Class clazz = bean.getClass();
		Set<Parameter<?>> params = query.getParameters();
		for (Parameter<?> namedParam : params) {
			try {
				final PropertyAccess propertyAccess = BuiltInPropertyAccessStrategies.BASIC.getStrategy()
						.buildPropertyAccess(clazz, namedParam.getName());
				final Getter getter = propertyAccess.getGetter();
				final Class retType = getter.getReturnType();
				final Object object = getter.get(bean);
				if (Collection.class.isAssignableFrom(retType)) {
					query.setParameter(namedParam.getName(), (Collection) object);
				} else if (retType.isArray()) {
					query.setParameter(namedParam.getName(), (Object[]) object);
				} else {
					query.setParameter(namedParam.getName(), object);
				}
			} catch (PropertyNotFoundException pnfe) {
				// ignore
			}
		}
	}

}
