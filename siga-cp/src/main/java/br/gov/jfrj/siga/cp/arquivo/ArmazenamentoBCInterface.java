package br.gov.jfrj.siga.cp.arquivo;

import br.gov.jfrj.siga.cp.CpArquivo;

public interface ArmazenamentoBCInterface {
	
	public void salvar(CpArquivo cpArquivo, byte[] conteudo);
	
	public byte[] recuperar(CpArquivo cpArquivo);
	
	public void apagar(CpArquivo cpArquivo);
	
}
