/**
 * Contato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Contato  implements java.io.Serializable {
    private java.lang.String staOperacao;

    private java.lang.String idContato;

    private java.lang.String idTipoContato;

    private java.lang.String nomeTipoContato;

    private java.lang.String sigla;

    private java.lang.String nome;

    private java.lang.String nomeSocial;

    private java.lang.String staNatureza;

    private java.lang.String idContatoAssociado;

    private java.lang.String nomeContatoAssociado;

    private java.lang.String sinEnderecoAssociado;

    private java.lang.String cnpjAssociado;

    private java.lang.String endereco;

    private java.lang.String complemento;

    private java.lang.String bairro;

    private java.lang.String idCidade;

    private java.lang.String nomeCidade;

    private java.lang.String idEstado;

    private java.lang.String siglaEstado;

    private java.lang.String idPais;

    private java.lang.String nomePais;

    private java.lang.String cep;

    private java.lang.String staGenero;

    private java.lang.String idCargo;

    private java.lang.String expressaoCargo;

    private java.lang.String expressaoTratamento;

    private java.lang.String expressaoVocativo;

    private java.lang.String cpf;

    private java.lang.String cnpj;

    private java.lang.String rg;

    private java.lang.String orgaoExpedidor;

    private java.lang.String numeroPassaporte;

    private java.lang.String idPaisPassaporte;

    private java.lang.String nomePaisPassaporte;

    private java.lang.String matricula;

    private java.lang.String matriculaOab;

    private java.lang.String telefoneComercial;

    private java.lang.String telefoneResidencial;

    private java.lang.String telefoneCelular;

    private java.lang.String dataNascimento;

    private java.lang.String email;

    private java.lang.String sitioInternet;

    private java.lang.String observacao;

    private java.lang.String conjuge;

    private java.lang.String funcao;

    private java.lang.String idTitulo;

    private java.lang.String expressaoTitulo;

    private java.lang.String abreviaturaTitulo;

    private java.lang.String sinAtivo;

    private java.lang.String idCategoria;

    private java.lang.String idNomeCategoria;

    public Contato() {
    }

    public Contato(
           java.lang.String staOperacao,
           java.lang.String idContato,
           java.lang.String idTipoContato,
           java.lang.String nomeTipoContato,
           java.lang.String sigla,
           java.lang.String nome,
           java.lang.String nomeSocial,
           java.lang.String staNatureza,
           java.lang.String idContatoAssociado,
           java.lang.String nomeContatoAssociado,
           java.lang.String sinEnderecoAssociado,
           java.lang.String cnpjAssociado,
           java.lang.String endereco,
           java.lang.String complemento,
           java.lang.String bairro,
           java.lang.String idCidade,
           java.lang.String nomeCidade,
           java.lang.String idEstado,
           java.lang.String siglaEstado,
           java.lang.String idPais,
           java.lang.String nomePais,
           java.lang.String cep,
           java.lang.String staGenero,
           java.lang.String idCargo,
           java.lang.String expressaoCargo,
           java.lang.String expressaoTratamento,
           java.lang.String expressaoVocativo,
           java.lang.String cpf,
           java.lang.String cnpj,
           java.lang.String rg,
           java.lang.String orgaoExpedidor,
           java.lang.String numeroPassaporte,
           java.lang.String idPaisPassaporte,
           java.lang.String nomePaisPassaporte,
           java.lang.String matricula,
           java.lang.String matriculaOab,
           java.lang.String telefoneComercial,
           java.lang.String telefoneResidencial,
           java.lang.String telefoneCelular,
           java.lang.String dataNascimento,
           java.lang.String email,
           java.lang.String sitioInternet,
           java.lang.String observacao,
           java.lang.String conjuge,
           java.lang.String funcao,
           java.lang.String idTitulo,
           java.lang.String expressaoTitulo,
           java.lang.String abreviaturaTitulo,
           java.lang.String sinAtivo,
           java.lang.String idCategoria,
           java.lang.String idNomeCategoria) {
           this.staOperacao = staOperacao;
           this.idContato = idContato;
           this.idTipoContato = idTipoContato;
           this.nomeTipoContato = nomeTipoContato;
           this.sigla = sigla;
           this.nome = nome;
           this.nomeSocial = nomeSocial;
           this.staNatureza = staNatureza;
           this.idContatoAssociado = idContatoAssociado;
           this.nomeContatoAssociado = nomeContatoAssociado;
           this.sinEnderecoAssociado = sinEnderecoAssociado;
           this.cnpjAssociado = cnpjAssociado;
           this.endereco = endereco;
           this.complemento = complemento;
           this.bairro = bairro;
           this.idCidade = idCidade;
           this.nomeCidade = nomeCidade;
           this.idEstado = idEstado;
           this.siglaEstado = siglaEstado;
           this.idPais = idPais;
           this.nomePais = nomePais;
           this.cep = cep;
           this.staGenero = staGenero;
           this.idCargo = idCargo;
           this.expressaoCargo = expressaoCargo;
           this.expressaoTratamento = expressaoTratamento;
           this.expressaoVocativo = expressaoVocativo;
           this.cpf = cpf;
           this.cnpj = cnpj;
           this.rg = rg;
           this.orgaoExpedidor = orgaoExpedidor;
           this.numeroPassaporte = numeroPassaporte;
           this.idPaisPassaporte = idPaisPassaporte;
           this.nomePaisPassaporte = nomePaisPassaporte;
           this.matricula = matricula;
           this.matriculaOab = matriculaOab;
           this.telefoneComercial = telefoneComercial;
           this.telefoneResidencial = telefoneResidencial;
           this.telefoneCelular = telefoneCelular;
           this.dataNascimento = dataNascimento;
           this.email = email;
           this.sitioInternet = sitioInternet;
           this.observacao = observacao;
           this.conjuge = conjuge;
           this.funcao = funcao;
           this.idTitulo = idTitulo;
           this.expressaoTitulo = expressaoTitulo;
           this.abreviaturaTitulo = abreviaturaTitulo;
           this.sinAtivo = sinAtivo;
           this.idCategoria = idCategoria;
           this.idNomeCategoria = idNomeCategoria;
    }


    /**
     * Gets the staOperacao value for this Contato.
     * 
     * @return staOperacao
     */
    public java.lang.String getStaOperacao() {
        return staOperacao;
    }


    /**
     * Sets the staOperacao value for this Contato.
     * 
     * @param staOperacao
     */
    public void setStaOperacao(java.lang.String staOperacao) {
        this.staOperacao = staOperacao;
    }


    /**
     * Gets the idContato value for this Contato.
     * 
     * @return idContato
     */
    public java.lang.String getIdContato() {
        return idContato;
    }


    /**
     * Sets the idContato value for this Contato.
     * 
     * @param idContato
     */
    public void setIdContato(java.lang.String idContato) {
        this.idContato = idContato;
    }


    /**
     * Gets the idTipoContato value for this Contato.
     * 
     * @return idTipoContato
     */
    public java.lang.String getIdTipoContato() {
        return idTipoContato;
    }


    /**
     * Sets the idTipoContato value for this Contato.
     * 
     * @param idTipoContato
     */
    public void setIdTipoContato(java.lang.String idTipoContato) {
        this.idTipoContato = idTipoContato;
    }


    /**
     * Gets the nomeTipoContato value for this Contato.
     * 
     * @return nomeTipoContato
     */
    public java.lang.String getNomeTipoContato() {
        return nomeTipoContato;
    }


    /**
     * Sets the nomeTipoContato value for this Contato.
     * 
     * @param nomeTipoContato
     */
    public void setNomeTipoContato(java.lang.String nomeTipoContato) {
        this.nomeTipoContato = nomeTipoContato;
    }


    /**
     * Gets the sigla value for this Contato.
     * 
     * @return sigla
     */
    public java.lang.String getSigla() {
        return sigla;
    }


    /**
     * Sets the sigla value for this Contato.
     * 
     * @param sigla
     */
    public void setSigla(java.lang.String sigla) {
        this.sigla = sigla;
    }


    /**
     * Gets the nome value for this Contato.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Contato.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the nomeSocial value for this Contato.
     * 
     * @return nomeSocial
     */
    public java.lang.String getNomeSocial() {
        return nomeSocial;
    }


    /**
     * Sets the nomeSocial value for this Contato.
     * 
     * @param nomeSocial
     */
    public void setNomeSocial(java.lang.String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }


    /**
     * Gets the staNatureza value for this Contato.
     * 
     * @return staNatureza
     */
    public java.lang.String getStaNatureza() {
        return staNatureza;
    }


    /**
     * Sets the staNatureza value for this Contato.
     * 
     * @param staNatureza
     */
    public void setStaNatureza(java.lang.String staNatureza) {
        this.staNatureza = staNatureza;
    }


    /**
     * Gets the idContatoAssociado value for this Contato.
     * 
     * @return idContatoAssociado
     */
    public java.lang.String getIdContatoAssociado() {
        return idContatoAssociado;
    }


    /**
     * Sets the idContatoAssociado value for this Contato.
     * 
     * @param idContatoAssociado
     */
    public void setIdContatoAssociado(java.lang.String idContatoAssociado) {
        this.idContatoAssociado = idContatoAssociado;
    }


    /**
     * Gets the nomeContatoAssociado value for this Contato.
     * 
     * @return nomeContatoAssociado
     */
    public java.lang.String getNomeContatoAssociado() {
        return nomeContatoAssociado;
    }


    /**
     * Sets the nomeContatoAssociado value for this Contato.
     * 
     * @param nomeContatoAssociado
     */
    public void setNomeContatoAssociado(java.lang.String nomeContatoAssociado) {
        this.nomeContatoAssociado = nomeContatoAssociado;
    }


    /**
     * Gets the sinEnderecoAssociado value for this Contato.
     * 
     * @return sinEnderecoAssociado
     */
    public java.lang.String getSinEnderecoAssociado() {
        return sinEnderecoAssociado;
    }


    /**
     * Sets the sinEnderecoAssociado value for this Contato.
     * 
     * @param sinEnderecoAssociado
     */
    public void setSinEnderecoAssociado(java.lang.String sinEnderecoAssociado) {
        this.sinEnderecoAssociado = sinEnderecoAssociado;
    }


    /**
     * Gets the cnpjAssociado value for this Contato.
     * 
     * @return cnpjAssociado
     */
    public java.lang.String getCnpjAssociado() {
        return cnpjAssociado;
    }


    /**
     * Sets the cnpjAssociado value for this Contato.
     * 
     * @param cnpjAssociado
     */
    public void setCnpjAssociado(java.lang.String cnpjAssociado) {
        this.cnpjAssociado = cnpjAssociado;
    }


    /**
     * Gets the endereco value for this Contato.
     * 
     * @return endereco
     */
    public java.lang.String getEndereco() {
        return endereco;
    }


    /**
     * Sets the endereco value for this Contato.
     * 
     * @param endereco
     */
    public void setEndereco(java.lang.String endereco) {
        this.endereco = endereco;
    }


    /**
     * Gets the complemento value for this Contato.
     * 
     * @return complemento
     */
    public java.lang.String getComplemento() {
        return complemento;
    }


    /**
     * Sets the complemento value for this Contato.
     * 
     * @param complemento
     */
    public void setComplemento(java.lang.String complemento) {
        this.complemento = complemento;
    }


    /**
     * Gets the bairro value for this Contato.
     * 
     * @return bairro
     */
    public java.lang.String getBairro() {
        return bairro;
    }


    /**
     * Sets the bairro value for this Contato.
     * 
     * @param bairro
     */
    public void setBairro(java.lang.String bairro) {
        this.bairro = bairro;
    }


    /**
     * Gets the idCidade value for this Contato.
     * 
     * @return idCidade
     */
    public java.lang.String getIdCidade() {
        return idCidade;
    }


    /**
     * Sets the idCidade value for this Contato.
     * 
     * @param idCidade
     */
    public void setIdCidade(java.lang.String idCidade) {
        this.idCidade = idCidade;
    }


    /**
     * Gets the nomeCidade value for this Contato.
     * 
     * @return nomeCidade
     */
    public java.lang.String getNomeCidade() {
        return nomeCidade;
    }


    /**
     * Sets the nomeCidade value for this Contato.
     * 
     * @param nomeCidade
     */
    public void setNomeCidade(java.lang.String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }


    /**
     * Gets the idEstado value for this Contato.
     * 
     * @return idEstado
     */
    public java.lang.String getIdEstado() {
        return idEstado;
    }


    /**
     * Sets the idEstado value for this Contato.
     * 
     * @param idEstado
     */
    public void setIdEstado(java.lang.String idEstado) {
        this.idEstado = idEstado;
    }


    /**
     * Gets the siglaEstado value for this Contato.
     * 
     * @return siglaEstado
     */
    public java.lang.String getSiglaEstado() {
        return siglaEstado;
    }


    /**
     * Sets the siglaEstado value for this Contato.
     * 
     * @param siglaEstado
     */
    public void setSiglaEstado(java.lang.String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }


    /**
     * Gets the idPais value for this Contato.
     * 
     * @return idPais
     */
    public java.lang.String getIdPais() {
        return idPais;
    }


    /**
     * Sets the idPais value for this Contato.
     * 
     * @param idPais
     */
    public void setIdPais(java.lang.String idPais) {
        this.idPais = idPais;
    }


    /**
     * Gets the nomePais value for this Contato.
     * 
     * @return nomePais
     */
    public java.lang.String getNomePais() {
        return nomePais;
    }


    /**
     * Sets the nomePais value for this Contato.
     * 
     * @param nomePais
     */
    public void setNomePais(java.lang.String nomePais) {
        this.nomePais = nomePais;
    }


    /**
     * Gets the cep value for this Contato.
     * 
     * @return cep
     */
    public java.lang.String getCep() {
        return cep;
    }


    /**
     * Sets the cep value for this Contato.
     * 
     * @param cep
     */
    public void setCep(java.lang.String cep) {
        this.cep = cep;
    }


    /**
     * Gets the staGenero value for this Contato.
     * 
     * @return staGenero
     */
    public java.lang.String getStaGenero() {
        return staGenero;
    }


    /**
     * Sets the staGenero value for this Contato.
     * 
     * @param staGenero
     */
    public void setStaGenero(java.lang.String staGenero) {
        this.staGenero = staGenero;
    }


    /**
     * Gets the idCargo value for this Contato.
     * 
     * @return idCargo
     */
    public java.lang.String getIdCargo() {
        return idCargo;
    }


    /**
     * Sets the idCargo value for this Contato.
     * 
     * @param idCargo
     */
    public void setIdCargo(java.lang.String idCargo) {
        this.idCargo = idCargo;
    }


    /**
     * Gets the expressaoCargo value for this Contato.
     * 
     * @return expressaoCargo
     */
    public java.lang.String getExpressaoCargo() {
        return expressaoCargo;
    }


    /**
     * Sets the expressaoCargo value for this Contato.
     * 
     * @param expressaoCargo
     */
    public void setExpressaoCargo(java.lang.String expressaoCargo) {
        this.expressaoCargo = expressaoCargo;
    }


    /**
     * Gets the expressaoTratamento value for this Contato.
     * 
     * @return expressaoTratamento
     */
    public java.lang.String getExpressaoTratamento() {
        return expressaoTratamento;
    }


    /**
     * Sets the expressaoTratamento value for this Contato.
     * 
     * @param expressaoTratamento
     */
    public void setExpressaoTratamento(java.lang.String expressaoTratamento) {
        this.expressaoTratamento = expressaoTratamento;
    }


    /**
     * Gets the expressaoVocativo value for this Contato.
     * 
     * @return expressaoVocativo
     */
    public java.lang.String getExpressaoVocativo() {
        return expressaoVocativo;
    }


    /**
     * Sets the expressaoVocativo value for this Contato.
     * 
     * @param expressaoVocativo
     */
    public void setExpressaoVocativo(java.lang.String expressaoVocativo) {
        this.expressaoVocativo = expressaoVocativo;
    }


    /**
     * Gets the cpf value for this Contato.
     * 
     * @return cpf
     */
    public java.lang.String getCpf() {
        return cpf;
    }


    /**
     * Sets the cpf value for this Contato.
     * 
     * @param cpf
     */
    public void setCpf(java.lang.String cpf) {
        this.cpf = cpf;
    }


    /**
     * Gets the cnpj value for this Contato.
     * 
     * @return cnpj
     */
    public java.lang.String getCnpj() {
        return cnpj;
    }


    /**
     * Sets the cnpj value for this Contato.
     * 
     * @param cnpj
     */
    public void setCnpj(java.lang.String cnpj) {
        this.cnpj = cnpj;
    }


    /**
     * Gets the rg value for this Contato.
     * 
     * @return rg
     */
    public java.lang.String getRg() {
        return rg;
    }


    /**
     * Sets the rg value for this Contato.
     * 
     * @param rg
     */
    public void setRg(java.lang.String rg) {
        this.rg = rg;
    }


    /**
     * Gets the orgaoExpedidor value for this Contato.
     * 
     * @return orgaoExpedidor
     */
    public java.lang.String getOrgaoExpedidor() {
        return orgaoExpedidor;
    }


    /**
     * Sets the orgaoExpedidor value for this Contato.
     * 
     * @param orgaoExpedidor
     */
    public void setOrgaoExpedidor(java.lang.String orgaoExpedidor) {
        this.orgaoExpedidor = orgaoExpedidor;
    }


    /**
     * Gets the numeroPassaporte value for this Contato.
     * 
     * @return numeroPassaporte
     */
    public java.lang.String getNumeroPassaporte() {
        return numeroPassaporte;
    }


    /**
     * Sets the numeroPassaporte value for this Contato.
     * 
     * @param numeroPassaporte
     */
    public void setNumeroPassaporte(java.lang.String numeroPassaporte) {
        this.numeroPassaporte = numeroPassaporte;
    }


    /**
     * Gets the idPaisPassaporte value for this Contato.
     * 
     * @return idPaisPassaporte
     */
    public java.lang.String getIdPaisPassaporte() {
        return idPaisPassaporte;
    }


    /**
     * Sets the idPaisPassaporte value for this Contato.
     * 
     * @param idPaisPassaporte
     */
    public void setIdPaisPassaporte(java.lang.String idPaisPassaporte) {
        this.idPaisPassaporte = idPaisPassaporte;
    }


    /**
     * Gets the nomePaisPassaporte value for this Contato.
     * 
     * @return nomePaisPassaporte
     */
    public java.lang.String getNomePaisPassaporte() {
        return nomePaisPassaporte;
    }


    /**
     * Sets the nomePaisPassaporte value for this Contato.
     * 
     * @param nomePaisPassaporte
     */
    public void setNomePaisPassaporte(java.lang.String nomePaisPassaporte) {
        this.nomePaisPassaporte = nomePaisPassaporte;
    }


    /**
     * Gets the matricula value for this Contato.
     * 
     * @return matricula
     */
    public java.lang.String getMatricula() {
        return matricula;
    }


    /**
     * Sets the matricula value for this Contato.
     * 
     * @param matricula
     */
    public void setMatricula(java.lang.String matricula) {
        this.matricula = matricula;
    }


    /**
     * Gets the matriculaOab value for this Contato.
     * 
     * @return matriculaOab
     */
    public java.lang.String getMatriculaOab() {
        return matriculaOab;
    }


    /**
     * Sets the matriculaOab value for this Contato.
     * 
     * @param matriculaOab
     */
    public void setMatriculaOab(java.lang.String matriculaOab) {
        this.matriculaOab = matriculaOab;
    }


    /**
     * Gets the telefoneComercial value for this Contato.
     * 
     * @return telefoneComercial
     */
    public java.lang.String getTelefoneComercial() {
        return telefoneComercial;
    }


    /**
     * Sets the telefoneComercial value for this Contato.
     * 
     * @param telefoneComercial
     */
    public void setTelefoneComercial(java.lang.String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }


    /**
     * Gets the telefoneResidencial value for this Contato.
     * 
     * @return telefoneResidencial
     */
    public java.lang.String getTelefoneResidencial() {
        return telefoneResidencial;
    }


    /**
     * Sets the telefoneResidencial value for this Contato.
     * 
     * @param telefoneResidencial
     */
    public void setTelefoneResidencial(java.lang.String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }


    /**
     * Gets the telefoneCelular value for this Contato.
     * 
     * @return telefoneCelular
     */
    public java.lang.String getTelefoneCelular() {
        return telefoneCelular;
    }


    /**
     * Sets the telefoneCelular value for this Contato.
     * 
     * @param telefoneCelular
     */
    public void setTelefoneCelular(java.lang.String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }


    /**
     * Gets the dataNascimento value for this Contato.
     * 
     * @return dataNascimento
     */
    public java.lang.String getDataNascimento() {
        return dataNascimento;
    }


    /**
     * Sets the dataNascimento value for this Contato.
     * 
     * @param dataNascimento
     */
    public void setDataNascimento(java.lang.String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }


    /**
     * Gets the email value for this Contato.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this Contato.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the sitioInternet value for this Contato.
     * 
     * @return sitioInternet
     */
    public java.lang.String getSitioInternet() {
        return sitioInternet;
    }


    /**
     * Sets the sitioInternet value for this Contato.
     * 
     * @param sitioInternet
     */
    public void setSitioInternet(java.lang.String sitioInternet) {
        this.sitioInternet = sitioInternet;
    }


    /**
     * Gets the observacao value for this Contato.
     * 
     * @return observacao
     */
    public java.lang.String getObservacao() {
        return observacao;
    }


    /**
     * Sets the observacao value for this Contato.
     * 
     * @param observacao
     */
    public void setObservacao(java.lang.String observacao) {
        this.observacao = observacao;
    }


    /**
     * Gets the conjuge value for this Contato.
     * 
     * @return conjuge
     */
    public java.lang.String getConjuge() {
        return conjuge;
    }


    /**
     * Sets the conjuge value for this Contato.
     * 
     * @param conjuge
     */
    public void setConjuge(java.lang.String conjuge) {
        this.conjuge = conjuge;
    }


    /**
     * Gets the funcao value for this Contato.
     * 
     * @return funcao
     */
    public java.lang.String getFuncao() {
        return funcao;
    }


    /**
     * Sets the funcao value for this Contato.
     * 
     * @param funcao
     */
    public void setFuncao(java.lang.String funcao) {
        this.funcao = funcao;
    }


    /**
     * Gets the idTitulo value for this Contato.
     * 
     * @return idTitulo
     */
    public java.lang.String getIdTitulo() {
        return idTitulo;
    }


    /**
     * Sets the idTitulo value for this Contato.
     * 
     * @param idTitulo
     */
    public void setIdTitulo(java.lang.String idTitulo) {
        this.idTitulo = idTitulo;
    }


    /**
     * Gets the expressaoTitulo value for this Contato.
     * 
     * @return expressaoTitulo
     */
    public java.lang.String getExpressaoTitulo() {
        return expressaoTitulo;
    }


    /**
     * Sets the expressaoTitulo value for this Contato.
     * 
     * @param expressaoTitulo
     */
    public void setExpressaoTitulo(java.lang.String expressaoTitulo) {
        this.expressaoTitulo = expressaoTitulo;
    }


    /**
     * Gets the abreviaturaTitulo value for this Contato.
     * 
     * @return abreviaturaTitulo
     */
    public java.lang.String getAbreviaturaTitulo() {
        return abreviaturaTitulo;
    }


    /**
     * Sets the abreviaturaTitulo value for this Contato.
     * 
     * @param abreviaturaTitulo
     */
    public void setAbreviaturaTitulo(java.lang.String abreviaturaTitulo) {
        this.abreviaturaTitulo = abreviaturaTitulo;
    }


    /**
     * Gets the sinAtivo value for this Contato.
     * 
     * @return sinAtivo
     */
    public java.lang.String getSinAtivo() {
        return sinAtivo;
    }


    /**
     * Sets the sinAtivo value for this Contato.
     * 
     * @param sinAtivo
     */
    public void setSinAtivo(java.lang.String sinAtivo) {
        this.sinAtivo = sinAtivo;
    }


    /**
     * Gets the idCategoria value for this Contato.
     * 
     * @return idCategoria
     */
    public java.lang.String getIdCategoria() {
        return idCategoria;
    }


    /**
     * Sets the idCategoria value for this Contato.
     * 
     * @param idCategoria
     */
    public void setIdCategoria(java.lang.String idCategoria) {
        this.idCategoria = idCategoria;
    }


    /**
     * Gets the idNomeCategoria value for this Contato.
     * 
     * @return idNomeCategoria
     */
    public java.lang.String getIdNomeCategoria() {
        return idNomeCategoria;
    }


    /**
     * Sets the idNomeCategoria value for this Contato.
     * 
     * @param idNomeCategoria
     */
    public void setIdNomeCategoria(java.lang.String idNomeCategoria) {
        this.idNomeCategoria = idNomeCategoria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Contato)) return false;
        Contato other = (Contato) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.staOperacao==null && other.getStaOperacao()==null) || 
             (this.staOperacao!=null &&
              this.staOperacao.equals(other.getStaOperacao()))) &&
            ((this.idContato==null && other.getIdContato()==null) || 
             (this.idContato!=null &&
              this.idContato.equals(other.getIdContato()))) &&
            ((this.idTipoContato==null && other.getIdTipoContato()==null) || 
             (this.idTipoContato!=null &&
              this.idTipoContato.equals(other.getIdTipoContato()))) &&
            ((this.nomeTipoContato==null && other.getNomeTipoContato()==null) || 
             (this.nomeTipoContato!=null &&
              this.nomeTipoContato.equals(other.getNomeTipoContato()))) &&
            ((this.sigla==null && other.getSigla()==null) || 
             (this.sigla!=null &&
              this.sigla.equals(other.getSigla()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.nomeSocial==null && other.getNomeSocial()==null) || 
             (this.nomeSocial!=null &&
              this.nomeSocial.equals(other.getNomeSocial()))) &&
            ((this.staNatureza==null && other.getStaNatureza()==null) || 
             (this.staNatureza!=null &&
              this.staNatureza.equals(other.getStaNatureza()))) &&
            ((this.idContatoAssociado==null && other.getIdContatoAssociado()==null) || 
             (this.idContatoAssociado!=null &&
              this.idContatoAssociado.equals(other.getIdContatoAssociado()))) &&
            ((this.nomeContatoAssociado==null && other.getNomeContatoAssociado()==null) || 
             (this.nomeContatoAssociado!=null &&
              this.nomeContatoAssociado.equals(other.getNomeContatoAssociado()))) &&
            ((this.sinEnderecoAssociado==null && other.getSinEnderecoAssociado()==null) || 
             (this.sinEnderecoAssociado!=null &&
              this.sinEnderecoAssociado.equals(other.getSinEnderecoAssociado()))) &&
            ((this.cnpjAssociado==null && other.getCnpjAssociado()==null) || 
             (this.cnpjAssociado!=null &&
              this.cnpjAssociado.equals(other.getCnpjAssociado()))) &&
            ((this.endereco==null && other.getEndereco()==null) || 
             (this.endereco!=null &&
              this.endereco.equals(other.getEndereco()))) &&
            ((this.complemento==null && other.getComplemento()==null) || 
             (this.complemento!=null &&
              this.complemento.equals(other.getComplemento()))) &&
            ((this.bairro==null && other.getBairro()==null) || 
             (this.bairro!=null &&
              this.bairro.equals(other.getBairro()))) &&
            ((this.idCidade==null && other.getIdCidade()==null) || 
             (this.idCidade!=null &&
              this.idCidade.equals(other.getIdCidade()))) &&
            ((this.nomeCidade==null && other.getNomeCidade()==null) || 
             (this.nomeCidade!=null &&
              this.nomeCidade.equals(other.getNomeCidade()))) &&
            ((this.idEstado==null && other.getIdEstado()==null) || 
             (this.idEstado!=null &&
              this.idEstado.equals(other.getIdEstado()))) &&
            ((this.siglaEstado==null && other.getSiglaEstado()==null) || 
             (this.siglaEstado!=null &&
              this.siglaEstado.equals(other.getSiglaEstado()))) &&
            ((this.idPais==null && other.getIdPais()==null) || 
             (this.idPais!=null &&
              this.idPais.equals(other.getIdPais()))) &&
            ((this.nomePais==null && other.getNomePais()==null) || 
             (this.nomePais!=null &&
              this.nomePais.equals(other.getNomePais()))) &&
            ((this.cep==null && other.getCep()==null) || 
             (this.cep!=null &&
              this.cep.equals(other.getCep()))) &&
            ((this.staGenero==null && other.getStaGenero()==null) || 
             (this.staGenero!=null &&
              this.staGenero.equals(other.getStaGenero()))) &&
            ((this.idCargo==null && other.getIdCargo()==null) || 
             (this.idCargo!=null &&
              this.idCargo.equals(other.getIdCargo()))) &&
            ((this.expressaoCargo==null && other.getExpressaoCargo()==null) || 
             (this.expressaoCargo!=null &&
              this.expressaoCargo.equals(other.getExpressaoCargo()))) &&
            ((this.expressaoTratamento==null && other.getExpressaoTratamento()==null) || 
             (this.expressaoTratamento!=null &&
              this.expressaoTratamento.equals(other.getExpressaoTratamento()))) &&
            ((this.expressaoVocativo==null && other.getExpressaoVocativo()==null) || 
             (this.expressaoVocativo!=null &&
              this.expressaoVocativo.equals(other.getExpressaoVocativo()))) &&
            ((this.cpf==null && other.getCpf()==null) || 
             (this.cpf!=null &&
              this.cpf.equals(other.getCpf()))) &&
            ((this.cnpj==null && other.getCnpj()==null) || 
             (this.cnpj!=null &&
              this.cnpj.equals(other.getCnpj()))) &&
            ((this.rg==null && other.getRg()==null) || 
             (this.rg!=null &&
              this.rg.equals(other.getRg()))) &&
            ((this.orgaoExpedidor==null && other.getOrgaoExpedidor()==null) || 
             (this.orgaoExpedidor!=null &&
              this.orgaoExpedidor.equals(other.getOrgaoExpedidor()))) &&
            ((this.numeroPassaporte==null && other.getNumeroPassaporte()==null) || 
             (this.numeroPassaporte!=null &&
              this.numeroPassaporte.equals(other.getNumeroPassaporte()))) &&
            ((this.idPaisPassaporte==null && other.getIdPaisPassaporte()==null) || 
             (this.idPaisPassaporte!=null &&
              this.idPaisPassaporte.equals(other.getIdPaisPassaporte()))) &&
            ((this.nomePaisPassaporte==null && other.getNomePaisPassaporte()==null) || 
             (this.nomePaisPassaporte!=null &&
              this.nomePaisPassaporte.equals(other.getNomePaisPassaporte()))) &&
            ((this.matricula==null && other.getMatricula()==null) || 
             (this.matricula!=null &&
              this.matricula.equals(other.getMatricula()))) &&
            ((this.matriculaOab==null && other.getMatriculaOab()==null) || 
             (this.matriculaOab!=null &&
              this.matriculaOab.equals(other.getMatriculaOab()))) &&
            ((this.telefoneComercial==null && other.getTelefoneComercial()==null) || 
             (this.telefoneComercial!=null &&
              this.telefoneComercial.equals(other.getTelefoneComercial()))) &&
            ((this.telefoneResidencial==null && other.getTelefoneResidencial()==null) || 
             (this.telefoneResidencial!=null &&
              this.telefoneResidencial.equals(other.getTelefoneResidencial()))) &&
            ((this.telefoneCelular==null && other.getTelefoneCelular()==null) || 
             (this.telefoneCelular!=null &&
              this.telefoneCelular.equals(other.getTelefoneCelular()))) &&
            ((this.dataNascimento==null && other.getDataNascimento()==null) || 
             (this.dataNascimento!=null &&
              this.dataNascimento.equals(other.getDataNascimento()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.sitioInternet==null && other.getSitioInternet()==null) || 
             (this.sitioInternet!=null &&
              this.sitioInternet.equals(other.getSitioInternet()))) &&
            ((this.observacao==null && other.getObservacao()==null) || 
             (this.observacao!=null &&
              this.observacao.equals(other.getObservacao()))) &&
            ((this.conjuge==null && other.getConjuge()==null) || 
             (this.conjuge!=null &&
              this.conjuge.equals(other.getConjuge()))) &&
            ((this.funcao==null && other.getFuncao()==null) || 
             (this.funcao!=null &&
              this.funcao.equals(other.getFuncao()))) &&
            ((this.idTitulo==null && other.getIdTitulo()==null) || 
             (this.idTitulo!=null &&
              this.idTitulo.equals(other.getIdTitulo()))) &&
            ((this.expressaoTitulo==null && other.getExpressaoTitulo()==null) || 
             (this.expressaoTitulo!=null &&
              this.expressaoTitulo.equals(other.getExpressaoTitulo()))) &&
            ((this.abreviaturaTitulo==null && other.getAbreviaturaTitulo()==null) || 
             (this.abreviaturaTitulo!=null &&
              this.abreviaturaTitulo.equals(other.getAbreviaturaTitulo()))) &&
            ((this.sinAtivo==null && other.getSinAtivo()==null) || 
             (this.sinAtivo!=null &&
              this.sinAtivo.equals(other.getSinAtivo()))) &&
            ((this.idCategoria==null && other.getIdCategoria()==null) || 
             (this.idCategoria!=null &&
              this.idCategoria.equals(other.getIdCategoria()))) &&
            ((this.idNomeCategoria==null && other.getIdNomeCategoria()==null) || 
             (this.idNomeCategoria!=null &&
              this.idNomeCategoria.equals(other.getIdNomeCategoria())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getStaOperacao() != null) {
            _hashCode += getStaOperacao().hashCode();
        }
        if (getIdContato() != null) {
            _hashCode += getIdContato().hashCode();
        }
        if (getIdTipoContato() != null) {
            _hashCode += getIdTipoContato().hashCode();
        }
        if (getNomeTipoContato() != null) {
            _hashCode += getNomeTipoContato().hashCode();
        }
        if (getSigla() != null) {
            _hashCode += getSigla().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getNomeSocial() != null) {
            _hashCode += getNomeSocial().hashCode();
        }
        if (getStaNatureza() != null) {
            _hashCode += getStaNatureza().hashCode();
        }
        if (getIdContatoAssociado() != null) {
            _hashCode += getIdContatoAssociado().hashCode();
        }
        if (getNomeContatoAssociado() != null) {
            _hashCode += getNomeContatoAssociado().hashCode();
        }
        if (getSinEnderecoAssociado() != null) {
            _hashCode += getSinEnderecoAssociado().hashCode();
        }
        if (getCnpjAssociado() != null) {
            _hashCode += getCnpjAssociado().hashCode();
        }
        if (getEndereco() != null) {
            _hashCode += getEndereco().hashCode();
        }
        if (getComplemento() != null) {
            _hashCode += getComplemento().hashCode();
        }
        if (getBairro() != null) {
            _hashCode += getBairro().hashCode();
        }
        if (getIdCidade() != null) {
            _hashCode += getIdCidade().hashCode();
        }
        if (getNomeCidade() != null) {
            _hashCode += getNomeCidade().hashCode();
        }
        if (getIdEstado() != null) {
            _hashCode += getIdEstado().hashCode();
        }
        if (getSiglaEstado() != null) {
            _hashCode += getSiglaEstado().hashCode();
        }
        if (getIdPais() != null) {
            _hashCode += getIdPais().hashCode();
        }
        if (getNomePais() != null) {
            _hashCode += getNomePais().hashCode();
        }
        if (getCep() != null) {
            _hashCode += getCep().hashCode();
        }
        if (getStaGenero() != null) {
            _hashCode += getStaGenero().hashCode();
        }
        if (getIdCargo() != null) {
            _hashCode += getIdCargo().hashCode();
        }
        if (getExpressaoCargo() != null) {
            _hashCode += getExpressaoCargo().hashCode();
        }
        if (getExpressaoTratamento() != null) {
            _hashCode += getExpressaoTratamento().hashCode();
        }
        if (getExpressaoVocativo() != null) {
            _hashCode += getExpressaoVocativo().hashCode();
        }
        if (getCpf() != null) {
            _hashCode += getCpf().hashCode();
        }
        if (getCnpj() != null) {
            _hashCode += getCnpj().hashCode();
        }
        if (getRg() != null) {
            _hashCode += getRg().hashCode();
        }
        if (getOrgaoExpedidor() != null) {
            _hashCode += getOrgaoExpedidor().hashCode();
        }
        if (getNumeroPassaporte() != null) {
            _hashCode += getNumeroPassaporte().hashCode();
        }
        if (getIdPaisPassaporte() != null) {
            _hashCode += getIdPaisPassaporte().hashCode();
        }
        if (getNomePaisPassaporte() != null) {
            _hashCode += getNomePaisPassaporte().hashCode();
        }
        if (getMatricula() != null) {
            _hashCode += getMatricula().hashCode();
        }
        if (getMatriculaOab() != null) {
            _hashCode += getMatriculaOab().hashCode();
        }
        if (getTelefoneComercial() != null) {
            _hashCode += getTelefoneComercial().hashCode();
        }
        if (getTelefoneResidencial() != null) {
            _hashCode += getTelefoneResidencial().hashCode();
        }
        if (getTelefoneCelular() != null) {
            _hashCode += getTelefoneCelular().hashCode();
        }
        if (getDataNascimento() != null) {
            _hashCode += getDataNascimento().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getSitioInternet() != null) {
            _hashCode += getSitioInternet().hashCode();
        }
        if (getObservacao() != null) {
            _hashCode += getObservacao().hashCode();
        }
        if (getConjuge() != null) {
            _hashCode += getConjuge().hashCode();
        }
        if (getFuncao() != null) {
            _hashCode += getFuncao().hashCode();
        }
        if (getIdTitulo() != null) {
            _hashCode += getIdTitulo().hashCode();
        }
        if (getExpressaoTitulo() != null) {
            _hashCode += getExpressaoTitulo().hashCode();
        }
        if (getAbreviaturaTitulo() != null) {
            _hashCode += getAbreviaturaTitulo().hashCode();
        }
        if (getSinAtivo() != null) {
            _hashCode += getSinAtivo().hashCode();
        }
        if (getIdCategoria() != null) {
            _hashCode += getIdCategoria().hashCode();
        }
        if (getIdNomeCategoria() != null) {
            _hashCode += getIdNomeCategoria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Contato.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Contato"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("staOperacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StaOperacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idContato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdContato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoContato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTipoContato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeTipoContato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeTipoContato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sigla");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Sigla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeSocial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeSocial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("staNatureza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StaNatureza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idContatoAssociado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdContatoAssociado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeContatoAssociado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeContatoAssociado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinEnderecoAssociado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinEnderecoAssociado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cnpjAssociado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CnpjAssociado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endereco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Endereco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("complemento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Complemento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bairro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Bairro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdCidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeCidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeCidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEstado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdEstado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siglaEstado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SiglaEstado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdPais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomePais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomePais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Cep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("staGenero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "StaGenero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCargo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdCargo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressaoCargo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExpressaoCargo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressaoTratamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExpressaoTratamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressaoVocativo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExpressaoVocativo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Cpf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cnpj");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Cnpj"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Rg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orgaoExpedidor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OrgaoExpedidor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroPassaporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroPassaporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPaisPassaporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdPaisPassaporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomePaisPassaporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomePaisPassaporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matricula");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Matricula"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matriculaOab");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MatriculaOab"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefoneComercial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TelefoneComercial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefoneResidencial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TelefoneResidencial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefoneCelular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TelefoneCelular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataNascimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DataNascimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sitioInternet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SitioInternet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Observacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conjuge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Conjuge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("funcao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Funcao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTitulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTitulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expressaoTitulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExpressaoTitulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abreviaturaTitulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AbreviaturaTitulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinAtivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinAtivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCategoria");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdCategoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNomeCategoria");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdNomeCategoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
