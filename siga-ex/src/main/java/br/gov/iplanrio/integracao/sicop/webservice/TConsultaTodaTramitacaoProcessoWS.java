
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
 *         &lt;element name="NUMPROC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "numproc",
    "concad"
})
@XmlRootElement(name = "TConsultaTodaTramitacaoProcesso_WS")
public class TConsultaTodaTramitacaoProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "NUMPROC")
    protected String numproc;
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
     * Obt�m o valor da propriedade numproc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC() {
        return numproc;
    }

    /**
     * Define o valor da propriedade numproc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC(String value) {
        this.numproc = value;
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
