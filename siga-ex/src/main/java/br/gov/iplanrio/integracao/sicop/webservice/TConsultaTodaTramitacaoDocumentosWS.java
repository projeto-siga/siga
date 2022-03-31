
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
 *         &lt;element name="ORGRESP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONULT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "orgresp",
    "conult"
})
@XmlRootElement(name = "TConsultaTodaTramitacaoDocumentos_WS")
public class TConsultaTodaTramitacaoDocumentosWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "TIPDOC")
    protected String tipdoc;
    @XmlElement(name = "NUMDOC")
    protected String numdoc;
    @XmlElement(name = "ORGRESP")
    protected String orgresp;
    @XmlElement(name = "CONULT")
    protected String conult;

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
     * Obt�m o valor da propriedade orgresp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGRESP() {
        return orgresp;
    }

    /**
     * Define o valor da propriedade orgresp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGRESP(String value) {
        this.orgresp = value;
    }

    /**
     * Obt�m o valor da propriedade conult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONULT() {
        return conult;
    }

    /**
     * Define o valor da propriedade conult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONULT(String value) {
        this.conult = value;
    }

}
