/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
