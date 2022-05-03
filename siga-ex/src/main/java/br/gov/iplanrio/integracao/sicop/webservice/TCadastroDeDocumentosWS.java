
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PRAZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCJUD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMEXTERNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOMREQUERENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDERECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODCEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BAIRRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODBAIRRO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODASSUNTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASSUNTOCOMPL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASSUNTOCOMPL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASSUNTOCOMPL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOCD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONFIDENCIAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rotina",
    "opcao",
    "tipdoc",
    "numdoc",
    "orgdoc",
    "datdoc",
    "prazo",
    "procjud",
    "email",
    "numexterno",
    "numprocesso",
    "nomrequerente",
    "endereco",
    "num",
    "complemento",
    "cep",
    "codcep",
    "bairro",
    "codbairro",
    "ra",
    "ap",
    "codassunto",
    "assuntocompl1",
    "assuntocompl2",
    "assuntocompl3",
    "tpdoc",
    "nrdoc",
    "orgdocd",
    "confidencial",
    "indconfirma"
})
@XmlRootElement(name = "TCadastroDeDocumentos_WS")
public class TCadastroDeDocumentosWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "TIPDOC")
    protected String tipdoc;
    @XmlElement(name = "NUMDOC")
    protected String numdoc;
    @XmlElement(name = "ORGDOC")
    protected String orgdoc;
    @XmlElement(name = "DATDOC")
    protected String datdoc;
    @XmlElement(name = "PRAZO")
    protected String prazo;
    @XmlElement(name = "PROCJUD")
    protected String procjud;
    @XmlElement(name = "EMAIL")
    protected String email;
    @XmlElement(name = "NUMEXTERNO")
    protected String numexterno;
    @XmlElement(name = "NUMPROCESSO")
    protected String numprocesso;
    @XmlElement(name = "NOMREQUERENTE")
    protected String nomrequerente;
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
    @XmlElement(name = "CODBAIRRO")
    protected String codbairro;
    @XmlElement(name = "RA")
    protected String ra;
    @XmlElement(name = "AP")
    protected String ap;
    @XmlElement(name = "CODASSUNTO")
    protected String codassunto;
    @XmlElement(name = "ASSUNTOCOMPL1")
    protected String assuntocompl1;
    @XmlElement(name = "ASSUNTOCOMPL2")
    protected String assuntocompl2;
    @XmlElement(name = "ASSUNTOCOMPL3")
    protected String assuntocompl3;
    @XmlElement(name = "TPDOC")
    protected String tpdoc;
    @XmlElement(name = "NRDOC")
    protected String nrdoc;
    @XmlElement(name = "ORGDOCD")
    protected String orgdocd;
    @XmlElement(name = "CONFIDENCIAL")
    protected String confidencial;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;

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
     * Obt�m o valor da propriedade tipdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC() {
        return tipdoc;
    }

    /**
     * Define o valor da propriedade tipdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC(String value) {
        this.tipdoc = value;
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
     * Obt�m o valor da propriedade orgdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC() {
        return orgdoc;
    }

    /**
     * Define o valor da propriedade orgdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC(String value) {
        this.orgdoc = value;
    }

    /**
     * Obt�m o valor da propriedade datdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDOC() {
        return datdoc;
    }

    /**
     * Define o valor da propriedade datdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDOC(String value) {
        this.datdoc = value;
    }

    /**
     * Obt�m o valor da propriedade prazo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRAZO() {
        return prazo;
    }

    /**
     * Define o valor da propriedade prazo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRAZO(String value) {
        this.prazo = value;
    }

    /**
     * Obt�m o valor da propriedade procjud.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCJUD() {
        return procjud;
    }

    /**
     * Define o valor da propriedade procjud.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCJUD(String value) {
        this.procjud = value;
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
     * Obt�m o valor da propriedade numexterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMEXTERNO() {
        return numexterno;
    }

    /**
     * Define o valor da propriedade numexterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMEXTERNO(String value) {
        this.numexterno = value;
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
     * Obt�m o valor da propriedade codbairro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODBAIRRO() {
        return codbairro;
    }

    /**
     * Define o valor da propriedade codbairro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODBAIRRO(String value) {
        this.codbairro = value;
    }

    /**
     * Obt�m o valor da propriedade ra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRA() {
        return ra;
    }

    /**
     * Define o valor da propriedade ra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRA(String value) {
        this.ra = value;
    }

    /**
     * Obt�m o valor da propriedade ap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAP() {
        return ap;
    }

    /**
     * Define o valor da propriedade ap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAP(String value) {
        this.ap = value;
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
     * Obt�m o valor da propriedade assuntocompl1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASSUNTOCOMPL1() {
        return assuntocompl1;
    }

    /**
     * Define o valor da propriedade assuntocompl1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASSUNTOCOMPL1(String value) {
        this.assuntocompl1 = value;
    }

    /**
     * Obt�m o valor da propriedade assuntocompl2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASSUNTOCOMPL2() {
        return assuntocompl2;
    }

    /**
     * Define o valor da propriedade assuntocompl2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASSUNTOCOMPL2(String value) {
        this.assuntocompl2 = value;
    }

    /**
     * Obt�m o valor da propriedade assuntocompl3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASSUNTOCOMPL3() {
        return assuntocompl3;
    }

    /**
     * Define o valor da propriedade assuntocompl3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASSUNTOCOMPL3(String value) {
        this.assuntocompl3 = value;
    }

    /**
     * Obt�m o valor da propriedade tpdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPDOC() {
        return tpdoc;
    }

    /**
     * Define o valor da propriedade tpdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPDOC(String value) {
        this.tpdoc = value;
    }

    /**
     * Obt�m o valor da propriedade nrdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRDOC() {
        return nrdoc;
    }

    /**
     * Define o valor da propriedade nrdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRDOC(String value) {
        this.nrdoc = value;
    }

    /**
     * Obt�m o valor da propriedade orgdocd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOCD() {
        return orgdocd;
    }

    /**
     * Define o valor da propriedade orgdocd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOCD(String value) {
        this.orgdocd = value;
    }

    /**
     * Obt�m o valor da propriedade confidencial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIDENCIAL() {
        return confidencial;
    }

    /**
     * Define o valor da propriedade confidencial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIDENCIAL(String value) {
        this.confidencial = value;
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

}
