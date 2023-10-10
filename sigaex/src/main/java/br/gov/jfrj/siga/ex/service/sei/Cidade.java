/**
 * Cidade.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Cidade  implements java.io.Serializable {
    private java.lang.String idCidade;

    private java.lang.String idEstado;

    private java.lang.String idPais;

    private java.lang.String nome;

    private java.lang.String codigoIbge;

    private java.lang.String sinCapital;

    private java.lang.String latitude;

    private java.lang.String longitude;

    public Cidade() {
    }

    public Cidade(
           java.lang.String idCidade,
           java.lang.String idEstado,
           java.lang.String idPais,
           java.lang.String nome,
           java.lang.String codigoIbge,
           java.lang.String sinCapital,
           java.lang.String latitude,
           java.lang.String longitude) {
           this.idCidade = idCidade;
           this.idEstado = idEstado;
           this.idPais = idPais;
           this.nome = nome;
           this.codigoIbge = codigoIbge;
           this.sinCapital = sinCapital;
           this.latitude = latitude;
           this.longitude = longitude;
    }


    /**
     * Gets the idCidade value for this Cidade.
     * 
     * @return idCidade
     */
    public java.lang.String getIdCidade() {
        return idCidade;
    }


    /**
     * Sets the idCidade value for this Cidade.
     * 
     * @param idCidade
     */
    public void setIdCidade(java.lang.String idCidade) {
        this.idCidade = idCidade;
    }


    /**
     * Gets the idEstado value for this Cidade.
     * 
     * @return idEstado
     */
    public java.lang.String getIdEstado() {
        return idEstado;
    }


    /**
     * Sets the idEstado value for this Cidade.
     * 
     * @param idEstado
     */
    public void setIdEstado(java.lang.String idEstado) {
        this.idEstado = idEstado;
    }


    /**
     * Gets the idPais value for this Cidade.
     * 
     * @return idPais
     */
    public java.lang.String getIdPais() {
        return idPais;
    }


    /**
     * Sets the idPais value for this Cidade.
     * 
     * @param idPais
     */
    public void setIdPais(java.lang.String idPais) {
        this.idPais = idPais;
    }


    /**
     * Gets the nome value for this Cidade.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Cidade.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the codigoIbge value for this Cidade.
     * 
     * @return codigoIbge
     */
    public java.lang.String getCodigoIbge() {
        return codigoIbge;
    }


    /**
     * Sets the codigoIbge value for this Cidade.
     * 
     * @param codigoIbge
     */
    public void setCodigoIbge(java.lang.String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }


    /**
     * Gets the sinCapital value for this Cidade.
     * 
     * @return sinCapital
     */
    public java.lang.String getSinCapital() {
        return sinCapital;
    }


    /**
     * Sets the sinCapital value for this Cidade.
     * 
     * @param sinCapital
     */
    public void setSinCapital(java.lang.String sinCapital) {
        this.sinCapital = sinCapital;
    }


    /**
     * Gets the latitude value for this Cidade.
     * 
     * @return latitude
     */
    public java.lang.String getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this Cidade.
     * 
     * @param latitude
     */
    public void setLatitude(java.lang.String latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this Cidade.
     * 
     * @return longitude
     */
    public java.lang.String getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this Cidade.
     * 
     * @param longitude
     */
    public void setLongitude(java.lang.String longitude) {
        this.longitude = longitude;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Cidade)) return false;
        Cidade other = (Cidade) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idCidade==null && other.getIdCidade()==null) || 
             (this.idCidade!=null &&
              this.idCidade.equals(other.getIdCidade()))) &&
            ((this.idEstado==null && other.getIdEstado()==null) || 
             (this.idEstado!=null &&
              this.idEstado.equals(other.getIdEstado()))) &&
            ((this.idPais==null && other.getIdPais()==null) || 
             (this.idPais!=null &&
              this.idPais.equals(other.getIdPais()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.codigoIbge==null && other.getCodigoIbge()==null) || 
             (this.codigoIbge!=null &&
              this.codigoIbge.equals(other.getCodigoIbge()))) &&
            ((this.sinCapital==null && other.getSinCapital()==null) || 
             (this.sinCapital!=null &&
              this.sinCapital.equals(other.getSinCapital()))) &&
            ((this.latitude==null && other.getLatitude()==null) || 
             (this.latitude!=null &&
              this.latitude.equals(other.getLatitude()))) &&
            ((this.longitude==null && other.getLongitude()==null) || 
             (this.longitude!=null &&
              this.longitude.equals(other.getLongitude())));
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
        if (getIdCidade() != null) {
            _hashCode += getIdCidade().hashCode();
        }
        if (getIdEstado() != null) {
            _hashCode += getIdEstado().hashCode();
        }
        if (getIdPais() != null) {
            _hashCode += getIdPais().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getCodigoIbge() != null) {
            _hashCode += getCodigoIbge().hashCode();
        }
        if (getSinCapital() != null) {
            _hashCode += getSinCapital().hashCode();
        }
        if (getLatitude() != null) {
            _hashCode += getLatitude().hashCode();
        }
        if (getLongitude() != null) {
            _hashCode += getLongitude().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Cidade.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Cidade"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdCidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEstado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdEstado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdPais"));
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
        elemField.setFieldName("codigoIbge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CodigoIbge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sinCapital");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SinCapital"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Longitude"));
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
