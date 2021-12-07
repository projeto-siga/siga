/**
 * RetornoConsultaDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class RetornoConsultaDocumento  implements java.io.Serializable {
    private java.lang.String idProcedimento;

    private java.lang.String procedimentoFormatado;

    private java.lang.String idDocumento;

    private java.lang.String documentoFormatado;

    private java.lang.String linkAcesso;

    private java.lang.String nivelAcessoLocal;

    private java.lang.String nivelAcessoGlobal;

    private br.gov.jfrj.siga.ex.service.sei.Serie serie;

    private java.lang.String numero;

    private java.lang.String nomeArvore;

    private java.lang.String descricao;

    private java.lang.String data;

    private br.gov.jfrj.siga.ex.service.sei.Unidade unidadeElaboradora;

    private br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao;

    private br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas;

    private br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao;

    private br.gov.jfrj.siga.ex.service.sei.Campo[] campos;

    public RetornoConsultaDocumento() {
    }

    public RetornoConsultaDocumento(
           java.lang.String idProcedimento,
           java.lang.String procedimentoFormatado,
           java.lang.String idDocumento,
           java.lang.String documentoFormatado,
           java.lang.String linkAcesso,
           java.lang.String nivelAcessoLocal,
           java.lang.String nivelAcessoGlobal,
           br.gov.jfrj.siga.ex.service.sei.Serie serie,
           java.lang.String numero,
           java.lang.String nomeArvore,
           java.lang.String descricao,
           java.lang.String data,
           br.gov.jfrj.siga.ex.service.sei.Unidade unidadeElaboradora,
           br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao,
           br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas,
           br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao,
           br.gov.jfrj.siga.ex.service.sei.Campo[] campos) {
           this.idProcedimento = idProcedimento;
           this.procedimentoFormatado = procedimentoFormatado;
           this.idDocumento = idDocumento;
           this.documentoFormatado = documentoFormatado;
           this.linkAcesso = linkAcesso;
           this.nivelAcessoLocal = nivelAcessoLocal;
           this.nivelAcessoGlobal = nivelAcessoGlobal;
           this.serie = serie;
           this.numero = numero;
           this.nomeArvore = nomeArvore;
           this.descricao = descricao;
           this.data = data;
           this.unidadeElaboradora = unidadeElaboradora;
           this.andamentoGeracao = andamentoGeracao;
           this.assinaturas = assinaturas;
           this.publicacao = publicacao;
           this.campos = campos;
    }


    /**
     * Gets the idProcedimento value for this RetornoConsultaDocumento.
     * 
     * @return idProcedimento
     */
    public java.lang.String getIdProcedimento() {
        return idProcedimento;
    }


    /**
     * Sets the idProcedimento value for this RetornoConsultaDocumento.
     * 
     * @param idProcedimento
     */
    public void setIdProcedimento(java.lang.String idProcedimento) {
        this.idProcedimento = idProcedimento;
    }


    /**
     * Gets the procedimentoFormatado value for this RetornoConsultaDocumento.
     * 
     * @return procedimentoFormatado
     */
    public java.lang.String getProcedimentoFormatado() {
        return procedimentoFormatado;
    }


    /**
     * Sets the procedimentoFormatado value for this RetornoConsultaDocumento.
     * 
     * @param procedimentoFormatado
     */
    public void setProcedimentoFormatado(java.lang.String procedimentoFormatado) {
        this.procedimentoFormatado = procedimentoFormatado;
    }


    /**
     * Gets the idDocumento value for this RetornoConsultaDocumento.
     * 
     * @return idDocumento
     */
    public java.lang.String getIdDocumento() {
        return idDocumento;
    }


    /**
     * Sets the idDocumento value for this RetornoConsultaDocumento.
     * 
     * @param idDocumento
     */
    public void setIdDocumento(java.lang.String idDocumento) {
        this.idDocumento = idDocumento;
    }


    /**
     * Gets the documentoFormatado value for this RetornoConsultaDocumento.
     * 
     * @return documentoFormatado
     */
    public java.lang.String getDocumentoFormatado() {
        return documentoFormatado;
    }


    /**
     * Sets the documentoFormatado value for this RetornoConsultaDocumento.
     * 
     * @param documentoFormatado
     */
    public void setDocumentoFormatado(java.lang.String documentoFormatado) {
        this.documentoFormatado = documentoFormatado;
    }


    /**
     * Gets the linkAcesso value for this RetornoConsultaDocumento.
     * 
     * @return linkAcesso
     */
    public java.lang.String getLinkAcesso() {
        return linkAcesso;
    }


    /**
     * Sets the linkAcesso value for this RetornoConsultaDocumento.
     * 
     * @param linkAcesso
     */
    public void setLinkAcesso(java.lang.String linkAcesso) {
        this.linkAcesso = linkAcesso;
    }


    /**
     * Gets the nivelAcessoLocal value for this RetornoConsultaDocumento.
     * 
     * @return nivelAcessoLocal
     */
    public java.lang.String getNivelAcessoLocal() {
        return nivelAcessoLocal;
    }


    /**
     * Sets the nivelAcessoLocal value for this RetornoConsultaDocumento.
     * 
     * @param nivelAcessoLocal
     */
    public void setNivelAcessoLocal(java.lang.String nivelAcessoLocal) {
        this.nivelAcessoLocal = nivelAcessoLocal;
    }


    /**
     * Gets the nivelAcessoGlobal value for this RetornoConsultaDocumento.
     * 
     * @return nivelAcessoGlobal
     */
    public java.lang.String getNivelAcessoGlobal() {
        return nivelAcessoGlobal;
    }


    /**
     * Sets the nivelAcessoGlobal value for this RetornoConsultaDocumento.
     * 
     * @param nivelAcessoGlobal
     */
    public void setNivelAcessoGlobal(java.lang.String nivelAcessoGlobal) {
        this.nivelAcessoGlobal = nivelAcessoGlobal;
    }


    /**
     * Gets the serie value for this RetornoConsultaDocumento.
     * 
     * @return serie
     */
    public br.gov.jfrj.siga.ex.service.sei.Serie getSerie() {
        return serie;
    }


    /**
     * Sets the serie value for this RetornoConsultaDocumento.
     * 
     * @param serie
     */
    public void setSerie(br.gov.jfrj.siga.ex.service.sei.Serie serie) {
        this.serie = serie;
    }


    /**
     * Gets the numero value for this RetornoConsultaDocumento.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this RetornoConsultaDocumento.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the nomeArvore value for this RetornoConsultaDocumento.
     * 
     * @return nomeArvore
     */
    public java.lang.String getNomeArvore() {
        return nomeArvore;
    }


    /**
     * Sets the nomeArvore value for this RetornoConsultaDocumento.
     * 
     * @param nomeArvore
     */
    public void setNomeArvore(java.lang.String nomeArvore) {
        this.nomeArvore = nomeArvore;
    }


    /**
     * Gets the descricao value for this RetornoConsultaDocumento.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this RetornoConsultaDocumento.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }


    /**
     * Gets the data value for this RetornoConsultaDocumento.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this RetornoConsultaDocumento.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }


    /**
     * Gets the unidadeElaboradora value for this RetornoConsultaDocumento.
     * 
     * @return unidadeElaboradora
     */
    public br.gov.jfrj.siga.ex.service.sei.Unidade getUnidadeElaboradora() {
        return unidadeElaboradora;
    }


    /**
     * Sets the unidadeElaboradora value for this RetornoConsultaDocumento.
     * 
     * @param unidadeElaboradora
     */
    public void setUnidadeElaboradora(br.gov.jfrj.siga.ex.service.sei.Unidade unidadeElaboradora) {
        this.unidadeElaboradora = unidadeElaboradora;
    }


    /**
     * Gets the andamentoGeracao value for this RetornoConsultaDocumento.
     * 
     * @return andamentoGeracao
     */
    public br.gov.jfrj.siga.ex.service.sei.Andamento getAndamentoGeracao() {
        return andamentoGeracao;
    }


    /**
     * Sets the andamentoGeracao value for this RetornoConsultaDocumento.
     * 
     * @param andamentoGeracao
     */
    public void setAndamentoGeracao(br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao) {
        this.andamentoGeracao = andamentoGeracao;
    }


    /**
     * Gets the assinaturas value for this RetornoConsultaDocumento.
     * 
     * @return assinaturas
     */
    public br.gov.jfrj.siga.ex.service.sei.Assinatura[] getAssinaturas() {
        return assinaturas;
    }


    /**
     * Sets the assinaturas value for this RetornoConsultaDocumento.
     * 
     * @param assinaturas
     */
    public void setAssinaturas(br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas) {
        this.assinaturas = assinaturas;
    }


    /**
     * Gets the publicacao value for this RetornoConsultaDocumento.
     * 
     * @return publicacao
     */
    public br.gov.jfrj.siga.ex.service.sei.Publicacao getPublicacao() {
        return publicacao;
    }


    /**
     * Sets the publicacao value for this RetornoConsultaDocumento.
     * 
     * @param publicacao
     */
    public void setPublicacao(br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao) {
        this.publicacao = publicacao;
    }


    /**
     * Gets the campos value for this RetornoConsultaDocumento.
     * 
     * @return campos
     */
    public br.gov.jfrj.siga.ex.service.sei.Campo[] getCampos() {
        return campos;
    }


    /**
     * Sets the campos value for this RetornoConsultaDocumento.
     * 
     * @param campos
     */
    public void setCampos(br.gov.jfrj.siga.ex.service.sei.Campo[] campos) {
        this.campos = campos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoConsultaDocumento)) return false;
        RetornoConsultaDocumento other = (RetornoConsultaDocumento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idProcedimento==null && other.getIdProcedimento()==null) || 
             (this.idProcedimento!=null &&
              this.idProcedimento.equals(other.getIdProcedimento()))) &&
            ((this.procedimentoFormatado==null && other.getProcedimentoFormatado()==null) || 
             (this.procedimentoFormatado!=null &&
              this.procedimentoFormatado.equals(other.getProcedimentoFormatado()))) &&
            ((this.idDocumento==null && other.getIdDocumento()==null) || 
             (this.idDocumento!=null &&
              this.idDocumento.equals(other.getIdDocumento()))) &&
            ((this.documentoFormatado==null && other.getDocumentoFormatado()==null) || 
             (this.documentoFormatado!=null &&
              this.documentoFormatado.equals(other.getDocumentoFormatado()))) &&
            ((this.linkAcesso==null && other.getLinkAcesso()==null) || 
             (this.linkAcesso!=null &&
              this.linkAcesso.equals(other.getLinkAcesso()))) &&
            ((this.nivelAcessoLocal==null && other.getNivelAcessoLocal()==null) || 
             (this.nivelAcessoLocal!=null &&
              this.nivelAcessoLocal.equals(other.getNivelAcessoLocal()))) &&
            ((this.nivelAcessoGlobal==null && other.getNivelAcessoGlobal()==null) || 
             (this.nivelAcessoGlobal!=null &&
              this.nivelAcessoGlobal.equals(other.getNivelAcessoGlobal()))) &&
            ((this.serie==null && other.getSerie()==null) || 
             (this.serie!=null &&
              this.serie.equals(other.getSerie()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.nomeArvore==null && other.getNomeArvore()==null) || 
             (this.nomeArvore!=null &&
              this.nomeArvore.equals(other.getNomeArvore()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.unidadeElaboradora==null && other.getUnidadeElaboradora()==null) || 
             (this.unidadeElaboradora!=null &&
              this.unidadeElaboradora.equals(other.getUnidadeElaboradora()))) &&
            ((this.andamentoGeracao==null && other.getAndamentoGeracao()==null) || 
             (this.andamentoGeracao!=null &&
              this.andamentoGeracao.equals(other.getAndamentoGeracao()))) &&
            ((this.assinaturas==null && other.getAssinaturas()==null) || 
             (this.assinaturas!=null &&
              java.util.Arrays.equals(this.assinaturas, other.getAssinaturas()))) &&
            ((this.publicacao==null && other.getPublicacao()==null) || 
             (this.publicacao!=null &&
              this.publicacao.equals(other.getPublicacao()))) &&
            ((this.campos==null && other.getCampos()==null) || 
             (this.campos!=null &&
              java.util.Arrays.equals(this.campos, other.getCampos())));
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
        if (getIdProcedimento() != null) {
            _hashCode += getIdProcedimento().hashCode();
        }
        if (getProcedimentoFormatado() != null) {
            _hashCode += getProcedimentoFormatado().hashCode();
        }
        if (getIdDocumento() != null) {
            _hashCode += getIdDocumento().hashCode();
        }
        if (getDocumentoFormatado() != null) {
            _hashCode += getDocumentoFormatado().hashCode();
        }
        if (getLinkAcesso() != null) {
            _hashCode += getLinkAcesso().hashCode();
        }
        if (getNivelAcessoLocal() != null) {
            _hashCode += getNivelAcessoLocal().hashCode();
        }
        if (getNivelAcessoGlobal() != null) {
            _hashCode += getNivelAcessoGlobal().hashCode();
        }
        if (getSerie() != null) {
            _hashCode += getSerie().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getNomeArvore() != null) {
            _hashCode += getNomeArvore().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getUnidadeElaboradora() != null) {
            _hashCode += getUnidadeElaboradora().hashCode();
        }
        if (getAndamentoGeracao() != null) {
            _hashCode += getAndamentoGeracao().hashCode();
        }
        if (getAssinaturas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAssinaturas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAssinaturas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPublicacao() != null) {
            _hashCode += getPublicacao().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoConsultaDocumento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "RetornoConsultaDocumento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimentoFormatado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProcedimentoFormatado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentoFormatado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DocumentoFormatado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkAcesso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LinkAcesso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcessoLocal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcessoLocal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcessoGlobal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcessoGlobal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Serie"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Serie"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeArvore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NomeArvore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descricao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadeElaboradora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UnidadeElaboradora"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Unidade"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("andamentoGeracao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AndamentoGeracao"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assinaturas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Assinaturas"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Assinatura"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Publicacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Publicacao"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Campos"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Campo"));
        elemField.setNillable(false);
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
