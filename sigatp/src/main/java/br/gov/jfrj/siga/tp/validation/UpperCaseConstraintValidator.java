package br.gov.jfrj.siga.tp.validation;

import java.lang.reflect.Field;

import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

@SuppressWarnings("serial")
public class UpperCaseConstraintValidator extends AbstractAnnotationCheck<UpperCase> {
	@Override
	public boolean isSatisfied(Object validatedObject, Object value, OValContext context, Validator validator) throws OValException {
		this.setMessage("Campo invalido.");

		try {
			Field campo = ((FieldContext) context).getField();

			if (!campo.isAnnotationPresent(UpperCase.class)) {
				return false;
			}

			if (!campo.getType().getName().equals("java.lang.String")) {
				return true;
			}

			Field field = validatedObject.getClass().getDeclaredField(campo.getName());
			field.setAccessible(true);

			if (value == null) {
				return true;
			}

			field.set(validatedObject, value.toString().toUpperCase());

		} catch (Exception e) {
			return false;
		}
		return true;
	}
}