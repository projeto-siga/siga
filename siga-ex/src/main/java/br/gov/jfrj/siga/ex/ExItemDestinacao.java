package br.gov.jfrj.siga.ex;

public class ExItemDestinacao {

	private ExMobil mob;

	private ExMarca mar;
	
	private ExItemDestinacao(){
		
	}

	public ExItemDestinacao(Object[] dupla) {
		this.mob = (ExMobil) dupla[0];
		this.mar = (ExMarca) dupla[1];
	}

	public ExMobil getMob() {
		return mob;
	}

	public ExMarca getMarca() {
		return mar;
	}

}
