package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoTextoEnum;

@Converter(autoApply = true)
public class CpMarcadorTipoTextoEnumConverter extends EnumWithIdConverter<CpMarcadorTipoTextoEnum> {

}