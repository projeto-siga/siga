/**
 * ProtocoloBloco.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class ProtocoloBloco  implements java.io.Serializable {
    private java.lang.String protocoloFormatado;

    private java.lang.String identificacao;

    private br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas;

    public ProtocoloBloco() {
    }

    public ProtocoloBloco(
           java.lang.String protocoloFormatado,
           java.lang.String identificacao,
           br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas) {
           this.protocoloFormatado = protocoloFormatado;
           this.identificacao = identificacao;
           this.assinaturas = assinaturas;
    }


    /**
     * Gets the protocoloFormatado value for this ProtocoloBloco.
     * 
     * @return protocoloFormatado
     */
    public java.lang.String getProtocoloFormatado() {
        return protocoloFormatado;
    }


    /**
     * Sets the protocoloFormatado value for this ProtocoloBloco.
     * 
     * @param protocoloFormatado
     */
    public void setProtocoloFormatado(java.lang.String protocoloFormatado) {
        this.protocoloFormatado = protocoloFormatado;
    }


    /**
     * Gets the identificacao value for this ProtocoloBloco.
     * 
     * @return identificacao
     */
    public java.lang.String getIdentificacao() {
        return identificacao;
    }


    /**
     * Sets the identificacao value for this ProtocoloBloco.
     * 
     * @param identificacao
     */
    public void setIdentificacao(java.lang.String identificacao) {
        this.identificacao = identificacao;
    }


    /**
     * Gets the assinaturas value for this ProtocoloBloco.
     * 
     * @return assinaturas
     */
    public br.gov.jfrj.siga.ex.service.sei.Assinatura[] getAssinaturas() {
        return assinaturas;
    }


    /**
     * Sets the assinaturas value for this ProtocoloBloco.
     * 
     * @param assinaturas
     */
    public void setAssinaturas(br.gov.jfrj.siga.ex.service.sei.Assinatura[] assinaturas) {
        this.assinaturas = assinaturas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProtocoloBloco)) return false;
        ProtocoloBloco other = (ProtocoloBloco) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.protocoloFormatado==null && other.getProtocoloFormatado()==null) || 
             (this.protocoloFormatado!=null &&
              this.protocoloFormatado.equals(other.getProtocoloFormatado()))) &&
            ((this.identificacao==null && other.getIdentificacao()==null) || 
             (this.identificacao!=null &&
              this.identificacao.equals(other.getIdentificacao()))) &&
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
        if (getProtocoloFormatado() != null) {
            _hashCode += getProtocoloFormatado().hashCode();
        }
        if (getIdentificacao() != null) {
            _hashCode += getIdentificacao().hashCode();
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
        new org.apache.axis.description.TypeDesc(ProtocoloBloco.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "ProtocoloBloco"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocoloFormatado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProtocoloFormatado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Identificacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
