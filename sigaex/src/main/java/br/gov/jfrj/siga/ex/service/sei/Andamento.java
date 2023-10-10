/**
 * Andamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class Andamento  implements java.io.Serializable {
    private java.lang.String idAndamento;

    private java.lang.String idTarefa;

    private java.lang.String idTarefaModulo;

    private java.lang.String descricao;

    private java.lang.String dataHora;

    private br.gov.jfrj.siga.ex.service.sei.Unidade unidade;

    private br.gov.jfrj.siga.ex.service.sei.Usuario usuario;

    private br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] atributos;

    public Andamento() {
    }

    public Andamento(
           java.lang.String idAndamento,
           java.lang.String idTarefa,
           java.lang.String idTarefaModulo,
           java.lang.String descricao,
           java.lang.String dataHora,
           br.gov.jfrj.siga.ex.service.sei.Unidade unidade,
           br.gov.jfrj.siga.ex.service.sei.Usuario usuario,
           br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] atributos) {
           this.idAndamento = idAndamento;
           this.idTarefa = idTarefa;
           this.idTarefaModulo = idTarefaModulo;
           this.descricao = descricao;
           this.dataHora = dataHora;
           this.unidade = unidade;
           this.usuario = usuario;
           this.atributos = atributos;
    }


    /**
     * Gets the idAndamento value for this Andamento.
     * 
     * @return idAndamento
     */
    public java.lang.String getIdAndamento() {
        return idAndamento;
    }


    /**
     * Sets the idAndamento value for this Andamento.
     * 
     * @param idAndamento
     */
    public void setIdAndamento(java.lang.String idAndamento) {
        this.idAndamento = idAndamento;
    }


    /**
     * Gets the idTarefa value for this Andamento.
     * 
     * @return idTarefa
     */
    public java.lang.String getIdTarefa() {
        return idTarefa;
    }


    /**
     * Sets the idTarefa value for this Andamento.
     * 
     * @param idTarefa
     */
    public void setIdTarefa(java.lang.String idTarefa) {
        this.idTarefa = idTarefa;
    }


    /**
     * Gets the idTarefaModulo value for this Andamento.
     * 
     * @return idTarefaModulo
     */
    public java.lang.String getIdTarefaModulo() {
        return idTarefaModulo;
    }


    /**
     * Sets the idTarefaModulo value for this Andamento.
     * 
     * @param idTarefaModulo
     */
    public void setIdTarefaModulo(java.lang.String idTarefaModulo) {
        this.idTarefaModulo = idTarefaModulo;
    }


    /**
     * Gets the descricao value for this Andamento.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this Andamento.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }


    /**
     * Gets the dataHora value for this Andamento.
     * 
     * @return dataHora
     */
    public java.lang.String getDataHora() {
        return dataHora;
    }


    /**
     * Sets the dataHora value for this Andamento.
     * 
     * @param dataHora
     */
    public void setDataHora(java.lang.String dataHora) {
        this.dataHora = dataHora;
    }


    /**
     * Gets the unidade value for this Andamento.
     * 
     * @return unidade
     */
    public br.gov.jfrj.siga.ex.service.sei.Unidade getUnidade() {
        return unidade;
    }


    /**
     * Sets the unidade value for this Andamento.
     * 
     * @param unidade
     */
    public void setUnidade(br.gov.jfrj.siga.ex.service.sei.Unidade unidade) {
        this.unidade = unidade;
    }


    /**
     * Gets the usuario value for this Andamento.
     * 
     * @return usuario
     */
    public br.gov.jfrj.siga.ex.service.sei.Usuario getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this Andamento.
     * 
     * @param usuario
     */
    public void setUsuario(br.gov.jfrj.siga.ex.service.sei.Usuario usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the atributos value for this Andamento.
     * 
     * @return atributos
     */
    public br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] getAtributos() {
        return atributos;
    }


    /**
     * Sets the atributos value for this Andamento.
     * 
     * @param atributos
     */
    public void setAtributos(br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] atributos) {
        this.atributos = atributos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Andamento)) return false;
        Andamento other = (Andamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idAndamento==null && other.getIdAndamento()==null) || 
             (this.idAndamento!=null &&
              this.idAndamento.equals(other.getIdAndamento()))) &&
            ((this.idTarefa==null && other.getIdTarefa()==null) || 
             (this.idTarefa!=null &&
              this.idTarefa.equals(other.getIdTarefa()))) &&
            ((this.idTarefaModulo==null && other.getIdTarefaModulo()==null) || 
             (this.idTarefaModulo!=null &&
              this.idTarefaModulo.equals(other.getIdTarefaModulo()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao()))) &&
            ((this.dataHora==null && other.getDataHora()==null) || 
             (this.dataHora!=null &&
              this.dataHora.equals(other.getDataHora()))) &&
            ((this.unidade==null && other.getUnidade()==null) || 
             (this.unidade!=null &&
              this.unidade.equals(other.getUnidade()))) &&
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario()))) &&
            ((this.atributos==null && other.getAtributos()==null) || 
             (this.atributos!=null &&
              java.util.Arrays.equals(this.atributos, other.getAtributos())));
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
        if (getIdAndamento() != null) {
            _hashCode += getIdAndamento().hashCode();
        }
        if (getIdTarefa() != null) {
            _hashCode += getIdTarefa().hashCode();
        }
        if (getIdTarefaModulo() != null) {
            _hashCode += getIdTarefaModulo().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        if (getDataHora() != null) {
            _hashCode += getDataHora().hashCode();
        }
        if (getUnidade() != null) {
            _hashCode += getUnidade().hashCode();
        }
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        if (getAtributos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAtributos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAtributos(), i);
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
        new org.apache.axis.description.TypeDesc(Andamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAndamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdAndamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTarefa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTarefa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTarefaModulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdTarefaModulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Descricao"));
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
        elemField.setFieldName("unidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Unidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Unidade"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Usuario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("atributos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Atributos"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "AtributoAndamento"));
        elemField.setMinOccurs(0);
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
