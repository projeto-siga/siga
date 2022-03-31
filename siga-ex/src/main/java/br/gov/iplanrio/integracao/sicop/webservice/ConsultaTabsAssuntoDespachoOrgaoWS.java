
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ConsultaTabsAssuntoDespachoOrgao_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ConsultaTabsAssuntoDespachoOrgao_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPPESQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESCRICAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DES14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIG14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COD14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ConsultaTabsAssuntoDespachoOrgao_WS", propOrder = {
    "rotina",
    "opcao",
    "tippesq",
    "descricao",
    "des1",
    "sig1",
    "cod1",
    "des2",
    "sig2",
    "cod2",
    "des3",
    "sig3",
    "cod3",
    "des4",
    "sig4",
    "cod4",
    "des5",
    "sig5",
    "cod5",
    "des6",
    "sig6",
    "cod6",
    "des7",
    "sig7",
    "cod7",
    "des8",
    "sig8",
    "cod8",
    "des9",
    "sig9",
    "cod9",
    "des10",
    "sig10",
    "cod10",
    "des11",
    "sig11",
    "cod11",
    "des12",
    "sig12",
    "cod12",
    "des13",
    "sig13",
    "cod13",
    "des14",
    "sig14",
    "cod14",
    "statusLine"
})
public class ConsultaTabsAssuntoDespachoOrgaoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "TIPPESQ")
    protected String tippesq;
    @XmlElement(name = "DESCRICAO")
    protected String descricao;
    @XmlElement(name = "DES1")
    protected String des1;
    @XmlElement(name = "SIG1")
    protected String sig1;
    @XmlElement(name = "COD1")
    protected String cod1;
    @XmlElement(name = "DES2")
    protected String des2;
    @XmlElement(name = "SIG2")
    protected String sig2;
    @XmlElement(name = "COD2")
    protected String cod2;
    @XmlElement(name = "DES3")
    protected String des3;
    @XmlElement(name = "SIG3")
    protected String sig3;
    @XmlElement(name = "COD3")
    protected String cod3;
    @XmlElement(name = "DES4")
    protected String des4;
    @XmlElement(name = "SIG4")
    protected String sig4;
    @XmlElement(name = "COD4")
    protected String cod4;
    @XmlElement(name = "DES5")
    protected String des5;
    @XmlElement(name = "SIG5")
    protected String sig5;
    @XmlElement(name = "COD5")
    protected String cod5;
    @XmlElement(name = "DES6")
    protected String des6;
    @XmlElement(name = "SIG6")
    protected String sig6;
    @XmlElement(name = "COD6")
    protected String cod6;
    @XmlElement(name = "DES7")
    protected String des7;
    @XmlElement(name = "SIG7")
    protected String sig7;
    @XmlElement(name = "COD7")
    protected String cod7;
    @XmlElement(name = "DES8")
    protected String des8;
    @XmlElement(name = "SIG8")
    protected String sig8;
    @XmlElement(name = "COD8")
    protected String cod8;
    @XmlElement(name = "DES9")
    protected String des9;
    @XmlElement(name = "SIG9")
    protected String sig9;
    @XmlElement(name = "COD9")
    protected String cod9;
    @XmlElement(name = "DES10")
    protected String des10;
    @XmlElement(name = "SIG10")
    protected String sig10;
    @XmlElement(name = "COD10")
    protected String cod10;
    @XmlElement(name = "DES11")
    protected String des11;
    @XmlElement(name = "SIG11")
    protected String sig11;
    @XmlElement(name = "COD11")
    protected String cod11;
    @XmlElement(name = "DES12")
    protected String des12;
    @XmlElement(name = "SIG12")
    protected String sig12;
    @XmlElement(name = "COD12")
    protected String cod12;
    @XmlElement(name = "DES13")
    protected String des13;
    @XmlElement(name = "SIG13")
    protected String sig13;
    @XmlElement(name = "COD13")
    protected String cod13;
    @XmlElement(name = "DES14")
    protected String des14;
    @XmlElement(name = "SIG14")
    protected String sig14;
    @XmlElement(name = "COD14")
    protected String cod14;
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
     * Obt�m o valor da propriedade tippesq.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPPESQ() {
        return tippesq;
    }

    /**
     * Define o valor da propriedade tippesq.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPPESQ(String value) {
        this.tippesq = value;
    }

    /**
     * Obt�m o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRICAO() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRICAO(String value) {
        this.descricao = value;
    }

    /**
     * Obt�m o valor da propriedade des1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES1() {
        return des1;
    }

    /**
     * Define o valor da propriedade des1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES1(String value) {
        this.des1 = value;
    }

    /**
     * Obt�m o valor da propriedade sig1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG1() {
        return sig1;
    }

    /**
     * Define o valor da propriedade sig1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG1(String value) {
        this.sig1 = value;
    }

    /**
     * Obt�m o valor da propriedade cod1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD1() {
        return cod1;
    }

    /**
     * Define o valor da propriedade cod1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD1(String value) {
        this.cod1 = value;
    }

    /**
     * Obt�m o valor da propriedade des2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES2() {
        return des2;
    }

    /**
     * Define o valor da propriedade des2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES2(String value) {
        this.des2 = value;
    }

    /**
     * Obt�m o valor da propriedade sig2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG2() {
        return sig2;
    }

    /**
     * Define o valor da propriedade sig2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG2(String value) {
        this.sig2 = value;
    }

    /**
     * Obt�m o valor da propriedade cod2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD2() {
        return cod2;
    }

    /**
     * Define o valor da propriedade cod2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD2(String value) {
        this.cod2 = value;
    }

    /**
     * Obt�m o valor da propriedade des3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES3() {
        return des3;
    }

    /**
     * Define o valor da propriedade des3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES3(String value) {
        this.des3 = value;
    }

    /**
     * Obt�m o valor da propriedade sig3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG3() {
        return sig3;
    }

    /**
     * Define o valor da propriedade sig3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG3(String value) {
        this.sig3 = value;
    }

    /**
     * Obt�m o valor da propriedade cod3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD3() {
        return cod3;
    }

    /**
     * Define o valor da propriedade cod3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD3(String value) {
        this.cod3 = value;
    }

    /**
     * Obt�m o valor da propriedade des4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES4() {
        return des4;
    }

    /**
     * Define o valor da propriedade des4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES4(String value) {
        this.des4 = value;
    }

    /**
     * Obt�m o valor da propriedade sig4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG4() {
        return sig4;
    }

    /**
     * Define o valor da propriedade sig4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG4(String value) {
        this.sig4 = value;
    }

    /**
     * Obt�m o valor da propriedade cod4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD4() {
        return cod4;
    }

    /**
     * Define o valor da propriedade cod4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD4(String value) {
        this.cod4 = value;
    }

    /**
     * Obt�m o valor da propriedade des5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES5() {
        return des5;
    }

    /**
     * Define o valor da propriedade des5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES5(String value) {
        this.des5 = value;
    }

    /**
     * Obt�m o valor da propriedade sig5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG5() {
        return sig5;
    }

    /**
     * Define o valor da propriedade sig5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG5(String value) {
        this.sig5 = value;
    }

    /**
     * Obt�m o valor da propriedade cod5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD5() {
        return cod5;
    }

    /**
     * Define o valor da propriedade cod5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD5(String value) {
        this.cod5 = value;
    }

    /**
     * Obt�m o valor da propriedade des6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES6() {
        return des6;
    }

    /**
     * Define o valor da propriedade des6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES6(String value) {
        this.des6 = value;
    }

    /**
     * Obt�m o valor da propriedade sig6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG6() {
        return sig6;
    }

    /**
     * Define o valor da propriedade sig6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG6(String value) {
        this.sig6 = value;
    }

    /**
     * Obt�m o valor da propriedade cod6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD6() {
        return cod6;
    }

    /**
     * Define o valor da propriedade cod6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD6(String value) {
        this.cod6 = value;
    }

    /**
     * Obt�m o valor da propriedade des7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES7() {
        return des7;
    }

    /**
     * Define o valor da propriedade des7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES7(String value) {
        this.des7 = value;
    }

    /**
     * Obt�m o valor da propriedade sig7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG7() {
        return sig7;
    }

    /**
     * Define o valor da propriedade sig7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG7(String value) {
        this.sig7 = value;
    }

    /**
     * Obt�m o valor da propriedade cod7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD7() {
        return cod7;
    }

    /**
     * Define o valor da propriedade cod7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD7(String value) {
        this.cod7 = value;
    }

    /**
     * Obt�m o valor da propriedade des8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES8() {
        return des8;
    }

    /**
     * Define o valor da propriedade des8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES8(String value) {
        this.des8 = value;
    }

    /**
     * Obt�m o valor da propriedade sig8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG8() {
        return sig8;
    }

    /**
     * Define o valor da propriedade sig8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG8(String value) {
        this.sig8 = value;
    }

    /**
     * Obt�m o valor da propriedade cod8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD8() {
        return cod8;
    }

    /**
     * Define o valor da propriedade cod8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD8(String value) {
        this.cod8 = value;
    }

    /**
     * Obt�m o valor da propriedade des9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES9() {
        return des9;
    }

    /**
     * Define o valor da propriedade des9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES9(String value) {
        this.des9 = value;
    }

    /**
     * Obt�m o valor da propriedade sig9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG9() {
        return sig9;
    }

    /**
     * Define o valor da propriedade sig9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG9(String value) {
        this.sig9 = value;
    }

    /**
     * Obt�m o valor da propriedade cod9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD9() {
        return cod9;
    }

    /**
     * Define o valor da propriedade cod9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD9(String value) {
        this.cod9 = value;
    }

    /**
     * Obt�m o valor da propriedade des10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES10() {
        return des10;
    }

    /**
     * Define o valor da propriedade des10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES10(String value) {
        this.des10 = value;
    }

    /**
     * Obt�m o valor da propriedade sig10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG10() {
        return sig10;
    }

    /**
     * Define o valor da propriedade sig10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG10(String value) {
        this.sig10 = value;
    }

    /**
     * Obt�m o valor da propriedade cod10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD10() {
        return cod10;
    }

    /**
     * Define o valor da propriedade cod10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD10(String value) {
        this.cod10 = value;
    }

    /**
     * Obt�m o valor da propriedade des11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES11() {
        return des11;
    }

    /**
     * Define o valor da propriedade des11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES11(String value) {
        this.des11 = value;
    }

    /**
     * Obt�m o valor da propriedade sig11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG11() {
        return sig11;
    }

    /**
     * Define o valor da propriedade sig11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG11(String value) {
        this.sig11 = value;
    }

    /**
     * Obt�m o valor da propriedade cod11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD11() {
        return cod11;
    }

    /**
     * Define o valor da propriedade cod11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD11(String value) {
        this.cod11 = value;
    }

    /**
     * Obt�m o valor da propriedade des12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES12() {
        return des12;
    }

    /**
     * Define o valor da propriedade des12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES12(String value) {
        this.des12 = value;
    }

    /**
     * Obt�m o valor da propriedade sig12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG12() {
        return sig12;
    }

    /**
     * Define o valor da propriedade sig12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG12(String value) {
        this.sig12 = value;
    }

    /**
     * Obt�m o valor da propriedade cod12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD12() {
        return cod12;
    }

    /**
     * Define o valor da propriedade cod12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD12(String value) {
        this.cod12 = value;
    }

    /**
     * Obt�m o valor da propriedade des13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES13() {
        return des13;
    }

    /**
     * Define o valor da propriedade des13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES13(String value) {
        this.des13 = value;
    }

    /**
     * Obt�m o valor da propriedade sig13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG13() {
        return sig13;
    }

    /**
     * Define o valor da propriedade sig13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG13(String value) {
        this.sig13 = value;
    }

    /**
     * Obt�m o valor da propriedade cod13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD13() {
        return cod13;
    }

    /**
     * Define o valor da propriedade cod13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD13(String value) {
        this.cod13 = value;
    }

    /**
     * Obt�m o valor da propriedade des14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDES14() {
        return des14;
    }

    /**
     * Define o valor da propriedade des14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDES14(String value) {
        this.des14 = value;
    }

    /**
     * Obt�m o valor da propriedade sig14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIG14() {
        return sig14;
    }

    /**
     * Define o valor da propriedade sig14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIG14(String value) {
        this.sig14 = value;
    }

    /**
     * Obt�m o valor da propriedade cod14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOD14() {
        return cod14;
    }

    /**
     * Define o valor da propriedade cod14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOD14(String value) {
        this.cod14 = value;
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
