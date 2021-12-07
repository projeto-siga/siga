/**
 * TipoConferencia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class TipoConferencia  implements java.io.Serializable {
    private java.lang.String idTipoConferencia;

    private java.lang.String descricao;

    public TipoConferencia() {
    }

    public TipoConferencia(
           java.lang.String idTipoConferencia,
           java.lang.String descricao) {
           this.idTipoConferencia = idTipoConferencia;
           this.descricao = descricao;
    }


    /**
     * Gets the idTipoConferencia value for this TipoConferencia.
     * 
     * @return idTipoConferencia
     */
    public java.lang.String getIdTipoConferencia() {
        return idTipoConferencia;
    }


    /**
     * Sets the idTipoConferencia value for this TipoConferencia.
     * 
     * @param idTipoConferencia
     */
    public void setIdTipoConferencia(java.lang.String idTipoConferencia) {
        this.idTipoConferencia = idTipoConferencia;
    }


    /**
     * Gets the descricao value for this TipoConferencia.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this TipoConferencia.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoConferencia)) return false;
        TipoConferencia other = (TipoConferencia) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idTipoConferencia==null && other.getIdTipoConferencia()==null) || 
             (this.idTipoConferencia!=null &&
              this.idTipoConferencia.equals(other.getIdTipoConferencia()))) &&
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
        if (getIdTipoConferencia() != null) {
            _hashCode += getIdTipoConferencia().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoConferencia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "TipoConferencia"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoConferencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTipoConferencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descricao"));
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
