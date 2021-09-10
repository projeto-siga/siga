package br.gov.jfrj.siga.model;

import br.gov.jfrj.siga.base.AplicacaoException;

public class SelecaoGenerica extends Selecao {

	@Override
	public String getAcaoBusca() {
		return null;
	}

	@Override
	public boolean buscarPorId() throws AplicacaoException {
		return false;
	}

	@Override
	public Selecionavel buscarObjeto() throws AplicacaoException {
		return null;
	}

	@Override
	public boolean buscarPorSigla() throws AplicacaoException {
		return false;
	}
}