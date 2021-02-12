package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;

@Converter(autoApply = true)
public class CpMarcadorFinalidadeEnumConverter extends EnumWithIdConverter<CpMarcadorFinalidadeEnum> {

}