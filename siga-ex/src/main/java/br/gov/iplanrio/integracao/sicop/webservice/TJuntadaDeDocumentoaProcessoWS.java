
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
 *         &lt;element name="NUMPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPODOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMANEXO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="QTDFOLHAS12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "numprocesso",
    "tipodoc1",
    "numdoc1",
    "orgdoc1",
    "numanexo1",
    "qtdfolhas1",
    "tipodoc2",
    "numdoc2",
    "orgdoc2",
    "numanexo2",
    "qtdfolhas2",
    "tipodoc3",
    "numdoc3",
    "orgdoc3",
    "numanexo3",
    "qtdfolhas3",
    "tipodoc4",
    "numdoc4",
    "orgdoc4",
    "numanexo4",
    "qtdfolhas4",
    "tipodoc5",
    "numdoc5",
    "orgdoc5",
    "numanexo5",
    "qtdfolhas5",
    "tipodoc6",
    "numdoc6",
    "orgdoc6",
    "numanexo6",
    "qtdfolhas6",
    "tipodoc7",
    "numdoc7",
    "orgdoc7",
    "numanexo7",
    "qtdfolhas7",
    "tipodoc8",
    "numdoc8",
    "orgdoc8",
    "numanexo8",
    "qtdfolhas8",
    "tipodoc9",
    "numdoc9",
    "orgdoc9",
    "numanexo9",
    "qtdfolhas9",
    "tipodoc10",
    "numdoc10",
    "orgdoc10",
    "numanexo10",
    "qtdfolhas10",
    "tipodoc11",
    "numdoc11",
    "orgdoc11",
    "numanexo11",
    "qtdfolhas11",
    "tipodoc12",
    "numdoc12",
    "orgdoc12",
    "numanexo12",
    "qtdfolhas12",
    "senha",
    "indconfirma"
})
@XmlRootElement(name = "TJuntadaDeDocumentoaProcesso_WS")
public class TJuntadaDeDocumentoaProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROCESSO")
    protected String numprocesso;
    @XmlElement(name = "TIPODOC1")
    protected String tipodoc1;
    @XmlElement(name = "NUMDOC1")
    protected String numdoc1;
    @XmlElement(name = "ORGDOC1")
    protected String orgdoc1;
    @XmlElement(name = "NUMANEXO1")
    protected String numanexo1;
    @XmlElement(name = "QTDFOLHAS1")
    protected String qtdfolhas1;
    @XmlElement(name = "TIPODOC2")
    protected String tipodoc2;
    @XmlElement(name = "NUMDOC2")
    protected String numdoc2;
    @XmlElement(name = "ORGDOC2")
    protected String orgdoc2;
    @XmlElement(name = "NUMANEXO2")
    protected String numanexo2;
    @XmlElement(name = "QTDFOLHAS2")
    protected String qtdfolhas2;
    @XmlElement(name = "TIPODOC3")
    protected String tipodoc3;
    @XmlElement(name = "NUMDOC3")
    protected String numdoc3;
    @XmlElement(name = "ORGDOC3")
    protected String orgdoc3;
    @XmlElement(name = "NUMANEXO3")
    protected String numanexo3;
    @XmlElement(name = "QTDFOLHAS3")
    protected String qtdfolhas3;
    @XmlElement(name = "TIPODOC4")
    protected String tipodoc4;
    @XmlElement(name = "NUMDOC4")
    protected String numdoc4;
    @XmlElement(name = "ORGDOC4")
    protected String orgdoc4;
    @XmlElement(name = "NUMANEXO4")
    protected String numanexo4;
    @XmlElement(name = "QTDFOLHAS4")
    protected String qtdfolhas4;
    @XmlElement(name = "TIPODOC5")
    protected String tipodoc5;
    @XmlElement(name = "NUMDOC5")
    protected String numdoc5;
    @XmlElement(name = "ORGDOC5")
    protected String orgdoc5;
    @XmlElement(name = "NUMANEXO5")
    protected String numanexo5;
    @XmlElement(name = "QTDFOLHAS5")
    protected String qtdfolhas5;
    @XmlElement(name = "TIPODOC6")
    protected String tipodoc6;
    @XmlElement(name = "NUMDOC6")
    protected String numdoc6;
    @XmlElement(name = "ORGDOC6")
    protected String orgdoc6;
    @XmlElement(name = "NUMANEXO6")
    protected String numanexo6;
    @XmlElement(name = "QTDFOLHAS6")
    protected String qtdfolhas6;
    @XmlElement(name = "TIPODOC7")
    protected String tipodoc7;
    @XmlElement(name = "NUMDOC7")
    protected String numdoc7;
    @XmlElement(name = "ORGDOC7")
    protected String orgdoc7;
    @XmlElement(name = "NUMANEXO7")
    protected String numanexo7;
    @XmlElement(name = "QTDFOLHAS7")
    protected String qtdfolhas7;
    @XmlElement(name = "TIPODOC8")
    protected String tipodoc8;
    @XmlElement(name = "NUMDOC8")
    protected String numdoc8;
    @XmlElement(name = "ORGDOC8")
    protected String orgdoc8;
    @XmlElement(name = "NUMANEXO8")
    protected String numanexo8;
    @XmlElement(name = "QTDFOLHAS8")
    protected String qtdfolhas8;
    @XmlElement(name = "TIPODOC9")
    protected String tipodoc9;
    @XmlElement(name = "NUMDOC9")
    protected String numdoc9;
    @XmlElement(name = "ORGDOC9")
    protected String orgdoc9;
    @XmlElement(name = "NUMANEXO9")
    protected String numanexo9;
    @XmlElement(name = "QTDFOLHAS9")
    protected String qtdfolhas9;
    @XmlElement(name = "TIPODOC10")
    protected String tipodoc10;
    @XmlElement(name = "NUMDOC10")
    protected String numdoc10;
    @XmlElement(name = "ORGDOC10")
    protected String orgdoc10;
    @XmlElement(name = "NUMANEXO10")
    protected String numanexo10;
    @XmlElement(name = "QTDFOLHAS10")
    protected String qtdfolhas10;
    @XmlElement(name = "TIPODOC11")
    protected String tipodoc11;
    @XmlElement(name = "NUMDOC11")
    protected String numdoc11;
    @XmlElement(name = "ORGDOC11")
    protected String orgdoc11;
    @XmlElement(name = "NUMANEXO11")
    protected String numanexo11;
    @XmlElement(name = "QTDFOLHAS11")
    protected String qtdfolhas11;
    @XmlElement(name = "TIPODOC12")
    protected String tipodoc12;
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
     * Obt�m o valor da propriedade tipodoc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC1() {
        return tipodoc1;
    }

    /**
     * Define o valor da propriedade tipodoc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC1(String value) {
        this.tipodoc1 = value;
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
     * Obt�m o valor da propriedade tipodoc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC2() {
        return tipodoc2;
    }

    /**
     * Define o valor da propriedade tipodoc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC2(String value) {
        this.tipodoc2 = value;
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
     * Obt�m o valor da propriedade tipodoc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC3() {
        return tipodoc3;
    }

    /**
     * Define o valor da propriedade tipodoc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC3(String value) {
        this.tipodoc3 = value;
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
     * Obt�m o valor da propriedade tipodoc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC4() {
        return tipodoc4;
    }

    /**
     * Define o valor da propriedade tipodoc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC4(String value) {
        this.tipodoc4 = value;
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
     * Obt�m o valor da propriedade tipodoc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC5() {
        return tipodoc5;
    }

    /**
     * Define o valor da propriedade tipodoc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC5(String value) {
        this.tipodoc5 = value;
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
     * Obt�m o valor da propriedade tipodoc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC6() {
        return tipodoc6;
    }

    /**
     * Define o valor da propriedade tipodoc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC6(String value) {
        this.tipodoc6 = value;
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
     * Obt�m o valor da propriedade tipodoc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC7() {
        return tipodoc7;
    }

    /**
     * Define o valor da propriedade tipodoc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC7(String value) {
        this.tipodoc7 = value;
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
     * Obt�m o valor da propriedade tipodoc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC8() {
        return tipodoc8;
    }

    /**
     * Define o valor da propriedade tipodoc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC8(String value) {
        this.tipodoc8 = value;
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
     * Obt�m o valor da propriedade tipodoc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC9() {
        return tipodoc9;
    }

    /**
     * Define o valor da propriedade tipodoc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC9(String value) {
        this.tipodoc9 = value;
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
     * Obt�m o valor da propriedade tipodoc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC10() {
        return tipodoc10;
    }

    /**
     * Define o valor da propriedade tipodoc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC10(String value) {
        this.tipodoc10 = value;
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
     * Obt�m o valor da propriedade tipodoc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC11() {
        return tipodoc11;
    }

    /**
     * Define o valor da propriedade tipodoc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC11(String value) {
        this.tipodoc11 = value;
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
     * Obt�m o valor da propriedade tipodoc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPODOC12() {
        return tipodoc12;
    }

    /**
     * Define o valor da propriedade tipodoc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPODOC12(String value) {
        this.tipodoc12 = value;
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
