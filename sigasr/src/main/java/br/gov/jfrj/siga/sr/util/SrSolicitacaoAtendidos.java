package br.gov.jfrj.siga.sr.util;
import java.util.TreeSet;

public class SrSolicitacaoAtendidos extends TreeSet<SrSolicitacaoItem> {

	@Override
	public boolean add(SrSolicitacaoItem e) {
		if (this.contains(e)) {
			this.floor(e).somar(e);
			return false;
		}
		return super.add(e);
	}

}