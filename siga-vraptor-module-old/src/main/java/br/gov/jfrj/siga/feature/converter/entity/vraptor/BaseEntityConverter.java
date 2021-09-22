package br.gov.jfrj.siga.feature.converter.entity.vraptor;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import br.com.caelum.vraptor.converter.Converter;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class BaseEntityConverter<T> implements Converter<T> {

	public static final Long ID_VAZIO = 0L;
	
	@Override
	public T convert(String idString, Class<? extends T> type) {
		if (idString != null && !idString.isEmpty()) {
			Long id = Long.valueOf(idString);

			if (existe(id))
				return buscarRegistro(type, id);
		}
		return novaInstancia(type);
	}

	private T buscarRegistro(Class<? extends T> type, Long id) {
		return ContextoPersistencia
					.em()
					.find(type, Long.valueOf(id));
	}

	private T novaInstancia(Class<? extends T> type) {
		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(MessageFormat.format("Erro ao instanciar objeto do tipo {0}. Existe constutor padrao?", type), e);
		}
	}
	
	public boolean existe(Long id) {
		return id != null && !ID_VAZIO.equals(id);
	}
}
