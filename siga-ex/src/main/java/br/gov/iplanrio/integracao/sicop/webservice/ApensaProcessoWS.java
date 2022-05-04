
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ApensaProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ApensaProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCESSOPRINCIPAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGORIGEM13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTSOLIC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRESP13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LBLCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ApensaProcesso_WS", propOrder = {
    "rotina",
    "opcao",
    "processoprincipal",
    "numproc1",
    "orgorigem1",
    "dtsolic1",
    "matresp1",
    "numproc2",
    "orgorigem2",
    "dtsolic2",
    "matresp2",
    "numproc3",
    "orgorigem3",
    "dtsolic3",
    "matresp3",
    "numproc4",
    "orgorigem4",
    "dtsolic4",
    "matresp4",
    "numproc5",
    "orgorigem5",
    "dtsolic5",
    "matresp5",
    "numproc6",
    "orgorigem6",
    "dtsolic6",
    "matresp6",
    "numproc7",
    "orgorigem7",
    "dtsolic7",
    "matresp7",
    "numproc8",
    "orgorigem8",
    "dtsolic8",
    "matresp8",
    "numproc9",
    "orgorigem9",
    "dtsolic9",
    "matresp9",
    "numproc10",
    "orgorigem10",
    "dtsolic10",
    "matresp10",
    "numproc11",
    "orgorigem11",
    "dtsolic11",
    "matresp11",
    "numproc12",
    "orgorigem12",
    "dtsolic12",
    "matresp12",
    "numproc13",
    "orgorigem13",
    "dtsolic13",
    "matresp13",
    "lblconfirma",
    "confirma",
    "statusLine"
})
public class ApensaProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "PROCESSOPRINCIPAL")
    protected String processoprincipal;
    @XmlElement(name = "NUMPROC1")
    protected String numproc1;
    @XmlElement(name = "ORGORIGEM1")
    protected String orgorigem1;
    @XmlElement(name = "DTSOLIC1")
    protected String dtsolic1;
    @XmlElement(name = "MATRESP1")
    protected String matresp1;
    @XmlElement(name = "NUMPROC2")
    protected String numproc2;
    @XmlElement(name = "ORGORIGEM2")
    protected String orgorigem2;
    @XmlElement(name = "DTSOLIC2")
    protected String dtsolic2;
    @XmlElement(name = "MATRESP2")
    protected String matresp2;
    @XmlElement(name = "NUMPROC3")
    protected String numproc3;
    @XmlElement(name = "ORGORIGEM3")
    protected String orgorigem3;
    @XmlElement(name = "DTSOLIC3")
    protected String dtsolic3;
    @XmlElement(name = "MATRESP3")
    protected String matresp3;
    @XmlElement(name = "NUMPROC4")
    protected String numproc4;
    @XmlElement(name = "ORGORIGEM4")
    protected String orgorigem4;
    @XmlElement(name = "DTSOLIC4")
    protected String dtsolic4;
    @XmlElement(name = "MATRESP4")
    protected String matresp4;
    @XmlElement(name = "NUMPROC5")
    protected String numproc5;
    @XmlElement(name = "ORGORIGEM5")
    protected String orgorigem5;
    @XmlElement(name = "DTSOLIC5")
    protected String dtsolic5;
    @XmlElement(name = "MATRESP5")
    protected String matresp5;
    @XmlElement(name = "NUMPROC6")
    protected String numproc6;
    @XmlElement(name = "ORGORIGEM6")
    protected String orgorigem6;
    @XmlElement(name = "DTSOLIC6")
    protected String dtsolic6;
    @XmlElement(name = "MATRESP6")
    protected String matresp6;
    @XmlElement(name = "NUMPROC7")
    protected String numproc7;
    @XmlElement(name = "ORGORIGEM7")
    protected String orgorigem7;
    @XmlElement(name = "DTSOLIC7")
    protected String dtsolic7;
    @XmlElement(name = "MATRESP7")
    protected String matresp7;
    @XmlElement(name = "NUMPROC8")
    protected String numproc8;
    @XmlElement(name = "ORGORIGEM8")
    protected String orgorigem8;
    @XmlElement(name = "DTSOLIC8")
    protected String dtsolic8;
    @XmlElement(name = "MATRESP8")
    protected String matresp8;
    @XmlElement(name = "NUMPROC9")
    protected String numproc9;
    @XmlElement(name = "ORGORIGEM9")
    protected String orgorigem9;
    @XmlElement(name = "DTSOLIC9")
    protected String dtsolic9;
    @XmlElement(name = "MATRESP9")
    protected String matresp9;
    @XmlElement(name = "NUMPROC10")
    protected String numproc10;
    @XmlElement(name = "ORGORIGEM10")
    protected String orgorigem10;
    @XmlElement(name = "DTSOLIC10")
    protected String dtsolic10;
    @XmlElement(name = "MATRESP10")
    protected String matresp10;
    @XmlElement(name = "NUMPROC11")
    protected String numproc11;
    @XmlElement(name = "ORGORIGEM11")
    protected String orgorigem11;
    @XmlElement(name = "DTSOLIC11")
    protected String dtsolic11;
    @XmlElement(name = "MATRESP11")
    protected String matresp11;
    @XmlElement(name = "NUMPROC12")
    protected String numproc12;
    @XmlElement(name = "ORGORIGEM12")
    protected String orgorigem12;
    @XmlElement(name = "DTSOLIC12")
    protected String dtsolic12;
    @XmlElement(name = "MATRESP12")
    protected String matresp12;
    @XmlElement(name = "NUMPROC13")
    protected String numproc13;
    @XmlElement(name = "ORGORIGEM13")
    protected String orgorigem13;
    @XmlElement(name = "DTSOLIC13")
    protected String dtsolic13;
    @XmlElement(name = "MATRESP13")
    protected String matresp13;
    @XmlElement(name = "LBLCONFIRMA")
    protected String lblconfirma;
    @XmlElement(name = "CONFIRMA")
    protected String confirma;
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
     * Obt�m o valor da propriedade orgorigem1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM1() {
        return orgorigem1;
    }

    /**
     * Define o valor da propriedade orgorigem1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM1(String value) {
        this.orgorigem1 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC1() {
        return dtsolic1;
    }

    /**
     * Define o valor da propriedade dtsolic1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC1(String value) {
        this.dtsolic1 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP1() {
        return matresp1;
    }

    /**
     * Define o valor da propriedade matresp1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP1(String value) {
        this.matresp1 = value;
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
     * Obt�m o valor da propriedade orgorigem2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM2() {
        return orgorigem2;
    }

    /**
     * Define o valor da propriedade orgorigem2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM2(String value) {
        this.orgorigem2 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC2() {
        return dtsolic2;
    }

    /**
     * Define o valor da propriedade dtsolic2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC2(String value) {
        this.dtsolic2 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP2() {
        return matresp2;
    }

    /**
     * Define o valor da propriedade matresp2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP2(String value) {
        this.matresp2 = value;
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
     * Obt�m o valor da propriedade orgorigem3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM3() {
        return orgorigem3;
    }

    /**
     * Define o valor da propriedade orgorigem3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM3(String value) {
        this.orgorigem3 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC3() {
        return dtsolic3;
    }

    /**
     * Define o valor da propriedade dtsolic3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC3(String value) {
        this.dtsolic3 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP3() {
        return matresp3;
    }

    /**
     * Define o valor da propriedade matresp3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP3(String value) {
        this.matresp3 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC4() {
        return numproc4;
    }

    /**
     * Define o valor da propriedade numproc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC4(String value) {
        this.numproc4 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM4() {
        return orgorigem4;
    }

    /**
     * Define o valor da propriedade orgorigem4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM4(String value) {
        this.orgorigem4 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC4() {
        return dtsolic4;
    }

    /**
     * Define o valor da propriedade dtsolic4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC4(String value) {
        this.dtsolic4 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP4() {
        return matresp4;
    }

    /**
     * Define o valor da propriedade matresp4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP4(String value) {
        this.matresp4 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC5() {
        return numproc5;
    }

    /**
     * Define o valor da propriedade numproc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC5(String value) {
        this.numproc5 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM5() {
        return orgorigem5;
    }

    /**
     * Define o valor da propriedade orgorigem5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM5(String value) {
        this.orgorigem5 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC5() {
        return dtsolic5;
    }

    /**
     * Define o valor da propriedade dtsolic5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC5(String value) {
        this.dtsolic5 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP5() {
        return matresp5;
    }

    /**
     * Define o valor da propriedade matresp5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP5(String value) {
        this.matresp5 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC6() {
        return numproc6;
    }

    /**
     * Define o valor da propriedade numproc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC6(String value) {
        this.numproc6 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM6() {
        return orgorigem6;
    }

    /**
     * Define o valor da propriedade orgorigem6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM6(String value) {
        this.orgorigem6 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC6() {
        return dtsolic6;
    }

    /**
     * Define o valor da propriedade dtsolic6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC6(String value) {
        this.dtsolic6 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP6() {
        return matresp6;
    }

    /**
     * Define o valor da propriedade matresp6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP6(String value) {
        this.matresp6 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC7() {
        return numproc7;
    }

    /**
     * Define o valor da propriedade numproc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC7(String value) {
        this.numproc7 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM7() {
        return orgorigem7;
    }

    /**
     * Define o valor da propriedade orgorigem7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM7(String value) {
        this.orgorigem7 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC7() {
        return dtsolic7;
    }

    /**
     * Define o valor da propriedade dtsolic7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC7(String value) {
        this.dtsolic7 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP7() {
        return matresp7;
    }

    /**
     * Define o valor da propriedade matresp7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP7(String value) {
        this.matresp7 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC8() {
        return numproc8;
    }

    /**
     * Define o valor da propriedade numproc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC8(String value) {
        this.numproc8 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM8() {
        return orgorigem8;
    }

    /**
     * Define o valor da propriedade orgorigem8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM8(String value) {
        this.orgorigem8 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC8() {
        return dtsolic8;
    }

    /**
     * Define o valor da propriedade dtsolic8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC8(String value) {
        this.dtsolic8 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP8() {
        return matresp8;
    }

    /**
     * Define o valor da propriedade matresp8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP8(String value) {
        this.matresp8 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC9() {
        return numproc9;
    }

    /**
     * Define o valor da propriedade numproc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC9(String value) {
        this.numproc9 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM9() {
        return orgorigem9;
    }

    /**
     * Define o valor da propriedade orgorigem9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM9(String value) {
        this.orgorigem9 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC9() {
        return dtsolic9;
    }

    /**
     * Define o valor da propriedade dtsolic9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC9(String value) {
        this.dtsolic9 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP9() {
        return matresp9;
    }

    /**
     * Define o valor da propriedade matresp9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP9(String value) {
        this.matresp9 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC10() {
        return numproc10;
    }

    /**
     * Define o valor da propriedade numproc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC10(String value) {
        this.numproc10 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM10() {
        return orgorigem10;
    }

    /**
     * Define o valor da propriedade orgorigem10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM10(String value) {
        this.orgorigem10 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC10() {
        return dtsolic10;
    }

    /**
     * Define o valor da propriedade dtsolic10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC10(String value) {
        this.dtsolic10 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP10() {
        return matresp10;
    }

    /**
     * Define o valor da propriedade matresp10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP10(String value) {
        this.matresp10 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC11() {
        return numproc11;
    }

    /**
     * Define o valor da propriedade numproc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC11(String value) {
        this.numproc11 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM11() {
        return orgorigem11;
    }

    /**
     * Define o valor da propriedade orgorigem11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM11(String value) {
        this.orgorigem11 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC11() {
        return dtsolic11;
    }

    /**
     * Define o valor da propriedade dtsolic11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC11(String value) {
        this.dtsolic11 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP11() {
        return matresp11;
    }

    /**
     * Define o valor da propriedade matresp11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP11(String value) {
        this.matresp11 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC12() {
        return numproc12;
    }

    /**
     * Define o valor da propriedade numproc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC12(String value) {
        this.numproc12 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM12() {
        return orgorigem12;
    }

    /**
     * Define o valor da propriedade orgorigem12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM12(String value) {
        this.orgorigem12 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC12() {
        return dtsolic12;
    }

    /**
     * Define o valor da propriedade dtsolic12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC12(String value) {
        this.dtsolic12 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP12() {
        return matresp12;
    }

    /**
     * Define o valor da propriedade matresp12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP12(String value) {
        this.matresp12 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC13() {
        return numproc13;
    }

    /**
     * Define o valor da propriedade numproc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC13(String value) {
        this.numproc13 = value;
    }

    /**
     * Obt�m o valor da propriedade orgorigem13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGORIGEM13() {
        return orgorigem13;
    }

    /**
     * Define o valor da propriedade orgorigem13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGORIGEM13(String value) {
        this.orgorigem13 = value;
    }

    /**
     * Obt�m o valor da propriedade dtsolic13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTSOLIC13() {
        return dtsolic13;
    }

    /**
     * Define o valor da propriedade dtsolic13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTSOLIC13(String value) {
        this.dtsolic13 = value;
    }

    /**
     * Obt�m o valor da propriedade matresp13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRESP13() {
        return matresp13;
    }

    /**
     * Define o valor da propriedade matresp13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRESP13(String value) {
        this.matresp13 = value;
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
     * Obt�m o valor da propriedade confirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONFIRMA() {
        return confirma;
    }

    /**
     * Define o valor da propriedade confirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONFIRMA(String value) {
        this.confirma = value;
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
