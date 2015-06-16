package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um V.O. de {@link SrLista}.
 */
public class SrListaVO {

    private Long idLista;
    private Long hisIdIni;
    private String nomeLista;
    private String nomeLotacao;
    private String descrAbrangencia;
    private String descrJustificativa;
    private String descrPriorizacao;
    private boolean ativo;
    private boolean podeEditar;
    private boolean podeConsultar;

    public SrListaVO(SrLista lista) {
        this.setIdLista(lista.getIdLista());
        this.setHisIdIni(lista.getHisIdIni());
        this.setNomeLista(lista.getNomeLista());
        this.setNomeLotacao(lista.getLotaCadastrante() != null ? lista.getLotaCadastrante().getDescricao() : "");
        this.setDescrAbrangencia(lista.getDescrAbrangencia());
        this.setDescrJustificativa(lista.getDescrJustificativa());
        this.setDescrPriorizacao(lista.getDescrPriorizacao());
        this.setAtivo(lista.isAtivo());
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

    public Long getIdLista() {
        return idLista;
    }

    public void setIdLista(Long idLista) {
        this.idLista = idLista;
    }

    public Long getHisIdIni() {
        return hisIdIni;
    }

    public void setHisIdIni(Long hisIdIni) {
        this.hisIdIni = hisIdIni;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getNomeLotacao() {
        return nomeLotacao;
    }

    public void setNomeLotacao(String nomeLotacao) {
        this.nomeLotacao = nomeLotacao;
    }

    public String getDescrAbrangencia() {
        return descrAbrangencia;
    }

    public void setDescrAbrangencia(String descrAbrangencia) {
        this.descrAbrangencia = descrAbrangencia;
    }

    public String getDescrJustificativa() {
        return descrJustificativa;
    }

    public void setDescrJustificativa(String descrJustificativa) {
        this.descrJustificativa = descrJustificativa;
    }

    public String getDescrPriorizacao() {
        return descrPriorizacao;
    }

    public void setDescrPriorizacao(String descrPriorizacao) {
        this.descrPriorizacao = descrPriorizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isPodeEditar() {
        return podeEditar;
    }

    public void setPodeEditar(boolean podeEditar) {
        this.podeEditar = podeEditar;
    }

    public boolean isPodeConsultar() {
        return podeConsultar;
    }

    public void setPodeConsultar(boolean podeConsultar) {
        this.podeConsultar = podeConsultar;
    }
}