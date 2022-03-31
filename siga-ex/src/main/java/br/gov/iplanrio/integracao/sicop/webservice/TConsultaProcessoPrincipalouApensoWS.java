
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
 *         &lt;element name="PROCPRINCIPAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCAPENSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONCAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "procprincipal",
    "procapenso",
    "concad"
})
@XmlRootElement(name = "TConsultaProcessoPrincipalouApenso_WS")
public class TConsultaProcessoPrincipalouApensoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "PROCPRINCIPAL")
    protected String procprincipal;
    @XmlElement(name = "PROCAPENSO")
    protected String procapenso;
    @XmlElement(name = "CONCAD")
    protected String concad;

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
     * Obt�m o valor da propriedade procprincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCPRINCIPAL() {
        return procprincipal;
    }

    /**
     * Define o valor da propriedade procprincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCPRINCIPAL(String value) {
        this.procprincipal = value;
    }

    /**
     * Obt�m o valor da propriedade procapenso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCAPENSO() {
        return procapenso;
    }

    /**
     * Define o valor da propriedade procapenso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCAPENSO(String value) {
        this.procapenso = value;
    }

    /**
     * Obt�m o valor da propriedade concad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONCAD() {
        return concad;
    }

    /**
     * Define o valor da propriedade concad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONCAD(String value) {
        this.concad = value;
    }

}
