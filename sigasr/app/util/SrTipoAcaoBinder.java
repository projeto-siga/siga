package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.SrTipoAcao;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class SrTipoAcaoBinder implements TypeBinder<SrTipoAcao> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals("")) 
			return SrTipoAcao.findById(Long.valueOf(value));
		return null;
	}

}
