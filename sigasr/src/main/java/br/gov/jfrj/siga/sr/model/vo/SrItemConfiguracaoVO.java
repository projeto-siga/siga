package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LazyInitializationException;

import br.gov.jfrj.siga.sr.model.SrFatorMultiplicacao;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Classe que representa um V.O. de {@link SrItemConfiguracao}.
 */
public class SrItemConfiguracaoVO implements ISelecionavel {

    private Long id;
    private String sigla;
    private String descricao;
    private Long idItemConfiguracao;
    private String descrItemConfiguracao;
    private String tituloItemConfiguracao;
    private String siglaItemConfiguracao;
    private Long hisIdIni;
    private String descricaoSimilaridade;
    private int numFatorMultiplicacaoGeral;
    private boolean ativo;
    private int nivel;
    private List<SrGestorItemVO> gestorSetVO;
    private List<SrFatorMultiplicacaoVO> fatorMultiplicacaoSetVO;

    public SrItemConfiguracaoVO(Long id, String descricao, String titulo, String sigla, Long hisIdIni, String descricaoSimilaridade, int numFatorMultiplicacaoGeral, boolean isAtivo, int nivel,
            List<SrGestorItem> gestorSet, List<SrFatorMultiplicacao> fatorMultiplicacaoSet) throws Exception {
        this.id = id;
        this.setIdItemConfiguracao(id);
        this.setDescrItemConfiguracao(descricao);
        this.setTituloItemConfiguracao(titulo);
        this.descricao = titulo;
        this.setSiglaItemConfiguracao(sigla);
        this.sigla = sigla;
        this.setHisIdIni(hisIdIni);
        this.setDescricaoSimilaridade(descricaoSimilaridade);
        this.setAtivo(isAtivo);
        this.setNumFatorMultiplicacaoGeral(numFatorMultiplicacaoGeral);
        this.setNivel(nivel);
        this.setGestorSetVO(new ArrayList<SrGestorItemVO>());
        this.setFatorMultiplicacaoSetVO(new ArrayList<SrFatorMultiplicacaoVO>());

        try{
        	if (gestorSet != null)
        		for (SrGestorItem item : gestorSet) {
        			getGestorSetVO().add(item.toVO());
        		}
        } catch(LazyInitializationException lie){
        	//Swallow!
        }

        try{
        	if (fatorMultiplicacaoSet != null)
        		for (SrFatorMultiplicacao item : fatorMultiplicacaoSet) {
        			getFatorMultiplicacaoSetVO().add(item.toVO());
        		}
        } catch(LazyInitializationException lie){
        	//Swallow!
        }
    }

    /**
     * Converte o objeto para Json.
     */
    public String toJson() {
        return toJsonObject().toString();
    }

    public JsonElement toJsonObject() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();

        return gson.toJsonTree(this);
    }

    public static SrItemConfiguracaoVO createFrom(SrItemConfiguracao item) throws Exception {
        if (item != null)
            return new SrItemConfiguracaoVO(item.getIdItemConfiguracao(), item.getDescrItemConfiguracao(), item.getTituloItemConfiguracao(), item.getSiglaItemConfiguracao(), item.getHisIdIni(),
                    item.getDescricaoSimilaridade(), item.getNumFatorMultiplicacaoGeral(), item.isAtivo(), item.getNivel(), item.getGestorSet(), item.getFatorMultiplicacaoSet());
        else
            return null;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getSigla() {
        return this.sigla;
    }

    @Override
    public String getDescricao() {
        return this.descricao;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdItemConfiguracao() {
        return idItemConfiguracao;
    }

    public void setIdItemConfiguracao(Long idItemConfiguracao) {
        this.idItemConfiguracao = idItemConfiguracao;
    }

    public String getDescrItemConfiguracao() {
        return descrItemConfiguracao;
    }

    public void setDescrItemConfiguracao(String descrItemConfiguracao) {
        this.descrItemConfiguracao = descrItemConfiguracao;
    }

    public String getTituloItemConfiguracao() {
        return tituloItemConfiguracao;
    }

    public void setTituloItemConfiguracao(String tituloItemConfiguracao) {
        this.tituloItemConfiguracao = tituloItemConfiguracao;
    }

    public String getSiglaItemConfiguracao() {
        return siglaItemConfiguracao;
    }

    public void setSiglaItemConfiguracao(String siglaItemConfiguracao) {
        this.siglaItemConfiguracao = siglaItemConfiguracao;
    }

    public Long getHisIdIni() {
        return hisIdIni;
    }

    public void setHisIdIni(Long hisIdIni) {
        this.hisIdIni = hisIdIni;
    }

    public String getDescricaoSimilaridade() {
        return descricaoSimilaridade;
    }

    public void setDescricaoSimilaridade(String descricaoSimilaridade) {
        this.descricaoSimilaridade = descricaoSimilaridade;
    }

    public int getNumFatorMultiplicacaoGeral() {
        return numFatorMultiplicacaoGeral;
    }

    public void setNumFatorMultiplicacaoGeral(int numFatorMultiplicacaoGeral) {
        this.numFatorMultiplicacaoGeral = numFatorMultiplicacaoGeral;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<SrGestorItemVO> getGestorSetVO() {
        return gestorSetVO;
    }

    public void setGestorSetVO(List<SrGestorItemVO> gestorSetVO) {
        this.gestorSetVO = gestorSetVO;
    }

    public List<SrFatorMultiplicacaoVO> getFatorMultiplicacaoSetVO() {
        return fatorMultiplicacaoSetVO;
    }

    public void setFatorMultiplicacaoSetVO(List<SrFatorMultiplicacaoVO> fatorMultiplicacaoSetVO) {
        this.fatorMultiplicacaoSetVO = fatorMultiplicacaoSetVO;
    }
}