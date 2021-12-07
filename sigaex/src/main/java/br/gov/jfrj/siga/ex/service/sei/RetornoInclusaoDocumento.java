/**
 * RetornoInclusaoDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class RetornoInclusaoDocumento  implements java.io.Serializable {
    private java.lang.String idDocumento;

    private java.lang.String documentoFormatado;

    private java.lang.String linkAcesso;

    public RetornoInclusaoDocumento() {
    }

    public RetornoInclusaoDocumento(
           java.lang.String idDocumento,
           java.lang.String documentoFormatado,
           java.lang.String linkAcesso) {
           this.idDocumento = idDocumento;
           this.documentoFormatado = documentoFormatado;
           this.linkAcesso = linkAcesso;
    }


    /**
     * Gets the idDocumento value for this RetornoInclusaoDocumento.
     * 
     * @return idDocumento
     */
    public java.lang.String getIdDocumento() {
        return idDocumento;
    }


    /**
     * Sets the idDocumento value for this RetornoInclusaoDocumento.
     * 
     * @param idDocumento
     */
    public void setIdDocumento(java.lang.String idDocumento) {
        this.idDocumento = idDocumento;
    }


    /**
     * Gets the documentoFormatado value for this RetornoInclusaoDocumento.
     * 
     * @return documentoFormatado
     */
    public java.lang.String getDocumentoFormatado() {
        return documentoFormatado;
    }


    /**
     * Sets the documentoFormatado value for this RetornoInclusaoDocumento.
     * 
     * @param documentoFormatado
     */
    public void setDocumentoFormatado(java.lang.String documentoFormatado) {
        this.documentoFormatado = documentoFormatado;
    }


    /**
     * Gets the linkAcesso value for this RetornoInclusaoDocumento.
     * 
     * @return linkAcesso
     */
    public java.lang.String getLinkAcesso() {
        return linkAcesso;
    }


    /**
     * Sets the linkAcesso value for this RetornoInclusaoDocumento.
     * 
     * @param linkAcesso
     */
    public void setLinkAcesso(java.lang.String linkAcesso) {
        this.linkAcesso = linkAcesso;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoInclusaoDocumento)) return false;
        RetornoInclusaoDocumento other = (RetornoInclusaoDocumento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idDocumento==null && other.getIdDocumento()==null) || 
             (this.idDocumento!=null &&
              this.idDocumento.equals(other.getIdDocumento()))) &&
            ((this.documentoFormatado==null && other.getDocumentoFormatado()==null) || 
             (this.documentoFormatado!=null &&
              this.documentoFormatado.equals(other.getDocumentoFormatado()))) &&
            ((this.linkAcesso==null && other.getLinkAcesso()==null) || 
             (this.linkAcesso!=null &&
              this.linkAcesso.equals(other.getLinkAcesso())));
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
        if (getIdDocumento() != null) {
            _hashCode += getIdDocumento().hashCode();
        }
        if (getDocumentoFormatado() != null) {
            _hashCode += getDocumentoFormatado().hashCode();
        }
        if (getLinkAcesso() != null) {
            _hashCode += getLinkAcesso().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetornoInclusaoDocumento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "RetornoInclusaoDocumento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
