package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class CpUnidadeMedidaBinder implements TypeBinder<CpUnidadeMedida> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return CpUnidadeMedida.findById(Long.valueOf(value));
		return null;
	}

}
