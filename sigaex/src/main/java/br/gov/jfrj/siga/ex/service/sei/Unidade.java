/**
 * Unidade.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Unidade  implements java.io.Serializable {
    private java.lang.String idUnidade;

    private java.lang.String sigla;

    private java.lang.String descricao;

    private java.lang.String sinProtocolo;

    private java.lang.String sinArquivamento;

    private java.lang.String sinOuvidoria;

    public Unidade() {
    }

    public Unidade(
           java.lang.String idUnidade,
           java.lang.String sigla,
           java.lang.String descricao,
           java.lang.String sinProtocolo,
           java.lang.String sinArquivamento,
           java.lang.String sinOuvidoria) {
           this.idUnidade = idUnidade;
           this.sigla = sigla;
           this.descricao = descricao;
           this.sinProtocolo = sinProtocolo;
           this.sinArquivamento = sinArquivamento;
           this.sinOuvidoria = sinOuvidoria;
    }


    /**
     * Gets the idUnidade value for this Unidade.
     * 
     * @return idUnidade
     */
    public java.lang.String getIdUnidade() {
        return idUnidade;
    }


    /**
     * Sets the idUnidade value for this Unidade.
     * 
     * @param idUnidade
     */
    public void setIdUnidade(java.lang.String idUnidade) {
        this.idUnidade = idUnidade;
    }


    /**
     * Gets the sigla value for this Unidade.
     * 
     * @return sigla
     */
    public java.lang.String getSigla() {
        return sigla;
    }


    /**
     * Sets the sigla value for this Unidade.
     * 
     * @param sigla
     */
    public void setSigla(java.lang.String sigla) {
        this.sigla = sigla;
    }


    /**
     * Gets the descricao value for this Unidade.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this Unidade.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }


    /**
     * Gets the sinProtocolo value for this Unidade.
     * 
     * @return sinProtocolo
     */
    public java.lang.String getSinProtocolo() {
        return sinProtocolo;
    }


    /**
     * Sets the sinProtocolo value for this Unidade.
     * 
     * @param sinProtocolo
     */
    public void setSinProtocolo(java.lang.String sinProtocolo) {
        this.sinProtocolo = sinProtocolo;
    }


    /**
     * Gets the sinArquivamento value for this Unidade.
     * 
     * @return sinArquivamento
     */
    public java.lang.String getSinArquivamento() {
        return sinArquivamento;
    }


    /**
     * Sets the sinArquivamento value for this Unidade.
     * 
     * @param sinArquivamento
     */
    public void setSinArquivamento(java.lang.String sinArquivamento) {
        this.sinArquivamento = sinArquivamento;
    }


    /**
     * Gets the sinOuvidoria value for this Unidade.
     * 
     * @return sinOuvidoria
     */
    public java.lang.String getSinOuvidoria() {
        return sinOuvidoria;
    }


    /**
     * Sets the sinOuvidoria value for this Unidade.
     * 
     * @param sinOuvidoria
     */
    public void setSinOuvidoria(java.lang.String sinOuvidoria) {
        this.sinOuvidoria = sinOuvidoria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Unidade)) return false;
        Unidade other = (Unidade) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idUnidade==null && other.getIdUnidade()==null) || 
             (this.idUnidade!=null &&
              this.idUnidade.equals(other.getIdUnidade()))) &&
            ((this.sigla==null && other.getSigla()==null) || 
             (this.sigla!=null &&
              this.sigla.equals(other.getSigla()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao()))) &&
            ((this.sinProtocolo==null && other.getSinProtocolo()==null) || 
             (this.sinProtocolo!=null &&
              this.sinProtocolo.equals(other.getSinProtocolo()))) &&
            ((this.sinArquivamento==null && other.getSinArquivamento()==null) || 
             (this.sinArquivamento!=null &&
              this.sinArquivamento.equals(other.getSinArquivamento()))) &&
            ((this.sinOuvidoria==null && other.getSinOuvidoria()==null) || 
             (this.sinOuvidoria!=null &&
              this.sinOuvidoria.equals(other.getSinOuvidoria())));
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
        if (getIdUnidade() != null) {
            _hashCode += getIdUnidade().hashCode();
        }
        if (getSigla() != null) {
            _hashCode += getSigla().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        if (getSinProtocolo() != null) {
            _hashCode += getSinProtocolo().hashCode();
        }
        if (getSinArquivamento() != null) {
            _hashCode += getSinArquivamento().hashCode();
        }
        if (getSinOuvidoria() != null) {
            _hashCode += getSinOuvidoria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Unidade.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Unidade"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUnidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdUnidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sigla");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Sigla"));
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
        elemField.setFieldName("sinProtocolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinProtocolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinArquivamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinArquivamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinOuvidoria");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinOuvidoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
