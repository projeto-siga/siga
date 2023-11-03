/**
 * AndamentoMarcador.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class AndamentoMarcador  implements java.io.Serializable {
    private java.lang.String idAndamentoMarcador;

    private java.lang.String texto;

    private java.lang.String dataHora;

    private br.gov.jfrj.siga.ex.service.sei.Usuario usuario;

    private br.gov.jfrj.siga.ex.service.sei.Marcador marcador;

    public AndamentoMarcador() {
    }

    public AndamentoMarcador(
           java.lang.String idAndamentoMarcador,
           java.lang.String texto,
           java.lang.String dataHora,
           br.gov.jfrj.siga.ex.service.sei.Usuario usuario,
           br.gov.jfrj.siga.ex.service.sei.Marcador marcador) {
           this.idAndamentoMarcador = idAndamentoMarcador;
           this.texto = texto;
           this.dataHora = dataHora;
           this.usuario = usuario;
           this.marcador = marcador;
    }


    /**
     * Gets the idAndamentoMarcador value for this AndamentoMarcador.
     * 
     * @return idAndamentoMarcador
     */
    public java.lang.String getIdAndamentoMarcador() {
        return idAndamentoMarcador;
    }


    /**
     * Sets the idAndamentoMarcador value for this AndamentoMarcador.
     * 
     * @param idAndamentoMarcador
     */
    public void setIdAndamentoMarcador(java.lang.String idAndamentoMarcador) {
        this.idAndamentoMarcador = idAndamentoMarcador;
    }


    /**
     * Gets the texto value for this AndamentoMarcador.
     * 
     * @return texto
     */
    public java.lang.String getTexto() {
        return texto;
    }


    /**
     * Sets the texto value for this AndamentoMarcador.
     * 
     * @param texto
     */
    public void setTexto(java.lang.String texto) {
        this.texto = texto;
    }


    /**
     * Gets the dataHora value for this AndamentoMarcador.
     * 
     * @return dataHora
     */
    public java.lang.String getDataHora() {
        return dataHora;
    }


    /**
     * Sets the dataHora value for this AndamentoMarcador.
     * 
     * @param dataHora
     */
    public void setDataHora(java.lang.String dataHora) {
        this.dataHora = dataHora;
    }


    /**
     * Gets the usuario value for this AndamentoMarcador.
     * 
     * @return usuario
     */
    public br.gov.jfrj.siga.ex.service.sei.Usuario getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this AndamentoMarcador.
     * 
     * @param usuario
     */
    public void setUsuario(br.gov.jfrj.siga.ex.service.sei.Usuario usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the marcador value for this AndamentoMarcador.
     * 
     * @return marcador
     */
    public br.gov.jfrj.siga.ex.service.sei.Marcador getMarcador() {
        return marcador;
    }


    /**
     * Sets the marcador value for this AndamentoMarcador.
     * 
     * @param marcador
     */
    public void setMarcador(br.gov.jfrj.siga.ex.service.sei.Marcador marcador) {
        this.marcador = marcador;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AndamentoMarcador)) return false;
        AndamentoMarcador other = (AndamentoMarcador) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idAndamentoMarcador==null && other.getIdAndamentoMarcador()==null) || 
             (this.idAndamentoMarcador!=null &&
              this.idAndamentoMarcador.equals(other.getIdAndamentoMarcador()))) &&
            ((this.texto==null && other.getTexto()==null) || 
             (this.texto!=null &&
              this.texto.equals(other.getTexto()))) &&
            ((this.dataHora==null && other.getDataHora()==null) || 
             (this.dataHora!=null &&
              this.dataHora.equals(other.getDataHora()))) &&
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario()))) &&
            ((this.marcador==null && other.getMarcador()==null) || 
             (this.marcador!=null &&
              this.marcador.equals(other.getMarcador())));
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
        if (getIdAndamentoMarcador() != null) {
            _hashCode += getIdAndamentoMarcador().hashCode();
        }
        if (getTexto() != null) {
            _hashCode += getTexto().hashCode();
        }
        if (getDataHora() != null) {
            _hashCode += getDataHora().hashCode();
        }
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        if (getMarcador() != null) {
            _hashCode += getMarcador().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AndamentoMarcador.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "AndamentoMarcador"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAndamentoMarcador");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdAndamentoMarcador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("texto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Texto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataHora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DataHora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Usuario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marcador");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Marcador"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Marcador"));
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
