package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpMarcadorTipoInteressadoEnum;

@Converter(autoApply = true)
public class CpMarcadorTipoInteressadoEnumConverter extends EnumWithIdConverter<CpMarcadorTipoInteressadoEnum> {

}