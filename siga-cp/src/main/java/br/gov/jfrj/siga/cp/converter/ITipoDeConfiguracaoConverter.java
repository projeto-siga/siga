package br.gov.jfrj.siga.cp.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;

@Converter(autoApply = true)
public class ITipoDeConfiguracaoConverter implements AttributeConverter<ITipoDeConfiguracao, Integer> {
	@Override
	public Integer convertToDatabaseColumn(ITipoDeConfiguracao attribute) {
		if (attribute == null)
			return null;
		return attribute.getId();
	}

	@Override
	public ITipoDeConfiguracao convertToEntityAttribute(Integer id) {
		if (id == null)
			return null;
		ITipoDeConfiguracao t = CpTipoDeConfiguracao.getById(id);
		
		// Nato: Tive que desabilitar esse teste porque o hibernate estava carregando
		// configurações extras em batch, e aí vinham configurações que não são do
		// escopo do módulo em questão.
		//
		// if (t == null)
		// throw new RuntimeException("Não encontrei tipo de configuração com id " +
		// id);
		//
		// substituí pela geração de um tipo fake, só para não produzir um registro
		// inválido
		if (t == null)
			t = new TipoDeConfiguracaoFake(id);
		
		return t;
	}

	private static class TipoDeConfiguracaoFake implements ITipoDeConfiguracao {
		int id;

		public TipoDeConfiguracaoFake(int id) {
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

		@Override
		public String getExplicacao() {
			return null;
		}

		@Override
		public boolean isEditavel() {
			return false;
		}

		@Override
		public CpSituacaoDeConfiguracaoEnum getSituacaoDefault() {
			return null;
		}

		@Override
		public CpSituacaoDeConfiguracaoEnum[] getSituacoes() {
			return null;
		}

		@Override
		public Enum[] getParams() {
			return null;
		}

		@Override
		public Enum[] getObrigatorios() {
			return null;
		}

	}
}