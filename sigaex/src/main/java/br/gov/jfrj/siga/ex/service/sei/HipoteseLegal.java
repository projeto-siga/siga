/**
 * HipoteseLegal.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class HipoteseLegal  implements java.io.Serializable {
    private java.lang.String idHipoteseLegal;

    private java.lang.String nome;

    private java.lang.String baseLegal;

    private java.lang.String nivelAcesso;

    public HipoteseLegal() {
    }

    public HipoteseLegal(
           java.lang.String idHipoteseLegal,
           java.lang.String nome,
           java.lang.String baseLegal,
           java.lang.String nivelAcesso) {
           this.idHipoteseLegal = idHipoteseLegal;
           this.nome = nome;
           this.baseLegal = baseLegal;
           this.nivelAcesso = nivelAcesso;
    }


    /**
     * Gets the idHipoteseLegal value for this HipoteseLegal.
     * 
     * @return idHipoteseLegal
     */
    public java.lang.String getIdHipoteseLegal() {
        return idHipoteseLegal;
    }


    /**
     * Sets the idHipoteseLegal value for this HipoteseLegal.
     * 
     * @param idHipoteseLegal
     */
    public void setIdHipoteseLegal(java.lang.String idHipoteseLegal) {
        this.idHipoteseLegal = idHipoteseLegal;
    }


    /**
     * Gets the nome value for this HipoteseLegal.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this HipoteseLegal.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the baseLegal value for this HipoteseLegal.
     * 
     * @return baseLegal
     */
    public java.lang.String getBaseLegal() {
        return baseLegal;
    }


    /**
     * Sets the baseLegal value for this HipoteseLegal.
     * 
     * @param baseLegal
     */
    public void setBaseLegal(java.lang.String baseLegal) {
        this.baseLegal = baseLegal;
    }


    /**
     * Gets the nivelAcesso value for this HipoteseLegal.
     * 
     * @return nivelAcesso
     */
    public java.lang.String getNivelAcesso() {
        return nivelAcesso;
    }


    /**
     * Sets the nivelAcesso value for this HipoteseLegal.
     * 
     * @param nivelAcesso
     */
    public void setNivelAcesso(java.lang.String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HipoteseLegal)) return false;
        HipoteseLegal other = (HipoteseLegal) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idHipoteseLegal==null && other.getIdHipoteseLegal()==null) || 
             (this.idHipoteseLegal!=null &&
              this.idHipoteseLegal.equals(other.getIdHipoteseLegal()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.baseLegal==null && other.getBaseLegal()==null) || 
             (this.baseLegal!=null &&
              this.baseLegal.equals(other.getBaseLegal()))) &&
            ((this.nivelAcesso==null && other.getNivelAcesso()==null) || 
             (this.nivelAcesso!=null &&
              this.nivelAcesso.equals(other.getNivelAcesso())));
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
        if (getIdHipoteseLegal() != null) {
            _hashCode += getIdHipoteseLegal().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getBaseLegal() != null) {
            _hashCode += getBaseLegal().hashCode();
        }
        if (getNivelAcesso() != null) {
            _hashCode += getNivelAcesso().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HipoteseLegal.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "HipoteseLegal"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHipoteseLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdHipoteseLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("baseLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BaseLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcesso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcesso"));
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
