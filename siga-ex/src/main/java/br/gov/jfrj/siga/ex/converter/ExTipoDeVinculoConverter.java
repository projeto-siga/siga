package br.gov.jfrj.siga.ex.converter;

import javax.persistence.Converter;

import br.gov.jfrj.siga.cp.converter.EnumWithIdConverter;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeVinculo;

@Converter(autoApply = true)
public class ExTipoDeVinculoConverter extends EnumWithIdConverter<ExTipoDeVinculo> {

}