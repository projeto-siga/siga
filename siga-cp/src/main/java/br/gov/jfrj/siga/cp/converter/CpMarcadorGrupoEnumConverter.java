package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;

@Converter(autoApply = true)
public class CpMarcadorGrupoEnumConverter extends EnumWithIdConverter<CpMarcadorGrupoEnum> {

}