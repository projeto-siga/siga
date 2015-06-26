package br.gov.jfrj.siga.sr.model.vo;

public class ColunasVO {
    private String titulo;
    private String nome;
    private String classe;
    private Long largura;
    private boolean exibir;
    private boolean alteravel;

    public ColunasVO(String titulo, String nome) {
        super();
        this.setTitulo(titulo);
        this.setNome(nome);
        this.setExibir(true);
        this.setAlteravel(true);
    }

    public ColunasVO(String titulo, String nome, String classe) {
        this(titulo, nome);
        this.setClasse(classe);
    }

    public ColunasVO(String titulo, String nome, String classe, Long largura) {
        this(titulo, nome, classe);
        this.setLargura(largura);
    }

    public ColunasVO(String titulo, String nome, String classe, boolean exibir, boolean alteravel) {
        this(titulo, nome, classe);
        this.setExibir(exibir);
        this.setAlteravel(alteravel);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Long getLargura() {
        return largura;
    }

    public void setLargura(Long largura) {
        this.largura = largura;
    }

    public boolean isExibir() {
        return exibir;
    }

    public void setExibir(boolean exibir) {
        this.exibir = exibir;
    }

    public boolean isAlteravel() {
        return alteravel;
    }

    public void setAlteravel(boolean alteravel) {
        this.alteravel = alteravel;
    }

}
