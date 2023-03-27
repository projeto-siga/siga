package br.gov.jfrj.siga.cp.arquivo;

public interface Armazenamento {

    public void salvar(String caminho, String tipoDeConteudo, byte[] conteudo);

    public void apagar(String caminho);

    public byte[] recuperar(String caminho);

}
