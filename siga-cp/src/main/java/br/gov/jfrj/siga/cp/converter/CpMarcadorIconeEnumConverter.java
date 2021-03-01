package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;

@Converter(autoApply = true)
public class CpMarcadorIconeEnumConverter extends EnumWithIdConverter<CpMarcadorIconeEnum> {

}