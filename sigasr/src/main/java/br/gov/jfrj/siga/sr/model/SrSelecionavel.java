package br.gov.jfrj.siga.sr.model;

import java.util.List;

public interface SrSelecionavel {
    public Long getId();

    public void setId(Long id);

    public String getSigla();

    public void setSigla(String sigla);

    public String getDescricao();

    public void setDescricao(String descricao);

    public SrSelecionavel selecionar(String sigla) throws Exception;

    public List<? extends SrSelecionavel> buscar() throws Exception;
}
