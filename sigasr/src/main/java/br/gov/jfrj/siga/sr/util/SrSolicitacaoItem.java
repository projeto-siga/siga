package br.gov.jfrj.siga.sr.util;

public class SrSolicitacaoItem implements Comparable<SrSolicitacaoItem> {

	public long mes;
	public long ano;
	public long abertos;
	public long fechados;
	public long outros;
	
	public SrSolicitacaoItem(long mes, long ano, long abertos,
			long fechados, long outros) {
		super();
		this.mes = mes;
		this.ano = ano;
		this.abertos = abertos;
		this.fechados = fechados;
		this.outros = outros;
	}

	@Override
	public int compareTo(SrSolicitacaoItem o) {
		int i = 0;
		i = Long.valueOf(ano).compareTo(o.ano);
		if (i != 0)
			return i;
		i = Long.valueOf(mes).compareTo(o.mes);
		if (i != 0)
			return i;
		return 0;
	}

	public void somar(SrSolicitacaoItem o) {
		this.abertos += o.abertos;
		this.fechados += o.fechados;
		this.outros += o.outros;
	}
}