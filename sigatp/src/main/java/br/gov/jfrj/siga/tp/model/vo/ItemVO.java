package br.gov.jfrj.siga.tp.model.vo;

public class ItemVO {

    public Long id;
    public String descricao;
    public boolean selected;

    public ItemVO(Long id, String descricao, boolean selected) {
        this.id = id;
        this.descricao = descricao;
        this.selected = selected;
    }
}
