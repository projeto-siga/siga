package br.gov.jfrj.siga.cp.arquivo;

public interface Armazenamento {

    public void salvar(Long id, String caminho, String tipoDeConteudo, byte[] conteudo);

    public void apagar(Long id, String caminho);

    public byte[] recuperar(Long id, String caminho);

}
