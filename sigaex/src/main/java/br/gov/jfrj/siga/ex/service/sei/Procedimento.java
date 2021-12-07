/**
 * Procedimento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Procedimento  implements java.io.Serializable {
    private java.lang.String idTipoProcedimento;

    private java.lang.String numeroProtocolo;

    private java.lang.String dataAutuacao;

    private java.lang.String especificacao;

    private br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos;

    private br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados;

    private java.lang.String observacao;

    private java.lang.String nivelAcesso;

    private java.lang.String idHipoteseLegal;

    public Procedimento() {
    }

    public Procedimento(
           java.lang.String idTipoProcedimento,
           java.lang.String numeroProtocolo,
           java.lang.String dataAutuacao,
           java.lang.String especificacao,
           br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos,
           br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados,
           java.lang.String observacao,
           java.lang.String nivelAcesso,
           java.lang.String idHipoteseLegal) {
           this.idTipoProcedimento = idTipoProcedimento;
           this.numeroProtocolo = numeroProtocolo;
           this.dataAutuacao = dataAutuacao;
           this.especificacao = especificacao;
           this.assuntos = assuntos;
           this.interessados = interessados;
           this.observacao = observacao;
           this.nivelAcesso = nivelAcesso;
           this.idHipoteseLegal = idHipoteseLegal;
    }


    /**
     * Gets the idTipoProcedimento value for this Procedimento.
     * 
     * @return idTipoProcedimento
     */
    public java.lang.String getIdTipoProcedimento() {
        return idTipoProcedimento;
    }


    /**
     * Sets the idTipoProcedimento value for this Procedimento.
     * 
     * @param idTipoProcedimento
     */
    public void setIdTipoProcedimento(java.lang.String idTipoProcedimento) {
        this.idTipoProcedimento = idTipoProcedimento;
    }


    /**
     * Gets the numeroProtocolo value for this Procedimento.
     * 
     * @return numeroProtocolo
     */
    public java.lang.String getNumeroProtocolo() {
        return numeroProtocolo;
    }


    /**
     * Sets the numeroProtocolo value for this Procedimento.
     * 
     * @param numeroProtocolo
     */
    public void setNumeroProtocolo(java.lang.String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }


    /**
     * Gets the dataAutuacao value for this Procedimento.
     * 
     * @return dataAutuacao
     */
    public java.lang.String getDataAutuacao() {
        return dataAutuacao;
    }


    /**
     * Sets the dataAutuacao value for this Procedimento.
     * 
     * @param dataAutuacao
     */
    public void setDataAutuacao(java.lang.String dataAutuacao) {
        this.dataAutuacao = dataAutuacao;
    }


    /**
     * Gets the especificacao value for this Procedimento.
     * 
     * @return especificacao
     */
    public java.lang.String getEspecificacao() {
        return especificacao;
    }


    /**
     * Sets the especificacao value for this Procedimento.
     * 
     * @param especificacao
     */
    public void setEspecificacao(java.lang.String especificacao) {
        this.especificacao = especificacao;
    }


    /**
     * Gets the assuntos value for this Procedimento.
     * 
     * @return assuntos
     */
    public br.gov.jfrj.siga.ex.service.sei.Assunto[] getAssuntos() {
        return assuntos;
    }


    /**
     * Sets the assuntos value for this Procedimento.
     * 
     * @param assuntos
     */
    public void setAssuntos(br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos) {
        this.assuntos = assuntos;
    }


    /**
     * Gets the interessados value for this Procedimento.
     * 
     * @return interessados
     */
    public br.gov.jfrj.siga.ex.service.sei.Interessado[] getInteressados() {
        return interessados;
    }


    /**
     * Sets the interessados value for this Procedimento.
     * 
     * @param interessados
     */
    public void setInteressados(br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados) {
        this.interessados = interessados;
    }


    /**
     * Gets the observacao value for this Procedimento.
     * 
     * @return observacao
     */
    public java.lang.String getObservacao() {
        return observacao;
    }


    /**
     * Sets the observacao value for this Procedimento.
     * 
     * @param observacao
     */
    public void setObservacao(java.lang.String observacao) {
        this.observacao = observacao;
    }


    /**
     * Gets the nivelAcesso value for this Procedimento.
     * 
     * @return nivelAcesso
     */
    public java.lang.String getNivelAcesso() {
        return nivelAcesso;
    }


    /**
     * Sets the nivelAcesso value for this Procedimento.
     * 
     * @param nivelAcesso
     */
    public void setNivelAcesso(java.lang.String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }


    /**
     * Gets the idHipoteseLegal value for this Procedimento.
     * 
     * @return idHipoteseLegal
     */
    public java.lang.String getIdHipoteseLegal() {
        return idHipoteseLegal;
    }


    /**
     * Sets the idHipoteseLegal value for this Procedimento.
     * 
     * @param idHipoteseLegal
     */
    public void setIdHipoteseLegal(java.lang.String idHipoteseLegal) {
        this.idHipoteseLegal = idHipoteseLegal;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Procedimento)) return false;
        Procedimento other = (Procedimento) obj;
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
            ((this.numeroProtocolo==null && other.getNumeroProtocolo()==null) || 
             (this.numeroProtocolo!=null &&
              this.numeroProtocolo.equals(other.getNumeroProtocolo()))) &&
            ((this.dataAutuacao==null && other.getDataAutuacao()==null) || 
             (this.dataAutuacao!=null &&
              this.dataAutuacao.equals(other.getDataAutuacao()))) &&
            ((this.especificacao==null && other.getEspecificacao()==null) || 
             (this.especificacao!=null &&
              this.especificacao.equals(other.getEspecificacao()))) &&
            ((this.assuntos==null && other.getAssuntos()==null) || 
             (this.assuntos!=null &&
              java.util.Arrays.equals(this.assuntos, other.getAssuntos()))) &&
            ((this.interessados==null && other.getInteressados()==null) || 
             (this.interessados!=null &&
              java.util.Arrays.equals(this.interessados, other.getInteressados()))) &&
            ((this.observacao==null && other.getObservacao()==null) || 
             (this.observacao!=null &&
              this.observacao.equals(other.getObservacao()))) &&
            ((this.nivelAcesso==null && other.getNivelAcesso()==null) || 
             (this.nivelAcesso!=null &&
              this.nivelAcesso.equals(other.getNivelAcesso()))) &&
            ((this.idHipoteseLegal==null && other.getIdHipoteseLegal()==null) || 
             (this.idHipoteseLegal!=null &&
              this.idHipoteseLegal.equals(other.getIdHipoteseLegal())));
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
        if (getNumeroProtocolo() != null) {
            _hashCode += getNumeroProtocolo().hashCode();
        }
        if (getDataAutuacao() != null) {
            _hashCode += getDataAutuacao().hashCode();
        }
        if (getEspecificacao() != null) {
            _hashCode += getEspecificacao().hashCode();
        }
        if (getAssuntos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAssuntos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAssuntos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInteressados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInteressados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInteressados(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getObservacao() != null) {
            _hashCode += getObservacao().hashCode();
        }
        if (getNivelAcesso() != null) {
            _hashCode += getNivelAcesso().hashCode();
        }
        if (getIdHipoteseLegal() != null) {
            _hashCode += getIdHipoteseLegal().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Procedimento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Procedimento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTipoProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTipoProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroProtocolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NumeroProtocolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAutuacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DataAutuacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("especificacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Especificacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assuntos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Assuntos"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Assunto"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interessados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Interessados"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Interessado"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Observacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcesso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcesso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHipoteseLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdHipoteseLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
