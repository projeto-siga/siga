
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
 *         &lt;element name="TConsultaProcesso_WSResult" type="{http://pcrj/Sma/Sicop/Grava}Message" minOccurs="0"/>
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
    "tConsultaProcessoWSResult"
})
@XmlRootElement(name = "TConsultaProcesso_WSResponse")
public class TConsultaProcessoWSResponse {

    @XmlElement(name = "TConsultaProcesso_WSResult")
    protected Message tConsultaProcessoWSResult;

    /**
     * Obt�m o valor da propriedade tConsultaProcessoWSResult.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getTConsultaProcessoWSResult() {
        return tConsultaProcessoWSResult;
    }

    /**
     * Define o valor da propriedade tConsultaProcessoWSResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setTConsultaProcessoWSResult(Message value) {
        this.tConsultaProcessoWSResult = value;
    }

}
