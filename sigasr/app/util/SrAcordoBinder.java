package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.SrAcordo;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class SrAcordoBinder implements TypeBinder<SrAcordo> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return SrAcordo.findById(Long.valueOf(value));
		return null;
	}

}
