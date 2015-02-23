package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.SrObjetivoAtributo;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class SrObjetivoAtributoBinder implements TypeBinder<SrObjetivoAtributo>{

	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return SrObjetivoAtributo.findById(Long.valueOf(value));
		
		return null;
	}

}
