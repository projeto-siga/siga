package br.gov.jfrj.siga.cp.converter;

import java.io.Serializable;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;

@Converter(autoApply = true)
public class CpSituacaoDeConfiguracaoEnumConverter extends EnumWithIdConverter<CpSituacaoDeConfiguracaoEnum> implements Serializable {
//	private static final long serialVersionUID = -6909898259499031118L;
//
//	@Override
//	public Integer convertToDatabaseColumn(CpSituacaoDeConfiguracaoEnum attribute) {
//		if (attribute == null)
//			return null;
//		return attribute != null ? attribute.getId() : null;
//	}
//
//	@Override
//	public CpSituacaoDeConfiguracaoEnum convertToEntityAttribute(Integer id) {
//		return CpSituacaoDeConfiguracaoEnum.getById(id);
//	}
//
}