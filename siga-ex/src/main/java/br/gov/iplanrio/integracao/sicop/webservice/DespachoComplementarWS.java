
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de DespachoComplementar_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="DespachoComplementar_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LBLCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DespachoComplementar_WS", propOrder = {
    "rotina",
    "opcao",
    "numproc1",
    "despacho1",
    "despacho2",
    "despacho3",
    "despacho4",
    "despacho5",
    "numproc2",
    "despacho6",
    "despacho7",
    "despacho8",
    "despacho9",
    "despacho10",
    "numproc3",
    "despacho11",
    "despacho12",
    "despacho13",
    "despacho14",
    "despacho15",
    "lblconfirma",
    "indconfirma",
    "statusLine"
})
public class DespachoComplementarWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROC1")
    protected String numproc1;
    @XmlElement(name = "DESPACHO1")
    protected String despacho1;
    @XmlElement(name = "DESPACHO2")
    protected String despacho2;
    @XmlElement(name = "DESPACHO3")
    protected String despacho3;
    @XmlElement(name = "DESPACHO4")
    protected String despacho4;
    @XmlElement(name = "DESPACHO5")
    protected String despacho5;
    @XmlElement(name = "NUMPROC2")
    protected String numproc2;
    @XmlElement(name = "DESPACHO6")
    protected String despacho6;
    @XmlElement(name = "DESPACHO7")
    protected String despacho7;
    @XmlElement(name = "DESPACHO8")
    protected String despacho8;
    @XmlElement(name = "DESPACHO9")
    protected String despacho9;
    @XmlElement(name = "DESPACHO10")
    protected String despacho10;
    @XmlElement(name = "NUMPROC3")
    protected String numproc3;
    @XmlElement(name = "DESPACHO11")
    protected String despacho11;
    @XmlElement(name = "DESPACHO12")
    protected String despacho12;
    @XmlElement(name = "DESPACHO13")
    protected String despacho13;
    @XmlElement(name = "DESPACHO14")
    protected String despacho14;
    @XmlElement(name = "DESPACHO15")
    protected String despacho15;
    @XmlElement(name = "LBLCONFIRMA")
    protected String lblconfirma;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;
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
     * Obt�m o valor da propriedade numproc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC1() {
        return numproc1;
    }

    /**
     * Define o valor da propriedade numproc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC1(String value) {
        this.numproc1 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO1() {
        return despacho1;
    }

    /**
     * Define o valor da propriedade despacho1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO1(String value) {
        this.despacho1 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO2() {
        return despacho2;
    }

    /**
     * Define o valor da propriedade despacho2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO2(String value) {
        this.despacho2 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO3() {
        return despacho3;
    }

    /**
     * Define o valor da propriedade despacho3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO3(String value) {
        this.despacho3 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO4() {
        return despacho4;
    }

    /**
     * Define o valor da propriedade despacho4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO4(String value) {
        this.despacho4 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO5() {
        return despacho5;
    }

    /**
     * Define o valor da propriedade despacho5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO5(String value) {
        this.despacho5 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC2() {
        return numproc2;
    }

    /**
     * Define o valor da propriedade numproc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC2(String value) {
        this.numproc2 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO6() {
        return despacho6;
    }

    /**
     * Define o valor da propriedade despacho6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO6(String value) {
        this.despacho6 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO7() {
        return despacho7;
    }

    /**
     * Define o valor da propriedade despacho7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO7(String value) {
        this.despacho7 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO8() {
        return despacho8;
    }

    /**
     * Define o valor da propriedade despacho8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO8(String value) {
        this.despacho8 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO9() {
        return despacho9;
    }

    /**
     * Define o valor da propriedade despacho9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO9(String value) {
        this.despacho9 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO10() {
        return despacho10;
    }

    /**
     * Define o valor da propriedade despacho10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO10(String value) {
        this.despacho10 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC3() {
        return numproc3;
    }

    /**
     * Define o valor da propriedade numproc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC3(String value) {
        this.numproc3 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO11() {
        return despacho11;
    }

    /**
     * Define o valor da propriedade despacho11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO11(String value) {
        this.despacho11 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO12() {
        return despacho12;
    }

    /**
     * Define o valor da propriedade despacho12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO12(String value) {
        this.despacho12 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO13() {
        return despacho13;
    }

    /**
     * Define o valor da propriedade despacho13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO13(String value) {
        this.despacho13 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO14() {
        return despacho14;
    }

    /**
     * Define o valor da propriedade despacho14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO14(String value) {
        this.despacho14 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO15() {
        return despacho15;
    }

    /**
     * Define o valor da propriedade despacho15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO15(String value) {
        this.despacho15 = value;
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
