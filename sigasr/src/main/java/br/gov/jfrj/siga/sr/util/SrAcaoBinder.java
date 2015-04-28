package br.gov.jfrj.siga.sr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import br.gov.jfrj.siga.sr.model.SrAcao;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class SrAcaoBinder implements TypeBinder<SrAcao> {

	@SuppressWarnings("rawtypes")
	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals("")) 
			return SrAcao.findById(Long.valueOf(value));
		return null;
	}

}
