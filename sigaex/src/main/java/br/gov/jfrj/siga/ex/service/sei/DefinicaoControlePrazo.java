/**
 * DefinicaoControlePrazo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class DefinicaoControlePrazo  implements java.io.Serializable {
    private java.lang.String protocoloProcedimento;

    private java.lang.String dataPrazo;

    private java.lang.String dias;

    private java.lang.String sinDiasUteis;

    public DefinicaoControlePrazo() {
    }

    public DefinicaoControlePrazo(
           java.lang.String protocoloProcedimento,
           java.lang.String dataPrazo,
           java.lang.String dias,
           java.lang.String sinDiasUteis) {
           this.protocoloProcedimento = protocoloProcedimento;
           this.dataPrazo = dataPrazo;
           this.dias = dias;
           this.sinDiasUteis = sinDiasUteis;
    }


    /**
     * Gets the protocoloProcedimento value for this DefinicaoControlePrazo.
     * 
     * @return protocoloProcedimento
     */
    public java.lang.String getProtocoloProcedimento() {
        return protocoloProcedimento;
    }


    /**
     * Sets the protocoloProcedimento value for this DefinicaoControlePrazo.
     * 
     * @param protocoloProcedimento
     */
    public void setProtocoloProcedimento(java.lang.String protocoloProcedimento) {
        this.protocoloProcedimento = protocoloProcedimento;
    }


    /**
     * Gets the dataPrazo value for this DefinicaoControlePrazo.
     * 
     * @return dataPrazo
     */
    public java.lang.String getDataPrazo() {
        return dataPrazo;
    }


    /**
     * Sets the dataPrazo value for this DefinicaoControlePrazo.
     * 
     * @param dataPrazo
     */
    public void setDataPrazo(java.lang.String dataPrazo) {
        this.dataPrazo = dataPrazo;
    }


    /**
     * Gets the dias value for this DefinicaoControlePrazo.
     * 
     * @return dias
     */
    public java.lang.String getDias() {
        return dias;
    }


    /**
     * Sets the dias value for this DefinicaoControlePrazo.
     * 
     * @param dias
     */
    public void setDias(java.lang.String dias) {
        this.dias = dias;
    }


    /**
     * Gets the sinDiasUteis value for this DefinicaoControlePrazo.
     * 
     * @return sinDiasUteis
     */
    public java.lang.String getSinDiasUteis() {
        return sinDiasUteis;
    }


    /**
     * Sets the sinDiasUteis value for this DefinicaoControlePrazo.
     * 
     * @param sinDiasUteis
     */
    public void setSinDiasUteis(java.lang.String sinDiasUteis) {
        this.sinDiasUteis = sinDiasUteis;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DefinicaoControlePrazo)) return false;
        DefinicaoControlePrazo other = (DefinicaoControlePrazo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.protocoloProcedimento==null && other.getProtocoloProcedimento()==null) || 
             (this.protocoloProcedimento!=null &&
              this.protocoloProcedimento.equals(other.getProtocoloProcedimento()))) &&
            ((this.dataPrazo==null && other.getDataPrazo()==null) || 
             (this.dataPrazo!=null &&
              this.dataPrazo.equals(other.getDataPrazo()))) &&
            ((this.dias==null && other.getDias()==null) || 
             (this.dias!=null &&
              this.dias.equals(other.getDias()))) &&
            ((this.sinDiasUteis==null && other.getSinDiasUteis()==null) || 
             (this.sinDiasUteis!=null &&
              this.sinDiasUteis.equals(other.getSinDiasUteis())));
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
        if (getProtocoloProcedimento() != null) {
            _hashCode += getProtocoloProcedimento().hashCode();
        }
        if (getDataPrazo() != null) {
            _hashCode += getDataPrazo().hashCode();
        }
        if (getDias() != null) {
            _hashCode += getDias().hashCode();
        }
        if (getSinDiasUteis() != null) {
            _hashCode += getSinDiasUteis().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DefinicaoControlePrazo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "DefinicaoControlePrazo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protocoloProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProtocoloProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataPrazo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DataPrazo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Dias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinDiasUteis");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinDiasUteis"));
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
