package br.gov.jfrj.siga.cp.converter;

import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EnumWithIdConverter<E extends IEnumWithId> implements AttributeConverter<E, Integer> {

	@Override
	public Integer convertToDatabaseColumn(E attribute) {
		if (attribute == null)
			return null;
		return attribute != null ? attribute.getId() : null;
	}

	@Override
	public E convertToEntityAttribute(Integer id) {
		Class<E> clazz = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		return IEnumWithId.getEnumFromId(id, clazz);
	}
}