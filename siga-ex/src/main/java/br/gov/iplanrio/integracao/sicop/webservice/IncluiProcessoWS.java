
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de IncluiProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="IncluiProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATAPROC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAOORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAODOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPOIDENT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRDOCIDENTIFICADOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODASSUNTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOMREQUERENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AUTOINFRACAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PLACAVEICULO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TPPESSOA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMCPFCGC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODLOGRADOURO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDERECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODCEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BAIRRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DDI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DDD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TELEFONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RAMAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOMNOMEPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDERECOPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTOPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BAIRROPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CEPPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODLOGRAPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TELEFONEPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QLFPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATAPROCURACAOPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPOIDENTPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRDOCIDENTIFICADORPARTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRRECEBEDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IMPRESSORA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LBLCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MSG1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MSG2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncluiProcesso_WS", propOrder = {
    "rotina",
    "opcao",
    "numprocesso",
    "dataproc",
    "orgaoorigem",
    "tipodoc",
    "numdoc",
    "orgaodoc",
    "tipoident",
    "nrdocidentificador",
    "codassunto",
    "nomrequerente",
    "autoinfracao",
    "placaveiculo",
    "tppessoa",
    "numcpfcgc",
    "codlogradouro",
    "endereco",
    "num",
    "complemento",
    "cep",
    "codcep",
    "bairro",
    "ddi",
    "ddd",
    "telefone",
    "ramal",
    "email",
    "infcompl1",
    "infcompl2",
    "infcompl3",
    "nomnomeparte",
    "enderecoparte",
    "numparte",
    "complementoparte",
    "bairroparte",
    "cepparte",
    "codlograparte",
    "telefoneparte",
    "qlfparte",
    "dataprocuracaoparte",
    "tipoidentparte",
    "nrdocidentificadorparte",
    "matrrecebedor",
    "impressora",
    "lblconfirma",
    "indconfirma",
    "msg1",
    "msg2",
    "statusLine"
})
public class IncluiProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROCESSO")
    protected String numprocesso;
    @XmlElement(name = "DATAPROC")
    protected String dataproc;
    @XmlElement(name = "ORGAOORIGEM")
    protected String orgaoorigem;
    @XmlElement(name = "TIPODOC")
    protected String tipodoc;
    @XmlElement(name = "NUMDOC")
    protected String numdoc;
    @XmlElement(name = "ORGAODOC")
    protected String orgaodoc;
    @XmlElement(name = "TIPOIDENT")
    protected String tipoident;
    @XmlElement(name = "NRDOCIDENTIFICADOR")
    protected String nrdocidentificador;
    @XmlElement(name = "CODASSUNTO")
    protected String codassunto;
    @XmlElement(name = "NOMREQUERENTE")
    protected String nomrequerente;
    @XmlElement(name = "AUTOINFRACAO")
    protected String autoinfracao;
    @XmlElement(name = "PLACAVEICULO")
    protected String placaveiculo;
    @XmlElement(name = "TPPESSOA")
    protected String tppessoa;
    @XmlElement(name = "NUMCPFCGC")
    protected String numcpfcgc;
    @XmlElement(name = "CODLOGRADOURO")
    protected String codlogradouro;
    @XmlElement(name = "ENDERECO")
    protected String endereco;
    @XmlElement(name = "NUM")
    protected String num;
    @XmlElement(name = "COMPLEMENTO")
    protected String complemento;
    @XmlElement(name = "CEP")
    protected String cep;
    @XmlElement(name = "CODCEP")
    protected String codcep;
    @XmlElement(name = "BAIRRO")
    protected String bairro;
    @XmlElement(name = "DDI")
    protected String ddi;
    @XmlElement(name = "DDD")
    protected String ddd;
    @XmlElement(name = "TELEFONE")
    protected String telefone;
    @XmlElement(name = "RAMAL")
    protected String ramal;
    @XmlElement(name = "EMAIL")
    protected String email;
    @XmlElement(name = "INFCOMPL1")
    protected String infcompl1;
    @XmlElement(name = "INFCOMPL2")
    protected String infcompl2;
    @XmlElement(name = "INFCOMPL3")
    protected String infcompl3;
    @XmlElement(name = "NOMNOMEPARTE")
    protected String nomnomeparte;
    @XmlElement(name = "ENDERECOPARTE")
    protected String enderecoparte;
    @XmlElement(name = "NUMPARTE")
    protected String numparte;
    @XmlElement(name = "COMPLEMENTOPARTE")
    protected String complementoparte;
    @XmlElement(name = "BAIRROPARTE")
    protected String bairroparte;
    @XmlElement(name = "CEPPARTE")
    protected String cepparte;
    @XmlElement(name = "CODLOGRAPARTE")
    protected String codlograparte;
    @XmlElement(name = "TELEFONEPARTE")
    protected String telefoneparte;
    @XmlElement(name = "QLFPARTE")
    protected String qlfparte;
    @XmlElement(name = "DATAPROCURACAOPARTE")
    protected String dataprocuracaoparte;
    @XmlElement(name = "TIPOIDENTPARTE")
    protected String tipoidentparte;
    @XmlElement(name = "NRDOCIDENTIFICADORPARTE")
    protected String nrdocidentificadorparte;
    @XmlElement(name = "MATRRECEBEDOR")
    protected String matrrecebedor;
    @XmlElement(name = "IMPRESSORA")
    protected String impressora;
    @XmlElement(name = "LBLCONFIRMA")
    protected String lblconfirma;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;
    @XmlElement(name = "MSG1")
    protected String msg1;
    @XmlElement(name = "MSG2")
    protected String msg2;
    @XmlElement(name = "StatusLine")
    protected String statusLine;

    /**
     * Obt�m o valor da propriedade rotina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROTINA() {
        return rotina;
    }

    /**
     * Define o valor da propriedade rotina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROTINA(String value) {
        this.rotina = value;
    }

    /**
     * Obt�m o valor da propriedade opcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPCAO() {
        return opcao;
    }

    /**
     * Define o valor da propriedade opcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPCAO(String value) {
        this.opcao = value;
    }

    /**
     * Obt�m o valor da propriedade numprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROCESSO() {
        return numprocesso;
    }

    /**
     * Define o valor da propriedade numprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROCESSO(String value) {
        this.numprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade dataproc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATAPROC() {
        return dataproc;
    }

    /**
     * Define o valor da propriedade dataproc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATAPROC(String value) {
        this.dataproc = value;
    }

    /**
     * Obt�m o valor da propriedade orgaoorigem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGAOORIGEM() {
        return orgaoorigem;
    }

    /**
     * Define o valor da propriedade orgaoorigem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGAOORIGEM(String value) {
        this.orgaoorigem = value;
    }

    /**
     * Obt�m o valor da propriedade tipodoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC() {
        return tipodoc;
    }

    /**
     * Define o valor da propriedade tipodoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC(String value) {
        this.tipodoc = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC() {
        return numdoc;
    }

    /**
     * Define o valor da propriedade numdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC(String value) {
        this.numdoc = value;
    }

    /**
     * Obt�m o valor da propriedade orgaodoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGAODOC() {
        return orgaodoc;
    }

    /**
     * Define o valor da propriedade orgaodoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGAODOC(String value) {
        this.orgaodoc = value;
    }

    /**
     * Obt�m o valor da propriedade tipoident.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPOIDENT() {
        return tipoident;
    }

    /**
     * Define o valor da propriedade tipoident.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPOIDENT(String value) {
        this.tipoident = value;
    }

    /**
     * Obt�m o valor da propriedade nrdocidentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRDOCIDENTIFICADOR() {
        return nrdocidentificador;
    }

    /**
     * Define o valor da propriedade nrdocidentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRDOCIDENTIFICADOR(String value) {
        this.nrdocidentificador = value;
    }

    /**
     * Obt�m o valor da propriedade codassunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODASSUNTO() {
        return codassunto;
    }

    /**
     * Define o valor da propriedade codassunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODASSUNTO(String value) {
        this.codassunto = value;
    }

    /**
     * Obt�m o valor da propriedade nomrequerente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOMREQUERENTE() {
        return nomrequerente;
    }

    /**
     * Define o valor da propriedade nomrequerente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOMREQUERENTE(String value) {
        this.nomrequerente = value;
    }

    /**
     * Obt�m o valor da propriedade autoinfracao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTOINFRACAO() {
        return autoinfracao;
    }

    /**
     * Define o valor da propriedade autoinfracao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTOINFRACAO(String value) {
        this.autoinfracao = value;
    }

    /**
     * Obt�m o valor da propriedade placaveiculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPLACAVEICULO() {
        return placaveiculo;
    }

    /**
     * Define o valor da propriedade placaveiculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPLACAVEICULO(String value) {
        this.placaveiculo = value;
    }

    /**
     * Obt�m o valor da propriedade tppessoa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPPESSOA() {
        return tppessoa;
    }

    /**
     * Define o valor da propriedade tppessoa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPPESSOA(String value) {
        this.tppessoa = value;
    }

    /**
     * Obt�m o valor da propriedade numcpfcgc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMCPFCGC() {
        return numcpfcgc;
    }

    /**
     * Define o valor da propriedade numcpfcgc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMCPFCGC(String value) {
        this.numcpfcgc = value;
    }

    /**
     * Obt�m o valor da propriedade codlogradouro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODLOGRADOURO() {
        return codlogradouro;
    }

    /**
     * Define o valor da propriedade codlogradouro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODLOGRADOURO(String value) {
        this.codlogradouro = value;
    }

    /**
     * Obt�m o valor da propriedade endereco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDERECO() {
        return endereco;
    }

    /**
     * Define o valor da propriedade endereco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDERECO(String value) {
        this.endereco = value;
    }

    /**
     * Obt�m o valor da propriedade num.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUM() {
        return num;
    }

    /**
     * Define o valor da propriedade num.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUM(String value) {
        this.num = value;
    }

    /**
     * Obt�m o valor da propriedade complemento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLEMENTO() {
        return complemento;
    }

    /**
     * Define o valor da propriedade complemento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLEMENTO(String value) {
        this.complemento = value;
    }

    /**
     * Obt�m o valor da propriedade cep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEP() {
        return cep;
    }

    /**
     * Define o valor da propriedade cep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEP(String value) {
        this.cep = value;
    }

    /**
     * Obt�m o valor da propriedade codcep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODCEP() {
        return codcep;
    }

    /**
     * Define o valor da propriedade codcep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODCEP(String value) {
        this.codcep = value;
    }

    /**
     * Obt�m o valor da propriedade bairro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBAIRRO() {
        return bairro;
    }

    /**
     * Define o valor da propriedade bairro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBAIRRO(String value) {
        this.bairro = value;
    }

    /**
     * Obt�m o valor da propriedade ddi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDDI() {
        return ddi;
    }

    /**
     * Define o valor da propriedade ddi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDDI(String value) {
        this.ddi = value;
    }

    /**
     * Obt�m o valor da propriedade ddd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDDD() {
        return ddd;
    }

    /**
     * Define o valor da propriedade ddd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDDD(String value) {
        this.ddd = value;
    }

    /**
     * Obt�m o valor da propriedade telefone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTELEFONE() {
        return telefone;
    }

    /**
     * Define o valor da propriedade telefone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTELEFONE(String value) {
        this.telefone = value;
    }

    /**
     * Obt�m o valor da propriedade ramal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRAMAL() {
        return ramal;
    }

    /**
     * Define o valor da propriedade ramal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRAMAL(String value) {
        this.ramal = value;
    }

    /**
     * Obt�m o valor da propriedade email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMAIL() {
        return email;
    }

    /**
     * Define o valor da propriedade email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMAIL(String value) {
        this.email = value;
    }

    /**
     * Obt�m o valor da propriedade infcompl1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFCOMPL1() {
        return infcompl1;
    }

    /**
     * Define o valor da propriedade infcompl1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFCOMPL1(String value) {
        this.infcompl1 = value;
    }

    /**
     * Obt�m o valor da propriedade infcompl2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFCOMPL2() {
        return infcompl2;
    }

    /**
     * Define o valor da propriedade infcompl2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFCOMPL2(String value) {
        this.infcompl2 = value;
    }

    /**
     * Obt�m o valor da propriedade infcompl3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFCOMPL3() {
        return infcompl3;
    }

    /**
     * Define o valor da propriedade infcompl3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFCOMPL3(String value) {
        this.infcompl3 = value;
    }

    /**
     * Obt�m o valor da propriedade nomnomeparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOMNOMEPARTE() {
        return nomnomeparte;
    }

    /**
     * Define o valor da propriedade nomnomeparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOMNOMEPARTE(String value) {
        this.nomnomeparte = value;
    }

    /**
     * Obt�m o valor da propriedade enderecoparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDERECOPARTE() {
        return enderecoparte;
    }

    /**
     * Define o valor da propriedade enderecoparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDERECOPARTE(String value) {
        this.enderecoparte = value;
    }

    /**
     * Obt�m o valor da propriedade numparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPARTE() {
        return numparte;
    }

    /**
     * Define o valor da propriedade numparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPARTE(String value) {
        this.numparte = value;
    }

    /**
     * Obt�m o valor da propriedade complementoparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLEMENTOPARTE() {
        return complementoparte;
    }

    /**
     * Define o valor da propriedade complementoparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLEMENTOPARTE(String value) {
        this.complementoparte = value;
    }

    /**
     * Obt�m o valor da propriedade bairroparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBAIRROPARTE() {
        return bairroparte;
    }

    /**
     * Define o valor da propriedade bairroparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBAIRROPARTE(String value) {
        this.bairroparte = value;
    }

    /**
     * Obt�m o valor da propriedade cepparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEPPARTE() {
        return cepparte;
    }

    /**
     * Define o valor da propriedade cepparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEPPARTE(String value) {
        this.cepparte = value;
    }

    /**
     * Obt�m o valor da propriedade codlograparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODLOGRAPARTE() {
        return codlograparte;
    }

    /**
     * Define o valor da propriedade codlograparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODLOGRAPARTE(String value) {
        this.codlograparte = value;
    }

    /**
     * Obt�m o valor da propriedade telefoneparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTELEFONEPARTE() {
        return telefoneparte;
    }

    /**
     * Define o valor da propriedade telefoneparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTELEFONEPARTE(String value) {
        this.telefoneparte = value;
    }

    /**
     * Obt�m o valor da propriedade qlfparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQLFPARTE() {
        return qlfparte;
    }

    /**
     * Define o valor da propriedade qlfparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQLFPARTE(String value) {
        this.qlfparte = value;
    }

    /**
     * Obt�m o valor da propriedade dataprocuracaoparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATAPROCURACAOPARTE() {
        return dataprocuracaoparte;
    }

    /**
     * Define o valor da propriedade dataprocuracaoparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATAPROCURACAOPARTE(String value) {
        this.dataprocuracaoparte = value;
    }

    /**
     * Obt�m o valor da propriedade tipoidentparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPOIDENTPARTE() {
        return tipoidentparte;
    }

    /**
     * Define o valor da propriedade tipoidentparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPOIDENTPARTE(String value) {
        this.tipoidentparte = value;
    }

    /**
     * Obt�m o valor da propriedade nrdocidentificadorparte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRDOCIDENTIFICADORPARTE() {
        return nrdocidentificadorparte;
    }

    /**
     * Define o valor da propriedade nrdocidentificadorparte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRDOCIDENTIFICADORPARTE(String value) {
        this.nrdocidentificadorparte = value;
    }

    /**
     * Obt�m o valor da propriedade matrrecebedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRRECEBEDOR() {
        return matrrecebedor;
    }

    /**
     * Define o valor da propriedade matrrecebedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRRECEBEDOR(String value) {
        this.matrrecebedor = value;
    }

    /**
     * Obt�m o valor da propriedade impressora.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMPRESSORA() {
        return impressora;
    }

    /**
     * Define o valor da propriedade impressora.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMPRESSORA(String value) {
        this.impressora = value;
    }

    /**
     * Obt�m o valor da propriedade lblconfirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLBLCONFIRMA() {
        return lblconfirma;
    }

    /**
     * Define o valor da propriedade lblconfirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLBLCONFIRMA(String value) {
        this.lblconfirma = value;
    }

    /**
     * Obt�m o valor da propriedade indconfirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINDCONFIRMA() {
        return indconfirma;
    }

    /**
     * Define o valor da propriedade indconfirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINDCONFIRMA(String value) {
        this.indconfirma = value;
    }

    /**
     * Obt�m o valor da propriedade msg1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSG1() {
        return msg1;
    }

    /**
     * Define o valor da propriedade msg1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSG1(String value) {
        this.msg1 = value;
    }

    /**
     * Obt�m o valor da propriedade msg2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSG2() {
        return msg2;
    }

    /**
     * Define o valor da propriedade msg2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSG2(String value) {
        this.msg2 = value;
    }

    /**
     * Obt�m o valor da propriedade statusLine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusLine() {
        return statusLine;
    }

    /**
     * Define o valor da propriedade statusLine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusLine(String value) {
        this.statusLine = value;
    }

}
