package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpMarcadorTipoTextoEnum;

@Converter(autoApply = true)
public class CpMarcadorTipoTextoEnumConverter extends EnumWithIdConverter<CpMarcadorTipoTextoEnum> {

}