package br.gov.jfrj.siga.vraptor;

import java.util.Set;

import br.gov.jfrj.siga.dp.DpLotacao;

public class ListaLotPubl {

	private Set<DpLotacao> lotacoes;
	private Long idLotDefault;

	public ListaLotPubl(Set<DpLotacao> lotacoes, Long idLotDefault) {
		super();
		this.lotacoes = lotacoes;
		this.idLotDefault = idLotDefault;
	}

	public Set<DpLotacao> getLotacoes() {
		return lotacoes;
	}

	public Long getIdLotDefault() {
		return idLotDefault;
	}
}