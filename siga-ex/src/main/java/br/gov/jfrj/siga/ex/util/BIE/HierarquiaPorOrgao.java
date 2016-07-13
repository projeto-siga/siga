package br.gov.jfrj.siga.ex.util.BIE;

import java.util.ArrayList;
import java.util.List;

public enum HierarquiaPorOrgao {

	//Edson: o melhor seria que estas hierarquias estivessem no próprio modelo Freemarker,
	//na área que define a parte do layout específica para cada órgão
	
	JFRJ {
		@Override
		List<TopicoMaior> getEstruturaTopicos() {
			List<TopicoMaior> topicos = new ArrayList<TopicoMaior>();
			
			TopicoMaior nodoSede = new TopicoMaior("SEÇÃO JUDICIÁRIA - SEDE",
					Localidade.RIO_DE_JANEIRO);
			for (TipoMateria t : TipoMateria.values())
				nodoSede.addSubTopico(new TopicoMenor(t));
			topicos.add(nodoSede);

			TopicoMaior nodoSubsecoes = new TopicoMaior(
					"PORTARIAS DAS SUBSEÇÕES JUDICIÁRIAS");
			for (Localidade l : Localidade.values())
				nodoSubsecoes.addSubTopico(new TopicoMenor(l));
			topicos.add(nodoSubsecoes);

			return topicos;
		}
	},
	TRF2 {
		@Override
		List<TopicoMaior> getEstruturaTopicos() {
			List<TopicoMaior> topicos = new ArrayList<TopicoMaior>();
			for (TipoMateria t : TipoMateria.values())
				topicos.add(new TopicoMaior(t));
			return topicos;
		}
	};
	abstract List<TopicoMaior> getEstruturaTopicos();
}
