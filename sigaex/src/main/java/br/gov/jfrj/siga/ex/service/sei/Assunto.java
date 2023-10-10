/**
 * Assunto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Assunto  implements java.io.Serializable {
    private java.lang.String codigoEstruturado;

    private java.lang.String descricao;

    public Assunto() {
    }

    public Assunto(
           java.lang.String codigoEstruturado,
           java.lang.String descricao) {
           this.codigoEstruturado = codigoEstruturado;
           this.descricao = descricao;
    }


    /**
     * Gets the codigoEstruturado value for this Assunto.
     * 
     * @return codigoEstruturado
     */
    public java.lang.String getCodigoEstruturado() {
        return codigoEstruturado;
    }


    /**
     * Sets the codigoEstruturado value for this Assunto.
     * 
     * @param codigoEstruturado
     */
    public void setCodigoEstruturado(java.lang.String codigoEstruturado) {
        this.codigoEstruturado = codigoEstruturado;
    }


    /**
     * Gets the descricao value for this Assunto.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this Assunto.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Assunto)) return false;
        Assunto other = (Assunto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoEstruturado==null && other.getCodigoEstruturado()==null) || 
             (this.codigoEstruturado!=null &&
              this.codigoEstruturado.equals(other.getCodigoEstruturado()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao())));
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
        if (getCodigoEstruturado() != null) {
            _hashCode += getCodigoEstruturado().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Assunto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Assunto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoEstruturado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoEstruturado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descricao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
