package models.vo;

import models.SrLista;
import models.SrPrioridade;

public class ListaInclusaoAutomatica {

	private SrLista lista;
	private SrPrioridade prioridadeNaLista;

	public ListaInclusaoAutomatica(SrLista lista, SrPrioridade prioridadeNaLista) {
		super();
		this.lista = lista;
		this.prioridadeNaLista = prioridadeNaLista;
	}

	public SrLista getLista() {
		return lista;
	}

	public SrPrioridade getPrioridadeNaLista() {
		return prioridadeNaLista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lista == null) ? 0 : lista.hashCode());
		result = prime
				* result
				+ ((prioridadeNaLista == null) ? 0 : prioridadeNaLista
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ListaInclusaoAutomatica other = (ListaInclusaoAutomatica) obj;
		if (lista == null) {
			if (other.lista != null)
				return false;
		} else if (!lista.equivale(other.lista))
			return false;
		if (prioridadeNaLista != other.prioridadeNaLista)
			return false;
		return true;
	}
}
