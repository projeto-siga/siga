
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TramitacaoDeDocumentos_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TramitacaoDeDocumentos_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESTINO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DSCDESTINO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATREMESSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRECEBEDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATRECEBEDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="T1IPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDES14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "TramitacaoDeDocumentos_WS", propOrder = {
    "rotina",
    "opcao",
    "origem",
    "destino",
    "dscdestino",
    "datremessa",
    "guia",
    "matrecebedor",
    "datrecebedor",
    "t1IPDOC",
    "numdoc1",
    "orgdoc1",
    "coddes1",
    "tipdoc2",
    "numdoc2",
    "orgdoc2",
    "coddes2",
    "tipdoc3",
    "numdoc3",
    "orgdoc3",
    "coddes3",
    "tipdoc4",
    "numdoc4",
    "orgdoc4",
    "coddes4",
    "tipdoc5",
    "numdoc5",
    "orgdoc5",
    "coddes5",
    "tipdoc6",
    "numdoc6",
    "orgdoc6",
    "coddes6",
    "tipdoc7",
    "numdoc7",
    "orgdoc7",
    "coddes7",
    "tipdoc8",
    "numdoc8",
    "orgdoc8",
    "coddes8",
    "tipdoc9",
    "numdoc9",
    "orgdoc9",
    "coddes9",
    "tipdoc10",
    "numdoc10",
    "orgdoc10",
    "coddes10",
    "tipdoc11",
    "numdoc11",
    "orgdoc11",
    "coddes11",
    "tipdoc12",
    "numdoc12",
    "orgdoc12",
    "coddes12",
    "tipdoc13",
    "numdoc13",
    "orgdoc13",
    "coddes13",
    "tipdoc14",
    "numdoc14",
    "orgdoc14",
    "coddes14",
    "statusLine"
})
public class TramitacaoDeDocumentosWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "ORIGEM")
    protected String origem;
    @XmlElement(name = "DESTINO")
    protected String destino;
    @XmlElement(name = "DSCDESTINO")
    protected String dscdestino;
    @XmlElement(name = "DATREMESSA")
    protected String datremessa;
    @XmlElement(name = "GUIA")
    protected String guia;
    @XmlElement(name = "MATRECEBEDOR")
    protected String matrecebedor;
    @XmlElement(name = "DATRECEBEDOR")
    protected String datrecebedor;
    @XmlElement(name = "T1IPDOC")
    protected String t1IPDOC;
    @XmlElement(name = "NUMDOC1")
    protected String numdoc1;
    @XmlElement(name = "ORGDOC1")
    protected String orgdoc1;
    @XmlElement(name = "CODDES1")
    protected String coddes1;
    @XmlElement(name = "TIPDOC2")
    protected String tipdoc2;
    @XmlElement(name = "NUMDOC2")
    protected String numdoc2;
    @XmlElement(name = "ORGDOC2")
    protected String orgdoc2;
    @XmlElement(name = "CODDES2")
    protected String coddes2;
    @XmlElement(name = "TIPDOC3")
    protected String tipdoc3;
    @XmlElement(name = "NUMDOC3")
    protected String numdoc3;
    @XmlElement(name = "ORGDOC3")
    protected String orgdoc3;
    @XmlElement(name = "CODDES3")
    protected String coddes3;
    @XmlElement(name = "TIPDOC4")
    protected String tipdoc4;
    @XmlElement(name = "NUMDOC4")
    protected String numdoc4;
    @XmlElement(name = "ORGDOC4")
    protected String orgdoc4;
    @XmlElement(name = "CODDES4")
    protected String coddes4;
    @XmlElement(name = "TIPDOC5")
    protected String tipdoc5;
    @XmlElement(name = "NUMDOC5")
    protected String numdoc5;
    @XmlElement(name = "ORGDOC5")
    protected String orgdoc5;
    @XmlElement(name = "CODDES5")
    protected String coddes5;
    @XmlElement(name = "TIPDOC6")
    protected String tipdoc6;
    @XmlElement(name = "NUMDOC6")
    protected String numdoc6;
    @XmlElement(name = "ORGDOC6")
    protected String orgdoc6;
    @XmlElement(name = "CODDES6")
    protected String coddes6;
    @XmlElement(name = "TIPDOC7")
    protected String tipdoc7;
    @XmlElement(name = "NUMDOC7")
    protected String numdoc7;
    @XmlElement(name = "ORGDOC7")
    protected String orgdoc7;
    @XmlElement(name = "CODDES7")
    protected String coddes7;
    @XmlElement(name = "TIPDOC8")
    protected String tipdoc8;
    @XmlElement(name = "NUMDOC8")
    protected String numdoc8;
    @XmlElement(name = "ORGDOC8")
    protected String orgdoc8;
    @XmlElement(name = "CODDES8")
    protected String coddes8;
    @XmlElement(name = "TIPDOC9")
    protected String tipdoc9;
    @XmlElement(name = "NUMDOC9")
    protected String numdoc9;
    @XmlElement(name = "ORGDOC9")
    protected String orgdoc9;
    @XmlElement(name = "CODDES9")
    protected String coddes9;
    @XmlElement(name = "TIPDOC10")
    protected String tipdoc10;
    @XmlElement(name = "NUMDOC10")
    protected String numdoc10;
    @XmlElement(name = "ORGDOC10")
    protected String orgdoc10;
    @XmlElement(name = "CODDES10")
    protected String coddes10;
    @XmlElement(name = "TIPDOC11")
    protected String tipdoc11;
    @XmlElement(name = "NUMDOC11")
    protected String numdoc11;
    @XmlElement(name = "ORGDOC11")
    protected String orgdoc11;
    @XmlElement(name = "CODDES11")
    protected String coddes11;
    @XmlElement(name = "TIPDOC12")
    protected String tipdoc12;
    @XmlElement(name = "NUMDOC12")
    protected String numdoc12;
    @XmlElement(name = "ORGDOC12")
    protected String orgdoc12;
    @XmlElement(name = "CODDES12")
    protected String coddes12;
    @XmlElement(name = "TIPDOC13")
    protected String tipdoc13;
    @XmlElement(name = "NUMDOC13")
    protected String numdoc13;
    @XmlElement(name = "ORGDOC13")
    protected String orgdoc13;
    @XmlElement(name = "CODDES13")
    protected String coddes13;
    @XmlElement(name = "TIPDOC14")
    protected String tipdoc14;
    @XmlElement(name = "NUMDOC14")
    protected String numdoc14;
    @XmlElement(name = "ORGDOC14")
    protected String orgdoc14;
    @XmlElement(name = "CODDES14")
    protected String coddes14;
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
     * Obt�m o valor da propriedade origem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGEM() {
        return origem;
    }

    /**
     * Define o valor da propriedade origem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGEM(String value) {
        this.origem = value;
    }

    /**
     * Obt�m o valor da propriedade destino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESTINO() {
        return destino;
    }

    /**
     * Define o valor da propriedade destino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESTINO(String value) {
        this.destino = value;
    }

    /**
     * Obt�m o valor da propriedade dscdestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDSCDESTINO() {
        return dscdestino;
    }

    /**
     * Define o valor da propriedade dscdestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDSCDESTINO(String value) {
        this.dscdestino = value;
    }

    /**
     * Obt�m o valor da propriedade datremessa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATREMESSA() {
        return datremessa;
    }

    /**
     * Define o valor da propriedade datremessa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATREMESSA(String value) {
        this.datremessa = value;
    }

    /**
     * Obt�m o valor da propriedade guia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA() {
        return guia;
    }

    /**
     * Define o valor da propriedade guia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA(String value) {
        this.guia = value;
    }

    /**
     * Obt�m o valor da propriedade matrecebedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRECEBEDOR() {
        return matrecebedor;
    }

    /**
     * Define o valor da propriedade matrecebedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRECEBEDOR(String value) {
        this.matrecebedor = value;
    }

    /**
     * Obt�m o valor da propriedade datrecebedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATRECEBEDOR() {
        return datrecebedor;
    }

    /**
     * Define o valor da propriedade datrecebedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATRECEBEDOR(String value) {
        this.datrecebedor = value;
    }

    /**
     * Obt�m o valor da propriedade t1IPDOC.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getT1IPDOC() {
        return t1IPDOC;
    }

    /**
     * Define o valor da propriedade t1IPDOC.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setT1IPDOC(String value) {
        this.t1IPDOC = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC1() {
        return numdoc1;
    }

    /**
     * Define o valor da propriedade numdoc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC1(String value) {
        this.numdoc1 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC1() {
        return orgdoc1;
    }

    /**
     * Define o valor da propriedade orgdoc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC1(String value) {
        this.orgdoc1 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES1() {
        return coddes1;
    }

    /**
     * Define o valor da propriedade coddes1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES1(String value) {
        this.coddes1 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC2() {
        return tipdoc2;
    }

    /**
     * Define o valor da propriedade tipdoc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC2(String value) {
        this.tipdoc2 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC2() {
        return numdoc2;
    }

    /**
     * Define o valor da propriedade numdoc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC2(String value) {
        this.numdoc2 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC2() {
        return orgdoc2;
    }

    /**
     * Define o valor da propriedade orgdoc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC2(String value) {
        this.orgdoc2 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES2() {
        return coddes2;
    }

    /**
     * Define o valor da propriedade coddes2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES2(String value) {
        this.coddes2 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC3() {
        return tipdoc3;
    }

    /**
     * Define o valor da propriedade tipdoc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC3(String value) {
        this.tipdoc3 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC3() {
        return numdoc3;
    }

    /**
     * Define o valor da propriedade numdoc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC3(String value) {
        this.numdoc3 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC3() {
        return orgdoc3;
    }

    /**
     * Define o valor da propriedade orgdoc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC3(String value) {
        this.orgdoc3 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES3() {
        return coddes3;
    }

    /**
     * Define o valor da propriedade coddes3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES3(String value) {
        this.coddes3 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC4() {
        return tipdoc4;
    }

    /**
     * Define o valor da propriedade tipdoc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC4(String value) {
        this.tipdoc4 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC4() {
        return numdoc4;
    }

    /**
     * Define o valor da propriedade numdoc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC4(String value) {
        this.numdoc4 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC4() {
        return orgdoc4;
    }

    /**
     * Define o valor da propriedade orgdoc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC4(String value) {
        this.orgdoc4 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES4() {
        return coddes4;
    }

    /**
     * Define o valor da propriedade coddes4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES4(String value) {
        this.coddes4 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC5() {
        return tipdoc5;
    }

    /**
     * Define o valor da propriedade tipdoc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC5(String value) {
        this.tipdoc5 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC5() {
        return numdoc5;
    }

    /**
     * Define o valor da propriedade numdoc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC5(String value) {
        this.numdoc5 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC5() {
        return orgdoc5;
    }

    /**
     * Define o valor da propriedade orgdoc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC5(String value) {
        this.orgdoc5 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES5() {
        return coddes5;
    }

    /**
     * Define o valor da propriedade coddes5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES5(String value) {
        this.coddes5 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC6() {
        return tipdoc6;
    }

    /**
     * Define o valor da propriedade tipdoc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC6(String value) {
        this.tipdoc6 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC6() {
        return numdoc6;
    }

    /**
     * Define o valor da propriedade numdoc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC6(String value) {
        this.numdoc6 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC6() {
        return orgdoc6;
    }

    /**
     * Define o valor da propriedade orgdoc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC6(String value) {
        this.orgdoc6 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES6() {
        return coddes6;
    }

    /**
     * Define o valor da propriedade coddes6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES6(String value) {
        this.coddes6 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC7() {
        return tipdoc7;
    }

    /**
     * Define o valor da propriedade tipdoc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC7(String value) {
        this.tipdoc7 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC7() {
        return numdoc7;
    }

    /**
     * Define o valor da propriedade numdoc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC7(String value) {
        this.numdoc7 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC7() {
        return orgdoc7;
    }

    /**
     * Define o valor da propriedade orgdoc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC7(String value) {
        this.orgdoc7 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES7() {
        return coddes7;
    }

    /**
     * Define o valor da propriedade coddes7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES7(String value) {
        this.coddes7 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC8() {
        return tipdoc8;
    }

    /**
     * Define o valor da propriedade tipdoc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC8(String value) {
        this.tipdoc8 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC8() {
        return numdoc8;
    }

    /**
     * Define o valor da propriedade numdoc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC8(String value) {
        this.numdoc8 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC8() {
        return orgdoc8;
    }

    /**
     * Define o valor da propriedade orgdoc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC8(String value) {
        this.orgdoc8 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES8() {
        return coddes8;
    }

    /**
     * Define o valor da propriedade coddes8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES8(String value) {
        this.coddes8 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC9() {
        return tipdoc9;
    }

    /**
     * Define o valor da propriedade tipdoc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC9(String value) {
        this.tipdoc9 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC9() {
        return numdoc9;
    }

    /**
     * Define o valor da propriedade numdoc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC9(String value) {
        this.numdoc9 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC9() {
        return orgdoc9;
    }

    /**
     * Define o valor da propriedade orgdoc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC9(String value) {
        this.orgdoc9 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES9() {
        return coddes9;
    }

    /**
     * Define o valor da propriedade coddes9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES9(String value) {
        this.coddes9 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC10() {
        return tipdoc10;
    }

    /**
     * Define o valor da propriedade tipdoc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC10(String value) {
        this.tipdoc10 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC10() {
        return numdoc10;
    }

    /**
     * Define o valor da propriedade numdoc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC10(String value) {
        this.numdoc10 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC10() {
        return orgdoc10;
    }

    /**
     * Define o valor da propriedade orgdoc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC10(String value) {
        this.orgdoc10 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES10() {
        return coddes10;
    }

    /**
     * Define o valor da propriedade coddes10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES10(String value) {
        this.coddes10 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC11() {
        return tipdoc11;
    }

    /**
     * Define o valor da propriedade tipdoc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC11(String value) {
        this.tipdoc11 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC11() {
        return numdoc11;
    }

    /**
     * Define o valor da propriedade numdoc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC11(String value) {
        this.numdoc11 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC11() {
        return orgdoc11;
    }

    /**
     * Define o valor da propriedade orgdoc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC11(String value) {
        this.orgdoc11 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES11() {
        return coddes11;
    }

    /**
     * Define o valor da propriedade coddes11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES11(String value) {
        this.coddes11 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC12() {
        return tipdoc12;
    }

    /**
     * Define o valor da propriedade tipdoc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC12(String value) {
        this.tipdoc12 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC12() {
        return numdoc12;
    }

    /**
     * Define o valor da propriedade numdoc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC12(String value) {
        this.numdoc12 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC12() {
        return orgdoc12;
    }

    /**
     * Define o valor da propriedade orgdoc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC12(String value) {
        this.orgdoc12 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES12() {
        return coddes12;
    }

    /**
     * Define o valor da propriedade coddes12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES12(String value) {
        this.coddes12 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC13() {
        return tipdoc13;
    }

    /**
     * Define o valor da propriedade tipdoc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC13(String value) {
        this.tipdoc13 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC13() {
        return numdoc13;
    }

    /**
     * Define o valor da propriedade numdoc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC13(String value) {
        this.numdoc13 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC13() {
        return orgdoc13;
    }

    /**
     * Define o valor da propriedade orgdoc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC13(String value) {
        this.orgdoc13 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES13() {
        return coddes13;
    }

    /**
     * Define o valor da propriedade coddes13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES13(String value) {
        this.coddes13 = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC14() {
        return tipdoc14;
    }

    /**
     * Define o valor da propriedade tipdoc14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC14(String value) {
        this.tipdoc14 = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC14() {
        return numdoc14;
    }

    /**
     * Define o valor da propriedade numdoc14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC14(String value) {
        this.numdoc14 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC14() {
        return orgdoc14;
    }

    /**
     * Define o valor da propriedade orgdoc14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC14(String value) {
        this.orgdoc14 = value;
    }

    /**
     * Obt�m o valor da propriedade coddes14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDES14() {
        return coddes14;
    }

    /**
     * Define o valor da propriedade coddes14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDES14(String value) {
        this.coddes14 = value;
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
