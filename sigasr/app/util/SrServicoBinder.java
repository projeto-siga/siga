package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.SrItemConfiguracao;
import models.SrSelecionavel;
import models.SrServico;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;

@Global
public class SrServicoBinder implements TypeBinder<SrServico> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals("")) 
			return SrServico.findById(Long.valueOf(value));
		return null;
	}

}
