/**
 * SeiBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.jfrj.siga.ex.service.sei;

public class SeiBindingSkeleton implements br.gov.jfrj.siga.ex.service.sei.SeiPortType, org.apache.axis.wsdl.Skeleton {
    private br.gov.jfrj.siga.ex.service.sei.SeiPortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Procedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "Procedimento"), br.gov.jfrj.siga.ex.service.sei.Procedimento.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Documentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfDocumento"), br.gov.jfrj.siga.ex.service.sei.Documento[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProcedimentosRelacionados"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfProcedimentoRelacionado"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "UnidadesEnvio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdUnidade"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinManterAbertoUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinEnviarEmailNotificacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DiasRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinDiasUteisRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdMarcador"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "TextoMarcador"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataControlePrazo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DiasControlePrazo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinDiasUteisControlePrazo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("gerarProcedimento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoGeracaoProcedimento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "gerarProcedimento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("gerarProcedimento") == null) {
            _myOperations.put("gerarProcedimento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("gerarProcedimento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Documento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "Documento"), br.gov.jfrj.siga.ex.service.sei.Documento.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("incluirDocumento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoInclusaoDocumento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "incluirDocumento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("incluirDocumento") == null) {
            _myOperations.put("incluirDocumento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("incluirDocumento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTipoProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdSerie"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarUnidades", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfUnidade"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarUnidades"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarUnidades") == null) {
            _myOperations.put("listarUnidades", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarUnidades")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdSerie"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarTiposProcedimento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfTipoProcedimento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarTiposProcedimento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarTiposProcedimento") == null) {
            _myOperations.put("listarTiposProcedimento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarTiposProcedimento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTipoProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarSeries", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfSerie"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarSeries"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarSeries") == null) {
            _myOperations.put("listarSeries", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarSeries")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTipoContato"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PaginaRegistros"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PaginaAtual"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Sigla"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Cpf"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Cnpj"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Matricula"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdContatos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdContatos"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarContatos", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfContato"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarContatos"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarContatos") == null) {
            _myOperations.put("listarContatos", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarContatos")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Contatos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfContato"), br.gov.jfrj.siga.ex.service.sei.Contato[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("atualizarContatos", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "atualizarContatos"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("atualizarContatos") == null) {
            _myOperations.put("atualizarContatos", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("atualizarContatos")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAssuntos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarInteressados"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarObservacoes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAndamentoGeracao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAndamentoConclusao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarUltimoAndamento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarUnidadesProcedimentoAberto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarProcedimentosRelacionados"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarProcedimentosAnexados"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("consultarProcedimento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoConsultaProcedimento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "consultarProcedimento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("consultarProcedimento") == null) {
            _myOperations.put("consultarProcedimento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("consultarProcedimento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdOrgaoProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTipoProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdOrgaoUsuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaUsuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("consultarProcedimentoIndividual", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ProcedimentoResumido"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "consultarProcedimentoIndividual"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("consultarProcedimentoIndividual") == null) {
            _myOperations.put("consultarProcedimentoIndividual", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("consultarProcedimentoIndividual")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAndamentoGeracao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAssinaturas"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarCampos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("consultarDocumento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoConsultaDocumento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "consultarDocumento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("consultarDocumento") == null) {
            _myOperations.put("consultarDocumento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("consultarDocumento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Motivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("cancelarDocumento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "cancelarDocumento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("cancelarDocumento") == null) {
            _myOperations.put("cancelarDocumento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("cancelarDocumento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("bloquearDocumento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "bloquearDocumento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("bloquearDocumento") == null) {
            _myOperations.put("bloquearDocumento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("bloquearDocumento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Descricao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "UnidadesDisponibilizacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdUnidade"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Documentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfDocumentoFormatado"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinDisponibilizar"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("gerarBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "gerarBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("gerarBloco") == null) {
            _myOperations.put("gerarBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("gerarBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarProtocolos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("consultarBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoConsultaBloco"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "consultarBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("consultarBloco") == null) {
            _myOperations.put("consultarBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("consultarBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("excluirBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "excluirBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("excluirBloco") == null) {
            _myOperations.put("excluirBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("excluirBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("excluirProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "excluirProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("excluirProcesso") == null) {
            _myOperations.put("excluirProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("excluirProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("excluirDocumento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "excluirDocumento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("excluirDocumento") == null) {
            _myOperations.put("excluirDocumento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("excluirDocumento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("disponibilizarBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "disponibilizarBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("disponibilizarBloco") == null) {
            _myOperations.put("disponibilizarBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("disponibilizarBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("cancelarDisponibilizacaoBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "cancelarDisponibilizacaoBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("cancelarDisponibilizacaoBloco") == null) {
            _myOperations.put("cancelarDisponibilizacaoBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("cancelarDisponibilizacaoBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("concluirBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "concluirBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("concluirBloco") == null) {
            _myOperations.put("concluirBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("concluirBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("reabrirBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "reabrirBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("reabrirBloco") == null) {
            _myOperations.put("reabrirBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("reabrirBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("devolverBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "devolverBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("devolverBloco") == null) {
            _myOperations.put("devolverBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("devolverBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Anotacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("incluirDocumentoBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "incluirDocumentoBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("incluirDocumentoBloco") == null) {
            _myOperations.put("incluirDocumentoBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("incluirDocumentoBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("retirarDocumentoBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "retirarDocumentoBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("retirarDocumentoBloco") == null) {
            _myOperations.put("retirarDocumentoBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("retirarDocumentoBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Anotacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("incluirProcessoBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "incluirProcessoBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("incluirProcessoBloco") == null) {
            _myOperations.put("incluirProcessoBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("incluirProcessoBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdBloco"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("retirarProcessoBloco", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "retirarProcessoBloco"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("retirarProcessoBloco") == null) {
            _myOperations.put("retirarProcessoBloco", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("retirarProcessoBloco")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("reabrirProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "reabrirProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("reabrirProcesso") == null) {
            _myOperations.put("reabrirProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("reabrirProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("concluirProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "concluirProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("concluirProcesso") == null) {
            _myOperations.put("concluirProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("concluirProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdArquivoExtensao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarExtensoesPermitidas", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfArquivoExtensao"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarExtensoesPermitidas"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarExtensoesPermitidas") == null) {
            _myOperations.put("listarExtensoesPermitidas", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarExtensoesPermitidas")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "UnidadesDestino"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdUnidade"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinManterAbertoUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRemoverAnotacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinEnviarEmailNotificacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DiasRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinDiasUteisRetornoProgramado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinReabrir"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("enviarProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "enviarProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("enviarProcesso") == null) {
            _myOperations.put("enviarProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("enviarProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUsuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarUsuarios", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfUsuario"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarUsuarios"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarUsuarios") == null) {
            _myOperations.put("listarUsuarios", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarUsuarios")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUsuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinReabrir"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("atribuirProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "atribuirProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("atribuirProcesso") == null) {
            _myOperations.put("atribuirProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("atribuirProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "NivelAcesso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarHipotesesLegais", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfHipoteseLegal"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarHipotesesLegais"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarHipotesesLegais") == null) {
            _myOperations.put("listarHipotesesLegais", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarHipotesesLegais")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarPaises", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfPais"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarPaises"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarPaises") == null) {
            _myOperations.put("listarPaises", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarPaises")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdPais"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarEstados", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfEstado"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarEstados"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarEstados") == null) {
            _myOperations.put("listarEstados", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarEstados")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdPais"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdEstado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarCidades", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfCidade"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarCidades"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarCidades") == null) {
            _myOperations.put("listarCidades", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarCidades")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarTiposProcedimentoOuvidoria", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfTipoProcedimento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarTiposProcedimentoOuvidoria"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarTiposProcedimentoOuvidoria") == null) {
            _myOperations.put("listarTiposProcedimentoOuvidoria", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarTiposProcedimentoOuvidoria")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdCargo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarCargos", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfCargo"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarCargos"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarCargos") == null) {
            _myOperations.put("listarCargos", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarCargos")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarTiposConferencia", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfTipoConferencia"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarTiposConferencia"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarTiposConferencia") == null) {
            _myOperations.put("listarTiposConferencia", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarTiposConferencia")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Tamanho"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Hash"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Conteudo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("adicionarArquivo", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "adicionarArquivo"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("adicionarArquivo") == null) {
            _myOperations.put("adicionarArquivo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("adicionarArquivo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdArquivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Conteudo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("adicionarConteudoArquivo", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "adicionarConteudoArquivo"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("adicionarConteudoArquivo") == null) {
            _myOperations.put("adicionarConteudoArquivo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("adicionarConteudoArquivo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTarefa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTarefaModulo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Atributos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfAtributoAndamento"), br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("lancarAndamento", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "Andamento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "lancarAndamento"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("lancarAndamento") == null) {
            _myOperations.put("lancarAndamento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("lancarAndamento")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAtributos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Andamentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdAndamentos"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Tarefas"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdTarefas"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "TarefasModulos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdTarefasModulo"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarAndamentos", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfAndamento"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarAndamentos"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarAndamentos") == null) {
            _myOperations.put("listarAndamentos", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarAndamentos")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("bloquearProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "bloquearProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("bloquearProcesso") == null) {
            _myOperations.put("bloquearProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("bloquearProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("desbloquearProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "desbloquearProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("desbloquearProcesso") == null) {
            _myOperations.put("desbloquearProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("desbloquearProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("relacionarProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "relacionarProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("relacionarProcesso") == null) {
            _myOperations.put("relacionarProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("relacionarProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("removerRelacionamentoProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "removerRelacionamentoProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("removerRelacionamentoProcesso") == null) {
            _myOperations.put("removerRelacionamentoProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("removerRelacionamentoProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimentoVinculado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Motivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("sobrestarProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "sobrestarProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("sobrestarProcesso") == null) {
            _myOperations.put("sobrestarProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("sobrestarProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("removerSobrestamentoProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "removerSobrestamentoProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("removerSobrestamentoProcesso") == null) {
            _myOperations.put("removerSobrestamentoProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("removerSobrestamentoProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimentoPrincipal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimentoAnexado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("anexarProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "anexarProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("anexarProcesso") == null) {
            _myOperations.put("anexarProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("anexarProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimentoPrincipal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimentoAnexado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Motivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("desanexarProcesso", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "desanexarProcesso"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("desanexarProcesso") == null) {
            _myOperations.put("desanexarProcesso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("desanexarProcesso")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarMarcadoresUnidade", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfMarcador"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarMarcadoresUnidade"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarMarcadoresUnidade") == null) {
            _myOperations.put("listarMarcadoresUnidade", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarMarcadoresUnidade")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Definicoes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfDefinicaoMarcador"), br.gov.jfrj.siga.ex.service.sei.DefinicaoMarcador[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("definirMarcador", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "definirMarcador"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("definirMarcador") == null) {
            _myOperations.put("definirMarcador", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("definirMarcador")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Definicoes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfDefinicaoControlePrazo"), br.gov.jfrj.siga.ex.service.sei.DefinicaoControlePrazo[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("definirControlePrazo", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "definirControlePrazo"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("definirControlePrazo") == null) {
            _myOperations.put("definirControlePrazo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("definirControlePrazo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocolosProcedimentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfProcedimento"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("concluirControlePrazo", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "concluirControlePrazo"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("concluirControlePrazo") == null) {
            _myOperations.put("concluirControlePrazo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("concluirControlePrazo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocolosProcedimentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfProcedimento"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("removerControlePrazo", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "removerControlePrazo"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("removerControlePrazo") == null) {
            _myOperations.put("removerControlePrazo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("removerControlePrazo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Marcadores"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdMarcadores"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarAndamentosMarcadores", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfAndamentoMarcador"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarAndamentosMarcadores"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarAndamentosMarcadores") == null) {
            _myOperations.put("listarAndamentosMarcadores", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarAndamentosMarcadores")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAndamento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetornarAssinaturas"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("consultarPublicacao", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoConsultaPublicacao"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "consultarPublicacao"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("consultarPublicacao") == null) {
            _myOperations.put("consultarPublicacao", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("consultarPublicacao")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "StaMotivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdVeiculoPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataDisponibilizacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Resumo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ImprensaNacional"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "PublicacaoImprensaNacional"), br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("agendarPublicacao", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "agendarPublicacao"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("agendarPublicacao") == null) {
            _myOperations.put("agendarPublicacao", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("agendarPublicacao")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "StaMotivo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdVeiculoPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataDisponibilizacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Resumo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ImprensaNacional"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "PublicacaoImprensaNacional"), br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("alterarPublicacao", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "alterarPublicacao"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("alterarPublicacao") == null) {
            _myOperations.put("alterarPublicacao", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("alterarPublicacao")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloDocumento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("cancelarAgendamentoPublicacao", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "cancelarAgendamentoPublicacao"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("cancelarAgendamentoPublicacao") == null) {
            _myOperations.put("cancelarAgendamentoPublicacao", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("cancelarAgendamentoPublicacao")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdOrgao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataInicial"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataFinal"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listarFeriados", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ArrayOfFeriado"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "listarFeriados"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listarFeriados") == null) {
            _myOperations.put("listarFeriados", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listarFeriados")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdVeiculoPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataDisponibilizacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "DataPublicacao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Numero"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdDocumento"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("confirmarDisponibilizacaoPublicacao", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "confirmarDisponibilizacaoPublicacao"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("confirmarDisponibilizacaoPublicacao") == null) {
            _myOperations.put("confirmarDisponibilizacaoPublicacao", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("confirmarDisponibilizacaoPublicacao")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdUnidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProtocoloProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "De"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Para"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "CCO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Assunto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Mensagem"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdDocumentos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfIdDocumento"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("enviarEmail", _params, new javax.xml.namespace.QName("", "parametros"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "RetornoEnvioEmail"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "enviarEmail"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("enviarEmail") == null) {
            _myOperations.put("enviarEmail", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("enviarEmail")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SiglaSistema"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdentificacaoServico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdOrgao"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "NomeSocial"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Email"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Cpf"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Rg"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "OrgaoExpedidor"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Telefone"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdEstado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdCidade"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IdTipoProcedimento"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Processos"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "SinRetorno"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "Mensagem"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AtributosAdicionais"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("Sei", "ArrayOfAtributoOuvidoria"), br.gov.jfrj.siga.ex.service.sei.AtributoOuvidoria[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registrarOuvidoria", _params, new javax.xml.namespace.QName("", "processo"));
        _oper.setReturnType(new javax.xml.namespace.QName("Sei", "ProcedimentoResumido"));
        _oper.setElementQName(new javax.xml.namespace.QName("Sei", "registrarOuvidoria"));
        _oper.setSoapAction("SeiAction");
        _myOperationsList.add(_oper);
        if (_myOperations.get("registrarOuvidoria") == null) {
            _myOperations.put("registrarOuvidoria", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registrarOuvidoria")).add(_oper);
    }

    public SeiBindingSkeleton() {
        this.impl = new br.gov.jfrj.siga.ex.service.sei.SeiBindingImpl();
    }

    public SeiBindingSkeleton(br.gov.jfrj.siga.ex.service.sei.SeiPortType impl) {
        this.impl = impl;
    }
    public br.gov.jfrj.siga.ex.service.sei.RetornoGeracaoProcedimento gerarProcedimento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.Procedimento procedimento, br.gov.jfrj.siga.ex.service.sei.Documento[] documentos, java.lang.String[] procedimentosRelacionados, java.lang.String[] unidadesEnvio, java.lang.String sinManterAbertoUnidade, java.lang.String sinEnviarEmailNotificacao, java.lang.String dataRetornoProgramado, java.lang.String diasRetornoProgramado, java.lang.String sinDiasUteisRetornoProgramado, java.lang.String idMarcador, java.lang.String textoMarcador, java.lang.String dataControlePrazo, java.lang.String diasControlePrazo, java.lang.String sinDiasUteisControlePrazo) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoGeracaoProcedimento ret = impl.gerarProcedimento(siglaSistema, identificacaoServico, idUnidade, procedimento, documentos, procedimentosRelacionados, unidadesEnvio, sinManterAbertoUnidade, sinEnviarEmailNotificacao, dataRetornoProgramado, diasRetornoProgramado, sinDiasUteisRetornoProgramado, idMarcador, textoMarcador, dataControlePrazo, diasControlePrazo, sinDiasUteisControlePrazo);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoInclusaoDocumento incluirDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.Documento documento) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoInclusaoDocumento ret = impl.incluirDocumento(siglaSistema, identificacaoServico, idUnidade, documento);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Unidade[] listarUnidades(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idTipoProcedimento, java.lang.String idSerie) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Unidade[] ret = impl.listarUnidades(siglaSistema, identificacaoServico, idTipoProcedimento, idSerie);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] listarTiposProcedimento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idSerie) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] ret = impl.listarTiposProcedimento(siglaSistema, identificacaoServico, idUnidade, idSerie);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Serie[] listarSeries(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idTipoProcedimento) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Serie[] ret = impl.listarSeries(siglaSistema, identificacaoServico, idUnidade, idTipoProcedimento);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Contato[] listarContatos(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idTipoContato, java.lang.String paginaRegistros, java.lang.String paginaAtual, java.lang.String sigla, java.lang.String nome, java.lang.String cpf, java.lang.String cnpj, java.lang.String matricula, java.lang.String[] idContatos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Contato[] ret = impl.listarContatos(siglaSistema, identificacaoServico, idUnidade, idTipoContato, paginaRegistros, paginaAtual, sigla, nome, cpf, cnpj, matricula, idContatos);
        return ret;
    }

    public java.lang.String atualizarContatos(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.Contato[] contatos) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.atualizarContatos(siglaSistema, identificacaoServico, idUnidade, contatos);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaProcedimento consultarProcedimento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String sinRetornarAssuntos, java.lang.String sinRetornarInteressados, java.lang.String sinRetornarObservacoes, java.lang.String sinRetornarAndamentoGeracao, java.lang.String sinRetornarAndamentoConclusao, java.lang.String sinRetornarUltimoAndamento, java.lang.String sinRetornarUnidadesProcedimentoAberto, java.lang.String sinRetornarProcedimentosRelacionados, java.lang.String sinRetornarProcedimentosAnexados) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoConsultaProcedimento ret = impl.consultarProcedimento(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, sinRetornarAssuntos, sinRetornarInteressados, sinRetornarObservacoes, sinRetornarAndamentoGeracao, sinRetornarAndamentoConclusao, sinRetornarUltimoAndamento, sinRetornarUnidadesProcedimentoAberto, sinRetornarProcedimentosRelacionados, sinRetornarProcedimentosAnexados);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido consultarProcedimentoIndividual(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idOrgaoProcedimento, java.lang.String idTipoProcedimento, java.lang.String idOrgaoUsuario, java.lang.String siglaUsuario) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido ret = impl.consultarProcedimentoIndividual(siglaSistema, identificacaoServico, idUnidade, idOrgaoProcedimento, idTipoProcedimento, idOrgaoUsuario, siglaUsuario);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaDocumento consultarDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloDocumento, java.lang.String sinRetornarAndamentoGeracao, java.lang.String sinRetornarAssinaturas, java.lang.String sinRetornarPublicacao, java.lang.String sinRetornarCampos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoConsultaDocumento ret = impl.consultarDocumento(siglaSistema, identificacaoServico, idUnidade, protocoloDocumento, sinRetornarAndamentoGeracao, sinRetornarAssinaturas, sinRetornarPublicacao, sinRetornarCampos);
        return ret;
    }

    public java.lang.String cancelarDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloDocumento, java.lang.String motivo) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.cancelarDocumento(siglaSistema, identificacaoServico, idUnidade, protocoloDocumento, motivo);
        return ret;
    }

    public java.lang.String bloquearDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloDocumento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.bloquearDocumento(siglaSistema, identificacaoServico, idUnidade, protocoloDocumento);
        return ret;
    }

    public java.lang.String gerarBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String tipo, java.lang.String descricao, java.lang.String[] unidadesDisponibilizacao, java.lang.String[] documentos, java.lang.String sinDisponibilizar) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.gerarBloco(siglaSistema, identificacaoServico, idUnidade, tipo, descricao, unidadesDisponibilizacao, documentos, sinDisponibilizar);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaBloco consultarBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco, java.lang.String sinRetornarProtocolos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoConsultaBloco ret = impl.consultarBloco(siglaSistema, identificacaoServico, idUnidade, idBloco, sinRetornarProtocolos);
        return ret;
    }

    public java.lang.String excluirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.excluirBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String excluirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.excluirProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public java.lang.String excluirDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloDocumento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.excluirDocumento(siglaSistema, identificacaoServico, idUnidade, protocoloDocumento);
        return ret;
    }

    public java.lang.String disponibilizarBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.disponibilizarBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String cancelarDisponibilizacaoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.cancelarDisponibilizacaoBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String concluirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.concluirBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String reabrirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.reabrirBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String devolverBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.devolverBloco(siglaSistema, identificacaoServico, idUnidade, idBloco);
        return ret;
    }

    public java.lang.String incluirDocumentoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloDocumento, java.lang.String anotacao) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.incluirDocumentoBloco(siglaSistema, identificacaoServico, idUnidade, idBloco, protocoloDocumento, anotacao);
        return ret;
    }

    public java.lang.String retirarDocumentoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloDocumento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.retirarDocumentoBloco(siglaSistema, identificacaoServico, idUnidade, idBloco, protocoloDocumento);
        return ret;
    }

    public java.lang.String incluirProcessoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloProcedimento, java.lang.String anotacao) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.incluirProcessoBloco(siglaSistema, identificacaoServico, idUnidade, idBloco, protocoloProcedimento, anotacao);
        return ret;
    }

    public java.lang.String retirarProcessoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.retirarProcessoBloco(siglaSistema, identificacaoServico, idUnidade, idBloco, protocoloProcedimento);
        return ret;
    }

    public java.lang.String reabrirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.reabrirProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public java.lang.String concluirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.concluirProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.ArquivoExtensao[] listarExtensoesPermitidas(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idArquivoExtensao) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.ArquivoExtensao[] ret = impl.listarExtensoesPermitidas(siglaSistema, identificacaoServico, idUnidade, idArquivoExtensao);
        return ret;
    }

    public java.lang.String enviarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String[] unidadesDestino, java.lang.String sinManterAbertoUnidade, java.lang.String sinRemoverAnotacao, java.lang.String sinEnviarEmailNotificacao, java.lang.String dataRetornoProgramado, java.lang.String diasRetornoProgramado, java.lang.String sinDiasUteisRetornoProgramado, java.lang.String sinReabrir) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.enviarProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, unidadesDestino, sinManterAbertoUnidade, sinRemoverAnotacao, sinEnviarEmailNotificacao, dataRetornoProgramado, diasRetornoProgramado, sinDiasUteisRetornoProgramado, sinReabrir);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Usuario[] listarUsuarios(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idUsuario) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Usuario[] ret = impl.listarUsuarios(siglaSistema, identificacaoServico, idUnidade, idUsuario);
        return ret;
    }

    public java.lang.String atribuirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String idUsuario, java.lang.String sinReabrir) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.atribuirProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, idUsuario, sinReabrir);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.HipoteseLegal[] listarHipotesesLegais(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String nivelAcesso) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.HipoteseLegal[] ret = impl.listarHipotesesLegais(siglaSistema, identificacaoServico, idUnidade, nivelAcesso);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Pais[] listarPaises(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Pais[] ret = impl.listarPaises(siglaSistema, identificacaoServico, idUnidade);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Estado[] listarEstados(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPais) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Estado[] ret = impl.listarEstados(siglaSistema, identificacaoServico, idUnidade, idPais);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Cidade[] listarCidades(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPais, java.lang.String idEstado) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Cidade[] ret = impl.listarCidades(siglaSistema, identificacaoServico, idUnidade, idPais, idEstado);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] listarTiposProcedimentoOuvidoria(java.lang.String siglaSistema, java.lang.String identificacaoServico) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] ret = impl.listarTiposProcedimentoOuvidoria(siglaSistema, identificacaoServico);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Cargo[] listarCargos(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idCargo) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Cargo[] ret = impl.listarCargos(siglaSistema, identificacaoServico, idUnidade, idCargo);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.TipoConferencia[] listarTiposConferencia(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.TipoConferencia[] ret = impl.listarTiposConferencia(siglaSistema, identificacaoServico, idUnidade);
        return ret;
    }

    public java.lang.String adicionarArquivo(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String nome, java.lang.String tamanho, java.lang.String hash, java.lang.String conteudo) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.adicionarArquivo(siglaSistema, identificacaoServico, idUnidade, nome, tamanho, hash, conteudo);
        return ret;
    }

    public java.lang.String adicionarConteudoArquivo(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idArquivo, java.lang.String conteudo) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.adicionarConteudoArquivo(siglaSistema, identificacaoServico, idUnidade, idArquivo, conteudo);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Andamento lancarAndamento(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String idTarefa, java.lang.String idTarefaModulo, br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] atributos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Andamento ret = impl.lancarAndamento(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, idTarefa, idTarefaModulo, atributos);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Andamento[] listarAndamentos(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String sinRetornarAtributos, java.lang.String[] andamentos, java.lang.String[] tarefas, java.lang.String[] tarefasModulos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Andamento[] ret = impl.listarAndamentos(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, sinRetornarAtributos, andamentos, tarefas, tarefasModulos);
        return ret;
    }

    public java.lang.String bloquearProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.bloquearProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public java.lang.String desbloquearProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.desbloquearProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public java.lang.String relacionarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento1, java.lang.String protocoloProcedimento2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.relacionarProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento1, protocoloProcedimento2);
        return ret;
    }

    public java.lang.String removerRelacionamentoProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento1, java.lang.String protocoloProcedimento2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.removerRelacionamentoProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento1, protocoloProcedimento2);
        return ret;
    }

    public java.lang.String sobrestarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String protocoloProcedimentoVinculado, java.lang.String motivo) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.sobrestarProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, protocoloProcedimentoVinculado, motivo);
        return ret;
    }

    public java.lang.String removerSobrestamentoProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.removerSobrestamentoProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento);
        return ret;
    }

    public java.lang.String anexarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimentoPrincipal, java.lang.String protocoloProcedimentoAnexado) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.anexarProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimentoPrincipal, protocoloProcedimentoAnexado);
        return ret;
    }

    public java.lang.String desanexarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimentoPrincipal, java.lang.String protocoloProcedimentoAnexado, java.lang.String motivo) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.desanexarProcesso(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimentoPrincipal, protocoloProcedimentoAnexado, motivo);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Marcador[] listarMarcadoresUnidade(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Marcador[] ret = impl.listarMarcadoresUnidade(siglaSistema, identificacaoServico, idUnidade);
        return ret;
    }

    public java.lang.String definirMarcador(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.DefinicaoMarcador[] definicoes) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.definirMarcador(siglaSistema, identificacaoServico, idUnidade, definicoes);
        return ret;
    }

    public java.lang.String definirControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.DefinicaoControlePrazo[] definicoes) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.definirControlePrazo(siglaSistema, identificacaoServico, idUnidade, definicoes);
        return ret;
    }

    public java.lang.String concluirControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String[] protocolosProcedimentos) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.concluirControlePrazo(siglaSistema, identificacaoServico, idUnidade, protocolosProcedimentos);
        return ret;
    }

    public java.lang.String removerControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String[] protocolosProcedimentos) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.removerControlePrazo(siglaSistema, identificacaoServico, idUnidade, protocolosProcedimentos);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.AndamentoMarcador[] listarAndamentosMarcadores(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String[] marcadores) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.AndamentoMarcador[] ret = impl.listarAndamentosMarcadores(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, marcadores);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaPublicacao consultarPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPublicacao, java.lang.String idDocumento, java.lang.String protocoloDocumento, java.lang.String sinRetornarAndamento, java.lang.String sinRetornarAssinaturas) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoConsultaPublicacao ret = impl.consultarPublicacao(siglaSistema, identificacaoServico, idUnidade, idPublicacao, idDocumento, protocoloDocumento, sinRetornarAndamento, sinRetornarAssinaturas);
        return ret;
    }

    public java.lang.String agendarPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idDocumento, java.lang.String protocoloDocumento, java.lang.String staMotivo, java.lang.String idVeiculoPublicacao, java.lang.String dataDisponibilizacao, java.lang.String resumo, br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional imprensaNacional) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.agendarPublicacao(siglaSistema, identificacaoServico, idUnidade, idDocumento, protocoloDocumento, staMotivo, idVeiculoPublicacao, dataDisponibilizacao, resumo, imprensaNacional);
        return ret;
    }

    public java.lang.String alterarPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPublicacao, java.lang.String idDocumento, java.lang.String protocoloDocumento, java.lang.String staMotivo, java.lang.String idVeiculoPublicacao, java.lang.String dataDisponibilizacao, java.lang.String resumo, br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional imprensaNacional) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.alterarPublicacao(siglaSistema, identificacaoServico, idUnidade, idPublicacao, idDocumento, protocoloDocumento, staMotivo, idVeiculoPublicacao, dataDisponibilizacao, resumo, imprensaNacional);
        return ret;
    }

    public java.lang.String cancelarAgendamentoPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPublicacao, java.lang.String idDocumento, java.lang.String protocoloDocumento) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.cancelarAgendamentoPublicacao(siglaSistema, identificacaoServico, idUnidade, idPublicacao, idDocumento, protocoloDocumento);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.Feriado[] listarFeriados(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idOrgao, java.lang.String dataInicial, java.lang.String dataFinal) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.Feriado[] ret = impl.listarFeriados(siglaSistema, identificacaoServico, idUnidade, idOrgao, dataInicial, dataFinal);
        return ret;
    }

    public java.lang.String confirmarDisponibilizacaoPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idVeiculoPublicacao, java.lang.String dataDisponibilizacao, java.lang.String dataPublicacao, java.lang.String numero, java.lang.String[] idDocumentos) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.confirmarDisponibilizacaoPublicacao(siglaSistema, identificacaoServico, idVeiculoPublicacao, dataDisponibilizacao, dataPublicacao, numero, idDocumentos);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.RetornoEnvioEmail enviarEmail(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String de, java.lang.String para, java.lang.String CCO, java.lang.String assunto, java.lang.String mensagem, java.lang.String[] idDocumentos) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.RetornoEnvioEmail ret = impl.enviarEmail(siglaSistema, identificacaoServico, idUnidade, protocoloProcedimento, de, para, CCO, assunto, mensagem, idDocumentos);
        return ret;
    }

    public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido registrarOuvidoria(java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idOrgao, java.lang.String nome, java.lang.String nomeSocial, java.lang.String email, java.lang.String cpf, java.lang.String rg, java.lang.String orgaoExpedidor, java.lang.String telefone, java.lang.String idEstado, java.lang.String idCidade, java.lang.String idTipoProcedimento, java.lang.String processos, java.lang.String sinRetorno, java.lang.String mensagem, br.gov.jfrj.siga.ex.service.sei.AtributoOuvidoria[] atributosAdicionais) throws java.rmi.RemoteException
    {
        br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido ret = impl.registrarOuvidoria(siglaSistema, identificacaoServico, idOrgao, nome, nomeSocial, email, cpf, rg, orgaoExpedidor, telefone, idEstado, idCidade, idTipoProcedimento, processos, sinRetorno, mensagem, atributosAdicionais);
        return ret;
    }

}
