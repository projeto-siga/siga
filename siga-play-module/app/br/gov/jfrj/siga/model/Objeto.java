package br.gov.jfrj.siga.model;

import org.hibernate.proxy.HibernateProxy;

public class Objeto extends ObjetoBase{

	@Override
	public String toString() {
		return ClassUtils.toString(this);
	}

	public boolean equals(Object obj) {
		return this == getImplementation(obj);
	}

	public static Object getImplementation(Object obj) {
		if (obj instanceof HibernateProxy) {
			return ((HibernateProxy) obj).getHibernateLazyInitializer()
					.getImplementation();
		} else {
			return obj;
		}
	}

	public Object getImplementation() {
		if (this instanceof HibernateProxy) {
			return ((HibernateProxy) this).getHibernateLazyInitializer()
					.getImplementation();
		} else {
			return this;
		}
	}

	public boolean isInstance(Class clazz) {
		return this.getClass().isAssignableFrom(clazz);
	}

	public Objeto unproxy() {
		return this;
	}

	public Objeto unproxyIfInstance(Class clazz) {
		return isInstance(clazz) ? this : null;
	}

}
