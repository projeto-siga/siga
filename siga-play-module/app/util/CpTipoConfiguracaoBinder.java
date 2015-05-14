package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;

@Global
public class CpTipoConfiguracaoBinder implements TypeBinder<CpTipoConfiguracao> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return JPA.em().find(CpTipoConfiguracao.class, Long.valueOf(value));
		return null;
	}

}
