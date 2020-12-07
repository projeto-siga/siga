package br.gov.jfrj.siga.cp.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.CpMarcadorTipoAplicacaoEnum;

@Converter(autoApply = true)
public class CpMarcadorTipoAplicacaoEnumConverter extends EnumWithIdConverter<CpMarcadorTipoAplicacaoEnum> {

}