package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExDocumento;

public class DocumentoFilhoComparator implements Comparator<ExDocumento> {

	public int compare(ExDocumento o1, ExDocumento o2) {
		if (o1.getNumSequencia() != null && o2.getNumSequencia() != null)
			return o1.getNumSequencia().compareTo(o2.getNumSequencia());
		if (o1.getDtFechamento() != null && o2.getDtFechamento() != null)
			return o1.getDtFechamento().compareTo(o2.getDtFechamento());
		if (o1.getDtRegDoc() != null && o2.getDtRegDoc() != null)
			return o1.getDtRegDoc().compareTo(o2.getDtRegDoc());
		if (o1.getIdDoc() != null && o2.getIdDoc() != null)
			return o1.getIdDoc().compareTo(o2.getIdDoc());
		throw new Error("Não é possivel comparar documentos.");
	}
}
