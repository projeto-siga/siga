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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassUtils {

	private ClassUtils() {
	}

	public static String toString(Object o) {
		return "";
	}

	private static void toString(Object o, Class clazz, List list) {
		/*Field f[] = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(f, true);
		for (int i = 0; i < f.length; i++) {
			if (!f[i].getName().equals("grPai")) {
//				try {
//					list.add(f[i].getName() + "=" + f[i].get(o));
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
			}
		}
		if (clazz.getSuperclass().getSuperclass() != null) {
			System.out.println("*** Classe: " + clazz.getName() + " - " + clazz.getSuperclass().getName());
			toString(o, clazz.getSuperclass(), list);
		}*/
	}

	public static String printList(List l) {
		Iterator i = l.iterator();
		if (!i.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (;;) {
			Object e = i.next();
			sb.append(e == l ? "(this Collection)" : e);
			if (!i.hasNext())
				return sb.append("]\n").toString();
			sb.append(", ");
		}
	}

}
