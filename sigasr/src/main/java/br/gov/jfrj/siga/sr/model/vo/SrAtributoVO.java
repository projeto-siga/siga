package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe que representa um V.O. de {@link SrGestorItem}.
 */
public class SrAtributoVO {
    private boolean ativo;
    private Long idAtributo;
    private String nomeAtributo;
    private String descrAtributo;
    private String codigoAtributo;
    private SrTipoAtributo tipoAtributo;
    private SrObjetivoAtributoVO objetivoAtributo;
    private String descrPreDefinido;
    private Long hisIdIni;
    private List<SrConfiguracaoVO> associacoesVO;

    public SrAtributoVO(SrAtributo atributo, List<SrConfiguracao> associacoes) throws Exception {
        this.setAtivo(atributo.isAtivo());
        this.setIdAtributo(atributo.getIdAtributo());
        this.setNomeAtributo(atributo.getNomeAtributo());
        this.setDescrAtributo(atributo.getDescrAtributo());
        this.setCodigoAtributo(atributo.getCodigoAtributo());
        this.setTipoAtributo(atributo.getTipoAtributo());
        this.setObjetivoAtributo(SrObjetivoAtributoVO.createFrom(atributo.getObjetivoAtributo()));
        this.setDescrPreDefinido(atributo.getDescrPreDefinido());
        this.setHisIdIni(atributo.getHisIdIni());
        this.setAssociacoesVO(new ArrayList<SrConfiguracaoVO>());

        if (associacoes != null)
            for (SrConfiguracao associacao : associacoes) {
                getAssociacoesVO().add(associacao.toVO());
            }
    }

    /**
     * Converte o objeto para Json.
     */
    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        JsonObject jsonTree = (JsonObject) gson.toJsonTree(this);

        if (this.getTipoAtributo() != null) {
            jsonTree.add("descrTipoAtributo", gson.toJsonTree(this.getTipoAtributo().getDescrTipoAtributo()));
        }

        if (this.getObjetivoAtributo() != null) {
            jsonTree.add("descrObjetivoAtributo", gson.toJsonTree(this.getObjetivoAtributo().getDescricao()));
        }

        return jsonTree.toString();
    }

    public static SrAtributoVO createFrom(SrAtributo atributo, boolean listarAssociacoes) throws Exception {
        if (atributo != null) {
            List<SrConfiguracao> associacoes = new ArrayList<SrConfiguracao>();

            if (listarAssociacoes)
                associacoes = SrConfiguracao.listarAssociacoesAtributo(atributo, Boolean.FALSE);

            return new SrAtributoVO(atributo, associacoes);
        }

        return null;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getIdAtributo() {
        return idAtributo;
    }

    public void setIdAtributo(Long idAtributo) {
        this.idAtributo = idAtributo;
    }

    public String getNomeAtributo() {
        return nomeAtributo;
    }

    public void setNomeAtributo(String nomeAtributo) {
        this.nomeAtributo = nomeAtributo;
    }

    public String getDescrAtributo() {
        return descrAtributo;
    }

    public void setDescrAtributo(String descrAtributo) {
        this.descrAtributo = descrAtributo;
    }

    public String getCodigoAtributo() {
        return codigoAtributo;
    }

    public void setCodigoAtributo(String codigoAtributo) {
        this.codigoAtributo = codigoAtributo;
    }

    public SrTipoAtributo getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(SrTipoAtributo tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public SrObjetivoAtributoVO getObjetivoAtributo() {
        return objetivoAtributo;
    }

    public void setObjetivoAtributo(SrObjetivoAtributoVO objetivoAtributo) {
        this.objetivoAtributo = objetivoAtributo;
    }

    public String getDescrPreDefinido() {
        return descrPreDefinido;
    }

    public void setDescrPreDefinido(String descrPreDefinido) {
        this.descrPreDefinido = descrPreDefinido;
    }

    public Long getHisIdIni() {
        return hisIdIni;
    }

    public void setHisIdIni(Long hisIdIni) {
        this.hisIdIni = hisIdIni;
    }

    public List<SrConfiguracaoVO> getAssociacoesVO() {
        return associacoesVO;
    }

    public void setAssociacoesVO(List<SrConfiguracaoVO> associacoesVO) {
        this.associacoesVO = associacoesVO;
    }
}