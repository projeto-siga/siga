package br.gov.jfrj.siga.cp.arquivo;

import br.gov.jfrj.siga.cp.CpArquivo;

public class ArmazenamentoBCFacade {

	public static ArmazenamentoBCInterface getArmazenamentoBC(CpArquivo cpArquivo) throws Exception {
		if(cpArquivo!=null) {
			switch (cpArquivo.getTipoArmazenamento()) {
			case HCP:
				return new ArmazenamentoHCP();				
			case TABELA:
				return new ArmazenamentoBDTabela();
			default:
				throw new Exception("Não há um tipo de armazenamento definido para este documento!");
			}
		}
		throw new Exception("O CpArquivo não foi informado!");
	}
}
