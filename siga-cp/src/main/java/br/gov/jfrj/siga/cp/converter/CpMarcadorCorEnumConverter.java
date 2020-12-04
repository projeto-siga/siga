package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpMarcadorCorEnum;

@Converter(autoApply = true)
public class CpMarcadorCorEnumConverter extends EnumWithIdConverter<CpMarcadorCorEnum> {

}