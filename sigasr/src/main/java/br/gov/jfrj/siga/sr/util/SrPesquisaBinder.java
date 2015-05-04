package br.gov.jfrj.siga.sr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import br.gov.jfrj.siga.sr.model.SrPesquisa;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class SrPesquisaBinder implements TypeBinder<SrPesquisa> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return SrPesquisa.findById(Long.valueOf(value));
		return null;
	}

}
