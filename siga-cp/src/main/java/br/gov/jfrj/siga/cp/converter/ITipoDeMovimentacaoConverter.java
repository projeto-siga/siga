package br.gov.jfrj.siga.cp.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpTipoDeMovimentacao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;

@Converter(autoApply = true)
public class ITipoDeMovimentacaoConverter implements AttributeConverter<ITipoDeMovimentacao, Integer> {
	@Override
	public Integer convertToDatabaseColumn(ITipoDeMovimentacao attribute) {
		if (attribute == null)
			return null;
		return attribute.getId();
	}

	@Override
	public ITipoDeMovimentacao convertToEntityAttribute(Integer id) {
		if (id == null)
			return null;
		ITipoDeMovimentacao t = CpTipoDeMovimentacao.getById(id);

		if (t == null)
			t = new TipoDeMovimentacaoFake(id);

		return t;
	}

	private static class TipoDeMovimentacaoFake implements ITipoDeMovimentacao {
		int id;

		public TipoDeMovimentacaoFake(int id) {
			this.id = id;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public String getDescr() {
			return "Tipo Fake";
		}
	}
}