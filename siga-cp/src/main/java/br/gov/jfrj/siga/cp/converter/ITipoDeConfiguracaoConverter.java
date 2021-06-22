package br.gov.jfrj.siga.cp.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;

@Converter(autoApply = true)
public class ITipoDeConfiguracaoConverter implements AttributeConverter<ITipoDeConfiguracao, Long> {
	@Override
	public Long convertToDatabaseColumn(ITipoDeConfiguracao attribute) {
		if (attribute == null)
			return null;
		return attribute.getId();
	}

	@Override
	public ITipoDeConfiguracao convertToEntityAttribute(Long id) {
		if (id == null)
			return null;
		ITipoDeConfiguracao t = CpTipoDeConfiguracao.getById(id);
		if (t == null)
			throw new RuntimeException("Não encontrei tipo de configuração com id " + id);
		return t;
	}
}