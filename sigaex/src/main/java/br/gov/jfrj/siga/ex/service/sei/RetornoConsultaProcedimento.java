/**
 * RetornoConsultaProcedimento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class RetornoConsultaProcedimento  implements java.io.Serializable {
    private java.lang.String idProcedimento;

    private java.lang.String procedimentoFormatado;

    private java.lang.String especificacao;

    private java.lang.String dataAutuacao;

    private java.lang.String linkAcesso;

    private java.lang.String nivelAcessoLocal;

    private java.lang.String nivelAcessoGlobal;

    private br.gov.jfrj.siga.ex.service.sei.TipoProcedimento tipoProcedimento;

    private br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao;

    private br.gov.jfrj.siga.ex.service.sei.Andamento andamentoConclusao;

    private br.gov.jfrj.siga.ex.service.sei.Andamento ultimoAndamento;

    private br.gov.jfrj.siga.ex.service.sei.UnidadeProcedimentoAberto[] unidadesProcedimentoAberto;

    private br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos;

    private br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados;

    private br.gov.jfrj.siga.ex.service.sei.Observacao[] observacoes;

    private br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosRelacionados;

    private br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosAnexados;

    public RetornoConsultaProcedimento() {
    }

    public RetornoConsultaProcedimento(
           java.lang.String idProcedimento,
           java.lang.String procedimentoFormatado,
           java.lang.String especificacao,
           java.lang.String dataAutuacao,
           java.lang.String linkAcesso,
           java.lang.String nivelAcessoLocal,
           java.lang.String nivelAcessoGlobal,
           br.gov.jfrj.siga.ex.service.sei.TipoProcedimento tipoProcedimento,
           br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao,
           br.gov.jfrj.siga.ex.service.sei.Andamento andamentoConclusao,
           br.gov.jfrj.siga.ex.service.sei.Andamento ultimoAndamento,
           br.gov.jfrj.siga.ex.service.sei.UnidadeProcedimentoAberto[] unidadesProcedimentoAberto,
           br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos,
           br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados,
           br.gov.jfrj.siga.ex.service.sei.Observacao[] observacoes,
           br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosRelacionados,
           br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosAnexados) {
           this.idProcedimento = idProcedimento;
           this.procedimentoFormatado = procedimentoFormatado;
           this.especificacao = especificacao;
           this.dataAutuacao = dataAutuacao;
           this.linkAcesso = linkAcesso;
           this.nivelAcessoLocal = nivelAcessoLocal;
           this.nivelAcessoGlobal = nivelAcessoGlobal;
           this.tipoProcedimento = tipoProcedimento;
           this.andamentoGeracao = andamentoGeracao;
           this.andamentoConclusao = andamentoConclusao;
           this.ultimoAndamento = ultimoAndamento;
           this.unidadesProcedimentoAberto = unidadesProcedimentoAberto;
           this.assuntos = assuntos;
           this.interessados = interessados;
           this.observacoes = observacoes;
           this.procedimentosRelacionados = procedimentosRelacionados;
           this.procedimentosAnexados = procedimentosAnexados;
    }


    /**
     * Gets the idProcedimento value for this RetornoConsultaProcedimento.
     * 
     * @return idProcedimento
     */
    public java.lang.String getIdProcedimento() {
        return idProcedimento;
    }


    /**
     * Sets the idProcedimento value for this RetornoConsultaProcedimento.
     * 
     * @param idProcedimento
     */
    public void setIdProcedimento(java.lang.String idProcedimento) {
        this.idProcedimento = idProcedimento;
    }


    /**
     * Gets the procedimentoFormatado value for this RetornoConsultaProcedimento.
     * 
     * @return procedimentoFormatado
     */
    public java.lang.String getProcedimentoFormatado() {
        return procedimentoFormatado;
    }


    /**
     * Sets the procedimentoFormatado value for this RetornoConsultaProcedimento.
     * 
     * @param procedimentoFormatado
     */
    public void setProcedimentoFormatado(java.lang.String procedimentoFormatado) {
        this.procedimentoFormatado = procedimentoFormatado;
    }


    /**
     * Gets the especificacao value for this RetornoConsultaProcedimento.
     * 
     * @return especificacao
     */
    public java.lang.String getEspecificacao() {
        return especificacao;
    }


    /**
     * Sets the especificacao value for this RetornoConsultaProcedimento.
     * 
     * @param especificacao
     */
    public void setEspecificacao(java.lang.String especificacao) {
        this.especificacao = especificacao;
    }


    /**
     * Gets the dataAutuacao value for this RetornoConsultaProcedimento.
     * 
     * @return dataAutuacao
     */
    public java.lang.String getDataAutuacao() {
        return dataAutuacao;
    }


    /**
     * Sets the dataAutuacao value for this RetornoConsultaProcedimento.
     * 
     * @param dataAutuacao
     */
    public void setDataAutuacao(java.lang.String dataAutuacao) {
        this.dataAutuacao = dataAutuacao;
    }


    /**
     * Gets the linkAcesso value for this RetornoConsultaProcedimento.
     * 
     * @return linkAcesso
     */
    public java.lang.String getLinkAcesso() {
        return linkAcesso;
    }


    /**
     * Sets the linkAcesso value for this RetornoConsultaProcedimento.
     * 
     * @param linkAcesso
     */
    public void setLinkAcesso(java.lang.String linkAcesso) {
        this.linkAcesso = linkAcesso;
    }


    /**
     * Gets the nivelAcessoLocal value for this RetornoConsultaProcedimento.
     * 
     * @return nivelAcessoLocal
     */
    public java.lang.String getNivelAcessoLocal() {
        return nivelAcessoLocal;
    }


    /**
     * Sets the nivelAcessoLocal value for this RetornoConsultaProcedimento.
     * 
     * @param nivelAcessoLocal
     */
    public void setNivelAcessoLocal(java.lang.String nivelAcessoLocal) {
        this.nivelAcessoLocal = nivelAcessoLocal;
    }


    /**
     * Gets the nivelAcessoGlobal value for this RetornoConsultaProcedimento.
     * 
     * @return nivelAcessoGlobal
     */
    public java.lang.String getNivelAcessoGlobal() {
        return nivelAcessoGlobal;
    }


    /**
     * Sets the nivelAcessoGlobal value for this RetornoConsultaProcedimento.
     * 
     * @param nivelAcessoGlobal
     */
    public void setNivelAcessoGlobal(java.lang.String nivelAcessoGlobal) {
        this.nivelAcessoGlobal = nivelAcessoGlobal;
    }


    /**
     * Gets the tipoProcedimento value for this RetornoConsultaProcedimento.
     * 
     * @return tipoProcedimento
     */
    public br.gov.jfrj.siga.ex.service.sei.TipoProcedimento getTipoProcedimento() {
        return tipoProcedimento;
    }


    /**
     * Sets the tipoProcedimento value for this RetornoConsultaProcedimento.
     * 
     * @param tipoProcedimento
     */
    public void setTipoProcedimento(br.gov.jfrj.siga.ex.service.sei.TipoProcedimento tipoProcedimento) {
        this.tipoProcedimento = tipoProcedimento;
    }


    /**
     * Gets the andamentoGeracao value for this RetornoConsultaProcedimento.
     * 
     * @return andamentoGeracao
     */
    public br.gov.jfrj.siga.ex.service.sei.Andamento getAndamentoGeracao() {
        return andamentoGeracao;
    }


    /**
     * Sets the andamentoGeracao value for this RetornoConsultaProcedimento.
     * 
     * @param andamentoGeracao
     */
    public void setAndamentoGeracao(br.gov.jfrj.siga.ex.service.sei.Andamento andamentoGeracao) {
        this.andamentoGeracao = andamentoGeracao;
    }


    /**
     * Gets the andamentoConclusao value for this RetornoConsultaProcedimento.
     * 
     * @return andamentoConclusao
     */
    public br.gov.jfrj.siga.ex.service.sei.Andamento getAndamentoConclusao() {
        return andamentoConclusao;
    }


    /**
     * Sets the andamentoConclusao value for this RetornoConsultaProcedimento.
     * 
     * @param andamentoConclusao
     */
    public void setAndamentoConclusao(br.gov.jfrj.siga.ex.service.sei.Andamento andamentoConclusao) {
        this.andamentoConclusao = andamentoConclusao;
    }


    /**
     * Gets the ultimoAndamento value for this RetornoConsultaProcedimento.
     * 
     * @return ultimoAndamento
     */
    public br.gov.jfrj.siga.ex.service.sei.Andamento getUltimoAndamento() {
        return ultimoAndamento;
    }


    /**
     * Sets the ultimoAndamento value for this RetornoConsultaProcedimento.
     * 
     * @param ultimoAndamento
     */
    public void setUltimoAndamento(br.gov.jfrj.siga.ex.service.sei.Andamento ultimoAndamento) {
        this.ultimoAndamento = ultimoAndamento;
    }


    /**
     * Gets the unidadesProcedimentoAberto value for this RetornoConsultaProcedimento.
     * 
     * @return unidadesProcedimentoAberto
     */
    public br.gov.jfrj.siga.ex.service.sei.UnidadeProcedimentoAberto[] getUnidadesProcedimentoAberto() {
        return unidadesProcedimentoAberto;
    }


    /**
     * Sets the unidadesProcedimentoAberto value for this RetornoConsultaProcedimento.
     * 
     * @param unidadesProcedimentoAberto
     */
    public void setUnidadesProcedimentoAberto(br.gov.jfrj.siga.ex.service.sei.UnidadeProcedimentoAberto[] unidadesProcedimentoAberto) {
        this.unidadesProcedimentoAberto = unidadesProcedimentoAberto;
    }


    /**
     * Gets the assuntos value for this RetornoConsultaProcedimento.
     * 
     * @return assuntos
     */
    public br.gov.jfrj.siga.ex.service.sei.Assunto[] getAssuntos() {
        return assuntos;
    }


    /**
     * Sets the assuntos value for this RetornoConsultaProcedimento.
     * 
     * @param assuntos
     */
    public void setAssuntos(br.gov.jfrj.siga.ex.service.sei.Assunto[] assuntos) {
        this.assuntos = assuntos;
    }


    /**
     * Gets the interessados value for this RetornoConsultaProcedimento.
     * 
     * @return interessados
     */
    public br.gov.jfrj.siga.ex.service.sei.Interessado[] getInteressados() {
        return interessados;
    }


    /**
     * Sets the interessados value for this RetornoConsultaProcedimento.
     * 
     * @param interessados
     */
    public void setInteressados(br.gov.jfrj.siga.ex.service.sei.Interessado[] interessados) {
        this.interessados = interessados;
    }


    /**
     * Gets the observacoes value for this RetornoConsultaProcedimento.
     * 
     * @return observacoes
     */
    public br.gov.jfrj.siga.ex.service.sei.Observacao[] getObservacoes() {
        return observacoes;
    }


    /**
     * Sets the observacoes value for this RetornoConsultaProcedimento.
     * 
     * @param observacoes
     */
    public void setObservacoes(br.gov.jfrj.siga.ex.service.sei.Observacao[] observacoes) {
        this.observacoes = observacoes;
    }


    /**
     * Gets the procedimentosRelacionados value for this RetornoConsultaProcedimento.
     * 
     * @return procedimentosRelacionados
     */
    public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] getProcedimentosRelacionados() {
        return procedimentosRelacionados;
    }


    /**
     * Sets the procedimentosRelacionados value for this RetornoConsultaProcedimento.
     * 
     * @param procedimentosRelacionados
     */
    public void setProcedimentosRelacionados(br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosRelacionados) {
        this.procedimentosRelacionados = procedimentosRelacionados;
    }


    /**
     * Gets the procedimentosAnexados value for this RetornoConsultaProcedimento.
     * 
     * @return procedimentosAnexados
     */
    public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] getProcedimentosAnexados() {
        return procedimentosAnexados;
    }


    /**
     * Sets the procedimentosAnexados value for this RetornoConsultaProcedimento.
     * 
     * @param procedimentosAnexados
     */
    public void setProcedimentosAnexados(br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido[] procedimentosAnexados) {
        this.procedimentosAnexados = procedimentosAnexados;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetornoConsultaProcedimento)) return false;
        RetornoConsultaProcedimento other = (RetornoConsultaProcedimento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idProcedimento==null && other.getIdProcedimento()==null) || 
             (this.idProcedimento!=null &&
              this.idProcedimento.equals(other.getIdProcedimento()))) &&
            ((this.procedimentoFormatado==null && other.getProcedimentoFormatado()==null) || 
             (this.procedimentoFormatado!=null &&
              this.procedimentoFormatado.equals(other.getProcedimentoFormatado()))) &&
            ((this.especificacao==null && other.getEspecificacao()==null) || 
             (this.especificacao!=null &&
              this.especificacao.equals(other.getEspecificacao()))) &&
            ((this.dataAutuacao==null && other.getDataAutuacao()==null) || 
             (this.dataAutuacao!=null &&
              this.dataAutuacao.equals(other.getDataAutuacao()))) &&
            ((this.linkAcesso==null && other.getLinkAcesso()==null) || 
             (this.linkAcesso!=null &&
              this.linkAcesso.equals(other.getLinkAcesso()))) &&
            ((this.nivelAcessoLocal==null && other.getNivelAcessoLocal()==null) || 
             (this.nivelAcessoLocal!=null &&
              this.nivelAcessoLocal.equals(other.getNivelAcessoLocal()))) &&
            ((this.nivelAcessoGlobal==null && other.getNivelAcessoGlobal()==null) || 
             (this.nivelAcessoGlobal!=null &&
              this.nivelAcessoGlobal.equals(other.getNivelAcessoGlobal()))) &&
            ((this.tipoProcedimento==null && other.getTipoProcedimento()==null) || 
             (this.tipoProcedimento!=null &&
              this.tipoProcedimento.equals(other.getTipoProcedimento()))) &&
            ((this.andamentoGeracao==null && other.getAndamentoGeracao()==null) || 
             (this.andamentoGeracao!=null &&
              this.andamentoGeracao.equals(other.getAndamentoGeracao()))) &&
            ((this.andamentoConclusao==null && other.getAndamentoConclusao()==null) || 
             (this.andamentoConclusao!=null &&
              this.andamentoConclusao.equals(other.getAndamentoConclusao()))) &&
            ((this.ultimoAndamento==null && other.getUltimoAndamento()==null) || 
             (this.ultimoAndamento!=null &&
              this.ultimoAndamento.equals(other.getUltimoAndamento()))) &&
            ((this.unidadesProcedimentoAberto==null && other.getUnidadesProcedimentoAberto()==null) || 
             (this.unidadesProcedimentoAberto!=null &&
              java.util.Arrays.equals(this.unidadesProcedimentoAberto, other.getUnidadesProcedimentoAberto()))) &&
            ((this.assuntos==null && other.getAssuntos()==null) || 
             (this.assuntos!=null &&
              java.util.Arrays.equals(this.assuntos, other.getAssuntos()))) &&
            ((this.interessados==null && other.getInteressados()==null) || 
             (this.interessados!=null &&
              java.util.Arrays.equals(this.interessados, other.getInteressados()))) &&
            ((this.observacoes==null && other.getObservacoes()==null) || 
             (this.observacoes!=null &&
              java.util.Arrays.equals(this.observacoes, other.getObservacoes()))) &&
            ((this.procedimentosRelacionados==null && other.getProcedimentosRelacionados()==null) || 
             (this.procedimentosRelacionados!=null &&
              java.util.Arrays.equals(this.procedimentosRelacionados, other.getProcedimentosRelacionados()))) &&
            ((this.procedimentosAnexados==null && other.getProcedimentosAnexados()==null) || 
             (this.procedimentosAnexados!=null &&
              java.util.Arrays.equals(this.procedimentosAnexados, other.getProcedimentosAnexados())));
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
        if (getIdProcedimento() != null) {
            _hashCode += getIdProcedimento().hashCode();
        }
        if (getProcedimentoFormatado() != null) {
            _hashCode += getProcedimentoFormatado().hashCode();
        }
        if (getEspecificacao() != null) {
            _hashCode += getEspecificacao().hashCode();
        }
        if (getDataAutuacao() != null) {
            _hashCode += getDataAutuacao().hashCode();
        }
        if (getLinkAcesso() != null) {
            _hashCode += getLinkAcesso().hashCode();
        }
        if (getNivelAcessoLocal() != null) {
            _hashCode += getNivelAcessoLocal().hashCode();
        }
        if (getNivelAcessoGlobal() != null) {
            _hashCode += getNivelAcessoGlobal().hashCode();
        }
        if (getTipoProcedimento() != null) {
            _hashCode += getTipoProcedimento().hashCode();
        }
        if (getAndamentoGeracao() != null) {
            _hashCode += getAndamentoGeracao().hashCode();
        }
        if (getAndamentoConclusao() != null) {
            _hashCode += getAndamentoConclusao().hashCode();
        }
        if (getUltimoAndamento() != null) {
            _hashCode += getUltimoAndamento().hashCode();
        }
        if (getUnidadesProcedimentoAberto() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUnidadesProcedimentoAberto());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUnidadesProcedimentoAberto(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        if (getObservacoes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObservacoes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObservacoes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProcedimentosRelacionados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProcedimentosRelacionados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProcedimentosRelacionados(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProcedimentosAnexados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProcedimentosAnexados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProcedimentosAnexados(), i);
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
        new org.apache.axis.description.TypeDesc(RetornoConsultaProcedimento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("Sei", "RetornoConsultaProcedimento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IdProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimentoFormatado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProcedimentoFormatado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("especificacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Especificacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAutuacao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DataAutuacao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkAcesso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LinkAcesso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcessoLocal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcessoLocal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivelAcessoGlobal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NivelAcessoGlobal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoProcedimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoProcedimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "TipoProcedimento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("andamentoGeracao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AndamentoGeracao"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("andamentoConclusao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AndamentoConclusao"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ultimoAndamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UltimoAndamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Andamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadesProcedimentoAberto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UnidadesProcedimentoAberto"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "UnidadeProcedimentoAberto"));
        elemField.setNillable(false);
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
        elemField.setFieldName("observacoes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Observacoes"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "Observacao"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimentosRelacionados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProcedimentosRelacionados"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "ProcedimentoResumido"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimentosAnexados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProcedimentosAnexados"));
        elemField.setXmlType(new javax.xml.namespace.QName("Sei", "ProcedimentoResumido"));
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
