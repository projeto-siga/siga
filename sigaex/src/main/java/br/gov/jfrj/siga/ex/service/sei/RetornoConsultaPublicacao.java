/**
 * RetornoConsultaPublicacao.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class RetornoConsultaPublicacao  implements java.io.Serializable {
    private br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao;

    private br.gov.jfrj.siga.ex.service.sei.Andamento andamento;

    private br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas;

    public RetornoConsultaPublicacao() {
    }

    public RetornoConsultaPublicacao(
           br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao,
           br.gov.jfrj.siga.ex.service.sei.Andamento andamento,
           br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas) {
           this.publicacao = publicacao;
           this.andamento = andamento;
           this.assinaturas = assinaturas;
    }


    /**
     * Gets the publicacao value for this RetornoConsultaPublicacao.
     * 
     * @return publicacao
     */
    public br.gov.jfrj.siga.ex.service.sei.Publicacao getPublicacao() {
        return publicacao;
    }


    /**
     * Sets the publicacao value for this RetornoConsultaPublicacao.
     * 
     * @param publicacao
     */
    public void setPublicacao(br.gov.jfrj.siga.ex.service.sei.Publicacao publicacao) {
        this.publicacao = publicacao;
    }


    /**
     * Gets the andamento value for this RetornoConsultaPublicacao.
     * 
     * @return andamento
     */
    public br.gov.jfrj.siga.ex.service.sei.Andamento getAndamento() {
        return andamento;
    }


    /**
     * Sets the andamento value for this RetornoConsultaPublicacao.
     * 
     * @param andamento
     */
    public void setAndamento(br.gov.jfrj.siga.ex.service.sei.Andamento andamento) {
        this.andamento = andamento;
    }


    /**
     * Gets the assinaturas value for this RetornoConsultaPublicacao.
     * 
     * @return assinaturas
     */
    public br.gov.jfrj.siga.ex.service.sei.Assinatura[] getAssinaturas() {
        return assinaturas;
    }


    /**
     * Sets the assinaturas value for this RetornoConsultaPublicacao.
     * 
     * @param assinaturas
     */
    public void setAssinaturas(br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas) {
        this.assinaturas = assinaturas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoConsultaPublicacao)) return false;
        RetornoConsultaPublicacao other = (RetornoConsultaPublicacao) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.publicacao==null && other.getPublicacao()==null) || 
             (this.publicacao!=null &&
              this.publicacao.equals(other.getPublicacao()))) &&
            ((this.andamento==null && other.getAndamento()==null) || 
             (this.andamento!=null &&
              this.andamento.equals(other.getAndamento()))) &&
            ((this.assinaturas==null && other.getAssinaturas()==null) || 
             (this.assinaturas!=null &&
              java.util.Arrays.equals(this.assinaturas, other.getAssinaturas())));
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
        if (getPublicacao() != null) {
            _hashCode += getPublicacao().hashCode();
        }
        if (getAndamento() != null) {
            _hashCode += getAndamento().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoConsultaPublicacao.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "RetornoConsultaPublicacao"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Publicacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Publicacao"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("andamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Andamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assinaturas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Assinaturas"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Assinatura"));
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
