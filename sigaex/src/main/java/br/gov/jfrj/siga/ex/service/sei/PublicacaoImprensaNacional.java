/**
 * PublicacaoImprensaNacional.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class PublicacaoImprensaNacional  implements java.io.Serializable {
    private java.lang.String idVeiculo;

    private java.lang.String siglaVeiculo;

    private java.lang.String descricaoVeiculo;

    private java.lang.String pagina;

    private java.lang.String idSecao;

    private java.lang.String secao;

    private java.lang.String data;

    public PublicacaoImprensaNacional() {
    }

    public PublicacaoImprensaNacional(
           java.lang.String idVeiculo,
           java.lang.String siglaVeiculo,
           java.lang.String descricaoVeiculo,
           java.lang.String pagina,
           java.lang.String idSecao,
           java.lang.String secao,
           java.lang.String data) {
           this.idVeiculo = idVeiculo;
           this.siglaVeiculo = siglaVeiculo;
           this.descricaoVeiculo = descricaoVeiculo;
           this.pagina = pagina;
           this.idSecao = idSecao;
           this.secao = secao;
           this.data = data;
    }


    /**
     * Gets the idVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @return idVeiculo
     */
    public java.lang.String getIdVeiculo() {
        return idVeiculo;
    }


    /**
     * Sets the idVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @param idVeiculo
     */
    public void setIdVeiculo(java.lang.String idVeiculo) {
        this.idVeiculo = idVeiculo;
    }


    /**
     * Gets the siglaVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @return siglaVeiculo
     */
    public java.lang.String getSiglaVeiculo() {
        return siglaVeiculo;
    }


    /**
     * Sets the siglaVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @param siglaVeiculo
     */
    public void setSiglaVeiculo(java.lang.String siglaVeiculo) {
        this.siglaVeiculo = siglaVeiculo;
    }


    /**
     * Gets the descricaoVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @return descricaoVeiculo
     */
    public java.lang.String getDescricaoVeiculo() {
        return descricaoVeiculo;
    }


    /**
     * Sets the descricaoVeiculo value for this PublicacaoImprensaNacional.
     * 
     * @param descricaoVeiculo
     */
    public void setDescricaoVeiculo(java.lang.String descricaoVeiculo) {
        this.descricaoVeiculo = descricaoVeiculo;
    }


    /**
     * Gets the pagina value for this PublicacaoImprensaNacional.
     * 
     * @return pagina
     */
    public java.lang.String getPagina() {
        return pagina;
    }


    /**
     * Sets the pagina value for this PublicacaoImprensaNacional.
     * 
     * @param pagina
     */
    public void setPagina(java.lang.String pagina) {
        this.pagina = pagina;
    }


    /**
     * Gets the idSecao value for this PublicacaoImprensaNacional.
     * 
     * @return idSecao
     */
    public java.lang.String getIdSecao() {
        return idSecao;
    }


    /**
     * Sets the idSecao value for this PublicacaoImprensaNacional.
     * 
     * @param idSecao
     */
    public void setIdSecao(java.lang.String idSecao) {
        this.idSecao = idSecao;
    }


    /**
     * Gets the secao value for this PublicacaoImprensaNacional.
     * 
     * @return secao
     */
    public java.lang.String getSecao() {
        return secao;
    }


    /**
     * Sets the secao value for this PublicacaoImprensaNacional.
     * 
     * @param secao
     */
    public void setSecao(java.lang.String secao) {
        this.secao = secao;
    }


    /**
     * Gets the data value for this PublicacaoImprensaNacional.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this PublicacaoImprensaNacional.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PublicacaoImprensaNacional)) return false;
        PublicacaoImprensaNacional other = (PublicacaoImprensaNacional) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idVeiculo==null && other.getIdVeiculo()==null) || 
             (this.idVeiculo!=null &&
              this.idVeiculo.equals(other.getIdVeiculo()))) &&
            ((this.siglaVeiculo==null && other.getSiglaVeiculo()==null) || 
             (this.siglaVeiculo!=null &&
              this.siglaVeiculo.equals(other.getSiglaVeiculo()))) &&
            ((this.descricaoVeiculo==null && other.getDescricaoVeiculo()==null) || 
             (this.descricaoVeiculo!=null &&
              this.descricaoVeiculo.equals(other.getDescricaoVeiculo()))) &&
            ((this.pagina==null && other.getPagina()==null) || 
             (this.pagina!=null &&
              this.pagina.equals(other.getPagina()))) &&
            ((this.idSecao==null && other.getIdSecao()==null) || 
             (this.idSecao!=null &&
              this.idSecao.equals(other.getIdSecao()))) &&
            ((this.secao==null && other.getSecao()==null) || 
             (this.secao!=null &&
              this.secao.equals(other.getSecao()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData())));
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
        if (getIdVeiculo() != null) {
            _hashCode += getIdVeiculo().hashCode();
        }
        if (getSiglaVeiculo() != null) {
            _hashCode += getSiglaVeiculo().hashCode();
        }
        if (getDescricaoVeiculo() != null) {
            _hashCode += getDescricaoVeiculo().hashCode();
        }
        if (getPagina() != null) {
            _hashCode += getPagina().hashCode();
        }
        if (getIdSecao() != null) {
            _hashCode += getIdSecao().hashCode();
        }
        if (getSecao() != null) {
            _hashCode += getSecao().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PublicacaoImprensaNacional.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "PublicacaoImprensaNacional"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idVeiculo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdVeiculo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siglaVeiculo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SiglaVeiculo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricaoVeiculo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DescricaoVeiculo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Pagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSecao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdSecao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Secao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
