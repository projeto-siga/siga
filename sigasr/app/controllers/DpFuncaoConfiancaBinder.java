package controllers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import models.SrSelecionavel;

import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;

@Global
public class DpFuncaoConfiancaBinder implements TypeBinder<DpFuncaoConfianca> {

	@Override
	public Object bind(String name, Annotation[] anns, String value,
			Class clazz, Type arg4) throws Exception {
		if (value != null && !value.equals(""))
			return JPA.em().find(DpFuncaoConfianca.class, Long.valueOf(value));
		return null;
	}

}
