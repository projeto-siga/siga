
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de JuntadaDeDocumentoaProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="JuntadaDeDocumentoaProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "JuntadaDeDocumentoaProcesso_WS", propOrder = {
    "rotina",
    "opcao",
    "numprocesso",
    "tipdoc1",
    "numdoc1",
    "orgdoc1",
    "numanexo1",
    "qtdfolhas1",
    "tipdoc2",
    "numdoc2",
    "orgdoc2",
    "numanexo2",
    "qtdfolhas2",
    "tipdoc3",
    "numdoc3",
    "orgdoc3",
    "numanexo3",
    "qtdfolhas3",
    "tipdoc4",
    "numdoc4",
    "orgdoc4",
    "numanexo4",
    "qtdfolhas4",
    "tipdoc5",
    "numdoc5",
    "orgdoc5",
    "numanexo5",
    "qtdfolhas5",
    "tipdoc6",
    "numdoc6",
    "orgdoc6",
    "numanexo6",
    "qtdfolhas6",
    "tipdoc7",
    "numdoc7",
    "orgdoc7",
    "numanexo7",
    "qtdfolhas7",
    "tipdoc8",
    "numdoc8",
    "orgdoc8",
    "numanexo8",
    "qtdfolhas8",
    "tipdoc9",
    "numdoc9",
    "orgdoc9",
    "numanexo9",
    "qtdfolhas9",
    "tipdoc10",
    "numdoc10",
    "orgdoc10",
    "numanexo10",
    "qtdfolhas10",
    "tipdoc11",
    "numdoc11",
    "orgdoc11",
    "numanexo11",
    "qtdfolhas11",
    "tipdoc12",
    "numdoc12",
    "orgdoc12",
    "numanexo12",
    "qtdfolhas12",
    "senha",
    "lblconfirma",
    "indconfirma",
    "statusLine"
})
public class JuntadaDeDocumentoaProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROCESSO")
    protected String numprocesso;
    @XmlElement(name = "TIPDOC1")
    protected String tipdoc1;
    @XmlElement(name = "NUMDOC1")
    protected String numdoc1;
    @XmlElement(name = "ORGDOC1")
    protected String orgdoc1;
    @XmlElement(name = "NUMANEXO1")
    protected String numanexo1;
    @XmlElement(name = "QTDFOLHAS1")
    protected String qtdfolhas1;
    @XmlElement(name = "TIPDOC2")
    protected String tipdoc2;
    @XmlElement(name = "NUMDOC2")
    protected String numdoc2;
    @XmlElement(name = "ORGDOC2")
    protected String orgdoc2;
    @XmlElement(name = "NUMANEXO2")
    protected String numanexo2;
    @XmlElement(name = "QTDFOLHAS2")
    protected String qtdfolhas2;
    @XmlElement(name = "TIPDOC3")
    protected String tipdoc3;
    @XmlElement(name = "NUMDOC3")
    protected String numdoc3;
    @XmlElement(name = "ORGDOC3")
    protected String orgdoc3;
    @XmlElement(name = "NUMANEXO3")
    protected String numanexo3;
    @XmlElement(name = "QTDFOLHAS3")
    protected String qtdfolhas3;
    @XmlElement(name = "TIPDOC4")
    protected String tipdoc4;
    @XmlElement(name = "NUMDOC4")
    protected String numdoc4;
    @XmlElement(name = "ORGDOC4")
    protected String orgdoc4;
    @XmlElement(name = "NUMANEXO4")
    protected String numanexo4;
    @XmlElement(name = "QTDFOLHAS4")
    protected String qtdfolhas4;
    @XmlElement(name = "TIPDOC5")
    protected String tipdoc5;
    @XmlElement(name = "NUMDOC5")
    protected String numdoc5;
    @XmlElement(name = "ORGDOC5")
    protected String orgdoc5;
    @XmlElement(name = "NUMANEXO5")
    protected String numanexo5;
    @XmlElement(name = "QTDFOLHAS5")
    protected String qtdfolhas5;
    @XmlElement(name = "TIPDOC6")
    protected String tipdoc6;
    @XmlElement(name = "NUMDOC6")
    protected String numdoc6;
    @XmlElement(name = "ORGDOC6")
    protected String orgdoc6;
    @XmlElement(name = "NUMANEXO6")
    protected String numanexo6;
    @XmlElement(name = "QTDFOLHAS6")
    protected String qtdfolhas6;
    @XmlElement(name = "TIPDOC7")
    protected String tipdoc7;
    @XmlElement(name = "NUMDOC7")
    protected String numdoc7;
    @XmlElement(name = "ORGDOC7")
    protected String orgdoc7;
    @XmlElement(name = "NUMANEXO7")
    protected String numanexo7;
    @XmlElement(name = "QTDFOLHAS7")
    protected String qtdfolhas7;
    @XmlElement(name = "TIPDOC8")
    protected String tipdoc8;
    @XmlElement(name = "NUMDOC8")
    protected String numdoc8;
    @XmlElement(name = "ORGDOC8")
    protected String orgdoc8;
    @XmlElement(name = "NUMANEXO8")
    protected String numanexo8;
    @XmlElement(name = "QTDFOLHAS8")
    protected String qtdfolhas8;
    @XmlElement(name = "TIPDOC9")
    protected String tipdoc9;
    @XmlElement(name = "NUMDOC9")
    protected String numdoc9;
    @XmlElement(name = "ORGDOC9")
    protected String orgdoc9;
    @XmlElement(name = "NUMANEXO9")
    protected String numanexo9;
    @XmlElement(name = "QTDFOLHAS9")
    protected String qtdfolhas9;
    @XmlElement(name = "TIPDOC10")
    protected String tipdoc10;
    @XmlElement(name = "NUMDOC10")
    protected String numdoc10;
    @XmlElement(name = "ORGDOC10")
    protected String orgdoc10;
    @XmlElement(name = "NUMANEXO10")
    protected String numanexo10;
    @XmlElement(name = "QTDFOLHAS10")
    protected String qtdfolhas10;
    @XmlElement(name = "TIPDOC11")
    protected String tipdoc11;
    @XmlElement(name = "NUMDOC11")
    protected String numdoc11;
    @XmlElement(name = "ORGDOC11")
    protected String orgdoc11;
    @XmlElement(name = "NUMANEXO11")
    protected String numanexo11;
    @XmlElement(name = "QTDFOLHAS11")
    protected String qtdfolhas11;
    @XmlElement(name = "TIPDOC12")
    protected String tipdoc12;
    @XmlElement(name = "NUMDOC12")
    protected String numdoc12;
    @XmlElement(name = "ORGDOC12")
    protected String orgdoc12;
    @XmlElement(name = "NUMANEXO12")
    protected String numanexo12;
    @XmlElement(name = "QTDFOLHAS12")
    protected String qtdfolhas12;
    @XmlElement(name = "SENHA")
    protected String senha;
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
     * Obt�m o valor da propriedade tipdoc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC1() {
        return tipdoc1;
    }

    /**
     * Define o valor da propriedade tipdoc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC1(String value) {
        this.tipdoc1 = value;
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
     * Obt�m o valor da propriedade numanexo1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO1() {
        return numanexo1;
    }

    /**
     * Define o valor da propriedade numanexo1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO1(String value) {
        this.numanexo1 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS1() {
        return qtdfolhas1;
    }

    /**
     * Define o valor da propriedade qtdfolhas1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS1(String value) {
        this.qtdfolhas1 = value;
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
     * Obt�m o valor da propriedade numanexo2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO2() {
        return numanexo2;
    }

    /**
     * Define o valor da propriedade numanexo2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO2(String value) {
        this.numanexo2 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS2() {
        return qtdfolhas2;
    }

    /**
     * Define o valor da propriedade qtdfolhas2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS2(String value) {
        this.qtdfolhas2 = value;
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
     * Obt�m o valor da propriedade numanexo3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO3() {
        return numanexo3;
    }

    /**
     * Define o valor da propriedade numanexo3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO3(String value) {
        this.numanexo3 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS3() {
        return qtdfolhas3;
    }

    /**
     * Define o valor da propriedade qtdfolhas3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS3(String value) {
        this.qtdfolhas3 = value;
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
     * Obt�m o valor da propriedade numanexo4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO4() {
        return numanexo4;
    }

    /**
     * Define o valor da propriedade numanexo4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO4(String value) {
        this.numanexo4 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS4() {
        return qtdfolhas4;
    }

    /**
     * Define o valor da propriedade qtdfolhas4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS4(String value) {
        this.qtdfolhas4 = value;
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
     * Obt�m o valor da propriedade numanexo5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO5() {
        return numanexo5;
    }

    /**
     * Define o valor da propriedade numanexo5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO5(String value) {
        this.numanexo5 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS5() {
        return qtdfolhas5;
    }

    /**
     * Define o valor da propriedade qtdfolhas5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS5(String value) {
        this.qtdfolhas5 = value;
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
     * Obt�m o valor da propriedade numanexo6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO6() {
        return numanexo6;
    }

    /**
     * Define o valor da propriedade numanexo6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO6(String value) {
        this.numanexo6 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS6() {
        return qtdfolhas6;
    }

    /**
     * Define o valor da propriedade qtdfolhas6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS6(String value) {
        this.qtdfolhas6 = value;
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
     * Obt�m o valor da propriedade numanexo7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO7() {
        return numanexo7;
    }

    /**
     * Define o valor da propriedade numanexo7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO7(String value) {
        this.numanexo7 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS7() {
        return qtdfolhas7;
    }

    /**
     * Define o valor da propriedade qtdfolhas7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS7(String value) {
        this.qtdfolhas7 = value;
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
     * Obt�m o valor da propriedade numanexo8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO8() {
        return numanexo8;
    }

    /**
     * Define o valor da propriedade numanexo8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO8(String value) {
        this.numanexo8 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS8() {
        return qtdfolhas8;
    }

    /**
     * Define o valor da propriedade qtdfolhas8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS8(String value) {
        this.qtdfolhas8 = value;
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
     * Obt�m o valor da propriedade numanexo9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO9() {
        return numanexo9;
    }

    /**
     * Define o valor da propriedade numanexo9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO9(String value) {
        this.numanexo9 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS9() {
        return qtdfolhas9;
    }

    /**
     * Define o valor da propriedade qtdfolhas9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS9(String value) {
        this.qtdfolhas9 = value;
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
     * Obt�m o valor da propriedade numanexo10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO10() {
        return numanexo10;
    }

    /**
     * Define o valor da propriedade numanexo10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO10(String value) {
        this.numanexo10 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS10() {
        return qtdfolhas10;
    }

    /**
     * Define o valor da propriedade qtdfolhas10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS10(String value) {
        this.qtdfolhas10 = value;
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
     * Obt�m o valor da propriedade numanexo11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO11() {
        return numanexo11;
    }

    /**
     * Define o valor da propriedade numanexo11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO11(String value) {
        this.numanexo11 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS11() {
        return qtdfolhas11;
    }

    /**
     * Define o valor da propriedade qtdfolhas11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS11(String value) {
        this.qtdfolhas11 = value;
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
     * Obt�m o valor da propriedade numanexo12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMANEXO12() {
        return numanexo12;
    }

    /**
     * Define o valor da propriedade numanexo12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMANEXO12(String value) {
        this.numanexo12 = value;
    }

    /**
     * Obt�m o valor da propriedade qtdfolhas12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQTDFOLHAS12() {
        return qtdfolhas12;
    }

    /**
     * Define o valor da propriedade qtdfolhas12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQTDFOLHAS12(String value) {
        this.qtdfolhas12 = value;
    }

    /**
     * Obt�m o valor da propriedade senha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSENHA() {
        return senha;
    }

    /**
     * Define o valor da propriedade senha.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSENHA(String value) {
        this.senha = value;
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
