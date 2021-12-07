/**
 * Documento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Documento  implements java.io.Serializable {
    private java.lang.String tipo;

    private java.lang.String idProcedimento;

    private java.lang.String protocoloProcedimento;

    private java.lang.String idSerie;

    private java.lang.String numero;

    private java.lang.String nomeArvore;

    private java.lang.String data;

    private java.lang.String descricao;

    private java.lang.String idTipoConferencia;

    private java.lang.String sinArquivamento;

    private br.gov.jfrj.siga.ex.service.sei.Remetente remetente;

    private br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados;

    private br.gov.jfrj.siga.ex.service.sei.Destinatario[] destinatarios;

    private java.lang.String observacao;

    private java.lang.String nomeArquivo;

    private java.lang.String nivelAcesso;

    private java.lang.String idHipoteseLegal;

    private java.lang.String conteudo;

    private byte[] conteudoMTOM;

    private java.lang.String idArquivo;

    private br.gov.jfrj.siga.ex.service.sei.Campo[] campos;

    private java.lang.String sinBloqueado;

    public Documento() {
    }

    public Documento(
           java.lang.String tipo,
           java.lang.String idProcedimento,
           java.lang.String protocoloProcedimento,
           java.lang.String idSerie,
           java.lang.String numero,
           java.lang.String nomeArvore,
           java.lang.String data,
           java.lang.String descricao,
           java.lang.String idTipoConferencia,
           java.lang.String sinArquivamento,
           br.gov.jfrj.siga.ex.service.sei.Remetente remetente,
           br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados,
           br.gov.jfrj.siga.ex.service.sei.Destinatario[] destinatarios,
           java.lang.String observacao,
           java.lang.String nomeArquivo,
           java.lang.String nivelAcesso,
           java.lang.String idHipoteseLegal,
           java.lang.String conteudo,
           byte[] conteudoMTOM,
           java.lang.String idArquivo,
           br.gov.jfrj.siga.ex.service.sei.Campo[] campos,
           java.lang.String sinBloqueado) {
           this.tipo = tipo;
           this.idProcedimento = idProcedimento;
           this.protocoloProcedimento = protocoloProcedimento;
           this.idSerie = idSerie;
           this.numero = numero;
           this.nomeArvore = nomeArvore;
           this.data = data;
           this.descricao = descricao;
           this.idTipoConferencia = idTipoConferencia;
           this.sinArquivamento = sinArquivamento;
           this.remetente = remetente;
           this.interessados = interessados;
           this.destinatarios = destinatarios;
           this.observacao = observacao;
           this.nomeArquivo = nomeArquivo;
           this.nivelAcesso = nivelAcesso;
           this.idHipoteseLegal = idHipoteseLegal;
           this.conteudo = conteudo;
           this.conteudoMTOM = conteudoMTOM;
           this.idArquivo = idArquivo;
           this.campos = campos;
           this.sinBloqueado = sinBloqueado;
    }


    /**
     * Gets the tipo value for this Documento.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this Documento.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the idProcedimento value for this Documento.
     * 
     * @return idProcedimento
     */
    public java.lang.String getIdProcedimento() {
        return idProcedimento;
    }


    /**
     * Sets the idProcedimento value for this Documento.
     * 
     * @param idProcedimento
     */
    public void setIdProcedimento(java.lang.String idProcedimento) {
        this.idProcedimento = idProcedimento;
    }


    /**
     * Gets the protocoloProcedimento value for this Documento.
     * 
     * @return protocoloProcedimento
     */
    public java.lang.String getProtocoloProcedimento() {
        return protocoloProcedimento;
    }


    /**
     * Sets the protocoloProcedimento value for this Documento.
     * 
     * @param protocoloProcedimento
     */
    public void setProtocoloProcedimento(java.lang.String protocoloProcedimento) {
        this.protocoloProcedimento = protocoloProcedimento;
    }


    /**
     * Gets the idSerie value for this Documento.
     * 
     * @return idSerie
     */
    public java.lang.String getIdSerie() {
        return idSerie;
    }


    /**
     * Sets the idSerie value for this Documento.
     * 
     * @param idSerie
     */
    public void setIdSerie(java.lang.String idSerie) {
        this.idSerie = idSerie;
    }


    /**
     * Gets the numero value for this Documento.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this Documento.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the nomeArvore value for this Documento.
     * 
     * @return nomeArvore
     */
    public java.lang.String getNomeArvore() {
        return nomeArvore;
    }


    /**
     * Sets the nomeArvore value for this Documento.
     * 
     * @param nomeArvore
     */
    public void setNomeArvore(java.lang.String nomeArvore) {
        this.nomeArvore = nomeArvore;
    }


    /**
     * Gets the data value for this Documento.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this Documento.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }


    /**
     * Gets the descricao value for this Documento.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this Documento.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }


    /**
     * Gets the idTipoConferencia value for this Documento.
     * 
     * @return idTipoConferencia
     */
    public java.lang.String getIdTipoConferencia() {
        return idTipoConferencia;
    }


    /**
     * Sets the idTipoConferencia value for this Documento.
     * 
     * @param idTipoConferencia
     */
    public void setIdTipoConferencia(java.lang.String idTipoConferencia) {
        this.idTipoConferencia = idTipoConferencia;
    }


    /**
     * Gets the sinArquivamento value for this Documento.
     * 
     * @return sinArquivamento
     */
    public java.lang.String getSinArquivamento() {
        return sinArquivamento;
    }


    /**
     * Sets the sinArquivamento value for this Documento.
     * 
     * @param sinArquivamento
     */
    public void setSinArquivamento(java.lang.String sinArquivamento) {
        this.sinArquivamento = sinArquivamento;
    }


    /**
     * Gets the remetente value for this Documento.
     * 
     * @return remetente
     */
    public br.gov.jfrj.siga.ex.service.sei.Remetente getRemetente() {
        return remetente;
    }


    /**
     * Sets the remetente value for this Documento.
     * 
     * @param remetente
     */
    public void setRemetente(br.gov.jfrj.siga.ex.service.sei.Remetente remetente) {
        this.remetente = remetente;
    }


    /**
     * Gets the interessados value for this Documento.
     * 
     * @return interessados
     */
    public br.gov.jfrj.siga.ex.service.sei.Interessado[] getInteressados() {
        return interessados;
    }


    /**
     * Sets the interessados value for this Documento.
     * 
     * @param interessados
     */
    public void setInteressados(br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados) {
        this.interessados = interessados;
    }


    /**
     * Gets the destinatarios value for this Documento.
     * 
     * @return destinatarios
     */
    public br.gov.jfrj.siga.ex.service.sei.Destinatario[] getDestinatarios() {
        return destinatarios;
    }


    /**
     * Sets the destinatarios value for this Documento.
     * 
     * @param destinatarios
     */
    public void setDestinatarios(br.gov.jfrj.siga.ex.service.sei.Destinatario[] destinatarios) {
        this.destinatarios = destinatarios;
    }


    /**
     * Gets the observacao value for this Documento.
     * 
     * @return observacao
     */
    public java.lang.String getObservacao() {
        return observacao;
    }


    /**
     * Sets the observacao value for this Documento.
     * 
     * @param observacao
     */
    public void setObservacao(java.lang.String observacao) {
        this.observacao = observacao;
    }


    /**
     * Gets the nomeArquivo value for this Documento.
     * 
     * @return nomeArquivo
     */
    public java.lang.String getNomeArquivo() {
        return nomeArquivo;
    }


    /**
     * Sets the nomeArquivo value for this Documento.
     * 
     * @param nomeArquivo
     */
    public void setNomeArquivo(java.lang.String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }


    /**
     * Gets the nivelAcesso value for this Documento.
     * 
     * @return nivelAcesso
     */
    public java.lang.String getNivelAcesso() {
        return nivelAcesso;
    }


    /**
     * Sets the nivelAcesso value for this Documento.
     * 
     * @param nivelAcesso
     */
    public void setNivelAcesso(java.lang.String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }


    /**
     * Gets the idHipoteseLegal value for this Documento.
     * 
     * @return idHipoteseLegal
     */
    public java.lang.String getIdHipoteseLegal() {
        return idHipoteseLegal;
    }


    /**
     * Sets the idHipoteseLegal value for this Documento.
     * 
     * @param idHipoteseLegal
     */
    public void setIdHipoteseLegal(java.lang.String idHipoteseLegal) {
        this.idHipoteseLegal = idHipoteseLegal;
    }


    /**
     * Gets the conteudo value for this Documento.
     * 
     * @return conteudo
     */
    public java.lang.String getConteudo() {
        return conteudo;
    }


    /**
     * Sets the conteudo value for this Documento.
     * 
     * @param conteudo
     */
    public void setConteudo(java.lang.String conteudo) {
        this.conteudo = conteudo;
    }


    /**
     * Gets the conteudoMTOM value for this Documento.
     * 
     * @return conteudoMTOM
     */
    public byte[] getConteudoMTOM() {
        return conteudoMTOM;
    }


    /**
     * Sets the conteudoMTOM value for this Documento.
     * 
     * @param conteudoMTOM
     */
    public void setConteudoMTOM(byte[] conteudoMTOM) {
        this.conteudoMTOM = conteudoMTOM;
    }


    /**
     * Gets the idArquivo value for this Documento.
     * 
     * @return idArquivo
     */
    public java.lang.String getIdArquivo() {
        return idArquivo;
    }


    /**
     * Sets the idArquivo value for this Documento.
     * 
     * @param idArquivo
     */
    public void setIdArquivo(java.lang.String idArquivo) {
        this.idArquivo = idArquivo;
    }


    /**
     * Gets the campos value for this Documento.
     * 
     * @return campos
     */
    public br.gov.jfrj.siga.ex.service.sei.Campo[] getCampos() {
        return campos;
    }


    /**
     * Sets the campos value for this Documento.
     * 
     * @param campos
     */
    public void setCampos(br.gov.jfrj.siga.ex.service.sei.Campo[] campos) {
        this.campos = campos;
    }


    /**
     * Gets the sinBloqueado value for this Documento.
     * 
     * @return sinBloqueado
     */
    public java.lang.String getSinBloqueado() {
        return sinBloqueado;
    }


    /**
     * Sets the sinBloqueado value for this Documento.
     * 
     * @param sinBloqueado
     */
    public void setSinBloqueado(java.lang.String sinBloqueado) {
        this.sinBloqueado = sinBloqueado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Documento)) return false;
        Documento other = (Documento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.idProcedimento==null && other.getIdProcedimento()==null) || 
             (this.idProcedimento!=null &&
              this.idProcedimento.equals(other.getIdProcedimento()))) &&
            ((this.protocoloProcedimento==null && other.getProtocoloProcedimento()==null) || 
             (this.protocoloProcedimento!=null &&
              this.protocoloProcedimento.equals(other.getProtocoloProcedimento()))) &&
            ((this.idSerie==null && other.getIdSerie()==null) || 
             (this.idSerie!=null &&
              this.idSerie.equals(other.getIdSerie()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.nomeArvore==null && other.getNomeArvore()==null) || 
             (this.nomeArvore!=null &&
              this.nomeArvore.equals(other.getNomeArvore()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao()))) &&
            ((this.idTipoConferencia==null && other.getIdTipoConferencia()==null) || 
             (this.idTipoConferencia!=null &&
              this.idTipoConferencia.equals(other.getIdTipoConferencia()))) &&
            ((this.sinArquivamento==null && other.getSinArquivamento()==null) || 
             (this.sinArquivamento!=null &&
              this.sinArquivamento.equals(other.getSinArquivamento()))) &&
            ((this.remetente==null && other.getRemetente()==null) || 
             (this.remetente!=null &&
              this.remetente.equals(other.getRemetente()))) &&
            ((this.interessados==null && other.getInteressados()==null) || 
             (this.interessados!=null &&
              java.util.Arrays.equals(this.interessados, other.getInteressados()))) &&
            ((this.destinatarios==null && other.getDestinatarios()==null) || 
             (this.destinatarios!=null &&
              java.util.Arrays.equals(this.destinatarios, other.getDestinatarios()))) &&
            ((this.observacao==null && other.getObservacao()==null) || 
             (this.observacao!=null &&
              this.observacao.equals(other.getObservacao()))) &&
            ((this.nomeArquivo==null && other.getNomeArquivo()==null) || 
             (this.nomeArquivo!=null &&
              this.nomeArquivo.equals(other.getNomeArquivo()))) &&
            ((this.nivelAcesso==null && other.getNivelAcesso()==null) || 
             (this.nivelAcesso!=null &&
              this.nivelAcesso.equals(other.getNivelAcesso()))) &&
            ((this.idHipoteseLegal==null && other.getIdHipoteseLegal()==null) || 
             (this.idHipoteseLegal!=null &&
              this.idHipoteseLegal.equals(other.getIdHipoteseLegal()))) &&
            ((this.conteudo==null && other.getConteudo()==null) || 
             (this.conteudo!=null &&
              this.conteudo.equals(other.getConteudo()))) &&
            ((this.conteudoMTOM==null && other.getConteudoMTOM()==null) || 
             (this.conteudoMTOM!=null &&
              java.util.Arrays.equals(this.conteudoMTOM, other.getConteudoMTOM()))) &&
            ((this.idArquivo==null && other.getIdArquivo()==null) || 
             (this.idArquivo!=null &&
              this.idArquivo.equals(other.getIdArquivo()))) &&
            ((this.campos==null && other.getCampos()==null) || 
             (this.campos!=null &&
              java.util.Arrays.equals(this.campos, other.getCampos()))) &&
            ((this.sinBloqueado==null && other.getSinBloqueado()==null) || 
             (this.sinBloqueado!=null &&
              this.sinBloqueado.equals(other.getSinBloqueado())));
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
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getIdProcedimento() != null) {
            _hashCode += getIdProcedimento().hashCode();
        }
        if (getProtocoloProcedimento() != null) {
            _hashCode += getProtocoloProcedimento().hashCode();
        }
        if (getIdSerie() != null) {
            _hashCode += getIdSerie().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getNomeArvore() != null) {
            _hashCode += getNomeArvore().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        if (getIdTipoConferencia() != null) {
            _hashCode += getIdTipoConferencia().hashCode();
        }
        if (getSinArquivamento() != null) {
            _hashCode += getSinArquivamento().hashCode();
        }
        if (getRemetente() != null) {
            _hashCode += getRemetente().hashCode();
        }
        if (getInteressados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInteressados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInteressados(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDestinatarios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestinatarios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestinatarios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getObservacao() != null) {
            _hashCode += getObservacao().hashCode();
        }
        if (getNomeArquivo() != null) {
            _hashCode += getNomeArquivo().hashCode();
        }
        if (getNivelAcesso() != null) {
            _hashCode += getNivelAcesso().hashCode();
        }
        if (getIdHipoteseLegal() != null) {
            _hashCode += getIdHipoteseLegal().hashCode();
        }
        if (getConteudo() != null) {
            _hashCode += getConteudo().hashCode();
        }
        if (getConteudoMTOM() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConteudoMTOM());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConteudoMTOM(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdArquivo() != null) {
            _hashCode += getIdArquivo().hashCode();
        }
        if (getCampos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCampos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCampos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSinBloqueado() != null) {
            _hashCode += getSinBloqueado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Documento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Documento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocoloProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProtocoloProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSerie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdSerie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeArvore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeArvore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descricao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoConferencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTipoConferencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinArquivamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinArquivamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remetente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Remetente"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Remetente"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interessados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Interessados"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Interessado"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Destinatarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Destinatario"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Observacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeArquivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeArquivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcesso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcesso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHipoteseLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdHipoteseLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conteudo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Conteudo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conteudoMTOM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ConteudoMTOM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArquivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdArquivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Campos"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Campo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinBloqueado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinBloqueado"));
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
