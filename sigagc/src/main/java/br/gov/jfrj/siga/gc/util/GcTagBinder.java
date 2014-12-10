package br.gov.jfrj.siga.gc.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.GcTag;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;

@Global
public class GcTagBinder implements TypeBinder<GcTag> {
	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return JPA.em().find(GcTag.class, Long.valueOf(value));
		return null;
	}
}
