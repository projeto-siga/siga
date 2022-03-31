
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ConsultaProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ConsultaProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DOCIDENTIFICADOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFVOLUME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PASTAASSENTAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOMREQUERENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDERECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BAIRRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TELEFONE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESCRTPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRTPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAODOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AUTOINFRACAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODASSUNTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFCOMPL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATAPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATACADPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HORACADPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRRECEBCADPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDIGPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCESSOPRINCIPAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDENTIFICAPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOVOPOSICIONAMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDIGTRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATADIGTRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATARECEBIMENTOTRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INFTELA12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATADESPACHOTRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHOUPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRRECEBEUPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRGUIATRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQTRAM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAOORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAODESTINO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDDESTINOPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHOPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ConsultaProcesso_WS", propOrder = {
    "rotina",
    "nrprocesso",
    "docidentificador",
    "infvolume",
    "inftela",
    "pastaassentamento",
    "nomrequerente",
    "orgorigem",
    "endereco",
    "cep",
    "bairro",
    "telefone",
    "tipodoc",
    "descrtpdoc",
    "nrtpdoc",
    "orgaodoc",
    "inftela1",
    "inftela2",
    "inftela3",
    "inftela4",
    "inftela5",
    "autoinfracao",
    "inftela6",
    "inftela7",
    "inftela8",
    "codassunto",
    "infcompl1",
    "infcompl2",
    "infcompl3",
    "dataprocesso",
    "datacadprocesso",
    "horacadprocesso",
    "matrrecebcadprocesso",
    "matrdigprocesso",
    "processoprincipal",
    "identificaprocesso",
    "novoposicionamento",
    "inftela9",
    "inftela10",
    "matrdigtram",
    "datadigtram",
    "datarecebimentotram",
    "inftela11",
    "inftela12",
    "datadespachotram",
    "matrdespachouprocesso",
    "matrrecebeuprocesso",
    "nrguiatram",
    "seqtram",
    "orgaoorigem",
    "orgaodestino",
    "enddestinoprocesso",
    "despachoprocesso",
    "statusLine"
})
public class ConsultaProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "NRPROCESSO")
    protected String nrprocesso;
    @XmlElement(name = "DOCIDENTIFICADOR")
    protected String docidentificador;
    @XmlElement(name = "INFVOLUME")
    protected String infvolume;
    @XmlElement(name = "INFTELA")
    protected String inftela;
    @XmlElement(name = "PASTAASSENTAMENTO")
    protected String pastaassentamento;
    @XmlElement(name = "NOMREQUERENTE")
    protected String nomrequerente;
    @XmlElement(name = "ORGORIGEM")
    protected String orgorigem;
    @XmlElement(name = "ENDERECO")
    protected String endereco;
    @XmlElement(name = "CEP")
    protected String cep;
    @XmlElement(name = "BAIRRO")
    protected String bairro;
    @XmlElement(name = "TELEFONE")
    protected String telefone;
    @XmlElement(name = "TIPODOC")
    protected String tipodoc;
    @XmlElement(name = "DESCRTPDOC")
    protected String descrtpdoc;
    @XmlElement(name = "NRTPDOC")
    protected String nrtpdoc;
    @XmlElement(name = "ORGAODOC")
    protected String orgaodoc;
    @XmlElement(name = "INFTELA1")
    protected String inftela1;
    @XmlElement(name = "INFTELA2")
    protected String inftela2;
    @XmlElement(name = "INFTELA3")
    protected String inftela3;
    @XmlElement(name = "INFTELA4")
    protected String inftela4;
    @XmlElement(name = "INFTELA5")
    protected String inftela5;
    @XmlElement(name = "AUTOINFRACAO")
    protected String autoinfracao;
    @XmlElement(name = "INFTELA6")
    protected String inftela6;
    @XmlElement(name = "INFTELA7")
    protected String inftela7;
    @XmlElement(name = "INFTELA8")
    protected String inftela8;
    @XmlElement(name = "CODASSUNTO")
    protected String codassunto;
    @XmlElement(name = "INFCOMPL1")
    protected String infcompl1;
    @XmlElement(name = "INFCOMPL2")
    protected String infcompl2;
    @XmlElement(name = "INFCOMPL3")
    protected String infcompl3;
    @XmlElement(name = "DATAPROCESSO")
    protected String dataprocesso;
    @XmlElement(name = "DATACADPROCESSO")
    protected String datacadprocesso;
    @XmlElement(name = "HORACADPROCESSO")
    protected String horacadprocesso;
    @XmlElement(name = "MATRRECEBCADPROCESSO")
    protected String matrrecebcadprocesso;
    @XmlElement(name = "MATRDIGPROCESSO")
    protected String matrdigprocesso;
    @XmlElement(name = "PROCESSOPRINCIPAL")
    protected String processoprincipal;
    @XmlElement(name = "IDENTIFICAPROCESSO")
    protected String identificaprocesso;
    @XmlElement(name = "NOVOPOSICIONAMENTO")
    protected String novoposicionamento;
    @XmlElement(name = "INFTELA9")
    protected String inftela9;
    @XmlElement(name = "INFTELA10")
    protected String inftela10;
    @XmlElement(name = "MATRDIGTRAM")
    protected String matrdigtram;
    @XmlElement(name = "DATADIGTRAM")
    protected String datadigtram;
    @XmlElement(name = "DATARECEBIMENTOTRAM")
    protected String datarecebimentotram;
    @XmlElement(name = "INFTELA11")
    protected String inftela11;
    @XmlElement(name = "INFTELA12")
    protected String inftela12;
    @XmlElement(name = "DATADESPACHOTRAM")
    protected String datadespachotram;
    @XmlElement(name = "MATRDESPACHOUPROCESSO")
    protected String matrdespachouprocesso;
    @XmlElement(name = "MATRRECEBEUPROCESSO")
    protected String matrrecebeuprocesso;
    @XmlElement(name = "NRGUIATRAM")
    protected String nrguiatram;
    @XmlElement(name = "SEQTRAM")
    protected String seqtram;
    @XmlElement(name = "ORGAOORIGEM")
    protected String orgaoorigem;
    @XmlElement(name = "ORGAODESTINO")
    protected String orgaodestino;
    @XmlElement(name = "ENDDESTINOPROCESSO")
    protected String enddestinoprocesso;
    @XmlElement(name = "DESPACHOPROCESSO")
    protected String despachoprocesso;
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
     * Obt�m o valor da propriedade nrprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO() {
        return nrprocesso;
    }

    /**
     * Define o valor da propriedade nrprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO(String value) {
        this.nrprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade docidentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCIDENTIFICADOR() {
        return docidentificador;
    }

    /**
     * Define o valor da propriedade docidentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCIDENTIFICADOR(String value) {
        this.docidentificador = value;
    }

    /**
     * Obt�m o valor da propriedade infvolume.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFVOLUME() {
        return infvolume;
    }

    /**
     * Define o valor da propriedade infvolume.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFVOLUME(String value) {
        this.infvolume = value;
    }

    /**
     * Obt�m o valor da propriedade inftela.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA() {
        return inftela;
    }

    /**
     * Define o valor da propriedade inftela.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA(String value) {
        this.inftela = value;
    }

    /**
     * Obt�m o valor da propriedade pastaassentamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPASTAASSENTAMENTO() {
        return pastaassentamento;
    }

    /**
     * Define o valor da propriedade pastaassentamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPASTAASSENTAMENTO(String value) {
        this.pastaassentamento = value;
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
     * Obt�m o valor da propriedade orgorigem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM() {
        return orgorigem;
    }

    /**
     * Define o valor da propriedade orgorigem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM(String value) {
        this.orgorigem = value;
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
     * Obt�m o valor da propriedade descrtpdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRTPDOC() {
        return descrtpdoc;
    }

    /**
     * Define o valor da propriedade descrtpdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRTPDOC(String value) {
        this.descrtpdoc = value;
    }

    /**
     * Obt�m o valor da propriedade nrtpdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRTPDOC() {
        return nrtpdoc;
    }

    /**
     * Define o valor da propriedade nrtpdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRTPDOC(String value) {
        this.nrtpdoc = value;
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
     * Obt�m o valor da propriedade inftela1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA1() {
        return inftela1;
    }

    /**
     * Define o valor da propriedade inftela1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA1(String value) {
        this.inftela1 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA2() {
        return inftela2;
    }

    /**
     * Define o valor da propriedade inftela2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA2(String value) {
        this.inftela2 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA3() {
        return inftela3;
    }

    /**
     * Define o valor da propriedade inftela3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA3(String value) {
        this.inftela3 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA4() {
        return inftela4;
    }

    /**
     * Define o valor da propriedade inftela4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA4(String value) {
        this.inftela4 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA5() {
        return inftela5;
    }

    /**
     * Define o valor da propriedade inftela5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA5(String value) {
        this.inftela5 = value;
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
     * Obt�m o valor da propriedade inftela6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA6() {
        return inftela6;
    }

    /**
     * Define o valor da propriedade inftela6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA6(String value) {
        this.inftela6 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA7() {
        return inftela7;
    }

    /**
     * Define o valor da propriedade inftela7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA7(String value) {
        this.inftela7 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA8() {
        return inftela8;
    }

    /**
     * Define o valor da propriedade inftela8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA8(String value) {
        this.inftela8 = value;
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
     * Obt�m o valor da propriedade dataprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATAPROCESSO() {
        return dataprocesso;
    }

    /**
     * Define o valor da propriedade dataprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATAPROCESSO(String value) {
        this.dataprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade datacadprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATACADPROCESSO() {
        return datacadprocesso;
    }

    /**
     * Define o valor da propriedade datacadprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATACADPROCESSO(String value) {
        this.datacadprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade horacadprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHORACADPROCESSO() {
        return horacadprocesso;
    }

    /**
     * Define o valor da propriedade horacadprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHORACADPROCESSO(String value) {
        this.horacadprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade matrrecebcadprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRRECEBCADPROCESSO() {
        return matrrecebcadprocesso;
    }

    /**
     * Define o valor da propriedade matrrecebcadprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRRECEBCADPROCESSO(String value) {
        this.matrrecebcadprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade matrdigprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDIGPROCESSO() {
        return matrdigprocesso;
    }

    /**
     * Define o valor da propriedade matrdigprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDIGPROCESSO(String value) {
        this.matrdigprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade processoprincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCESSOPRINCIPAL() {
        return processoprincipal;
    }

    /**
     * Define o valor da propriedade processoprincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCESSOPRINCIPAL(String value) {
        this.processoprincipal = value;
    }

    /**
     * Obt�m o valor da propriedade identificaprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDENTIFICAPROCESSO() {
        return identificaprocesso;
    }

    /**
     * Define o valor da propriedade identificaprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDENTIFICAPROCESSO(String value) {
        this.identificaprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade novoposicionamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOVOPOSICIONAMENTO() {
        return novoposicionamento;
    }

    /**
     * Define o valor da propriedade novoposicionamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOVOPOSICIONAMENTO(String value) {
        this.novoposicionamento = value;
    }

    /**
     * Obt�m o valor da propriedade inftela9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA9() {
        return inftela9;
    }

    /**
     * Define o valor da propriedade inftela9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA9(String value) {
        this.inftela9 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA10() {
        return inftela10;
    }

    /**
     * Define o valor da propriedade inftela10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA10(String value) {
        this.inftela10 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdigtram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDIGTRAM() {
        return matrdigtram;
    }

    /**
     * Define o valor da propriedade matrdigtram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDIGTRAM(String value) {
        this.matrdigtram = value;
    }

    /**
     * Obt�m o valor da propriedade datadigtram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATADIGTRAM() {
        return datadigtram;
    }

    /**
     * Define o valor da propriedade datadigtram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATADIGTRAM(String value) {
        this.datadigtram = value;
    }

    /**
     * Obt�m o valor da propriedade datarecebimentotram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATARECEBIMENTOTRAM() {
        return datarecebimentotram;
    }

    /**
     * Define o valor da propriedade datarecebimentotram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATARECEBIMENTOTRAM(String value) {
        this.datarecebimentotram = value;
    }

    /**
     * Obt�m o valor da propriedade inftela11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA11() {
        return inftela11;
    }

    /**
     * Define o valor da propriedade inftela11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA11(String value) {
        this.inftela11 = value;
    }

    /**
     * Obt�m o valor da propriedade inftela12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFTELA12() {
        return inftela12;
    }

    /**
     * Define o valor da propriedade inftela12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFTELA12(String value) {
        this.inftela12 = value;
    }

    /**
     * Obt�m o valor da propriedade datadespachotram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATADESPACHOTRAM() {
        return datadespachotram;
    }

    /**
     * Define o valor da propriedade datadespachotram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATADESPACHOTRAM(String value) {
        this.datadespachotram = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespachouprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHOUPROCESSO() {
        return matrdespachouprocesso;
    }

    /**
     * Define o valor da propriedade matrdespachouprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHOUPROCESSO(String value) {
        this.matrdespachouprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade matrrecebeuprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRRECEBEUPROCESSO() {
        return matrrecebeuprocesso;
    }

    /**
     * Define o valor da propriedade matrrecebeuprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRRECEBEUPROCESSO(String value) {
        this.matrrecebeuprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade nrguiatram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRGUIATRAM() {
        return nrguiatram;
    }

    /**
     * Define o valor da propriedade nrguiatram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRGUIATRAM(String value) {
        this.nrguiatram = value;
    }

    /**
     * Obt�m o valor da propriedade seqtram.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQTRAM() {
        return seqtram;
    }

    /**
     * Define o valor da propriedade seqtram.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQTRAM(String value) {
        this.seqtram = value;
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
     * Obt�m o valor da propriedade orgaodestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGAODESTINO() {
        return orgaodestino;
    }

    /**
     * Define o valor da propriedade orgaodestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGAODESTINO(String value) {
        this.orgaodestino = value;
    }

    /**
     * Obt�m o valor da propriedade enddestinoprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDDESTINOPROCESSO() {
        return enddestinoprocesso;
    }

    /**
     * Define o valor da propriedade enddestinoprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDDESTINOPROCESSO(String value) {
        this.enddestinoprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade despachoprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHOPROCESSO() {
        return despachoprocesso;
    }

    /**
     * Define o valor da propriedade despachoprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHOPROCESSO(String value) {
        this.despachoprocesso = value;
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
