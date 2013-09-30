package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.GcAcesso;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.dp.DpPessoa;

@Global
public class GcAcessoBinder implements TypeBinder<GcAcesso> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return JPA.em().find(GcAcesso.class, Long.valueOf(value));
		return null;
	}

}
