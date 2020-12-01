package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe que representa um {@link SrConfiguracaoVO VO} da classe {@link SrConfiguracao}.
 *
 * @author DB1
 */
public class SrConfiguracaoVO {
    private Long idConfiguracao;
    private Long hisIdIni;
    private boolean herdado;
    private boolean utilizarItemHerdado;
    private boolean atributoObrigatorio;
    private boolean ativo;
    private String descrConfiguracao;

    private List<SrListaVO> listaVO;
    private List<SrItemConfiguracaoVO> listaItemConfiguracaoVO;
    private List<SrAcao.SrAcaoVO> listaAcaoVO;
    private List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> listaTipoPermissaoListaVO;
    private SelecionavelVO atendente;
    private SelecionavelVO orgaoUsuario;
    private SelecionavelVO complexo;
    private SrPrioridade prioridade;
    private String descPrioridade;

    // Solicitante
    private SelecionavelVO dpPessoa;
    private SelecionavelVO lotacao;
    private SelecionavelVO lotacaoParaInclusaoAutomatica;
    private SelecionavelVO cargo;
    private SelecionavelVO funcaoConfianca;
    private SelecionavelVO cpGrupo;
    private SelecionavelVO solicitante;
    private SrPrioridade prioridadeNaLista;
    private String descPrioridadeNaLista;
    private SelecionavelVO dpPessoaParaInclusaoAutomatica;

    public SrConfiguracaoVO(SrConfiguracao configuracao) throws Exception {
        setIdConfiguracao(configuracao.getId());
        setHisIdIni(configuracao.getHisIdIni());
        setHerdado(configuracao.isHerdado());
        setUtilizarItemHerdado(configuracao.isUtilizarItemHerdado());
        setAtivo(configuracao.isAtivo());
        setDescrConfiguracao(configuracao.getDescrConfiguracao());
        setPrioridade(configuracao.getPrioridade());
        setPrioridadeNaLista(configuracao.getPrioridadeNaLista());
        setDescPrioridade(configuracao.getPrioridade() != null ? configuracao.getPrioridade().getDescPrioridade() : "");
        setDescPrioridadeNaLista(configuracao.getPrioridadeNaLista() != null ? configuracao.getPrioridadeNaLista().getDescPrioridade() : "");

        setListaItemConfiguracaoVO(new ArrayList<SrItemConfiguracaoVO>());
        if (configuracao.getItemConfiguracaoSet() != null) {
            for (SrItemConfiguracao item : configuracao.getItemConfiguracaoSet()) {
            	
                getListaItemConfiguracaoVO().add(item.toVO());
            }
        }

        setListaAcaoVO(new ArrayList<SrAcao.SrAcaoVO>());
        if (configuracao.getAcoesSet() != null) {
            for (SrAcao item : configuracao.getAcoesSet()) {
                getListaAcaoVO().add(item.toVO());
            }
        }

        setListaTipoPermissaoListaVO(new ArrayList<SrTipoPermissaoLista.SrTipoPermissaoListaVO>());
        if (configuracao.getTipoPermissaoSet() != null) {
            for (SrTipoPermissaoLista item : configuracao.getTipoPermissaoSet()) {
                getListaTipoPermissaoListaVO().add(item.toVO());
            }
        }

        if (configuracao.getAtendente() != null && configuracao.getAtendente().getId() != null) {
        	//Edson: por causa do detach no ObjectInstantiator...
        	DpLotacao atendente = DpLotacao.AR.findById(configuracao.getAtendente().getId());
            setAtendente(SelecionavelVO.createFrom(atendente.getLotacaoAtual()));
        }

        if (configuracao.getOrgaoUsuario() != null)
            setOrgaoUsuario(SelecionavelVO.createFrom(configuracao.getOrgaoUsuario().getId(), configuracao.getOrgaoUsuario().getDescricao(), configuracao.getOrgaoUsuario().getAcronimoOrgaoUsu()));

        setComplexo(CpComplexoVO.createFrom(configuracao.getComplexo()));

        // Dados do Solicitante
        setDpPessoa(SelecionavelVO.createFrom(configuracao.getDpPessoa(), configuracao.getTipoSolicitante()));
        setDpPessoaParaInclusaoAutomatica(getDpPessoa());
        setLotacao(SelecionavelVO.createFrom(configuracao.getLotacao(), configuracao.getTipoSolicitante()));
        setLotacaoParaInclusaoAutomatica(getLotacao());
        setCargo(SelecionavelVO.createFrom(configuracao.getCargo(), configuracao.getTipoSolicitante()));
        setFuncaoConfianca(SelecionavelVO.createFrom(configuracao.getFuncaoConfianca(), configuracao.getTipoSolicitante()));
        setCpGrupo(SelecionavelVO.createFrom(configuracao.getCpGrupo(), configuracao.getTipoSolicitante()));

        setSolicitante(SelecionavelVO.createFrom(configuracao.getSolicitante(), configuracao.getTipoSolicitante()));
    }

    public SrConfiguracaoVO(SrConfiguracao configuracao, boolean atributoObrigatorio) throws Exception {
        this(configuracao);
        this.setAtributoObrigatorio(atributoObrigatorio);
    }

    /**
     * Converte o objeto para Json.
     */
    public String toJson() {
        return toJsonObject().toString();
    }

    public JsonObject toJsonObject() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        Gson gson = builder.create();
        return (JsonObject) gson.toJsonTree(this);
    }

    private Long getIdConfiguracao() {
        return idConfiguracao;
    }

    private void setIdConfiguracao(Long idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    public Long getHisIdIni() {
        return hisIdIni;
    }

    public void setHisIdIni(Long hisIdIni) {
        this.hisIdIni = hisIdIni;
    }

    public boolean isHerdado() {
        return herdado;
    }

    public void setHerdado(boolean herdado) {
        this.herdado = herdado;
    }

    public boolean isUtilizarItemHerdado() {
        return utilizarItemHerdado;
    }

    public void setUtilizarItemHerdado(boolean utilizarItemHerdado) {
        this.utilizarItemHerdado = utilizarItemHerdado;
    }

    public boolean isAtributoObrigatorio() {
        return atributoObrigatorio;
    }

    public void setAtributoObrigatorio(boolean atributoObrigatorio) {
        this.atributoObrigatorio = atributoObrigatorio;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getDescrConfiguracao() {
        return descrConfiguracao;
    }

    public void setDescrConfiguracao(String descrConfiguracao) {
        this.descrConfiguracao = descrConfiguracao;
    }

    public List<SrListaVO> getListaVO() {
        return listaVO;
    }

    public void setListaVO(List<SrListaVO> listaVO) {
        this.listaVO = listaVO;
    }

    public List<SrItemConfiguracaoVO> getListaItemConfiguracaoVO() {
        return listaItemConfiguracaoVO;
    }

    public void setListaItemConfiguracaoVO(List<SrItemConfiguracaoVO> listaItemConfiguracaoVO) {
        this.listaItemConfiguracaoVO = listaItemConfiguracaoVO;
    }

    public List<SrAcao.SrAcaoVO> getListaAcaoVO() {
        return listaAcaoVO;
    }

    public void setListaAcaoVO(List<SrAcao.SrAcaoVO> listaAcaoVO) {
        this.listaAcaoVO = listaAcaoVO;
    }

    public List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> getListaTipoPermissaoListaVO() {
        return listaTipoPermissaoListaVO;
    }

    public void setListaTipoPermissaoListaVO(List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> listaTipoPermissaoListaVO) {
        this.listaTipoPermissaoListaVO = listaTipoPermissaoListaVO;
    }

    public SelecionavelVO getAtendente() {
        return atendente;
    }

    public void setAtendente(SelecionavelVO atendente) {
        this.atendente = atendente;
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

    public SelecionavelVO getDpPessoa() {
        return dpPessoa;
    }

    public void setDpPessoa(SelecionavelVO pessoa) {
        this.dpPessoa = pessoa;
    }

    public SelecionavelVO getLotacao() {
        return lotacao;
    }

    public void setLotacao(SelecionavelVO lotacao) {
        this.lotacao = lotacao;
    }

    public SelecionavelVO getLotacaoParaInclusaoAutomatica() {
        return lotacaoParaInclusaoAutomatica;
    }

    public void setLotacaoParaInclusaoAutomatica(SelecionavelVO lotacaoParaInclusaoAutomatica) {
        this.lotacaoParaInclusaoAutomatica = lotacaoParaInclusaoAutomatica;
    }

    public SelecionavelVO getCargo() {
        return cargo;
    }

    public void setCargo(SelecionavelVO cargo) {
        this.cargo = cargo;
    }

    public SelecionavelVO getFuncaoConfianca() {
        return funcaoConfianca;
    }

    public void setFuncaoConfianca(SelecionavelVO funcao) {
        this.funcaoConfianca = funcao;
    }

    public SelecionavelVO getCpGrupo() {
        return cpGrupo;
    }

    public void setCpGrupo(SelecionavelVO cpGrupo) {
        this.cpGrupo = cpGrupo;
    }

    public SelecionavelVO getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(SelecionavelVO solicitante) {
        this.solicitante = solicitante;
    }

    public SrPrioridade getPrioridadeNaLista() {
        return prioridadeNaLista;
    }

    public void setPrioridadeNaLista(SrPrioridade prioridadeNaLista) {
        this.prioridadeNaLista = prioridadeNaLista;
    }

    public String getDescPrioridadeNaLista() {
        return descPrioridadeNaLista;
    }

    public void setDescPrioridadeNaLista(String descPrioridadeNaLista) {
        this.descPrioridadeNaLista = descPrioridadeNaLista;
    }

    public SelecionavelVO getDpPessoaParaInclusaoAutomatica() {
        return dpPessoaParaInclusaoAutomatica;
    }

    public void setDpPessoaParaInclusaoAutomatica(SelecionavelVO dpPessoaParaInclusaoAutomatica) {
        this.dpPessoaParaInclusaoAutomatica = dpPessoaParaInclusaoAutomatica;
    }
}