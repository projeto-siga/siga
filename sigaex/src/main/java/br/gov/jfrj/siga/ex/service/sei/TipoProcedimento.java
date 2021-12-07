/**
 * TipoProcedimento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class TipoProcedimento  implements java.io.Serializable {
    private java.lang.String idTipoProcedimento;

    private java.lang.String nome;

    public TipoProcedimento() {
    }

    public TipoProcedimento(
           java.lang.String idTipoProcedimento,
           java.lang.String nome) {
           this.idTipoProcedimento = idTipoProcedimento;
           this.nome = nome;
    }


    /**
     * Gets the idTipoProcedimento value for this TipoProcedimento.
     * 
     * @return idTipoProcedimento
     */
    public java.lang.String getIdTipoProcedimento() {
        return idTipoProcedimento;
    }


    /**
     * Sets the idTipoProcedimento value for this TipoProcedimento.
     * 
     * @param idTipoProcedimento
     */
    public void setIdTipoProcedimento(java.lang.String idTipoProcedimento) {
        this.idTipoProcedimento = idTipoProcedimento;
    }


    /**
     * Gets the nome value for this TipoProcedimento.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this TipoProcedimento.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoProcedimento)) return false;
        TipoProcedimento other = (TipoProcedimento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idTipoProcedimento==null && other.getIdTipoProcedimento()==null) || 
             (this.idTipoProcedimento!=null &&
              this.idTipoProcedimento.equals(other.getIdTipoProcedimento()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome())));
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
        if (getIdTipoProcedimento() != null) {
            _hashCode += getIdTipoProcedimento().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoProcedimento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "TipoProcedimento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTipoProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Nome"));
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
