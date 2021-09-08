package br.gov.jfrj.siga.cp.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LongNonNullConverter implements AttributeConverter<Long, Long> {
	@Override
	public Long convertToDatabaseColumn(Long attribute) {
		if (attribute == null)
			return 0L;
		return attribute;
	}

	@Override
	public Long convertToEntityAttribute(Long id) {
		return id == null ? 0L : id;
	}
}