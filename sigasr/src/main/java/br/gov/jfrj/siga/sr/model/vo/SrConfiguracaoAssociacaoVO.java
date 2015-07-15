package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPrioridade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um {@link SrConfiguracaoAssociacaoVO VO} da classe {@link SrConfiguracao}.
 *
 * @author DB1
 */
public class SrConfiguracaoAssociacaoVO {
    private Long idConfiguracao;
    private boolean atributoObrigatorio;

    private int tipoSolicitante;
    private SelecionavelVO orgaoUsuario;
    private SelecionavelVO complexo;
    private SelecionavelVO solicitante;
    private SelecionavelVO atendente;
    private SrPrioridade prioridade;
    private String descPrioridade;
    private boolean ativo;

    public SrConfiguracaoAssociacaoVO(SrConfiguracao configuracao) {
        this.setIdConfiguracao(configuracao.getIdConfiguracao());
        this.setAtributoObrigatorio(configuracao.isAtributoObrigatorio());

        this.setTipoSolicitante(configuracao.getTipoSolicitante());
        this.setOrgaoUsuario(SelecionavelVO.createFrom(configuracao.getOrgaoUsuario()));
        this.setComplexo(CpComplexoVO.createFrom(configuracao.getComplexo()));
        this.setSolicitante(SelecionavelVO.createFrom(configuracao.getSolicitante()));
        this.setAtendente(SelecionavelVO.createFrom(configuracao.getAtendente()));
        this.setPrioridade(configuracao.getPrioridade());
        this.setDescPrioridade(configuracao.getPrioridade() != null ? configuracao.getPrioridade().getDescPrioridade() : "");
        this.setAtivo(configuracao.isAtivo());
    }

    /**
     * Converte o objeto para Json.
     */
    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        return gson.toJson(this);
    }

    public Long getIdConfiguracao() {
        return idConfiguracao;
    }

    public void setIdConfiguracao(Long idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    public boolean isAtributoObrigatorio() {
        return atributoObrigatorio;
    }

    public void setAtributoObrigatorio(boolean atributoObrigatorio) {
        this.atributoObrigatorio = atributoObrigatorio;
    }

    public int getTipoSolicitante() {
        return tipoSolicitante;
    }

    public void setTipoSolicitante(int tipoSolicitante) {
        this.tipoSolicitante = tipoSolicitante;
    }

    public SelecionavelVO getOrgaoUsuario() {
        return orgaoUsuario;
    }

    public void setOrgaoUsuario(SelecionavelVO orgaoUsuario) {
        this.orgaoUsuario = orgaoUsuario;
    }

    public SelecionavelVO getComplexo() {
        return complexo;
    }

    public void setComplexo(SelecionavelVO complexo) {
        this.complexo = complexo;
    }

    public SelecionavelVO getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(SelecionavelVO solicitante) {
        this.solicitante = solicitante;
    }

    public SelecionavelVO getAtendente() {
        return atendente;
    }

    public void setAtendente(SelecionavelVO atendente) {
        this.atendente = atendente;
    }

    public SrPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(SrPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getDescPrioridade() {
        return descPrioridade;
    }

    public void setDescPrioridade(String descPrioridade) {
        this.descPrioridade = descPrioridade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}