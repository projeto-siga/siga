package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpMarcadorTipoDataEnum;

@Converter(autoApply = true)
public class CpMarcadorTipoDataEnumConverter extends EnumWithIdConverter<CpMarcadorTipoDataEnum> {

}