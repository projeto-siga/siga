package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;

@Converter(autoApply = true)
public class CpTipoMarcadorEnumConverter extends EnumWithIdConverter<CpTipoMarcadorEnum> {

}