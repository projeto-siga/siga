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
}
